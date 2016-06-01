package com.jingsky.customer.web.util;

import com.alibaba.fastjson.JSONObject;
import com.jingsky.customer.util.util.StringUtil;
import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;


/**
 * For printing logs
 * @author pg
 */
public class AllFilter implements Filter {
    private static final Logger log = Logger.getLogger(AllFilter.class);

    public void init(FilterConfig arg0) throws ServletException {
        log.info("AllFilter init！");
    }

    public void destroy() {

    }

    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        //Clear Cache encoding settings
        clearCacheAndSetEnCoding(request, response);
        String uri = ((HttpServletRequest) request).getRequestURI();
        //Static resource skip
        if (chkStaticResource(uri)) {
            chain.doFilter(request, response);
            return;
        }

        //Parameters transmitted from splicing
        logRequestInfo((HttpServletRequest) request, uri);

        chain.doFilter(request, response);
    }

    /**
     * Print request information
     */
    private void logRequestInfo(HttpServletRequest request, String uri) {
        JSONObject jo = new JSONObject();
        Enumeration paras = request.getParameterNames();
        while (paras.hasMoreElements()) {
            Object name = paras.nextElement();
            jo.put(name.toString(), request.getParameter(name.toString()));
        }
        log.info("IP:" + getRemoteAddrIp(request) + ",requestCode:"+request.getAttribute("requestCode")+",URI:" + uri + ",parameters:" + jo.toJSONString());
    }

    /**
     * chkStaticResource
     * @param uri
     * @return yes true，no false
     */
    private boolean chkStaticResource(String uri) {
        return uri.contains(".jpg") || uri.contains(".png") || uri.contains(".css") || uri.contains(".gif")
                || uri.contains(".js") || uri.contains(".woff") || uri.contains(".swf") || uri.contains(".ico");
    }

    /**
     * clearCacheAndSetEnCoding
     * @param response
     */
    private void clearCacheAndSetEnCoding(ServletRequest request, ServletResponse response) throws UnsupportedEncodingException {
        request.setCharacterEncoding("utf-8");
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setHeader("Pragma", "No-cache");
        httpResponse.setHeader("Cache-Control", "no-cache");
        httpResponse.setDateHeader("Expires", 0);
        httpResponse.setContentType("text/html;charset=utf-8");
    }

    /**
     * Get real ip address
     * @param request
     * @return
     */
    public static String getRemoteAddrIp(HttpServletRequest request) {
        String value = request.getHeader("X-Real-IP");
        String ipFromNginx = !StringUtil.isEmpty(value) && !"unknown".equalsIgnoreCase(value) ? value : "";
        return StringUtil.isEmpty(ipFromNginx) ? request.getRemoteAddr() : ipFromNginx;
    }

}
