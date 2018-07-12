package com.zsmarter.mdmDevice.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.zsmarter.mdmDevice.network.bean.DeviceListResponseBody;
import com.zsmarter.mdmdevice.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by hecheng on 2018/6/23
 */
public class MdmDeviceListAdapter extends RecyclerView.Adapter<MdmDeviceListAdapter.ViewHolder> {

    private Context context;
    private List<DeviceListResponseBody.Context.Result.RowsItem> list;
    private List<DeviceListResponseBody.Context.Result.RowsItem> chooseList;
    private boolean mutiChoose;

    public MdmDeviceListAdapter(Context context, List<DeviceListResponseBody.Context.Result.RowsItem> list) {
        this.context = context;
        this.list = list;
        chooseList = new ArrayList<>();
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
        holder.checkBox.setClickable(false);
        holder.itemView.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mutiChoose) {
                    clearChoose();
                }
                holder.checkBox.setChecked(!holder.checkBox.isChecked());
                list.get(position).setCheck(holder.checkBox.isChecked());

            }
        });
    }

    public boolean isMutiChoose() {
        return mutiChoose;
    }

    public void setMutiChoose(boolean mutiChoose) {
        this.mutiChoose = mutiChoose;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void clearChoose() {
        for (DeviceListResponseBody.Context.Result.RowsItem bean : list) {
            bean.setCheck(false);
        }
        notifyDataSetChanged();
    }

    public List<DeviceListResponseBody.Context.Result.RowsItem> getChooseItems() {
        chooseList.clear();
        for (DeviceListResponseBody.Context.Result.RowsItem bean : list) {
            if (bean.isCheck()) {
                chooseList.add(bean);
            }
        }
        return chooseList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

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
