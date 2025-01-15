package com.lyq.apiProject.common;

import com.lyq.apiProject.constant.RedisConstant;
import com.lyq.apiProject.exception.BusinessException;
import com.lyq.apiProject.model.dto.user.UserSmsDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * 短信工具类
 * @author lyq
 */
@Component
@Slf4j
public class SmsUtils {
    @Resource
    private RedisTemplate<String, String> redisTemplate;
    @Resource
    private RedisTokenBucket redisTokenBucket;
    @Resource
    private RabbitMqUtils rabbitMqUtils;

    public boolean sendSms(UserSmsDTO userSmsDTO) {
        String phoneNum = userSmsDTO.getPhoneNumber();
        String code = userSmsDTO.getCaptchaCode();

        // 1.从Redis令牌桶中获取令牌，获取失败则不允许发短信
        if (!redisTokenBucket.tryAcquire(phoneNum)) {
            log.info("phoneNum：{}，send SMS frequent", phoneNum);
            return false;
        }
        log.info("发送短信：{}", userSmsDTO);

        // 2.将手机号对应的验证码存入Redis，并设置过期时间，后续登录/注册时校验
        redisTemplate.opsForValue().set(RedisConstant.SMS_BUCKET_PREFIX+phoneNum, String.valueOf(code), 5, TimeUnit.MINUTES);

        // 3.使用消息队列，异步发送短信
        try {
            rabbitMqUtils.sendSmsAsync(userSmsDTO);
        } catch (Exception e) {
            // 删除令牌桶？
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "发送验证码失败，请稍后再试");
        }
        log.info("发送验证码成功---->手机号为{}，验证码为{}", phoneNum, code);
        return true;
    }
}
