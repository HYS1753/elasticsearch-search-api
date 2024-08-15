package elasticsearch.application.adapter.out.es.indices;

import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.IndexedIndexName;

@AllArgsConstructor
@Document(indexName = "book-test")
public class BookEntity {
    @Id
    private String id;

    @IndexedIndexName
    private String indexName;

    @Field(name= "BOOK_NAME", type = FieldType.Text)
    private String bookName;

    @Field(name= "PUBLISHER", type = FieldType.Text)
    private String publisher;

    @Field(name= "BARCODE", type = FieldType.Keyword)
    private String barcode;

    @Field(name= "PRICE", type = FieldType.Double)
    private Double price;

    @Field(name= "RLSE_DATE", type = FieldType.Date)
    private String rlseDate;

    @Field(name= "AUTHOR", type = FieldType.Text)
    private String author;
}

