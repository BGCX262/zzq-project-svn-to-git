package cn.thirdgwin.lib;

/**
 * <b>AnimPlayerListener</b><br>
 * 每个动画会触发事件的对象都应该继承该接口
 * @author MaNing
 * 用来
 */
public abstract interface zAnimPlayerListener {

	/**
	 * 当AnimPlayer paint到指定的module的时候触发该函数。
	 * 目前使用Sprite中的 MD_TYPE = MD_FILL_TRIANGLE || MD_TYPE = MD_TRIANGLE;
	 * params原数据（color(int), p2x(short), p2y(short), p3x(short), p3y(short)）共5个数
	 * 当做param数组传入
	 */
	public boolean onTouchedJYModule(int[] params, int paramNum);
	
	/** 监听器  */
	static final int IDX_TYPE = 4;
	static final int IDX_PARAM1 = 0;
	static final int IDX_PARAM2 = 1;
	static final int IDX_PARAM3 = 2;
	static final int IDX_PARAM4 = 3;
	
	/**IDX_TYPE*/
	static final int TYPE_SKILL_START 	= 1;
	static final int TYPE_MOVE_START 	= 2;
	static final int TYPE_MOVE_END	 	= 3;
	static final int TYPE_BG_START		= 4;
	static final int TYPE_BG_END		= 5;
	
}

