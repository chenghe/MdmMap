package com.zsmarter.mdmDevice;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.zsmarter.mdmDevice.clusterutil.DeviceBean;
import com.zsmarter.mdmdevice.R;

import java.util.List;

public class DeviceListAdapter extends RecyclerView.Adapter<DeviceListAdapter.ViewHolder> {

    private List<DeviceBean> list;
    private Context context;
    public boolean mutiChoose;

    public DeviceListAdapter(List<DeviceBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.device_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.tvTitle.setText(list.get(position).getDeviceName());
        holder.ivIcon.setImageResource(list.get(position).getDeviceIcon());
        holder.checkBox.setChecked(list.get(position).isCheck());
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mutiChoose) {
                    clearChoose();
//                    for (DeviceBean bean : list) {
//                        bean.setCheck(false);
//                    }
//                    for(int i = 0; i<list.size(); i++){
//                        if (i!=position){
//                            notifyItemChanged(i);
//                        }
//                    }

                }
                list.get(position).setCheck(holder.checkBox.isChecked());


            }
        });
    }


//    public void mutiChoose(){
//        for (DeviceBean bean:list){
//            bean.setCheck(false);
//        }
//    }

    public void clearChoose() {
        for (DeviceBean bean : list) {
            bean.setCheck(false);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTitle;
        public ImageView ivIcon;
        public CheckBox checkBox;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            ivIcon = itemView.findViewById(R.id.iv_icon);
            checkBox = itemView.findViewById(R.id.checkbox);
        }
    }
}
