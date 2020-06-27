package com.labour.lar.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.labour.lar.BaseActivity;
import com.labour.lar.R;
import com.yzq.zxinglibrary.encode.CodeCreator;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 设置界面
 */
public class ShowQRCodeActivity extends BaseActivity {

    @BindView(R.id.title_tv)
    TextView title_tv;

    @BindView(R.id.img_qrcode)
    ImageView img_qrcode;

    @Override
    public int getActivityLayoutId() {
        return R.layout.activity_show_qrcode;
    }
    @Override
    public void afterInitLayout(){
        Intent intent = getIntent();
        String content = intent.getStringExtra("content");

        title_tv.setText("加入班组");
        showBitmap(content);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @OnClick({R.id.back_iv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_iv:
                finish();
                break;
        }
    }

    private void showBitmap(String content){
        if (!TextUtils.isEmpty(content)) {
            Bitmap logo = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
            Bitmap bitmap = CodeCreator.createQRCode(content, 400, 400, logo);

            img_qrcode.setImageBitmap(bitmap);
        }
    }
}
