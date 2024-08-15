package elasticsearch.application.adapter.in.dto.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CommonSearchRes {

    @Schema(description = "검색 쿼리", name = "query")
    private String query;

    @Schema(description = "총 검색 결과 개수", name = "totalSize")
    private Long totalSize;

    @Schema(description = "실 검색 결과 개수", name = "realSize")
    private Long realSize;
}
