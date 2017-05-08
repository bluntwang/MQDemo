package com.hsu.demo.rabbitmq;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Created by xushengxiang on 2017/4/22.
 */
@Component("rabbitReceiver")
public class Receiver {

    @RabbitListener(queues = "rabbit0")
    public String receiveMsg(String msg){
        System.out.println("receive rabbit mq message: "+msg);
        return "ok";
    }
}
