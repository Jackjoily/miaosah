package com.jack.miaosha.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CodeMsg {
    private int code;
    private String msg;

    public CodeMsg fillArgs(Object... args) {
        int code = this.code;
        String message = String.format(this.msg, args);
        return new CodeMsg(code, message);
    }

    public static CodeMsg SUCCESS = new CodeMsg(0, "success");
    public static CodeMsg SERVER_ERROR = new CodeMsg(500100, "error");
    public static CodeMsg PASSWORD_EMPTY = new CodeMsg(500211, "登陆密码不能为空");
    public static CodeMsg REQUEST_ILLEGAL = new CodeMsg(110120, "非法请求");
    public static CodeMsg BIND_ERROR = new CodeMsg(500101, "参数校验异常：%s");
    public static CodeMsg MIAO_SHA_FAIL = new CodeMsg(500211, "秒杀失败");
    public static CodeMsg PASSWORD_ERROR = new CodeMsg(500215, "登陆密码错误");
    public static CodeMsg MOBILE_EMPTY = new CodeMsg(500212, "手机号不能为空");
    public static CodeMsg MOBILE_ERROR = new CodeMsg(500213, "手机格式不正确");
    public static CodeMsg MOBILE_NOT_EXIST = new CodeMsg(500214, "手机号码不存在");
    public static CodeMsg USER_SESSION_ERROR = new CodeMsg(500215, "SESSION错误");
    public static CodeMsg MIAO_SHA_OVER = new CodeMsg(500500, "商品已经秒杀完毕");
    public static CodeMsg MIAO_SHA_REPEAT = new CodeMsg(500501, "不能重复秒杀");
    public static CodeMsg ORDER_NOT_EXIST = new CodeMsg(500400, "订单不存在");


}
