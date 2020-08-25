package com.jack.miaosha.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MiaoShaGoods {
    private Long id;
    private Long goodsId;
    private Integer stockCount;
    private Double miaoshaPrice;
    private Date startDate;
    private Date endDate;
}
