package elasticsearch.application.biz.domain;

import elasticsearch.application.adapter.in.dto.SearchReq;
import elasticsearch.application.adapter.in.dto.SearchRes;
import elasticsearch.application.biz.port.in.SearchInPort;
import elasticsearch.application.biz.port.out.SearchOutPort;
import elasticsearch.application.biz.vo.SearchVo;
import elasticsearch.application.biz.vo.ServiceMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchService implements SearchInPort {

    private final SearchOutPort persistence;

    private final ServiceMapper<SearchReq, SearchVo> searchReqMapper;
    private final ServiceMapper<SearchVo, SearchRes> searchResMapper;

    @Override
    public SearchRes search(SearchReq searchReq) {
        SearchVo searchVo = searchReqMapper.convert(searchReq);
        searchVo = persistence.search(searchVo);
        return searchResMapper.convert(searchVo);
    }

}
