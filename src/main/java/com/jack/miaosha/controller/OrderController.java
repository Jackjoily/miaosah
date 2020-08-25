package com.jack.miaosha.controller;

import com.jack.miaosha.domain.MiaoShaUser;
import com.jack.miaosha.domain.OrderInfo;
import com.jack.miaosha.result.CodeMsg;
import com.jack.miaosha.result.Result;
import com.jack.miaosha.service.GoodsService;
import com.jack.miaosha.service.OrderService;
import com.jack.miaosha.vo.GoodsVo;
import com.jack.miaosha.vo.OrderDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    OrderService orderService;
    @Autowired
    GoodsService goodsService;

    @RequestMapping(value = "/getOrder", method = RequestMethod.POST)
    @ResponseBody
    public Result<OrderDetailVo> getOrderDetail(
            MiaoShaUser user,
            @RequestParam("orderId") long orderId
    ) {
        if (user == null) {
            return Result.error(CodeMsg.USER_SESSION_ERROR);
        }
        OrderInfo orderInfo = orderService.getOrderById(orderId);
        if (orderInfo == null) {
            return Result.error(CodeMsg.ORDER_NOT_EXIST);
        }
        Long id = orderInfo.getGoodsId();
        GoodsVo goodsVoByGoodsId = goodsService.getGoodsVoByGoodsId(id);
        OrderDetailVo orderDetailVo = new OrderDetailVo();
        orderDetailVo.setGoods(goodsVoByGoodsId);
        orderDetailVo.setOrder(orderInfo);
        return Result.success(orderDetailVo);
    }

}
