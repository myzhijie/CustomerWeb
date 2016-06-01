package com.jingsky.customer.web.util;

import com.jingsky.customer.util.util.ExceptionUtil;
import com.jingsky.customer.util.util.ResultMessage;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * spring mvc global exception handler
 * @author pg
 */
public class ExceptionHandler implements HandlerExceptionResolver {
    private final Logger log = Logger.getLogger(this.getClass());

    @Override
    public ModelAndView resolveException(HttpServletRequest request,
                                         HttpServletResponse response, Object handler, Exception ex) {
        String uri = request.getRequestURI();
        log.error(uri+" error:",ex);

        ResultMessage resultMessage = new ResultMessage();
        resultMessage.put("status","failure");
        resultMessage.put("ex", ExceptionUtil.getStackTrace(ex));
        resultMessage.put("requestCode",request.getAttribute("requestCode"));
        request.setAttribute("resultMessage",new JSONObject(resultMessage));

        return new ModelAndView("500");
    }

}
