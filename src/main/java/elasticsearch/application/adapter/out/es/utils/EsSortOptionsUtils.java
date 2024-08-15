package elasticsearch.application.adapter.out.es.utils;

import co.elastic.clients.elasticsearch._types.FieldSort;
import co.elastic.clients.elasticsearch._types.ScoreSort;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Elasticsearch Sort Option 쿼리 생성 Utils
 */
public class EsSortOptionsUtils {

    private static final String SCORE = "score";
    private List<SortOptions> sortOptions;

    public EsSortOptionsUtils() {
        this.sortOptions = new ArrayList<SortOptions>();
    }

    public List<SortOptions> getSortOptions() {
        return sortOptions;
    }

    public void setSortOptions(String sortTarget, String sortOrder) {
        SortOrder sortOpt = StringUtils.equalsIgnoreCase(sortOrder, "desc") ? SortOrder.Desc : SortOrder.Asc;
        if (StringUtils.equalsIgnoreCase(sortTarget, SCORE)) {
            sortOptions.add(SortOptions.of(s -> s.score(ScoreSort.of(a -> a.order(sortOpt)))));
        } else {
            sortOptions.add(SortOptions.of(s -> s.field(FieldSort.of(f -> f.field(sortTarget).order(sortOpt)))));
        }
    }
}
