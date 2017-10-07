package cn.finalteam.glue;

import android.content.Context;
import android.os.Handler;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import cz.msebera.android.httpclient.Header;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Author:carlshen
 * Date:17/10/10 下午3:07
 */
public class LibOkHttp {
    private static OkHttpClient client;
    private static Handler handler;

    public static void post(Context context, String url, RequestParams requestParams, final JsonHttpResponseHandler responseHandler) {
        if(client == null) {
            client = new OkHttpClient();
        }
        if (handler == null) {
            handler = new Handler();
        }
        Callback callback = new Callback() {
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
        };
        Map<String, String> params = new HashMap<>();
        FormBody.Builder builder = new FormBody.Builder();
        try {
            Class<? extends RequestParams> myParams = requestParams.getClass();
            Field fieldUrl = myParams.getDeclaredField("urlParams");
            fieldUrl.setAccessible(true);
            Object object = fieldUrl.get(myParams);
            params = (ConcurrentHashMap<String, String>)(object);
            if(params != null && params.size() > 0) {
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    builder.add(entry.getKey(), entry.getValue());
                }
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        Request request = new Request.Builder()
                .url(url)
                .post(builder.build())
                .tag(context)
                .build();

        client.newCall(request).enqueue(callback);

    }

    public static void post(Context context, String url, RequestParams requestParams, Callback callback) {
        if(client == null) {
            client = new OkHttpClient();
        }

        Map<String, String> params = new HashMap<>();
        FormBody.Builder builder = new FormBody.Builder();
        try {
            Class<? extends RequestParams> myParams = requestParams.getClass();
            Field fieldUrl = myParams.getDeclaredField("urlParams");
            fieldUrl.setAccessible(true);
            Object object = fieldUrl.get(myParams);
            params = (ConcurrentHashMap<String, String>)(object);
            if(params != null && params.size() > 0) {
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    builder.add(entry.getKey(), entry.getValue());
                }
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        Request request = new Request.Builder()
                .url(url)
                .post(builder.build())
                .tag(context)
                .build();

        client.newCall(request).enqueue(callback);

    }

    public static void get(Context context, String url, Callback callback) {
        if(client == null) {
            client = new OkHttpClient();
        }

        Request request = new Request.Builder()
                .url(url)
                .tag(context)
                .build();

        client.newCall(request).enqueue(callback);
    }

    /**
     * 取消当前context的所有请求
     * @param context 当前所在context
     */
    public static void cancelRequest(Context context) {
        if(client != null) {
            for(Call call : client.dispatcher().queuedCalls()) {
                if(call.request().tag().equals(context))
                    call.cancel();
            }
            for(Call call : client.dispatcher().runningCalls()) {
                if(call.request().tag().equals(context))
                    call.cancel();
            }
        }
    }
}
