package elasticsearch.application.adapter.out.es.common;

import elasticsearch.application.adapter.in.dto.common.AggregationsDto;
import elasticsearch.application.adapter.out.es.indices.EntityMapper;
import elasticsearch.application.adapter.out.es.utils.EsAggregationParseUtils;
import lombok.Data;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class EsSearchResult {
    private String query;
    private Long totalSize;
    private Long realSize;
    private AggregationsDto aggregations;
    private List<?> source;

    public void setSearchResult(String query, SearchHits<?> searchHits, EntityMapper mapper) {
        EsAggregationParseUtils aggParseUtils = new EsAggregationParseUtils();
        AggregationsDto resultAggs = aggParseUtils.aggregationsResultParser(searchHits.getAggregations());
        List<?> resultSource = searchHits.stream().map(SearchHit::getContent).collect(Collectors.toList());

        this.query = query;
        totalSize = searchHits.getTotalHits();
        realSize = (long) searchHits.getSearchHits().size();
        aggregations = resultAggs;
        source = (List<?>) mapper.convert(resultSource);
    }
}