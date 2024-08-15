package elasticsearch.application.adapter.out.es.utils;

import co.elastic.clients.elasticsearch._types.aggregations.*;
import elasticsearch.application.adapter.in.dto.common.*;
import elasticsearch.application.adapter.out.es.common.RangeAggregationMapper;
import elasticsearch.application.adapter.out.es.common.TermsAggregateMapper;
import elasticsearch.application.adapter.out.es.indices.EntityMapper;
import elasticsearch.config.exception.BizRuntimeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchAggregation;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchAggregations;
import org.springframework.data.elasticsearch.core.AggregationsContainer;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * elasticsearch Aggregation parse util (One Depth 로 구성 된 집계 결과를 파싱합니다.)
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class EsAggregationParseUtils {

    private EntityMapper<List<StringTermsBucket>, List<TermsBucketDto>> termsAggMapper = new TermsAggregateMapper();
    private EntityMapper<List<RangeBucket>, List<RangeBucketDto>> rangeAggMapper = new RangeAggregationMapper();

    /**
     * 집계 쿼리 파서
     *
     * @param aggContainer : elasticsearchOperation으로 부터 전달 받은 집계 값.
     * @return aggregations dto : 반환될 집계 결과 dto
     */
    public AggregationsDto aggregationsResultParser(AggregationsContainer<?> aggContainer) {

        /* 집계 결과 저장 Dto */
        AggregationsDto aggregations = new AggregationsDto();

        /* 집계 결과 map 으로 매핑 */
        Map<String, ElasticsearchAggregation> aggregationsMap =
                ((ElasticsearchAggregations) aggContainer).aggregationsAsMap();

        /* 집계 결과 파싱 시작 */
        try {
            for (Map.Entry<String, ElasticsearchAggregation> aggregationsEntry : aggregationsMap.entrySet()) {
                String aggKey = aggregationsEntry.getKey();
                Aggregate aggValue = aggregationsEntry.getValue().aggregation().getAggregate();
                aggregations = setAggregation(aggregations, aggKey, aggValue);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new BizRuntimeException("Aggregation 데이터 파싱 실패.", e);
        }

        return aggregations;
    }

    /*************************************************************************************************************
     * aggregationsResult 파싱 관련 메소드 Start
     *************************************************************************************************************/

    /**
     * 각 aggregation 결과 타입 맞는 집계 결과 setter mapping
     *
     * @param aggregations : 현재 가지고 있는 aggregations
     * @param aggKey : aggregation 의 Key(aggregation name)
     * @param aggValue : elasticsearch 로 반환 받은 aggregation 결과
     * @return aggregationsDto : 반환될 집계 결과 dto
     */
    private AggregationsDto setAggregation(AggregationsDto aggregations, String aggKey, Aggregate aggValue) {

        /* Bucket Aggregation 파싱 */
        if (aggValue.isRange()) {
            aggregations.setRange(setRangeAggregation(aggregations, aggKey, aggValue));
        } else if (aggValue.isSterms()) {
            aggregations.setTerms(setTermsAggregation(aggregations, aggKey, aggValue));
        } else if (aggValue.isFilter()) {
            aggregations = setFilterAggregation(aggregations, aggKey, aggValue);
        }
        /* TODO - 추가적인 aggregation 매핑 추가 */

        return aggregations;
    }

    /**
     * filter Aggregation 결과 setter
     */
    private AggregationsDto setFilterAggregation(AggregationsDto aggregations, String aggKey, Aggregate aggValue) {

        Map<String, Aggregate> subAggregations = ((FilterAggregate) aggValue._get()).aggregations();
        try {
            for (Map.Entry<String, Aggregate> subAggregationsEntry : subAggregations.entrySet()) {
                String subAggKey = subAggregationsEntry.getKey();
                Aggregate subAggValue = subAggregationsEntry.getValue();
                aggregations = setAggregation(aggregations, subAggKey, subAggValue);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new BizRuntimeException("Filter Aggregation 데이터 파싱 실패.", e);
        }

        return aggregations;
    }

    /**
     * range Aggregation 결과 setter
     *
     * @param aggregations : 현재 가지고 있는 aggregations
     * @param aggKey : aggregation 의 Key(aggregation name)
     * @param aggValue : elasticsearch 로 반환 받은 aggregation 결과
     * @return aggregationsDto : 반환될 집계 결과 dto
     */
    private List<RangeAggregateDto> setRangeAggregation(AggregationsDto aggregations, String aggKey, Aggregate aggValue) {

        // range 집계 결과 List 초기화
        List<RangeAggregateDto> rangeAggregations =
                Objects.isNull(aggregations.getRange()) ?
                        new ArrayList<RangeAggregateDto>() : aggregations.getRange();

        // range 집계 결과 매핑
        rangeAggregations.add(RangeAggregateDto.builder()
                .name(aggKey)
                .buckets(rangeAggMapper.convert( ((RangeAggregate) aggValue._get()).buckets().array()))
                .build());

        return rangeAggregations;
    }

    /**
     * terms Aggregation 결과 setter
     */
    private List<TermsAggregateDto> setTermsAggregation(AggregationsDto aggregations, String aggKey, Aggregate aggValue) {

        // terms 집계 결과 List 초기화
        List<TermsAggregateDto> termsAggregations =
                Objects.isNull(aggregations.getTerms()) ?
                        new ArrayList<TermsAggregateDto>() : aggregations.getTerms();

        // terms 집계 결과 매핑
        termsAggregations.add(TermsAggregateDto.builder()
                .name(aggKey)
                .buckets(termsAggMapper.convert( ((StringTermsAggregate) aggValue._get()).buckets().array()))
                .build());

        return termsAggregations;
    }

    /*************************************************************************************************************
     * aggregationsResult 파싱 관련 메소드 End
     *************************************************************************************************************/

}
