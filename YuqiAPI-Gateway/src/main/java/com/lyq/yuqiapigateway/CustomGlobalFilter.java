package com.lyq.yuqiapigateway;

import com.lyq.yuqiapiclientsdk.utils.SignUtils;
import com.lyq.yuqiapicommon.model.entity.InterfaceInfo;
import com.lyq.yuqiapicommon.model.entity.User;
import com.lyq.yuqiapicommon.service.InnerInterfaceInfoService;
import com.lyq.yuqiapicommon.service.InnerUserInterfaceInfoService;
import com.lyq.yuqiapicommon.service.InnerUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 全局过滤
 * @author lyq
 */
@Slf4j
@Component
public class CustomGlobalFilter implements GlobalFilter, Ordered {
    @DubboReference
    private InnerUserService innerUserService;

    @DubboReference
    private InnerInterfaceInfoService innerInterfaceInfoService;

    @DubboReference
    private InnerUserInterfaceInfoService innerUserInterfaceInfoService;

    private static final List<String> IP_WHITE_LIST = Arrays.asList("127.0.0.1");

    private static final String INTERFACE_HOST = "http://localhost:8111";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 1.请求日志
        ServerHttpRequest request = exchange.getRequest();

        String path = INTERFACE_HOST + request.getPath().value();
        String method = request.getMethod().toString();
        log.info("请求唯一标识：{}", request.getId());
        log.info("请求路径：" + path);
        log.info("请求方法：" + method);
        log.info("请求参数：{}", request.getQueryParams());

        String sourceAddress = request.getLocalAddress().getHostString();
        log.info("请求来源地址：{}", sourceAddress);
        log.info("请求来源地址：{}", request.getRemoteAddress());

        // 2.访问控制-黑白名单
        ServerHttpResponse response = exchange.getResponse();
        if (!IP_WHITE_LIST.contains(sourceAddress)) {
            return handleNoAuth(response);
        }

        // 3.用户鉴权（判断AK、SK是否合法）
        HttpHeaders headers = request.getHeaders();
        String accessKey = headers.getFirst("accessKey");
        String nonce = headers.getFirst("nonce");
        String timestamp = headers.getFirst("timestamp");
        String sign = headers.getFirst("sign");
        String body = headers.getFirst("body");
        // todo 实际情况应该是去数据库中查是否已分配给用户
        User invokeUser = null;
        try {
            //调用内部服务，根据密钥获取用户信息
            invokeUser = innerUserService.getInvokeUser(accessKey);
        } catch (Exception e) {
            log.error("getInvokeUser error", e);
        }
        if (invokeUser == null) {
            // 如果用户为空，认为未授权并返回403
            return handleNoAuth(response);
        }

        // 如果随机值超过10000，则报异常
        if (Long.parseLong(nonce) > 10000) {
            throw new RuntimeException("无权限");
        }
        // 时间和当前时间不能超过 5 分钟
        Long currentTime = System.currentTimeMillis() / 1000;
        final Long FIVE_MINUTES = 60 * 5L;
        if ((currentTime - Long.parseLong(timestamp)) >= FIVE_MINUTES) {
            return handleNoAuth(response);
        }
        // todo 实际情况中是从数据库中查出 secretKey
        // 从查询到的用户信息中获取用户的sk
        String secretKey = invokeUser.getSecretKey();
        String serverSign = SignUtils.genSign(body, secretKey);
        if (sign == null || !sign.equals(serverSign)) {
            return handleNoAuth(response);
        }

        // 4.判断请求的模拟接口是否存在
        //todo 直接远程调用后端提供的相关接口
        InterfaceInfo interfaceInfo = null;
        try {
            interfaceInfo = innerInterfaceInfoService.getInterfaceInfo(path, method);
        } catch (Exception e) {
            log.error("getInterfaceInfo error", e);
        }
        if (interfaceInfo == null) {
            return handleNoAuth(response);
        }

        // 5.请求转发，调用模拟接口
        // 6.响应日志
        // 7.调用成功，接口调用次数+1
        // 567均在handleResponse中实现
        return handleResponse(exchange, chain, interfaceInfo.getId(), interfaceInfo.getUserId());

//        log.info("custom global filter");
//        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -1;
    }

    public Mono<Void> handleNoAuth(ServerHttpResponse response) {
        // 设置响应码为 403 FORBIDDEN
        response.setStatusCode(HttpStatus.FORBIDDEN);
        return response.setComplete();
    }

    public Mono<Void> handleInvokeError(ServerHttpResponse response) {
        // 设置响应码为 500
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        return response.setComplete();
    }

    public Mono<Void> handleResponse(ServerWebExchange exchange, GatewayFilterChain chain, long interfaceInfoId, long userId) {
        try {
            // 获取原始的响应对象
            ServerHttpResponse originalResponse = exchange.getResponse();
            // 获取数据缓冲工厂
            DataBufferFactory bufferFactory = originalResponse.bufferFactory();
            // 获取响应状态码
            HttpStatus statusCode = originalResponse.getStatusCode();
            if (statusCode != HttpStatus.OK) {
                return chain.filter(exchange);//降级处理返回数据
            }
            // 被装饰过的响应
            ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
                // 重写writeWith方法，用于处理响应体数据
                // 模拟接口调用完成后，执行该方法
                @Override
                public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                    if (body instanceof Flux) {
                        Flux<? extends DataBuffer> fluxBody = Flux.from(body);

                        return super.writeWith(fluxBody.buffer().map(dataBuffers -> {
                            // 合并多个流集合，解决返回体分段传输
                            DataBufferFactory dataBufferFactory = new DefaultDataBufferFactory();
                            DataBuffer buff = dataBufferFactory.join(dataBuffers);
                            byte[] content = new byte[buff.readableByteCount()];
                            buff.read(content);
                            DataBufferUtils.release(buff);//释放掉内存
                            // 构建日志
                            StringBuilder sb2 = new StringBuilder(200);
                            List<Object> rspArgs = new ArrayList<>();
                            rspArgs.add(originalResponse.getStatusCode());
                            String data = new String(content, StandardCharsets.UTF_8);
                            sb2.append(data);
                            // 7.todo 调用成功，接口调用次数+1
                            try {
                                innerUserInterfaceInfoService.invokeCount(interfaceInfoId, userId);
                            } catch (Exception e) {
                                log.error("invokeCount error", e);
                            }

                            log.info("响应结果：" + data);
                            // 将处理后的内容重新包装为DataBuffer并返回
                            return bufferFactory.wrap(content);
                        }));
                    } else {
                        // 8.调用失败，返回一个规范的错误码
                        log.error("<-- {} 响应code异常", getStatusCode());
                    }
                    return super.writeWith(body);
                }
            };
            // 对于200OK的请求，将装饰后的响应对象传递给下一个过滤器，并继续处理
            return chain.filter(exchange.mutate().response(decoratedResponse).build());
        } catch (Exception e) {
            log.error("网关处理响应异常" + e);
            return chain.filter(exchange);
        }
    }

}


