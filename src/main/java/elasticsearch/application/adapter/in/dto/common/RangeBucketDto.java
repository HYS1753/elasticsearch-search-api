package elasticsearch.application.adapter.in.dto.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RangeBucketDto {

    @Schema(description = "range 범위 값", name = "key")
    private String key;

    @Schema(description = "range 시작 값", name = "from")
    private double from;

    @Schema(description = "range 종료 값", name = "to")
    private double to;

    @Schema(description = "집계 결과 문서 수", name = "docCount")
    private Long docCount;

}
