package elasticsearch.application.biz.port.out;

import elasticsearch.application.biz.vo.SearchVo;

public interface SearchOutPort {

    SearchVo search(SearchVo searchVo);
}
