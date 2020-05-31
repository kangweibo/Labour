package com.labour.lar.version;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.RemoteViews;

import com.labour.lar.MainActivity;
import com.labour.lar.R;
import com.labour.lar.util.Utils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

public class UpdateService extends Service {
    private static String down_url; // = "http://192.168.1.112:8080/360.apk";
    private static final int DOWN_OK = 1; // 下载完成
    private static final int DOWN_ERROR = 0;

    private String app_name = "";

    private NotificationManager notificationManager;
    private NotificationCompat.Builder mCompatBuilder;
    private Notification.Builder mBuilder;

    private Intent updateIntent;
    private PendingIntent pendingIntent;
    private String updateFile;

    private int notification_id = 0;
    long totalSize = 0;// 文件总大小
    private String fileName = "labour.apk";
    private Context context;

    /***
     * 更新UI
     */
    final Handler handler = new Handler() {
        @SuppressWarnings("deprecation")
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DOWN_OK:
                    // 下载完成，点击安装
                    Intent installApkIntent = getFileIntent(new File(updateFile));
                    pendingIntent = PendingIntent.getActivity(UpdateService.this, 0, installApkIntent, 0);
                    contentView.setTextViewText(R.id.notificationTitle, "下载成功，点击安装");

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {//Android O 8.0 通知(Notification)
                        mBuilder.setContentIntent(pendingIntent);
                        notificationManager.notify(notification_id, mBuilder.build());
                    } else {
                        mCompatBuilder.setContentIntent(pendingIntent);
                        notificationManager.notify(notification_id, mCompatBuilder.build());
                    }
                    stopService(updateIntent);
                    break;
                case DOWN_ERROR:
                    contentView.setTextViewText(R.id.notificationTitle, "下载失败");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {//Android O 8.0 通知(Notification)
                        notificationManager.notify(notification_id, mBuilder.build());
                    } else {
                        notificationManager.notify(notification_id, mCompatBuilder.build());
                    }
                    break;
                default:
                    stopService(updateIntent);
                    break;
            }
        }
    };

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            try {
                app_name = intent.getStringExtra("app_name");
                down_url = intent.getStringExtra("downurl");
                // 创建文件
                File updateFile = new File(Utils.getCachePath(), fileName);
                if (!updateFile.exists()) {
                    try {
                        updateFile.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                // 创建通知
                createNotification();
                // 开始下载
                downloadUpdateFile(down_url, updateFile.getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }

    /***
     * 创建通知栏
     */
    RemoteViews contentView;
    public static final String channelId = "channel_1";
    public static final String channelName = "channel_name_1";

    @SuppressWarnings("deprecation")
    public void createNotification() {
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {//Android O 8.0 通知(Notification)
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
            mBuilder = new Notification.Builder(context, channelId)
                    .setOngoing(true)//设置是否是一个正在执行的通知
                    .setContentTitle("更新包")
                    .setContentText("Apk 下载中...")
                    .setSmallIcon(R.mipmap.ic_launcher);
        } else {
            mCompatBuilder = new NotificationCompat.Builder(this);
            mCompatBuilder.setContentTitle("更新包")
                    .setContentText("Apk 下载中...")
                    .setTicker("开始下载")
                    .setSmallIcon(R.mipmap.ic_launcher);
        }

        /***
         * 在这里我们用自定的view来显示Notification
         */
        contentView = new RemoteViews(getPackageName(), R.layout.notification_item);
        contentView.setTextViewText(R.id.notificationTitle, "正在下载");
        contentView.setTextViewText(R.id.notificationPercent, "0%");
        contentView.setProgressBar(R.id.notificationProgress, 100, 0, false);

//		mCompatBuilder.setCustomContentView(contentView);

        updateIntent = new Intent(this, MainActivity.class);
        updateIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        pendingIntent = PendingIntent.getActivity(this, 0, updateIntent, 0);

//		mCompatBuilder.setContentIntent(pendingIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {//Android O 8.0 通知(Notification)
            mBuilder.setCustomContentView(contentView);
            mBuilder.setContentIntent(pendingIntent);

            notificationManager.notify(notification_id, mBuilder.build());
        } else {
            mCompatBuilder.setCustomContentView(contentView);
            mCompatBuilder.setContentIntent(pendingIntent);
            notificationManager.notify(notification_id, mCompatBuilder.build());
        }
    }

    /***
     * 下载文件
     */
    public void downloadUpdateFile(String down_url, String file) throws Exception {
        String path = Utils.getCachePath().getAbsolutePath();
        updateFile = new File(path, fileName).getAbsolutePath();
        OkGo.<File>get(down_url)
                .execute(new FileCallback(path, fileName/*可以传入路径和名称*/) {
                    @Override
                    public void onSuccess(Response<File> response) {
                        Log.d("meee", getClass() + ":\n" + "test:" + (response.body().length() / 1024) + "KB");
                        // 下载成功
                        Message message = handler.obtainMessage();
                        message.what = DOWN_OK;
                        handler.sendMessage(message);
                        installApk(new File(updateFile), UpdateService.this);

                    }

                    @Override
                    public void onError(Response<File> response) {
                        Message message = handler.obtainMessage();
                        message.what = DOWN_ERROR;
                        handler.sendMessage(message);
                    }

                    //progress.fraction获取当前的下载进度
                    @Override
                    public void downloadProgress(Progress progress) {
                        Log.d("meee", getClass() + ":\n" + "progress:" + progress.currentSize);
                        double x_double = progress.currentSize * 1.0;
                        double tempresult = x_double / progress.totalSize;
                        DecimalFormat df1 = new DecimalFormat("0.00"); // ##.00%
                        // 百分比格式，后面不足2位的用0补齐
                        String result = df1.format(tempresult);
                        contentView.setTextViewText(R.id.notificationPercent, (int) (Float.parseFloat(result) * 100) + "%");
                        contentView.setProgressBar(R.id.notificationProgress, 100, (int) (Float.parseFloat(result) * 100), false);

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {//Android O 8.0 通知(Notification)
                            notificationManager.notify(notification_id, mBuilder.build());
                        } else {
                            notificationManager.notify(notification_id, mCompatBuilder.build());
                        }
                    }
                });
    }

    // 下载完成后打开安装apk界面
    public void installApk(File file, Context context) {
        //L.i("msg", "版本更新获取sd卡的安装包的路径=" + file.getAbsolutePath());
        Intent openFile = getFileIntent(file);
        context.startActivity(openFile);
    }

    public Intent getFileIntent(File file) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //判读版本是否在7.0以上
            Uri apkUri = FileProvider.getUriForFile(context, "com.labour.lar" + ".fileprovider", file);
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri uri = Uri.fromFile(file);
            String type = getMIMEType(file);
            intent.setDataAndType(uri, type);
        }

        return intent;
    }

    public String getMIMEType(File f) {
        String type = "";
        String fName = f.getName();
        // 取得扩展名
        String end = fName
                .substring(fName.lastIndexOf(".") + 1, fName.length());
        if (end.equals("apk")) {
            type = "application/vnd.android.package-archive";
        } else {
            // /*如果无法直接打开，就跳出软件列表给用户选择 */
            type = "*/*";
        }
        return type;
    }
}