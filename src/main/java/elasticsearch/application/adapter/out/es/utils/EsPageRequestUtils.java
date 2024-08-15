package elasticsearch.application.adapter.out.es.utils;

import elasticsearch.config.exception.BizRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;

import java.util.Objects;

/**
 * Elasticsearch PageRequest Query 생성 Utils
 */
@Slf4j
public class EsPageRequestUtils {

    private static final Integer DEFAULT_PAGE_NUMBER = 1;
    private static final Integer DEFAULT_PAGE_SIZE = 20;
    private static final Integer MAX_RESULT_WINDOW = 10000;
    private static final String MAX_RESULT_WINDOW_ERROR_STRING = "검색 가능 Document 크기 초과. (max = " + MAX_RESULT_WINDOW + ")";
    private PageRequest pageRequest;

    public PageRequest getPageRequest() {
        return pageRequest;
    }

    public void setPageRequest(Integer page, Integer pageSize) {
        try {
            page = Objects.nonNull(page) ? page : DEFAULT_PAGE_NUMBER;
            pageSize = Objects.nonNull(pageSize) ? pageSize : DEFAULT_PAGE_SIZE;

            if (isMaxResultWindowOver(page, pageSize)) {
                throw new Exception(MAX_RESULT_WINDOW_ERROR_STRING);
            } else {
                Integer realPage = (page < 0) ? 0 : page - 1;
                pageRequest = PageRequest.of(realPage, pageSize);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new BizRuntimeException("페이지 쿼리 생성 실패", e);
        }
    }

    private boolean isMaxResultWindowOver(Integer page, Integer pageSize) {
        Integer windowSize = page * pageSize;
        return windowSize > MAX_RESULT_WINDOW ? true : false;
    }

}
