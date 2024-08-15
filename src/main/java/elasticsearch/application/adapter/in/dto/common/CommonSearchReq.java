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
public class CommonSearchReq {

    @Schema(description = "검색 키워드", name = "query", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String query;

    /*
    page param
    0 보다 큰 숫자만 입력 가능
    default = 1
    */
    @Schema(description = "검색 페이지 설정 [page (ex, 1, 2 ...) - 0 보다 큰 숫자 형식 입력]"
            , name = "page", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    //@Pattern(regexp = "^[1-9]\\d*$", message = "Page 는 0보다 큰 숫자 형식으로 입력되어야 합니다.")
    private Integer page;

    /*
    pageSize param
    0 보다 큰 숫자만 입력 가능
    default = 1
    */
    @Schema(description = "검색 페이지 당 노출 상품 수 설정 [page (ex, 1, 2 ...) - 0 보다 큰 숫자 형식 입력]"
            , name = "pageSize", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    //@Pattern(regexp = "^[1-9]\\d*$", message = "PageSize 는 0보다 큰 숫자 형식으로 입력되어야 합니다.")
    private Integer pageSize;
}
