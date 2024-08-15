package elasticsearch.application.adapter.out.es.utils;

import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.aggregations.AggregationRange;
import co.elastic.clients.elasticsearch._types.aggregations.RangeAggregation;
import co.elastic.clients.elasticsearch._types.aggregations.TermsAggregation;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import elasticsearch.config.exception.BizRuntimeException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Elasticsearch Aggregation query 생성 utils
 */
public class EsAggregationUtils {
    private Aggregation agg;
    private Aggregation subAgg;

    public Aggregation getAggregation(){
        return agg;
    }

    public void setTermsAggregation(String field) {
        agg = Aggregation.of(a -> a.terms(TermsAggregation.of(t -> t.field(field))));
    }

    public void setTwoDepthTermsAggregation(String field1, String field2) {
        Aggregation aggDepth2 = Aggregation.of(a -> a.terms(TermsAggregation.of(t -> t.field(field2))));
        agg = Aggregation.of(a -> a.terms(TermsAggregation.of(t -> t.field(field1)))
                .aggregations(new HashMap<>() {{ put(field2, aggDepth2);}}));
    }

    public void setRangeAggregation(String field, List<AggregationRange> aggRange) {
        agg = Aggregation.of(a -> a.range(RangeAggregation.of(r -> r.field(field).ranges(aggRange))));
    }

    public void setRangeAggregation(String field, Integer max, Integer range) {
        agg = Aggregation.of(a -> a.range(RangeAggregation.of(r -> r.field(field)
                .ranges(makeAggregationRange(max, range)))));
    }

    public void setTermSubAggregation(String aggName, String field, String value) {
        if (agg == null) {
            throw new BizRuntimeException("서브쿼리가 지정되어 있지 않습니다.");
        }
        subAgg = agg;
        agg = Aggregation.of(a -> a.filter(TermQuery.of(t -> t.field(field).value(value))._toQuery())
                .aggregations(new HashMap<>() {{ put(aggName, subAgg);}}));
    }

    private List<AggregationRange> makeAggregationRange(Integer max, Integer range) {
        List<AggregationRange> aggRange = new ArrayList<AggregationRange>();
        Integer from = 0;
        Integer to = 0;

        while (to <= max){
            to += range;
            if (to.equals(range)) { // first range
                Integer finalTo = to;
                aggRange.add(AggregationRange.of(r -> r.to(String.valueOf(finalTo))));
            } else if (from >= max) {
                Integer finalFrom = from;
                aggRange.add(AggregationRange.of(r -> r.from(String.valueOf(finalFrom))));
            } else {
                Integer finalTo = to;
                Integer finalFrom = from;
                aggRange.add(AggregationRange.of(r -> r.from(String.valueOf(finalFrom)).to(String.valueOf(finalTo))));
            }
            from += range;
        }
        return aggRange;
    }

}
