package com.jack.miaosha.service;

import com.jack.miaosha.domain.Goods;
import com.jack.miaosha.domain.MiaoShaOrder;
import com.jack.miaosha.domain.MiaoShaUser;
import com.jack.miaosha.domain.OrderInfo;
import com.jack.miaosha.redis.MiaoShaKey;
import com.jack.miaosha.redis.RedisService;
import com.jack.miaosha.util.MD5Utils;
import com.jack.miaosha.util.UUIDUtil;
import com.jack.miaosha.vo.GoodsVo;
import com.mysql.cj.x.protobuf.MysqlxCrud;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.UUID;

@Service
public class MiaoShaService {
    @Autowired
    GoodsService goodsService;
    @Autowired
    OrderService orderService;
    @Autowired
    RedisService redisService;

    @Transactional
    public OrderInfo miaosha(MiaoShaUser user, GoodsVo goods) {
        //减库存 下订单，写入秒杀订单
        boolean b = goodsService.reduceStock(goods);
        if (b) {
            //orderInfo ,miaosha_order
            Goods g = new Goods();
            g.setId(goods.getId());
            g.setGoodsStock(goods.getGoodsStock() - 1);
            OrderInfo orderInfo = orderService.createOrder(user, goods);
            return orderInfo;
        } else {
            //商品被秒杀玩了
            setGoodsOver(goods.getId());
            return null;
        }
    }


    public String getMiaoshaResult(Long id, long goodsId) {
        MiaoShaOrder miaoShaOrderByUserIdGoodsId = orderService.getMiaoShaOrderByUserIdGoodsId(id, goodsId);
        if (miaoShaOrderByUserIdGoodsId != null) {
            return miaoShaOrderByUserIdGoodsId.getOrderId();
        } else {
            boolean isOver = getGoodsOver(goodsId);
            if (isOver) {
                return -1 + "";
            } else {
                return 0 + "";
            }
        }
    }

    private void setGoodsOver(Long id) {
        redisService.set(MiaoShaKey.isGoodOver, "" + id, true);
    }

    private boolean getGoodsOver(long goodsId) {
        return redisService.exists(MiaoShaKey.isGoodOver, "" + goodsId);
    }

    public boolean checkPath(String path, MiaoShaUser user, long goodsId) {
        if (user == null || path == null) {
            return false;
        }
        String s = redisService.get(MiaoShaKey.getMiaoShaPath, user.getId() + "_" + goodsId, String.class);
        return s.equals(path);
    }

    public String createMiaoshaPath(MiaoShaUser user, long goodsId) {
        String str = MD5Utils.md5(UUIDUtil.uuid() + "123456");
        redisService.set(MiaoShaKey.getMiaoShaPath, user.getId() + "_" + goodsId, str);
        return str;
    }

    public BufferedImage createVerfiyCode(MiaoShaUser user, long goodsId) {
        if (user == null || goodsId <= 0) {
            return null;
        }
        int width = 80;
        int height = 32;
        //create the image
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        // set the background color
        g.setColor(new Color(0xDCDCDC));
        g.fillRect(0, 0, width, height);
        // draw the border
        g.setColor(Color.black);
        g.drawRect(0, 0, width - 1, height - 1);
        // create a random instance to generate the codes
        Random rdm = new Random();
        // make some confusion
        for (int i = 0; i < 50; i++) {
            int x = rdm.nextInt(width);
            int y = rdm.nextInt(height);
            g.drawOval(x, y, 0, 0);
        }
        // generate a random code
        String verifyCode = generateVerifyCode(rdm);
        g.setColor(new Color(0, 100, 0));
        g.setFont(new Font("Candara", Font.BOLD, 24));
        g.drawString(verifyCode, 8, 24);
        g.dispose();
        //把验证码存到redis中
        int rnd = calc(verifyCode);
        redisService.set(MiaoShaKey.getMiaoshaVerifyCode, user.getId() + "," + goodsId, rnd);
        //输出图片
        return image;
    }

    private static int calc(String exp) {
        try {
            //JDK1.6之后引入的
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("JavaScript");
            return (Integer) engine.eval(exp);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    private static char[] ops = new char[]{'+', '-', '*'};

    /**
     * + - *
     */
    private String generateVerifyCode(Random rdm) {
        int num1 = rdm.nextInt(10);
        int num2 = rdm.nextInt(10);
        int num3 = rdm.nextInt(10);
        char op1 = ops[rdm.nextInt(3)];
        char op2 = ops[rdm.nextInt(3)];
        String exp = "" + num1 + op1 + num2 + op2 + num3;
        return exp;
    }

    public boolean checkVerifyCode(MiaoShaUser user, long goodsId, int verifyCode) {
        if (user == null || goodsId < 0) {
            return false;
        }
        Integer integer = redisService.get(MiaoShaKey.getMiaoshaVerifyCode, user.getId() + "," + goodsId, Integer.class);
        if (integer == null || (integer - verifyCode) != 0) {
            return false;
        }
        redisService.delete(MiaoShaKey.getMiaoshaVerifyCode, user.getId() + "," + goodsId);
        return true;
    }
}
