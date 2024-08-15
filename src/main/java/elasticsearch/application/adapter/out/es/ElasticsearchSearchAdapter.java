package elasticsearch.application.adapter.out.es;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import elasticsearch.application.adapter.in.dto.SearchSource;
import elasticsearch.application.adapter.out.es.common.EsSearchResult;
import elasticsearch.application.adapter.out.es.indices.BookEntity;
import elasticsearch.application.adapter.out.es.indices.EntityMapper;
import elasticsearch.application.adapter.out.es.utils.*;
import elasticsearch.application.biz.port.out.SearchOutPort;
import elasticsearch.application.biz.vo.SearchVo;
import elasticsearch.common.utils.ElasticsearchLogUtils;
import elasticsearch.config.elasticsearch.EsConnectionConfig;
import elasticsearch.config.exception.BizRuntimeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.SourceFilter;
import org.springframework.stereotype.Repository;

import java.awt.print.Book;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@RequiredArgsConstructor
@Repository
public class ElasticsearchSearchAdapter implements SearchOutPort {

    private final ElasticsearchLogUtils logUtils;
    private final ElasticsearchOperations esOperations;
    private final ElasticsearchClient esClient;
    private final EntityMapper<List<BookEntity>, List<SearchSource>> bookEntityMapper;

    private static final List<String> SOURCE_INCLUDES = new ArrayList<>();
    private static final List<String> SOURCE_EXCLUDES = new ArrayList<>();

    @Override
    public SearchVo search(SearchVo data) {
        /* variables */
        String keyword = data.getQuery();
        Integer page = data.getPage();
        Integer pageSize = data.getPageSize();

        /* make query */
        // source filter
        String[] sourceIncludes = SOURCE_INCLUDES.stream().toArray(String[]::new);
        String[] sourceExcludes = SOURCE_EXCLUDES.stream().toArray(String[]::new);
        SourceFilter sourceFilter = new FetchSourceFilter(sourceIncludes, sourceExcludes);

        // 검색 쿼리
        Query boolQuery = makeBoolQuery(data)._toQuery();
        FunctionScoreQuery searchQuery = makeFunctionScoreQuery(boolQuery);

        // 정렬 쿼리

        // 페이징 쿼리
        EsPageRequestUtils pageRequest = new EsPageRequestUtils();
        pageRequest.setPageRequest(page, pageSize);

        // 집계 쿼리 생성
        EsAggregationUtils priceAggregation = new EsAggregationUtils();
        priceAggregation.setRangeAggregation("PRICE", 50000, 10000);

        // 전체 쿼리 생성
        NativeQuery query = new NativeQueryBuilder()
                .withTrackTotalHits(true)
                .withSourceFilter(sourceFilter)
                .withQuery(searchQuery._toQuery())
                .withPageable(pageRequest.getPageRequest())
                .withAggregation("PRICE_AGG", priceAggregation.getAggregation())
                .build();

        logUtils.logQueryDSL(query);

        // 검색 실행
        SearchHits<BookEntity> searchHits = null;
        try {
            searchHits = esOperations.search(query, BookEntity.class);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new BizRuntimeException("Elasticsearch search Query Execute Error. (" + e.toString() + ")");
        }

        // 검색 결과 파싱
        EsSearchResult searchResult = new EsSearchResult();
        searchResult.setSearchResult(keyword, searchHits, bookEntityMapper);

        return SearchVo.builder()
                .query(searchResult.getQuery())
                .totalSize(searchResult.getTotalSize())
                .realSize(searchResult.getRealSize())
                .aggregations(searchResult.getAggregations())
                .source((List<SearchSource>) searchResult.getSource())
                .build();
    }

    private FunctionScoreQuery makeFunctionScoreQuery(Query query) {
        EsFunctionScoreQueryUtils functionScoreQuery = new EsFunctionScoreQueryUtils();

        functionScoreQuery.setQuery(query);
        // score mode 설정(default multiply -> sum)
        functionScoreQuery.setScoreMode(FunctionScoreMode.Sum);
        // boost mode 설정(default multiply)
        functionScoreQuery.setBoostMode(FunctionBoostMode.Replace);
        // Similarity score normalize
        functionScoreQuery.setScriptScoreFunction("_score");

        return functionScoreQuery.getFunctionScoreQuery();
    }

    private BoolQuery makeBoolQuery(SearchVo data) {
        EsBoolQueryUtils boolQuery = new EsBoolQueryUtils();

        String keyword = data.getQuery();
        String publishName = data.getPublish();
        String authorName = data.getAuthor();
        List<String> mustFields = List.of("BOOK_NAME", "AUTHOR", "PUBLISHER", "BARCODE");

        /* MUST QUERY */
        boolQuery.setMustMultiMatchAndOpQuery(mustFields, keyword, 1F);

        /* FILTER QUERY */
        if (StringUtils.isNotBlank(publishName)) {
            boolQuery.setFilterTermQuery("PUBLISHER", publishName);
        }
        if (StringUtils.isNotBlank(authorName)) {
            boolQuery.setFilterTermQuery("AUTHOR", authorName);
        }

        /* SHOULD QUERY */
        boolQuery.setShouldMatchPhrase("BOOK_NAME", keyword, 3, 1F);
        boolQuery.setMinimumShouldMatch("0");

        return boolQuery.getBoolQuery();
    }


}
