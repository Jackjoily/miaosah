package com.jack.miaosha.controller;

import com.jack.miaosha.domain.MiaoShaUser;
import com.jack.miaosha.result.Result;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserController {


    @RequestMapping("/info")
    @ResponseBody
    public Result<MiaoShaUser> getInf(MiaoShaUser miaoShaUser) {
        return Result.success(miaoShaUser);
    }

}
