package com.cmcc.music;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Graphics;

public class GameCanvas extends Canvas implements Runnable {
	public static MusicMIDlet s_this;
	public static Display s_display;
	public static Thread thread;
	int w = 0;
	int h = 0;
	int i = 0;

	public GameCanvas(MusicMIDlet gm, Display d) {
		super();
		setFullScreenMode(true);
		s_this = gm;
		s_display = d;
		w=getWidth();
		h=getHeight();
		thread = new Thread(this);
		thread.start();
	}

	public void run() {
		while (true) {
			try {
				Thread.sleep(50);
				i++;
				repaint();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	protected void paint(Graphics g) {
		g.setColor(0);
		g.fillRect(0, 0, w, 320);
		g.setColor(0xFF0000);
		g.drawString(String.valueOf(w), 50, 100, Graphics.LEFT | Graphics.TOP);
		g.drawString(String.valueOf(i), 10, 10, Graphics.LEFT | Graphics.TOP);
		g.drawString(String.valueOf(h), 100, 100, Graphics.LEFT | Graphics.TOP);
	}

}
