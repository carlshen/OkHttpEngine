/*
 * Copyright (C) 2015 pengjianbo(pengjianbosoft@gmail.com), Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package cn.finalteam.okhttpengine.sample;

import android.os.Bundle;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.finalteam.okhttpengine.HttpRequest;
import cn.finalteam.okhttpengine.JsonHttpRequestCallback;
import cn.finalteam.okhttpengine.OkRequestParams;
import cn.finalteam.okhttpengine.sample.http.Api;
import cn.finalteam.toolsfinal.JsonFormatUtils;
import us.feras.mdv.MarkdownView;

/**
 * Desction:
 * Author:carlshen
 * Date:17/10/10 下午3:07
 */
public class HttpRequestCallbackJsonActivity extends BaseActivity {

    @Bind(R.id.mv_code)
    MarkdownView mMvCode;
    @Bind(R.id.tv_result)
    TextView mTvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http_request_callback);
        ButterKnife.bind(this);

        setTitle("接口Json回调");

        OkRequestParams params = new OkRequestParams(this);
        params.addFormDataPart("page", 1);
        params.addFormDataPart("limit", 12);
        HttpRequest.post(Api.NEW_GAME, params, new JsonHttpRequestCallback() {
            @Override
            protected void onSuccess(JSONObject jsonObject) {
                super.onSuccess(jsonObject);
                mTvResult.setText(JsonFormatUtils.formatJson(jsonObject.toJSONString()));
            }
        });

        mMvCode.loadMarkdownFile("file:///android_asset/HttpRequestCallbackJson.md", "file:///android_asset/css-themes/classic.css");
    }
}
