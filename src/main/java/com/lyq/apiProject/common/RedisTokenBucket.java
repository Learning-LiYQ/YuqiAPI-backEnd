package com.lyq.apiProject.common;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class RedisTokenBucket {
    @Resource
    private RedisTemplate<String, String> redisTemplate;

    /**
     *  过期时间，400秒后过期
     */
    private final long EXPIRE_TIME = 400;

    /**
     * 获取令牌，每个手机号每分钟最多发送一条短信
     * @param phoneNumber
     * @return
     */
    public boolean tryAcquire(String phoneNumber) {
        return true;
    }
}
