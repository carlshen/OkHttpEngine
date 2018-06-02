package cn.finalteam.glue;

/**
 * Created by carl on 2017/8/11.
 */

public interface OnServerListener {

    void onCompleted(String rslt);

    void onFailed(String error);
}
