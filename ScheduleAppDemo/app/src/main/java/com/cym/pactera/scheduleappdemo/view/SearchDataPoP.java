package com.cym.pactera.scheduleappdemo.view;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;

import com.amap.api.services.core.AMapException;
import com.amap.api.services.help.Tip;
import com.cym.pactera.scheduleappdemo.R;
import com.cym.pactera.scheduleappdemo.util.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by pactera on 2018/8/28.
 */

public class SearchDataPoP extends PopupWindow {
    private View mView;
    private ListView listView;
    public SearchDataPoP(Activity context,final List<Tip> tipList, int rCode){
        super(context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = inflater.inflate(R.layout.searchdatapop, null);
        listView=(ListView) mView.findViewById(R.id.list_data);
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            List<HashMap<String, String>> listString = new ArrayList<HashMap<String, String>>();
            for (int i = 0; i < tipList.size(); i++) {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("name", tipList.get(i).getName());
                map.put("address", tipList.get(i).getDistrict());
                listString.add(map);
            }
            SimpleAdapter aAdapter = new SimpleAdapter(context, listString, R.layout.item_layout,
                    new String[] {"name","address"}, new int[] {R.id.poi_field_id, R.id.poi_value_id});

            listView.setAdapter(aAdapter);
            aAdapter.notifyDataSetChanged();

        } else {
            ToastUtil
                    .showerror(context, rCode);
        }

    }

}
