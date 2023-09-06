package com.example.taobao.view;

import com.example.taobao.base.IBaseCallback;
import com.example.taobao.model.domain.TicketResult;

public interface iTicketPagerCallback extends IBaseCallback {
    void onTicketLoaded(String cover, TicketResult result);
}
