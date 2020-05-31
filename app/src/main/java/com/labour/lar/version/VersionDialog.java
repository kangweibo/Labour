package com.labour.lar.version;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.labour.lar.widget.DialogUtil;

/**
 * Created by Administrator on 2018/7/15.
 */

public class VersionDialog {

    public static void show(final Context context, final String downloadUrl) {

        //final String url = Constant.HTTP_BASE + downloadUri;
        DialogUtil.showConfirmDialog(context,"提示信息","发现新版本确定要更新吗？",new DialogUtil.OnDialogEvent<Void>(){
            @Override
            public void onPositiveButtonClick(Void t) {
                Toast.makeText(context,"开始下载新版本...", Toast.LENGTH_SHORT).show();

                Intent updateIntent = new Intent(context, UpdateService.class);
                updateIntent.putExtra("app_name", "话淘吧");
                updateIntent.putExtra("downurl", downloadUrl);

                context.startService(updateIntent);
            }
        });
    }
}
