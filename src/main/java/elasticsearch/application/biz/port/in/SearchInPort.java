package elasticsearch.application.biz.port.in;

import elasticsearch.application.adapter.in.dto.SearchReq;
import elasticsearch.application.adapter.in.dto.SearchRes;

public interface SearchInPort {

    SearchRes search(SearchReq req);
}
