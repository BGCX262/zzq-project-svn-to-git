package com.ljp.laucher.util;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.ljp.laucher.R;

/**
 * 
 * 继承gridview,重写相关方法
 * 
 * **/
public class DragGrid extends GridView {

    protected ViewGroup fromView;
    private G_PageListener pageListener;
    private G_ItemChangeListener itemListener;
    private WindowManager windowManager;
    private WindowManager.LayoutParams windowParams;
    private ImageView iv_drag;
    /**
     * Rectangle used for hit testing children
     */
    private Rect mTouchFrame;
    /** 浮动窗前要拆去的部分 **/
    private static final int WINDOWS_TAKE_AWAY_PART = 8;

    /** 不是首次touch事件，首次touch 记录x,y 的位置 **/
    boolean isCountXY = false;

    /** 最后点击的x，y坐标 **/
    private int mLastX, mLastY;

    /** 当长按生成的窗口和原view左边的差距 **/
    private static final int WINDOWS_PADDING_LEFT = 28;

    /** 当长按生成的窗口和原view上边的差距 **/
    private static final int WINDOWS_PADDING_TOP = (int) (40 * Configure.screenDensity) + 8;

    /** 窗口的透明度　 **/
    private static final float WINDOWS_ALPHA = 0.8f;

    /** 移动到那一屏 **/
    public int moveToNum;

    /** 每个item宽度的一半 **/
    private int halfItemWidth;

    /** grid item的总数 **/
    private int itemTotalCount;

    /** 每行几列 **/
    private int nColumns = 2;

    /** 是否在移动过程中 **/
    private boolean isMoving = false;

    /** 第几次位于可以翻页的位置 **/
    int changePageCount = 0;

    /** 可以翻页的次数 **/
    private static final int CHANGEPAGECOUNTNUMBER = 3;

    /** 往下翻页的范围间距 **/
    private static final int INTERVAL_CHANGE_TO_NEXT_PAGE = 100;

    /** 往上翻页的范围间距 **/
    private static final int INTERVAL_CHANGE_TO_UP_PAGE = 8;

    /** 每屏几个view **/
    private static final int EVERY_SCREEN_NUMBER = 8;

    private int xtox;
    private int ytoy;
    private int lastPosition = -1;
    private int specialItemY;
    private int startPosition;
    private int dragPosition;
    private int dropPosition;
    private int holdPosition;
    private boolean isChangeData = false;
    private String LastAnimationID;

    public int testFlagNumber = -1;

    public static ArrayList<DragGrid> gridviews = new ArrayList<DragGrid>();

    /** 构造方法 **/
    public DragGrid(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DragGrid(Context context) {

        super(context);
    }

    /** 长按事件处理 **/
    public boolean setOnItemLongClickListener(final MotionEvent ev) {
        gridviews.get(Configure.curentPage).setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                Configure.lastPage = Configure.curentPage;

                /** item 能够移动 **/
                Configure.isMove = true;
                int x = (int) ev.getX();
                int y = (int) ev.getY();

                mLastX = x;
                mLastY = y;
                startPosition = dragPosition = dropPosition = arg2;

                /** 无效位置失效 **/
                if (dragPosition == AdapterView.INVALID_POSITION) {

                    return false;

                }

                fromView = (ViewGroup) gridviews.get(Configure.curentPage).getChildAt(
                        dragPosition - getFirstVisiblePosition());

                if (!isCountXY) {

                    halfItemWidth = fromView.getWidth() / 2;
                    itemTotalCount = gridviews.get(Configure.curentPage).getCount();
                    lastPosition = itemTotalCount - 1 - itemTotalCount % nColumns;
                    isCountXY = true;

                }
                if (lastPosition != dragPosition && dragPosition != -1) {
                    specialItemY = gridviews.get(Configure.curentPage).getChildAt(lastPosition).getTop();
                }
                /**
                 * 
                 * 以下代码的作用是将view转化成一个bitmap，要删除， 需求实现只是需要变成，这里是需要的 dragPosition，代表的当前view的位置，考虑到grid的缓存机制 当前view
                 * 应该是dragPosition - getFirstVisiblePosition()
                 * 
                 * **/

                fromView.destroyDrawingCache();
                fromView.setDrawingCacheEnabled(true);
                fromView.setDrawingCacheBackgroundColor(0xff6DB7ED);
                Bitmap bm = Bitmap.createBitmap(fromView.getDrawingCache());
                Bitmap bitmap = Bitmap.createBitmap(bm, WINDOWS_TAKE_AWAY_PART, WINDOWS_TAKE_AWAY_PART, bm.getWidth()
                        - WINDOWS_TAKE_AWAY_PART, bm.getHeight() - WINDOWS_TAKE_AWAY_PART);
                startDrag(bitmap, x, y);
                hideDropItem(arg2);
                isMoving = false;
                return false;
            };
        });
        return super.onInterceptTouchEvent(ev);

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            return setOnItemLongClickListener(ev);
        }
        return super.onInterceptTouchEvent(ev);
    }

    private void startDrag(final Bitmap bm, final int x, final int y) {

        windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);

        /** 执行一个动画将view全部消失 **/
        Animation disappear = AnimationUtils.loadAnimation(getContext(), R.anim.out);
        disappear.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                /** 当前动画结束view消失 **/
                fromView.setVisibility(View.GONE);
                stopDrag();
                windowParams = new WindowManager.LayoutParams();
                windowParams.gravity = Gravity.TOP | Gravity.LEFT;
                /** 确定window窗口的位置 **/
                windowParams.x = fromView.getLeft() + WINDOWS_PADDING_LEFT;
                windowParams.y = fromView.getTop() + WINDOWS_PADDING_TOP;
                windowParams.alpha = WINDOWS_ALPHA;
                windowParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
                windowParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
                iv_drag = new ImageView(getContext());
                iv_drag.setImageBitmap(bm);
                windowManager.addView(iv_drag, windowParams);
                iv_drag.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.del_done));
            }
        });
        fromView.startAnimation(disappear);
    }

    public void hideDropItem(int selectPosition) {
        final OrderDataAdapter adapter = (OrderDataAdapter) gridviews.get(Configure.curentPage).getAdapter();
        adapter.showDropItem(false);

    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (iv_drag != null && dragPosition != AdapterView.INVALID_POSITION) {
            int x = (int) ev.getX();
            int y = (int) ev.getY();

            switch (ev.getAction()) {

            case MotionEvent.ACTION_MOVE:

                if (!isCountXY) {

                    xtox = x - mLastX;
                    ytoy = y - mLastY;
                    isCountXY = true;

                }
                onDrag(x, y);

                if (!isMoving) {

                    OnMove(x, y);

                }

                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:

                /** 停止窗口显示 **/
                stopDrag();
                /** 交换view对象 **/
                onDrop(x, y);
                break;

            }
        }
        return super.onTouchEvent(ev);

    }

    private void onDrag(int x, int y) {

        Log.v("test", "y--------------> " + y);

        /** 随手指生成一个window显示窗体 **/
        if (iv_drag != null) {

            windowParams.alpha = WINDOWS_ALPHA;
            windowParams.x = (x - mLastX - xtox) + fromView.getLeft() + WINDOWS_PADDING_TOP;
            windowParams.y = (y - mLastY - ytoy) + fromView.getTop() + WINDOWS_PADDING_LEFT - moveToNum
                    * Configure.screenHeight;
            windowManager.updateViewLayout(iv_drag, windowParams);

        }

        /** 翻页的操作 **/
        if ((y >= (moveToNum + 1) * Configure.screenHeight - INTERVAL_CHANGE_TO_NEXT_PAGE * Configure.screenDensity || y <= moveToNum
                * Configure.screenHeight + INTERVAL_CHANGE_TO_UP_PAGE * Configure.screenDensity)
                && !Configure.isChangingPage) {

            Log.v("test", "my-----------------moveToNum -----" + moveToNum);

            Log.v("test", "y-------------------------------> " + y);

            Log.v("test", "moveToNum-----------------------> " + moveToNum);

            Log.v("test", "Configure.curentPage-----------------------> " + Configure.curentPage);
            changePageCount++;
            if (changePageCount > CHANGEPAGECOUNTNUMBER) {

                changePageCount = 0;
                if (y >= (moveToNum + 1) * Configure.screenHeight - INTERVAL_CHANGE_TO_NEXT_PAGE
                        * Configure.screenDensity
                        && Configure.curentPage < Configure.countPages - 1) {

                    Configure.isChangingPage = true;
                    /** 向下翻页 **/
                    Configure.lastPage = Configure.curentPage;
                    pageListener.page(0, ++Configure.curentPage);
                    moveToNum++;
                    mTouchFrame = null;
                    isChangeData = true;

                } else if (y <= moveToNum * Configure.screenHeight + INTERVAL_CHANGE_TO_UP_PAGE
                        * Configure.screenDensity
                        && Configure.curentPage > 0) {

                    Configure.isChangingPage = true;
                    Configure.lastPage = Configure.curentPage;
                    /** 向上翻页 **/
                    pageListener.page(0, --Configure.curentPage);
                    moveToNum--;
                    mTouchFrame = null;
                    isChangeData = true;

                }

            }
        } else {

            changePageCount = 0;

        }

    }

    public Animation getMyAnimation(float x, float y) {
        TranslateAnimation go = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, x,
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, y);
        go.setFillAfter(true);
        go.setDuration(550);
        return go;
    }

    public Animation getDownAnimation(float x, float y) {
        AnimationSet set = new AnimationSet(true);
        TranslateAnimation go = new TranslateAnimation(Animation.RELATIVE_TO_SELF, x, Animation.RELATIVE_TO_SELF, x,
                Animation.RELATIVE_TO_SELF, y, Animation.RELATIVE_TO_SELF, y);
        go.setFillAfter(true);
        go.setDuration(550);

        AlphaAnimation alpha = new AlphaAnimation(0.1f, 1.0f);
        alpha.setFillAfter(true);
        alpha.setDuration(550);

        ScaleAnimation scale = new ScaleAnimation(1.2f, 1.0f, 1.2f, 1.0f);
        scale.setFillAfter(true);
        scale.setDuration(550);

        set.addAnimation(go);
        set.addAnimation(alpha);
        set.addAnimation(scale);
        return set;
    }

    public Animation getDelAnimation(int x, int y) {
        AnimationSet set = new AnimationSet(true);
        RotateAnimation rotate = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setFillAfter(true);
        rotate.setDuration(550);
        AlphaAnimation alpha = new AlphaAnimation(1.0f, 0.0f);
        alpha.setFillAfter(true);
        alpha.setDuration(550);
        set.addAnimation(alpha);
        set.addAnimation(rotate);
        return set;
    }

    public void setPageListener(G_PageListener pageListener) {
        this.pageListener = pageListener;
    }

    public interface G_PageListener {
        void page(int cases, int page);
    }

    public void setOnItemChangeListener(G_ItemChangeListener pageListener) {
        this.itemListener = pageListener;
    }

    public interface G_ItemChangeListener {
        void change(int from, int to, int hidePosition);
    }

    private void stopDrag() {
        if (iv_drag != null) {
            windowManager.removeView(iv_drag);
            iv_drag = null;
        }
    }

    private void onDrop(int x, int y) {

        Configure.isMove = false;

        if (moveToNum != 0) {

            moveToNum = 0;
        }

        Configure.lastPage = Configure.curentPage;

        for (int i = 0; i < Configure.countPages; i++) {

            OrderDataAdapter adapter = (OrderDataAdapter) gridviews.get(i).getAdapter();
            adapter.showDropItem(true);
            adapter.notifyDataSetChanged();
            gridviews.get(i).invalidate();
        }

    }

    public int getMyPointToPosition(int x, int y) {
        Rect frame = mTouchFrame;
        if (frame == null) {
            mTouchFrame = new Rect();
            frame = mTouchFrame;
        }

        int count = gridviews.get(Configure.curentPage).getChildCount();

        for (int i = count - 1; i >= 0; i--) {
            final View child = gridviews.get(Configure.curentPage).getChildAt(i);
            if (child.getVisibility() == View.VISIBLE) {
                child.getHitRect(frame);
                if (frame.contains(x, y)) {

                    return i;
                }
            }
        }
        return INVALID_POSITION;
    }

    /**
     * 
     * @param x
     * @param y
     * 
     *            执行移动操作
     * 
     */
    public void OnMove(int x, int y) {

        int TempPosition;
        if (moveToNum != 0) {

            TempPosition = getMyPointToPosition(x, y - Configure.screenHeight * moveToNum);

        } else {

            TempPosition = getMyPointToPosition(x, y);
        }

        if (Configure.curentPage == 0 && TempPosition <= 1) {

            return;

        }

        int MoveNum = 0;

        if (!isChangeData) {

            int sOffsetY = specialItemY == -1 ? y - mLastY : y - specialItemY - halfItemWidth;

            if (TempPosition != AdapterView.INVALID_POSITION && TempPosition != dragPosition) {

                dropPosition = TempPosition;

            } else if (lastPosition != -1 && dragPosition == lastPosition && sOffsetY >= halfItemWidth) {

                dropPosition = (itemTotalCount - 1);

            }
            if (dragPosition != startPosition) {

                dragPosition = startPosition;

            }

            MoveNum = dropPosition - dragPosition;

        } else {

            if (Configure.lastPage < Configure.curentPage) {

                startPosition = 0;

            } else {

                startPosition = EVERY_SCREEN_NUMBER - 1;
            }

            /** 交换数据 **/
            itemListener.change(startPosition, dropPosition, dropPosition);

            /** 移动数据并返回 **/
            isChangeData = false;

        }

        if (dragPosition != startPosition && dragPosition == dropPosition) {

            MoveNum = 0;

        }
        if (MoveNum != 0) {

            int itemMoveNum = Math.abs(MoveNum);
            float Xoffset, Yoffset;
            for (int i = 0; i < itemMoveNum; i++) {

                if (MoveNum > 0) {

                    holdPosition = dragPosition + 1;
                    Xoffset = (dragPosition / nColumns == holdPosition / nColumns) ? (-1) : (nColumns - 1);
                    Yoffset = (dragPosition / nColumns == holdPosition / nColumns) ? 0 : (-1);

                } else {

                    holdPosition = dragPosition - 1;
                    Xoffset = (dragPosition / nColumns == holdPosition / nColumns) ? 1 : (-(nColumns - 1));
                    Yoffset = (dragPosition / nColumns == holdPosition / nColumns) ? 0 : 1;

                }
                ViewGroup moveView = (ViewGroup) gridviews.get(Configure.curentPage).getChildAt(holdPosition);
                Animation animation = getMoveAnimation(Xoffset, Yoffset);
                animation.setFillAfter(true);
                moveView.startAnimation(animation);
                dragPosition = holdPosition;
                if (dragPosition == dropPosition) {

                    LastAnimationID = animation.toString();

                }

                final OrderDataAdapter adapter = (OrderDataAdapter) gridviews.get(Configure.curentPage).getAdapter();
                animation.setAnimationListener(new Animation.AnimationListener() {

                    @Override
                    public void onAnimationStart(Animation animation) {
                        isMoving = true;
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                        String animaionID = animation.toString();

                        if (animaionID.equalsIgnoreCase(LastAnimationID)) {

                            adapter.exchange(startPosition, dropPosition);
                            startPosition = dropPosition;
                            isMoving = false;

                        }
                    }
                });
            }
        }
    }

    /** 生成交换动画 **/
    public Animation getMoveAnimation(float x, float y) {
        TranslateAnimation go = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, x,
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, y);
        go.setFillAfter(true);
        go.setDuration(200);
        return go;
    }

}
