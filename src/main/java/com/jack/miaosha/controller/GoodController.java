package com.jack.miaosha.controller;

import com.jack.miaosha.domain.MiaoShaUser;
import com.jack.miaosha.redis.GoodsKey;
import com.jack.miaosha.redis.RedisService;
import com.jack.miaosha.result.Result;
import com.jack.miaosha.service.GoodsService;
import com.jack.miaosha.service.MiaoShaUserService;
import com.jack.miaosha.vo.GoodsDetailVo;
import com.jack.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.Thymeleaf;
import org.thymeleaf.context.IWebContext;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller

@RequestMapping("/good")
public class GoodController {
    @Autowired
    MiaoShaUserService miaoShaUserService;
    @Autowired
    RedisService redisService;
    @Autowired
    GoodsService goodsService;
    @Autowired
    ThymeleafViewResolver thymeleafViewResolver;


    //进行缓存页面的操作
    @RequestMapping(value = "/to_list", produces = "text/html")
    @ResponseBody
    public String toList(Model model,
                         MiaoShaUser user,
                         HttpServletRequest request,
                         HttpServletResponse response
    ) {
        model.addAttribute("user", user);
        //查询商品列表
        List<GoodsVo> goodsVos = goodsService.listGoodVo();
        model.addAttribute("goodsVos", goodsVos);
        String html = redisService.get(GoodsKey.getGoodsList, "", String.class);
        if (!StringUtils.isEmpty(html)) {
            return html;
        }
        //不是空,进行手动渲染
        IWebContext ctx = new WebContext(request, response,
                request.getServletContext(), request.getLocale(), model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goods_list", ctx);
        if (!StringUtils.isEmpty(html)) {
            redisService.set(GoodsKey.getGoodsList, "", html);
        }
        return html;
    }

    //URL缓存
    @RequestMapping(value = "/to_detail/{goodsId}", produces = "text/html")
    @ResponseBody
    public String toDetail(Model model,
                           MiaoShaUser user,
                           @PathVariable("goodsId") long goodsId,
                           HttpServletRequest request,
                           HttpServletResponse response
    ) {
        model.addAttribute("user", user);
        //查询商品详情
        GoodsVo goodsVo = goodsService.getGoodsVoByGoodsId(goodsId);
        model.addAttribute("goods", goodsVo);
        long startTime = goodsVo.getStartDate().getTime();
        long endTime = goodsVo.getEndDate().getTime();
        long now = System.currentTimeMillis();
        int miaoshaStatus = 0;
        int remainSeconds = 0;
        if (now < startTime) {
            miaoshaStatus = 0;
            //秒杀没有开始呢
            remainSeconds = (int) (startTime - now) / 1000;
        } else if (now > endTime) {
            miaoshaStatus = 2;
            //秒杀结束了
            remainSeconds = -1;

        } else {
            miaoshaStatus = 1;
            //秒杀正在持续中
            remainSeconds = 0;
        }
        model.addAttribute("miaoshaStatus", miaoshaStatus);
        model.addAttribute("remainSeconds", remainSeconds);


        //从redis里面取缓存
        String html = redisService.get(GoodsKey.getGoodsDetail, "", String.class);
        if (!StringUtils.isEmpty(html)) {
            return html;
        }
        //不是空,进行手动渲染
        IWebContext ctx = new WebContext(request, response,
                request.getServletContext(), request.getLocale(), model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goods_detail", ctx);
        if (!StringUtils.isEmpty(html)) {
            redisService.set(GoodsKey.getGoodsDetail, "", html);
        }
        return html;
    }


    //URL缓存
    @RequestMapping(value = "/detail/{goodsId}")
    @ResponseBody
    public Result<GoodsDetailVo> toDetail2(
            MiaoShaUser user,
            @PathVariable("goodsId") long goodsId
            ) {
        GoodsVo goodsVo = goodsService.getGoodsVoByGoodsId(goodsId);
        long startTime = goodsVo.getStartDate().getTime();
        long endTime = goodsVo.getEndDate().getTime();
        long now = System.currentTimeMillis();
        int miaoshaStatus = 0;
        int remainSeconds = 0;
        if (now < startTime) {
            miaoshaStatus = 0;
            //秒杀没有开始呢
            remainSeconds = (int) (startTime - now) / 1000;
        } else if (now > endTime) {
            miaoshaStatus = 2;
            //秒杀结束了
            remainSeconds = -1;

        } else {
            miaoshaStatus = 1;
            //秒杀正在持续中
            remainSeconds = 0;
        }
        GoodsDetailVo goodsDetailVo = new GoodsDetailVo();
        goodsDetailVo.setGoodsVo(goodsVo);
        goodsDetailVo.setMiaoshaStatus(miaoshaStatus);
        goodsDetailVo.setRemainSeconds(remainSeconds);
        goodsDetailVo.setUser(user);
        return Result.success(goodsDetailVo);
    }


}
