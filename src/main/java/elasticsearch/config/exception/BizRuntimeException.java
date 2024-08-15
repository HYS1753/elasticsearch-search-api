package elasticsearch.config.exception;

/**
 * BizRuntimeException constructor
 */
public class BizRuntimeException extends BaseBizException {

    /**
     * BizRuntimeException
     * @param errorMessage - 에러 메세지
     */
    public BizRuntimeException(String errorMessage) {
        super(errorMessage);
    }

    /**
     * BizRuntimeException
     * @param cause - Throwable
     */
    public BizRuntimeException(Throwable cause) {
        super(cause);
    }
    /**
     * BizRuntimeException
     * @param errorCode - 에러 코드
     * @param errorMessage - 에러 메세지
     */
    public BizRuntimeException(String errorCode, String errorMessage) {
        super(errorCode, errorMessage);
    }

    /**
     * BizRuntimeException
     * @param errorMessage - 에러 메세지
     * @param cause - Throwable
     */
    public BizRuntimeException(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }

    /**
     * BizRuntimeException
     * @param errorCode - 에러 코드
     * @param errorMessage - 에러 메세지
     * @param cause - Throwable
     */
    public BizRuntimeException(String errorCode, String errorMessage, Throwable cause) {
        super(errorCode, errorMessage, cause);
    }
}
