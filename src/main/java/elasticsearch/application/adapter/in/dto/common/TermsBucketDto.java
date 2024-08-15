package elasticsearch.application.adapter.in.dto.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TermsBucketDto {

    @Schema(description = "집계 키워드", name = "key")
    private String key;

    @Schema(description = "집계 결과 문서 수", name = "docCount")
    private Long docCount;

}