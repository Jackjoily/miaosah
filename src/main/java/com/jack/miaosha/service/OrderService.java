package com.jack.miaosha.service;

import com.jack.miaosha.dao.OrderDao;
import com.jack.miaosha.domain.MiaoShaOrder;
import com.jack.miaosha.domain.MiaoShaUser;
import com.jack.miaosha.domain.OrderInfo;
import com.jack.miaosha.redis.OrderKey;
import com.jack.miaosha.redis.RedisService;
import com.jack.miaosha.util.KeyWorker;
import com.jack.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class OrderService {
    @Autowired
    private OrderDao orderDao;
    @Autowired
    RedisService redisService;

    public MiaoShaOrder getMiaoShaOrderByUserIdGoodsId(Long userId, long goodsId) {
        MiaoShaOrder miaoShaOrder = redisService.get(OrderKey.getMiaoshaOrderByUidGid, "" + userId + "_" + goodsId, MiaoShaOrder.class);
        return miaoShaOrder;
    }

    @Transactional
    public OrderInfo createOrder(MiaoShaUser user, GoodsVo goods) {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setId(KeyWorker.nextId());
        orderInfo.setCreateDate(new Date());
        orderInfo.setGoodsCount(1);
        orderInfo.setGoodsId(goods.getId());
        orderInfo.setGoodsName(goods.getGoodsName());
        orderInfo.setGoodsPrice(goods.getMiaoshaPrice());
        orderInfo.setOrderChannel(1);
        orderInfo.setStatus(0);
        orderInfo.setUserId(user.getId());
        orderDao.insert(orderInfo);
        MiaoShaOrder miaoShaOrder = new MiaoShaOrder();
        miaoShaOrder.setId(KeyWorker.nextId());
        miaoShaOrder.setGoodsId(goods.getId());
        miaoShaOrder.setOrderId(orderInfo.getId());
        miaoShaOrder.setUserId(user.getId());
        orderDao.insertMiaoshaOrder(miaoShaOrder);
        redisService.set(OrderKey.getMiaoshaOrderByUidGid, "" + user.getId() + "_" + goods.getId(), miaoShaOrder);
        return orderInfo;
    }

    public OrderInfo getOrderById(long orderId) {
        return orderDao.getOrderById(orderId);
    }
}
