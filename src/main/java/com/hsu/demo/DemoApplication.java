package com.hsu.demo;

import org.springframework.amqp.core.Queue;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan(basePackages = {"com.hsu.demo.activemq",
                                "com.hsu.demo.rabbitmq",
                                "com.hsu.demo.controller"})
public class DemoApplication{

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
    public Queue rabbitMQQueue(){
	    return new Queue("rabbit0");
    }
}
