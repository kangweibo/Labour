package com.labour.lar.event;

import com.labour.lar.MainActivity;
import com.labour.lar.event.beans.Logout;

/**
 * Author: lx
 * CreateDate: 2019/6/29 18:55
 * Company Hebei Xiaoxiong Technology Co., Ltd.
 * Description:
 */
public class EventBuilder extends BaseEventBuilder {

    @Override
    public void buildEvents() {
        build(Logout.class, MainActivity.class,"loginExpired");
    }
}
