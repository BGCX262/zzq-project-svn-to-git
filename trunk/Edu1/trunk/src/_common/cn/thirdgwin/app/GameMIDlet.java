package cn.thirdgwin.app;  
import javax.microedition.lcdui.Display;
//#if ENABLE_TOUCH=="TRUE"		
import javax.microedition.lcdui.Graphics;
import cn.thirdgwin.lib.cTouch;
//#endif
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

import cn.thirdgwin.lib.DevConfig;
import cn.thirdgwin.lib.cCP;
import cn.thirdgwin.lib.cMath;
import cn.thirdgwin.lib.cWeaknetwork;
import cn.thirdgwin.lib.zServiceText;
public class GameMIDlet extends MIDlet
{
	public static GameMIDlet s_this;
	public static Display	s_display;
	public static GameCanvas	s_canvas;
	
	
	public GameMIDlet()
	{
		super();
		s_this = this;
		cWeaknetwork.initOlogin("天才养成计划E", 10123, "700010820000", "djxogcZMHEkD7KHKzn6XtZFf/RA=", "/icon.png",true);
		s_display = Display.getDisplay(this);
		GameCanvas.s_Paused = false;
		cCP.SetKeyValue(new int[] {DevConfig.KEY_LSK,DevConfig.KEY_RSK});
		cMath.Math_RandSetSeed(System.currentTimeMillis());
	}

	protected void destroyApp(boolean arg0) throws MIDletStateChangeException
	{
		s_canvas=null;
		s_display = null;
		System.gc();
		notifyDestroyed();
	}

	protected void pauseApp()
	{
			GameCanvas.s_Paused = true;
			//#if BRAND=="Motorola"
			s_canvas.hideNotify();
			//#endif
	}
//#if ENABLE_TOUCH=="TRUE"		
	public static void InitTouch_LRSK()
		{
			cTouch.SetScreenSize(DevConfig.SCR_W, DevConfig.SCR_H);
			cTouch.SetScreenSize(DevConfig.SCR_W,DevConfig.SCR_H);
			cTouch.AddBtn(DevConfig.SCR_W, DevConfig.SCR_H, new int[]{0,0,50,30}, Graphics.LEFT|Graphics.BOTTOM, null, null, DevConfig.KEY_LSK);
			cTouch.AddBtn(DevConfig.SCR_W, DevConfig.SCR_H, new int[]{0,0,50,30}, Graphics.RIGHT|Graphics.BOTTOM, null, null, DevConfig.KEY_RSK);
			cTouch.TargetCanvas = s_canvas;			
		}
//#endif		
	protected void startApp() throws MIDletStateChangeException
	{
		if(s_canvas==null){
			s_canvas = new GameCanvas(this,s_display);
	//#if ENABLE_TOUCH=="TRUE"		
			InitTouch_LRSK();
	//#endif		
		
//			(new Thread(s_canvas)).start();
		}
		//#if BRAND=="Motorola"
		else{
			s_canvas.OnResume();
		}
		//#endif
		GameCanvas.s_Paused = true;
		s_display.setCurrent(s_canvas);
	}

	public static void SetSoundOnOff(boolean b) {
		GameCanvas.s_SoundOn = b;
	}

	public static void keyReleased(int i) {
		s_canvas.keyReleased(i);
	}

	public static void keyPressed(int i) {
		s_canvas.keyPressed(i);
	}

	public static MIDlet GetInstance() {
		// TODO Auto-generated method stub
		return s_this;
	}
}