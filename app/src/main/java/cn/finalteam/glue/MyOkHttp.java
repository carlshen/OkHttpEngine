package cn.finalteam.glue;

import android.content.Context;
import android.os.Handler;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Author:carlshen
 * Date:17/10/10 下午3:07
 */
public class MyOkHttp {

    public static void doLibOkHttpPost(Context context, String url, final RequestParams params, final JsonHttpResponseHandler responseHandler) {
        final Handler handler = new Handler();
        LibOkHttp.post(context, url, params, new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
//                        responseHandler.onFailure(0, "IOException");
                        responseHandler.onFailure(0, new Header[1], call.toString(), e.getCause());
                    }
                });
            }

            @Override
            public void onResponse(final Call call, final Response response) throws IOException {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        String response_body = "";
                        try {
                            response_body = response.body().string();
                            if(response.isSuccessful()) {
                                JSONObject body = new JSONObject(response_body);
                                responseHandler.onSuccess(response.code(), new Header[1], body);
                            } else {
                                JSONObject body = new JSONObject(response_body);
                                responseHandler.onFailure(response.code(), new Header[1], null, body);
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                            responseHandler.onFailure(response.code(), new Header[1], call.toString(), e.getCause());
                        } catch (JSONException e) {
                            e.printStackTrace();
                            responseHandler.onFailure(response.code(), new Header[1], call.toString(), e.getCause());
                        }
                    }
                });
            }
        });
    }

    public static void doLibOkHttpPost(Context context, String url, final RequestParams params, final MyHttpJsonResponseHandler responseHandler) {
        final Handler handler = new Handler();
        LibOkHttp.post(context, url, params, new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        responseHandler.onFailure(0, "IOException");
//                        responseHandler.onFailure(0, params, call.toString(), e.getCause());
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        String response_body = "";
                        try {
                            response_body = response.body().string();
                            if(response.isSuccessful()) {
                                JSONObject body = new JSONObject(response_body);
                                responseHandler.onSuccess(response.code(), body);
                            } else {
                                responseHandler.onFailure(response.code(), response_body);
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                            responseHandler.onFailure(response.code(), "IOException");
                        } catch (JSONException e) {
                            e.printStackTrace();
                            responseHandler.onFailure(response.code(), "parse json error:" + response_body);
                        }
                    }
                });
            }
        });
    }

    public static void doLibOkHttpGet(Context context, String url, Map<String, String> params, final MyHttpJsonResponseHandler responseHandler) {
        //拼接url
        if(params != null && params.size() > 0) {
            int i = 0;
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if(i++ == 0) {
                    url = url + "?" + entry.getKey() + "=" + entry.getValue();
                } else {
                    url = url + "&" + entry.getKey() + "=" + entry.getValue();
                }
            }
        }

        final Handler handler = new Handler();
        LibOkHttp.get(context, url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        responseHandler.onFailure(0, "IOException");
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        String response_body = "";
                        try {
                            response_body = response.body().string();
                            if(response.isSuccessful()) {
                                JSONObject body = new JSONObject(response_body);
                                responseHandler.onSuccess(response.code(), body);
                            } else {
                                responseHandler.onFailure(response.code(), response_body);
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                            responseHandler.onFailure(response.code(), "IOException");
                        } catch (JSONException e) {
                            e.printStackTrace();
                            responseHandler.onFailure(response.code(), "parse json error:" + response_body);
                        }
                    }
                });
            }
        });
    }
}
