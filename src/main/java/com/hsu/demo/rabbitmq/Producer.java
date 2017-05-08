package com.hsu.demo.rabbitmq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by xushengxiang on 2017/4/22.
 */
@Component("rabbitProducer")
public class Producer {

    @Resource
    private RabbitTemplate rabbitTemplate;

    public String send(String msg){
        return rabbitTemplate.convertSendAndReceive("rabbit0", msg).toString();
    }
}
