package com.oofgz.fight.rabbit;

import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Log4j2
@Component
public class Sender {

    @Autowired
    private AmqpTemplate rabbitMQTemplate;


    public void send() {
        String context = "hello " + new Date();
        log.info("Sender : " + context);
        this.rabbitMQTemplate.convertAndSend("hello", context);
    }
}
