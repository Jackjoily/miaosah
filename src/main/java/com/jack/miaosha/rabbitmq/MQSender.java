package com.jack.miaosha.rabbitmq;

import com.jack.miaosha.redis.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MQSender {

    @Autowired
    AmqpTemplate amqpTemplate;

//    public void send(Object message) {
//        String msg = RedisService.beanToString(message);
//        log.info("send message" + msg);
//        amqpTemplate.convertAndSend(MQConfig.QUEUE, message);
//    }
//
//    public void sendTopic(Object message) {
//        String msg = RedisService.beanToString(message);
//        log.info("send topic message" + msg);
//        amqpTemplate.convertAndSend(MQConfig.TOPICEXCHANGER, MQConfig.ROUTINGKEY1, msg + "1");
//        amqpTemplate.convertAndSend(MQConfig.TOPICEXCHANGER, "topic.key2", msg + "2");
//    }
//
//
//    public void sentFanout(Object message) {
//        String msg = RedisService.beanToString(message);
//        log.info("send fanout message" + msg);
//        amqpTemplate.convertAndSend(MQConfig.FANOUTEXCHANGER, "", msg);
//    }
//    public void  sendHeader(Object message) {
//        String msg = RedisService.beanToString(message);
//        log.info("send header message" + msg);
//        MessageProperties messageProperties=new MessageProperties();
//        messageProperties.setHeader("header1","value1");
//        messageProperties.setHeader("header2","value2");
//        Message obj=new Message(msg.getBytes(),messageProperties);
//        amqpTemplate.convertAndSend(MQConfig.HEADERSEXCHANGER,"", obj);
//    }

    public void sendMiaoshaMessage(MiaoShaMessage mm) {
        String msg = RedisService.beanToString(mm);
        log.info("send message" + msg);
        amqpTemplate.convertAndSend(MQConfig.MIAOSHAQUEUE, msg);
    }
}
