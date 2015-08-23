package cn.thirdgwin.app;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;


import cn.thirdgwin.lib.DevConfig;
import cn.thirdgwin.lib.GLLib;
import cn.thirdgwin.lib.Utils;
import cn.thirdgwin.lib.cCP;
import cn.thirdgwin.lib.cMath;
import cn.thirdgwin.lib.cWeaknetwork;
import cn.thirdgwin.lib.zAnimPlayer;
import cn.thirdgwin.lib.zMenu;
import cn.thirdgwin.lib.zServiceAnim;
import cn.thirdgwin.lib.zServiceSprite;
import cn.thirdgwin.lib.zServiceText;
import cn.thirdgwin.lib.zSprite;

public class zMainMenu extends zMenu {
	/** 活动区域在这里了 */
	public static int[]	Rect;

	public static boolean PressAnyKeyPassed = false;	//如果用户已经看过这个，就不用了
	/** Splash.sprite中的所有动画 */
	public static zAnimPlayer[] Splashes 	= new zAnimPlayer[SPLASH.NUM_ANIMS]; 
	/** 所有菜单项目如果存在动画资源，则都存放在这里了 */
	public static Hashtable	AniItems		= new Hashtable();
	
	public static final int RECT_OFFSET_X	= 10; //水平方向的位移
	public static zAnimPlayer GetButtonAnim(int sprID)
	{
		if (sprID<0) return null;
		Integer key = new Integer(sprID);
		zAnimPlayer anim = (zAnimPlayer)AniItems.get(key);
		if (anim==null)
		{
			
			String[] imgs = new String[ Const.MINames[sprID].length-1];
			System.arraycopy(Const.MINames[sprID], 1, imgs, 0, imgs.length);
			anim =  zServiceAnim.AnimCreate(Const.MINames[sprID][0], imgs);
			if (anim!=null)
				{
				AniItems.put(key,anim);
				anim.SetAnim(MI_START.ANIM_NORMAL,-1);
				};
		}
		return anim;
	}	
	/** 动态生成的菜单ID */
	private static int ID_DYNA					= 200;
	public static final int NewID() {return ID_DYNA++;}; 
	public static Vector Texts_Dyna = new Vector();
	/** 字段定义:背景ID,动画ID,菜单ID,缩进,文本ID,状态,命令,参数1,参数2,X,Y,W,H */
	public static int[][] MenuDefs = {
		{0,	-1,	Const.ID_MAINMENU,		0,		T_G.MAINMENU,		MI_STATE_NORMAL|MI_STATE_HIDE_TEXT,					0,				0,		0,		10,		10,40,20},		//主菜单
		{1,	0,	Const.ID_NEWGAME,		1,		T_G.NEWGAME,		MI_STATE_NORMAL|MI_STATE_HIDE_TEXT,					Const.ID_NEWGAME,		0,		0,	(DevConfig.SCR_W>>1),(DevConfig.SCR_H>>2),40,LINE_HEIGHT},		//主菜单
		

		{2,7,	Const.ID_GM_NORMAL,		2,		T_G.GM_NORMAL,		MI_STATE_NORMAL|MI_STATE_HIDE_TEXT,					Const.ID_GM_NORMAL,	0,		0,	(DevConfig.SCR_W>>1) - Const.MENU_XOFF_FROM_CTR,Const.MENU_Y,40,LINE_HEIGHT},		//主菜单
		{2,8,	Const.ID_GM_TIMELIMIT,	2,		T_G.GM_TIMER,		MI_STATE_NORMAL|MI_STATE_HIDE_TEXT,					Const.ID_GM_TIMELIMIT,0,		0,	(DevConfig.SCR_W>>1) + Const.MENU_XOFF_FROM_CTR,Const.MENU_Y + LINE_HEIGHT,40,LINE_HEIGHT},			//主菜单
		{2,9,	Const.ID_GM_RANDOM,		2,		T_G.GM_RANDOM,		MI_STATE_NORMAL|MI_STATE_HIDE_TEXT,					Const.ID_GM_RANDOM,	0,		0,	(DevConfig.SCR_W>>1) - Const.MENU_XOFF_FROM_CTR,Const.MENU_Y + LINE_HEIGHT*2,40,LINE_HEIGHT},		//主菜单	
//#if REVIEW=="FALSE" && WEAKNETWORK!="FALSE"	
   //#if WEAKONLINE_NET_SCORESENDING=="TRUE"
		{1,	30,	Const.ID_ZERO,			1,		T_G.GM_NORMAL,		MI_STATE_NORMAL|MI_STATE_HIDE_TEXT,					Const.ID_ZERO,		0,		0,	(DevConfig.SCR_W>>1),(DevConfig.SCR_H>>2),40,LINE_HEIGHT},		//主菜单
   //#endif
   //#if WEAKONLINE_MOREGAME=="TRUE" && WEAKNETWORK!="FALSE"		
		{1,	31,	Const.ID_INTRO,			1,		T_G.GM_NORMAL,		MI_STATE_NORMAL|MI_STATE_HIDE_TEXT,					Const.ID_INTRO,		0,		0,	(DevConfig.SCR_W>>1),(DevConfig.SCR_H>>2),40,LINE_HEIGHT},		//主菜单
   //#endif
//#endif
//#if MOREGAME=="TRUE" && MODEL!="D608" && MODEL!="D508"&&EDUCATION!="TRUE"
		{-1,2,	Const.ID_MOREGAME,		1,		T_G.MOREGAME,		MI_STATE_NORMAL|MI_STATE_HIDE_TEXT,					Const.ID_MOREGAME,	0,		0,	(DevConfig.SCR_W>>1) - Const.MENU_XOFF_FROM_CTR,Const.MENU_Y + LINE_HEIGHT*2,40,LINE_HEIGHT},		//主菜单
//#endif
		{4, 1,	Const.ID_HIGHSCORE,		1,		T_G.HIGHSCORE,		MI_STATE_NORMAL|MI_STATE_HIDE_TEXT,					Const.ID_HIGHSCORE,	0,		0,	(DevConfig.SCR_W>>1) - Const.MENU_XOFF_FROM_CTR,Const.MENU_Y,40,LINE_HEIGHT},		//主菜单
//#if SCREENWIDTH>=176 && MODEL!="D608" && MODEL!="S700" &&  MODEL!="KG90n" && 	MODEL!="L7"	&& MODEL!="E398" && MODEL!="D508" && MODEL!="K1"
		{-1,12,	Const.ID_MUSIC,			1,		T_G.SOUND,		MI_STATE_NORMAL|MI_STATE_HIDE_TEXT|MI_STATE_ENABLE_IF_HAS_SOUND,		Const.ID_MUSIC,		0,		0,	(DevConfig.SCR_W>>1) + Const.MENU_XOFF_FROM_CTR,Const.MENU_Y + LINE_HEIGHT*1,40,LINE_HEIGHT},		//主菜单
//#endif	
		{3, 4,	Const.ID_ABOUT,			1,		T_G.HELP,		MI_STATE_NORMAL|MI_STATE_HIDE_TEXT,					Const.ID_ABOUT,		0,		0,	(DevConfig.SCR_W>>1) + Const.MENU_XOFF_FROM_CTR,Const.MENU_Y + LINE_HEIGHT*3,40,LINE_HEIGHT},
		{3,22,	Const.ID_ABOUT_CONTENT,	2,		T_G.HELP,		MI_STATE_NORMAL,									0,				0,		0},
		{0,5,	Const.ID_QUIT,			1,		T_G.EXIT,		MI_STATE_NORMAL|MI_STATE_HIDE_TEXT,					0,				0,		0,	(DevConfig.SCR_W>>1) - Const.MENU_XOFF_FROM_CTR,Const.MENU_Y + LINE_HEIGHT*4,40,LINE_HEIGHT},
		{0,29,	Const.ID_QUIT_CONFIRM,	2,		T_G.EXIT_CONFIRM,		MI_STATE_NORMAL|MI_STATE_HIDE_TEXT,					Const.ID_QUIT_CONFIRM,0,		0,	(DevConfig.SCR_W>>1) - Const.MENU_XOFF_FROM_CTR,Const.MENU_Y,40,LINE_HEIGHT},
	};
	
	public static Hashtable ItemRect = new Hashtable();
	public static int[] GetItemRect(int id)
	{
		return (int[])ItemRect.get(new Integer(id));
	}
	public zMainMenu()
	{
		super();
		/** 初始化条目位置 */
		/** 取条目的坐标定义，如果存在的话 */
		{
		int[] R ;
		for (int i = MenuDefs.length - 1;i>=0;i--)
			{
				R = new int[4];
				if (MenuDefs[i].length>=(zMenu.IDX_V2+1) + 4)
				{
					System.arraycopy(MenuDefs[i], zMenu.IDX_V2+1, R,0,4);
					ItemRect.put(new Integer((MenuDefs[i][zMenu.IDX_ID])), R);
				};
			}
		}
		zAnimPlayer anim = zServiceAnim.AnimCreate(Const.MINames[0][0], Const.MINames[0][1]);
		anim.SetAnim(MI_START.ANIM_NORMAL, -1);
		int[] r = anim.GetFrameRect(MI_START.ANIM_NORMAL, 0);
		LINE_HEIGHT = (r[3] - r[1]);
		int lineCount = 5;
		Rect = new int[] {-RECT_OFFSET_X,((DevConfig.SCR_H - LINE_HEIGHT*lineCount)>>1) - (Const.SK_H<<1),DevConfig.SCR_W>>1,LINE_HEIGHT*lineCount};

		zServiceAnim.AnimDestroy(anim);
		GameCanvas.SetSK(true,false);
	}
	
	private void InsertCataListAt(int pos,int indent,int cmd,int var2)
	{	int cataCount =Libs.Catalog.size();
		String[] cataline;
		int textid = 0;
		int Flag = MI_STATE_NORMAL;
		int libid = 0;
		for(int i = cataCount - 1;i>=0;i--)
		{
			libid = i;
			cataline = (String[])Libs.Catalog.elementAt(i);
			textid = Texts_Dyna.size();
			Texts_Dyna.addElement(cataline[Libs.CAT_TITLE]);
			/** ID_GM_TIMELIMIT 在ID_MAINMENU的子菜单中的索引实际上是 3 */
			/** 指向 mi_catalog.bmp 资源 */
			InsertItem(pos, new int[] {-1,20, NewID(),indent,-textid,Flag,cmd,libid,var2});
		}	
	}
	/** 装入题库列表 */
	public void LoadCataList()
	{
		
		/** 题库菜单，追加到各模式中 */
		InsertCataListAt(3,3,Const.ID_CHOOSELIB,Const.GM_TIMER);
		/** 题库菜单，追加到各模式中 */
		InsertCataListAt(2,3,Const.ID_CHOOSELIB,Const.GM_NORMAL);
		
		

	}
	public void Draw(Graphics g) {
		switch (GameCanvas.getStateSub())
		{
		case Const.MM_S_Init:
			GameCanvas.setStateSub(Const.MM_S_RUN);
			break;
		case Const.MM_S_RUN:
			if (!PressAnyKeyPassed)
				DrawPressAnyKey(g);
			else
				DrawNormal(g);
			break;
		}
	}
/****************************************************************\
 * PressAnyKey 画面 	
\****************************************************************/
	private static int[] AnyKey_TargetPos = new int[]{DevConfig.SCR_W>>1, DevConfig.SCR_H - (Const.SK_H <<2)};
	private static  int AnyKey_MoveSpeed = 16;
	private void DrawPressAnyKey(Graphics g) {
		OnDrawBackground(g,6);	//0 对应第0号背景动画
		if (!PressAnyKeyPassed)
		{
		Splashes[SPLASH.ANIM_ANYKEY].SetPos(AnyKey_TargetPos[0], AnyKey_TargetPos[1]);
		Splashes[SPLASH.ANIM_ANYKEY].Update();
		Splashes[SPLASH.ANIM_ANYKEY].Render(g);
		}
	}
/****************************************************************\
 * 正常画面 	
\****************************************************************/	
	private void DrawNormal(Graphics g) {
		int[] line = this.GetItemData(m_rootIdx);
		switch (line[zMenu.IDX_ID])
		{
		case Const.ID_GM_NORMAL:
		case Const.ID_GM_RANDOM:
			break;
		default:
			super.Draw(g);
		}
	}	
	public int[] OnGetDrawRegion()
	{
/*
		int[] line = this.GetItemData(m_rootIdx);
		switch (line[zMenu.IDX_ID])
		{
		case Const.ID_GM_NORMAL:
		case Const.ID_GM_RANDOM:
			return testRect;
		default:
			return Rect;
		}	
*/
			
		return Rect;
	}
	public String OnGetText(int idx) {
		if(idx>=0)
			return zServiceText.GetText(idx);
		else
			return (String)Texts_Dyna.elementAt(-idx);
	}
	public int[][] OnGetDefines()
	{
		return MenuDefs;
	}

	public void OnDraw(Graphics g) {
		// TODO Auto-generated method stub
		
	}
	public void OnDrawTitle(Graphics g,String text)
	{
		
	}
	public void OnDrawBackground (Graphics g,int bg_id)
	{
		GLLib.SetColor(0);
		GLLib.FillRect(0,0,DevConfig.SCR_W,DevConfig.SCR_H);
		zAnimPlayer anim = GameCanvas.GetBGAnim(bg_id);
		if (anim!=null)
			{
			anim.Update();
			anim.SetPos(DevConfig.SCR_W>>1,DevConfig.SCR_H>>1);
			anim.Render(g);
			};
	if (PressAnyKeyPassed)	
	{
		switch(m_rootID)
		{
			case Const.ID_ABOUT:
			case Const.ID_QUIT:
				break;
			default:
				GameCanvas.Arrow[ARROW.ANIM_UP].SetPos(Rect[0] + (Rect[2]>>1), Rect[1]);	
				GameCanvas.Arrow[ARROW.ANIM_UP].Update();
				GameCanvas.Arrow[ARROW.ANIM_UP].Render(g);
				GameCanvas.Arrow[ARROW.ANIM_DOWN].SetPos(Rect[0] + (Rect[2]>>1), Rect[1] + Rect[3] + LINE_HEIGHT);	
				GameCanvas.Arrow[ARROW.ANIM_DOWN].Update();
				GameCanvas.Arrow[ARROW.ANIM_DOWN].Render(g);				
				break;
			
		}
	};		
	}
	public boolean OnItemSelected(int[] Itemline)
	{
			Utils.Dbg("OnItemSelected");
		switch (Itemline[zMenu.IDX_CMD])
		{
		case Const.ID_CHOOSELIB:
				{
				GameCanvas.LibID = Itemline[zMenu.IDX_V1];
				GameCanvas.GameMode = Itemline[zMenu.IDX_V2];
				GameCanvas.SetState(Const.GS_PLAY);
				}
			break;
		case Const.ID_ZERO:
			if(!cWeaknetwork.isNetWork){
				//如果未联网则跳转到联网确认界面
				GameCanvas.SetState(Const.GS_ZEROLOGIN);
			}else{
				//联网则跳转到弱联网平台
				cWeaknetwork.toGameCenter();
			}
			break;
		case Const.ID_INTRO:
			cWeaknetwork.callMoreGame();
			break;
		case Const.ID_GM_RANDOM:
				{
				GameCanvas.LibID = cMath.Math_Rand(0, Libs.Catalog.size());
				GameCanvas.GameMode = Const.GM_RANDOM;
				GameCanvas.SetState(Const.GS_PLAY);
				}			
			break;
		case Const.ID_CONTINUE:
			break;
		case Const.ID_HIGHSCORE:
				GameCanvas.SetState(Const.GS_HIGHSCORE);
			break;
		case Const.ID_MUSIC:
			GameCanvas.s_SoundOn = !GameCanvas.s_SoundOn;
			if(GameCanvas.s_SoundOn)
			{
				GameCanvas.m_soundPlayer.Play();
			} else {
				GameCanvas.m_soundPlayer.Stop();
			}
			break;
		case Const.ID_MOREGAME:
//#if MOREGAME=="TRUE" && MODEL!="D608" && MODEL!="D508"			
			try {
				GameMIDlet.s_this.platformRequest(cCP.url_moreGame);
				GameMIDlet.s_this.destroyApp(true);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//#endif			
			break;
		case Const.ID_QUIT_CONFIRM:
			GameCanvas.SetState(Const.GS_EXITMOREGAME);
			GameCanvas.SetSKLabel("确定", "取消");
			GameCanvas.SetSK(true,true);
			break;
		default:
			return false;
		}
		return true;
	}
	public void OnBeforeDraw(Graphics g)
			{
				if (!PressAnyKeyPassed)
				{
					zAnimPlayer[] Anims = Splashes;
					for (int i = 0; i < SPLASH.NUM_ANIMS; i++)
					{
						if (Anims[i] != null)
							Anims[i].Update();
					}
				}
			}

	public void OnDrawCursor(Graphics g, int x, int y, int w, int h) {
		//TODO:测试用光标
		int[] m_drawRect = OnGetDrawRegion();
		GLLib.g.setColor(0xfff222);
		GLLib.g.fillRect(m_drawRect[0] + x, y, 10, 10);	
	}

	
	
	public void OnDrawItem(Graphics g, int[] curItem,int drawtype,  int x, int y, int w, int h) {
	
			switch (this.m_rootID)
			{
				case Const.ID_ABOUT:
					DrawHelpAbout(g);
					GameCanvas.SetSK(false,true);

					return;
			};
			
		zAnimPlayer anim;
		switch (curItem[zMenu.IDX_ID])
		{
		case Const.ID_MUSIC:
			if (GameCanvas.s_SoundOn)
				anim = GetButtonAnim(12);
			else
				anim = GetButtonAnim(13);
			break;
		default:
			// 是否Focus的条目
			anim = GetButtonAnim(curItem[zMenu.IDX_SPRITE]);
			break;
		}
		
		switch (drawtype)
		{
		case zMenu.DRAW_FOCUS:
			x+=RECT_OFFSET_X;
			anim.SetAnim(MI_START.ANIM_FOCUS, -1);
			break;
		default:
			anim.SetAnim(MI_START.ANIM_NORMAL, -1);
//			x-=5;
		}
		if(false)
		{
			g.setColor(0xFF0000);
			g.drawRect(x,y,w,h);
		}
		
		
		if (((curItem[zMenu.IDX_STATE] & MI_STATE_HIDE_SPR)==0) && (anim!=null))
		{
			anim.Update();
			anim.SetPos(x+(w>>1), y+(h>>1));
			anim.Render(g);
		}
		if ((curItem[zMenu.IDX_STATE] & MI_STATE_HIDE_TEXT)==0)
		{
			int fontHeight = g.getFont().getHeight();
			//#if GAME_NAME=="J_2011_12_Edu1_HappyLife" || GAME_NAME=="Edu1" || GAME_NAME=="EduDriver" || GAME_NAME=="J_2011_12_Edu1_ChuYi"
			GameCanvas.MainFont.SetSystemFontColor(0x0);
			//#else
			GameCanvas.MainFont.SetSystemFontColor(0xffffff);
			//#endif
			String text = OnGetText(curItem[zMenu.IDX_TEXT]);
			text = GameCanvas.MainFont.TextFitToFixedWidth(text, DevConfig.SCR_W - RECT_OFFSET_X);
//			GameCanvas.MainFont.DrawString(g,OnGetText(curItem[zMenu.IDX_TEXT]), x+(w>>1) , y+(h>>1) + ((fontHeight)>>1) ,Graphics.BOTTOM|Graphics.HCENTER);
			switch (this.m_rootID)
			{
				default:
					GameCanvas.MainFont.DrawPage(g,text, x+(w>>1) , y+(h>>1) + ((fontHeight)>>1) ,Graphics.BOTTOM|Graphics.HCENTER);
					break;
			};
		}
	}
	public static int HelpPage = 0;

	public static void DrawHelpAbout(Graphics g)
		{
		GameCanvas.Arrow[ARROW.ANIM_LEFT].SetPos(RECT_OFFSET_X, DevConfig.SCR_H>>1);	
		GameCanvas.Arrow[ARROW.ANIM_LEFT].Update();
		GameCanvas.Arrow[ARROW.ANIM_LEFT].Render(g);
		GameCanvas.Arrow[ARROW.ANIM_RIGHT].SetPos(DevConfig.SCR_W - RECT_OFFSET_X, DevConfig.SCR_H>>1);	
		GameCanvas.Arrow[ARROW.ANIM_RIGHT].Update();
		GameCanvas.Arrow[ARROW.ANIM_RIGHT].Render(g);
		int fontHeight = GameCanvas.MainFont.GetFontHeight();
//#if GAME_NAME=="J_2011_12_Edu1_HappyLife"

//#if RESPACK=="240_S" || SCREENWIDTH<=176		
		GameCanvas.MainFont.SetSystemFontColor(0xffffff);
//#else
		GameCanvas.MainFont.SetSystemFontColor(0x0);
//#endif
//#else
//#if RESPACK=="240_S" || SCREENWIDTH<=176		
		GameCanvas.MainFont.SetSystemFontColor(0xffffff);
//#else
		GameCanvas.MainFont.SetSystemFontColor(0x0);
//#endif
//#endif
		GameCanvas.MainFont.DrawString(g,zServiceText.GetText(T_G.HELP_TITLE), DevConfig.SCR_W>>1,  
//#if SCREENWIDTH>176
				fontHeight>>1,
//#elif SCREENWIDTH>128
				fontHeight>>1,
//#else
				5,
//#endif
				Graphics.TOP|Graphics.HCENTER);

		//#if SCREENWIDTH>=176
		String content = GameCanvas.MainFont.TextFitToFixedWidth(zServiceText.GetText(Const.TextsHelp[HelpPage]), DevConfig.SCR_W * 4 / 5);
		//#else
		String content = GameCanvas.MainFont.TextFitToFixedWidth(zServiceText.GetText(Const.TextsHelp[HelpPage]), DevConfig.SCR_W);
		//#endif

		GameCanvas.MainFont.DrawPage(g,content, 
//#if SCREENWIDTH>176
				RECT_OFFSET_X*3 ,
				fontHeight*3
//#elif SCREENWIDTH>128
				RECT_OFFSET_X*3 ,
				fontHeight +(fontHeight>>1)
//#else
				RECT_OFFSET_X ,
				fontHeight
//#endif
				,Graphics.TOP|Graphics.LEFT);
//#if SCREENWIDTH>=176
		GameCanvas.MainFont.DrawPage(g,String.valueOf(HelpPage+1) + "/" + Const.TextsHelp.length, DevConfig.SCR_W>>1 ,DevConfig.SCR_H,Graphics.BOTTOM|Graphics.HCENTER);
//#endif
		}
	public void OnDrawTitle(Graphics g, String text, int[] R) 
	{
		int[] m_drawRect = R;
		int posX = m_drawRect[0] + (m_drawRect[2]>>1);
		int posY = m_drawRect[1] + BG_FRAME_TO_ITEM_HEIGHT;
		GLLib.SetColor(0xFF0000);
		GLLib.DrawString(text, posX,posY,Graphics.HCENTER|Graphics.BOTTOM);
	}
	public boolean keyPressed(int keyCode)
	{
		switch (GameCanvas.getStateSub())
		{
		case Const.MM_S_Init:
			break;
		case Const.MM_S_RUN:
			if (!PressAnyKeyPassed)
				{
				PressAnyKeyPassed = true;
				GameCanvas.SetSK(true,false);	
				}
			else
				{
					switch (m_rootID)
					{
						case Const.ID_ABOUT:
							switch (keyCode)
							{
								case DevConfig.KEY_NUM2:
								case DevConfig.KEY_NUM4:
								case DevConfig.KEY_LEFT:
									HelpPage--;
									if (HelpPage<0)HelpPage = Const.TextsHelp.length - 1;
									break;
								case DevConfig.KEY_NUM6:
								case DevConfig.KEY_NUM8:
								case DevConfig.KEY_RIGHT:
									HelpPage++;
									if (HelpPage>=Const.TextsHelp.length)HelpPage = 0;
									break;
							default:
								super.keyPressed(keyCode);
								break;
							}
							break;
						default:
							super.keyPressed(keyCode);
					}
				}
			break;
		}
		return true;
	}
	/***/
	public void OnMenuStackChange(int _stackTop) {
		if (_stackTop>0)
		{
			GameCanvas.SetSK(true,true);
		}
		else
		{
			GameCanvas.SetSK(true,false);
		}
	}
	/** 用于激活动画 */
	public void OnFocusChange(int[] oldfocus, int[] newfocus) {
		zAnimPlayer anim;
		if (oldfocus!=null)
		{
			anim = GetButtonAnim(oldfocus[zMenu.IDX_SPRITE]);
		}
		if (newfocus!=null)
		{
		anim = GetButtonAnim(newfocus[zMenu.IDX_SPRITE]);
		};
	}

	public boolean OnClose() {
		/** 主菜单不允许关闭 */
		return false;
	}
	private void ResetAnims(zAnimPlayer[] players)
	{
		for(int i = 0;i<players.length;i++)
		{
			if(players[i]!=null)
				{
				zServiceAnim.AnimDestroy(players[i],true,true);
				players[i].SetSprite(null);
				players[i]= null;
				};
		}
	
	}
	
	public void OnInit() {
		SetMenu(Const.ID_MAINMENU);
		HideTitle();
		Texts_Dyna.addElement("站位字符串");
		LoadCataList();		
		zSprite spr;
		if (!PressAnyKeyPassed)
		{
		//装入Splash
		//#if SCREENWIDTH>176
		//#if GAME_NAME=="EDU1" || GAME_NAME=="EDU4" || GAME_NAME=="EduShuXue" || GAME_NAME=="EDU7" || GAME_NAME=="J_2011_12_Edu1_HappyNature" || GAME_NAME=="J_2011_12_Edu1_ChuYi" || GAME_NAME=="J_2011_12_Edu1_HappyLife" || GAME_NAME=="EduDriver1"
		spr = zServiceSprite.Get("/splash.bmp", new String[]{"/splash_bg.png","/splash_bird.png","/splash_boom.png","/splash_title.png","/splash_press.png"},false);
		//#elif GAME_NAME=="EDU2" || GAME_NAME=="EDU3" || GAME_NAME=="Edu6" || GAME_NAME=="J_2011_11_Edu1_EduEng" || GAME_NAME=="J_2011_11_Edu1_EduShuXue1" || GAME_NAME=="J_2011_11_Edu1_EduYuWen1" || GAME_NAME=="J_2011_11_Edu3_CrazyShuXue" || GAME_NAME=="J_2011_11_Edu2_HappyYuWen"
		spr = zServiceSprite.Get("/splash.bmp", new String[]{"/splash_bg.png","/splash_title.png","/splash_press.png"},false);
		//#elif GAME_NAME=="EduYuWen"
		spr = zServiceSprite.Get("/splash.bmp", new String[]{"/splash_bg.png","/splash_press.png"},false);
		//#elif GAME_NAME=="EDU5"
		
		//#else
		spr = zServiceSprite.Get("/splash.bmp", new String[]{"/splash_bg.png","/splash_bird.png","/splash_boom.png","/splash_title.png","/splash_press.png"},false);
		//#endif
		//#else
		spr = zServiceSprite.Get("/splash.bmp", new String[]{"/splash_bg.png","/splash_press.png"},false);
		//#endif
			for(int i = 0;i<SPLASH.NUM_ANIMS;i++)
			{
				Splashes[i] = zServiceAnim.AnimCreate(spr);
				Splashes[i].SetAnim(i, -1);
			}
			Splashes[SPLASH.ANIM_ANYKEY].SetPos(DevConfig.SCR_W>>1, DevConfig.SCR_H>>2);
			GameCanvas.SetSKLabel("确认","返回");
			GameCanvas.SetSK(false,false);
		};
			
	}
	public void OnFinalize()
	{
		ResetAnims(Splashes);
		for(Enumeration e = AniItems.keys();e.hasMoreElements();)
		{
			Integer key = (Integer)e.nextElement();
			zAnimPlayer anim = (zAnimPlayer) AniItems.get(key);
			zServiceAnim.AnimDestroy(anim,true,true);
		}
		AniItems.clear();
	}	
}
