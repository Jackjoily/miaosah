package com.jack.miaosha.redis;

import com.fasterxml.jackson.databind.ser.Serializers;

public class AccessKey extends BasePrefix {

    public AccessKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static AccessKey access = new AccessKey(5, "access");
}
