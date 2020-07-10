package com.labour.lar.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.baidu.idl.face.platform.FaceConfig;
import com.baidu.idl.face.platform.FaceEnvironment;
import com.baidu.idl.face.platform.FaceSDKManager;
import com.baidu.idl.face.platform.FaceStatusEnum;
import com.baidu.idl.face.platform.LivenessTypeEnum;
import com.baidu.idl.face.platform.ui.FaceLivenessActivity;
import com.labour.lar.widget.toast.AppToast;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FaceLivenessExpActivity extends FaceLivenessActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setFaceConfig();

        super.onCreate(savedInstanceState);
    }

    @Override
    public void onLivenessCompletion(FaceStatusEnum status, String message, HashMap<String, String> base64ImageMap) {
        super.onLivenessCompletion(status, message, base64ImageMap);
        if (status == FaceStatusEnum.OK && mIsCompletion) {
            AppToast.show(this,"活体检测,检测成功!");

            String bitmap = getImage(base64ImageMap);
            Intent intent = new Intent();
            intent.putExtra("data", bitmap);
            setResult(Activity.RESULT_OK, intent);
            finish();
        } else if (status == FaceStatusEnum.Error_DetectTimeout ||
                status == FaceStatusEnum.Error_LivenessTimeout ||
                status == FaceStatusEnum.Error_Timeout) {
            AppToast.show(this,"活体检测,采集超时");
        }
    }

    private String getImage(HashMap<String, String> imageMap) {
        Set<Map.Entry<String, String>> sets = imageMap.entrySet();
        String bmp = null;
        for (Map.Entry<String, String> entry : sets) {
            bmp = entry.getValue();
        }
        return bmp;
    }

    private void setFaceConfig() {
        FaceConfig config = FaceSDKManager.getInstance().getFaceConfig();
        // SDK初始化已经设置完默认参数（推荐参数），您也根据实际需求进行数值调整
        List<LivenessTypeEnum> livenessList = new ArrayList<LivenessTypeEnum>();
        livenessList.add(LivenessTypeEnum.Eye);
//        livenessList.add(LivenessTypeEnum.HeadLeft);
//        livenessList.add(LivenessTypeEnum.HeadRight);
        livenessList.add(LivenessTypeEnum.Mouth);
        config.setLivenessTypeList(livenessList);
        config.setLivenessRandom(false);
        config.setBlurnessValue(FaceEnvironment.VALUE_BLURNESS);
        config.setBrightnessValue(FaceEnvironment.VALUE_BRIGHTNESS);
        config.setCropFaceValue(FaceEnvironment.VALUE_CROP_FACE_SIZE);
        config.setHeadPitchValue(FaceEnvironment.VALUE_HEAD_PITCH);
        config.setHeadRollValue(FaceEnvironment.VALUE_HEAD_ROLL);
        config.setHeadYawValue(FaceEnvironment.VALUE_HEAD_YAW);
        config.setMinFaceSize(FaceEnvironment.VALUE_MIN_FACE_SIZE);
        config.setNotFaceValue(FaceEnvironment.VALUE_NOT_FACE_THRESHOLD);
        config.setOcclusionValue(FaceEnvironment.VALUE_OCCLUSION);
        config.setCheckFaceQuality(true);
        config.setFaceDecodeNumberOfThreads(2);

        FaceSDKManager.getInstance().setFaceConfig(config);
    }

    @Override
    public void finish() {
        super.finish();
    }

}
