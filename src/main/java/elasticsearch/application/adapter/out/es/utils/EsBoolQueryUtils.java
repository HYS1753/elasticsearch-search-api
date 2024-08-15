package elasticsearch.application.adapter.out.es.utils;

import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.json.JsonData;
import io.micrometer.common.util.StringUtils;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Elasticsearch Bool Query 생성 클래스
 */
@Getter
public class EsBoolQueryUtils {
    private List<Query> filter;
    private List<Query> must;
    private List<Query> mustNot;
    private List<Query> should;
    private String minimumShouldMatch;      // default 0

    public EsBoolQueryUtils() {
        this.filter = new ArrayList<Query>();
        this.must = new ArrayList<Query>();
        this.mustNot = new ArrayList<Query>();
        this.should = new ArrayList<Query>();
        this.minimumShouldMatch = "0";
    }

    public BoolQuery getBoolQuery() {
        return QueryBuilders.bool()
                .filter(filter)
                .must(must)
                .mustNot(mustNot)
                .should(should)
                .minimumShouldMatch(minimumShouldMatch)
                .build();
    }

    /*
       MUST MultiMatch 쿼리 생성
       - Operator : OR
       - Type : cross_fields
    */
    public void setMustMultiMatchQuery(List<String> matchFields, String keywords) {
        this.must.add(MultiMatchQuery.of(m -> m.fields(matchFields).query(keywords).type(TextQueryType.CrossFields))._toQuery());
    }

    /*
       MUST MultiMatch 쿼리 생성
       - Operator : AND
       - Type : cross_fields
    */
    public void setMustMultiMatchAndOpQuery(List<String> matchFields, String keywords) {
        this.must.add(MultiMatchQuery.of(m -> m.fields(matchFields).query(keywords).operator(Operator.And).type(TextQueryType.CrossFields))._toQuery());
    }

    /*
       MUST MultiMatch 쿼리 생성
       - Operator : AND
       - Type : cross_fields
       - Boost : boost
    */
    public void setMustMultiMatchAndOpQuery(List<String> matchFields, String keywords, Float boost) {
        this.must.add(MultiMatchQuery.of(m -> m.fields(matchFields).query(keywords).operator(Operator.And).type(TextQueryType.CrossFields).boost(boost))._toQuery());
    }

    public void setFilterTermQuery(String field, String value) {
        this.filter.add(TermQuery.of(t -> t.field(field).value(value))._toQuery());
    }

    public void setFilterTermsQuery(String field, List<String> values) {
        TermsQueryField termsQueryField =
                new TermsQueryField.Builder().value(filedValueParser(values)).build();
        this.filter.add(TermsQuery.of(t -> t.field(field).terms(termsQueryField))._toQuery());
    }

    public void setFilterTermsQuery(String field, String values, String separator) {
        TermsQueryField termsQueryField =
                new TermsQueryField.Builder().value(filedValueParser(values, separator)).build();
        this.filter.add(TermsQuery.of(t -> t.field(field).terms(termsQueryField))._toQuery());
    }

    public void setFilterRangeQuery(String field, String gte, String lte) {
        this.filter.add(RangeQuery.of(r -> r.field(field).gte(JsonData.of(gte)).lte(JsonData.of(lte)))._toQuery());
    }

    /*
       SHOULD MatchPhrase 쿼리 생성
       - Slop : 토큰 간 거리
       - Boost : boost
    */
    public void setShouldMatchPhrase(String field, String keywords, Integer slop, Float boost) {
        this.should.add(MatchPhraseQuery.of(m -> m.field(field).query(keywords).slop(slop).boost(boost))._toQuery());
    }

    /*
       SHOULD MultiMatch 쿼리 생성
       - Operator : OR
       - Type : cross_fields
    */
    public void setShouldMultiMatchQuery(List<String> matchFields, String keywords) {
        this.should.add(MultiMatchQuery.of(m -> m.fields(matchFields).query(keywords).type(TextQueryType.CrossFields))._toQuery());
    }

    /*
       SHOULD MultiMatch 쿼리 생성
       - Operator : AND
       - Type : cross_fields
    */
    public void setShouldMultiMatchAndOpQuery(List<String> matchFields, String keywords) {
        this.should.add(MultiMatchQuery.of(m -> m.fields(matchFields).query(keywords).operator(Operator.And).type(TextQueryType.CrossFields))._toQuery());
    }

    /*
       SHOULD MultiMatch 쿼리 생성
       - Operator : AND
       - Type : cross_fields
       - Boost : boost
    */
    public void setShouldMultiMatchAndOpQuery(List<String> matchFields, String keywords, Float boost) {
        this.should.add(MultiMatchQuery.of(m -> m.fields(matchFields).query(keywords).operator(Operator.And).type(TextQueryType.CrossFields).boost(boost))._toQuery());
    }

    public void setShouldAllOfIntervals(String field, String keywords, Integer maxGaps, boolean ordered) {
        List<Intervals> innerIntervalsQuery = new ArrayList<Intervals>();
        innerIntervalsQuery.add(Intervals.of(i -> i.match(IntervalsMatch.of(im -> im.query(keywords).maxGaps(maxGaps).ordered(ordered)))));
        this.should.add(IntervalsQuery.of(i -> i.field(field).allOf(IntervalsAllOf.of(in -> in.intervals(innerIntervalsQuery))))._toQuery());
    }

    public void setMinimumShouldMatch(String minimumShouldMatchStr) {
        this.minimumShouldMatch = minimumShouldMatchStr;
    }

    /**
     * terms Query 의 여러 필드 조건 설정.
     */
    private List<FieldValue> filedValueParser(String values, String separator) {
        List<FieldValue> termsFieldValue = new ArrayList<FieldValue>();

        separator = StringUtils.isBlank(separator) ? "," : separator;
        String[] inputValueList = values.split(separator, -1);

        for (String inputValue : inputValueList) {
            termsFieldValue.add(FieldValue.of(inputValue));
        }

        return termsFieldValue;
    }
    /**
     * terms Query 의 여러 필드 조건 설정.
     */
    private List<FieldValue> filedValueParser(List<String> values) {
        List<FieldValue> termsFieldValue = new ArrayList<FieldValue>();

        for (String inputValue : values) {
            termsFieldValue.add(FieldValue.of(inputValue));
        }

        return termsFieldValue;
    }
}
