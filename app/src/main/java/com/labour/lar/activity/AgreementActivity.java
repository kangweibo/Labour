package com.labour.lar.activity;

import android.widget.TextView;
import com.labour.lar.BaseActivity;
import com.labour.lar.R;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import butterknife.BindView;

/**
 * 服务协议
 */
public class AgreementActivity extends BaseActivity {

    @BindView(R.id.txt_content)
    TextView txt_content;

    @Override
    public int getActivityLayoutId() {
        return R.layout.activity_agreement;
    }

    @Override
    public void afterInitLayout() {
        String content = getFromAssets("sla.txt");
        txt_content.setText(content);
    }

    // 读取assets中文件
    private String getFromAssets(String fileName){
        StringBuilder result = new StringBuilder();
        try {
            InputStreamReader inputReader = new InputStreamReader(this.getAssets().open(fileName) );
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line;
            while((line = bufReader.readLine()) != null) {
                result.append(line);
                result.append("\r\n");//写入换行
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result.toString();
    }
}
