package com.cmcc.music;  
import javax.microedition.lcdui.Display;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;
public class MusicMIDlet extends MIDlet
{
	public static MusicMIDlet s_this;
	public static Display	s_display;
	public static GameCanvas	s_canvas;
	
	
	public MusicMIDlet()
	{
		super();
		s_this = this;
		s_display = Display.getDisplay(this);
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
	}
	protected void startApp() throws MIDletStateChangeException
	{
		if(s_canvas==null){
			s_canvas = new GameCanvas(this,s_display);
		}
		s_display.setCurrent(s_canvas);
	}

	public static void SetSoundOnOff(boolean b) {
	}

	public static void keyReleased(int i) {
	}

	public static void keyPressed(int i) {
	}

	public static MIDlet GetInstance() {
		return s_this;
	}
}