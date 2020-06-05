package com.labour.lar.ocr;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Base64;

import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.exception.SDKError;
import com.baidu.ocr.sdk.jni.JniInterface;
import com.baidu.ocr.sdk.model.AccessToken;
import com.baidu.ocr.sdk.model.BankCardParams;
import com.baidu.ocr.sdk.model.BankCardResult;
import com.baidu.ocr.sdk.model.GeneralBasicParams;
import com.baidu.ocr.sdk.model.GeneralParams;
import com.baidu.ocr.sdk.model.GeneralResult;
import com.baidu.ocr.sdk.model.IDCardParams;
import com.baidu.ocr.sdk.model.OcrRequestParams;
import com.baidu.ocr.sdk.model.OcrResponseResult;
import com.baidu.ocr.sdk.utils.AccessTokenParser;
import com.baidu.ocr.sdk.utils.BankCardResultParser;
import com.baidu.ocr.sdk.utils.CrashReporterHandler;
import com.baidu.ocr.sdk.utils.DeviceUtil;
import com.baidu.ocr.sdk.utils.GeneralResultParser;
import com.baidu.ocr.sdk.utils.GeneralSimpleResultParser;
import com.baidu.ocr.sdk.utils.HttpUtil;
import com.baidu.ocr.sdk.utils.HttpsClient;
import com.baidu.ocr.sdk.utils.ImageUtil;
import com.baidu.ocr.sdk.utils.OcrResultParser;
import com.baidu.ocr.sdk.utils.Parser;
import com.baidu.ocr.sdk.utils.Util;

import java.io.File;

public class BaiDuOCR {
    public static final String OCR_SDK_VERSION = "1_4_4";
    private static final String RECOGNIZE_GENERAL_URL = "https://aip.baidubce.com/rest/2.0/ocr/v1/general?";
    private static final String RECOGNIZE_GENERAL_BASIC_URL = "https://aip.baidubce.com/rest/2.0/ocr/v1/general_basic?";
    private static final String RECOGNIZE_ACCURATE_URL = "https://aip.baidubce.com/rest/2.0/ocr/v1/accurate?";
    private static final String RECOGNIZE_ACCURATE_BASIC_URL = "https://aip.baidubce.com/rest/2.0/ocr/v1/accurate_basic?";
    private static final String RECOGNIZE_GENERAL_ENHANCE_URL = "https://aip.baidubce.com/rest/2.0/ocr/v1/general_enhanced?";
    private static final String RECOGNIZE_GENERAL_WEBIMAGE_URL = "https://aip.baidubce.com/rest/2.0/ocr/v1/webimage?";
    private static final String RECOGNIZE_VEHICLE_LICENSE_URL = "https://aip.baidubce.com/rest/2.0/ocr/v1/vehicle_license?";
    private static final String RECOGNIZE_DRIVING_LICENSE_URL = "https://aip.baidubce.com/rest/2.0/ocr/v1/driving_license?";
    private static final String RECOGNIZE_LICENSE_PLATE_URL = "https://aip.baidubce.com/rest/2.0/ocr/v1/license_plate?";
    private static final String RECOGNIZE_BUSINESS_LICENSE_URL = "https://aip.baidubce.com/rest/2.0/ocr/v1/business_license?";
    private static final String RECOGNIZE_RECEIPT_URL = "https://aip.baidubce.com/rest/2.0/ocr/v1/receipt?";
    private static final String RECOGNIZE_VAT_INVOICE_URL = "https://aip.baidubce.com/rest/2.0/ocr/v1/vat_invoice?";
    private static final String RECOGNIZE_HANDWRITING_URL = "https://aip.baidubce.com/rest/2.0/ocr/v1/handwriting?";
    private static final String RECOGNIZE_QRCODE_URL = "https://aip.baidubce.com/rest/2.0/ocr/v1/qrcode?";
    private static final String RECOGNIZE_NUMBERS_URL = "https://aip.baidubce.com/rest/2.0/ocr/v1/numbers?";
    private static final String RECOGNIZE_PASSPORT_URL = "https://aip.baidubce.com/rest/2.0/ocr/v1/passport?";
    private static final String RECOGNIZE_LOTTERY_URL = "https://aip.baidubce.com/rest/2.0/ocr/v1/lottery?";
    private static final String RECOGNIZE_BUSINESS_CARD_URL = "https://aip.baidubce.com/rest/2.0/ocr/v1/business_card?";
    private static final String RECOGNIZE_CUSTOM = "https://aip.baidubce.com/rest/2.0/solution/v1/iocr/recognise?";
    private static final String ID_CARD_URL = "https://aip.baidubce.com/rest/2.0/ocr/v1/idcard?";
    private static final String BANK_CARD_URL = "https://aip.baidubce.com/rest/2.0/ocr/v1/bankcard?";
    private static final String QUERY_TOKEN = "https://verify.baidubce.com/verify/1.0/token/sk?sdkVersion=1_4_4";
    private static final String QUERY_TOKEN_BIN = "https://verify.baidubce.com/verify/1.0/token/bin?sdkVersion=1_4_4";
    private static final String PREFRENCE_FILE_KEY = "com.baidu.ocr.sdk";
    private static final String PREFRENCE_TOKENJSON_KEY = "token_json";
    private static final String PREFRENCE_EXPIRETIME_KEY = "token_expire_time";
    private static final String PREFRENCE_AUTH_TYPE = "token_auth_type";
    private static final int IMAGE_MAX_WIDTH = 1280;
    private static final int IMAGE_MAX_HEIGHT = 1280;
    private AccessToken accessToken = null;
    private static final int AUTHWITH_NOTYET = 0;
    private static final int AUTHWITH_LICENSE = 1;
    private static final int AUTHWITH_AKSK = 2;
    private static final int AUTHWITH_TOKEN = 3;
    private int authStatus = 0;
    private String ak = null;
    private String sk = null;
    private boolean isAutoCacheToken = false;
    private String license = null;
    @SuppressLint({"StaticFieldLeak"})
    private Context context;
    private CrashReporterHandler crInst;
    private static volatile BaiDuOCR instance;

    public boolean isAutoCacheToken() {
        return this.isAutoCacheToken;
    }

    public void setAutoCacheToken(boolean autoCacheToken) {
        this.isAutoCacheToken = autoCacheToken;
    }

    private BaiDuOCR(Context ctx) {
        if (ctx != null) {
            this.context = ctx;
        }

    }

    public static BaiDuOCR getInstance(Context ctx) {
        if (instance == null) {
            Class var1 = BaiDuOCR.class;
            synchronized(BaiDuOCR.class) {
                if (instance == null) {
                    instance = new BaiDuOCR(ctx);
                }
            }
        }

        return instance;
    }

    public void init(Context context) {
        this.context = context;
        this.crInst = CrashReporterHandler.init(context).addSourceClass(OCR.class);

        try {
            Class uiClass = Class.forName("com.baidu.ocr.ui.camera.CameraActivity");
            this.crInst.addSourceClass(uiClass);
        } catch (Throwable var3) {
        }

        HttpUtil.getInstance().init();
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public synchronized void setAccessToken(AccessToken accessToken) {
        if (accessToken.getTokenJson() != null) {
            SharedPreferences mSharedPreferences = this.context.getSharedPreferences("com.baidu.ocr.sdk", 0);
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putString("token_json", accessToken.getTokenJson());
            editor.putLong("token_expire_time", accessToken.getExpiresTime());
            editor.putInt("token_auth_type", this.authStatus);
            editor.apply();
        }

        this.accessToken = accessToken;
    }

    public synchronized AccessToken getAccessToken() {
        return this.accessToken;
    }

    public void recognizeGeneral(GeneralParams param, OnResultListener<GeneralResult> listener) {
        this.recognizeLocation(param, listener, "https://aip.baidubce.com/rest/2.0/ocr/v1/general?");
    }

    public void recognizeAccurate(GeneralParams param, OnResultListener<GeneralResult> listener) {
        this.recognizeLocation(param, listener, "https://aip.baidubce.com/rest/2.0/ocr/v1/accurate?");
    }

    private void recognizeLocation(final GeneralParams param, final OnResultListener<GeneralResult> listener, final String url) {
        File imageFile = param.getImageFile();
        final File tempImage = new File(this.context.getCacheDir(), String.valueOf(System.currentTimeMillis()));
        ImageUtil.resize(imageFile.getAbsolutePath(), tempImage.getAbsolutePath(), 1280, 1280);
        param.setImageFile(tempImage);
        final Parser<GeneralResult> generalResultParser = new GeneralResultParser();
        this.getToken(new OnResultListener() {
            public void onResult(Object result) {
                HttpUtil.getInstance().post(BaiDuOCR.this.urlAppendCommonParams(url), param, generalResultParser, new OnResultListener<GeneralResult>() {
                    public void onResult(GeneralResult result) {
                        tempImage.delete();
                        if (listener != null) {
                            listener.onResult(result);
                        }

                    }

                    public void onError(OCRError error) {
                        tempImage.delete();
                        if (listener != null) {
                            listener.onError(error);
                        }

                    }
                });
            }

            public void onError(OCRError error) {
                listener.onError(error);
            }
        });
    }

    public void recognizeGeneralBasic(GeneralBasicParams param, OnResultListener<GeneralResult> listener) {
        this.recognizeNoLocation(param, listener, "https://aip.baidubce.com/rest/2.0/ocr/v1/general_basic?");
    }

    public void recognizeAccurateBasic(GeneralBasicParams param, OnResultListener<GeneralResult> listener) {
        this.recognizeNoLocation(param, listener, "https://aip.baidubce.com/rest/2.0/ocr/v1/accurate_basic?");
    }

    public void recognizeGeneralEnhanced(GeneralBasicParams param, OnResultListener<GeneralResult> listener) {
        this.recognizeNoLocation(param, listener, "https://aip.baidubce.com/rest/2.0/ocr/v1/general_enhanced?");
    }

    public void recognizeWebimage(GeneralBasicParams param, OnResultListener<GeneralResult> listener) {
        this.recognizeNoLocation(param, listener, "https://aip.baidubce.com/rest/2.0/ocr/v1/webimage?");
    }

    private void recognizeNoLocation(final GeneralBasicParams param, final OnResultListener<GeneralResult> listener, final String url) {
        File imageFile = param.getImageFile();
        final File tempImage = new File(this.context.getCacheDir(), String.valueOf(System.currentTimeMillis()));
        ImageUtil.resize(imageFile.getAbsolutePath(), tempImage.getAbsolutePath(), 1280, 1280);
        param.setImageFile(tempImage);
        final Parser<GeneralResult> generalSimpleResultParser = new GeneralSimpleResultParser();
        this.getToken(new OnResultListener() {
            public void onResult(Object result) {
                HttpUtil.getInstance().post(BaiDuOCR.this.urlAppendCommonParams(url), param, generalSimpleResultParser, new OnResultListener<GeneralResult>() {
                    public void onResult(GeneralResult result) {
                        tempImage.delete();
                        if (listener != null) {
                            listener.onResult(result);
                        }

                    }

                    public void onError(OCRError error) {
                        tempImage.delete();
                        if (listener != null) {
                            listener.onError(error);
                        }

                    }
                });
            }

            public void onError(OCRError error) {
                listener.onError(error);
            }
        });
    }

    public void recognizeIDCard(final IDCardParams param, final OnResultListener<BaiDuIDCardResult> listener) {
        File imageFile = param.getImageFile();
        final File tempImage = new File(this.context.getCacheDir(), String.valueOf(System.currentTimeMillis()));
        ImageUtil.resize(imageFile.getAbsolutePath(), tempImage.getAbsolutePath(), 1280, 1280, param.getImageQuality());
        param.setImageFile(tempImage);
        final Parser<BaiDuIDCardResult> idCardResultParser = new BaiDuIDCardResultParser(param.getIdCardSide());
        this.getToken(new OnResultListener() {
            public void onResult(Object result) {
                HttpUtil.getInstance().post(BaiDuOCR.this.urlAppendCommonParams("https://aip.baidubce.com/rest/2.0/ocr/v1/idcard?"), param, idCardResultParser, new OnResultListener<BaiDuIDCardResult>() {
                    public void onResult(BaiDuIDCardResult result) {
                        tempImage.delete();
                        if (listener != null) {
                            listener.onResult(result);
                        }

                    }

                    public void onError(OCRError error) {
                        tempImage.delete();
                        if (listener != null) {
                            listener.onError(error);
                        }

                    }
                });
            }

            public void onError(OCRError error) {
                listener.onError(error);
            }
        });
    }

    public void recognizeBankCard(final BankCardParams params, final OnResultListener<BankCardResult> listener) {
        File imageFile = params.getImageFile();
        final File tempImage = new File(this.context.getCacheDir(), String.valueOf(System.currentTimeMillis()));
        ImageUtil.resize(imageFile.getAbsolutePath(), tempImage.getAbsolutePath(), 1280, 1280);
        params.setImageFile(tempImage);
        final Parser<BankCardResult> bankCardResultParser = new BankCardResultParser();
        this.getToken(new OnResultListener() {
            public void onResult(Object result) {
                HttpUtil.getInstance().post(BaiDuOCR.this.urlAppendCommonParams("https://aip.baidubce.com/rest/2.0/ocr/v1/bankcard?"), params, bankCardResultParser, new OnResultListener<BankCardResult>() {
                    public void onResult(BankCardResult result) {
                        tempImage.delete();
                        if (listener != null) {
                            listener.onResult(result);
                        }

                    }

                    public void onError(OCRError error) {
                        tempImage.delete();
                        if (listener != null) {
                            listener.onError(error);
                        }

                    }
                });
            }

            public void onError(OCRError error) {
                listener.onError(error);
            }
        });
    }

    public void recognizeVehicleLicense(OcrRequestParams params, OnResultListener<OcrResponseResult> listener) {
        this.recognizeCommon(params, listener, "https://aip.baidubce.com/rest/2.0/ocr/v1/vehicle_license?");
    }

    public void recognizeDrivingLicense(OcrRequestParams params, OnResultListener<OcrResponseResult> listener) {
        this.recognizeCommon(params, listener, "https://aip.baidubce.com/rest/2.0/ocr/v1/driving_license?");
    }

    public void recognizeLicensePlate(OcrRequestParams params, OnResultListener<OcrResponseResult> listener) {
        this.recognizeCommon(params, listener, "https://aip.baidubce.com/rest/2.0/ocr/v1/license_plate?");
    }

    public void recognizeBusinessLicense(OcrRequestParams params, OnResultListener<OcrResponseResult> listener) {
        this.recognizeCommon(params, listener, "https://aip.baidubce.com/rest/2.0/ocr/v1/business_license?");
    }

    public void recognizeReceipt(OcrRequestParams params, OnResultListener<OcrResponseResult> listener) {
        this.recognizeCommon(params, listener, "https://aip.baidubce.com/rest/2.0/ocr/v1/receipt?");
    }

    public void recognizeVatInvoice(OcrRequestParams params, OnResultListener<OcrResponseResult> listener) {
        this.recognizeCommon(params, listener, "https://aip.baidubce.com/rest/2.0/ocr/v1/vat_invoice?");
    }

    public void recognizeHandwriting(OcrRequestParams params, OnResultListener<OcrResponseResult> listener) {
        this.recognizeCommon(params, listener, "https://aip.baidubce.com/rest/2.0/ocr/v1/handwriting?");
    }

    public void recognizeQrcode(OcrRequestParams params, OnResultListener<OcrResponseResult> listener) {
        this.recognizeCommon(params, listener, "https://aip.baidubce.com/rest/2.0/ocr/v1/qrcode?");
    }

    public void recognizeNumbers(OcrRequestParams params, OnResultListener<OcrResponseResult> listener) {
        this.recognizeCommon(params, listener, "https://aip.baidubce.com/rest/2.0/ocr/v1/numbers?");
    }

    public void recognizePassport(OcrRequestParams params, OnResultListener<OcrResponseResult> listener) {
        this.recognizeCommon(params, listener, "https://aip.baidubce.com/rest/2.0/ocr/v1/passport?");
    }

    public void recognizeLottery(OcrRequestParams params, OnResultListener<OcrResponseResult> listener) {
        this.recognizeCommon(params, listener, "https://aip.baidubce.com/rest/2.0/ocr/v1/lottery?");
    }

    public void recognizeBusinessCard(OcrRequestParams params, OnResultListener<OcrResponseResult> listener) {
        this.recognizeCommon(params, listener, "https://aip.baidubce.com/rest/2.0/ocr/v1/business_card?");
    }

    public void recognizeCustom(OcrRequestParams params, OnResultListener<OcrResponseResult> listener) {
        this.recognizeCommon(params, listener, "https://aip.baidubce.com/rest/2.0/solution/v1/iocr/recognise?");
    }

    public void recognizeCommon(final OcrRequestParams params, final OnResultListener<OcrResponseResult> listener, final String url) {
        File imageFile = params.getImageFile();
        final File tempImage = new File(this.context.getCacheDir(), String.valueOf(System.currentTimeMillis()));
        ImageUtil.resize(imageFile.getAbsolutePath(), tempImage.getAbsolutePath(), 1280, 1280);
        params.setImageFile(tempImage);
        final Parser<OcrResponseResult> ocrResultParser = new OcrResultParser();
        this.getToken(new OnResultListener() {
            public void onResult(Object result) {
                HttpUtil.getInstance().post(BaiDuOCR.this.urlAppendCommonParams(url), params, ocrResultParser, new OnResultListener<OcrResponseResult>() {
                    public void onResult(OcrResponseResult result) {
                        tempImage.delete();
                        if (listener != null) {
                            listener.onResult(result);
                        }

                    }

                    public void onError(OCRError error) {
                        tempImage.delete();
                        if (listener != null) {
                            listener.onError(error);
                        }

                    }
                });
            }

            public void onError(OCRError error) {
                listener.onError(error);
            }
        });
    }

    /** @deprecated */
    @Deprecated
    public void initWithToken(Context context, AccessToken token) {
        this.init(context);
        this.setAccessToken(token);
    }

    public void initAccessTokenWithAkSk(OnResultListener<AccessToken> listener, Context context, String ak, String sk) {
        this.authStatus = 2;
        this.ak = ak;
        this.sk = sk;
        this.init(context);
        AccessToken tokenFromCache = this.getByCache();
        if (tokenFromCache != null) {
            this.accessToken = tokenFromCache;
            listener.onResult(tokenFromCache);
            this.setLicense(tokenFromCache.getLic());
        } else {
            Throwable loadLibError = JniInterface.getLoadLibraryError();
            if (loadLibError != null) {
                SDKError e = new SDKError(283506, "Load jni so library error", loadLibError);
                listener.onError(e);
            } else {
                JniInterface jniInterface = new JniInterface();
                String hashSk = Util.md5(sk);
                byte[] buf = jniInterface.init(context, DeviceUtil.getDeviceInfo(context));
                String param = ak + ";" + hashSk + Base64.encodeToString(buf, 2);
                getHttpUtilAccessToken(listener, "https://verify.baidubce.com/verify/1.0/token/sk?sdkVersion=1_4_4", param);
            }
        }
    }

    /**
     * 从 httpUtil 类整合过来的 避免setAccessToken 空指针异常
     * @param listener
     * @param url
     * @param param
     */
    public void getHttpUtilAccessToken(final OnResultListener<AccessToken> listener, String url, String param) {
        final Parser<AccessToken> accessTokenParser = new AccessTokenParser();
        HttpsClient cl = new HttpsClient();
        HttpsClient.RequestBody body = new HttpsClient.RequestBody();
        body.setBody(param);
        HttpsClient.RequestInfo reqInfo = new HttpsClient.RequestInfo(url, body);
        reqInfo.setHeader("Content-Type", "text/html");
        reqInfo.build();
        cl.newCall(reqInfo).enqueue(new HttpsClient.Callback() {
            public void onFailure(Throwable e) {
                throwSDKError(listener, 283504, "Network error", e);
            }

            public void onResponse(String resultStr) {
                if (resultStr != null && !TextUtils.isEmpty(resultStr)) {
                    try {
                        AccessToken accessToken = (AccessToken)accessTokenParser.parse(resultStr);
                        if (accessToken != null) {
                            BaiDuOCR.getInstance((Context)null).setAccessToken(accessToken);
                            BaiDuOCR.getInstance((Context)null).setLicense(accessToken.getLic());
                            listener.onResult(accessToken);
                        } else {
                            throwSDKError(listener, 283505, "Server illegal response " + resultStr);
                        }
                    } catch (SDKError var3) {
                        listener.onError(var3);
                    } catch (Exception var4) {
                        throwSDKError(listener, 283505, "Server illegal response " + resultStr, var4);
                    }
                } else {
                    throwSDKError(listener, 283505, "Server illegal response " + resultStr);
                }
            }
        });
    }

    private static void throwSDKError(OnResultListener listener, int errorCode, String msg) {
        SDKError error = new SDKError(errorCode, msg);
        listener.onError(error);
    }

    private static void throwSDKError(OnResultListener listener, int errorCode, String msg, Throwable cause) {
        SDKError error = new SDKError(errorCode, msg, cause);
        listener.onError(error);
    }

    public String getLicense() {
        JniInterface jniInterface = new JniInterface();
        if (this.authStatus == 1) {
            return jniInterface.getToken(this.context);
        } else if (this.authStatus == 2 && this.license != null) {
            try {
                byte[] bin = Base64.decode(this.license, 0);
                String ret = jniInterface.getTokenFromLicense(this.context, bin, bin.length);
                return ret;
            } catch (Throwable var4) {
                return null;
            }
        } else {
            return null;
        }
    }

    public void initAccessToken(OnResultListener<AccessToken> listener, Context context) {
        this.initAccessTokenImpl(listener, (String)null, context);
    }

    public void initAccessToken(OnResultListener<AccessToken> listener, String licenseFile, Context context) {
        this.initAccessTokenImpl(listener, licenseFile, context);
    }

    private void initAccessTokenImpl(OnResultListener<AccessToken> listener, String licenseFile, Context context) {
        this.authStatus = 1;
        this.init(context);
        Throwable loadLibError = JniInterface.getLoadLibraryError();
        if (loadLibError != null) {
            SDKError e = new SDKError(283506, "Load jni so library error", loadLibError);
            listener.onError(e);
        } else {
            JniInterface jniInterface = new JniInterface();

            try {
                byte[] buf;
                if (licenseFile == null) {
                    buf = jniInterface.initWithBin(context, DeviceUtil.getDeviceInfo(context));
                } else {
                    buf = jniInterface.initWithBinLic(context, DeviceUtil.getDeviceInfo(context), licenseFile);
                }

                String param = Base64.encodeToString(buf, 2);
                AccessToken tokenFromCache = this.getByCache();
                if (tokenFromCache != null) {
                    this.accessToken = tokenFromCache;
                    listener.onResult(tokenFromCache);
                } else {
                    getHttpUtilAccessToken(listener, "https://verify.baidubce.com/verify/1.0/token/bin?sdkVersion=1_4_4", param);
                }
            } catch (OCRError var9) {
                listener.onError(var9);
            }

        }
    }

    private AccessToken getByCache() {
        if (!this.isAutoCacheToken) {
            return null;
        } else {
            SharedPreferences mSharedPreferences = this.context.getSharedPreferences("com.baidu.ocr.sdk", 0);
            String json = mSharedPreferences.getString("token_json", "");
            int type = mSharedPreferences.getInt("token_auth_type", 0);
            if (type != this.authStatus) {
                SharedPreferences.Editor editor = mSharedPreferences.edit();
                editor.clear();
                editor.commit();
                return null;
            } else {
                AccessTokenParser parser = new AccessTokenParser();

                try {
                    AccessToken token = parser.parse(json);
                    long expireTime = mSharedPreferences.getLong("token_expire_time", 0L);
                    token.setExpireTime(expireTime);
                    this.authStatus = type;
                    return token;
                } catch (SDKError var8) {
                    return null;
                }
            }
        }
    }

    private synchronized boolean isTokenInvalid() {
        return null == this.accessToken || this.accessToken.hasExpired();
    }

    private void getToken(final OnResultListener listener) {
        if (this.isTokenInvalid()) {
            if (this.authStatus == 2) {
                this.initAccessTokenWithAkSk(new OnResultListener<AccessToken>() {
                    public void onResult(AccessToken result) {
                        BaiDuOCR.this.setAccessToken(result);
                        listener.onResult(result);
                    }

                    public void onError(OCRError error) {
                        listener.onError(error);
                    }
                }, this.context, this.ak, this.sk);
            }

            if (this.authStatus == 1) {
                this.initAccessToken(new OnResultListener<AccessToken>() {
                    public void onResult(AccessToken result) {
                        BaiDuOCR.this.setAccessToken(result);
                        listener.onResult(result);
                    }

                    public void onError(OCRError error) {
                        listener.onError(error);
                    }
                }, this.context);
            }
        } else {
            listener.onResult(this.accessToken);
        }

    }

    private String urlAppendCommonParams(String url) {
        StringBuilder sb = new StringBuilder(url);
        sb.append("access_token=").append(this.getAccessToken().getAccessToken());
        sb.append("&aipSdk=Android");
        sb.append("&aipSdkVersion=").append("1_4_4");
        sb.append("&aipDevid=").append(DeviceUtil.getDeviceId(this.context));
        sb.append("&detect_photo=true");
        return sb.toString();
    }

    public void release() {
        HttpUtil.getInstance().release();
        this.crInst.release();
        this.crInst = null;
        this.context = null;
        if (instance != null) {
            instance = null;
        }

    }
}
