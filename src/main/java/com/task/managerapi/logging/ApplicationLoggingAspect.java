//package com.task.managerapi.util;
//
//import com.spring.BulkMessageSystem.Models.ErrorLog;
//import com.spring.BulkMessageSystem.Models.LogRequests;
//import com.spring.BulkMessageSystem.Rpositories.ErrorLogRepository;
//import com.spring.BulkMessageSystem.Rpositories.LogRepositories.TemplateActivityLogRepository;
//import com.spring.BulkMessageSystem.Rpositories.TemplateRepository;
//import com.spring.BulkMessageSystem.Srevics.ActivityLogService;
//import com.spring.BulkMessageSystem.dto.responses.ErrorResponse;
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Before;
//import org.aspectj.lang.annotation.Pointcut;
//import org.aspectj.lang.reflect.MethodSignature;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.AnonymousAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//
//import javax.servlet.http.HttpServletRequest;
//import java.lang.reflect.Method;
//import java.net.InetAddress;
//import java.net.UnknownHostException;
//import java.time.LocalDateTime;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Objects;
//import java.util.logging.Logger;
//
//@Aspect
//@Configuration
//public class ApplicationLoggingAspect {
//
//    private final Logger logger = Logger.getLogger(ApplicationLoggingAspect.class.getName());
//
//    @Autowired
//    ActivityLogService activityLogService;
//
//    @Autowired
//    TemplateRepository templateRepository;
//
//    @Autowired
//    ErrorLogRepository errorLogRepository;
//
//    @Pointcut("@annotation(com.spring.BulkMessageSystem.Extra.annotation.Log)")
//    public void pointcut() {
//    }
//
//    // @Around("pointcut()")
//    @Around("execution(* com.spring.BulkMessageSystem.Controllers..*.*(..))")
//    public Object logApiMethods(ProceedingJoinPoint joinPoint) throws Throwable {
//        Object result;
//        long beginTime = System.currentTimeMillis();
//        Throwable throwable = null;
//
//        try {
//            result = joinPoint.proceed();
//        } catch (Throwable e) {
//            throwable = e;
//            result = handleApiException(e);
//        }
//
//        saveLog(joinPoint, beginTime, throwable);
//        return result;
//    }
//
//    private ResponseEntity<Object> handleApiException(Throwable e) {
//        ErrorLog errorLog = new ErrorLog();
//        errorLog.setErrorMessage(e.getMessage());
//        errorLog.setErrorTime(LocalDateTime.now());
//        errorLogRepository.save(errorLog);
//
//        ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), errorLog.getId());
//        return new ResponseEntity<>(errorResponse, HttpStatus.FAILED_DEPENDENCY);
//    }
//
//    private void saveLog(ProceedingJoinPoint joinPoint, long time, Throwable throwable) {
//        try {
//            if (joinPoint instanceof TemplateActivityLogRepository) {
//                templateRepository = (TemplateRepository) joinPoint.getThis();
//            }
//
//            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
//            Method method = signature.getMethod();
//            LogRequests logRequests = new LogRequests();
//            String className = joinPoint.getTarget().getClass().getName();
//
//            String methodName = signature.getName();
//            logRequests.setMethod(className + "." + methodName + "()");
//            Object[] args = joinPoint.getArgs();
//            LocalVariableTableParameterNameDiscoverer u = new LocalVariableTableParameterNameDiscoverer();
//            String[] paramNames = u.getParameterNames(method);
//            Map<String, Object> getParams = new HashMap<>();
//            if (args != null && paramNames != null) {
//                StringBuilder params = new StringBuilder();
//                for (int i = 0; i < args.length; i++) {
//                    params.append("  ").append(paramNames[i]).append(": ").append(args[i]);
//                    if (paramNames[i].contains("id")) {
//                        getParams.put("id", args[i]);
//                    }
//                    if (paramNames[i].equalsIgnoreCase("editTemplateRequest")) {
//                        getParams.put(paramNames[i], args[i]);
//                    }
//                }
//                logRequests.setParams(params.toString());
//            }
//
//            if (getParams.containsKey("id")) {
//                logRequests.setEntityId((Long) getParams.get("id"));
//            }
//
//            if (!(authentication instanceof AnonymousAuthenticationToken)) {
//                String currentUserName = authentication.getName();
//                logRequests.setUpdatedBy(currentUserName);
//            }
//
//            HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
//            logRequests.setEndPoint(request.getServletPath());
//
//            String IPAddress = getIPAddress(request);
//
//            if (throwable != null) {
//                logRequests.setError_message(throwable.getMessage());
//            } else {
//                logRequests.setError_message(null);
//            }
//
//            logRequests.setActionTime(new Date());
//            logRequests.setOperation(request.getMethod());
//            logRequests.setIpAddress(IPAddress);
//            activityLogService.saveLogRequests(logRequests);
//        } catch (Throwable e) {
//            ErrorLog errorLog = new ErrorLog();
//            errorLog.setErrorMessage(e.getMessage());
//            errorLog.setErrorTime(LocalDateTime.now());
//            errorLogRepository.save(errorLog);
//        }
//    }
//
//    @Before("execution(* com.spring.BulkMessageSystem.Rpositories.*.*(..))")
//    public void logMethodCall(JoinPoint jp) {
//        String methodName = jp.getSignature().getName();
//        logger.info("Before " + methodName);
//    }
//
//    public String getIPAddress(HttpServletRequest request) {
//        String IPAddress = request.getHeader("X-Forwarded-For");
//        if (IPAddress == null || IPAddress.isEmpty() || "unknown".equalsIgnoreCase(IPAddress)) {
//            IPAddress = request.getHeader("X-Real-IP");
//        }
//        if (IPAddress == null || IPAddress.isEmpty() || "unknown".equalsIgnoreCase(IPAddress)) {
//            IPAddress = request.getRemoteAddr();
//        }
//
//        if (IPAddress != null && IPAddress.contains(",")) {
//            IPAddress = IPAddress.split(",")[0].trim();
//        }
//
//        if ("127.0.0.1".equals(IPAddress) || "localhost".equalsIgnoreCase(IPAddress)) {
//            try {
//                InetAddress localHost = InetAddress.getLocalHost();
//                IPAddress = localHost.getHostAddress();
//            } catch (UnknownHostException e) {
//                IPAddress = "Unknown";
//            }
//        }
//        return IPAddress;
//    }
//}