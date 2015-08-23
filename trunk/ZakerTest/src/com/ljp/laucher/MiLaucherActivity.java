package com.ljp.laucher;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ljp.laucher.util.Configure;
import com.ljp.laucher.util.DragGrid;
import com.ljp.laucher.util.OrderDataAdapter;
import com.ljp.laucher.util.ScrollLayout;

public class MiLaucherActivity extends Activity {

    /** 填充到每个横向滑动的scrolllayout中的每个view **/
    private LinearLayout fillLinearLayout;

    /** 代表每屏的gridview **/
    private DragGrid gridView;

    /** 整个纵向滑动的layout **/
    private ScrollLayout scrollLayout;

    /** 显示是第几页的textview **/
    TextView tv_page;

    /** 可纵向滑动的layout中控件的layoutParams **/
    LinearLayout.LayoutParams LinearLayoutParam;

    /** 存放所有gridview的list **/
    private ArrayList<DragGrid> gridviews = new ArrayList<DragGrid>();

    /** 模拟的虚拟数据　 **/
    ArrayList<ArrayList<String>> lists = new ArrayList<ArrayList<String>>();
    ArrayList<String> lstDate = new ArrayList<String>();

    SensorManager sm;
    SensorEventListener lsn;
    boolean isClean = false;
    Vibrator vibrator;
    int rockCount = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        /** 初始化view **/
        initView();

        /** 初始化数据 **/
        initData();

    }

    private void initView() {

        setContentView(R.layout.main);
        scrollLayout = (ScrollLayout) findViewById(R.id.views);

        /** 显示在第几页 **/
        tv_page = (TextView) findViewById(R.id.tv_page);
        tv_page.setText(getString(R.string.first_page));

        /** 初始化全局工具类 **/
        Configure.init(MiLaucherActivity.this);

        /** 每屏的布局参数 **/
        LinearLayoutParam = new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT,
                android.view.ViewGroup.LayoutParams.FILL_PARENT);
        LinearLayoutParam.rightMargin = (int) this.getResources().getDimension(R.dimen.view_margin_left);
        LinearLayoutParam.leftMargin = (int) this.getResources().getDimension(R.dimen.view_margin_right);

        /** 清空原来layout中的数据 **/
        if (gridView != null) {
            scrollLayout.removeAllViews();
        }
    }

    public void init() {

    }

    public void initData() {

        /** 填充模拟数据 **/
        for (int i = 0; i < 22; i++) {

            lstDate.add("" + i);
        }

        Configure.countPages = (int) Math.ceil(lstDate.size() / (float) Configure.PAGE_SIZE);
        lists = new ArrayList<ArrayList<String>>();
        for (int i = 0; i < Configure.countPages; i++) {
            lists.add(new ArrayList<String>());
            for (int j = Configure.PAGE_SIZE * i; j < (Configure.PAGE_SIZE * (i + 1) > lstDate.size() ? lstDate.size()
                    : Configure.PAGE_SIZE * (i + 1)); j++)
                lists.get(i).add(lstDate.get(j));
        }
        boolean isLast = true;
        for (int i = lists.get(Configure.countPages - 1).size(); i < Configure.PAGE_SIZE; i++) {
            if (isLast) {
                lists.get(Configure.countPages - 1).add(null);
                isLast = false;
            } else
                lists.get(Configure.countPages - 1).add("none");
        }

        for (int i = 0; i < Configure.countPages; i++) {

            scrollLayout.addView(addGridView(i));

        }

        scrollLayout.setPageListener(new ScrollLayout.PageListener() {

            @Override
            public void page(int page) {
                setCurPage(page);
            }

        });

        DragGrid.gridviews = gridviews;

    }

    public void CleanItems() {

    }

    public int getFristNonePosition(ArrayList<String> array) {
        for (int i = 0; i < array.size(); i++) {
            if (array.get(i) != null && array.get(i).toString().equals("none")) {
                return i;
            }
        }
        return -1;
    }

    public int getFristNullPosition(ArrayList<String> array) {
        for (int i = 0; i < array.size(); i++) {
            if (array.get(i) == null) {
                return i;
            }
        }
        return -1;
    }

    public LinearLayout addGridView(int i) {

        fillLinearLayout = new LinearLayout(MiLaucherActivity.this);

        fillLinearLayout = new LinearLayout(MiLaucherActivity.this);
        gridView = new DragGrid(MiLaucherActivity.this);
        gridView.setAdapter(new OrderDataAdapter(MiLaucherActivity.this, lists.get(i)));
        gridView.setNumColumns(2);
        gridView.setHorizontalSpacing(0);
        gridView.setVerticalSpacing(0);
        final int ii = i;
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2, long arg3) {

                if (lists.get(ii).get(arg2) == null) {
                    new AlertDialog.Builder(MiLaucherActivity.this).setTitle("添加")
                            .setItems(R.array.items, new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    final String[] arrayAddItems = getResources().getStringArray(R.array.items); // array
                                    lists.get(ii).add(arg2, arrayAddItems[which]);
                                    lists.get(ii).remove(arg2 + 1);

                                    if (getFristNonePosition(lists.get(ii)) > 0
                                            && getFristNullPosition(lists.get(ii)) < 0) {
                                        lists.get(ii).set(getFristNonePosition(lists.get(ii)), null);
                                    }
                                    if (getFristNonePosition(lists.get(ii)) < 0
                                            && getFristNullPosition(lists.get(ii)) < 0) {
                                        System.out.println("===");
                                        if (ii == Configure.countPages - 1
                                                || (getFristNullPosition(lists.get(lists.size() - 1)) < 0 && getFristNonePosition(lists
                                                        .get(lists.size() - 1)) < 0)) {
                                            lists.add(new ArrayList<String>());
                                            lists.get(lists.size() - 1).add(null);
                                            for (int i = 1; i < Configure.PAGE_SIZE; i++)
                                                lists.get(lists.size() - 1).add("none");

                                            scrollLayout.addView(addGridView(Configure.countPages));
                                            Configure.countPages++;
                                        } else if (getFristNonePosition(lists.get(lists.size() - 1)) > 0
                                                && getFristNullPosition(lists.get(lists.size() - 1)) < 0) {
                                            lists.get(lists.size() - 1).set(
                                                    getFristNonePosition(lists.get(lists.size() - 1)), null);
                                            ((OrderDataAdapter) ((gridviews.get(lists.size() - 1)).getAdapter()))
                                                    .notifyDataSetChanged();
                                        }
                                    }
                                    ((OrderDataAdapter) ((gridviews.get(ii)).getAdapter())).notifyDataSetChanged();
                                }
                            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    dialog.dismiss();
                                }
                            }).show();
                } else {
                    Toast.makeText(MiLaucherActivity.this, lists.get(ii).get(arg2), Toast.LENGTH_SHORT).show();
                }
            }
        });
        gridView.setSelector(R.anim.grid_light);

        /** gridview page操作的监听 **/
        gridView.setPageListener(new DragGrid.G_PageListener() {
            @Override
            public void page(int cases, int page) {
                switch (cases) {

                /** 滑动页面 **/
                case 0://
                    scrollLayout.snapToScreen(page);
                    setCurPage(page);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Configure.isChangingPage = false;
                        }
                    }, 800);
                    break;

                }
            }
        });

        /** gridview切屏了，item变化 **/
        gridView.setOnItemChangeListener(new DragGrid.G_ItemChangeListener() {
            @Override
            public void change(int from, int to, int hidePosition) {

                Log.v("test", "Configure.curentPage-------888888--------> " + Configure.curentPage);

                Log.v("test", "Configure.lastPage-------888888--------> " + Configure.lastPage);
                if (Configure.lastPage != Configure.curentPage) {

                    /** 当前的adpter信息 **/
                    OrderDataAdapter currentDataAdapter = (OrderDataAdapter) gridviews.get(Configure.curentPage)
                            .getAdapter();
                    String formString = (String) lists.get(Configure.curentPage).get(from);

                    /** 前一屏的adpter信息 **/
                    OrderDataAdapter lastDataAdapter = (OrderDataAdapter) gridviews.get(Configure.lastPage)
                            .getAdapter();
                    String toString = (String) lists.get(Configure.lastPage).get(to);
                    lastDataAdapter.showDropItem(true);

                    lists.get(Configure.lastPage).add(to, formString);
                    lists.get(Configure.lastPage).remove(to + 1);
                    lists.get(Configure.curentPage).add(from, toString);
                    lists.get(Configure.curentPage).remove(from + 1);

                    currentDataAdapter.notifyDataSetChanged();
                    lastDataAdapter.notifyDataSetChanged();

                }

            }
        });

        gridView.testFlagNumber = gridviews.size();

        gridviews.add(gridView);
        fillLinearLayout.addView(gridviews.get(i), LinearLayoutParam);
        return fillLinearLayout;
    }

    /**
     * 设置显示当前是第几页
     * 
     * @param page
     */
    public void setCurPage(final int page) {

        Animation a = AnimationUtils.loadAnimation(MiLaucherActivity.this, R.anim.scale_in);
        a.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                tv_page.setText((page + 1) + "");
                tv_page.startAnimation(AnimationUtils.loadAnimation(MiLaucherActivity.this, R.anim.scale_out));
            }
        });
        tv_page.startAnimation(a);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
