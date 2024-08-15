package elasticsearch.application.biz.vo;

import elasticsearch.application.adapter.in.dto.SearchReq;
import elasticsearch.common.utils.CommonUtils;
import lombok.NonNull;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class SearchReqMapper implements ServiceMapper<SearchReq, SearchVo> {
    @Override
    public SearchVo convert(@NonNull SearchReq searchReq) {
        SearchVo searchVo = new SearchVo();
        BeanUtils.copyProperties(searchReq, searchVo, CommonUtils.getNullPropertyNames(searchReq));
        return searchVo;
    }
}
