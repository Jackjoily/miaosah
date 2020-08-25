package com.jack.miaosha.controller;

import com.jack.miaosha.domain.MiaoShaOrder;
import com.jack.miaosha.domain.MiaoShaUser;
import com.jack.miaosha.domain.OrderInfo;
import com.jack.miaosha.rabbitmq.MQSender;
import com.jack.miaosha.rabbitmq.MiaoShaMessage;
import com.jack.miaosha.redis.AccessKey;
import com.jack.miaosha.redis.GoodsKey;
import com.jack.miaosha.redis.MiaoShaKey;
import com.jack.miaosha.redis.RedisService;
import com.jack.miaosha.result.CodeMsg;
import com.jack.miaosha.result.Result;
import com.jack.miaosha.service.GoodsService;
import com.jack.miaosha.service.MiaoShaService;
import com.jack.miaosha.service.MiaoShaUserService;
import com.jack.miaosha.service.OrderService;
import com.jack.miaosha.util.MD5Utils;
import com.jack.miaosha.util.UUIDUtil;
import com.jack.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/miaosha")
@Controller
public class MiaoShaController implements InitializingBean {
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
    @Autowired
    MQSender mqSender;
    private Map<Long, Boolean> localOverMap = new HashMap<Long, Boolean>();

    @RequestMapping(value = "/{path}/domiaosha", method = RequestMethod.POST)
    @ResponseBody
    public Result<Integer> miaosha(Model model, MiaoShaUser user,
                                   @RequestParam("goodsId") long goodsId,
                                   @PathVariable("path") String path) {
        model.addAttribute("user", user);
        if (user == null) {
            return Result.error(CodeMsg.SERVER_ERROR);
        }
        //验证Path
        boolean flag = miaoShaService.checkPath(path, user, goodsId);
        if (!flag) {
            return Result.error(CodeMsg.SERVER_ERROR);
        }

        //内存标记减少，redis的访问
        boolean over = localOverMap.get(goodsId);
        if (over) {
            return Result.error(CodeMsg.MIAO_SHA_OVER);
        }

        //进行与欲减库存
        long stock = redisService.decr(GoodsKey.getMiaoshaGoodsStock, goodsId + "");

        //判断商品是否还有库存
        if (stock < 0) {
            localOverMap.put(goodsId, true);
            return Result.error(CodeMsg.MIAO_SHA_OVER);
        }
        //判断是否已经秒杀到了
        MiaoShaOrder miaoShaOrder = orderService.getMiaoShaOrderByUserIdGoodsId(user.getId(), goodsId);
        if (miaoShaOrder != null) {
            return Result.error(CodeMsg.MIAO_SHA_REPEAT);
        }
        //入队
        MiaoShaMessage mm = new MiaoShaMessage();
        mm.setUser(user);
        mm.setGoodsId(goodsId);
        mqSender.sendMiaoshaMessage(mm);
        return Result.success(0);
//        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
//        Integer stockCount = goods.getStockCount();
//        if (stockCount <= 0) {
//            return Result.error(CodeMsg.MIAO_SHA_OVER);
//        }
        //判断是否已经秒杀到了

        //进行减库存 ，下订单，写入秒杀订单
//        OrderInfo orderInfo = miaoShaService.miaosha(user, goods);
//        return Result.success(orderInfo);
    }

    //在系统初始化的时候

    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> goodsVos = goodsService.listGoodVo();
        if (goodsVos == null) {
            return;
        }
        for (GoodsVo goodsVo : goodsVos) {
            redisService.set(GoodsKey.getMiaoshaGoodsStock, goodsVo.getId() + "", goodsVo.getStockCount());
            localOverMap.put(goodsVo.getId(), false);

        }
    }

    /**
     * @param model
     * @param user
     * @param goodsId
     * @return 秒杀成功返回订单id
     * 失败返回-1
     * 0：排队中
     */

    @RequestMapping(value = "/result", method = RequestMethod.GET)
    @ResponseBody
    public Result<String> result(Model model, MiaoShaUser user,
                                 @RequestParam("goodsId") long goodsId) {
        if (user == null) {
            return Result.error(CodeMsg.SERVER_ERROR);
        }
        String result = miaoShaService.getMiaoshaResult(user.getId(), goodsId);
        return Result.success(result);
    }

    @RequestMapping(value = "/path", method = RequestMethod.GET)
    @ResponseBody
    public Result<String> getMiaoShaPath(HttpServletRequest req, MiaoShaUser user,
                                         @RequestParam("verifyCode") int verifyCode,
                                         @RequestParam("goodsId") long goodsId) {
        if (user == null) {
            return Result.error(CodeMsg.SERVER_ERROR);
        }
//查询访问的次数
        String requestURI = req.getRequestURI();
        String key = requestURI + "_" + user.getId();
        Integer count = redisService.get(AccessKey.access, key, Integer.class);
        if (count == null) {
            redisService.set(AccessKey.access, key, 1);
        } else if (count < 5) {
            redisService.incr(AccessKey.access, key);
        } else {
            return Result.error(CodeMsg.SERVER_ERROR);
        }

        boolean check = miaoShaService.checkVerifyCode(user, goodsId, verifyCode);
        if (!check) {
            return Result.error(CodeMsg.REQUEST_ILLEGAL);
        }
        String path = miaoShaService.createMiaoshaPath(user, goodsId);
        return Result.success(path);
    }


    @RequestMapping(value = "/verfiyCodeImage", method = RequestMethod.GET)
    @ResponseBody
    public Result<String> getVerfiyCodeImage(MiaoShaUser user,
                                             @RequestParam("goodsId") long goodsId,
                                             HttpServletResponse response) {
        if (user == null) {
            return Result.error(CodeMsg.SERVER_ERROR);
        }
        BufferedImage img = miaoShaService.createVerfiyCode(user, goodsId);
        try {
            ServletOutputStream out = response.getOutputStream();
            ImageIO.write(img, "JPEG", out);
            out.flush();
            out.close();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return Result.error(CodeMsg.MIAO_SHA_FAIL);
        }
    }

}
