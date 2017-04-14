package com.example.administrator.myapplication.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.entity.LocationInfo;
import com.example.administrator.myapplication.utils.Utils;

import java.util.List;

/**
 * Created by liaoruochen on 2017/3/22.
 * Description:
 */

public class TextAdapter extends RecyclerView.Adapter<TextAdapter.ViewHolder> {
    private List<LocationInfo>  mList;

    public TextAdapter(List<LocationInfo> mList) {
        this.mList = mList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_text, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        long time = mList.get(position).getTime();
        holder.mTime.setText(Utils.formatUTC(time,"HH:mm:ss"));
        holder.mAddress.setText(mList.get(position).getAddress());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView mTime;
        private final TextView mAddress;

        public ViewHolder(View itemView) {
            super(itemView);
            mTime = ((TextView) itemView.findViewById(R.id.time));
            mAddress = ((TextView) itemView.findViewById(R.id.address));
        }

    }

    public void addLocationInfo(LocationInfo locationInfo){
        mList.add(0,locationInfo);
        notifyItemInserted(0);

    }
}
