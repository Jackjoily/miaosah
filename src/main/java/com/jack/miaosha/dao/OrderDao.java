package com.jack.miaosha.dao;

import com.jack.miaosha.domain.MiaoShaOrder;
import com.jack.miaosha.domain.OrderInfo;
import com.mysql.cj.x.protobuf.MysqlxCrud;
import org.apache.ibatis.annotations.*;

@Mapper
public interface OrderDao {
    @Select("select * from miaosha_order where user_id =#{userId} and goods_id=#{goodsId}")
    MiaoShaOrder getMiaoShaOrderByUserIdGoodsId(@Param("userId") Long userId, @Param("goodsId") long goodsId);

    @Insert("insert into order_info( id,user_id,goods_id,goods_name,goods_count,goods_price,order_channel,status,create_date)values(" +
            "#{id},#{userId},#{goodsId},#{goodsName},#{goodsCount},#{goodsPrice},#{orderChannel},#{status},#{createDate})")
    int insert(OrderInfo orderInfo);

    @Insert("insert into miaosha_order(id,user_id,goods_id,order_id) values(#{id},#{userId},#{goodsId},#{orderId})")
    int insertMiaoshaOrder(MiaoShaOrder miaoShaOrder);

    @Select("select * from order_info where id=#{orderId}")
    OrderInfo getOrderById(@Param("orderId") long orderId);
}
