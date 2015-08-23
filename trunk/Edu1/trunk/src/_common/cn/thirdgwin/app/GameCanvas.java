package cn.thirdgwin.app;  
import java.io.InputStream;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

import cn.thirdgwin.io.zByteArrayStream;
import cn.thirdgwin.io.zRMS;
import cn.thirdgwin.lib.DevConfig;
import cn.thirdgwin.lib.GLLib;
import cn.thirdgwin.lib.Utils;
import cn.thirdgwin.lib.cCP;
import cn.thirdgwin.lib.cMath;
import cn.thirdgwin.lib.cTouch;
import cn.thirdgwin.lib.cWeaknetwork;
import cn.thirdgwin.lib.zAnimPlayer;
import cn.thirdgwin.lib.zFont;
import cn.thirdgwin.lib.zServiceAnim;
import cn.thirdgwin.lib.zServiceSprite;
import cn.thirdgwin.lib.zServiceText;
import cn.thirdgwin.lib.zSoundPlayer;
import cn.thirdgwin.lib.zSprite;

//import com.nokia.mid.ui.FullCanvas;

public class GameCanvas extends GLLib implements CommandListener {
	/** 游戏全局变量 */
	public static GameMIDlet midlet;
	public static boolean s_SoundOn;
	public static boolean s_Paused;
	public static zFont MainFont;
	/** Arrow.sprite中的所有动画 */
	public static zAnimPlayer[] Arrow		= new zAnimPlayer[ARROW.NUM_ANIMS];
	
	/** 题库 ID */
	public static int LibID;	
	public static int GameMode;		// 参见Const.java中GM_NORMAL系列常量
	
	
	
	//#if GAME_NAME=="EDU1" || GAME_NAME=="EDU2" || GAME_NAME=="EDU3" || GAME_NAME=="EDU4" || GAME_NAME=="Edu5" || GAME_NAME=="Edu6" || GAME_NAME=="EDU7" || GAME_NAME=="EduShuXue" || GAME_NAME=="EduYuWen"
	public String[] SubStr = new String[]{"学校教育类","本游戏适合","6-12岁适用","人群软件内容","为课本知识","巩固和拓展包括","但不限于语数外"};
	//#elif  GAME_NAME=="EduDriver" || GAME_NAME=="EduDriver1"
	public String[] SubStr = new String[]{"社会教育类","本游戏适合","6-99岁适用人群","软件内容为","课外知识","巩固和拓展","内容"};
	//#else
	public String[] SubStr = new String[]{"学校教育类","本游戏适合","6-12岁适用","人群软件内容","为课本知识","巩固和拓展包括","但不限于语数外"};
	//#endif
	
	
	/******************************** 背景管理 ********************************/	
	public static zAnimPlayer BG;
	public static int BG_IDX = -1;
	public void hideNotify() {
		super.hideNotify();
		if(m_soundPlayer != null && s_SoundOn)
//		m_soundPlayer.Pause();
			m_soundPlayer.Stop();
	}
//	public void Resume(){
//		
////		Utils.Dbg("唤醒");
//	}
	public static zAnimPlayer GetBGAnim(int idx)
	{
		
		if (idx==BG_IDX && BG!=null)
			return BG;
		if (idx>=0)
			{
			if (BG!=null)
			{
				zServiceSprite.Put(BG.GetSprite(), true,true);
			}
			
			String[] imgs = new String[ Const.BGNames[idx].length-1];
			System.arraycopy(Const.BGNames[idx], 1, imgs, 0, imgs.length);
			
			BG = zServiceAnim.AnimCreate(Const.BGNames[idx][0],imgs);
			if (BG!=null)
				BG.SetAnim(0,-1);
			}
		else
			{
			BG = null;
			}
		BG_IDX = idx;
		return BG;
	}
	/******************************** 左右软键 ********************************/	
	
	public static zAnimPlayer[] AnimSK = new zAnimPlayer[2];
	public static final int COLOR_SK = 0x111111;
	public static int SK_Y;
	public static boolean[] SKState = new boolean[2];
	public static short[][] SKPos;
//#if SOFTKEY_OK_ON_LEFT=="TRUE"	
	public static String[] SKLabel = new String[]{"确定","返回"};
//#else
	public static String[] SKLabel = new String[]{"返回","确定"};
//#endif	
	public static void SetSKLabel(String lsk,String rsk)
	{
//#if SOFTKEY_OK_ON_LEFT=="TRUE"	
		SKLabel = new String[] {lsk,rsk};
//#else
		SKLabel = new String[] {rsk,lsk};
//#endif		
	}
	public static void SetSK(boolean lsk,boolean rsk) {
//#if SOFTKEY_OK_ON_LEFT=="TRUE"	
		SKState = new boolean[]{lsk,rsk};
//#else
		SKState = new boolean[]{rsk,lsk};
//#endif		
	};

	public static void InitSoftkey()
	{
		
	}
	public static void DrawSoftkey(Graphics g)
	{
//#if SOFTKEY_OK_ON_LEFT=="TRUE"
		int[] _indexs = new int[] {0,1};
//#else		
		int[] _indexs = new int[] {1,0};
//#endif
		/** 坐标值 */
		if (SKPos==null)
		{
			AnimSK[0] = zServiceAnim.AnimCreate("/skl.bmp", new String[] {"/sk.png"});
			AnimSK[1] = zServiceAnim.AnimCreate("/skr.bmp", new String[] {"/sk.png"});
			SKPos = new short[2][2];
			int[] RR = AnimSK[0].GetFrameRect(SKL.ANIM_NORMAL,0);
			/** X 坐标 */
			SKPos[0][0] = (short) ((RR[2]-RR[0])>>1);
			SKPos[1][0] = (short) (DevConfig.SCR_W-((RR[2]-RR[0])>>1));
			/** Y 坐标 */
			int fontHeight = g.getFont().getHeight();
			SKPos[0][1] = SKPos[1][1] = (short) (DevConfig.SCR_H - ((RR[3]-RR[0])>>1));
		}
		int _tempidx;
		for(int i = 0;i<_indexs.length;i++)
		{
			_tempidx = _indexs[i];
			if (SKState[_tempidx])
			{
			AnimSK[_tempidx].SetPos(SKPos[i][0], SKPos[i][1]);
			AnimSK[_tempidx].SetAnim(SKL.ANIM_NORMAL, -1);
			AnimSK[_tempidx].Render(g);
//#if SCREENWIDTH<176			
//左右软件貌似暂时没用			
//			g.setColor(COLOR_SK);
//			MainFont.DrawString(g,SKLabel[_tempidx], SKPos[i][0], SKPos[i][1] , Graphics.HCENTER|Graphics.BOTTOM);
//#endif			
			};
			
		}
	}
	
	/******************************** 状态机管理 ********************************/	
	
	public static int	s_GS_Donot_Direct_Use_This;
	public static final int getStateSub()
	{
		return s_GS_Donot_Direct_Use_This>>16;
	}
	public static final int getState()
	{
		return s_GS_Donot_Direct_Use_This & 0xFFFF;
	}
	public static final void setStateSub(int gs_sub)
	{
		s_GS_Donot_Direct_Use_This = (s_GS_Donot_Direct_Use_This & 0xFFFF) | (gs_sub<<16);
	}
	public final static int SetState(int gs)
	{	
		int Old = getState();
		switch (Old) {
			case Const.GS_MAINMENU:
				{
				MainMenu_Leave();
				break;
				}
			case Const.GS_PLAY:
				Play_Leave();
				break;
			case Const.GS_SUMMARY:
				Summary_Leave();
				break;
			case Const.GS_HIGHSCORE:
				HighScore_Leave();
				break;
			case Const.GS_LOADING:
				Loading_Leave();
				break;
		}
		switch (gs) {
			case Const.GS_MAINMENU: {
				MainMenu_Enter();
				break;
			}
			case Const.GS_PLAY:
				Play_Enter();
				break;
			case Const.GS_SUMMARY:
				Summary_Enter();
				break;
			case Const.GS_HIGHSCORE:
				HighScore_Enter();
				break;
			case Const.GS_LOADING:
				Loading_Enter();
				break;
			default: {
				break;
			}
		}
		s_GS_Donot_Direct_Use_This = gs;
		return Old;
	}
	public GameCanvas(Object application, Object display) {
		super(application,display);
		Libs.LoadCatalog();
		zSprite spr;
		spr = zServiceSprite.Get("/arrow.bmp", new String[]{"/arrow.png"},false);

		//装入Arrow
		for(int i = 0;i<ARROW.NUM_ANIMS;i++)
		{
			GameCanvas.Arrow[i] = zServiceAnim.AnimCreate(spr);
			GameCanvas.Arrow[i].SetAnim(i, -1);
		}

		MainFont = new zFont(null, null);
		
//		MainFont.SetLineHeight(Font.getDefaultFont().getHeight());
//		Utils.Dbg("SetLineHeight:"+Font.getDefaultFont().getHeight());
		zServiceText.Init();
//#if SCREENWIDTH<176
		MainFont.InitSystemFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN,
				Font.SIZE_LARGE);
//#endif
		
	}

	public void commandAction(Command c, Displayable d) {
		// TODO Auto-generated method stub
		
	}
	public void OnPaint() throws Exception {
		switch (getState()) {
			case Const.GS_SOUNDONOFF: {	
				if (cCP.SoundOnOff_paint(g))
					SetState(Const.GS_SP);			
				break;
			}
			case Const.GS_SP: 
			{
				if(cCP.Tiger_Paint(g)){
//#if ENABLE_TOUCH=="TRUE"		
					GameMIDlet.InitTouch_LRSK();
//#endif
					SetState(Const.GS_NETWORKING);
				}
													
				break;
			}
			case Const.GS_SUP:
//				if(!Suggestion.isFinish){
//					Suggestion.paint(g);
//				}else{
//					SetState(Const.GS_INIT);
//				}	
				if(cCP.SuggestionPaint(g,SubStr)){
					SetState(Const.GS_INIT);
				}
				break;
				//开始联网确认
			case Const.GS_NETWORKING:
				if(cWeaknetwork.NetOnOff_paint(g)){	
					SetState(Const.GS_SUP);
				}
				
				break;
			
			//圈圈登录如果未联网，联网确认
			case Const.GS_ZEROLOGIN:
				GameCanvas.SetSK(false,false);
				if(cWeaknetwork.NetOnOff_paint(g)){	
				}
				
				break;
			case Const.GS_CP: {
//#if ENABLE_TOUCH=="TRUE"		
					GameMIDlet.InitTouch_LRSK();
//#endif		
					SetState(Const.GS_INIT);

				break;
			}
			case Const.GS_INIT:{
				SetState(Const.GS_MAINMENU);
				zServiceText.LoadPack(0,zServiceText.ID_SET_GLOBAL);
				zServiceText.BuildCacheGlobal();
				break;
			}
			case Const.GS_MAINMENU:{
				MainMenu_Paint(g);
				break;
			}
			case Const.GS_PLAY:
				Play_Paint(g);
				break;
			case Const.GS_SUMMARY:
				Summary_Paint(g);
				break;
			case Const.GS_UPSCORE:
				if(cWeaknetwork.uploadScore(g,zPlay.GetScore())){
					SetState(Const.GS_MAINMENU);
				}
				break;
			case Const.GS_HIGHSCORE:
				HighScore_Paint(g);
				break;
			case Const.GS_LOADING:
				Loading_Paint(g);
				break;
			case Const.GS_EXITMOREGAME:
//#if MOREGAME=="TRUE" && MODEL!="D608" && MODEL!="D508" && EDUCATION!="TRUE"
				cCP.MoreGame_Paint(g);
//#else
				GameMIDlet.s_this.destroyApp(true);
//#endif				
				break;
			default: 
				break;
			

			}
		DrawSoftkey(g);
	}

	public void OnPaintPause() throws Exception {
	}

	public void OnTick() throws Exception {
	}
		
	
	public void OnResume() {
//		if(m_soundPlayer != null && s_SoundOn)
//			m_soundPlayer.Resume();
			switch (GameCanvas.getState())
			{
				case Const.GS_MAINMENU:
					if(m_soundPlayer != null && s_SoundOn)
//						m_soundPlayer.Resume();
						m_soundPlayer.Play(-1);
						Resume();
					break;
				case Const.GS_PLAY:
					if (GameCanvas.IGM.m_visible==false)
						{
						GameCanvas.IGM.m_visible = true;
						GameCanvas.setStateSub(Const.PLAY_S_IGM);
						Resume();
						};
					break;
			}
			
			
	}
/**************************************************************************\
 * MainMenu 主菜单
\**************************************************************************/
	public static zMainMenu mainMenu;
	public static zSoundPlayer m_soundPlayer;
	private static void MainMenu_Enter() {
		if (mainMenu==null)
		{
			mainMenu = new zMainMenu();
		}
		mainMenu.Init();
		
		if(m_soundPlayer == null)
		{
			m_soundPlayer = new zSoundPlayer();
			m_soundPlayer.Load("/title.mid",zSoundPlayer.TYPE_MIDI);
		}
		if(s_SoundOn)
		{
			m_soundPlayer.Play();
		}
		
	}	
	public static void MainMenu_Leave()
	{
		if(mainMenu!=null)
			mainMenu.Finalize();

		m_soundPlayer.Unload();
		m_soundPlayer = null;
	}	
	public static boolean MainMenu_Paint(Graphics g)
	{
			if (mainMenu!=null)
			{
				mainMenu.Draw(g);
			}			
		return false;
	}

	public static int MainMenu_Update() {
		return 0;
	}

/**************************************************************************\
 * GS_PLAY
\**************************************************************************/
	public static zPlay Play;
	public static zIGM IGM;
	private static void Play_Enter() {
		// TODO Auto-generated method stub
		if (Play==null)
			Play = new zPlay();
		if (IGM==null)
			IGM = new zIGM();
		IGM.Init();
		Play.Load(LibID, GameMode);
	}	
	public static void Play_Leave()
	{
		IGM.Finalize();
		Play.Unload();
	}	
	public static boolean Play_Paint(Graphics g)
	{
		
		if (Play!=null)
			Play.Draw(g);
		return false;
	}
	public static int Play_Update()
	{
		Play.Update();
		return 0;
	}	

/**************************************************************************\
 * GS_SUMMARY
\**************************************************************************/
	private static zAnimPlayer[] summaryBoardPlayer;
	private static zAnimPlayer summarySlidePlayer;
	private static zAnimPlayer summaryFacePlayer;
	private static zAnimPlayer summaryGradePlayer;
	private static zAnimPlayer summaryGradeEffectPlayer;
	private static final int SUMMARY_BOARD_NUM = 5;
//#if SCREENWIDTH>176
	private static final int SUMMARY_LINE_HEIGHT = 53;
//#else
	private static final int SUMMARY_LINE_HEIGHT = 36;
//#endif
	private static String summaryTip;
	
	private static void Summary_Enter() 
	{
		summarySlidePlayer = zServiceAnim.AnimCreate(Const.MINames[14][0], Const.MINames[14][1]);
		summarySlidePlayer.SetAnim(MI_SUMMARY.ANIM_SLIDE, 1);
		summarySlidePlayer.SetPos(DevConfig.SCR_W / 4, 0);
		
		summaryBoardPlayer = new zAnimPlayer[SUMMARY_BOARD_NUM];
		
		if(Play.isWin())
		{
			summaryFacePlayer = zServiceAnim.AnimCreate(Const.MINames[19][0], Const.MINames[19][1]);
			summaryFacePlayer.SetAnim(levelsmile.ANIM_SMILE, -1);
		} else {
			summaryFacePlayer = zServiceAnim.AnimCreate(Const.MINames[18][0], Const.MINames[18][1]);
			summaryFacePlayer.SetAnim(levelcry.ANIM_CRY, -1);
		}
		summaryFacePlayer.SetPos(DevConfig.SCR_W * 3 / 4, DevConfig.SCR_H * 3 / 4);
		
		
		for(int i = 0;i<SUMMARY_BOARD_NUM;i++)
		{
			summaryBoardPlayer[i] = zServiceAnim.AnimCreate(Const.MINames[14][0], Const.MINames[14][1]);
			summaryBoardPlayer[i].SetAnim(MI_SUMMARY.ANIM_BOARD_RODE, -1);
			summaryBoardPlayer[i].SetPos(DevConfig.SCR_W/4, 0 + i * SUMMARY_LINE_HEIGHT);
			for(int n = 0;n<i;n++)
			{
				summaryBoardPlayer[i].Update();
			}
		}
		
		summaryGradePlayer = zServiceAnim.AnimCreate(Const.MINames[23][0], Const.MINames[23][1]);
		int gradeAnim = Summary_GetGradeAnimID();
		summaryGradePlayer.SetAnim(gradeAnim , -1);
		summaryGradePlayer.SetPos(DevConfig.SCR_W * 3 / 4, DevConfig.SCR_H * 4 / 10);
		//#if SCREENWIDTH>176
		if(gradeAnim <= SPR_GRADE.ANIM_A_MINUS)
		{
			summaryGradeEffectPlayer = zServiceAnim.AnimCreate(Const.MINames[23][0], Const.MINames[23][1]);
			summaryGradeEffectPlayer.SetAnim(SPR_GRADE.ANIM_EXPLODE, -1);
			summaryGradeEffectPlayer.SetPos(DevConfig.SCR_W * 3 / 4, DevConfig.SCR_H * 4 / 10);
		} 
		//#endif
		
//		else if(gradeAnim >= SPR_GRADE.ANIM_C_PLUS)
//		{
//			summaryGradeEffectPlayer = zServiceAnim.AnimCreate(Const.MINames[23][0], Const.MINames[23][1]);
//			summaryGradeEffectPlayer.SetAnim(SPR_GRADE.ANIM_RAIN, -1);
//			summaryGradeEffectPlayer.SetPos(DevConfig.SCR_W * 3 / 4, DevConfig.SCR_H * 4 / 10);
//		}
		
		GameCanvas.SetSK(true,false);
	}
	private static int Summary_GetGradeAnimID()
	{

		int grade = Play.GetCorrectRate();
		if(grade >= 95)
		{
			summaryTip = zServiceText.GetText(T_G.LEVEL_A_PLUS);
			return SPR_GRADE.ANIM_A_PLUS;
		} else if(grade > 85)
		{
			summaryTip = zServiceText.GetText(T_G.LEVEL_A);
			return SPR_GRADE.ANIM_A;
		} else if(grade > 80)
		{
			summaryTip = zServiceText.GetText(T_G.LEVEL_A_MINUS);
			return SPR_GRADE.ANIM_A_MINUS;
		} else if(grade > 75)
		{
			summaryTip = zServiceText.GetText(T_G.LEVEL_B_PLUS);
			return SPR_GRADE.ANIM_B_PLUS;
		}
		else if(grade > 70)
		{
			summaryTip = zServiceText.GetText(T_G.LEVEL_B);
			return SPR_GRADE.ANIM_B;
		}
		else if(grade > 65)
		{
			summaryTip = zServiceText.GetText(T_G.LEVEL_B_MINUS);
			return SPR_GRADE.ANIM_B_MINUS;
		}
		else if(grade > 60)
		{
			summaryTip = zServiceText.GetText(T_G.LEVEL_C_PLUS);
			return SPR_GRADE.ANIM_C_PLUS;
		}
		else if(grade > 50)
		{
			summaryTip = zServiceText.GetText(T_G.LEVEL_C);
			return SPR_GRADE.ANIM_C;
		}
		
		summaryTip = zServiceText.GetText(T_G.LEVEL_C_MINUS);
		return SPR_GRADE.ANIM_C_MINUS;
	}
	private static void Summary_Leave() 
	{
		zServiceAnim.AnimDestroy(summarySlidePlayer,true,true);
		zServiceAnim.AnimDestroy(summaryFacePlayer,true,true);
		summarySlidePlayer = null;
		summaryFacePlayer = null;
		for(int i = 0;i<SUMMARY_BOARD_NUM;i++)
		{
			zServiceAnim.AnimDestroy(summaryBoardPlayer[i]);
			summaryBoardPlayer[i] = null;
		}	
		summaryBoardPlayer = null;
		
		zServiceAnim.AnimDestroy(summaryGradePlayer,true,true);
		summaryGradePlayer = null;
		zServiceAnim.AnimDestroy(summaryGradeEffectPlayer,true,true);
		summaryGradeEffectPlayer = null;
		
		LastScore = Play.GetScore();
	}
	public static boolean Summary_Paint(Graphics g)
	{
		//#if SCREENWIDTH>176
		Play.DrawBG(g);
		//#else
		g.setColor(0x000000);
		g.fillRect(0, 0, DevConfig.SCR_W, DevConfig.SCR_H);
		//#endif
		
		if(!summarySlidePlayer.IsAnimOver())
		{
			summarySlidePlayer.Update();
			summarySlidePlayer.Render(g);
		} else {
			for(int i = 0;i<SUMMARY_BOARD_NUM;i++)
			{
				summaryBoardPlayer[i].Update();
				summaryBoardPlayer[i].Render(g);
				Summary_PaintString(i,g);
			}
			
			summaryFacePlayer.Update();
			summaryFacePlayer.Render(g);

			//#if SCREENWIDTH>176
			if (summaryGradeEffectPlayer != null && summaryGradeEffectPlayer.GetAnim() == SPR_GRADE.ANIM_EXPLODE)
			{
				summaryGradeEffectPlayer.Update();
				summaryGradeEffectPlayer.Render(g);
			}
			//#endif
			
			summaryGradePlayer.Update();
			summaryGradePlayer.Render(g);
			
//			if (summaryGradeEffectPlayer != null && summaryGradeEffectPlayer.GetAnim() == SPR_GRADE.ANIM_RAIN)
//			{
//				summaryGradeEffectPlayer.Update();
//				summaryGradeEffectPlayer.Render(g);
//			}
		}
		
		
		return false;
	}
	private static void Summary_PaintString(int n, Graphics g)
	{
		int y = SUMMARY_LINE_HEIGHT/2 + n * SUMMARY_LINE_HEIGHT;
		int[] rect = summaryBoardPlayer[n].GetCurFrameRect();
		int x = DevConfig.SCR_W/4 + rect[0] + (rect[2] - rect[0])/2;
		switch(n)
		{
			case 0:
			{
				MainFont.SetSystemFontColor(0x0000FF);
				MainFont.DrawString(g,"成绩", x, y, Graphics.HCENTER | Graphics.TOP);
				break;
			}
			case 1:
			{
				MainFont.SetSystemFontColor(0);
				MainFont.DrawString(g,"用时:"+Play.GetTime(), x, y, Graphics.HCENTER | Graphics.TOP);
				break;
			}
			case 2:
			{
				MainFont.SetSystemFontColor(0);
				MainFont.DrawString(g,"正确率:" + Play.GetCorrectRate() + "%", x, y, Graphics.HCENTER | Graphics.TOP);
				break;
			}
			case 3:
			{
				MainFont.SetSystemFontColor(0);
				MainFont.DrawString(g,"分数:"+Play.GetScore(), x, y, Graphics.HCENTER | Graphics.TOP);
				break;
			}
			case 4:
			{
				if(summaryTip != null)
				{
					MainFont.SetSystemFontColor(0x0000FF);
					MainFont.DrawString(g,summaryTip, x, y, Graphics.HCENTER | Graphics.TOP);
				}
				break;
			}
		}
	}
	
	public static void Summary_Keypressed(int keyCode)
	{
		if(keyCode == DevConfig.KEY_LSK)
		{
////#if REVIEW=="FALSE"
			SetState(Const.GS_UPSCORE);
////#else
//			SetState(Const.GS_HIGHSCORE);
////#endif
		}
	}
	public static int Summary_Update()
	{
		return 0;
	}	
/**************************************************************************\
 * GS_HIGHSCORE
\**************************************************************************/
	/** 字段定义: */
	public final static int TOP_COUNT = 4; 
	public static int[][] HighScores;
//	= {
//				{30,20,10},
//				{300,200,100},
//				{3000,2000,1000},
//		};
	public static String[][] HighScoresDate;
//	= {
//		{"30","20","10"},
//		{"300","200","100"},
//		{"3000","2000","1000"},
//	};
	public static int LastScore	= 0;
//	public static zAnimPlayer AnimHighScoreTitle;
	public static int HighScoreTitle_Y = -1;
	public static int HighScore_Paint_Count ;
	public static final int HighScore_MoreLine_By_Frame = 12 ;
	public static int[] ListRect = new int[4];
	public static final int HighScore_Margin_X = 5;
	public static int HighScore_ListMode = Const.GM_NORMAL;
	public static int HighScore_SelfIndex = -1;
	public static int HighScore_SelfMode = 0;
	
	public static void HighScore_Load()
	{
		if (HighScores == null) 
		{
			boolean _Loaded = false;
			try {
				InputStream is = zRMS.GetRmsInputStream(Const.RMS_NAME);
				if (is != null) {
					HighScores = new int[Const.GM_COUNT][TOP_COUNT];
					HighScoresDate = new String[Const.GM_COUNT][TOP_COUNT];			
					zByteArrayStream bas = new zByteArrayStream(is);
					is.close();
					for (int i = 0; i < Const.GM_COUNT; i++) {
						for (int j = 0; j < TOP_COUNT; j++) {
							HighScores[i][j] = bas.GetInt();
							HighScoresDate[i][j] = bas.GetStr();
						}
					}
					_Loaded = true;

				}
			} catch (Exception e) {
	
			}
			if (!_Loaded)
			{
				HighScores = new int[Const.GM_COUNT][TOP_COUNT];
				HighScoresDate = new String[Const.GM_COUNT][TOP_COUNT];		
			}
		}	
	}
	public static void HighScore_Save()
	{
		if (HighScores != null) {
			try {
				String text;
				int len;
				byte[] buffer = new byte[128];
				zByteArrayStream bas = new zByteArrayStream(buffer);
				{
					for (int i = 0; i < Const.GM_COUNT; i++) {
						for (int j = 0; j < TOP_COUNT; j++) {
							bas.SetInt(HighScores[i][j]);
							text = HighScoresDate[i][j];
							if (text==null)text="";
							len = text.length();
							if (len>Const.RMS_PNAME_LEN) len = Const.RMS_PNAME_LEN;
							bas.SetStr(text.substring(0, len));
						}
					}

				}
			zRMS.Rms_Write(Const.RMS_NAME, buffer);
			} catch (Exception e) {

			}
		}	
	}	
	public static int HighScoreCheck(int mode,int Score)
	{
		boolean Inserted = false;
		int lenToCopy = 0;
		int[] scorelist = HighScores[mode];
		int i;
		HighScore_SelfIndex = -1;
		HighScore_SelfMode = -1;		
		HighScore_ListMode = mode;
		for(i =0;i<scorelist.length;i++)
		{
			if (Score>scorelist[i])
			{
				lenToCopy = scorelist.length - (i+1);
				if (lenToCopy>0)
					System.arraycopy(scorelist,i,scorelist,i+1,lenToCopy);
				scorelist[i] = Score;
				HighScore_Save();
				Inserted = true;
				HighScore_SelfIndex = i;
				HighScore_SelfMode = mode;
				break;
			}
		}
		if (Inserted)
			
			return i;
		else
			return -1;
	}

	public static void HighScore_Enter() {
		HighScore_SelfIndex = -1;
		HighScore_Load();
		if(LastScore > 0)
		{
			HighScoreCheck(GameMode, LastScore);
		}
//		AnimHighScoreTitle = Utils.AnimCreate(Const.MINames[6][0], Const.MINames[6][1]);
//		AnimHighScoreTitle.SetAnim(HIGHSCORE.ANIM_NORMAL, -1);
		HighScore_Paint_Count = 0;
		GameCanvas.SetSK(true,false);
//		GameCanvas.SetSKLabel("返回", "返回");
	}
	private static void HighScore_Leave() 
	{
		LastScore = -1;
//		GameCanvas.SetSK(true,false);
	}
	public static final int RECT_OFFSET_X = 10;
	public static boolean HighScore_Paint(Graphics g)
	{
		{
//#if SCREENWIDTH>176
			zAnimPlayer bg = GameCanvas.GetBGAnim(5);
			if(bg!=null)
			{
				bg.Update();
				bg.SetPos(DevConfig.SCR_W>>1, DevConfig.SCR_H>>1);
				bg.Render(g);
			}
//#else
			g.setColor(0xFFFFFF);
			g.fillRect(0, 0, DevConfig.SCR_W, DevConfig.SCR_H);
//#endif
		}
		GameCanvas.Arrow[ARROW.ANIM_LEFT].SetPos(RECT_OFFSET_X, DevConfig.SCR_H>>1);	
		GameCanvas.Arrow[ARROW.ANIM_LEFT].Update();
		GameCanvas.Arrow[ARROW.ANIM_LEFT].Render(g);
		GameCanvas.Arrow[ARROW.ANIM_RIGHT].SetPos(DevConfig.SCR_W - RECT_OFFSET_X, DevConfig.SCR_H>>1);	
		GameCanvas.Arrow[ARROW.ANIM_RIGHT].Update();
		GameCanvas.Arrow[ARROW.ANIM_RIGHT].Render(g);

//		{
//			AnimHighScoreTitle.Update();
//			if (HighScoreTitle_Y==-1)
//			{
//				int []r = AnimHighScoreTitle.GetFrameRect(HIGHSCORE.ANIM_NORMAL,0);
//				HighScoreTitle_Y = r[3];
//				ListRect[0] = HighScore_Margin_X;
//				ListRect[1] = HighScoreTitle_Y + HighScore_Margin_X + ( g.getFont().getHeight()<<1);
//				ListRect[2] = DevConfig.SCR_W - (HighScore_Margin_X<<1);
//				ListRect[3] = DevConfig.SCR_H - (HighScore_Margin_X<<1) - ( g.getFont().getHeight()<<1) - Const.SK_H ;
//			}
//			AnimHighScoreTitle.SetPos(DevConfig.SCR_W>>1, HighScoreTitle_Y);
//			AnimHighScoreTitle.Render(g);
//		};
		{
			int _curY = DevConfig.SCR_H*2/5;//ListRect[1];
			int lineHeight = g.getFont().getHeight();
			int _score;
			int maxline,real_maxline;
			MainFont.SetSystemFontColor(0x0000FF);
			MainFont.DrawString(g, zServiceText.GetText(Const.MODENAME[HighScore_ListMode]),DevConfig.SCR_W/2 /*ListRect[0]+(ListRect[2]>>1)*/, _curY, Graphics.HCENTER|Graphics.BOTTOM);
			_curY +=lineHeight;
			maxline = real_maxline = HighScore_Paint_Count / HighScore_MoreLine_By_Frame;
			if (real_maxline>TOP_COUNT)
				maxline = TOP_COUNT;
//			_curY +=lineHeight;
			for (int i = 0;i<maxline;i++)
			{
				g.setColor(0);
				if(HighScoresDate[HighScore_ListMode][i]==null)
				{
					HighScoresDate[HighScore_ListMode][i] ="";
				}
				MainFont.DrawString(g,HighScoresDate[HighScore_ListMode][i], ListRect[0], _curY, Graphics.LEFT|Graphics.BOTTOM);
				if(i == HighScore_SelfIndex && HighScore_SelfMode == HighScore_ListMode)
				{
					MainFont.SetSystemFontColor(0xFF0000);
				}
				else
				{
					MainFont.SetSystemFontColor(0);
				}
				_score = HighScores[HighScore_ListMode][i];
				if (i == real_maxline - 1)
					_score = cMath.Math_Rand(0, 10000);
				MainFont.DrawString(g,String.valueOf(_score), DevConfig.SCR_W/2/*ListRect[0]+(ListRect[2]>>1)*/, _curY, Graphics.HCENTER|Graphics.BOTTOM);
				_curY +=lineHeight;
			}
		}
		HighScore_Paint_Count++;
		return false;
	}
	public static boolean HighScore_keyPressed(int keycode)
	{
		switch (keycode)
		{
		case DevConfig.KEY_LEFT:
		case DevConfig.KEY_NUM4:
			HighScore_Paint_Count = 0;
			HighScore_ListMode--;
			if (HighScore_ListMode<0)HighScore_ListMode = Const.GM_RANDOM;
			break;
		case DevConfig.KEY_RIGHT:
		case DevConfig.KEY_NUM6:
			HighScore_Paint_Count = 0;
			HighScore_ListMode++;
			if (HighScore_ListMode>=Const.GM_COUNT)HighScore_ListMode = Const.GM_NORMAL;
			break;
		case DevConfig.KEY_LSK:
//		case DevConfig.KEY_RSK:
			GameCanvas.SetState(Const.GS_MAINMENU);
			break;
		}
		return false;
	}
	public static int HighScore_Update()
	{
		return 0;
	}	
/**************************************************************************\
 * GS_LOADING
\**************************************************************************/
	public static int _target_GS;

	private static void Loading_Enter() {
		// TODO Auto-generated method stub
		
	}
	private static void Loading_Leave() {
		// TODO Auto-generated method stub
		
	}
	public static void StartLoading(int Target_GS)
	{
		
	}
	public static boolean Loading_Paint(Graphics g)
	{
		return false;
	}
	public static int Loading_Update()
	{
		return 0;
	}
/**************************************************************************\
 * KeyPressed
\**************************************************************************/
//#if ENABLE_TOUCH=="TRUE"		
	public void pointerPressed(int x, int y) 
		{
			cTouch.pointerPressed(x,y);
		}
	public void pointerReleased(int x, int y) 
		{
			cTouch.pointerReleased(x,y);
		}	
//#endif
	public void keyPressed(int keycode) {
		super.keyPressed(keycode);
		switch (getState())
		{
		case Const.GS_SOUNDONOFF: 
			{	
				if (cCP.SoundOnOff_Update(keycode))
					SetState(Const.GS_SP);
							break;
					}	
			//开始提示是否联网
			case Const.GS_NETWORKING:
				if(cWeaknetwork.NetOnOff_Update(keycode)){
					SetState(Const.GS_SUP);
				}
			break;
			//点击游戏圈圈判断是否联网，做相应跳转
			case Const.GS_ZEROLOGIN:
				if(cWeaknetwork.NetOnOff_Update(keycode)){
					//选择否跳转到主菜单
					if(!cWeaknetwork.isNetWork){
						
						SetState(Const.GS_MAINMENU);
						GameCanvas.SetSK(true,false);
					}else{
						//选择是则跳转到平台
						cWeaknetwork.toGameCenter();
						//跳转后将游戏状态设置为主菜单
						SetState(Const.GS_MAINMENU);
						
					}
				}
			break;
			case Const.GS_MAINMENU:{
				mainMenu.keyPressed(keycode);
				break;
			}
			case Const.GS_PLAY:{
				Play.keyPressed(keycode);
				break;
			}
			case Const.GS_SUMMARY:{
				Summary_Keypressed(keycode);
				break;
			}
			case Const.GS_UPSCORE:
				GameCanvas.SetSK(false,false);
				if(cWeaknetwork.NetOnOff_UpdateScore(keycode,zPlay.GetScore())){
					GameCanvas.SetSK(false,false);
					SetState(Const.GS_MAINMENU);
				}
				
				break;
			case Const.GS_HIGHSCORE:{
				HighScore_keyPressed(keycode);
				break;
			}
			case Const.GS_EXITMOREGAME:{
				try {
					switch (cCP.MoreGame_Update(GameMIDlet.s_this,keycode))
					{
					case cCP.MOREGAME_NULL:
						break;
					case cCP.MOREGAME_OPENED:
						GameMIDlet.s_this.platformRequest(cCP.url_moreGame);
					case cCP.MOREGAME_CANCEL:
						GameMIDlet.s_this.destroyApp(true);
						break;
					}
				} catch (Exception e) {

				}
			}
				
			default: {
				break;
			}
		}
	}

	public void keyReleased(int keycode) {
		super.keyReleased(keycode);
	}
}