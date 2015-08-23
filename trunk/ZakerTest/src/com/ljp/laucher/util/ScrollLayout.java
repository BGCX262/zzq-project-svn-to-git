package com.ljp.laucher.util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * 
 * 从写控件，实现上下翻屏的操作
 * 
 */
public class ScrollLayout extends ViewGroup {

    /** 滑动控制对象 **/
    private Scroller mScroller;
    /** 用来追踪触摸事件的速率 **/
    private VelocityTracker mVelocityTracker;

    /** 当前是第几屏 **/
    private int mCurScreen;

    /** 默认显示第几屏 **/
    private int mDefaultScreen = 0;

    /** 当手指抬起时释放touch事件 **/
    private static final int TOUCH_STATE_REST = 0;

    /** 一直在滚动过程中 **/
    private static final int TOUCH_STATE_SCROLLING = 1;

    /** 快速滑动切屏的速度 **/
    private static final int SNAP_VELOCITY = 600;

    /** touch事件的状态 **/
    private int mTouchState = TOUCH_STATE_REST;

    /** 意思应该是触发移动事件的最短距离 **/
    private int mTouchSlop;

    /** 记录Y值的位置 **/
    private float mLastMotionY;

    /** pageListener **/
    private PageListener pageListener;

    public ScrollLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScrollLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mScroller = new Scroller(context);
        mCurScreen = mDefaultScreen;
        /** 触发移动事件的最短距离，如果小于这个距离就不触发移动控件 **/
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

    /** onlayout方法用于确定布局位置 ，重写viewgroup，onlayout必须重写 **/
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childTop = 0;
        final int childCount = getChildCount();

        /** 设置每个子view的位置 **/
        for (int i = 0; i < childCount; i++) {
            final View childView = getChildAt(i);
            if (childView.getVisibility() != View.GONE) {
                final int childHeight = childView.getMeasuredHeight();
                childView.layout(0, childTop, childView.getMeasuredWidth(), childTop + childHeight);
                childTop += childHeight;
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        if (widthMode != MeasureSpec.EXACTLY) {

            throw new IllegalStateException("ScrollLayout only canmCurScreen run at EXACTLY mode!");

        }

        /**
         * wrap_content 传进去的是AT_MOST 固定数值或fill_parent 传入的模式是EXACTLY
         */
        final int height = MeasureSpec.getSize(heightMeasureSpec);
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (heightMode != MeasureSpec.EXACTLY) {
            throw new IllegalStateException("ScrollLayout only can run at EXACTLY mode!");
        }

        /** The children are given the same width and height as the scrollLayout **/
        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec);
        }

        /** 滑动到当前屏 **/
        scrollTo(0, mCurScreen * height);
    }

    /**
     * According to the position of current layout scroll to the destination page.
     */
    public void snapToDestination() {

        final int screenHeight = getHeight();

        final int destScreen = (getScrollY() + screenHeight / 2) / screenHeight;
        snapToScreen(destScreen);
    }

    public void snapToScreen(int whichScreen) {

        /** whichScreen 只能在0，和 getChildCount()-1之间 **/
        whichScreen = Math.max(0, Math.min(whichScreen, getChildCount() - 1));

        /** 是否需要切屏 **/
        if (getScrollY() != (whichScreen * getHeight())) {

            final int delta = whichScreen * getHeight() - getScrollY();
            mScroller.startScroll(0, getScrollY(), 0, delta, Math.abs(delta) * 2);

            /** 设置当前屏数 **/
            mCurScreen = whichScreen;
            Configure.curentPage = whichScreen;

            if (pageListener != null) {

                pageListener.page(Configure.curentPage);
            }

            invalidate();
        }
    }

    public void setToScreen(int whichScreen) {

        whichScreen = Math.max(0, Math.min(whichScreen, getChildCount() - 1));
        mCurScreen = whichScreen;
        scrollTo(0, whichScreen * getHeight());

    }

    /**
     * 获得当前页码
     */
    public int getCurScreen() {

        return mCurScreen;
    }

    /**
     * 当滑动后的当前页码
     */
    public int getPage() {

        return Configure.curentPage;

    }

    @Override
    public void computeScroll() {

        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (mVelocityTracker == null) {

            mVelocityTracker = VelocityTracker.obtain();

        }

        /** 监听当前事件 **/
        mVelocityTracker.addMovement(event);
        final float y = event.getY();

        switch (event.getAction()) {

        case MotionEvent.ACTION_DOWN:

            /** 如果上次的切屏效果没有执行结束，马上停止切屏效果 **/
            if (!mScroller.isFinished()) {
                mScroller.abortAnimation();
            }
            mLastMotionY = y;
            break;
        case MotionEvent.ACTION_MOVE:

            /** 跟随手指移动，移动距离为deltaY **/
            int deltaY = (int) (mLastMotionY - y);
            mLastMotionY = y;
            scrollBy(0, deltaY);

            break;

        case MotionEvent.ACTION_UP:

            final VelocityTracker velocityTracker = mVelocityTracker;
            velocityTracker.computeCurrentVelocity(1000);
            int velocityY = (int) velocityTracker.getYVelocity();

            if (velocityY > SNAP_VELOCITY && mCurScreen > 0) {
                snapToScreen(mCurScreen - 1);

            } else if (velocityY < -SNAP_VELOCITY && mCurScreen < getChildCount() - 1) {
                snapToScreen(mCurScreen + 1);
            } else {
                snapToDestination();
            }
            if (mVelocityTracker != null) {
                mVelocityTracker.recycle();
                mVelocityTracker = null;
            }
            mTouchState = TOUCH_STATE_REST;

            break;

        case MotionEvent.ACTION_CANCEL:

            mTouchState = TOUCH_STATE_REST;
            break;
        }
        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        /** 假如已经长按了，将事件分发给下层view **/

        if (Configure.isMove) {

            return false;

        }

        /** 如果ScrollLayout正在移动过程中，直接截断touch事件，给本view的onTouchEvent(MotionEvent ev)执行 **/
        final int action = ev.getAction();
        if ((action == MotionEvent.ACTION_MOVE) && (mTouchState != TOUCH_STATE_REST)) {
            return true;
        }

        final float y = ev.getY();
        switch (action) {

        case MotionEvent.ACTION_MOVE:

            final int yDiff = (int) Math.abs(mLastMotionY - y);
            if (yDiff > mTouchSlop) {
                mTouchState = TOUCH_STATE_SCROLLING;
            }

            break;

        case MotionEvent.ACTION_DOWN:

            mLastMotionY = y;
            mTouchState = mScroller.isFinished() ? TOUCH_STATE_REST : TOUCH_STATE_SCROLLING;

            break;

        case MotionEvent.ACTION_CANCEL:
        case MotionEvent.ACTION_UP:

            mTouchState = TOUCH_STATE_REST;

            break;
        }

        return mTouchState != TOUCH_STATE_REST;
    }

    public void setPageListener(PageListener pageListener) {
        this.pageListener = pageListener;
    }

    public interface PageListener {
        void page(int page);
    }

}