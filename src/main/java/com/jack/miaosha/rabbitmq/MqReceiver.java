package com.jack.miaosha.rabbitmq;

import com.jack.miaosha.domain.MiaoShaOrder;
import com.jack.miaosha.domain.MiaoShaUser;
import com.jack.miaosha.domain.OrderInfo;
import com.jack.miaosha.redis.RedisService;
import com.jack.miaosha.result.CodeMsg;
import com.jack.miaosha.result.Result;
import com.jack.miaosha.service.GoodsService;
import com.jack.miaosha.service.MiaoShaService;
import com.jack.miaosha.service.MiaoShaUserService;
import com.jack.miaosha.service.OrderService;
import com.jack.miaosha.vo.GoodsVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MqReceiver {
    /**
     * Direct 模式 交换机Exchange
     *
     * @param message
     */
    @Autowired
    MiaoShaUserService miaoShaUserService;
    @Autowired
    RedisService redisService;
    @Autowired
    GoodsService goodsService;
    @Autowired
    OrderService orderService;
    @Autowired
    MiaoShaService miaoShaService;

    //
//    @RabbitListener(queues = MQConfig.QUEUE)
//    public void receive(String message) {
//        log.info("receive message :" + message);
//    }
//
//    @RabbitListener(queues = MQConfig.TOPICEQUEUE1)
//    public void receiveTopiceQUEUE1(String message) {
//        log.info("receive topic queue1 message :" + message);
//    }
//
//    @RabbitListener(queues = MQConfig.TOPICEQUEUE2)
//    public void receiveTopiceQUEUE2(String message) {
//        log.info("receive topic queue2 message :" + message);
//    }
//
//    @RabbitListener(queues = MQConfig.HEADQUEUE)
//    public void receiveHeadQUEUE(byte []message)
//    {
//        log.info("receive header queue message :" + new String(message));
//    }
    @RabbitListener(queues = MQConfig.MIAOSHAQUEUE)
    public void receive(String message) {
        log.info("receive message :" + message);
        MiaoShaMessage mm = RedisService.stringToBean(message, MiaoShaMessage.class);
        MiaoShaUser user = mm.getUser();
        Long goodsId = mm.getGoodsId();
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        Integer stockCount = goods.getStockCount();
        if (stockCount <= 0) {
            return;
        }
        MiaoShaOrder miaoShaOrder = orderService.getMiaoShaOrderByUserIdGoodsId(user.getId(), goodsId);
        if (miaoShaOrder != null) {
            return;
        }
        OrderInfo orderInfo = miaoShaService.miaosha(user, goods);
    }

}
