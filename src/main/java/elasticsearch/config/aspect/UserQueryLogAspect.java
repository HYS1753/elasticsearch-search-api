package elasticsearch.config.aspect;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import elasticsearch.common.utils.CommonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * AOP 를 활용해 log4j2 의 kafka appender 로 사용자 쿼리 로그를 전송합니다.
 */
@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class UserQueryLogAspect {
    private final CommonUtils utils;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Pointcut("within(elasticsearch.application.adapter.in.web.Rest*)")
    public void targetController() { }

    @Around("targetController()")
    public Object aroundSearchRestController(ProceedingJoinPoint joinPoint) {
        Object methodResult = null;
        try {
            methodResult = joinPoint.proceed();
        } catch (Throwable e) {
            log.error("UserQueryLoggingAspect throwable error: " + e);
        }

        if (Objects.nonNull(methodResult)) {
            Map<String, Object> req = (joinPoint.getArgs().length > 0) ? objectToMap(joinPoint.getArgs()[0]) : new HashMap<String, Object>();
            Map<String, Object> res = objectToMap(methodResult);
            logQuery(req, res);
        }

        return methodResult;
    }

    private String removeListFormat(String listStyleString) {
        return listStyleString.replaceAll("^\\[|\\]$", "");
    }

    private Map<String, Object> objectToMap(Object source) {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> convertMap = objectMapper.convertValue(source, Map.class);
        return convertMap;
    }

    private Map<String, Object> setDefaultQueryInfo(Map<String, Object> userQuery, Map<String, Object> result) {
        Map<String, Object> resultData = objectToMap(result.get("body"));
        Long totalSize = (Long) resultData.get("totalSize");
        if (Objects.nonNull(totalSize)) {
            userQuery.put("totalSize", totalSize);
        }

        OffsetDateTime now = OffsetDateTime.now(ZoneId.of("Asia/Seoul"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        userQuery.put("timestamp", now.format(formatter));
        userQuery.put("requestUrl", utils.getRequestURI());
        return userQuery;
    }

    private void logQuery(Map<String, Object> req, Map<String, Object> res) {
        try {
            Map<String, Object> userQuery = setDefaultQueryInfo(req, res);
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            String userQueryString = objectMapper.writeValueAsString(userQuery);
            log.info(removeListFormat(userQueryString));
        } catch (Exception e) {
            log.error("UserQueryLoggingAspect error: " + e);
        }
    }
}

