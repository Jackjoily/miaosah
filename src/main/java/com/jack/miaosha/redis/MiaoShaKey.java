package com.jack.miaosha.redis;

public class MiaoShaKey extends BasePrefix {
    private MiaoShaKey(String prefix) {
        super(prefix);
    }

    private MiaoShaKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static MiaoShaKey isGoodOver = new MiaoShaKey("go");

    public static MiaoShaKey getMiaoShaPath = new MiaoShaKey(60, "mp");
    public static MiaoShaKey getMiaoshaVerifyCode = new MiaoShaKey(300, "mv");
}
