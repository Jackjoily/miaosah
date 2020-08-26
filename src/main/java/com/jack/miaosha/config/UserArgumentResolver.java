package com.jack.miaosha.config;

import com.jack.miaosha.domain.MiaoShaUser;
import com.jack.miaosha.service.MiaoShaUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class UserArgumentResolver implements HandlerMethodArgumentResolver {
    @Autowired
    MiaoShaUserService miaoShaUserService;

    /**
     * 自定义的参数解析器，每一个Request请求中，对参数的中的MiaoShaUse进行操作，如果没有含有这个参数，则不经过这个解析器进行处理，
     * 如果经有这个参树过这个解析器
     *
     * @param methodParameter
     * @return
     */
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        Class<?> clazz = methodParameter.getParameterType();
        return clazz == MiaoShaUser.class;
    }

    /**
     * 进行到这个方法中，则说明参数中含有MiaoShaUser这个对象，则需要从redis取出User这个参数
     * 如果没有这个参数的时候，则会在前端进行判断
     *
     * @param methodParameter
     * @param modelAndViewContainer
     * @param nativeWebRequest
     * @param webDataBinderFactory
     * @return
     * @throws Exception
     */

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        HttpServletRequest request = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
        HttpServletResponse response = nativeWebRequest.getNativeResponse(HttpServletResponse.class);

        String paramToken = request.getParameter(MiaoShaUserService.COOK1_NAME_TOKEN);
        String cookieValue = getCookkieValue(request, MiaoShaUserService.COOK1_NAME_TOKEN);
        if (StringUtils.isEmpty(cookieValue) && StringUtils.isEmpty(paramToken)) {
            return null;
        }
        String token = StringUtils.isEmpty(paramToken) ? cookieValue : paramToken;
        MiaoShaUser user = miaoShaUserService.getByToken(response, token);
        return user;
    }

    private String getCookkieValue(HttpServletRequest request, String cook1NameToken) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length == 0) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(MiaoShaUserService.COOK1_NAME_TOKEN)) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
