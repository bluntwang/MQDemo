package com.hsu.demo.controller;

import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Created by xushengxiang on 2017/4/21.
 */
@RestController
public class MQController {
    @Resource(name = "activeProducer")
    private com.hsu.demo.activemq.Producer activeProducer;

    @Resource(name = "rabbitProducer")
    private com.hsu.demo.rabbitmq.Producer rabbitProducer;

    @RequestMapping("/")
    String home() {
        return "Hello World!";
    }

    @RequestMapping(value = "/active", method = RequestMethod.GET)
    public String receiveActive(@RequestParam String msg) throws Exception {

        ActiveMQTextMessage message = (ActiveMQTextMessage) activeProducer.send(msg);
        return message.getText();
    }

    @RequestMapping(value = "/rabbit", method = RequestMethod.GET)
    public String receiveRabbit(@RequestParam String msg) throws Exception {

        return rabbitProducer.send(msg);
    }
}

