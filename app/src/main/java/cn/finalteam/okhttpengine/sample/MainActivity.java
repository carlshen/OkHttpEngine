package cn.finalteam.okhttpengine.sample;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_api_bean)
    public void apiDemoAction() {
        startActivity(new Intent(this, NewGameListActivity.class));
    }

    @OnClick(R.id.btn_upload)
    public void upload() {
        startActivity(new Intent(this, UploadActivity.class));
    }

    @OnClick(R.id.btn_api_string)
    public void apiString() {
        startActivity(new Intent(this, HttpRequestCallbackStringActivity.class));
    }

    @OnClick(R.id.btn_dm)
    public void downloadManager() {
        Uri uri = Uri.parse("https://github.com/carlshen/OkHttpEngine");
        Intent  intent = new  Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    @OnClick(R.id.btn_download)
    public void simpleDownload() {
        startActivity(new Intent(this, DownloadActivity.class));
    }

    @OnClick(R.id.btn_api_jsonobject)
    public void apiJsonObject() {
        startActivity(new Intent(this, HttpRequestCallbackJsonActivity.class));
    }

    @OnClick(R.id.btn_other_funcation)
    public void otherFunction() {
        startActivity(new Intent(this, OtherFuncationActivity.class));
    }

}
