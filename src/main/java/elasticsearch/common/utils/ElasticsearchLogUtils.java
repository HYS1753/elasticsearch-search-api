package elasticsearch.common.utils;

import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.query.SourceFilter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class ElasticsearchLogUtils {

    @Value("${logging.logLevel}")
    private String logLevel;

    public void logQueryDSL(NativeQuery nativeQuery) {
        if (logLevel.equalsIgnoreCase("debug") || logLevel.equalsIgnoreCase("trace")) {
            this.queryDSLToString(nativeQuery);
        }
    }

    private void queryDSLToString(NativeQuery nativeQuery) {
        List<String> queryDSLList = new ArrayList<String>();
        try {
            String trackTotalHits = trackTotalHitsToString(nativeQuery.getTrackTotalHits());
            String sourceFitler = sourceFilterToString(nativeQuery.getSourceFilter());
            String query = queryToString(nativeQuery.getQuery());
            String paging = pagingToString(nativeQuery.getPageable());
            String sort = sortToString(nativeQuery.getSortOptions());
            String aggs = aggsToString(nativeQuery.getAggregations());

            if (!trackTotalHits.isEmpty()) {
                queryDSLList.add(trackTotalHits);
            }
            if (!sourceFitler.isEmpty()) {
                queryDSLList.add(sourceFitler);
            }
            if (!aggs.isEmpty()) {
                queryDSLList.add(aggs);
            }
            if (!query.isEmpty()) {
                queryDSLList.add(query);
            }
            if (!paging.isEmpty()) {
                queryDSLList.add(paging);
            }
            if (!sort.isEmpty()) {
                queryDSLList.add(sort);
            }
        } catch(Exception e) {
            log.debug("Parse QueryDSL Log Error :" + e);
        }
        log.debug("Req QueryDSL: {%s}".formatted(String.join(", ", queryDSLList)));
    }

    private String sourceFilterToString(SourceFilter sourceFilter) {
        String[] include = sourceFilter.getIncludes();
        String[] exclude = sourceFilter.getExcludes();
        boolean isIncludesExist = ((include != null ? include.length : 0) > 0);
        boolean isExcludesExist = ((exclude != null ? exclude.length : 0) > 0);

        JsonObject sourceJsonObject = new JsonObject();

        if (isIncludesExist) {
            JsonArray jsonArray = new JsonArray();
            for (String s: include) {
                JsonElement element = new JsonPrimitive(s);
                jsonArray.add(element);
            }
            sourceJsonObject.add("includes", jsonArray);
        }
        if (isExcludesExist) {
            JsonArray jsonArray = new JsonArray();
            for (String s: exclude) {
                JsonElement element = new JsonPrimitive(s);
                jsonArray.add(element);
            }
            sourceJsonObject.add("excludes", jsonArray);
        }

        if (isIncludesExist || isExcludesExist) {
            return "\"_source\":"+sourceJsonObject;
        } else {
            return "";
        }
    }

    private String trackTotalHitsToString(Boolean isTrackTotalHits) {
        return "\"track_total_hits\": %s".formatted(isTrackTotalHits);
    }

    private String queryToString(Query query) {
        String queryPrefix = "Query: ";
        String queryString = query.toString();
        if (queryString.substring(0, 7).equals(queryPrefix)) {
            return "\"query\":"+queryString.substring(7);
        }else {
            return "";
        }
    }

    private String pagingToString(Pageable pageRequest) {
        int pageNumber = pageRequest.getPageNumber();
        int pageSize = pageRequest.getPageSize();

        return "\"from\":%s,\"size\":%s".formatted(pageNumber * pageSize, pageSize);
    }

    private String sortToString(List<SortOptions> sortOptions) {
        String sortoptionsPrefix = "SortOptions: ";
        List<String> sortList = new ArrayList<String>();

        for (SortOptions option : sortOptions) {
            String sortOptionStr = option.toString();
            if (sortOptionStr.substring(0, sortoptionsPrefix.length()).equals(sortoptionsPrefix)) {
                sortList.add(sortOptionStr.substring(sortoptionsPrefix.length()));
            }
        }

        if (!sortList.isEmpty()) {
            return "\"sort\": [%s]".formatted(String.join(", ", sortList));
        } else {
            return "";
        }
    }

    private String aggsToString(Map<String, Aggregation> aggs) {
        String aggregationPrefix = "Aggregation: ";
        List<String> aggsList = new ArrayList<String>();

        for (Map.Entry<String, Aggregation> entry : aggs.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue().toString();
            if (value.substring(0,aggregationPrefix.length()).equals(aggregationPrefix)) {
                aggsList.add("\"%s\": %s".formatted(key, value.substring(aggregationPrefix.length())));
            }
        }

        if (!aggsList.isEmpty()) {
            return "\"aggs\": {%s}".formatted(String.join(", ", aggsList));
        } else {
            return "";
        }
    }
}
