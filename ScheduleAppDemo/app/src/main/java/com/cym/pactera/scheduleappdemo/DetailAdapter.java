package com.cym.pactera.scheduleappdemo;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.opengl.Visibility;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cym.pactera.scheduleappdemo.entity.ContentView;
import com.cym.pactera.scheduleappdemo.util.DataStorage;
import com.cym.pactera.scheduleappdemo.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pactera on 2018/8/31.
 */

public class DetailAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<ContentView> datas;
    private DataStorage dataStorage;
    private SQLiteDatabase db;

    public DetailAdapter(Context mContext, ArrayList<ContentView> datas) {
        this.mContext = mContext;
        this.datas = datas;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int i) {
        return datas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        //TODO================================
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_schedule, parent, false);
            holder = new ViewHolder();
            holder.textView1 = (TextView) convertView.findViewById(R.id.address);
            holder.textView2 = (TextView) convertView.findViewById(R.id.time);
            holder.textView3 = (TextView) convertView.findViewById(R.id.arriveTime);
            holder.textView4 = (TextView) convertView.findViewById(R.id.goTime);
            holder.textView5 = (TextView) convertView.findViewById(R.id.miles);
            holder.imageView = (ImageView) convertView.findViewById(R.id.imgView);
            holder.go = (Button) convertView.findViewById(R.id.go);
            holder.cancel = (Button) convertView.findViewById(R.id.cancel);
            convertView.setTag(holder);   //将Holder存储到convertView中
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.imageView.setBackgroundResource(R.drawable.ic_launcher_background);
        if (datas != null) {
            holder.textView1.setText(datas.get(i).getAddress());
            holder.textView2.setText(datas.get(i).getTime());
            holder.textView3.setText(datas.get(i).getArriveTime());
            holder.textView4.setText(datas.get(i).getGoTime());
            holder.textView5.setText(datas.get(i).getDistance());
        }
        holder.go.setOnClickListener(new ListBtnListener(i, mContext));
        holder.cancel.setOnClickListener(new ListBtnListener(i, mContext));
        return convertView;
    }

    static class ViewHolder {
        ImageView imageView;
        TextView textView1;
        TextView textView2;
        TextView textView3;
        TextView textView4;
        TextView textView5;
        Button go;
        Button cancel;
    }

    public class ListBtnListener implements View.OnClickListener {

        int mPosition;
        Context mContext;

        public ListBtnListener(int position, Context context) {
            this.mPosition = position;
            this.mContext = context;
        }

        @Override
        public void onClick(View v) {
//TODO-------------------------------------------------------------------

            switch (v.getId()) {
                case R.id.go:
                    ToastUtil.show(mContext, "导航触发按钮-------------------------");
                    break;
                case R.id.cancel:
                    //删除数据,更新view
                    ToastUtil.showShortToast(mContext, "成功删除一条安排");
                    dataStorage = new DataStorage(mContext, "schedule_table", null, DataStorage.VERSION);
                    db = dataStorage.getReadableDatabase();
                    String whereClauses = "address=?";
                    String[] whereArgs = new String[]{datas.get(mPosition).getAddress()};
//                //调用delete方法，删除数据
                    db.delete("schedule_table", whereClauses, whereArgs);
                    db.close();
//                    View v1 = (View) v.getParent().getParent();
//                    v1.setVisibility(View.GONE);
                    Intent i = new Intent(mContext,DetailSchedule.class);
                    mContext.startActivity(i);


                    break;
            }
        }

    }
}

