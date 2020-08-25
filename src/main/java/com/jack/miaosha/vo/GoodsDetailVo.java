package com.jack.miaosha.vo;

import com.jack.miaosha.domain.MiaoShaUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoodsDetailVo {
    private int miaoshaStatus = 0;
    private int remainSeconds = 0;
    private GoodsVo goodsVo;
    private MiaoShaUser user;
}
