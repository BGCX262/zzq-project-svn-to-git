package cn.thirdgwin.app;
/**
 * TODO::因将 绘制和逻辑分离, zIGM 和 zMainMenu的代码几乎完全一样。除了数据定义，应考虑整理一下
 * 
 * */
import java.util.Enumeration;
import java.util.Hashtable;

import javax.microedition.lcdui.Graphics;

import cn.thirdgwin.lib.DevConfig;
import cn.thirdgwin.lib.GLLib;
import cn.thirdgwin.lib.zAnimPlayer;
import cn.thirdgwin.lib.zMenu;
import cn.thirdgwin.lib.zServiceAnim;
import cn.thirdgwin.lib.zServiceSprite;
import cn.thirdgwin.lib.zServiceText;
import cn.thirdgwin.lib.zSprite;

public class zIGM extends zMenu {
	/** 活动区域在这里了 */
	public static int[]	Rect = new int[] {-10,0,DevConfig.SCR_W,DevConfig.SCR_H - Const.SK_H};

	/** 所有菜单项目如果存在动画资源，则都存放在这里了 */
	public static Hashtable	AniItems		= new Hashtable();
	public zIGM()
	{
		super();
		
		zAnimPlayer anim = zServiceAnim.AnimCreate(Const.MINames[12][0], Const.MINames[12][1]);
		anim.SetAnim(MI_START.ANIM_NORMAL, -1);
		int[] r = anim.GetFrameRect(MI_START.ANIM_NORMAL, 0);
		LINE_HEIGHT = (r[3] - r[1]);
		int lineCount = 5;
		Rect = new int[] {-10,((DevConfig.SCR_H - LINE_HEIGHT*lineCount)>>1) - (Const.SK_H<<1),DevConfig.SCR_W>>1,LINE_HEIGHT*lineCount};
		zServiceAnim.AnimDestroy(anim);

	}
	public static zAnimPlayer GetButtonAnim(int sprID)
	{
		if (sprID<0) return null;
		Integer key = new Integer(sprID);
		zAnimPlayer anim = (zAnimPlayer)AniItems.get(key);
		if (anim==null)
		{
			anim =  zServiceAnim.AnimCreate(Const.MINames[sprID][0], Const.MINames[sprID][1]);
			if (anim!=null)
				{
				AniItems.put(key,anim);
				anim.SetAnim(MI_START.ANIM_NORMAL,-1);
				};
		}
		return anim;
	}	
	public static Hashtable ItemRect = new Hashtable();
/** 取条目的坐标定义，如果存在的话 */
	public static int[] GetItemRect(int id)
	{
		return (int[])ItemRect.get(new Integer(id));
	}

	/** 字段定义:背景ID,动画ID,菜单ID,缩进,文本ID,状态,命令,参数1,参数2,X,Y,W,H */
	public static int[][] MenuDefs = {
		{0,	-1,	Const.ID_IGM,				0,		T_G.IGM,		MI_STATE_NORMAL,									0,							0,		0,	10,10,40,20},		//主菜单
		//#if SCREENWIDTH>=176 && MODEL!="D608" && MODEL!="S700" &&  MODEL!="KG90n" && 	MODEL!="L7"	&& MODEL!="E398" && MODEL!="D508" && MODEL!="K1"
		{0, -1,	Const.ID_MUSIC,				1,		T_G.SOUND,		MI_STATE_NORMAL|MI_STATE_ENABLE_IF_HAS_SOUND,		Const.ID_MUSIC,				0,		0,	(DevConfig.SCR_W>>1) + Const.MENU_XOFF_FROM_CTR,Const.MENU_Y + LINE_HEIGHT*1,40,LINE_HEIGHT},		//主菜单
		//#endif
		{3, 4,	Const.ID_ABOUT,				1,		T_G.HELP,		MI_STATE_NORMAL,									Const.ID_ABOUT,				0,		0,	(DevConfig.SCR_W>>1) + Const.MENU_XOFF_FROM_CTR,Const.MENU_Y + LINE_HEIGHT*3,40,LINE_HEIGHT},
		{3,22,	Const.ID_ABOUT_CONTENT,		2,		T_G.HELP,		MI_STATE_NORMAL,									0,				0,		0},
		{0,	27,	Const.ID_IGM_BACK,			1,		T_G.BACK_TO_MAINMENU,		MI_STATE_NORMAL,									0,							0,		0,	(DevConfig.SCR_W>>1) - Const.MENU_XOFF_FROM_CTR,Const.MENU_Y + LINE_HEIGHT*4,40,LINE_HEIGHT},
		{0,	28,	Const.ID_IGM_BACK_CONFIRM,	2,		T_G.BACK_TO_MAINMENU_CONFIRM,		MI_STATE_NORMAL,									Const.ID_IGM_BACK_CONFIRM,	0,		0,	(DevConfig.SCR_W>>1) - Const.MENU_XOFF_FROM_CTR,Const.MENU_Y + LINE_HEIGHT*4,40,LINE_HEIGHT},
	};
	public String OnGetText(int idx) {
		if(idx>=0)
			return zServiceText.GetText(idx);
		else
			return "";
	}


	public int[][] OnGetDefines() {
		// TODO Auto-generated method stub
		return MenuDefs;
	}

	public int[] OnGetDrawRegion() {
		return Rect;
	}


	public boolean OnItemSelected(int[] Itemline) {
		switch (Itemline[zMenu.IDX_CMD])
		{
		case Const.ID_MUSIC:
			GameCanvas.s_SoundOn =!GameCanvas.s_SoundOn;
			if(GameCanvas.s_SoundOn)
			{
				GameCanvas.Play.m_soundCorrect.Play(1);
			} else {
				GameCanvas.Play.m_soundCorrect.Stop();
			}
			break;
		case Const.ID_IGM_BACK_CONFIRM:
			GameCanvas.SetState(Const.GS_MAINMENU);
			Close();
			Finalize();
			return true;
		}
		return false;
	}

	public void OnBeforeDraw(Graphics g) {

	}

	public void OnDrawBackground(Graphics g, int bg_id) {
		GLLib.SetColor(0);
		GLLib.FillRect(0,0,DevConfig.SCR_W,DevConfig.SCR_H);
		zAnimPlayer anim = GameCanvas.GetBGAnim(bg_id);
		if (anim!=null)
			{
			anim.Update();
			anim.SetPos(DevConfig.SCR_W>>1,DevConfig.SCR_H>>1);
			anim.Render(g);
			}
		else
			{

			}
		switch (this.m_rootID)
		{
			case Const.ID_ABOUT:
			case Const.ID_IGM_BACK:
				break;
			default:
				GameCanvas.Arrow[ARROW.ANIM_UP].SetPos(Rect[0] + (Rect[2]>>1), Rect[1]);	
				GameCanvas.Arrow[ARROW.ANIM_UP].Update();
				GameCanvas.Arrow[ARROW.ANIM_UP].Render(g);
				GameCanvas.Arrow[ARROW.ANIM_DOWN].SetPos(Rect[0] + (Rect[2]>>1), Rect[1] + Rect[3] + LINE_HEIGHT);	
				GameCanvas.Arrow[ARROW.ANIM_DOWN].Update();
				GameCanvas.Arrow[ARROW.ANIM_DOWN].Render(g);
				break;
		};
	}

	public void OnDrawCursor(Graphics g, int x, int y, int w, int h) {

		
	}

	public void OnDrawItem(Graphics g, int[] curItem,int drawtype,  int x, int y, int w, int h) {
	
			switch (this.m_rootID)
			{
				case Const.ID_ABOUT:
					zMainMenu.DrawHelpAbout(g);
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
			x+=10;
			if (anim!=null)anim.SetAnim(MI_START.ANIM_FOCUS, -1);			
			break;
		default:
			if (anim!=null)anim.SetAnim(MI_START.ANIM_NORMAL, -1);			
//			x-=5;
		}
		
		if (anim!=null)
		{
			anim.Update();
			anim.SetPos(x+(w>>1), y+(h>>1));
			anim.Render(g);
		}
		else
		{
			int fontHeight = g.getFont().getHeight();		
 			g.drawString(OnGetText(curItem[zMenu.IDX_TEXT]), x+(w>>1) , y+(h>>1) + ((fontHeight)>>1) ,Graphics.BOTTOM|Graphics.HCENTER);
		}
	}

	public void OnDrawTitle(Graphics g, String text, int[] R) {
		int[] m_drawRect = R;
		int posX = m_drawRect[0] + (m_drawRect[2]>>1);
		int posY = m_drawRect[1] + BG_FRAME_TO_ITEM_HEIGHT;
		GLLib.SetColor(0xFF0000);
		GLLib.DrawString(text, posX,posY,Graphics.HCENTER|Graphics.BOTTOM);
	}

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

	public void OnMenuStackChange(int _stackTop) {
		// TODO Auto-generated method stub
		if (_stackTop>0)
		{
			GameCanvas.SetSK(true,true);
		}
		else
		{
			GameCanvas.SetSK(true,true);
		}
	}
	private void ResetAnims(zAnimPlayer[] players)
	{
		for(int i = 0;i<players.length;i++)
		{
			if(players[i]!=null)
				{
				zServiceSprite.Put(players[i].GetSprite(),true,true);
				players[i].SetSprite(null);
				players[i]= null;
				};
		}
	
	}
	public void OnFinalize() {
		for(Enumeration e = AniItems.keys();e.hasMoreElements();)
		{
			Integer key = (Integer)e.nextElement();
			zAnimPlayer anim = (zAnimPlayer) AniItems.get(key);
			zServiceSprite.Put(anim.GetSprite(),true,true);
			anim.SetSprite(null);
		}
		AniItems.clear();	
		Inited = false;
	}
	public void Draw(Graphics g) {
		g.setClip(0,0,DevConfig.SCR_W,DevConfig.SCR_H);
		if (!Inited)
			Init();
		super.Draw(g);
	}
	public boolean OnClose() {
		return true;
	}
	public void OnInit() {
		/** 初始化条目位置 */
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

		SetMenu(Const.ID_IGM);
		HideTitle();

		GameCanvas.SetSKLabel("确认","返回");
		GameCanvas.SetSK(true,true);
	}	
	public boolean keyPressed(int keyCode)
		{
						switch (m_rootID)
						{
							case Const.ID_ABOUT:
								switch (keyCode)
								{
									case DevConfig.KEY_NUM2:
									case DevConfig.KEY_NUM4:
									case DevConfig.KEY_LEFT:
										zMainMenu.HelpPage--;
										if (zMainMenu.HelpPage<0)zMainMenu.HelpPage = Const.TextsHelp.length - 1;
										break;
									case DevConfig.KEY_NUM6:
									case DevConfig.KEY_NUM8:
									case DevConfig.KEY_RIGHT:
										zMainMenu.HelpPage++;
										if (zMainMenu.HelpPage>=Const.TextsHelp.length)zMainMenu.HelpPage = 0;
										break;
								default:
									super.keyPressed(keyCode);
									break;
								}
								break;
							default:
								super.keyPressed(keyCode);
						}
			return true;
		}	
}
