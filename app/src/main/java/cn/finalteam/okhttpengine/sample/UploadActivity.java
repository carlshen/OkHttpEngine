
package cn.finalteam.okhttpengine.sample;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.finalteam.okhttpengine.BaseHttpRequestCallback;
import cn.finalteam.okhttpengine.HttpRequest;
import cn.finalteam.okhttpengine.OkRequestParams;
import cn.finalteam.okhttpengine.sample.http.model.UploadResponse;
import us.feras.mdv.MarkdownView;

/**
 * Desction:
 * Author:carlshen
 * Date:17/10/10 下午3:07
 */
public class UploadActivity extends BaseActivity {

    @Bind(R.id.pb_upload)
    ProgressBar mPbUpload;
    @Bind(R.id.btn_upload)
    Button mBtnUpload;
    @Bind(R.id.mv_code)
    MarkdownView mMvCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        ButterKnife.bind(this);
        setTitle("文件上传");

        mMvCode.loadMarkdownFile("file:///android_asset/Upload.md", "file:///android_asset/css-themes/classic.css");
    }

    @OnClick(R.id.btn_upload)
    public void upload() {
//        GalleryFinal.openGallerySingle(0, new GalleryFinal.OnHanlderResultCallback() {
//            @Override
//            public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
//                uploadFile(new File(resultList.get(0).getPhotoPath()));
//            }
//
//            @Override
//            public void onHanlderFailure(int requestCode, String errorMsg) {
//
//            }
//        });
    }

    private void uploadFile(File file) {
        String userId = "3097424";
        OkRequestParams params = new OkRequestParams(this);
        params.addFormDataPart("file", file);
        params.addFormDataPart("userId", userId);
        params.addFormDataPart("token", "NTCrWFKFCn1r8iaV3K0fLz2gX9LZS1SR");
        params.addFormDataPart("udid", "f0ba33e4de8a657d");
        params.addFormDataPart("sign", "39abfa9af6f6e3c8776b01ae612bc14c");
        params.addFormDataPart("version", "2.1.0");
        params.addFormDataPart("mac", "8c:3a:e3:5e:68:e0");
        params.addFormDataPart("appId", "paojiao_aiyouyou20");
        params.addFormDataPart("imei", "359250051610200");
        params.addFormDataPart("model", "Nexus 5");
        params.addFormDataPart("cid", "paojiao");
        String fileuploadUri = "http://uploader.paojiao.cn/avatarAppUploader?userId=" + userId;

        HttpRequest.post(fileuploadUri, params, new BaseHttpRequestCallback<UploadResponse>() {
            @Override
            public void onSuccess(UploadResponse uploadResponse) {
                super.onSuccess(uploadResponse);
                Toast.makeText(getBaseContext(), "上传成功：" + uploadResponse.getData(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int errorCode, String msg) {
                super.onFailure(errorCode, msg);
                Toast.makeText(getBaseContext(), "上传失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onProgress(int progress, long networkSpeed, boolean done) {
                mPbUpload.setProgress(progress);
            }
        });
    }
}
