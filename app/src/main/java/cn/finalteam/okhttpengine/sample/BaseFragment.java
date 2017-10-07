package cn.finalteam.okhttpengine.sample;

import android.content.Context;
import android.support.v4.app.Fragment;
import cn.finalteam.okhttpengine.HttpTaskHandler;
import cn.finalteam.okhttpengine.sample.http.MyHttpCycleContext;

/**
 * Desction:
 * Author:carlshen
 * Date:17/10/10 下午3:07
 */
public class BaseFragment extends Fragment implements MyHttpCycleContext{

    protected final String HTTP_TASK_KEY = "HttpTaskKey_" + hashCode();

    @Override
    public String getHttpTaskKey() {
        return HTTP_TASK_KEY;
    }

    @Override
    public Context getContext() {
        return getActivity();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        HttpTaskHandler.getInstance().removeTask(HTTP_TASK_KEY);
    }
}
