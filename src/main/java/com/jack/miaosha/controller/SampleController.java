package com.jack.miaosha.controller;

import com.jack.miaosha.rabbitmq.MQSender;
import com.jack.miaosha.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SampleController {

//    @Autowired
//    MQSender mqSender;
//
//    @RequestMapping("/mq")
//    @ResponseBody
//    public Result<String> mq() {
//        mqSender.send(":hello imooc");
//        return Result.success("Hello ,world");
//    }
//
//    @RequestMapping("/mq/topic")
//    @ResponseBody
//    public Result<String> topic() {
//        mqSender.sendTopic(":hello imooc");
//        return Result.success("Hello ,world");
//    }
//
//
//    @RequestMapping("/mq/fanout")
//    @ResponseBody
//    public Result<String> fanout() {
//        mqSender.sentFanout(":hello fanout");
//        return Result.success("Hello ,world");
//    }
//
//    @RequestMapping("/mq/header")
//    @ResponseBody
//    public Result<String> header() {
//        mqSender.sendHeader(":hello fanout");
//        return Result.success("Hello ,world");
//    }
}
