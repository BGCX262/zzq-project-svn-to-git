package cn.thirdgwin.lib;

public interface GLKey {
	/// Constant for a Dummy key. Every invalid key press will be triggered as dummy.
	final static int k_dummy			= 1<<0;
	/// Constant for a invalid key. Every invalid key press will be triggered as dummy.
	final static int k_invalid			= k_dummy;

	/// Constant for the UP key press.
	final static int k_up 				= 1<<1;
	/// Constant for the DOWN key press.
	final static int k_down 			= 1<<2;
	/// Constant for the LEFT key press.
	final static int k_left 			= 1<<3;
	/// Constant for the RIGHT key press.
	final static int k_right 			= 1<<4;
	/// Constant for the FIRE / ENTER key press.
	final static int k_fire 			= 1<<5;

	/// Constant for the Number 0 key press.
	final static int k_num0 			= 1<<6;
	/// Constant for the Number 1 key press.
	final static int k_num1 			= 1<<7;
	/// Constant for the Number 2 key press.
	final static int k_num2 			= 1<<8;
	/// Constant for the Number 3 key press.
	final static int k_num3 			= 1<<9;
	/// Constant for the Number 4 key press.
	final static int k_num4 			= 1<<10;
	/// Constant for the Number 5 key press.
	final static int k_num5 			= 1<<11;
	/// Constant for the Number 6 key press.
	final static int k_num6 			= 1<<12;
	/// Constant for the Number 7 key press.
	final static int k_num7 			= 1<<13;
	/// Constant for the Number 8 key press.
	final static int k_num8 			= 1<<14;
	/// Constant for the Number 9 key press.
	final static int k_num9 			= 1<<15;

	/// Constant for the * key press.
	final static int k_star 			= 1<<16;
	/// Constant for the & key press.
	final static int k_pound 			= 1<<17;

	/// All keys after k_menuOK do not use accumulation!

	/// Constant for the OK Softkey press.
	///!note This key is  handled as immediately released eg: no accumulation is possible.
	final static int k_menuOK 			= 1<<18;
	/// Constant for the Back Softkey press.
	///!note This key is  handled as immediately released eg: no accumulation is possible.
	final static int k_menuBack		= 1<<19;
	
	final static int k_pageUp          = 1<<20;
	final static int k_pageDown        = 1<<21;

	final static int k_volumeUp        = 1<<22;
	final static int k_volumeDown      = 1<<23;

	/// Constant for the key count. (Must be <= 32)
	final static int k_nbKey           = 24;
	
	
	//逻辑键值
	/**逻辑上*/
	final static int L_UP 			= k_up | k_num2;
	/**逻辑下*/
	final static int L_DOWN 		= k_down | k_num8;
	/**逻辑左*/
	final static int L_LEFT 		= k_left | k_num4;
	/**逻辑右*/
	final static int L_RIGHT 		= k_right | k_num6;
	
	/**逻辑选择*/
	final static int L_MENU_MSK = k_fire;
	/**逻辑左软键*/
	final static int L_MENU_LSK = k_menuOK;
	/**逻辑右软键*/
	final static int L_MENU_RSK = k_menuBack;
	
	/**逻辑选择*/
	final static int L_MENU_SELECT = k_menuOK | k_fire | k_num5;
	
	/**左右软键*/
	final static int L_SOFT_LRSK = L_MENU_LSK | L_MENU_RSK;
	
	/**游戏中主角fire*/
	final static int L_FIRE	= k_fire | k_num5;
	
	/**数字键*/
	final static int L_NUM_KEYS = k_num0 | k_num1 | k_num2 | k_num3 | k_num4 | k_num5 | k_num6 | k_num7 | k_num8 | k_num9;
	
	/**方向键*/
	final static int L_DIR_KEYS = L_UP | L_DOWN | L_LEFT | L_RIGHT;
	
	/**X方向键*/
	final static int L_DIR_X = L_LEFT | L_RIGHT;
	
	/**Y方向键*/
	final static int L_DIR_Y = L_UP | L_DOWN;
	
	final static int L_HOT_KEY = k_num1 | k_num3 | k_num7 | k_num9 | k_star | k_pound;
	
	/**任意键(这里定义了的所有按键。 绿键，红键等未定义按键不算作任意键)*/
	final static int L_ANY_KEYS = L_NUM_KEYS | L_DIR_KEYS | L_MENU_MSK | L_MENU_LSK | L_MENU_RSK | k_star | k_pound | k_pageUp | k_pageDown | k_volumeUp | k_volumeDown;

}