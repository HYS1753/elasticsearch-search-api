package elasticsearch.application.adapter.out.es.indices;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import elasticsearch.application.adapter.in.dto.SearchSource;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.util.List;

@Component
public class BookEntityMapper implements EntityMapper<List<BookEntity>, List<SearchSource>> {
    @Override
    public List<SearchSource> convert(List<BookEntity> entity) {
        Gson gson = new Gson();

        // 1. Json 형태로 변환
        String jsonString = gson.toJson(entity);

        // 2. 변환 될 타입 설정
        Type returnType = new TypeToken<List<SearchSource>>(){}.getType();

        // 3. Json String -> 반환할 객체로 변환
        return gson.fromJson(jsonString, returnType);
    }
}
