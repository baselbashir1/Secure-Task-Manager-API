package com.task.managerapi.logging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.task.managerapi.models.RequestLog;
import com.task.managerapi.repositories.RequestLogRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.server.ResponseStatusException;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class ApplicationLoggingAspect {

    private final ObjectMapper objectMapper;
    private final RequestLogRepository requestLogRepository;

    @Around("execution(* com.task.managerapi.controllers..*.*(..))")
    public Object logMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        String endpoint = request.getRequestURI();
        String methodSignature = joinPoint.getSignature().toShortString();
        String ipAddress = getIPAddress(request);

        RequestLog requestLog = new RequestLog();
        requestLog.setEndpoint(endpoint);
        requestLog.setMethod(methodSignature);
        requestLog.setIpAddress(ipAddress);

        Object result;

        try {
            result = joinPoint.proceed();
            requestLog.setStatusCode(getStatusCodeFromResult(result));
            requestLog.setMessage(serializeResult(result));
            log.info("Request processed successfully");
        } catch (Throwable e) {
            requestLog.setStatusCode(getStatusCodeFromException(e));
            requestLog.setMessage("Error: " + e.getMessage());
            log.error("Request processing failed", e);
            throw e;
        } finally {
            try {
                requestLogRepository.save(requestLog);
            } catch (Exception e) {
                log.warn("Failed to save request log", e);
            }
        }

        return result;
    }

    private String serializeResult(Object result) {
        try {
            Object content = result instanceof ResponseEntity ? ((ResponseEntity<?>) result).getBody() : result;
            return objectMapper.writeValueAsString(content);
        } catch (Exception e) {
            return "Serialization error: " + e.getMessage();
        }
    }

    private int getStatusCodeFromResult(Object result) {
        if (result instanceof ResponseEntity) {
            return ((ResponseEntity<?>) result).getStatusCode().value();
        }
        return 200;
    }

    private int getStatusCodeFromException(Throwable e) {
        if (e instanceof ResponseStatusException) {
            return ((ResponseStatusException) e).getStatusCode().value();
        } else if (e instanceof AccessDeniedException) {
            return 403;
        } else if (e instanceof AuthenticationException) {
            return 401;
        } else if (e instanceof MethodArgumentNotValidException) {
            return 400;
        }
        return 500;
    }

    public String getIPAddress(HttpServletRequest request) {
        String IPAddress = request.getHeader("X-Forwarded-For");
        if (IPAddress == null || IPAddress.isEmpty() || "unknown".equalsIgnoreCase(IPAddress)) {
            IPAddress = request.getHeader("X-Real-IP");
        }
        if (IPAddress == null || IPAddress.isEmpty() || "unknown".equalsIgnoreCase(IPAddress)) {
            IPAddress = request.getRemoteAddr();
        }

        if (IPAddress != null && IPAddress.contains(",")) {
            IPAddress = IPAddress.split(",")[0].trim();
        }

        if ("127.0.0.1".equals(IPAddress) || "localhost".equalsIgnoreCase(IPAddress)) {
            try {
                InetAddress localHost = InetAddress.getLocalHost();
                IPAddress = localHost.getHostAddress();
            } catch (UnknownHostException e) {
                IPAddress = "Unknown";
            }
        }
        return IPAddress;
    }
}