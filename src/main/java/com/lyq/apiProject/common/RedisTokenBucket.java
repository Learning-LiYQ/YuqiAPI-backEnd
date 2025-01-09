package com.lyq.apiProject.common;

import com.lyq.apiProject.constant.RedisConstant;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

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
        int     permitsPerMinute = 1; // 每个手机号码一分钟内只能发送一条短信
        int     maxPermits       = 1; // 令牌桶容量
        int     tokensNum        = 0; // 已有的令牌数
        long    currentTime      = System.currentTimeMillis(); // 当前时间戳
        long    lastRefillTime   = 0; //令牌桶上次填充的时间戳

        String key = RedisConstant.SMS_BUCKET_PREFIX + phoneNumber;
        String tokens = redisTemplate.opsForValue().get(key + "_tokens");
        if (tokens != null) {
            tokensNum = Integer.parseInt(tokens);
        }
        String lastRefillTimeStr = redisTemplate.opsForValue().get(key + "_last_refill_time");
        if (lastRefillTimeStr != null) {
            lastRefillTime = Long.parseLong(lastRefillTimeStr);
        }

        // 通过当前与上一次填充的时间差，计算出需要填充的令牌数
        int refillTokens = (int) (currentTime-lastRefillTime) / 1000 / 60 * permitsPerMinute;
        tokensNum = Math.min(maxPermits, refillTokens + tokensNum);
        // 更新上次填充时间戳
        redisTemplate.opsForValue().set(key + "_last_refill_time", String.valueOf(currentTime),EXPIRE_TIME, TimeUnit.SECONDS);
        // 如果令牌数大于等于1，则获取令牌
        if (tokensNum >= 1) {
            tokensNum--;
            redisTemplate.opsForValue().set(key + "_tokens", String.valueOf(tokensNum),EXPIRE_TIME, TimeUnit.SECONDS);
            return true; // 获取到令牌，返回true
        }
        return false; // 没有获取到令牌，返回false
    }
}
