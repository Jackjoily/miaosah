package com.jack.miaosha.service;

import com.jack.miaosha.dao.GoodsDao;
import com.jack.miaosha.domain.Goods;
import com.jack.miaosha.domain.MiaoShaGoods;
import com.jack.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsService {
    @Autowired
    GoodsDao goodsDao;

    public List<GoodsVo> listGoodVo() {
        return goodsDao.listGoodsVo();
    }


    public GoodsVo getGoodsVoByGoodsId(long goodsId) {
        return goodsDao.getGoodsVoByGoodsId(goodsId);
    }

    public boolean reduceStock(Goods goods) {
        MiaoShaGoods g = new MiaoShaGoods();
        g.setGoodsId(goods.getId());
        int ret = goodsDao.reduceStock(g);
        return ret > 0;
    }
}
