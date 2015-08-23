package cn.thirdgwin.app;

import cn.thirdgwin.lib.DevConfig;
import cn.thirdgwin.lib.zServiceSprite;

public interface Const {
/** 存档，存档的文字的最大长度 */	
	public static final String RMS_NAME 	= "HIGHSCORE";
	public static final int RMS_PNAME_LEN	= 6;
	/** 左右软键的尺寸 */
	public static final int SK_W			= 32;
	public static final int SK_H			= 16;
/** 游戏状态 */	
	public static final int GS_SOUNDONOFF	= 0;
	public static final int GS_SP			= 1 + GS_SOUNDONOFF;
	public static final int GS_CP			= 1 + GS_SP;
	public static final int GS_SUP          = 1+ GS_CP;
	public static final int GS_INIT			= 1 + GS_SUP;
	public static final int GS_MAINMENU		= 1 + GS_INIT;
	public static final int GS_NETWORKING   = 1 + GS_MAINMENU;
	public static final int GS_PLAY			= 1 + GS_NETWORKING;
	public static final int GS_SUMMARY		= 1 + GS_PLAY;
	public static final int GS_HIGHSCORE	= 1 + GS_SUMMARY;
	public static final int GS_LOADING		= 1 + GS_HIGHSCORE;
	public static final int GS_EXITMOREGAME	= 1 + GS_LOADING;
	public static final int GS_ZEROLOGIN    = 1 + GS_EXITMOREGAME;
	public static final int GS_INTRO		= 1 + GS_ZEROLOGIN;
	public static final int GS_UPSCORE		= 1 + GS_INTRO;
	/** 子状态:菜单 */
	public static final int MM_S_Init = 0;
	public static final int MM_S_RUN = 1 + MM_S_Init;
	/** 子状态: PLAY */
	public static final int PLAY_S_Init = 0;
	public static final int PLAY_S_Play = 1 + PLAY_S_Init;	
	public static final int PLAY_S_IGM = 1 + PLAY_S_Play;	
	/** 菜单项目定义 */	
	public static final int ID_MAINMENU			= 1;
	public static final int ID_NEWGAME			= 1 + ID_MAINMENU;
	public static final int ID_ZERO				= 1 + ID_NEWGAME;
	public static final int ID_INTRO			= 1 + ID_ZERO;
	public static final int ID_CONTINUE			= 1 + ID_INTRO;
	public static final int ID_HIGHSCORE	 	= 1 + ID_CONTINUE;
	public static final int ID_MUSIC		 	= 1 + ID_HIGHSCORE;
	public static final int ID_MOREGAME			= 1 + ID_MUSIC;
	public static final int ID_ABOUT			= 1 + ID_MOREGAME;
	public static final int ID_ABOUT_CONTENT	= 1 + ID_ABOUT;
	public static final int ID_QUIT				= 1 + ID_ABOUT_CONTENT;
	public static final int ID_QUIT_CONFIRM		= 1 + ID_QUIT;
	public static final int ID_GM_NORMAL		= 1 + ID_QUIT_CONFIRM;
	public static final int ID_GM_TIMELIMIT		= 1 + ID_GM_NORMAL;
	public static final int ID_GM_RANDOM		= 1 + ID_GM_TIMELIMIT;
	public static final int ID_CHOOSELIB		= 1 + ID_GM_RANDOM;
	public static final int ID_IGM				= 1 + ID_CHOOSELIB;
	public static final int ID_IGM_BACK			= 1 + ID_IGM;
	public static final int ID_IGM_BACK_CONFIRM	= 1 + ID_IGM_BACK;
	
	/** 菜单坐标相关常数 */	
	public static final int MENU_XOFF_FROM_CTR 	= DevConfig.SCR_W>>2;
	public static final int MENU_Y 				= DevConfig.SCR_H >> 1;
	
	/** 高分榜提交结果,HighScoreCheck()返回 */
	public static final int SCORE_TOP_1			= 1;
	public static final int SCORE_TOP_2			= 1 + SCORE_TOP_1;
	public static final int SCORE_TOP_3			= 1 + SCORE_TOP_2;
	public static final int SCORE_NOTONLIST		= -1;
	
	
	/** 游戏模式名称，注意其ID用于存档 */
	public static final int GM_NORMAL			= 0;
	public static final int GM_TIMER			= 1 + GM_NORMAL;
	public static final int GM_RANDOM			= 1 + GM_TIMER;
	public static final int GM_COUNT			= 1 + GM_RANDOM;
	public static final int[] MODENAME	= {
		T_G.GM_NORMAL,
		T_G.GM_TIMER,
		T_G.GM_RANDOM,
	};
	/** 全局背景常量，在zMainMenu.java中会使用这里的索引，作为菜单的背景的ID */
	public static String[][]  BGNames = {
		{"/bgmain.bmp","/bgmain.png"},
		{"/bg1.bmp","/bg1.png"},
		{"/bg2.bmp","/bg2.png"},
		{"/bg3.bmp","/bg3.png"},
		{"/bg4.bmp","/bg4.png"},
		{"/bg5.bmp","/bg5.png"},
		
		//#if GAME_NAME=="EDU1" || GAME_NAME=="EDU4" || GAME_NAME=="EduShuXue" || GAME_NAME=="EDU7" || GAME_NAME=="Edu5" || GAME_NAME=="EduDriver1" || GAME_NAME=="J_2011_12_Edu1_HappyNature" || GAME_NAME=="J_2011_12_Edu1_ChuYi" || GAME_NAME=="J_2011_12_Edu1_HappyLife"
		{"/splash.bmp","/splash_bg.png","/splash_bird.png","/splash_boom.png","/splash_title.png","/splash_press.png"},
		//#elif GAME_NAME=="EDU2" || GAME_NAME=="EDU3" || GAME_NAME=="Edu6"  || GAME_NAME=="J_2011_11_Edu1_EduEng" || GAME_NAME=="J_2011_11_Edu1_EduShuXue1" || GAME_NAME=="J_2011_11_Edu1_EduYuWen1" || GAME_NAME=="J_2011_11_Edu2_HappyYuWen" || GAME_NAME=="J_2011_11_Edu3_CrazyShuXue"
		{"/splash.bmp","/splash_bg.png","/splash_title.png","/splash_press.png"},
		//#elif GAME_NAME=="EduYuWen"
		{"/splash.bmp","/splash_bg.png","/splash_press.png"},
		//#else
		{"/splash.bmp","/splash_bg.png","/splash_bird.png","/splash_boom.png","/splash_title.png","/splash_press.png"},
		//#endif
		
//		{"/splash.bmp","/splash_bg.png","/splash_bird.png","/splash_boom.png","/splash_title.png","/splash_press.png"},
	};
	
	/** 全局资源常量，在zMainMenu.java中会使用这里的索引，作为菜单项的背景的ID */
	public static String[][] MINames = {
		{"/mi_start.bmp","/menu.png"},			
		{"/mi_highscore.bmp","/menu.png"},
		{"/mi_more.bmp","/menu.png"},
		{"/mi_about.bmp","/menu.png"},
		{"/mi_help.bmp","/menu.png"},
		{"/mi_exit.bmp","/menu.png"},						//	5
		{"/highscore.bmp","/menu.png"},						//6
		{"/mi_mode_normal.bmp","/menu_lib.png"},			
		{"/mi_mode_timer.bmp","/menu_lib.png"},
		{"/mi_mode_random.bmp","/menu_lib.png"},
		{"/answercorrect.bmp","/answercorrect.png"},   		//10
		{"/answerwrong.bmp","/answerwrong.png"},			//11
		{"/mi_soundon.bmp","/menu.png"},					//12
		{"/mi_soundoff.bmp","/menu.png"},					//13
		{"/mi_summary.bmp","/mi_summary.png"},				//14
		{"/handup.bmp","/handup.png"},						//15
		{"/blackboard.bmp","/blackboard.png"},				//16
		{"/arrow.bmp","/arrow.png"},						//17
		{"/levelcry.bmp","/levelcry.png"},					//18
		{"/levelsmile.bmp","/levelsmile.png"},				//19
		{"/mi_catalog.bmp","/menu.png"},					//20		已使用，注意在周一合并的时候，处理ID变更，涉及MainMenu
		{"/time.bmp","/time.png"},							//21		
		{"/mi_helpline.bmp","/sk.png"},						//22
		{"/grade.bmp","/grade.png"},						//23
		{"/teacherold.bmp","/teacherold.png"},				//24
		{"/teacherman.bmp","/teacherman.png"},				//25
		{"/teacherwoman.bmp","/teacherwoman.png"},			//26
		{"/mi_backtomain.bmp","/menu.png"},					//27
		{"/mi_backtomain_confirm.bmp","/menu.png"},			//28
		{"/mi_exit_confirm.bmp","/menu.png"},				//29
		{"/mi_zero.bmp","/menu.png"},						//30
		{"/mi_intro.bmp","/menu.png"}						//31
		
		
			
	};
	public  static final int[] TextsHelp = {
//#if SCREENWIDTH>176
			
			
			
			//#if CHANNEL_NAME == "BBX"
			
			T_G.HELP_1,
			T_G.HELP_2,
			T_G.HELP_3,

			//#if ENABLE_TOUCH=="TRUE"
				//#if MODEL== "E6" || MODEL== "E680" 
			T_G.HELP_CONTROL_SEMI_TOUCH,
		
				//#else
			T_G.HELP_CONTROL_KEYBOARD
				//#endif	
				
			//#elif MODEL=="N97"
			T_G.HELP_CONTROL_TOUCH,
				//#else
			T_G.HELP_CONTROL_KEYBOARD,
			
			//#endif

			//#if ABOUTUS=="TRUE"
			T_G.HELP_ABOUTUS_1,
			T_G.HELP_ABOUTUS_2,
			T_G.HELP_ABOUTUS_3,
			//#endif
			
			//#elif CHANNEL_NAME == "MM"
			T_G.HELP_1,
			T_G.HELP_2,
			T_G.HELP_3,
				//#if ENABLE_TOUCH=="TRUE"
				//#if MODEL== "E6" || MODEL== "E680" 
				T_G.HELP_CONTROL_SEMI_TOUCH,		
				//#else
				T_G.HELP_CONTROL_KEYBOARD
				//#endif					
				//#elif MODEL=="N97"
				T_G.HELP_CONTROL_TOUCH,
				//#else
				T_G.HELP_CONTROL_KEYBOARD,
				//#endif
			T_G.MM_HELP_ABOUTUS_1,
			T_G.MM_HELP_ABOUTUS_2,
			T_G.MM_HELP_ABOUTUS_3,
			T_G.MM_HELP_ABOUTUS_4,
			//#endif
			
			
			
			
//#else
			T_G.HELP_1_128,
			T_G.HELP_2_128,
			T_G.HELP_3_128,
			T_G.HELP_CONTROL_KEYBOARD_128,
			T_G.HELP_CONTROL_KEYBOARD_1_128,
			T_G.HELP_ABOUTUS_1_128,
			T_G.HELP_ABOUTUS_2_128,
			T_G.HELP_ABOUTUS_3_128,
			T_G.HELP_ABOUTUS_4_128,
//#endif
	};
}
