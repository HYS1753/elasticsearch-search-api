package elasticsearch.config.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Rest API 컨트롤러에서 throw 한 Exception에 대한 공통 처리
 */
@Slf4j
@RestControllerAdvice
public class RestControllerExceptionHandler {

    @ExceptionHandler({BizRuntimeException.class})
    @ResponseBody
    public ResponseEntity<String> handleException(BizRuntimeException e) {
        log.error("BizRuntime Error Code : " + e.getErrorMessage(), e);

        String message = "서버 내부 오류 : " + e.getErrorMessage();
        return ResponseEntity.internalServerError().body(message);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ResponseEntity<String> handleException(Exception e, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String msgId = null, msg = null;
        if (e instanceof BaseBizException) {
            BaseBizException base = (BaseBizException) e;
            msgId = base.getErrorCode();
            msg = base.getMessage();
        } else {
            msgId = "";
            msg = e.getMessage();
        }

        String errorMessage = "서버 내부 오류 : 에러 코드(" + msgId + "), 에러 메시지(" + msg + ")";
        return ResponseEntity.internalServerError().body(errorMessage);
    }

}