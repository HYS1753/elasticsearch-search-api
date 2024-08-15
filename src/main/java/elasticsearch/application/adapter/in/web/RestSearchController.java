package elasticsearch.application.adapter.in.web;

import elasticsearch.application.adapter.in.dto.SearchReq;
import elasticsearch.application.adapter.in.dto.SearchRes;
import elasticsearch.application.biz.port.in.SearchInPort;
import elasticsearch.common.enumeration.ApiResponseContents;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Elasticsearch Search API", description = "Elasticsearch 검색 API")
@RequestMapping("/api/v1/")
public class RestSearchController {

    private final SearchInPort service;

    @Operation(summary = "검색", description = "검색 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200"
                    , description = ApiResponseContents.MESSAGE_200_PREFIX + "SearchRes" + ApiResponseContents.MESSAGE_200_POSTFIX
                    , content = {@Content(mediaType = "application/json", schema = @Schema(implementation = SearchRes.class)) }),
            @ApiResponse(responseCode = "404"
                    , description = ApiResponseContents.MESSAGE_404),
            @ApiResponse(responseCode = "500"
                    , description = ApiResponseContents.MESSAGE_500)
    })
    @GetMapping(value = "/search")
    public ResponseEntity<SearchRes> search(@Valid @Parameter(name = "검색 파라미터", required = true) @ParameterObject SearchReq searchReq) {
        return ResponseEntity.ok().body(service.search(searchReq));
    }
}
