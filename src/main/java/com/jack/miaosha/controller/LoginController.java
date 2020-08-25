package com.jack.miaosha.controller;

import com.jack.miaosha.result.CodeMsg;
import com.jack.miaosha.result.Result;
import com.jack.miaosha.service.MiaoShaUserService;
import com.jack.miaosha.util.ValidatorUtil;
import com.jack.miaosha.vo.LoginVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/user")
@Slf4j
public class LoginController {
    @Autowired
    MiaoShaUserService miaoShaUserService;

    @RequestMapping("/to_login")
    public String toLogin() {
        return "login";
    }

    @RequestMapping("/do_login")
    @ResponseBody
    public Result<Boolean> doLogin(HttpServletResponse response, @Validated LoginVo loginVo) {
        log.info(loginVo.toString());
        String passInput = loginVo.getPassword();
        String mobile = loginVo.getMobile();
        if (!ValidatorUtil.isMobile(mobile)) {
            return Result.error(CodeMsg.MOBILE_ERROR);
        }
        boolean login = miaoShaUserService.login(response, loginVo);
        return Result.success(true);
    }
}
