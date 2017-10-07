package cn.finalteam.okhttpengine.sample;

import android.app.Application;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import cn.finalteam.okhttpengine.OkHttpFinal;
import cn.finalteam.okhttpengine.OkHttpFinalConfiguration;
import cn.finalteam.okhttpengine.Part;
import okhttp3.Headers;
import okhttp3.Interceptor;

/**
 * Desction:
 * Author:carlshen
 * Date:17/10/10 下午3:07
 */
public class IApplication extends Application {
    private static IApplication app;
    private static Context context;

    public static synchronized IApplication getInstance() {
        return app;
    }

    public static Context getContext() {
        return context;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        context = getApplicationContext();

        initOkHttpFinal();

    }

    private void initOkHttpFinal() {

        List<Part> commomParams = new ArrayList<>();
        Headers commonHeaders = new Headers.Builder().build();

        List<Interceptor> interceptorList = new ArrayList<>();
        OkHttpFinalConfiguration.Builder builder = new OkHttpFinalConfiguration.Builder()
                .setCommenParams(commomParams)
                .setCommenHeaders(commonHeaders)
                .setTimeout(Constants.REQ_TIMEOUT)
                .setInterceptors(interceptorList)
                        //.setCookieJar(CookieJar.NO_COOKIES)
                        //.setCertificates(...)
                        //.setHostnameVerifier(new SkirtHttpsHostnameVerifier())
                .setDebug(true);
//        addHttps(builder);
        OkHttpFinal.getInstance().init(builder.build());
    }

//    private static void addHttps( OkHttpFinalConfiguration.Builder builder){
//        try {
//            SSLContext sc = SSLContext.getInstance("SSL");
//            sc.init(null, new TrustManager[]{new X509TrustManager() {
//                @Override
//                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
//                }
//
//                @Override
//                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
//
//                }
//
//                @Override
//                public X509Certificate[] getAcceptedIssuers() {
//                    return new X509Certificate[]{};
//                }
//            }}, new SecureRandom());
//            builder.setSSLSocketFactory(sc.getSocketFactory());
//            builder.setHostnameVerifier(new HostnameVerifier() {
//                @Override
//                public boolean verify(String hostname, SSLSession session) {
//                    return true;
//                }
//            });
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        } catch (KeyManagementException e) {
//            e.printStackTrace();
//        }
//    }
}
