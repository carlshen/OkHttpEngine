package cn.finalteam.okhttpengine.sample.http;

import android.content.Context;
import cn.finalteam.okhttpengine.HttpCycleContext;

/**
 * Desction:
 * Author:carlshen
 * Date:17/10/10 下午3:07
 */
public interface MyHttpCycleContext extends HttpCycleContext{
     Context getContext();
}
