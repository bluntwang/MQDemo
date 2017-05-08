package com.hsu.demo.activemq;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by xushengxiang on 2017/4/21.
 */
@Component("activeReceiver")
public class Receiver {


    @JmsListener(destination = "active0")
    public String receiveMsg(String msg){
        System.out.println("receive message: "+msg);
        return "OK";
    }
}
