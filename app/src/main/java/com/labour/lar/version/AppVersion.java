package com.labour.lar.version;

/**
 * Created by Administrator on 2018/6/25.
 */

public class AppVersion {

    private int status;//":200,
    private String info;//":"成功",
    private Data data = new Data();

    public static class Data {
        private String code;//":"10", //版本code
        private String version;//":"1", //版本号
        private String note;//":"11",//更新内容
        private String download;//"

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
        }

        public String getDownload() {
            return download;
        }

        public void setDownload(String download) {
            this.download = download;
        }
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}
