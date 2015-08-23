package com.zzq.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.AccelerateInterpolator;
import android.widget.Scroller;

public class MyViewPager extends ViewGroup {
	private Context mContext;

	public MyViewPager(Context context) {
		super(context);
		this.mContext = context;
	}

	public MyViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
	}

	private int mWidth;// 组件宽度
	private int mHeight;// 组件高度
	private int mHalfHeight;// 组件一般宽度，默认为滑动过半后执行翻页
	private Scroller mScroller;// 滚动器

	public void init() {
		mScroller = new Scroller(mContext, new AccelerateInterpolator());
		// 设置观察者以获取组件宽度和高度
		ViewTreeObserver observer = this.getViewTreeObserver();
		observer.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

			@Override
			public void onGlobalLayout() {
				MyViewPager.this.mWidth = getWidth();
				MyViewPager.this.mHeight = getHeight();
				MyViewPager.this.mHalfHeight = mHeight / 2;
			}
		});

	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int left = 0;
		int top = 0;
		int childCount = getChildCount();
		for (int i = 0; i < childCount; i++) {
			View child = getChildAt(i);
			// 为子类分派布局
			child.layout(left, top, left + mWidth, top + mHeight);
			top+=mHeight;
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		// 测量组件高度和宽度
		int width = MeasureSpec.getSize(widthMeasureSpec);
		int height = MeasureSpec.getSize(heightMeasureSpec);
		setMeasuredDimension(width, height);
		int childCount = getChildCount();
		for (int i = 0; i < childCount; i++) {
			View child = getChildAt(i);
			// 测量子组件高度和宽度
			child.measure(0, 0);
		}
	}

	private float pressDownY;// 按下时坐标
	private float pressUpY;// 抬起时坐标
	private float oldY;// 旧坐标
	private float nowOffset;// 页面总移动偏移量
	private int curPage = 0;// 记录当前页面
	private boolean isChangedPage = false;// 页面是否改变了，用于通知监听器

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			this.pressDownY = event.getY();// 记录手指按下时坐标
			this.oldY = pressDownY;// 更新旧坐标
			break;
		case MotionEvent.ACTION_UP:
			this.pressUpY = event.getY();// 记录手指弹起时坐标
			this.actionUp();
			break;
		case MotionEvent.ACTION_MOVE:
			this.userMove(event);// 用户移动
			break;
		}
		return true;
	}

	/**
	 * 
	 * 
	 * 函数名称 : userMove
	 * 
	 * 功能描述 : 用户移动
	 * 
	 * 参数及返回值说明：
	 * 
	 * @param event
	 * 
	 *            描述 ：
	 */
	public void userMove(MotionEvent event) {
		float newY = event.getY();// 获取新坐标
		float move = oldY - newY;// 计算需要移动的距离
		float tempOffset = nowOffset + move;
		if (tempOffset > 0
				&& tempOffset < (mHeight * (getChildCount() - 1))) {// 允许左右各多移动100px宽度
			this.oldY = newY;// 更新旧坐标
			this.nowOffset += move;// 计算总移动偏移量
			scrollBy(0, (int) move);// 执行移动
			if (mListener != null) {
				float scrHeight = (float) mHeight * getChildCount();
				float location = (float) (nowOffset / scrHeight);
				mListener.pageScroll(location);
			}

		}
	}

	/**
	 * 
	 * 
	 * 函数名称 : actionUp
	 * 
	 * 功能描述 : 手指松开
	 * 
	 * 参数及返回值说明：
	 * 
	 * 描述 ：
	 */
	private void actionUp() {
		boolean isToUPMove = pressDownY > pressUpY ? true : false;// 是否向左边移动
		float moveHeight = pressDownY > pressUpY ? pressDownY - pressUpY
				: pressUpY - pressDownY;// 计算手指移动的长度
		System.out.println(" pressDownY " + pressDownY + ",pressUpY "
				+ pressUpY);
		if (moveHeight >= mHalfHeight) {// 如果手指移动的长度超过半屏，则执行移动,否则回弹
			curPage = (int) (nowOffset / mHeight);// 偏移量 / 屏幕宽度 计算需要跳转的页面
			if (isToUPMove) {// 如果是上--->左移动 页面需要执行++
				curPage++;
			}
			isChangedPage = true;// 通知监听
		}
		this.changePage(); // 执行页面改变方法
		oldY = 0;
		pressDownY = 0;
		pressUpY = 0;
	}

	/**
	 * 
	 * 
	 * 函数名称 : changePage
	 * 
	 * 功能描述 :
	 * 
	 * 参数及返回值说明：
	 * 
	 * 描述 ：
	 */
	private void changePage() {
		if (curPage < 0) { // 如果页面超过已有页面，则进行纠正
			curPage = 0;
		} else if (curPage > getChildCount() - 1) {
			curPage = (getChildCount() - 1);
		}
		if (isChangedPage && mListener != null) {// 通知监听页面改变
			mListener.pageChanged(curPage);
		}
		System.out.println("++后 curPage " + curPage);
		// 计算目标滚动位置
		float moveoffset = curPage * mHeight;
		// 计算需要滚动的长度 ----> 目标滚动位置-当前位置
		int dy = (int) (moveoffset - nowOffset);
		// 开始执行滚动 需要和computeScroll 配合使用才行
		mScroller.startScroll(0, (int) nowOffset, 0, dy, 500);
		postInvalidate();
	}

	@Override
	public void computeScroll() {
		// 如果滚动器还没有滚动完成
		if (mScroller.computeScrollOffset()) {
			// 滚动到滚动器的当前位置
			int currY = mScroller.getCurrY();
			scrollTo(0,currY);
			postInvalidate();
			nowOffset = currY;
			// 通知监听器
			if (mListener != null) {
				float scrHeight = (float) mHeight * getChildCount();
				// 当前页面在总页面的位置
				float location = (float) (nowOffset / scrHeight);
				mListener.pageScroll(location);
			}
		}
	}

	/**
	 * 
	 * 
	 * 函数名称 : moveToPage
	 * 
	 * 功能描述 : 移动到指定页面
	 * 
	 * 参数及返回值说明：
	 * 
	 * @param page
	 * 
	 *            描述 ：
	 */
	public void moveToPage(int page) {
		if (!mScroller.isFinished()) {// 如果正在滚动，先停止滚动
			mScroller.forceFinished(true);
		}
		this.curPage = page;
		changePage();
	}

	public int getmHeight() {
		return mHeight;
	}

	public int getCurPage() {
		return curPage;
	}

	// 页面改变监听
	private PageChangedListener mListener;

	public void setPageChangedListener(PageChangedListener listener) {
		this.mListener = listener;
	}

	public interface PageChangedListener {
		public void pageChanged(int page);

		public void pageScroll(float location);
	}
}
