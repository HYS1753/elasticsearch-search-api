package elasticsearch.application.biz.vo;

import org.springframework.core.convert.converter.Converter;

public interface ServiceMapper<T, U> extends Converter<T, U> {

}