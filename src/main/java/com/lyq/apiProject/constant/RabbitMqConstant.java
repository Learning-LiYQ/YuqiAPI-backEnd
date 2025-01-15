package com.lyq.apiProject.constant;

/**
 * RabbitMq相关的前缀
 * @author lyq
 */
public interface RabbitMqConstant {
        /* 短信相关 */
    String SMS_EXCHANGE_TOPIC_NAME = "sms.exchange.topic";
    String SMS_QUEUE_NAME = "sms.queue";
    String SMS_EXCHANGE_ROUTING_KEY = "sms.send";
    String SMS_MESSAGE_PREFIX = "sms.message";

    String SMS_DELAY_QUEUE_NAME = "sms.delay.queue";
    String SMS_DELAY_EXCHANGE_ROUTING_KEY = "sms.release";
}
