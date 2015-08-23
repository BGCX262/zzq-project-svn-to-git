package com.ljp.laucher.util;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ljp.laucher.R;

public class OrderDataAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> lstDate;
    private TextView txtAge;
    private int holdPosition;

    private boolean isChanged = false;
    private boolean ShowItem = false;

    public OrderDataAdapter(Context mContext, ArrayList<String> list) {
        this.context = mContext;
        lstDate = list;
    }

    @Override
    public int getCount() {
        return lstDate.size();
    }

    @Override
    public Object getItem(int position) {
        return lstDate.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public int getHoldPosition() {
        return holdPosition;
    }

    public void setHoldPosition(int holdPosition) {
        this.holdPosition = holdPosition;
    }

    public void exchange(int startPosition, int endPosition) {

        holdPosition = endPosition;
        Object startObject = getItem(startPosition);
        isChanged = true;
        ShowItem = false;

        if (startPosition < endPosition) {
            lstDate.add(endPosition + 1, (String) startObject);
            lstDate.remove(startPosition);
        } else {
            lstDate.add(endPosition, (String) startObject);
            lstDate.remove(startPosition + 1);
        }
        notifyDataSetChanged();
    }

    public void showDropItem(boolean showItem) {
        this.ShowItem = showItem;
    }

    public boolean isChanged() {
        return isChanged;
    }

    public void setChanged(boolean isChanged) {
        this.isChanged = isChanged;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.item, null);
        txtAge = (TextView) convertView.findViewById(R.id.txt_userAge);
        if (lstDate.get(position) == null) {
            txtAge.setText("+");
            txtAge.setBackgroundResource(R.drawable.red);
        } else if (lstDate.get(position).equals("none")) {
            txtAge.setText("");
            txtAge.setBackgroundDrawable(null);
        } else {

            txtAge.setText("Item" + lstDate.get(position));
        }

        if (isChanged) {
            if (position == holdPosition) {
                if (!ShowItem) {
                    convertView.setVisibility(View.INVISIBLE);
                }
            }
        }

        return convertView;
    }

}
