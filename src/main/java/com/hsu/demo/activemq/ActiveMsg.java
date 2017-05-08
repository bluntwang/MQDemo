package com.hsu.demo.activemq;

import org.springframework.jms.core.MessageCreator;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

/**
 * Created by xushengxiang on 2017/4/21.
 */
public class ActiveMsg implements MessageCreator {

    private String msg;

    public ActiveMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public Message createMessage(Session session) throws JMSException {
        return session.createTextMessage(msg);
    }
}
