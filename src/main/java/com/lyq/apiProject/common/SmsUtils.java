package com.lyq.apiProject.common;

import com.lyq.apiProject.model.dto.user.UserSmsDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

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

    public boolean sendSms(UserSmsDTO userSmsDTO) {
        // 1.从Redis令牌桶中获取令牌，获取失败则不允许发短信

        // 2.将手机号对应的验证码存入Redis，并设置过期时间，后续登录/注册时校验

        // 3.使用消息队列，异步发送短信
        return true;
    }
}
