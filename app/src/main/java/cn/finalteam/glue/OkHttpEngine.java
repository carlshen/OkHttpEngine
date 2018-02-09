package cn.finalteam.glue;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import cz.msebera.android.httpclient.message.BasicHeader;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttpEngine {

    private static String TAG = "OkHttpEngine";
    protected static final int BUFFER_SIZE = 4096;

    private static final String ACCESSTOKEN = "AccessToken";
    private static OkHttpEngine mInstance;
    private static OkHttpClient mOkHttpClient;
    private Headers headers;

    public static OkHttpEngine getInstance() {
        if (mInstance == null) {
            synchronized (OkHttpEngine.class) {
                if (mInstance == null) {
                    mInstance = new OkHttpEngine();
                }
            }
        }
        return mInstance;
    }

    private OkHttpEngine() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS);
        mOkHttpClient = builder.build();
        headers = null;
    }

    public void setOkHttpClient(OkHttpClient mHttpClient) {
        mOkHttpClient = mHttpClient;
    }

    public void cancelRequest(Context context) {
        if (mOkHttpClient != null) {
            for (Call call : mOkHttpClient.dispatcher().queuedCalls()) {
                if (call.request().tag().equals(context))
                    call.cancel();
            }
            for (Call call : mOkHttpClient.dispatcher().runningCalls()) {
                if (call.request().tag().equals(context))
                    call.cancel();
            }
        }
    }

    public void addHeader(String name, String value) {
        headers = Headers.of(name, value);
    }

    private RequestBody FormBody4Request(Map<String, String> params) {
        FormBody.Builder builder = new FormBody.Builder();
        if (params != null && params.size() > 0) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder.add(entry.getKey(), entry.getValue());
            }
        }
        return builder.build();
    }

    private void onFailResult(final Call call, final IOException e, final JsonHttpResponseHandler responseHandler) {
        Handler mHandler;
        if (Looper.myLooper() == null) {
            mHandler = new Handler(Looper.getMainLooper());
        } else {
            mHandler = new Handler(Looper.myLooper());
        }
        if (responseHandler == null) {
            return;
        }
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (call == null) {
                    if (e == null) {
                        responseHandler.onFailure(400, null, "", null);
                    } else {
                        responseHandler.onFailure(400, null, "", e.getCause());
                    }
                    return;
                }
                Request callRequest = call.request();
                if (callRequest != null && callRequest.headers() != null && callRequest.headers().size() > 0) {
                    Headers headers = callRequest.headers();
                    BasicHeader retHeader[] = new BasicHeader[headers.size()];
                    for (int len = 0; len < headers.size(); len++) {
                        retHeader[len] = new BasicHeader(headers.name(len), headers.value(len));
                    }
                    if (e == null) {
                        responseHandler.onFailure(400, retHeader, call.toString(), null);
                    } else {
                        responseHandler.onFailure(400, retHeader, call.toString(), e.getCause());
                    }
                } else {
                    if (e == null) {
                        responseHandler.onFailure(400, null, call.toString(), null);
                    } else {
                        responseHandler.onFailure(400, null, call.toString(), e.getCause());
                    }
                }
            }
        });
    }

    private void onSuccessResult(final Call call, final Response response, final JsonHttpResponseHandler responseHandler) {
        Handler mHandler;
        if (Looper.myLooper() == null) {
            mHandler = new Handler(Looper.getMainLooper());
        } else {
            mHandler = new Handler(Looper.myLooper());
        }
        if (responseHandler == null) {
            return;
        }
        if (response == null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (call == null) {
                        responseHandler.onFailure(500, null, "", null);
                    } else {
                        responseHandler.onFailure(500, null, call.toString(), null);
                    }
                }
            });
        } else {
            try {
                final String response_body = response.body().string();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject body = new JSONObject(response_body);
                            if (response.isSuccessful()) {
                                Headers headers = response.headers();
                                if (headers != null && headers.size() > 0) {
                                    Log.d(TAG, "onSuccessResult Headers size = " + headers.size());
                                    BasicHeader retHeader[] = new BasicHeader[headers.size()];
                                    for (int len = 0; len < headers.size(); len++) {
                                        retHeader[len] = new BasicHeader(headers.name(len), headers.value(len));
                                    }
                                    responseHandler.onSuccess(response.code(), retHeader, body);
                                } else {
                                    Log.d(TAG, "onSuccessResult Headers is null.");
                                    responseHandler.onSuccess(response.code(), null, body);
                                }
                            } else {
                                Headers headers = response.headers();
                                if (headers != null && headers.size() > 0) {
                                    Log.d(TAG, "onSuccessResult onFailure Headers size = " + headers.size());
                                    BasicHeader retHeader[] = new BasicHeader[headers.size()];
                                    for (int len = 0; len < headers.size(); len++) {
                                        retHeader[len] = new BasicHeader(headers.name(len), headers.value(len));
                                    }
                                    responseHandler.onFailure(response.code(), retHeader, null, body);
                                } else {
                                    Log.d(TAG, "onSuccessResult onFailure Headers is null.");
                                    responseHandler.onFailure(response.code(), null, null, body);
                                }
                            }
                            return;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        responseHandler.onFailure(response.code(), null, response_body, null);
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        responseHandler.onFailure(response.code(), null, "", null);
                    }
                });
            }
        }
    }

    private void onFailResult(final Call call, final IOException e, final BaseAsyncHttpResponseHandler responseHandler) {
        Handler mHandler;
        if (Looper.myLooper() == null) {
            mHandler = new Handler(Looper.getMainLooper());
        } else {
            mHandler = new Handler(Looper.myLooper());
        }
        if (responseHandler == null) {
            return;
        }
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (call == null) {
                    if (e == null) {
                        responseHandler.onFailure(400, null, "", null);
                    } else {
                        responseHandler.onFailure(400, null, "", e.getCause());
                    }
                    return;
                }
                Request callRequest = call.request();
                HashMap<String, Object> responseObjects = new HashMap<String, Object>();
                if (callRequest != null && callRequest.headers() != null && callRequest.headers().size() > 0) {
                    Headers headers = callRequest.headers();
                    for (int len = 0; len < headers.size(); len++) {
                        responseObjects.put(headers.name(len), headers.value(len));
                    }
                    responseHandler.onFailure(400, responseObjects);
                } else {
                    responseHandler.onFailure(400, responseObjects);
                }
            }
        });
    }

    private void onSuccessResult(final Call call, final Response response, final BaseAsyncHttpResponseHandler responseHandler) {
        Handler mHandler;
        if (Looper.myLooper() == null) {
            mHandler = new Handler(Looper.getMainLooper());
        } else {
            mHandler = new Handler(Looper.myLooper());
        }
        if (responseHandler == null) {
            return;
        }
        if (response == null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (call == null) {
                        responseHandler.onFailure(500, null, "", null);
                    } else {
                        responseHandler.onFailure(500, null, call.toString(), null);
                    }
                }
            });
        } else {
            try {
                final String response_body = response.body().string();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject body = new JSONObject(response_body);
                            if (response.isSuccessful()) {
                                responseHandler.onSuccess(response.code(), responseHandler.parseResponseObjects(body));
                            } else {
                                responseHandler.onFailure(response.code(), responseHandler.parseResponseObjects(body));
                            }
                            return;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        responseHandler.onFailure(response.code(), null);
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        responseHandler.onFailure(response.code(), null);
                    }
                });
            }
        }
    }

    private void onSyncFailResult(Request callRequest, Response response, BaseAsyncHttpResponseHandler responseHandler) {
        if (responseHandler == null) {
            return;
        }
        if (response == null) {
            if (callRequest == null) {
                responseHandler.onFailure(400, null, "", null);
            } else {
                responseHandler.onFailure(400, null, callRequest.toString(), null);
            }
            return;
        }
        try {
            JSONObject body = new JSONObject(response.body().string());
            responseHandler.onFailure(response.code(), responseHandler.parseResponseObjects(body));
            return;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        responseHandler.onFailure(response.code(), null);
    }

    private void onSyncSuccessResult(Response response, BaseAsyncHttpResponseHandler responseHandler) throws JSONException, IOException {
        if (responseHandler == null) {
            return;
        }
        if (response == null) {
            responseHandler.onFailure(500, null, "", null);
            return;
        }
        try {
            JSONObject body = new JSONObject(response.body().string());
            if (response.isSuccessful()) {
                responseHandler.onSuccess(response.code(), responseHandler.parseResponseObjects(body));
            } else {
                responseHandler.onFailure(response.code(), responseHandler.parseResponseObjects(body));
            }
            return;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        responseHandler.onFailure(response.code(), null);
    }

    private void onSyncFailResult(Request callRequest, Response response, JsonHttpResponseHandler responseHandler) {
        if (responseHandler == null) {
            return;
        }
        String response_body = "";
        if (response == null) {
            if (callRequest == null) {
                responseHandler.onFailure(400, null, "", null);
            } else {
                responseHandler.onFailure(400, null, callRequest.toString(), null);
            }
            return;
        }
        try {
            response_body = response.body().string();
            JSONObject body = new JSONObject(response_body);
            Headers headers = callRequest.headers();
            if (headers != null && headers.size() > 0) {
                Log.d(TAG, "onSyncFailResult onFailure Headers size = " + headers.size());
                BasicHeader retHeader[] = new BasicHeader[headers.size()];
                for (int len = 0; len < headers.size(); len++) {
                    retHeader[len] = new BasicHeader(headers.name(len), headers.value(len));
                }
                responseHandler.onFailure(response.code(), retHeader, null, body);
            } else {
                Log.d(TAG, "onSyncFailResult onFailure Headers is null.");
                responseHandler.onFailure(response.code(), null, null, body);
            }
            return;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        responseHandler.onFailure(response.code(), null, response_body, null);
    }

    private void onSyncSuccessResult(Response response, JsonHttpResponseHandler responseHandler) throws JSONException, IOException {
        if (responseHandler == null) {
            return;
        }
        String response_body = "";
        if (response == null) {
            responseHandler.onFailure(500, null, response_body, null);
            return;
        }
        try {
            response_body = response.body().string();
            JSONObject body = new JSONObject(response_body);
            if (response.isSuccessful()) {
                Headers headers = response.headers();
                if (headers != null && headers.size() > 0) {
                    Log.d(TAG, "onSyncSuccessResult Headers size = " + headers.size());
                    BasicHeader retHeader[] = new BasicHeader[headers.size()];
                    for (int len = 0; len < headers.size(); len++) {
                        retHeader[len] = new BasicHeader(headers.name(len), headers.value(len));
                    }
                    responseHandler.onSuccess(response.code(), retHeader, body);
                } else {
                    Log.d(TAG, "onSyncSuccessResult Headers is null.");
                    responseHandler.onSuccess(response.code(), null, body);
                }
            } else {
                Headers headers = response.headers();
                if (headers != null && headers.size() > 0) {
                    Log.d(TAG, "onSyncSuccessResult onFailure Headers size = " + headers.size());
                    BasicHeader retHeader[] = new BasicHeader[headers.size()];
                    for (int len = 0; len < headers.size(); len++) {
                        retHeader[len] = new BasicHeader(headers.name(len), headers.value(len));
                    }
                    responseHandler.onFailure(response.code(), retHeader, null, body);
                } else {
                    Log.d(TAG, "onSyncSuccessResult onFailure Headers is null.");
                    responseHandler.onFailure(response.code(), null, null, body);
                }
            }
            return;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        responseHandler.onFailure(response.code(), null, response_body, null);
    }

    private void onFailResult(final Call call, final IOException e, final FileAsyncHttpResponseHandler responseHandler) {
        Handler mHandler;
        if (Looper.myLooper() == null) {
            mHandler = new Handler(Looper.getMainLooper());
        } else {
            mHandler = new Handler(Looper.myLooper());
        }
        if (responseHandler == null) {
            return;
        }
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (call == null) {
                    if (e == null) {
                        responseHandler.onFailure(400, null, new byte[1], null);
                    } else {
                        responseHandler.onFailure(400, null, null, e.getCause());
                    }
                    return;
                } else {
                    Request callRequest = call.request();
                    if (callRequest != null && callRequest.headers() != null && callRequest.headers().size() > 0) {
                        Headers headers = callRequest.headers();
                        BasicHeader retHeader[] = new BasicHeader[headers.size()];
                        for (int len = 0; len < headers.size(); len++) {
                            retHeader[len] = new BasicHeader(headers.name(len), headers.value(len));
                        }
                        if (e == null) {
                            responseHandler.onFailure(400, retHeader, null, responseHandler.getTargetFile());
                        } else {
                            responseHandler.onFailure(400, retHeader, e.getCause(), responseHandler.getTargetFile());
                        }
                    } else {
                        if (e == null) {
                            responseHandler.onFailure(400, null, null, responseHandler.getTargetFile());
                        } else {
                            responseHandler.onFailure(400, null, e.getCause(), responseHandler.getTargetFile());
                        }
                    }
                }
            }
        });
    }

    private void onSuccessResult(final Call call, final Response response, final FileAsyncHttpResponseHandler responseHandler) {
        Handler mHandler;
        if (Looper.myLooper() == null) {
            mHandler = new Handler(Looper.getMainLooper());
        } else {
            mHandler = new Handler(Looper.myLooper());
        }
        if (responseHandler == null) {
            return;
        }
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (response == null) {
                    responseHandler.onFailure(500, null, null, responseHandler.getTargetFile());
                    return;
                }
                if (response.isSuccessful()) {
                    Headers headers = response.headers();
                    if (headers != null && headers.size() > 0) {
                        Log.d(TAG, "onSuccessResult Headers size = " + headers.size());
                        BasicHeader retHeader[] = new BasicHeader[headers.size()];
                        for (int len = 0; len < headers.size(); len++) {
                            retHeader[len] = new BasicHeader(headers.name(len), headers.value(len));
                        }
                        responseHandler.onSuccess(response.code(), retHeader, responseHandler.getTargetFile());
                    } else {
                        Log.d(TAG, "onSuccessResult Headers is null.");
                        responseHandler.onSuccess(response.code(), null, responseHandler.getTargetFile());
                    }
                } else {
                    Headers headers = response.headers();
                    if (headers != null && headers.size() > 0) {
                        Log.d(TAG, "onSuccessResult onFailure Headers size = " + headers.size());
                        BasicHeader retHeader[] = new BasicHeader[headers.size()];
                        for (int len = 0; len < headers.size(); len++) {
                            retHeader[len] = new BasicHeader(headers.name(len), headers.value(len));
                        }
                        responseHandler.onFailure(response.code(), retHeader, null, responseHandler.getTargetFile());
                    } else {
                        Log.d(TAG, "onSuccessResult onFailure Headers is null.");
                        responseHandler.onFailure(response.code(), null, null, responseHandler.getTargetFile());
                    }
                }
            }
        });
    }

    private void onSyncFailResult(Response response, IOException e, FileAsyncHttpResponseHandler responseHandler) {
        if (responseHandler == null) {
            return;
        }
        if (response == null) {
            if (e == null) {
                responseHandler.onFailure(400, null, null, responseHandler.getTargetFile());
            } else {
                responseHandler.onFailure(400, null, e.getCause(), responseHandler.getTargetFile());
            }
            return;
        } else {
            Headers headers = response.headers();
            if (headers != null && headers.size() > 0) {
                BasicHeader retHeader[] = new BasicHeader[headers.size()];
                for (int len = 0; len < headers.size(); len++) {
                    retHeader[len] = new BasicHeader(headers.name(len), headers.value(len));
                }
                if (e == null) {
                    responseHandler.onFailure(response.code(), retHeader, null, responseHandler.getTargetFile());
                } else {
                    responseHandler.onFailure(response.code(), retHeader, e.getCause(), responseHandler.getTargetFile());
                }
            } else {
                if (e == null) {
                    responseHandler.onFailure(response.code(), null, null, responseHandler.getTargetFile());
                } else {
                    responseHandler.onFailure(response.code(), null, e.getCause(), responseHandler.getTargetFile());
                }
            }
        }
    }

    private void onSyncSuccessResult(Response response, FileAsyncHttpResponseHandler responseHandler) {
        if (responseHandler == null) {
            return;
        }
        if (response == null) {
            responseHandler.onFailure(500, null, null, responseHandler.getTargetFile());
            return;
        }
        if (response.isSuccessful()) {
            Headers headers = response.headers();
            if (headers != null && headers.size() > 0) {
                Log.d(TAG, "onSuccessResult Headers size = " + headers.size());
                BasicHeader retHeader[] = new BasicHeader[headers.size()];
                for (int len = 0; len < headers.size(); len++) {
                    retHeader[len] = new BasicHeader(headers.name(len), headers.value(len));
                }
                responseHandler.onSuccess(response.code(), retHeader, responseHandler.getTargetFile());
            } else {
                Log.d(TAG, "onSuccessResult Headers is null.");
                responseHandler.onSuccess(response.code(), null, responseHandler.getTargetFile());
            }
        } else {
            Headers headers = response.headers();
            if (headers != null && headers.size() > 0) {
                Log.d(TAG, "onSuccessResult onFailure Headers size = " + headers.size());
                BasicHeader retHeader[] = new BasicHeader[headers.size()];
                for (int len = 0; len < headers.size(); len++) {
                    retHeader[len] = new BasicHeader(headers.name(len), headers.value(len));
                }
                responseHandler.onFailure(response.code(), retHeader, null, responseHandler.getTargetFile());
            } else {
                Log.d(TAG, "onSuccessResult onFailure Headers is null.");
                responseHandler.onFailure(response.code(), null, null, responseHandler.getTargetFile());
            }
        }
    }

    // get request methods
    public void get(Context context, String url, Map<String, String> requestParams, final JsonHttpResponseHandler responseHandler) {
        headers = Headers.of(requestParams);
        Request request = new Request.Builder().tag(context).url(Utils.getFullUrl(url, requestParams)).get().build();
        Callback callback = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                onFailResult(call, e, responseHandler);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                onSuccessResult(call, response, responseHandler);
            }
        };
        mOkHttpClient.newCall(request).enqueue(callback);
    }

    public void get(Context context, String url, Map<String, String> requestParams, final JsonHttpResponseHandler responseHandler, boolean async) {
        headers = Headers.of(requestParams);
        Request request = new Request.Builder().tag(context).url(Utils.getFullUrl(url, requestParams)).get().build();
        Callback callback = new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                onFailResult(call, e, responseHandler);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                onSuccessResult(call, response, responseHandler);
            }
        };
        if (async) {
            mOkHttpClient.newCall(request).enqueue(callback);
        } else {
            Response response = null;
            try {
                response = mOkHttpClient.newCall(request).execute();
                onSyncSuccessResult(response, responseHandler);
                return;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            onSyncFailResult(request, response, responseHandler);
        }
    }

    public void get(Context context, String token, String url, Map<String, String> requestParams, final JsonHttpResponseHandler responseHandler) {
        Request request;
        if (TextUtils.isEmpty(token)) {
            request = new Request.Builder().tag(context).url(Utils.getFullUrl(url, requestParams)).get().build();
        } else {
            headers = Headers.of(ACCESSTOKEN, token);
            request = new Request.Builder().tag(context).url(Utils.getFullUrl(url, requestParams)).header(ACCESSTOKEN, token).get().build();
        }
        Callback callback = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                onFailResult(call, e, responseHandler);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                onSuccessResult(call, response, responseHandler);
            }
        };
        mOkHttpClient.newCall(request).enqueue(callback);
    }

    public void get(Context context, String token, String url, Map<String, String> requestParams, final JsonHttpResponseHandler responseHandler, boolean async) {
        Request request;
        if (TextUtils.isEmpty(token)) {
            request = new Request.Builder().tag(context).url(Utils.getFullUrl(url, requestParams)).get().build();
        } else {
            headers = Headers.of(ACCESSTOKEN, token);
            request = new Request.Builder().tag(context).url(Utils.getFullUrl(url, requestParams)).header(ACCESSTOKEN, token).get().build();
        }
        Callback callback = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                onFailResult(call, e, responseHandler);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                onSuccessResult(call, response, responseHandler);
            }
        };
        if (async) {
            mOkHttpClient.newCall(request).enqueue(callback);
        } else {
            Response response = null;
            try {
                response = mOkHttpClient.newCall(request).execute();
                onSyncSuccessResult(response, responseHandler);
                return;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            onSyncFailResult(request, response, responseHandler);
        }
    }

    // download file
    public void get(Context context, String url, Map<String, String> requestParams, final FileAsyncHttpResponseHandler responseHandler, boolean async) {
        headers = Headers.of(requestParams);
        Request request = new Request.Builder().tag(context).url(Utils.getFullUrl(url, requestParams)).get().build();
        Callback callback = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                onFailResult(call, e, responseHandler);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response == null) {
                    throw new IOException("response is null.");
                }
                InputStream inputStream = response.body().byteStream();
                FileOutputStream fileOutputStream = null;
                if (inputStream != null) {
                    try {
                        fileOutputStream = new FileOutputStream(responseHandler.getTargetFile());
                        byte[] buffer = new byte[BUFFER_SIZE];
                        int len = 0;
                        while ((len = inputStream.read(buffer)) != -1) {
                            fileOutputStream.write(buffer, 0, len);
                        }
                        fileOutputStream.flush();
                        fileOutputStream.close();
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        onFailResult(call, e, responseHandler);
                    } finally {
                        if (fileOutputStream != null) {
                            fileOutputStream.close();
                        }
                        if (inputStream != null) {
                            inputStream.close();
                        }
                    }
                    onSuccessResult(call, response, responseHandler);
                    return;
                } else {
                    onFailResult(call, null, responseHandler);
                }
            }
        };
        if (async) {
            mOkHttpClient.newCall(request).enqueue(callback);
        } else {
            Response response = null;
            try {
                response = mOkHttpClient.newCall(request).execute();
                if (response == null) {
                    throw new IOException("response is null.");
                }
                InputStream inputStream = response.body().byteStream();
                FileOutputStream fileOutputStream = null;
                if (inputStream != null) {
                    try {
                        fileOutputStream = new FileOutputStream(responseHandler.getTargetFile());
                        byte[] buffer = new byte[BUFFER_SIZE];
                        int len = 0;
                        while ((len = inputStream.read(buffer)) != -1) {
                            fileOutputStream.write(buffer, 0, len);
                        }
                        fileOutputStream.flush();
                        fileOutputStream.close();
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        onSyncFailResult(response, e, responseHandler);
                    } finally {
                        if (fileOutputStream != null) {
                            fileOutputStream.close();
                        }
                        if (inputStream != null) {
                            inputStream.close();
                        }
                    }
                    onSyncSuccessResult(response, responseHandler);
                    return;
                }
            } catch (IOException e) {
                e.printStackTrace();
                onSyncFailResult(response, e, responseHandler);
            }
            onSyncFailResult(response, null, responseHandler);
        }
    }

    public void get(Context context, String token, String url, Map<String, String> requestParams, final FileAsyncHttpResponseHandler responseHandler) {
        Request request;
        if (TextUtils.isEmpty(token)) {
            request = new Request.Builder().tag(context).url(Utils.getFullUrl(url, requestParams)).get().build();
        } else {
            headers = Headers.of(ACCESSTOKEN, token);
            request = new Request.Builder().tag(context).url(Utils.getFullUrl(url, requestParams)).header(ACCESSTOKEN, token).get().build();
        }
        Callback callback = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                onFailResult(call, e, responseHandler);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response == null) {
                    throw new IOException("response is null.");
                }
                InputStream inputStream = response.body().byteStream();
                FileOutputStream fileOutputStream = null;
                if (inputStream != null) {
                    try {
                        fileOutputStream = new FileOutputStream(responseHandler.getTargetFile());
                        byte[] buffer = new byte[BUFFER_SIZE];
                        int len = 0;
                        while ((len = inputStream.read(buffer)) != -1) {
                            fileOutputStream.write(buffer, 0, len);
                        }
                        fileOutputStream.flush();
                        fileOutputStream.close();
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        onFailResult(call, e, responseHandler);
                    } finally {
                        if (fileOutputStream != null) {
                            fileOutputStream.close();
                        }
                        if (inputStream != null) {
                            inputStream.close();
                        }
                    }
                    onSuccessResult(call, response, responseHandler);
                    return;
                } else {
                    onFailResult(call, null, responseHandler);
                }
            }
        };
        mOkHttpClient.newCall(request).enqueue(callback);
    }

    // post request methods
    public void post(Context context, String url, Map<String, String> requestParams, final JsonHttpResponseHandler responseHandler) {
        Request request = new Request.Builder().tag(context).url(url).post(FormBody4Request(requestParams)).build();
        Callback callback = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                onFailResult(call, e, responseHandler);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                onSuccessResult(call, response, responseHandler);
            }
        };
        mOkHttpClient.newCall(request).enqueue(callback);
    }

    public void post(Context context, String url, Map<String, String> requestParams, final JsonHttpResponseHandler responseHandler, boolean async) {
        Request request = new Request.Builder().tag(context).url(url).post(FormBody4Request(requestParams)).build();
        Callback callback = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                onFailResult(call, e, responseHandler);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                onSuccessResult(call, response, responseHandler);
            }
        };
        if (async) {
            mOkHttpClient.newCall(request).enqueue(callback);
        } else {
            Response response = null;
            try {
                response = mOkHttpClient.newCall(request).execute();
                onSyncSuccessResult(response, responseHandler);
                return;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            onSyncFailResult(request, response, responseHandler);
        }
    }

    public void post(Context context, String token, String url, Map<String, String> requestParams, final JsonHttpResponseHandler responseHandler) {
        Request request;
        if (TextUtils.isEmpty(token)) {
            request = new Request.Builder().tag(context).url(url).post(FormBody4Request(requestParams)).build();
        } else {
            headers = Headers.of(ACCESSTOKEN, token);
            request = new Request.Builder().tag(context).addHeader(ACCESSTOKEN, token).url(url).post(FormBody4Request(requestParams)).build();
        }
        Callback callback = new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                onFailResult(call, e, responseHandler);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                onSuccessResult(call, response, responseHandler);
            }
        };
        mOkHttpClient.newCall(request).enqueue(callback);
    }

    public void post(Context context, String token, String url, Map<String, String> requestParams, final JsonHttpResponseHandler responseHandler, boolean async) {
        Request request;
        if (TextUtils.isEmpty(token)) {
            request = new Request.Builder().tag(context).url(url).post(FormBody4Request(requestParams)).build();
        } else {
            headers = Headers.of(ACCESSTOKEN, token);
            request = new Request.Builder().tag(context).addHeader(ACCESSTOKEN, token).url(url).post(FormBody4Request(requestParams)).build();
        }
        Callback callback = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                onFailResult(call, e, responseHandler);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                onSuccessResult(call, response, responseHandler);
            }
        };
        if (async) {
            mOkHttpClient.newCall(request).enqueue(callback);
        } else {
            Response response = null;
            try {
                response = mOkHttpClient.newCall(request).execute();
                onSyncSuccessResult(response, responseHandler);
                return;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            onSyncFailResult(request, response, responseHandler);
        }
    }

    public void post(Context context, String token, String url, Map<String, String> requestParams, final BaseAsyncHttpResponseHandler responseHandler, boolean async) {
        Request request;
        if (TextUtils.isEmpty(token)) {
            request = new Request.Builder().tag(context).url(url).post(FormBody4Request(requestParams)).build();
        } else {
            headers = Headers.of(ACCESSTOKEN, token);
            request = new Request.Builder().tag(context).addHeader(ACCESSTOKEN, token).url(url).post(FormBody4Request(requestParams)).build();
        }
        Callback callback = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                onFailResult(call, e, responseHandler);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                onSuccessResult(call, response, responseHandler);
            }
        };
        if (async) {
            mOkHttpClient.newCall(request).enqueue(callback);
        } else {
            Response response = null;
            try {
                response = mOkHttpClient.newCall(request).execute();
                onSyncSuccessResult(response, responseHandler);
                return;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            onSyncFailResult(request, response, responseHandler);
        }
    }

    public void post(Context context, String token, String url, MultipartBody requestParams, final JsonHttpResponseHandler responseHandler, boolean async) {
        Request request;
        if (TextUtils.isEmpty(token)) {
            request = new Request.Builder().tag(context).url(url).post(requestParams).build();
        } else {
            headers = Headers.of(ACCESSTOKEN, token);
            request = new Request.Builder().tag(context).addHeader(ACCESSTOKEN, token).url(url).post(requestParams).build();
        }
        Callback callback = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                onFailResult(call, e, responseHandler);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                onSuccessResult(call, response, responseHandler);
            }
        };
        if (async) {
            mOkHttpClient.newCall(request).enqueue(callback);
        } else {
            Response response = null;
            try {
                response = mOkHttpClient.newCall(request).execute();
                onSyncSuccessResult(response, responseHandler);
                return;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            onSyncFailResult(request, response, responseHandler);
        }
    }

    public void put(Context context, String url, Map<String, String> requestParams, final JsonHttpResponseHandler responseHandler) {
        Request request = new Request.Builder().tag(context).url(url).put(FormBody4Request(requestParams)).build();
        Callback callback = new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                onFailResult(call, e, responseHandler);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                onSuccessResult(call, response, responseHandler);
            }
        };
        mOkHttpClient.newCall(request).enqueue(callback);
    }

    public void put(Context context, String url, Map<String, String> requestParams, final JsonHttpResponseHandler responseHandler, boolean async) {
        Request request = new Request.Builder().tag(context).url(url).put(FormBody4Request(requestParams)).build();
        Callback callback = new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                onFailResult(call, e, responseHandler);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                onSuccessResult(call, response, responseHandler);
            }
        };
        if (async) {
            mOkHttpClient.newCall(request).enqueue(callback);
        } else {
            Response response = null;
            try {
                response = mOkHttpClient.newCall(request).execute();
                onSyncSuccessResult(response, responseHandler);
                return;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            onSyncFailResult(request, response, responseHandler);
        }
    }

    public void put(Context context, String token, String url, Map<String, String> requestParams, final JsonHttpResponseHandler responseHandler) {
        Request request;
        if (TextUtils.isEmpty(token)) {
            request = new Request.Builder().tag(context).url(url).put(FormBody4Request(requestParams)).build();
        } else {
            headers = Headers.of(ACCESSTOKEN, token);
            request = new Request.Builder().tag(context).addHeader(ACCESSTOKEN, token).url(url).put(FormBody4Request(requestParams)).build();
        }
        Callback callback = new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                onFailResult(call, e, responseHandler);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                onSuccessResult(call, response, responseHandler);
            }
        };
        mOkHttpClient.newCall(request).enqueue(callback);
    }

    public void put(Context context, String token, String url, Map<String, String> requestParams, final JsonHttpResponseHandler responseHandler, boolean async) {
        Request request;
        if (TextUtils.isEmpty(token)) {
            request = new Request.Builder().tag(context).url(url).put(FormBody4Request(requestParams)).build();
        } else {
            headers = Headers.of(ACCESSTOKEN, token);
            request = new Request.Builder().tag(context).addHeader(ACCESSTOKEN, token).url(url).put(FormBody4Request(requestParams)).build();
        }
        Callback callback = new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                onFailResult(call, e, responseHandler);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                onSuccessResult(call, response, responseHandler);
            }
        };
        if (async) {
            mOkHttpClient.newCall(request).enqueue(callback);
        } else {
            Response response = null;
            try {
                response = mOkHttpClient.newCall(request).execute();
                onSyncSuccessResult(response, responseHandler);
                return;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            onSyncFailResult(request, response, responseHandler);
        }
    }
    public void put(Context context, String token, String url, MultipartBody requestParams, final JsonHttpResponseHandler responseHandler, boolean async) {
        Request request;
        if (TextUtils.isEmpty(token)) {
            request = new Request.Builder().tag(context).url(url).put(requestParams).build();
        } else {
            headers = Headers.of(ACCESSTOKEN, token);
            request = new Request.Builder().tag(context).addHeader(ACCESSTOKEN, token).url(url).put(requestParams).build();
        }
        Callback callback = new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                onFailResult(call, e, responseHandler);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                onSuccessResult(call, response, responseHandler);
            }
        };
        if (async) {
            mOkHttpClient.newCall(request).enqueue(callback);
        } else {
            Response response = null;
            try {
                response = mOkHttpClient.newCall(request).execute();
                onSyncSuccessResult(response, responseHandler);
                return;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            onSyncFailResult(request, response, responseHandler);
        }
    }

    public void delete(Context context, String url, Map<String, String> requestParams, final JsonHttpResponseHandler responseHandler) {
        Request request = new Request.Builder().tag(context).url(url).delete(FormBody4Request(requestParams)).build();
        Callback callback = new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                onFailResult(call, e, responseHandler);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                onSuccessResult(call, response, responseHandler);
            }
        };
        mOkHttpClient.newCall(request).enqueue(callback);
    }

    public void delete(Context context, String url, Map<String, String> requestParams, final JsonHttpResponseHandler responseHandler, boolean async) {
        Request request = new Request.Builder().tag(context).url(url).delete(FormBody4Request(requestParams)).build();
        Callback callback = new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                onFailResult(call, e, responseHandler);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                onSuccessResult(call, response, responseHandler);
            }
        };
        if (async) {
            mOkHttpClient.newCall(request).enqueue(callback);
        } else {
            Response response = null;
            try {
                response = mOkHttpClient.newCall(request).execute();
                onSyncSuccessResult(response, responseHandler);
                return;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            onSyncFailResult(request, response, responseHandler);
        }
    }

    public void delete(Context context, String token, String url, Map<String, String> requestParams, final JsonHttpResponseHandler responseHandler) {
        Request request;
        if (TextUtils.isEmpty(token)) {
            request = new Request.Builder().tag(context).url(url).delete(FormBody4Request(requestParams)).build();
        } else {
            headers = Headers.of(ACCESSTOKEN, token);
            request = new Request.Builder().tag(context).addHeader(ACCESSTOKEN, token).url(url).delete(FormBody4Request(requestParams)).build();
        }
        Callback callback = new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                onFailResult(call, e, responseHandler);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                onSuccessResult(call, response, responseHandler);
            }
        };
        mOkHttpClient.newCall(request).enqueue(callback);
    }

    public void delete(Context context, String token, String url, Map<String, String> requestParams, final JsonHttpResponseHandler responseHandler, boolean async) {
        Request request;
        if (TextUtils.isEmpty(token)) {
            request = new Request.Builder().tag(context).url(url).delete(FormBody4Request(requestParams)).build();
        } else {
            headers = Headers.of(ACCESSTOKEN, token);
            request = new Request.Builder().tag(context).addHeader(ACCESSTOKEN, token).url(url).delete(FormBody4Request(requestParams)).build();
        }
        Callback callback = new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                onFailResult(call, e, responseHandler);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                onSuccessResult(call, response, responseHandler);
            }
        };
        if (async) {
            mOkHttpClient.newCall(request).enqueue(callback);
        } else {
            Response response = null;
            try {
                response = mOkHttpClient.newCall(request).execute();
                onSyncSuccessResult(response, responseHandler);
                return;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            onSyncFailResult(request, response, responseHandler);
        }
    }


}
