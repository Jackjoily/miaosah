package com.jack.miaosha.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MiaoShaOrder {
    private String id;
    private Long userId;
    private String orderId;
    private Long goodsId;
}
