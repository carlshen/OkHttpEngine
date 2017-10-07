package cn.finalteam.okhttpengine.sample;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.finalteam.okhttpengine.HttpRequest;
import cn.finalteam.okhttpengine.OkRequestParams;
import cn.finalteam.okhttpengine.sample.adapter.NewGameListAdapter;
import cn.finalteam.okhttpengine.sample.http.Api;
import cn.finalteam.okhttpengine.sample.http.MyBaseHttpRequestCallback;
import cn.finalteam.okhttpengine.sample.http.model.GameInfo;
import cn.finalteam.okhttpengine.sample.http.model.NewGameResponse;
import cn.finalteam.okhttpengine.sample.widget.swipeview.SwipeRefreshLayout;
import cn.finalteam.okhttpengine.sample.widget.swipeview.SwipeRefreshLayoutDirection;
import cn.finalteam.toolsfinal.StringUtils;
import us.feras.mdv.MarkdownView;

/**
 * Desction:
 * Author:carlshen
 * Date:17/10/10 下午3:07
 */
public class NewGameListActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    @Bind(R.id.gv_game)
    GridView mGvGame;
    NewGameListAdapter mNewGameListAdapter;
    List<GameInfo> mGameList;
    @Bind(R.id.swipe_layout)
    SwipeRefreshLayout mSwipeLayout;
    @Bind(R.id.mv_code)
    MarkdownView mMvCode;
    private int mPage = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game_list);
        ButterKnife.bind(this);

        setTitle("接口解析Bean");
        mGameList = new ArrayList<>();
        mNewGameListAdapter = new NewGameListAdapter(this, mGameList);
        mGvGame.setAdapter(mNewGameListAdapter);
        mSwipeLayout.setDirection(SwipeRefreshLayoutDirection.BOTH);
        mSwipeLayout.setOnRefreshListener(this);
        requestData(mPage);

        mMvCode.loadMarkdownFile("file:///android_asset/HttpRequestCallbackBean.md", "file:///android_asset/css-themes/classic.css");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_code, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if ( itemId == R.id.action_code ) {
            if (mMvCode.getVisibility()== View.VISIBLE) {
                mMvCode.setVisibility(View.GONE);
            } else {
                mMvCode.setVisibility(View.VISIBLE);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void requestData(final int page) {
        OkRequestParams params = new OkRequestParams(this);
        params.addFormDataPart("page", page);
        params.addFormDataPart("limit", 12);
        HttpRequest.post(Api.NEW_GAME, params, new MyBaseHttpRequestCallback<NewGameResponse>() {

            @Override
            public void onLogicSuccess(NewGameResponse newGameResponse) {
                mPage = page + 1;
                if (newGameResponse.getData() != null) {
                    mGameList.addAll(newGameResponse.getData());
                    mNewGameListAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getBaseContext(), newGameResponse.getMsg(), Toast.LENGTH_SHORT).show();
                }

                if (newGameResponse.getData() != null && newGameResponse.getData().size() > 0) {
                    mSwipeLayout.setDirection(SwipeRefreshLayoutDirection.BOTH);
                } else {
                    mSwipeLayout.setDirection(SwipeRefreshLayoutDirection.TOP);
                }
            }

            @Override
            public void onLogicFailure(NewGameResponse newGameResponse) {
                super.onLogicFailure(newGameResponse);
                String msg = newGameResponse.getMsg();
                if (StringUtils.isEmpty(msg)) {
                    msg = "网络异常";
                }
                Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int errorCode, String msg) {
                super.onFailure(errorCode, msg);
                Toast.makeText(getBaseContext(), "网络异常", Toast.LENGTH_SHORT).show();
            }

            @Override public void onFinish() {
                super.onFinish();
                mSwipeLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onRefresh(SwipeRefreshLayoutDirection direction) {
        if ( direction == SwipeRefreshLayoutDirection.TOP ) {
            requestData(1);
        } else {
            requestData(mPage);
        }
    }
}
