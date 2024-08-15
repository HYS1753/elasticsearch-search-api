package elasticsearch.application.adapter.in.dto.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class RangeAggregateDto {

    @Schema(description = "Range 집계 명", name = "name")
    private String name;

    @Schema(description = "Range 집계 명", name = "buckets")
    private List<RangeBucketDto> buckets;

}

