package com.zzq.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.zzq.R;
import com.zzq.adapter.DateAdapter;
import com.zzq.view.DragGrid;
import com.zzq.view.MyViewPager;
import com.zzq.view.MyViewPager.PageChangedListener;

public class MainActivity extends Activity {
    private MyViewPager pagers;

    private int pageNum = 3;// 三页，18条数据
    private int perPageItems = 6;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.initComponent();
    }

    public void initComponent() {
        pagers = (MyViewPager) findViewById(R.id.pagers);
        LayoutInflater inflater = getLayoutInflater();
        pagers.init();
        for (int i = 0; i < pageNum; i++) {
            View view = inflater.inflate(R.layout.draggriditem, null);
            DragGrid dragView = (DragGrid) view.findViewById(R.id.gridview);
            ArrayList<String> list = new ArrayList<String>();
            for (int j = 0; j < perPageItems; j++) {
                list.add((i * perPageItems + j) + "");
            }
            dragView.setAdapter(new DateAdapter(this, list));
            dragView.setOnItemClickListener(new ItemClickEvent());
            pagers.addView(view);
        }

    }

    public void registerListener() {
        pagers.setPageChangedListener(new PageChangedListener() {

            @Override
            public void pageScroll(float location) {
                System.out.println("location改变--- " + location);
            }

            @Override
            public void pageChanged(int page) {
                System.out.println("页面改变--- " + page);
            }
        });
    }

    private class OnItemClickListenerImp implements OnItemClickListener {
        int page;

        public OnItemClickListenerImp(int page) {
            this.page = page;
        }

        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            pageChanged(page);

        }

    }

    public void pageChanged(int page) {
        pagers.moveToPage(page);
    }

    class ItemClickEvent implements AdapterView.OnItemClickListener {
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            Toast.makeText(MainActivity.this, "test click", Toast.LENGTH_SHORT).show();
            arg1.setPressed(false);
            arg1.setSelected(false);
        }

    }

}
