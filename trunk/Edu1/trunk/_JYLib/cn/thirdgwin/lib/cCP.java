package cn.thirdgwin.lib;

import java.io.IOException;
import java.io.InputStream;
import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.media.Manager;
import javax.microedition.media.Player;
import javax.microedition.midlet.MIDlet;

import cn.thirdgwin.app.GameMIDlet;

public class cCP
{
	public static int SoundOn = 0;
//#expand public static int SCR_W = %SCREENWIDTH%;
	public static int SCR_W = 240;
//#expand public static int SCR_H = %SCREENHEIGHT%;	
	public static int SCR_H = 320;
//#expand public static final int FPSLIMIT = %FPSLIMIT%;
	public static final int FPSLIMIT = 10;
	public static final int FRAME_DT = 1000/FPSLIMIT;
	public static boolean HasSound = true;
	//图像资料定义
	public static final int IDX_BG 				= 0;
	public static final int IDX_BLUR1			= 1 + IDX_BG;
	public static final int IDX_BLUR2			= 1 + IDX_BLUR1;
	public static final int IDX_BLUR3			= 1 + IDX_BLUR2;
	public static final int IDX_CNM 			= 1 + IDX_BLUR3;
	public static final int IDX_CNM_2 			= 1 + IDX_CNM;
	public static final int IDX_CNM_3 			= 1 + IDX_CNM_2;
	public static final int IDX_CP				= 1 + IDX_CNM_3;
	public static final int IDX_FIRSTLOGO		= 1 + IDX_CP;
	public static final int IDX_GAME			= 1 + IDX_FIRSTLOGO;
	public static final int IDX_LAST			= 1 + IDX_GAME;
	public static final int IDX_LINE			= 1 + IDX_LAST;
	public static final int IDX_NO				= 1 + IDX_LINE;
	public static final int IDX_SOUND			= 1 + IDX_NO;
	public static final int IDX_YES				= 1 + IDX_SOUND;
	public static final int IDX_CPYL				= 1 + IDX_YES;
	public static final int IDX_MAX				= 1 + IDX_CPYL;
	public static Image [] Images = new Image[IDX_MAX];
	//文件名，与IDX系列顺序相同
	public static final String[] Imgnames = {
		"/sp/bg.png",
		"/sp/blur1.png",
		"/sp/blur2.png",
		"/sp/blur3.png",
		"/sp/cnm.png",
		"/sp/cnm_2.png",
		"/sp/cnm_3.png",
		"/spg/cp.png",
		"/spg/firstlogo.png", 
		"/spg/game.png",
		"/sp/last.png",
		"/sp/line.png",
		"/sp/no.png",
		"/sp/sound.png",
		"/sp/yes.png",
		"/spg/cp_yl.png",
	};

	private static final Image GetImage(int id)
	{
		Image retV =Images[id]; 
		try {
			if (retV==null)
				{
						retV = Images[id] = Image.createImage(Imgnames[id]);
				}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return retV;
	}
	private static final void FreeImage(int id)
	{
		Images[id] = null;
	}
	private static final void FreeAllImage()
	{
		for(int i = Images.length -1 ;i>=0;i--)
			Images[i] = null;
	}
	public static final void SetScreenSize(int w,int h)
	{
		SCR_W = w;
		SCR_H = h;
//#if ENABLE_TOUCH=="TRUE"
		cTouch.SetScreenSize(w, h);
//#endif		
	}
	
	//public static boolean IsTigerOver = false;
	public static final void cCPdrawCleanScreen(Graphics g, int c) {
		g.setColor(c);
		g.fillRect(0, 0, SCR_W, SCR_H);
	}
	public cCP(){
	}
	/***************************************************************\
	|** 老虎机 Tiger
	\***************************************************************/
//#if (TIGER=="TRUE")	
		public  static int runTime = 0;
	//#if SCREENWIDTH>=176	
		public static int TIGER_DURING = 6000;
	//#else
		public static int TIGER_DURING = 6000;
	//#endif	
		public static Image logo[];
	//#if SCREENWIDTH>=176	
		public static final int[] PicIdx = {IDX_BG,IDX_CNM,IDX_CNM_2,IDX_CNM_3,IDX_BLUR2,IDX_BLUR3,IDX_BLUR1,IDX_CP,IDX_GAME};
	//#else
		public static final int[] PicIdx = {IDX_CNM,IDX_CP,IDX_GAME};
	//#endif
		public static boolean Tiger_Finished = false;
		
		public static Player SoundPlayer;
//#endif	
	public static void Tiger_Sound_Play()
	{
//#if (TIGER=="TRUE")
		String tempName;
		tempName = "/sound/tiger.mid"; 
		InputStream isSound = Utils.getResourceAsStream(tempName);
		try {
			SoundPlayer = Manager.createPlayer(isSound, "audio/midi");
			isSound = null;
			SoundPlayer.realize();
			SoundPlayer.prefetch();
			SoundPlayer.setLoopCount(1);
			SoundPlayer.start();
		} catch (Exception e) {
			e.printStackTrace();
		}	
//#endif		
	}
	public static final void Tiger_Sound_Final()
	{
//#if (TIGER=="TRUE")
		if(SoundPlayer!=null)
		{
			if(SoundPlayer.getState() == Player.STARTED)
			{
				try
				{
					SoundPlayer.stop();
				}catch(Exception e){}
				
			}
			SoundPlayer.deallocate();
			SoundPlayer.close();
			SoundPlayer = null;
		};
//#endif		
	}
	public static final boolean Tiger_Paint(Graphics g){
//#if !(TIGER=="TRUE")
		return true;
//#else		
//#if SCREENWIDTH>=176
		return Tiger_Paint_Big(g);
//#else
		return Tiger_Paint_Small(g);
//#endif	
//#endif		
	}
	private  static final boolean Tiger_Paint_Small(Graphics g){
//#if !(TIGER=="TRUE")
		return true;
//#else		
		if (logo==null)
		{
			
			logo = new Image[PicIdx.length];
			for(int i = PicIdx.length - 1;i>=0;i--)
				logo[i] = GetImage(PicIdx[i]);
			if (SoundOn!=0)
				Tiger_Sound_Play();
		}
		
		
		
		if (++runTime>=TIGER_DURING / FRAME_DT ){
			logo = null;
			FreeAllImage();
//#if ENABLE_TOUCH=="TRUE"					
			cTouch.ClearBtns();
//#endif
			Tiger_Finished = true;
			Tiger_Sound_Final();
			return true;
		} else {
			cCPdrawCleanScreen(g, 0x0);//清屏
			 if(runTime<(TIGER_DURING/3) / FRAME_DT) {
				g.drawImage(logo[0], (SCR_W>>1), (SCR_H>>1), Graphics.HCENTER|Graphics.VCENTER);
			}else if(runTime<(TIGER_DURING*2/3) / FRAME_DT) {
				g.drawImage(logo[1], (SCR_W>>1), (SCR_H>>1), Graphics.HCENTER|Graphics.VCENTER);
			}else if(runTime<TIGER_DURING / FRAME_DT) {
				g.drawImage(logo[2], (SCR_W>>1), (SCR_H>>1), Graphics.HCENTER|Graphics.VCENTER);
			}
			return false;
		}	
	//#endif	
	}
	
	public  static final boolean Tiger_Paint_Big(Graphics g){
//#if !(TIGER=="TRUE")
		return true;
//#else		
		if (logo==null)
		{
			logo = new Image[PicIdx.length];
			for(int i = PicIdx.length - 1;i>=0;i--)
				logo[i] = GetImage(PicIdx[i]);
			if (SoundOn!=0)
				Tiger_Sound_Play();
		}
		if (++runTime>=TIGER_DURING / FRAME_DT){
//#if MODEL=="N73"
			if(SoundPlayer == null || SoundPlayer.getMediaTime()==0){ //不播放声音或者判断声音播放完毕时
//#endif
				logo = null;
				FreeAllImage();
//#if ENABLE_TOUCH=="TRUE"					
			cTouch.ClearBtns();
//#endif
				Tiger_Finished = true;
				Tiger_Sound_Final();
				return true;	
//#if MODEL=="N73"
			}else{
				return false;
				}
//#endif
		} else {
//			System.out.println("程序执行到此处");
			//cCPdrawCleanScreen(g, 0x0);//清屏
			g.drawImage(logo[0], SCR_W>>1, SCR_H>>1, Graphics.HCENTER|Graphics.VCENTER);

			 if(runTime<(TIGER_DURING * 1 / 8 )/ FRAME_DT) {
				g.drawImage(logo[2], (SCR_W>>1)-2, (SCR_H>>1)-12, Graphics.HCENTER|Graphics.VCENTER);
				g.drawImage(logo[1], (SCR_W>>1)-logo[2].getWidth()-2, (SCR_H>>1)-12, Graphics.HCENTER|Graphics.VCENTER);
				g.drawImage(logo[3], (SCR_W>>1)-4+logo[2].getWidth()+2, (SCR_H>>1)-12+1, Graphics.HCENTER|Graphics.VCENTER);
			}else if(runTime<(TIGER_DURING * 2 / 8 )/ FRAME_DT) {
				g.drawImage(logo[4+runTime%3], SCR_W>>1, (SCR_H>>1)-10, Graphics.HCENTER|Graphics.VCENTER);
			}else if(runTime<(TIGER_DURING * 3 / 8 )/ FRAME_DT) {
				g.drawImage(logo[4+runTime%3], SCR_W>>1, (SCR_H>>1)-10, Graphics.HCENTER|Graphics.VCENTER);
				g.drawImage(logo[4+(runTime+2)%3], (SCR_W>>1)-logo[2].getWidth()-2, (SCR_H>>1)-10, Graphics.HCENTER|Graphics.VCENTER);
			}else if(runTime<(TIGER_DURING * 4 / 8 )/ FRAME_DT) {
				g.drawImage(logo[4+runTime%3], SCR_W>>1, (SCR_H>>1)-10, Graphics.HCENTER|Graphics.VCENTER);
				g.drawImage(logo[4+(runTime+2)%3], (SCR_W>>1)-logo[2].getWidth()-2, (SCR_H>>1)-10, Graphics.HCENTER|Graphics.VCENTER);
				g.drawImage(logo[4+(runTime+2)%3], (SCR_W>>1)+logo[2].getWidth(), (SCR_H>>1)-10, Graphics.HCENTER|Graphics.VCENTER);
			}else if(runTime<(TIGER_DURING * 5 / 8 )/ FRAME_DT) {
				g.drawImage(logo[1], SCR_W>>1, (SCR_H>>1)-10, Graphics.HCENTER|Graphics.VCENTER);
				g.drawImage(logo[4+(runTime+2)%3], (SCR_W>>1)-logo[2].getWidth()-2, (SCR_H>>1)-10, Graphics.HCENTER|Graphics.VCENTER);
				g.drawImage(logo[4+(runTime+2)%3], (SCR_W>>1)+logo[2].getWidth(), (SCR_H>>1)-10, Graphics.HCENTER|Graphics.VCENTER);
			}else if(runTime<(TIGER_DURING * 6 / 8 )/ FRAME_DT) {
				g.drawImage(logo[1], SCR_W>>1, (SCR_H>>1)-10, Graphics.HCENTER|Graphics.VCENTER);
				g.drawImage(logo[7], (SCR_W>>1)+1-logo[2].getWidth()-2, (SCR_H>>1)-12, Graphics.HCENTER|Graphics.VCENTER);
				g.drawImage(logo[4+(runTime+2)%3], (SCR_W>>1)+logo[2].getWidth(), (SCR_H>>1)-10, Graphics.HCENTER|Graphics.VCENTER);
			}else if(runTime<(TIGER_DURING * 8 / 8 )/ FRAME_DT) {
				g.drawImage(logo[1], SCR_W>>1, (SCR_H>>1)-10, Graphics.HCENTER|Graphics.VCENTER);
				g.drawImage(logo[7], (SCR_W>>1)+1-logo[2].getWidth()-2, (SCR_H>>1)-12, Graphics.HCENTER|Graphics.VCENTER);
				g.drawImage(logo[8], (SCR_W>>1)-1+logo[2].getWidth(), (SCR_H>>1)-12, Graphics.HCENTER|Graphics.VCENTER);
			
			}
			return false;
		}
//#endif		
	}

	public static final boolean Tiger_Update(int keycode)
	{
//#if !(TIGER=="TRUE")
		return true;
//#else			
		return Tiger_Finished;
//#endif		
	}
	
	//键值定义
	public static final int L = 0;
	public static final int R = 1 + L;
	public static int[] KeyValue = new int[]{DevConfig.KEY_LSK,DevConfig.KEY_RSK};
	/***************************************************************\
	|** SoundOnOff 声音选项状态 									   *|
	\***************************************************************/
////	#if SOUNDONOFF=="TRUE"	
	public static final int LINESPACE	= 3;
	/** 返回 true表示，本状态结束 */
	public static int SoundOnOffPaintCount = 0;
	public static int SoundOnOffTotalFrame = 30;
	public static boolean BtnSoundOnOffInited = false;
	public static boolean SoundOnOffisFinished = false;
////#endif	
	public static final void SetKeyValue(int[] k)
	{
////#if SOUNDONOFF=="TRUE"			
		KeyValue = k;
////#endif		
	}
	public static final void SetHasSound( boolean On)
	{
////#if SOUNDONOFF=="TRUE"			
		HasSound = On;
////#endif		
	}
	public static final boolean SoundOnOff_paint(Graphics g){
//#if SCREENWIDTH<176 || MODEL=="D508" || MODEL=="KG90n" || MODEL=="D608"|| MODEL=="E398" || MODEL=="L7" || MODEL=="K1"|| MODEL=="S700"
		return SoundOnOff_paint_small(g);
//#else
		return SoundOnOff_paint_big(g);
//#endif
	}
	public static long TotalTime = 0;
	public static final boolean SoundOnOff_paint_small(Graphics g){
		cCPdrawCleanScreen(g,0);
		Image Img_firstLogo = GetImage(IDX_FIRSTLOGO);
		Image Img_CNM = GetImage(IDX_CNM);
		g.drawImage(Img_firstLogo, DevConfig.SCR_W>>1, Img_firstLogo.getHeight()>>1, Graphics.HCENTER|Graphics.VCENTER);
		g.drawImage(Img_CNM, cCP.SCR_W>>1, Img_firstLogo.getHeight()  , Graphics.HCENTER|Graphics.TOP);
//		TotalTime += GLLib.s_Tick_Paint_FrameDT;
		
		if(TotalTime++>20){
			GameMIDlet.SetSoundOnOff(false);
			return true;
		}else{
			return false;
		}
	}
	public static final boolean SoundOnOff_paint_big(Graphics g)
	{		
		cCPdrawCleanScreen(g,0);
		if (!BtnSoundOnOffInited)
		{
//#if MODEL != "G12_Review"
//#if ENABLE_TOUCH=="TRUE"					
			cTouch.InitVirtualKeysForLRSK(SCR_W,SCR_H);
//#endif
//#endif
			BtnSoundOnOffInited = true;
			
		}
		int FontHeight  = g.getFont().getHeight();
		if (!HasSound)
		{
			if ((++SoundOnOffPaintCount) > SoundOnOffTotalFrame)
			{
				FreeAllImage();
//#if ENABLE_TOUCH=="TRUE"					
				cTouch.ClearBtns();
				cTouch.TargetCanvas = null;
//#endif
				SoundOnOffisFinished =true;
				return true;
			}
		}
		// 读取资源
//#if SOUNDONOFF=="TRUE"
		Image Img_firstLogo = GetImage(IDX_FIRSTLOGO);
//#endif
		Image Img_sound	= GetImage(IDX_SOUND);
		Image Img_line = GetImage(IDX_LINE);
		Image Img_yes = GetImage(IDX_YES);
		Image Img_no = GetImage(IDX_NO);
		Image Img_cp = GetImage(IDX_CP);
		
		Image Img_CNM = GetImage(IDX_CNM);
//#if CHANNEL_NAME=="SHYL"
		Image Img_CPYL = GetImage(IDX_CPYL);
//#endif
		// 开始绘制
		int height = SCR_H;
		if(HasSound)
		{		
		// 绘制左右软键
		g.drawImage(Img_yes, 	0, 		height , Graphics.LEFT|Graphics.BOTTOM);
		g.drawImage(Img_no, 	SCR_W, 	height , Graphics.RIGHT|Graphics.BOTTOM);
		height -= Img_yes.getHeight();
		height -=LINESPACE;
		// 绘制线条
		height -= Img_line.getHeight();
		g.drawImage(Img_line, SCR_W>>1, height , Graphics.HCENTER|Graphics.TOP);
		height -=LINESPACE;

		// 绘制  是否开启声音
		height -= Img_sound.getHeight();
		g.drawImage(Img_sound, SCR_W>>1, height , Graphics.HCENTER|Graphics.TOP);
		height -=LINESPACE;	
		}
//#if CHANNEL_NAME=="SHYL"
		g.drawImage(Img_CPYL, 	SCR_W>>1, 		height>>1 , Graphics.VCENTER|Graphics.HCENTER);
//#endif
//#if SOUNDONOFF=="TRUE"
//#if LOGO=="TRUE"
		// 绘制CNM Logo
		height -= Img_CNM.getHeight();
		g.drawImage(Img_CNM, SCR_W>>1, height , Graphics.HCENTER|Graphics.TOP);
		height -=LINESPACE;	
//#endif		
		
		// 绘制  游戏 Logo
		// 这正是剩余的高度，将FirstLogo 放到正中
		g.drawImage(Img_firstLogo, SCR_W>>1, height>>1 , Graphics.HCENTER|Graphics.VCENTER);
//#endif
//#if SHOWGAMENAME=="TRUE"
		//画游戏名字
//#endif
		return false;	
	};
	/** 返回 true表示，本状态结束 */
	public static final boolean SoundOnOff_Update(int keycode)
	{
		
////#if SOUNDONOFF=="TRUE"			
		int idx = -1;
		boolean retV = false;
//#if SCREENWIDTH<176 || MODEL=="D508" || MODEL=="KG90n" || MODLE=="D608"|| MODEL=="E398" || MODEL=="L7"
//#else
		if(!HasSound)
		{
			return SoundOnOffisFinished;
		}
		for (int i = KeyValue.length - 1;i>=0;i--)
		{
			if (KeyValue[i] == keycode)
			{
				idx = i;
				break;
			}
		}
		if (idx<0) return false;

		switch (idx)
		{
		case L:
			SoundOn = -1;

			GameMIDlet.SetSoundOnOff(true);

			retV = true;
			break;
		case R:
			SoundOn = 0;
			GameMIDlet.SetSoundOnOff(false);
			retV = true;
			break;
		}
		if (retV)
		{
			FreeAllImage();
//#if ENABLE_TOUCH=="TRUE"					
			cTouch.ClearBtns();
			//cTouch.TargetCanvas = null;
//#endif
		}
		//#endif
		return retV;
////#else
//		return true;
////#endif		
	}
//#if ENABLE_TOUCH=="TRUE"
public static final void SoundOnOff_pointerPressed(int x, int y) {
////#if SOUNDONOFF=="TRUE"			
	cTouch.pointerPressed(x,y);
////#endif	
}
public static final void SoundOnOff_pointerReleased(int x, int y) {
////#if SOUNDONOFF=="TRUE"			
	cTouch.pointerPressed(x,y);
////#endif	
}
//#endif	
	
	
/********************************************************************\	
 * MoreGame QuitConfirm 部分
\********************************************************************/
	public static String[] Info = null;
	public static String[] ConfirmInfo = null;
//#expand	public static final String url_moreGame = "%URL_MOREGAME%" + "?" + "%CPID%" + "&" + "%CONTENTID%";
//	public static final String url_moreGame = "%URL_MOREGAME%";
//#if SCREENWIDTH>=176
	public static Font MyFont = Font.getFont(Font.FACE_PROPORTIONAL,0,Font.SIZE_SMALL);
//#else
	public static Font MyFont = Font.getFont(Font.FACE_PROPORTIONAL,0,Font.SIZE_LARGE);
//#endif

	public static final boolean MoreGame_Paint(Graphics g)
	{
		if(GameMIDlet.GetInstance() != null){
		GameMIDlet.GetInstance().notifyDestroyed();
		return true;
		}
//#if MOREGAME=="TRUE" && MODEL!="D608" && MODEL!="D508"		
		Image last = cCP.GetImage(IDX_LAST);
		if (Info ==null)
		{
//#if SCREENWIDTH>=176			
			Info = new String[] {"更多精彩游戏","尽在游戏频道","Wap.xjoys.com","确定", "退出"};
//#else
			Info = new String[] {"更多精彩游戏","尽在游戏频道","Wap.xjoys.com","确定", "退出"};
//#endif			
		}
		if (last!=null)
		{
			g.drawImage(last,SCR_W>>1,SCR_H>>1,Graphics.VCENTER|Graphics.HCENTER);
		}
		if (Info!=null)
		{
			g.setFont(MyFont);
			g.setColor(0xFFFFFF);
			/** 去掉  确定 退出 所占的2 */
			int ContextHeight = MyFont.getHeight() * ((Info.length - 2));
			int Y = (SCR_H - MyFont.getHeight() - ContextHeight)>> 1;
			int i = 0,loop_end = Info.length - 2;
			while (i<loop_end)
			{
				g.drawString(Info[i], SCR_W>>1, Y + (i*MyFont.getHeight()), Graphics.HCENTER|Graphics.TOP);
				i++;
			}
			/** 左右软键，可以用宏，处理那些对调的机型,-3 为让基线下的文字显示出来 */
			g.drawString(Info[Info.length -2], 0, SCR_H - 3, Graphics.LEFT|Graphics.BOTTOM);
			g.drawString(Info[Info.length -1], SCR_W, SCR_H - 3, Graphics.RIGHT|Graphics.BOTTOM);
			
		}
		return false;
//#else
		return true;
//#endif		
	}
	public static final boolean QuitConfirm_Paint(Graphics g)
	{	
//		Image last = cCP.GetImage(IDX_LAST);
		if (ConfirmInfo ==null)
		{
//#if SCREENWIDTH>=176			
			ConfirmInfo = new String[] {"确定退出？","确定", "返回"};
//#else
			ConfirmInfo = new String[] {"确定退出？","确定", "返回"};
//#endif
		}
//		if (last!=null)
//		{
//			g.drawImage(last,SCR_W>>1,SCR_H>>1,Graphics.VCENTER|Graphics.HCENTER);
//		}
		if (ConfirmInfo!=null)
		{
			g.setColor(0x000000);
			g.fillRect(0, 0, SCR_W, SCR_H);
			g.setColor(0xffffff);
			/** 去掉  确定 退出 所占的2 */
			int ContextHeight = MyFont.getHeight() * ((ConfirmInfo.length - 2));
			int Y = (SCR_H - MyFont.getHeight() - ContextHeight)>> 1;
			int i = 0,loop_end = ConfirmInfo.length - 2;
			while (i<loop_end)
			{
				g.drawString(ConfirmInfo[i], SCR_W>>1, Y + (i*MyFont.getHeight()), Graphics.HCENTER|Graphics.TOP);
				i++;
			}
			/** 左右软键，可以用宏，处理那些对调的机型,-3 为让基线下的文字显示出来 */
			g.drawString(ConfirmInfo[ConfirmInfo.length -2], 0, SCR_H - 3, Graphics.LEFT|Graphics.BOTTOM);
			g.drawString(ConfirmInfo[ConfirmInfo.length -1], SCR_W, SCR_H - 3, Graphics.RIGHT|Graphics.BOTTOM);
			
		}
		return false;	
	}
	public static final int MOREGAME_NULL		= 0;
	public static final int MOREGAME_OPENED	= 1;
	public static final int MOREGAME_CANCEL 	= 2;
	public static final int MoreGame_Update(MIDlet midlet,int keycode)
	{
//#if MOREGAME=="TRUE" && MODEL!="D608" && MODEL!="D508"		
		int idx = -1;
		int retV = MOREGAME_NULL;
		for (int i = KeyValue.length - 1;i>=0;i--)
		{
			if (KeyValue[i] == keycode)
			{
				idx = i;
				break;
			}
		}
		if (idx<0) return MOREGAME_NULL;

		switch (idx)
		{
		case L:
			MoreGame_DoOpen(midlet);
			retV = MOREGAME_OPENED;
			break;
		case R:
			retV = MOREGAME_CANCEL;
			break;
		}
		if (retV!=MOREGAME_NULL)
		{
			FreeAllImage();
//#if ENABLE_TOUCH=="TRUE"		
			cTouch.ClearBtns();
//#endif			
		}
		return retV;	
//#else
		return MOREGAME_CANCEL;
//#endif		
	}
	public static final int QuitConfirm_Update(int keycode)
	{		
		int idx = -1;
		int retV = MOREGAME_NULL;
		for (int i = KeyValue.length - 1;i>=0;i--)
		{
			if (KeyValue[i] == keycode)
			{
				idx = i;
				break;
			}
		}
		if (idx<0) return MOREGAME_NULL;

		switch (idx)
		{
		case L:
			//MoreGame_DoOpen(midlet);
			retV = MOREGAME_CANCEL;
			GameMIDlet.GetInstance().notifyDestroyed();
			break;
		case R:
			retV = MOREGAME_NULL;
			break;
		}
		if (retV!=MOREGAME_NULL)
		{
			FreeAllImage();
//#if ENABLE_TOUCH=="TRUE"		
			cTouch.ClearBtns();
//#endif			
		}
		return retV;			
	}
	private static final void MoreGame_DoOpen(MIDlet midlet){
		try {
			midlet.platformRequest(url_moreGame);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static final int PAUSE_NULL		= 0;
	public static final int PAUSE_OPENED	= 1;
	public static final int PAUSE_CANCEL 	= 2;
	public static final boolean Pause_Paint(Graphics g)
	{
		return false;
	}
	public static final int Pause_Update(int keyCode)
	{
		switch (keyCode)
		{
		case DevConfig.KEY_LSK:
			return PAUSE_OPENED;
		case DevConfig.KEY_RSK:
			return PAUSE_CANCEL;
		default:
			return PAUSE_NULL;
		}

	}
	public static int Paint_Index = 0;
	/**
	* 区别黑字白字 用法：H>>6黑字 H原值白字
	  */
	public static final void String_Splash_Paint(Graphics g,String str,int W,int H){
		if(Paint_Index++%10<5){
			if(H>1000){//区别黑字白字
				g.setColor(0x000000);
				g.drawString(str, W>>1, H>>7,Graphics.TOP
						| Graphics.HCENTER);
			}else{
				g.setColor(0xFFFFFF);
				g.drawString(str, W>>1, H>>1,Graphics.TOP
						| Graphics.HCENTER);
			}
		}
	}
	public static String[] SugInfo = null;
	public static int SugRunTime = 0;
	/**教育类提示屏
	 * @param g
	 * @param str
	 * @return
	 */
//	//#if SCREENWIDTH>=176
//	public static int totaltime = 50;
//	//#else
//	public static int totaltime = 100;
//	//#endif
//	public static boolean SuggestionPaint(Graphics g,String[] str){
////#if REVIEW!="FALSE"
//		if(SugRunTime++>totaltime){
//			return true;
//		}
//		if(SugInfo == null){
//			SugInfo = str;
//		}
//		//#if SCREENWIDTH>=176
//		if(SugInfo != null){
//			g.setColor(0, 0, 0);
//			g.fillRect(0, 0, SCR_W, SCR_H);
//			//#if SCREENWIDTH<176
//			g.setFont(MyFont);
//			//#endif
//			g.setColor(0xFFFFFF);
//			/** 去掉  确定 退出 所占的2 */
//			int ContextHeight = MyFont.getHeight() * ((SugInfo.length));
//			int Y = (SCR_H - MyFont.getHeight() - ContextHeight)>> 1;
//			int i = 0,loop_end = SugInfo.length;
//			while (i<loop_end)
//			{
//				g.drawString(SugInfo[i], SCR_W>>1, Y + (i*MyFont.getHeight()), Graphics.HCENTER|Graphics.TOP);
//				i++;
//			}
//		}
//		//#else
//		if(SugInfo != null){
//			g.setColor(0, 0, 0);
//			g.fillRect(0, 0, SCR_W, SCR_H);
//			//#if SCREENWIDTH<176
//			g.setFont(MyFont);
//			//#endif
//			g.setColor(0xFFFFFF);
//			/** 去掉  确定 退出 所占的2 */
//			int ContextHeight = MyFont.getHeight() * ((SugInfo.length>>1));
//			int Y = (SCR_H - MyFont.getHeight() - ContextHeight)>> 1;
//			int i = 0,loop_end = SugInfo.length>>1;
//			if(SugRunTime<=(totaltime>>1)){
//				while (i<loop_end)
//				{
//					g.drawString(SugInfo[i], SCR_W>>1, Y + (i*MyFont.getHeight()), Graphics.HCENTER|Graphics.TOP);
//					i++;
//				}
//			}else{
//				while (i<loop_end)
//				{
//					g.drawString(SugInfo[i+(SugInfo.length>>1)], SCR_W>>1, Y + (i*MyFont.getHeight()), Graphics.HCENTER|Graphics.TOP);
//					i++;
//				}
//			}
//		}
//		//#endif
//		return false;
////#else
//		return true;
////#endif
//		
//	}
	//#if SCREENWIDTH>=176
	public static int totaltime = 50;
	//#else
	public static int totaltime = 100;
	//#endif
	public static boolean SuggestionPaint(Graphics g,String[] str){
//#if REVIEW=="FALSE" && EDUCATION =="TRUE"
		if(SugRunTime++>totaltime){
			return true;
		}
		if(SugInfo == null){
			SugInfo = str;
		}
		//#if SCREENWIDTH>=176
		if(SugInfo != null){
			g.setColor(0, 0, 0);
			g.fillRect(0, 0, SCR_W, SCR_H);
			//#if SCREENWIDTH<176
			g.setFont(MyFont);
			//#endif
			g.setColor(0xFFFFFF);
			/** 去掉  确定 退出 所占的2 */
			int ContextHeight = MyFont.getHeight() * ((SugInfo.length));
			int Y = (SCR_H - MyFont.getHeight() - ContextHeight)>> 1;
			int i = 0,loop_end = SugInfo.length;
			while (i<loop_end)
			{
				g.drawString(SugInfo[i], SCR_W>>1, Y + (i*MyFont.getHeight()), Graphics.HCENTER|Graphics.TOP);
				i++;
			}
		}
		//#else
		if(SugInfo != null){
			g.setColor(0, 0, 0);
			g.fillRect(0, 0, SCR_W, SCR_H);
			//#if SCREENWIDTH<176
			g.setFont(MyFont);
			//#endif
			g.setColor(0xFFFFFF);
			/** 去掉  确定 退出 所占的2 */
			int ContextHeight = MyFont.getHeight() * ((SugInfo.length>>1));
			int Y = (SCR_H - MyFont.getHeight() - ContextHeight)>> 1;
			int i = 0,loop_end = SugInfo.length>>1;
			if(SugRunTime<=(totaltime>>1)){
				while (i<=loop_end)
				{
					g.drawString(SugInfo[i], SCR_W>>1, Y + (i*MyFont.getHeight()), Graphics.HCENTER|Graphics.TOP);
					i++;
				}
			}else{
				while (i<loop_end)
				{
					g.drawString(SugInfo[i+(SugInfo.length>>1) + 1], SCR_W>>1, Y + (i*MyFont.getHeight()), Graphics.HCENTER|Graphics.TOP);
					i++;
				}
			}
		}
		//#endif
		return false;
//#else
		return true;
//#endif
		
	}
};
