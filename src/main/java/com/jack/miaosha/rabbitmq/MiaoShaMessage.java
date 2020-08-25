package com.jack.miaosha.rabbitmq;

import com.jack.miaosha.domain.MiaoShaUser;
import lombok.Data;

@Data
public class MiaoShaMessage {
    private MiaoShaUser user;
    private Long goodsId;
}
