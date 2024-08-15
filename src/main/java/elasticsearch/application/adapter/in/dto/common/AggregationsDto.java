package elasticsearch.application.adapter.in.dto.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AggregationsDto {

    // terms
    @Schema(description = "terms 집계 결과", name = "termsAggregation")
    List<TermsAggregateDto> terms;

    // range
    @Schema(description = "range 집계 결과", name = "rangeAggregation")
    List<RangeAggregateDto> range;

}
