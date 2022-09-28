package com.maishuo.tingshuohenhaowan.api.response;

/**
 * Create by yHai on 2021/1/27
 * EXPLAIN:
 */
public class VersionBean {
    /**
     * code : 200
     * data : {"versionCode":1,"packagename":"tingshuowan","size":"1111","name":"tingshuowan","desc":"tingshuowan","apkUrl":"http://baidu.com"}
     * msg :
     */


    /**
     * versionCode : 1
     * packagename : tingshuowan
     * size : 1111
     * name : tingshuowan
     * desc : tingshuowan
     * apkUrl : http://baidu.com
     */

    private int versionCode;
    private String versionName;
    private String packagename;
    private String size;
    private String title;
    private String content;
    private String apkUrl;
    private int isForce;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getIsForce() {
        return isForce;
    }

    public void setIsForce(int isForce) {
        this.isForce = isForce;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getPackagename() {
        return packagename;
    }

    public void setPackagename(String packagename) {
        this.packagename = packagename;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }


    public String getApkUrl() {
        return apkUrl;
    }

    public void setApkUrl(String apkUrl) {
        this.apkUrl = apkUrl;
    }
}
