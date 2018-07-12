package com.zsmarter.mdmDevice.util;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.zsmarter.mdmDevice.adapter.OrgListAdapter;
import com.zsmarter.mdmDevice.network.bean.OrganListResponseBody;
import com.zsmarter.mdmdevice.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hecheng on 2018/6/28
 */
public class OrgChooseView extends LinearLayout {

    private Context mContext;
    private ListView leftRV;
    private ListView rightRV;
    private List<OrganListResponseBody.Context.Page.RowItem> leftList = new ArrayList<>();
    private List<OrganListResponseBody.Context.Page.RowItem> rightList = new ArrayList<>();
    private OrgListAdapter leftAdapter;
    private OrgListAdapter rightAdapter;
    private String type;
    private ItemClickListenter leftItemClickListenter;
    private AdapterView.OnItemClickListener rightItemClickListenter;

    public OrgChooseView(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    public OrgChooseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }

    private void initView() {
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.view_org_choose, null);
        addView(contentView);
        leftRV = contentView.findViewById(R.id.rv_left);
        rightRV = contentView.findViewById(R.id.rv_right);
        leftAdapter = new OrgListAdapter(mContext, false);
        leftRV.setAdapter(leftAdapter);

        rightAdapter = new OrgListAdapter(mContext, true);
        rightRV.setAdapter(rightAdapter);

    }

    public void notifyLeft(List<OrganListResponseBody.Context.Page.RowItem> list) {
        if (list != null) {
            leftAdapter.notifyData(list);
        }
    }

    public void notifyRight(List<OrganListResponseBody.Context.Page.RowItem> list) {
        if (list != null) {
            rightAdapter.notifyData(list);

        }
    }

    public void setLeftItemClickListenter(final ItemClickListenter listenter) {
        leftRV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                listenter.itemClick(i,leftAdapter.getChooseDataId(i));
            }
        });
    }

    public void setRightItemClickListenter(final ItemClickListenter listenter){
        rightRV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                rightAdapter.setSelectItem(position);
                rightAdapter.notifyDataSetChanged();
                listenter.itemClick(position,rightAdapter.getChooseDataId(position));
            }
        });
    }


//    public void setRightItemClickListenter(AdapterView.OnItemClickListener listenter){
//        rightItemClickListenter = listenter;
//    }};


    public interface ItemClickListenter{
        void itemClick(int position,String dataId);
    }
}


