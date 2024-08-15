package elasticsearch.application.biz.vo.common;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
public class CommonSearchVo {

    // 검색 조건 Params
    private String query;
    private Integer page;
    private Integer pageSize;

    // 검색 결과 Params (query 공통 사용)
    Long totalSize;
    Long realSize;
}
