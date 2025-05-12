package com.task.managerapi.configurations;

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
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.server.ResponseStatusException;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Optional;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class ApplicationLoggingAspect {

    private final ObjectMapper objectMapper;
    private final RequestLogRepository requestLogRepository;

    @Around("execution(* com.task.managerapi.controllers..*.*(..))")
    public Object logMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        RequestLog requestLog = new RequestLog();
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
                populateCommonFields(requestLog, request, joinPoint);
                requestLogRepository.save(requestLog);
            } catch (Exception e) {
                log.warn("Failed to save request log", e);
            }
        }

        return result;
    }

    private void populateCommonFields(RequestLog requestLog, HttpServletRequest request, ProceedingJoinPoint joinPoint) {
        requestLog.setEndpoint(request.getRequestURI());
        requestLog.setMethod(joinPoint.getSignature().toShortString());
        requestLog.setIpAddress(getClientIPAddress(request));
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
        }
        return 500;
    }

    private String getClientIPAddress(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader("X-Forwarded-For"))
                .or(() -> Optional.ofNullable(request.getHeader("Proxy-Client-IP")))
                .or(() -> Optional.ofNullable(request.getHeader("WL-Proxy-Client-IP")))
                .or(() -> Optional.ofNullable(request.getHeader("HTTP_CLIENT_IP")))
                .or(() -> Optional.ofNullable(request.getHeader("HTTP_X_FORWARDED_FOR")))
                .or(() -> Optional.ofNullable(request.getHeader("X-Real-IP")))
                .or(() -> Optional.ofNullable(request.getRemoteAddr()))
                .map(ip -> ip.split(",")[0].trim())
                .map(this::resolveLocalAddress)
                .orElse("Unknown");
    }

    private String resolveLocalAddress(String ipAddress) {
        if ("127.0.0.1".equals(ipAddress) || "localhost".equalsIgnoreCase(ipAddress)) {
            try {
                return InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException e) {
                log.debug("Failed to resolve localhost address", e);
            }
        }
        return ipAddress;
    }
}