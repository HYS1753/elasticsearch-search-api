package elasticsearch.application.adapter.in.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@Builder
@AllArgsConstructor
public class SearchSource {

    @Schema(description = "ES도큐먼트 ID", name = "id")
    private String id;

    @Schema(description = "도서 명", name = "bookName")
    private String bookName;

    @Schema(description = "출판사", name = "publisher")
    private String publisher;

    @Schema(description = "저자", name = "author")
    private String author;

    @Schema(description = "바코드", name = "barcode")
    private String barcode;

    @Schema(description = "정가", name = "price")
    private String price;

    @Schema(description = "출간일", name = "rlseDate")
    private String rlseDate;

}
