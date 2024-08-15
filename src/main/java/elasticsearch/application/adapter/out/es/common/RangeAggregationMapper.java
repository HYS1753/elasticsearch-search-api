package elasticsearch.application.adapter.out.es.common;

import co.elastic.clients.elasticsearch._types.aggregations.RangeBucket;
import elasticsearch.application.adapter.in.dto.common.RangeBucketDto;
import elasticsearch.application.adapter.out.es.indices.EntityMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class RangeAggregationMapper implements EntityMapper<List<RangeBucket>, List<RangeBucketDto>> {

    @Override
    public List<RangeBucketDto> convert(List<RangeBucket> entity) {

        List<RangeBucketDto> converted = new ArrayList<RangeBucketDto>();

        for (RangeBucket bucket : entity) {
            RangeBucketDto dto = new RangeBucketDto();
            dto.setDocCount(bucket.docCount());
            dto.setKey(bucket.key());
            if (Objects.nonNull(bucket.from())) {
                dto.setFrom(bucket.from());
            }
            if (Objects.nonNull(bucket.to())) {
                dto.setTo(bucket.to());
            }

            converted.add(dto);
        }

        return converted;
    }
}