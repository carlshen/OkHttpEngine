package cn.finalteam.glue;

import android.util.Log;

/**
 * 文件下载responseHandler
 * Author:carlshen
 * Date:17/10/10 下午3:07
 */
public abstract class MyHttpFileResponseHandler {
    /**
     * 成功返回
     * @param statusCode
     */
    public abstract void onSuccess(int statusCode);

    /**
     * 失败返回
     * @param statusCode
     * @param error_msg
     */
    public abstract void onFailure(int statusCode, String error_msg);

    /**
     * 下载进度
     * @param bytesWritten 已经长传字节
     * @param totalSize 总字节
     */
    public abstract void onProgress(long bytesWritten, long totalSize);

    /**
     * 请求被取消
     */
    public void onCancel() {
        Log.v("myhttp", "request on cancel");
    }
}
