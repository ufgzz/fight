package com.oofgz.fight.aspect;

import com.mongodb.BasicDBObject;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.jboss.logging.Logger;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Aspect
@Order(1)
@Log4j2
@Component
public class WebLogAspect {

    private Logger logger = Logger.getLogger(getClass());
    private ThreadLocal<Long> startTime = new ThreadLocal<>();

    @Pointcut("execution(public * com.oofgz.fight.controller..*.*(..))")
    public void webLog(){}

    /**
     * 在切入点前的操作，按order的值由小到大执行
     * @param joinPoint
     */
    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) {
        startTime.set(System.currentTimeMillis());

        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        // 记录下请求内容
        BasicDBObject logInfo = getBasicDBObject(request, joinPoint);
        log.info(logInfo);
        logger.info(logInfo);
    }

    private BasicDBObject getBasicDBObject(HttpServletRequest request, JoinPoint joinPoint) {
        // 基本信息
        BasicDBObject r = new BasicDBObject();
        r.append("requestURL", request.getRequestURL().toString());
        r.append("requestURI", request.getRequestURI());
        r.append("queryString", request.getQueryString());
        r.append("remoteAddr", request.getRemoteAddr());
        r.append("remoteHost", request.getRemoteHost());
        r.append("remotePort", request.getRemotePort());
        r.append("localAddr", request.getLocalAddr());
        r.append("localName", request.getLocalName());
        r.append("method", request.getMethod());
        r.append("headers", getHeadersInfo(request));
        r.append("parameters", request.getParameterMap());
        r.append("classMethod", joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        r.append("args", Arrays.toString(joinPoint.getArgs()));
        return r;
    }


    private Map<String, String> getHeadersInfo(HttpServletRequest request) {
        Map<String, String> map = new HashMap<>();
        Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            map.put(key, value);
        }
        return map;
    }


    /**
     * 在切入点后的操作，按order的值由大到小执行
     * @param ret
     */
    @AfterReturning(returning = "ret", pointcut = "webLog()")
    public void doAfterReturning(Object ret) {
        // 处理完请求，返回内容
        log.info("RESPONSE : " + ret);
        log.info("SPEND TIME : " + (System.currentTimeMillis() - startTime.get()));
    }




}
