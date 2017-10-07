package com.oradt.ecard.okhttp;

import android.content.Context;
import android.os.Handler;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OkHttpEngine {
    private static final String ACCESSTOKEN = "AccessToken";
    private static OkHttpEngine mInstance;
    private static OkHttpClient mOkHttpClient;
    private static Handler mHandler;
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
        mOkHttpClient = new OkHttpClient.Builder().build();
        mHandler = new Handler();
        headers = null;
    }

    public void addHeader(String name, String value) {
        headers = Headers.of(name, value);
    }

    private FormBody FormBody4Request(RequestParams requestParams) {
        Map<String, String> params = new HashMap<>();
        FormBody.Builder builder = new FormBody.Builder();
        try {
            Class<? extends RequestParams> myParams = requestParams.getClass();
            Field fieldUrl = myParams.getDeclaredField("urlParams");
            fieldUrl.setAccessible(true);
            params = (ConcurrentHashMap<String, String>)(fieldUrl.get(myParams));
            if (params != null && params.size() > 0) {
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    builder.add(entry.getKey(), entry.getValue());
                }
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return builder.build();
    }

    private void onFailResult(final Call call, final IOException e, final JsonHttpResponseHandler responseHandler) {
        if (mHandler == null) {
            mHandler = new Handler();
        }
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Request callRequest = call.request();
                if (callRequest != null && callRequest.headers() != null) {
                    BasicHeader retHeader[] = new BasicHeader[1];
                    retHeader[0] = new BasicHeader(callRequest.headers().name(0), callRequest.headers().value(0));
                    responseHandler.onFailure(100, retHeader, call.toString(), e.getCause());
                } else {
                    responseHandler.onFailure(100, null, call.toString(), e.getCause());
                }
            }
        });
    }

    private void onSuccessResult(final Call call, final Response response, final JsonHttpResponseHandler responseHandler) {
        if (mHandler == null) {
            mHandler = new Handler();
        }
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                String response_body = "";
                try {
                    response_body = response.body().string();
                    if(response.isSuccessful()) {
                        JSONObject body = new JSONObject(response_body);
                        Request callRequest = call.request();
                        if (callRequest != null && callRequest.headers() != null) {
                            BasicHeader retHeader[] = new BasicHeader[1];
                            retHeader[0] = new BasicHeader(callRequest.headers().name(0), callRequest.headers().value(0));
                            responseHandler.onSuccess(response.code(), retHeader, body);
                        } else {
                            responseHandler.onSuccess(response.code(), new Header[1], body);
                        }
                    } else {
                        JSONObject body = new JSONObject(response_body);
                        Request callRequest = call.request();
                        if (callRequest != null && callRequest.headers() != null) {
                            BasicHeader retHeader[] = new BasicHeader[1];
                            retHeader[0] = new BasicHeader(callRequest.headers().name(0), callRequest.headers().value(0));
                            responseHandler.onFailure(response.code(), retHeader, null, body);
                        } else {
                            responseHandler.onFailure(response.code(), new Header[1], null, body);
                        }
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

    private void onSyncSuccessResult(final Headers thisHeader, final Response response, final JsonHttpResponseHandler responseHandler) throws JSONException, IOException {
        String response_body = "";
        if (response.isSuccessful()) {
            response_body = response.body().string();
            JSONObject body = new JSONObject(response_body);
            if (thisHeader != null) {
                BasicHeader retHeader[] = new BasicHeader[1];
                retHeader[0] = new BasicHeader(thisHeader.name(0), thisHeader.value(0));
                responseHandler.onSuccess(response.code(), retHeader, body);
            } else {
                responseHandler.onSuccess(response.code(), new Header[1], body);
            }
        } else {
            JSONObject body = new JSONObject(response_body);
            if (thisHeader != null) {
                BasicHeader retHeader[] = new BasicHeader[1];
                retHeader[0] = new BasicHeader(thisHeader.name(0), thisHeader.value(0));
                responseHandler.onFailure(response.code(), retHeader, null, body);
            } else {
                responseHandler.onFailure(response.code(), new Header[1], null, body);
            }
        }
    }

    public void get(Context context, String url, RequestParams requestParams, final JsonHttpResponseHandler responseHandler) {
        Request request;
        if (headers == null) {
            request = new Request.Builder().url(url).tag(context).get().build();
        } else {
            Headers thisHeader = headers;
            headers = null;
            request = new Request.Builder().url(url).tag(context).headers(thisHeader).get().build();
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

    public void get(Context context, String url, RequestParams requestParams, final JsonHttpResponseHandler responseHandler, boolean async) {
        Request request;
        Headers thisHeader = headers;
        if (headers == null) {
            request = new Request.Builder().url(url).tag(context).get().build();
        } else {
            headers = null;
            request = new Request.Builder().url(url).tag(context).headers(thisHeader).get().build();
        }
        Callback callback = new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                onFailResult(call, e, responseHandler);
            }

            @Override
            public void onResponse(final Call call, final Response response) throws IOException {
                onSuccessResult(call, response, responseHandler);
            }
        };
        if (async) {
            mOkHttpClient.newCall(request).enqueue(callback);
        } else {
            try {
                Response response = mOkHttpClient.newCall(request).execute();
                onSyncSuccessResult(thisHeader, response, responseHandler);
            } catch (IOException e) {
                e.printStackTrace();
                responseHandler.onFailure(100, new Header[1], request.toString(), e.getCause());
            } catch (JSONException e) {
                e.printStackTrace();
                responseHandler.onFailure(100, new Header[1], request.toString(), e.getCause());
            }
        }
    }

    public void get(Context context, String url, final Header[] heads, RequestParams requestParams, final JsonHttpResponseHandler responseHandler) {
        Request request;
        if (headers == null) {
            request = new Request.Builder().url(url).tag(context).get().build();
        } else {
            Headers thisHeader = headers;
            headers = null;
            request = new Request.Builder().url(url).tag(context).headers(thisHeader).get().build();
        }
        Callback callback = new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                onFailResult(call, e, responseHandler);
            }

            @Override
            public void onResponse(final Call call, final Response response) throws IOException {
                onSuccessResult(call, response, responseHandler);
            }
        };
        mOkHttpClient.newCall(request).enqueue(callback);
    }

    public void get(Context context, String url, final Header[] heads, RequestParams requestParams, final JsonHttpResponseHandler responseHandler, boolean async) {
        Request request;
        Headers thisHeader = headers;
        if (headers == null) {
            request = new Request.Builder().url(url).tag(context).get().build();
        } else {
            headers = null;
            request = new Request.Builder().url(url).tag(context).headers(thisHeader).get().build();
        }
        Callback callback = new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                onFailResult(call, e, responseHandler);
            }

            @Override
            public void onResponse(final Call call, final Response response) throws IOException {
                onSuccessResult(call, response, responseHandler);
            }
        };
        if (async) {
            mOkHttpClient.newCall(request).enqueue(callback);
        } else {
            try {
                Response response = mOkHttpClient.newCall(request).execute();
                onSyncSuccessResult(thisHeader, response, responseHandler);
            } catch (IOException e) {
                e.printStackTrace();
                responseHandler.onFailure(100, new Header[1], request.toString(), e.getCause());
            } catch (JSONException e) {
                e.printStackTrace();
                responseHandler.onFailure(100, new Header[1], request.toString(), e.getCause());
            }
        }
    }

    public void post(Context context, String url, RequestParams requestParams, final JsonHttpResponseHandler responseHandler) {
        Request request;
        if (headers == null) {
            request = new Request.Builder().url(url).tag(context).post(FormBody4Request(requestParams)).build();
        } else {
            Headers thisHeader = headers;
            headers = null;
            request = new Request.Builder().url(url).tag(context).headers(thisHeader).post(FormBody4Request(requestParams)).build();
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

    public void post(Context context, String url, RequestParams requestParams, final JsonHttpResponseHandler responseHandler, boolean async) {
        Request request;
        Headers thisHeader = headers;
        if (headers == null) {
            request = new Request.Builder().url(url).tag(context).post(FormBody4Request(requestParams)).build();
        } else {
            headers = null;
            request = new Request.Builder().url(url).tag(context).headers(thisHeader).post(FormBody4Request(requestParams)).build();
        }
        Callback callback = new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                onFailResult(call, e, responseHandler);
            }

            @Override
            public void onResponse(final Call call, final Response response) throws IOException {
                onSuccessResult(call, response, responseHandler);
            }
        };
        if (async) {
            mOkHttpClient.newCall(request).enqueue(callback);
        } else {
            try {
                Response response = mOkHttpClient.newCall(request).execute();
                onSyncSuccessResult(thisHeader, response, responseHandler);
            } catch (IOException e) {
                e.printStackTrace();
                responseHandler.onFailure(100, new Header[1], request.toString(), e.getCause());
            } catch (JSONException e) {
                e.printStackTrace();
                responseHandler.onFailure(100, new Header[1], request.toString(), e.getCause());
            }
        }
    }

    public void post(Context context, String url, final Header[] heads, RequestParams requestParams, final JsonHttpResponseHandler responseHandler) {
        Request request;
        if (headers == null) {
            request = new Request.Builder().url(url).tag(context).post(FormBody4Request(requestParams)).build();
        } else {
            Headers thisHeader = headers;
            headers = null;
            request = new Request.Builder().url(url).tag(context).headers(thisHeader).post(FormBody4Request(requestParams)).build();
        }
        Callback callback = new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                onFailResult(call, e, responseHandler);
            }

            @Override
            public void onResponse(final Call call, final Response response) throws IOException {
                onSuccessResult(call, response, responseHandler);
            }
        };
        mOkHttpClient.newCall(request).enqueue(callback);
    }

    public void post(Context context, String url, final Header[] heads, RequestParams requestParams, final JsonHttpResponseHandler responseHandler, boolean async) {
        Request request;
        Headers thisHeader = headers;
        if (headers == null) {
            request = new Request.Builder().url(url).tag(context).post(FormBody4Request(requestParams)).build();
        } else {
            headers = null;
            request = new Request.Builder().url(url).tag(context).headers(thisHeader).post(FormBody4Request(requestParams)).build();
        }
        Callback callback = new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                onFailResult(call, e, responseHandler);
            }

            @Override
            public void onResponse(final Call call, final Response response) throws IOException {
                onSuccessResult(call, response, responseHandler);
            }
        };
        if (async) {
            mOkHttpClient.newCall(request).enqueue(callback);
        } else {
            try {
                Response response = mOkHttpClient.newCall(request).execute();
                onSyncSuccessResult(thisHeader, response, responseHandler);
            } catch (IOException e) {
                e.printStackTrace();
                responseHandler.onFailure(100, new Header[1], request.toString(), e.getCause());
            } catch (JSONException e) {
                e.printStackTrace();
                responseHandler.onFailure(100, new Header[1], request.toString(), e.getCause());
            }
        }
    }

    public void post(Context context, String url, final Header[] heads, RequestParams requestParams, Object holder, final JsonHttpResponseHandler responseHandler) {
        Request request;
        if (headers == null) {
            request = new Request.Builder().url(url).tag(context).post(FormBody4Request(requestParams)).build();
        } else {
            Headers thisHeader = headers;
            headers = null;
            request = new Request.Builder().url(url).tag(context).headers(thisHeader).post(FormBody4Request(requestParams)).build();
        }
        Callback callback = new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                onFailResult(call, e, responseHandler);
            }

            @Override
            public void onResponse(final Call call, final Response response) throws IOException {
                onSuccessResult(call, response, responseHandler);
            }
        };
        mOkHttpClient.newCall(request).enqueue(callback);
    }

    public void post(Context context, String url, final Header[] heads, RequestParams requestParams, Object holder, final JsonHttpResponseHandler responseHandler, boolean async) {
        Request request;
        Headers thisHeader = headers;
        if (headers == null) {
            request = new Request.Builder().url(url).tag(context).post(FormBody4Request(requestParams)).build();
        } else {
            headers = null;
            request = new Request.Builder().url(url).tag(context).headers(thisHeader).post(FormBody4Request(requestParams)).build();
        }
        Callback callback = new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                onFailResult(call, e, responseHandler);
            }

            @Override
            public void onResponse(final Call call, final Response response) throws IOException {
                onSuccessResult(call, response, responseHandler);
            }
        };
        if (async) {
            mOkHttpClient.newCall(request).enqueue(callback);
        } else {
            try {
                Response response = mOkHttpClient.newCall(request).execute();
                onSyncSuccessResult(thisHeader, response, responseHandler);
            } catch (IOException e) {
                e.printStackTrace();
                responseHandler.onFailure(100, new Header[1], request.toString(), e.getCause());
            } catch (JSONException e) {
                e.printStackTrace();
                responseHandler.onFailure(100, new Header[1], request.toString(), e.getCause());
            }
        }
    }

    public void put(Context context, String url, RequestParams requestParams, final JsonHttpResponseHandler responseHandler) {
        Request request;
        if (headers == null) {
            request = new Request.Builder().url(url).tag(context).put(FormBody4Request(requestParams)).build();
        } else {
            Headers thisHeader = headers;
            headers = null;
            request = new Request.Builder().url(url).tag(context).headers(thisHeader).put(FormBody4Request(requestParams)).build();
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

    public void put(Context context, String url, RequestParams requestParams, final JsonHttpResponseHandler responseHandler, boolean async) {
        Request request;
        Headers thisHeader = headers;
        if (headers == null) {
            request = new Request.Builder().url(url).tag(context).put(FormBody4Request(requestParams)).build();
        } else {
            headers = null;
            request = new Request.Builder().url(url).tag(context).headers(thisHeader).put(FormBody4Request(requestParams)).build();
        }
        Callback callback = new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                onFailResult(call, e, responseHandler);
            }

            @Override
            public void onResponse(final Call call, final Response response) throws IOException {
                onSuccessResult(call, response, responseHandler);
            }
        };
        if (async) {
            mOkHttpClient.newCall(request).enqueue(callback);
        } else {
            try {
                Response response = mOkHttpClient.newCall(request).execute();
                onSyncSuccessResult(thisHeader, response, responseHandler);
            } catch (IOException e) {
                e.printStackTrace();
                responseHandler.onFailure(100, new Header[1], request.toString(), e.getCause());
            } catch (JSONException e) {
                e.printStackTrace();
                responseHandler.onFailure(100, new Header[1], request.toString(), e.getCause());
            }
        }
    }

    public void put(Context context, String url, final Header[] heads, RequestParams requestParams, final JsonHttpResponseHandler responseHandler) {
        Request request;
        if (headers == null) {
            request = new Request.Builder().url(url).tag(context).put(FormBody4Request(requestParams)).build();
        } else {
            Headers thisHeader = headers;
            headers = null;
            request = new Request.Builder().url(url).tag(context).headers(thisHeader).put(FormBody4Request(requestParams)).build();
        }
        Callback callback = new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                onFailResult(call, e, responseHandler);
            }

            @Override
            public void onResponse(final Call call, final Response response) throws IOException {
                onSuccessResult(call, response, responseHandler);
            }
        };
        mOkHttpClient.newCall(request).enqueue(callback);
    }

    public void put(Context context, String url, final Header[] heads, RequestParams requestParams, final JsonHttpResponseHandler responseHandler, boolean async) {
        Request request;
        Headers thisHeader = headers;
        if (headers == null) {
            request = new Request.Builder().url(url).tag(context).put(FormBody4Request(requestParams)).build();
        } else {
            headers = null;
            request = new Request.Builder().url(url).tag(context).headers(thisHeader).put(FormBody4Request(requestParams)).build();
        }
        Callback callback = new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                onFailResult(call, e, responseHandler);
            }

            @Override
            public void onResponse(final Call call, final Response response) throws IOException {
                onSuccessResult(call, response, responseHandler);
            }
        };
        if (async) {
            mOkHttpClient.newCall(request).enqueue(callback);
        } else {
            try {
                Response response = mOkHttpClient.newCall(request).execute();
                onSyncSuccessResult(thisHeader, response, responseHandler);
            } catch (IOException e) {
                e.printStackTrace();
                responseHandler.onFailure(100, new Header[1], request.toString(), e.getCause());
            } catch (JSONException e) {
                e.printStackTrace();
                responseHandler.onFailure(100, new Header[1], request.toString(), e.getCause());
            }
        }
    }

}
