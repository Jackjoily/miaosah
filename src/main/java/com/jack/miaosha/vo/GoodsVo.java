package com.jack.miaosha.vo;

import com.jack.miaosha.domain.Goods;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoodsVo extends Goods {
    private Integer stockCount;
    private Date startDate;
    private Date endDate;
    private Double miaoshaPrice;

}
