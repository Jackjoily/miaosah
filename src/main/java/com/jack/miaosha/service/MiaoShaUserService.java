package com.jack.miaosha.service;

import com.jack.miaosha.dao.MiaoShaUserDao;
import com.jack.miaosha.domain.MiaoShaUser;
import com.jack.miaosha.exception.GlobleException;
import com.jack.miaosha.redis.MiaoShaUserKey;
import com.jack.miaosha.redis.RedisService;
import com.jack.miaosha.result.CodeMsg;
import com.jack.miaosha.util.MD5Utils;
import com.jack.miaosha.util.UUIDUtil;
import com.jack.miaosha.vo.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Service
public class MiaoShaUserService {

    public static final String COOK1_NAME_TOKEN = "token";

    @Autowired
    MiaoShaUserDao miaoShaUserDao;
    @Autowired
    RedisService redisService;

    public MiaoShaUser getById(long id) {
//取出一个缓存
        MiaoShaUser miaoShaUser = redisService.get(MiaoShaUserKey.getById, "" + id, MiaoShaUser.class);
        if (miaoShaUser != null) {
            return miaoShaUser;
        }

        miaoShaUser = miaoShaUserDao.getById(id);
        if (miaoShaUser != null) {
            redisService.set(MiaoShaUserKey.getById, "" + id, miaoShaUser);
        }
        return miaoShaUser;

    }

    public boolean updatePassword(String token, long id, String password) {
        //取出user对象getById
        MiaoShaUser byId = getById(id);
        if (byId == null) {
            throw new GlobleException(CodeMsg.MOBILE_NOT_EXIST);
        }
        MiaoShaUser tuBeUpdate = new MiaoShaUser();
        tuBeUpdate.setId(id);
        tuBeUpdate.setPassword(MD5Utils.inputPassToDbPass(password, byId.getSalt()));
        miaoShaUserDao.update(tuBeUpdate);
        //处理缓存
        byId.setPassword(tuBeUpdate.getPassword());
        redisService.delete(MiaoShaUserKey.getById, "" + id);
        redisService.set(MiaoShaUserKey.token, token, byId);
        return true;
    }


    public boolean login(HttpServletResponse response, LoginVo loginVo) {
        if (loginVo == null) {
            throw new GlobleException(CodeMsg.SERVER_ERROR);
        }
        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();
        MiaoShaUser byId = getById(Long.parseLong(mobile));
        if (byId == null) {
            throw new GlobleException(CodeMsg.MOBILE_NOT_EXIST);
        }
        String dbPass = byId.getPassword();
        String salt = byId.getSalt();
        String calcPass = MD5Utils.PassFormToDbPass(password, salt);
        if (!calcPass.equals(dbPass)) {
            throw new GlobleException(CodeMsg.PASSWORD_ERROR);
        }
        //登陆成功之后生成cookie
        String token = UUIDUtil.uuid();
        addCookie(byId, token, response);
        return true;
    }

    public MiaoShaUser getByToken(HttpServletResponse response, String token) {
        if (StringUtils.isEmpty(token)) {
            return null;
        } else {
            MiaoShaUser miaoShaUser = redisService.get(MiaoShaUserKey.token, token, MiaoShaUser.class);
            //延长有效期
            if (miaoShaUser != null) {
                addCookie(miaoShaUser, token, response);
            }
            return miaoShaUser;
        }
    }

    private void addCookie(MiaoShaUser user, String token, HttpServletResponse response) {
        redisService.set(MiaoShaUserKey.token, token, user);
        Cookie cookie = new Cookie(COOK1_NAME_TOKEN, token);
        cookie.setMaxAge(MiaoShaUserKey.token.expireSeconds());
        cookie.setPath("/");
        response.addCookie(cookie);
    }

}
