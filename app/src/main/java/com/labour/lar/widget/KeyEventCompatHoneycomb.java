package com.labour.lar.widget;

import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.view.KeyEvent;

/**
 * Author: lx
 * CreateDate: 2019/6/15 21:06
 * Company Hebei Xiaoxiong Technology Co., Ltd.
 * Description:
 */
@RequiresApi(11)
@TargetApi(11)
public class KeyEventCompatHoneycomb {
    public static int normalizeMetaState(int metaState) {
        return KeyEvent.normalizeMetaState(metaState);
    }

    public static boolean metaStateHasModifiers(int metaState, int modifiers) {
        return KeyEvent.metaStateHasModifiers(metaState, modifiers);
    }

    public static boolean metaStateHasNoModifiers(int metaState) {
        return KeyEvent.metaStateHasNoModifiers(metaState);
    }

    public static boolean isCtrlPressed(KeyEvent event) {
        return event.isCtrlPressed();
    }
}
