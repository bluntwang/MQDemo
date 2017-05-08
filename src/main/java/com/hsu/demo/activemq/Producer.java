package com.hsu.demo.activemq;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.jms.Message;

/**
 * Created by xushengxiang on 2017/4/21.
 */
@Component("activeProducer")
public class Producer{

    @Resource
    private JmsTemplate jmsTemplate;

    public Message send(String msg) throws Exception {
        //jmsTemplate.convertAndSend("active0",msg);
        return jmsTemplate.sendAndReceive("active0",new ActiveMsg(msg));
    }
}
