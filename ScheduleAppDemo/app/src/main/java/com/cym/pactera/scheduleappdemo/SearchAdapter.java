package com.cym.pactera.scheduleappdemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.cym.pactera.scheduleappdemo.entity.ItemValue;

import java.util.HashMap;
import java.util.List;

/**
 * Created by pactera on 2018/8/29.
 */

public class SearchAdapter extends BaseAdapter {
    private Context mContext;
    private List<HashMap<String, String>> listString ;
    public SearchAdapter(Context mContext,List<HashMap<String, String>> listString){
        this.mContext=mContext;
        this.listString=listString;
    }
    @Override
    public int getCount() {
        return listString.size();
    }

    @Override
    public Object getItem(int i) {
        ItemValue itemValue = new ItemValue();
        itemValue.setName(listString.get(i).get("name"));
        itemValue.setAddress(listString.get(i).get("address"));
        return itemValue;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        SearchAdapter.ViewHolder holder = null;

        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_layout,viewGroup,false);
            holder = new ViewHolder();
            holder.textView1 = (TextView) convertView.findViewById(R.id.poi_field_id);
            holder.textView2 = (TextView) convertView.findViewById(R.id.poi_value_id);
            convertView.setTag(holder);   //将Holder存储到convertView中
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.textView1.setText(listString.get(i).get("name"));
//        System.out.println(listString.get(i).get("name")+"@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        holder.textView2.setText(listString.get(i).get("address"));
//        System.out.println(listString.get(i).get("address")+"@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        return convertView;
    }

    static class ViewHolder{
        TextView textView1;
        TextView textView2;
    }
}
