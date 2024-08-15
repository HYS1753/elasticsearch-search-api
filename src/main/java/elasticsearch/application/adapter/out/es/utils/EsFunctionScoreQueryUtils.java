package elasticsearch.application.adapter.out.es.utils;

import co.elastic.clients.elasticsearch._types.query_dsl.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Elasticsearch FunctionScore Query 생성 클래스
 */
public class EsFunctionScoreQueryUtils {
    private Query query;
    private List<FunctionScore> functions;
    private FunctionScoreMode scoreMode;    // sum, avg, max, min, multiply(default)
    private FunctionBoostMode boostMode;    // sum, avg, max, min, multiply(default)

    public EsFunctionScoreQueryUtils() {
        this.query = null;
        this.functions = new ArrayList<FunctionScore>();
        this.scoreMode = FunctionScoreMode.Multiply;        // default Multiply
        this.boostMode = FunctionBoostMode.Multiply;        // default Multiply
    }

    public FunctionScoreQuery getFunctionScoreQuery() {
        if (this.query != null) {
            return QueryBuilders.functionScore()
                    .query(this.query)
                    .functions(this.functions)
                    .scoreMode(this.scoreMode)
                    .boostMode(this.boostMode)
                    .build();
        } else {
            return QueryBuilders.functionScore().build();
        }
    }

    public void setQuery(Query query) {
        this.query = query;
    }

    // 각 function 안의 스코어 값을 어떻게 연산 할 것 인가에 대한 모드
    public void setScoreMode(FunctionScoreMode scoreMode) {
        this.scoreMode = scoreMode;
    }

    // functions 에서 연산된 score 값을 실 hits 내의 _score 값에 반영 할 것 인가에 대한 모드
    public void setBoostMode(FunctionBoostMode boostMode) {
        this.boostMode = boostMode;
    }

    public void setFieldValueFactorFunction(String field, Double factor, FieldValueFactorModifier modifier, Double missing) {
        this.functions.add(FieldValueFactorScoreFunction.of(f -> f.field(field).factor(factor).modifier(modifier).missing(missing))._toFunctionScore());
    }

    public void setMatchFilterFunction(String field, String value, Double weight) {
        // function score filter match query
        Query matchQuery = Query.of(q -> q.match(m -> m.field(field).query(value)));
        // add function scores list
        this.functions.add(FunctionScore.of(f -> f.filter(matchQuery).weight(weight)));
    }

    public void setScriptScoreFunction(String script) {
        this.functions.add(FunctionScore.of(f -> f.scriptScore(ss -> ss.script(s -> s.inline(i -> i.source(script))))));
    }

}
