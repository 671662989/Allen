package com.leyou.filter;

import com.leyou.auth.utils.JwtUtils;
import com.leyou.common.utils.CookieUtils;
import com.leyou.config.FilterProperties;
import com.leyou.config.JwtProperties;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * create by: cdy
 * description:$
 * create time: $ $
 * <p>
 * $
 *
 * @return $
 */
@Component
@EnableConfigurationProperties({JwtProperties.class,FilterProperties.class})
public class LoginFilter extends ZuulFilter {

    @Autowired
    private JwtProperties prop;

    @Autowired
    private FilterProperties filterProperties;

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginFilter.class);
    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 5;
    }

    @Override
    public boolean shouldFilter() {
        List<String> allowPaths = filterProperties.getAllowPaths();
        RequestContext context = RequestContext.getCurrentContext();
        String requestURI = context.getRequest().getRequestURI();
        for (String path :allowPaths){
            if(requestURI.startsWith(path)){
                return false;
            }
        }

        return true;
    }

    @Override
    public Object run() throws ZuulException {

            RequestContext context = RequestContext.getCurrentContext();
            HttpServletRequest request=context.getRequest();
        String cookieValue = CookieUtils.getCookieValue(request, prop.getCookieName());
        try {
            JwtUtils.getInfoFromToken(cookieValue,prop.getPublicKey());
        } catch (Exception e) {
            context.setSendZuulResponse(false);
            context.setResponseStatusCode(HttpStatus.FORBIDDEN.value());
            LOGGER.error("非法访问，地址{}"+ request.getRemoteHost(),e);

        }
        return null;
    }
}
