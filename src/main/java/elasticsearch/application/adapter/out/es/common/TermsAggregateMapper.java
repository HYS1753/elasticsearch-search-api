package elasticsearch.application.adapter.out.es.common;

import co.elastic.clients.elasticsearch._types.aggregations.StringTermsBucket;
import elasticsearch.application.adapter.in.dto.common.TermsBucketDto;
import elasticsearch.application.adapter.out.es.indices.EntityMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TermsAggregateMapper implements EntityMapper<List<StringTermsBucket>, List<TermsBucketDto>> {

    @Override
    public List<TermsBucketDto> convert(List<StringTermsBucket> entity) {

        List<TermsBucketDto> converted = new ArrayList<TermsBucketDto>();

        for (StringTermsBucket bucket : entity) {
            converted.add(TermsBucketDto.builder()
                    .docCount(bucket.docCount())
                    .key(bucket.key().stringValue())
                    .build());
        }

        return converted;
    }
}