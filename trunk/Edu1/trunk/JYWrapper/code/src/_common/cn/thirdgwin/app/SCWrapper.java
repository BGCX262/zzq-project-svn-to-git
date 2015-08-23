package cn.thirdgwin.app;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Graphics;

import cn.thirdgwin.lib.DevConfig;
import cn.thirdgwin.lib.cTouch;

public class SCWrapper{
	public static GameMIDlet mid;
	public static Canvas display;
	public static WrapperMain main;
	private static boolean inited = false;
	public static WrapperInfo info;
	public static void init(GameMIDlet midlet,Canvas canvas,WrapperMain mymain){
		mid = midlet;
		display = canvas;
		main=mymain;
		inited = true;
		info=new WrapperInfo();
	}
	/**跳转到付费界面
	 * @param payType
	 */
	public static void setCanvas(int payType){
		if(inited){
			Display.getDisplay(mid).setCurrent(main);
			main.payType =  payType;
		}
			
	}
	/**
	 * 返回到游戏界面
	 */
	public static void setCanvas(){
		Display.getDisplay(mid).setCurrent(display);
	}

}