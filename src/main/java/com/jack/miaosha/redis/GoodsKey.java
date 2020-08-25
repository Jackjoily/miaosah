package com.jack.miaosha.redis;

public class GoodsKey extends BasePrefix {
    private GoodsKey(int se, String prefix) {
        super(se, prefix);
    }

    public static GoodsKey getGoodsList = new GoodsKey(60, "gl");
    public static GoodsKey getGoodsDetail = new GoodsKey(60, "gd");
    public static GoodsKey getMiaoshaGoodsStock = new GoodsKey(0, "gs");
}
