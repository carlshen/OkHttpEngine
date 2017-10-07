package cn.finalteam.glue;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * MyHttpResponse回调 Json数据
 * Author:carlshen
 * Date:17/10/10 下午3:07
 */
public abstract class MyHttpJsonResponseHandler {

    /**
     * 成功返回
     * @param statusCode
     * @param response
     */
    public abstract void onSuccess(int statusCode, JSONObject response);
    public abstract void onSuccess(int statusCode, Header[] headers, JSONObject response);

    /**
     * 失败返回
     * @param statusCode
     * @param error_msg
     */
    public abstract void onFailure(int statusCode, String error_msg);
    public abstract void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable);
    public abstract void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse);

    /**
     * 请求被取消
     */
    public void onCancel() {
        //Log.v("myhttp", "request on cancel");
    }
}
