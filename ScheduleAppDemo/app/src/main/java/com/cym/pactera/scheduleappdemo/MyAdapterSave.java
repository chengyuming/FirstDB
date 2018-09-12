package com.cym.pactera.scheduleappdemo;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.cym.pactera.scheduleappdemo.entity.SaveUse;
import com.cym.pactera.scheduleappdemo.util.DataStorage;
import com.cym.pactera.scheduleappdemo.util.ToastUtil;
import java.util.ArrayList;

/**
 * Created by pactera on 2018/9/4.
 */
public class MyAdapterSave extends BaseAdapter {
    private Context mContext;
    private ArrayList<SaveUse> datas;
    private DataStorage dataStorage;
    private SQLiteDatabase db;
    public MyAdapterSave(Context mContext,ArrayList<SaveUse> datas){
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_save, parent, false);
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
            switch (v.getId()) {
                case R.id.go:
                    ToastUtil.show(mContext, "导航触发按钮-------------------------");
                    break;
                case R.id.cancel:
                    //删除数据,更新view
                    ToastUtil.showShortToast(mContext, "成功删除一条待用安排");
                    dataStorage = new DataStorage(mContext, "saveUse_table", null, DataStorage.VERSION);
                    db = dataStorage.getReadableDatabase();
                    String whereClauses = "address=?";
                    String[] whereArgs = new String[]{datas.get(mPosition).getAddress()};
//                //调用delete方法，删除数据
                    db.delete("saveUse_table", whereClauses, whereArgs);
                    db.close();
//                    View v1 = (View) v.getParent().getParent();
//                    v1.setVisibility(View.GONE);
                    Intent i = new Intent(mContext,SaveUseActivity.class);
                    mContext.startActivity(i);


                    break;
            }
        }

    }
}
