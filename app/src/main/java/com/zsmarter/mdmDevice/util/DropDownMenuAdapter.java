package com.zsmarter.mdmDevice.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zsmarter.mdmDevice.network.bean.OrganListResponseBody;
import com.zsmarter.mdmdevice.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chengbin He on 2016/12/5.
 */

public class DropDownMenuAdapter extends BaseAdapter {
    private Context context;
    private List<OrganListResponseBody.Context.Page.RowItem> list = new ArrayList<>();
//    private int type = 0;// 0为获取城市，1为获取项目名，2为获取人员
    private boolean showCheck;
    private int SelectIndex;//被选中位置

    public DropDownMenuAdapter(Context context, List<OrganListResponseBody.Context.Page.RowItem> list){
        this.list = list;
        this.context = context;
//        this.type = type;
    }
    public void notifyData(List<OrganListResponseBody.Context.Page.RowItem> list){
        this.list = list;
//        this.type = type;
        notifyDataSetChanged();
    }
    public void setShowCheck(boolean showCheck) {
        this.showCheck = showCheck;
    }
    public void setSelectIndex(int selectIndex) {
        SelectIndex = selectIndex;
    }
    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v= LayoutInflater.from(context).inflate(R.layout.item_dropdownmenu,parent,false);
        TextView name = (TextView) v.findViewById(R.id.tv_menu_item);
        name.setText(list.get(position).getOrgName());
//        View line = v.findViewById(R.id.line);
//        if (SelectIndex==position) {
//            line.setVisibility(View.VISIBLE);
//            name.setTextColor(context.getResources().getColor(R.color.app_green));
//        }else {
//            line.setVisibility(View.GONE);
//            name.setTextColor(context.getResources().getColor(R.color.content_two));
//        }
        return v;
    }

}
