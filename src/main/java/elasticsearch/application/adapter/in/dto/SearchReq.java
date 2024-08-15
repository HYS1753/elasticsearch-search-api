package elasticsearch.application.adapter.in.dto;

import elasticsearch.application.adapter.in.dto.common.CommonSearchReq;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class SearchReq extends CommonSearchReq {

    @Schema(description = "출판사 명 필터링"
            , name = "publish", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String publish;

    @Schema(description = "저자 명 필터링"
            , name = "author", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String author;
}
