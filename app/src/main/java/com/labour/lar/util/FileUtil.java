/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.labour.lar.util;

import java.io.File;

import android.content.Context;

public class FileUtil {
    public static File getSaveFile(Context context,String fileName) {
        File file = new File(Utils.getTakePhotoPath(), fileName);
        return file;
    }
}
