package com.labour.lar.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.labour.lar.BaseActivity;
import com.labour.lar.R;
import com.labour.lar.util.Utils;
import com.labour.lar.widget.toast.AppToast;
import com.yzq.zxinglibrary.encode.CodeCreator;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 设置界面
 */
public class ShowQRCodeActivity extends BaseActivity {

    @BindView(R.id.title_tv)
    TextView title_tv;
    @BindView(R.id.right_header_btn)
    TextView right_header_btn;

    @BindView(R.id.img_qrcode)
    ImageView img_qrcode;

    private Bitmap bitmap;

    @Override
    public int getActivityLayoutId() {
        return R.layout.activity_show_qrcode;
    }
    @Override
    public void afterInitLayout(){
        Intent intent = getIntent();
        String content = intent.getStringExtra("content");
        String title = intent.getStringExtra("title");

        title_tv.setText(title);
        right_header_btn.setText("保存");
        showBitmap(content);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @OnClick({R.id.back_iv,R.id.right_header_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_iv:
                finish();
            case R.id.right_header_btn:
                saveBitmap(bitmap);
                break;
        }
    }

    private void showBitmap(String content){
        if (!TextUtils.isEmpty(content)) {
            Bitmap logo = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
            bitmap = CodeCreator.createQRCode(content, 400, 400, logo);

            img_qrcode.setImageBitmap(bitmap);
        }
    }

    public void saveBitmap(Bitmap bitmap) {
        if (bitmap == null){
            AppToast.show(this, "二维码图片出错");
            return;
        }

        // 首先保存图片
        String createTime = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(new Date());
        String path = Utils.getTakePhotoPath();
        String picturePath = path + createTime + ".jpg";

        File pictureFile = new File(picturePath);
        String fileName = pictureFile.getName();

        if (pictureFile.exists()) {
            pictureFile.delete();
        }

        try {
            FileOutputStream out = new FileOutputStream(pictureFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(getContentResolver(),
                    pictureFile .getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        AppToast.show(this, "二维码图片保存成功");

        // 最后通知图库更新
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(pictureFile);intent.setData(uri);
        sendBroadcast(intent);//这个广播的目的就是更新图库，发了这个广播进入相册就可以找到你保存的图片了！，记得要传你更新的file哦
    }
}
