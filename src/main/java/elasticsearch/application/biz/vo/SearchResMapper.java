package elasticsearch.application.biz.vo;

import elasticsearch.application.adapter.in.dto.SearchRes;
import elasticsearch.common.utils.CommonUtils;
import lombok.NonNull;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class SearchResMapper implements ServiceMapper<SearchVo, SearchRes> {
    @Override
    public SearchRes convert(@NonNull SearchVo searchVo) {
        SearchRes searchRes = new SearchRes();
        BeanUtils.copyProperties(searchVo, searchRes, CommonUtils.getNullPropertyNames(searchVo));
        return searchRes;
    }
}
