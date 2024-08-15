package elasticsearch.application.adapter.out.es.indices;

import org.springframework.core.convert.converter.Converter;

public interface EntityMapper<T, U> extends Converter<T, U> {

}

