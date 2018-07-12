package com.zsmarter.mdmDevice.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zsmarter.mdmDevice.network.bean.OrganListResponseBody;
import com.zsmarter.mdmDevice.util.OrgChooseView;
import com.zsmarter.mdmdevice.R;

import java.util.List;

/**
 * Created by hecheng on 2018/6/28
 */
public class OrgListAdapter extends BaseAdapter {

    private List<OrganListResponseBody.Context.Page.RowItem> dataList;
    private Context mContext;
    private boolean isRight;
    private int selectItem = -1;

    public void notifyData(List<OrganListResponseBody.Context.Page.RowItem> dataList) {
        this.dataList = dataList;
        selectItem = -1;
        notifyDataSetChanged();
    }

    public OrgListAdapter(Context mContext, boolean isRight) {
        this.mContext = mContext;
        this.isRight = isRight;
    }

    public OrgListAdapter(List<OrganListResponseBody.Context.Page.RowItem> dataList, Context mContext) {
        this.dataList = dataList;
        this.mContext = mContext;
    }

    public int getSelectItem() {
        return selectItem;
    }

    public void setSelectItem(int selectItem) {
        this.selectItem = selectItem;
    }

    public String getChooseDataId(int position){
        if (dataList!=null&&dataList.size()>0&&dataList.size()>=position){
            return dataList.get(position).getOrgCode();
        }else {
            return "";
        }
    }

    @Override
    public int getCount() {
        return dataList == null ? 0 : dataList.size();
    }

    @Override
    public Object getItem(int i) {
        return dataList == null ? 0 : dataList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        ViewHolder viewHolder = null;
        if (view == null){
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.item_org_list, parent, false);
            viewHolder.ivCheck = view.findViewById(R.id.iv_check);
            viewHolder.ivCheck.setVisibility(View.GONE);
            viewHolder.tvName = view.findViewById(R.id.tv_name);

            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) view.getTag();
        }

        if (position == selectItem&&isRight){
            viewHolder.ivCheck.setVisibility(View.VISIBLE);
        }else{
            viewHolder.ivCheck.setVisibility(View.GONE);
        }

        if (dataList != null && dataList.get(position) != null) {
            viewHolder.tvName.setText(dataList.get(position).getOrgName());
        }

//        TextView tvName = itemView.findViewById(R.id.tv_name);
////        final ImageView ivCheck = itemView.findViewById(R.id.iv_check);
//
//        if (dataList != null && dataList.get(position) != null) {
//            tvName.setText(dataList.get(position).getOrgName());
//        }
//        itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ivCheck.setVisibility(View.VISIBLE);
//            }
//        });
        return view;
    }


    class ViewHolder{
        private ImageView ivCheck;
        private TextView tvName;
    }

}
