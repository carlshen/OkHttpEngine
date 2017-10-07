package cn.finalteam.okhttpengine.sample.http;

import cn.finalteam.okhttpengine.BaseHttpRequestCallback;
import cn.finalteam.okhttpengine.sample.http.model.BaseApiResponse;

/**
 * Desction:
 * Author:carlshen
 * Date:17/10/10 下午3:07
 */
public class MyBaseHttpRequestCallback<T extends BaseApiResponse> extends BaseHttpRequestCallback<T>{

    @Override
    protected void onSuccess(T t) {
        int code = t.getCode();
        if ( code == 1 ) {
            onLogicSuccess(t);
        } else {
            onLogicFailure(t);
        }
    }

    public void onLogicSuccess(T t) {

    }

    public void onLogicFailure(T t) {

    }
}
