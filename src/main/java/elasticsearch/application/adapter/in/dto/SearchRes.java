package elasticsearch.application.adapter.in.dto;

import elasticsearch.application.adapter.in.dto.common.AggregationsDto;
import elasticsearch.application.adapter.in.dto.common.CommonSearchRes;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class SearchRes extends CommonSearchRes {

    @Schema(description = "검색 Aggregations 결과", name = "aggregations")
    private AggregationsDto aggregations;

    @Schema(description = "검색 결과 List", name = "source")
    private List<SearchSource> source;
}
