package elasticsearch.application.adapter.in.dto.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class TermsAggregateDto {

    @Schema(description = "Terms 집계 명", name = "name")
    private String name;

    @Schema(description = "Terms 집계 결과 bucket", name = "buckets")
    private List<TermsBucketDto> buckets;
}
