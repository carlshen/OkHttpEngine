package cn.finalteam.okhttpengine.sample.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.finalteam.okhttpengine.sample.Global;
import cn.finalteam.okhttpengine.sample.IApplication;
import cn.finalteam.okhttpengine.sample.R;
import cn.finalteam.okhttpengine.sample.http.MyHttpCycleContext;
import cn.finalteam.okhttpengine.sample.http.model.GameInfo;

/**
 * Desction:
 * Author:carlshen
 * Date:17/10/10 下午3:07
 */
public class NewGameListAdapter extends CommonBaseAdapter<NewGameListAdapter.NewGameViewHolder, GameInfo> {

    public NewGameListAdapter(MyHttpCycleContext httpCycleContext, List<GameInfo> list) {
        super(httpCycleContext, list);
    }

    @Override
    public NewGameViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        View view = inflate(R.layout.adapter_new_game_list_item, null);
        NewGameViewHolder viewHolder = new NewGameViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(NewGameViewHolder holder, int position) {
        GameInfo data = mList.get(position);
        holder.mTvGameName.setText(data.getName());
        Glide.with(IApplication.getContext()).load(data.getIconUrl()).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).into(holder.mIvGameIcon);
        holder.mTvPlayingManNumber.setText(String.valueOf(data.getCommentCount()));

        if(data.getOpenState()==0 || data.getOpenState()==21){
            holder.mTvGameSocre.setVisibility(View.INVISIBLE);
        }else{
            holder.mTvGameSocre.setText(data.getTotalSocreV() + "分");
            holder.mTvGameSocre.setVisibility(View.VISIBLE);
        }

    }

    static class NewGameViewHolder extends CommonBaseAdapter.ViewHolder {
        @Bind(R.id.tv_playing_man_number)
        TextView mTvPlayingManNumber;
        @Bind(R.id.iv_game_icon)
        ImageView mIvGameIcon;
        @Bind(R.id.tv_game_name)
        TextView mTvGameName;
        @Bind(R.id.tv_game_socre)
        TextView mTvGameSocre;

        public NewGameViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            int height = (Global.SCREEN_WIDTH / 3) - 32;
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, height);
            mIvGameIcon.setLayoutParams(params);
        }
    }
}
