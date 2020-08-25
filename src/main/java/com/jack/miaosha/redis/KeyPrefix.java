package com.jack.miaosha.redis;

public interface KeyPrefix {

    int expireSeconds();

    String getPrefix();


}



