package com.redli.tmvpsimple;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.redli.tmvpsimple.bean.MovieItemBean;

import java.util.List;

/**
 * Created by redli on 2017/3/16.
 */

public class MovieAdapter extends Adapter<MovieAdapter.ViewHolder> {

    private Context mContext;
    private List<MovieItemBean> mList;

    public MovieAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setmList(List<MovieItemBean> mList) {
        this.mList = mList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(
                mContext).inflate(R.layout.item_movie, parent,
                false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textView.setText(mList.get(position).getTitle());
        Glide.with(mContext).load(mList.get(position).getImageUrl()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            textView = (TextView) itemView.findViewById(R.id.textView);
        }
    }
}
