package com.jack.miaosha.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class MQConfig {



    public static final String MIAOSHAQUEUE = "miaosha.queue";



    public static final String QUEUE = "queue";
    public static final String TOPICEQUEUE1 = "topic.queue1";
    public static final String TOPICEQUEUE2 = "topic.queue2";
    public static final String HEADQUEUE = "head.queue";

    public static final String TOPICEXCHANGER = "topic.exchanger";
    public static final String ROUTINGKEY1 = "topic.key1";
    public static final String ROUTINGKEY2 = "topic.#";
    public static final String FANOUTEXCHANGER = "fanout.exchanger";
    public static final String HEADERSEXCHANGER = "headersExchanger";

    @Bean
    public Queue queue() {
        return new Queue(MIAOSHAQUEUE, true);
    }

    /**
     * topic模式交换机Exchange
     */
//    @Bean
//    public Queue topicQueue1() {
//        return new Queue(TOPICEQUEUE1, true);
//    }
//
//    @Bean
//    public Queue topicQueue2() {
//        return new Queue(TOPICEQUEUE2, true);
//    }
//
//    @Bean
//    public TopicExchange topicExchange() {
//        return new TopicExchange(TOPICEXCHANGER);
//    }
//
//    @Bean
//    public Binding topicBinding1() {
//        return BindingBuilder.bind(topicQueue1()).to(topicExchange())
//                .with(ROUTINGKEY1);
//    }
//
//    @Bean
//    public Binding topicBinding2() {
//        return BindingBuilder.bind(topicQueue2()).to(topicExchange())
//                .with(ROUTINGKEY2);
//    }

    /**
     * Fanout 模式，广播模式，可以同时发给多个Queue
     */
//    @Bean
//    public FanoutExchange fanoutExchange() {
//        return new FanoutExchange(FANOUTEXCHANGER);
//    }
//
//    @Bean
//    public Binding BindingFanout1() {
//        return BindingBuilder.bind(topicQueue1()).to(fanoutExchange());
//    }
//
//    @Bean
//    public Binding BindingFanout2() {
//        return BindingBuilder.bind(topicQueue2()).to(fanoutExchange());
//    }

    /**
     * Hearder模式交换机Exchange
     */

//    @Bean
//    public HeadersExchange headersExchange() {
//        return new HeadersExchange(HEADERSEXCHANGER);
//    }
//
//    @Bean
//    public Queue headerQueue1() {
//        return new Queue(HEADQUEUE, true);
//    }
//
//    @Bean
//    public Binding BindingHeader() {
//        Map<String, Object> map = new HashMap<>();
//        map.put("header1", "value1");
//        map.put("header2", "value2");
//        return BindingBuilder.bind(headerQueue1()).to(headersExchange())
//                .whereAll(map).match();
//    }


}

