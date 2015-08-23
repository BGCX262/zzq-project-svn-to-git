package cn.thirdgwin.app;

import javax.microedition.lcdui.Graphics;

import cn.thirdgwin.lib.cCP;

public class Suggestion {
	public static String sugStr = "此屏用于教育类游戏提示";
	public static int startX = cCP.SCR_W;
	public static int startY = cCP.SCR_H;
	private static int flag = 0;		
	public static boolean isFinish = false;
	public static void paint(Graphics g){
		if (isFinish)
			return;
		flag++;
		g.setClip(0, 0, cCP.SCR_W, cCP.SCR_H);
		g.setColor(0x000000);
		g.fillRect(0, 0 , startX, startY);
		g.setColor(0xffffff);
		g.drawString(sugStr, 20, 20,Graphics.TOP|Graphics.LEFT );
		if(flag>=50){
			isFinish = true;
			return;
		}
	}
}
