package cn.finalteam.glue;

import android.os.Handler;
import android.os.Looper;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by carl on 2017/8/11.
 */

public class ServerManager {
    public static final int CONNECT_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;

    public enum Type {
        GET,
        GET_ASYNC,
        POST,
        POST_ASYNC,
        SOAP
    }

    private static ServerManager mServerManager;
    private static OkHttpClient mOkHttpClient = null;
    private static Handler mHandler;

    public static ServerManager getInstance() {
        if (mServerManager == null) {
            synchronized (ServerManager.class) {
                if (mServerManager == null) {
                    mServerManager = new ServerManager();
                }
            }
        }
        return mServerManager;
    }

    private OkHttpClient newHttpClient() {
        OkHttpClient.Builder builder = (new OkHttpClient.Builder())
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.MILLISECONDS)
                .retryOnConnectionFailure(false);
        return builder.build();
    }

    private String getSoapRequestData(String methodName, HashMap<String, String> paramMap) {
        StringBuffer sb = new StringBuffer("<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">");
        sb.append("<soap:Body>");
        if (paramMap != null) {
            sb.append("<" + methodName + " xmlns=\"http://WebXml.com.cn/\">");
            for (String key : paramMap.keySet()) {
                String value = paramMap.get(key);
                sb.append("<" + key + ">" + value + "</" + key + ">");
            }
            sb.append("</" + methodName + ">");
        }
        sb.append("</soap:Body>");
        sb.append("</soap:Envelope>");
        return sb.toString();
    }

    /**
     * okHttp get异步请求
     *
     * @param url       接口地址
     * @param paramsMap 请求参数
     * @param listener
     */
    public void requestGet(String url, HashMap<String, String> paramsMap, final OnServerListener listener) {
        if (mOkHttpClient == null) {
            mOkHttpClient = newHttpClient();
        }
        StringBuilder tempParams = new StringBuilder();
        try {
            //处理参数
            int pos = 0;
            for (String key : paramsMap.keySet()) {
                if (pos > 0) {
                    tempParams.append("&");
                }
                //对参数进行URLEncoder
                tempParams.append(String.format("%s=%s", key, URLEncoder.encode(paramsMap.get(key), "utf-8")));
                pos++;
            }
            //补全请求地址
            String requestUrl = String.format("%s?%s", url, tempParams.toString());
            //创建一个请求
            Request request = new Request.Builder().url(requestUrl).build();
            //创建一个Call
            final Call call = mOkHttpClient.newCall(request);
            //执行请求
            Response response = call.execute();
            if (response.isSuccessful()) {
                if (listener != null) {
                    listener.onCompleted(response.body().string());
                }
            } else {
                if (listener != null) {
                    listener.onFailed("Unexpected code " + response);
                }
            }
        } catch (Exception e) {
            if (listener != null) {
                listener.onFailed(e.getMessage());
            }
        }
    }

    /**
     * okHttp get异步请求
     *
     * @param url       接口地址
     * @param paramsMap 请求参数
     * @param listener
     */
    public void requestGetByAsync(String url, HashMap<String, String> paramsMap, final OnServerListener listener) {
        if (mOkHttpClient == null) {
            mOkHttpClient = newHttpClient();
        }
        StringBuilder tempParams = new StringBuilder();
        try {
            //处理参数
            int pos = 0;
            for (String key : paramsMap.keySet()) {
                if (pos > 0) {
                    tempParams.append("&");
                }
                //对参数进行URLEncoder
                tempParams.append(String.format("%s=%s", key, URLEncoder.encode(paramsMap.get(key), "utf-8")));
                pos++;
            }
            //补全请求地址
            String requestUrl = String.format("%s?%s", url, tempParams.toString());
            //创建一个请求
            Request request = new Request.Builder().url(requestUrl).build();
            //创建一个Call
            final Call call = mOkHttpClient.newCall(request);
            //执行请求
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    if (listener != null) {
                        listener.onFailed(e.getMessage());
                    }
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        if (listener != null) {
                            listener.onCompleted(response.body().string());
                        }
                    } else {
                        if (listener != null) {
                            listener.onFailed("Unexpected code " + response);
                        }
                    }
                }
            });
        } catch (Exception e) {
            if (listener != null) {
                listener.onFailed(e.getMessage());
            }
        }
    }

    public void requestPost(String url, HashMap<String, String> paramMap, final OnServerListener listener) {
        if (mOkHttpClient == null) {
            mOkHttpClient = newHttpClient();
        }
        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        for (String key : paramMap.keySet()) {
            formBodyBuilder.add(key, paramMap.get(key));
        }
        RequestBody formBody = formBodyBuilder.build();
        Headers.Builder headBuilder = new Headers.Builder();
        headBuilder.add("Connection", "close");
        Headers headers = headBuilder.build();
        Request request = new Request.Builder().headers(headers)
                .url(url)
                .post(formBody)
                .build();
        try {
            Response response = mOkHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                if (listener != null) {
                    listener.onCompleted(response.body().string());
                }
            } else {
                if (listener != null) {
                    listener.onFailed("Unexpected code " + response);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            if (listener != null) {
                listener.onFailed("IO error ");
            }
        }
    }

    public void requestPostByAsync(String url, HashMap<String, String> paramMap, final OnServerListener listener) throws IOException {
        if (mOkHttpClient == null) {
            mOkHttpClient = newHttpClient();
        }
        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        for (String key : paramMap.keySet()) {
            formBodyBuilder.add(key, paramMap.get(key));
        }
        RequestBody formBody = formBodyBuilder.build();
        Headers.Builder headBuilder = new Headers.Builder();
        headBuilder.add("Connection", "close");
        Headers headers = headBuilder.build();
        Request request = new Request.Builder().headers(headers)
                .url(url)
                .post(formBody)
                .build();

        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                if (listener == null) {
                    return;
                }
                Handler mHandler;
                if (Looper.myLooper() == null) {
                    mHandler = new Handler(Looper.getMainLooper());
                } else {
                    mHandler = new Handler(Looper.myLooper());
                }
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onFailed(e.getMessage());
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (listener == null) {
                    return;
                }
                Handler mHandler;
                if (Looper.myLooper() == null) {
                    mHandler = new Handler(Looper.getMainLooper());
                } else {
                    mHandler = new Handler(Looper.myLooper());
                }
                try {
                    final String response_body = response.body().string();
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (response.isSuccessful()) {
                                listener.onCompleted(response_body);
                            } else {
                                listener.onFailed("Unexpected code " + response);
                            }
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                    listener.onFailed("IOException code " + response);
                }
            }
        });
    }

    public void requesSoapService(String url, final String methodName, HashMap<String, String> paramMap, final OnServerListener listener) {
        RequestBody body = RequestBody.create(MediaType.parse("text/xml;charset=utf-8"), getSoapRequestData(methodName, paramMap));
        if (mOkHttpClient == null) {
            mOkHttpClient = newHttpClient();
        }

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        try {
            Response response = mOkHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                //Log.d(DvGlobalSetting.LOG_TAG, response.body().string());
                ByteArrayInputStream inputStream = new ByteArrayInputStream(response.body().string().getBytes());
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = null;

                builder = factory.newDocumentBuilder();
                Document document = builder.parse(inputStream);
                // 获取根节点
                Element root = document.getDocumentElement();
                NodeList nodeList = root.getElementsByTagName(methodName + "Result");
                String content = nodeList.item(0).getTextContent();
                if (listener != null) {
                    listener.onCompleted(content);
                }

                inputStream.close();
            } else {
                //throw new IOException("Unexpected code " + response);
                if (listener != null) {
                    listener.onFailed("responese failed");
                }
            }
        } catch (IOException e) {
            //e.printStackTrace();
            if (listener != null) {
                listener.onFailed("IO error");
            }
        } catch (ParserConfigurationException e) {
            //e.printStackTrace();
            if (listener != null) {
                listener.onFailed("ParserConfigurationException error");
            }
        } catch (SAXException e) {
            //e.printStackTrace();
            if (listener != null) {
                listener.onFailed("SAXException error");
            }
        }

    }

    public void requesSoapServiceAsync(String url, final String methodName, HashMap<String, String> paramMap, final OnServerListener listener) {
        RequestBody body = RequestBody.create(MediaType.parse("text/xml;charset=utf-8"), getSoapRequestData(methodName, paramMap));
        if (mOkHttpClient == null) {
            mOkHttpClient = newHttpClient();
        }

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    //Log.d(DvGlobalSetting.LOG_TAG, response.body().string());
                    ByteArrayInputStream inputStream = new ByteArrayInputStream(response.body().string().getBytes());
                    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder builder = null;
                    try {
                        builder = factory.newDocumentBuilder();
                        Document document = builder.parse(inputStream);
                        // 获取根节点
                        Element root = document.getDocumentElement();
                        NodeList nodeList = root.getElementsByTagName(methodName + "Result");
                        String content = nodeList.item(0).getTextContent();
                        if (listener != null) {
                            listener.onCompleted(content);
                        }
                    } catch (ParserConfigurationException e) {
                        //e.printStackTrace();
                        if (listener != null) {
                            listener.onFailed("ParserConfigurationException error");
                        }
                    } catch (SAXException e) {
                        //e.printStackTrace();
                        if (listener != null) {
                            listener.onFailed("SAXException error");
                        }
                    }
                    inputStream.close();
                } else {
                    //throw new IOException("Unexpected code " + response);
                    if (listener != null) {
                        listener.onFailed("responese failed");
                    }
                }
            }
        });
    }
}
