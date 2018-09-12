package com.cym.pactera.scheduleappdemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by pactera on 2018/8/23.
 */

public class MyAdapeter extends BaseAdapter {
    private Context mContext;
    private ArrayList arrayList;
    public MyAdapeter(Context mContext, ArrayList arrayList){
        this.mContext=mContext;
        this.arrayList=arrayList;
    }
    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item,parent,false);
            holder = new ViewHolder();
            holder.img1 = (ImageView) convertView.findViewById(R.id.img_item);
            holder.txt1 = (TextView) convertView.findViewById(R.id.text_item);
            convertView.setTag(holder);   //将Holder存储到convertView中
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.img1.setBackgroundResource(R.drawable.ic_launcher_background);
        holder.txt1.setText(arrayList.get(position).toString());
        return convertView;
    }
    static class ViewHolder{
        ImageView img1;
        TextView txt1;
    }
}
