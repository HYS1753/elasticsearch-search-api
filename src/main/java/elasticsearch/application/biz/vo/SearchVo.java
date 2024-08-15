package elasticsearch.application.biz.vo;

import elasticsearch.application.adapter.in.dto.SearchSource;
import elasticsearch.application.adapter.in.dto.common.AggregationsDto;
import elasticsearch.application.biz.vo.common.CommonSearchVo;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@NoArgsConstructor
@SuperBuilder
public class SearchVo extends CommonSearchVo {

    // 검색 조건 Params
    String publish;
    String author;

    // 검색 결과 Params
    private AggregationsDto aggregations;
    List<SearchSource> source;
}
