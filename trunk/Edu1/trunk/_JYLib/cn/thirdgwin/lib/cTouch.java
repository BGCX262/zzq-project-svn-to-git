package cn.thirdgwin.lib;
/**
 * 
 * 
 * 
按键对应方案：
L: LSK
R: RSK
A：#
B：0
C：5
D：*
方位:
L                       R
 7 8 9             C(*)
 4 5 6          A(5)  B(0)
 1 2 3             D(#)	
 */
import java.io.IOException;
import java.util.Vector;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import cn.thirdgwin.app.*;
public class cTouch {
//#if ENABLE_SENSOR=="TRUE"
	public static cSensor sensor;
//#endif	
	/** 图片、数据的索引 0 = 未按下/1= 按下 */
	public static final int IDX_UP = 0;
	public static final int IDX_DOWN = 1 + IDX_UP;
	public static final int IDX_MAX = 1 + IDX_DOWN;
	/** 键值的索引 */
	public static final int KC_IDX_KEYCODE = 0;
	public static final int KC_IDX_STATUS = 1 + KC_IDX_KEYCODE;
	public static final int KC_IDX_MAX = 1 + KC_IDX_STATUS;
	public static boolean DEBUG_TOUCHSCREEN = false;
	/**在新项目中虚拟Dragged事件*/
	public static boolean EnableDragged = false;
	/**在新项目中touch事件*/
	public static boolean EnableTouh = false;
	/** 当有Touch事件的时候，转换后的事件的目标类 */
	public static javax.microedition.lcdui.Canvas TargetCanvas; 
	public static Vector R = new Vector(5);
	public static Vector BtnImage = new Vector(5);
	public static Vector KeyCodeAndStatus = new Vector(5);
	public static void SetScreenSize(int w,int h)
	{
		s_screenW = w;
		s_screenH = h;
	}
	public static int AddBtn(int scrW,int scrH,int[]rect,int align,Image Up,Image Down,int retCode)
	{
		if ((align & Graphics.RIGHT)!=0) rect[0] = scrW - rect[2];
		
//#if PLATFORM=="J2ME"
		if ((align & Graphics.BOTTOM)!=0) rect[1] = scrH - rect[3];
		R.addElement(rect);
		BtnImage.addElement(new Image[] {Up,Down});
		KeyCodeAndStatus.addElement(new int[] {retCode,0});
//#else	
		if ((align & Graphics.BOTTOM)!=0) rect[1] = scrH - rect[1];
		R.add(rect);
		BtnImage.add(new Image[] {Up,Down});
		KeyCodeAndStatus.add(new int[] {retCode,0});
//#endif		
		return R.size() - 1;
	}
	
	public static void ClearBtns()
	{
//#if PLATFORM=="J2ME"
		R.removeAllElements();
		BtnImage.removeAllElements();
		KeyCodeAndStatus.removeAllElements();
//#else
		R.clear();
		BtnImage.clear();
		KeyCodeAndStatus.clear();		
//#endif		
	}
	
	
	private static int s_screenW;
	private static int s_screenH;
	public static void InitVirtualKeysForLRSK(int w,int h)
	{
 		InitVirtualKeys(w,h,SCENERIO_LR);
	}
	
	public final static int DirW = 30;
	public final static int DirH = 30;
	public final static int CenterW = 20;
	public final static int CenterH = 20;
	public final static int OffW	= 25;
	public final static int fireW = 30;
	public final static int fireH = 30;
	public final static int SoftW = 30;
	public final static int SoftH = 20;
	public final static int fireOffX = 5;
	public final static int fireOffY = 5;
  	
	
	
	public static final String[] BtnFace = new String[]{
		"5",
		"l",		//10
		"r",		
		"a",
		"b",
		"c",
		"d",
	};
	
	/** ABCD按键定义的字段定义 */
	public static final int ID_X		= 0;
	public static final int ID_Y		= 1 + ID_X;
	public static final int ID_W		= 1 + ID_Y;
	public static final int ID_H		= 1 + ID_W;
	public static final int ID_ALIGN	= 1 + ID_H;
	public static final int ID_KEYVALUE = 1 + ID_ALIGN;
	public static final int ID_PNG		= 1 + ID_KEYVALUE;
	public static final int ID_MAX		= 1 + ID_PNG;
//#if PLATFORM=="Android"		
	/** 所有按键方案索引 */
	public static final int SCENERIO_LR		= 0;
	public static final int SCENERIO_ABCD	= 1 + SCENERIO_LR;
	public static final int SCENERIO_FULL	= 1 + SCENERIO_ABCD;
	/** 所有按键方案,按键位置 */
	public static int[][][] BUTTONS = new int[][][] {
	
		{
			/*L*/	{0,											0,										SoftW,	SoftH,	Graphics.LEFT|Graphics.BOTTOM,	DevConfig.KEY_LSK,	1},
			/*R*/	{SoftW,										0,										SoftW,	SoftH,	Graphics.RIGHT|Graphics.BOTTOM,	DevConfig.KEY_RSK,  2},
		},
		{
			/*A*/	{(fireW>>1) + fireW*2 + (fireW*2/3),		fireH + (fireH*2/3),	fireW,	fireH,	Graphics.RIGHT|Graphics.BOTTOM,	Canvas.KEY_NUM5,	3},
			/*B*/	{(fireW>>1) + fireW,						fireH + (fireH*2/3),	fireW,	fireH,	Graphics.RIGHT|Graphics.BOTTOM,	Canvas.KEY_NUM0,	4},
			/*C*/	{(fireW>>1) + fireW + (fireW*2/3)+(fireW/6),fireH*2 + (fireH/3),	fireW,	fireH,	Graphics.RIGHT|Graphics.BOTTOM,	Canvas.KEY_STAR,	5},
			/*D*/	{(fireW>>1) + fireW + (fireW*2/3)+(fireW/6),fireH*1,				fireW,	fireH,	Graphics.RIGHT|Graphics.BOTTOM,	Canvas.KEY_POUND,	6},
		},
		/** 全键盘方案 */
        {
			//正方向优先 
			/*2*/	{OffW + (DirW+(CenterW>>1) - (DirW>>1) ),	DirH*2 + (CenterH),						DirW,	DirH,	Graphics.LEFT|Graphics.BOTTOM,	Canvas.KEY_NUM2,	-1},
			/*4*/	{OffW,										DirH + (CenterH>>1) + (DirH>>1),		DirW,	DirH,	Graphics.LEFT|Graphics.BOTTOM,	Canvas.KEY_NUM4,	-1},
			/*6*/	{OffW + (DirW+(CenterW>>1) + (CenterW>>1) ),DirH + (CenterH>>1) + (DirH>>1),		DirW,	DirH,	Graphics.LEFT|Graphics.BOTTOM,	Canvas.KEY_NUM6,	-1},
			/*8*/	{OffW + (DirW+(CenterW>>1) - (DirW>>1) ),	DirH,									DirW,	DirH,	Graphics.LEFT|Graphics.BOTTOM,	Canvas.KEY_NUM8,	-1},
			//斜方向
			/*1*/	{OffW,										DirH*2 + (CenterH),						DirW,	DirH,	Graphics.LEFT|Graphics.BOTTOM,	Canvas.KEY_NUM1,	-1},
			/*3*/	{OffW + (DirW+(CenterW>>1) + (CenterW>>1)),	DirH*2 + (CenterH),						DirW,	DirH,	Graphics.LEFT|Graphics.BOTTOM,	Canvas.KEY_NUM3,	-1},
			/*7*/	{OffW,										DirH,									DirW,	DirH,	Graphics.LEFT|Graphics.BOTTOM,	Canvas.KEY_NUM7,	-1},
			/*9*/	{OffW + (DirW+(CenterW>>1) + (CenterW>>1)),	DirH,									DirW,	DirH,	Graphics.LEFT|Graphics.BOTTOM,	Canvas.KEY_NUM9,	-1},


			/*5*/	{OffW + (DirW+(CenterW>>1)),				DirH + (CenterH>>1),					1,		1,		Graphics.LEFT|Graphics.BOTTOM,	Canvas.KEY_NUM5,	0},
			
			/*L*///	{0,			fireH*4,	fireW*2,fireH,	Graphics.LEFT|Graphics.BOTTOM,	GLLibConfig.keycodeLeftSoftkey,	1},
			/*R*///	{fireW*2,	fireH*4,	fireW*2,fireH,	Graphics.RIGHT|Graphics.BOTTOM,	GLLibConfig.keycodeRightSoftkey,2},
			/*A*///	{(fireW>>1) + fireW*2 + (fireW*2/3),		fireH + (fireH*2/3),	fireW,	fireH,	Graphics.RIGHT|Graphics.BOTTOM,	Canvas.KEY_NUM5,	3},
			/*B*///	{(fireW>>1) + fireW,						fireH + (fireH*2/3),	fireW,	fireH,	Graphics.RIGHT|Graphics.BOTTOM,	Canvas.KEY_NUM0,	4},
			/*C*///	{(fireW>>1) + fireW + (fireW*2/3)+(fireW/6),fireH*2 + (fireH/3),	fireW,	fireH,	Graphics.RIGHT|Graphics.BOTTOM,	Canvas.KEY_STAR,	5},
			/*D*///	{(fireW>>1) + fireW + (fireW*2/3)+(fireW/6),fireH*1,				fireW,	fireH,	Graphics.RIGHT|Graphics.BOTTOM,	Canvas.KEY_POUND,	6},
        },   
	};
//#elif PLATFORM=="J2ME"
	/** 所有按键方案索引 */
	public static final int SCENERIO_LR		= 0;
	public static final int SCENERIO_ABCD	= 1 + SCENERIO_LR;
	public static final int SCENERIO_FULL	= 1 + SCENERIO_ABCD;
	/** 所有按键方案,按键位置 */
	public static int[][][] BUTTONS = new int[][][] {
		{
			/*L*/	{0,											0,										SoftW,	SoftH,	Graphics.LEFT|Graphics.BOTTOM,	DevConfig.KEY_LSK,	1},
			/*R*/	{SoftW,										0,										SoftW,	SoftH,	Graphics.RIGHT|Graphics.BOTTOM,	DevConfig.KEY_RSK,  2},
		},
		{
		},
		/** 全键盘方案 */
        {
			/*L*/	{0,											0,										SoftW,	SoftH,	Graphics.LEFT|Graphics.BOTTOM,	DevConfig.KEY_LSK,	1},
			/*R*/	{SoftW,										0,										SoftW,	SoftH,	Graphics.RIGHT|Graphics.BOTTOM,	DevConfig.KEY_RSK,  2},
       },   
	};	
//#endif	
	public static Image CreateImage(int idx,boolean isDown)
	{
		Image retv = null;
		if (idx>=0)
			{
			try {
				String name = BtnFace[idx];
				if (isDown)
					name+="1";
				retv = Image.createImage("/dpad/" + name + ".png");
			} catch (IOException e) {
			} 
			}
		return retv;
	}
	public static void InitVirtualKeys(int w,int h,int Idx)
	{
//#if ENABLE_SENSOR=="TRUE"		
		if (sensor==null)
		{
			sensor = new cSensor();
		}
//#endif		
			int[][] Scenerio = BUTTONS[Idx];
  			int[] def;
  			Image On,Off;
  			for (int i = Scenerio.length - 1;i>=0;i--)
  			{
  				def = Scenerio[i];
  				On = Off = null;
  				if (def[ID_PNG]>=0)
  					{
  					Off = CreateImage(def[ID_PNG],false);
  					On = CreateImage(def[ID_PNG],true);
  					};
  				cTouch.AddBtn(w,h,
  						new int[] {def[ID_X],def[ID_Y],def[ID_W],def[ID_H]},
  						def[ID_ALIGN],
  						Off,On,
  						def[ID_KEYVALUE]);  				
  			}
	}
	public static void InitVirtualABCD(int w,int h,int Idx,boolean[] ABCD)
	{
		int[][] Scenerio = BUTTONS[Idx];
			int[] def;
			Image On,Off;
			for (int i = Scenerio.length - 1;i>=0;i--)
			{
				if (!ABCD[i])continue;
				def = Scenerio[i];
				On = Off = null;
				if (def[ID_PNG]>=0)
					{
					Off = CreateImage(def[ID_PNG],false);
					On = CreateImage(def[ID_PNG],true);
					};
				cTouch.AddBtn(w,h,
						new int[] {def[ID_X],def[ID_Y],def[ID_W],def[ID_H]},
						def[ID_ALIGN],
						Off,On,
						def[ID_KEYVALUE]);  				
			}	
	}
  	public static void InitVirtualKeys(int W,int H,boolean [] ABCD)
  	{
  		//此套按键，用全屏幕
  		InitVirtualKeys(s_screenW,s_screenH,SCENERIO_FULL);
  		InitVirtualABCD(s_screenW,s_screenH,SCENERIO_ABCD,ABCD);
  		//这2个按键，用游戏的屏幕高宽
  		InitVirtualKeysForLRSK(W,H);
  	}
  	public static void paintVirtualKeysBG(Graphics g,int x,int y,int w,int h)
  	{
  		g.setClip(x,y,w,h);
  		g.setColor(0x001122);
  		g.fillRect(x,y,w,h);
  	}
	public static void paintVirtualKeys(Graphics g) {
		int i;
		int[] r;
		Image[] imgs;
		Image theImg;
		int[] keycode;
		for (i = R.size()- 1;i>=0;i--)
		{
			imgs = (Image[])BtnImage.elementAt(i);
			keycode = (int[])KeyCodeAndStatus.elementAt(i);
			theImg = imgs[keycode[1]];
			r = (int[])R.elementAt(i);
			if (theImg!=null)
				{
				g.drawImage(theImg,r[0]+(r[2]>>1),r[1]+(r[3]>>1), Graphics.HCENTER|Graphics.VCENTER);
				};
		if(false)
			{				
			g.setColor(0xFF0000);
			g.drawRect(r[0],r[1],r[2],r[3]);
			};
		}
	}

	public static int s_pressX  = -1;
	public static int s_pressY  = -1;
	public static int s_releaseX  = -1;
	public static int s_releaseY  = -1;
	public static int s_dragX   = -1;
	public static int s_dragY   = -1;
	
	public static void pointerDragged(int x, int y) 
	{
		if(EnableTouh){
			//#if PLATFORM="Android"
						TargetCanvas.pointerDragged(x,y);
			//#endif
						return;
					}
		s_dragX = x;
		s_dragY = y;
		
	}
	public static int[] LastPressed = {0,0,0,0,0,0,0,0,0};
	public static int PressedLen = 0;
	public static void pointerPressed(int x, int y) 
	{
		if(EnableTouh){
//#if PLATFORM=="Android"
			TargetCanvas.pointerPressed(x,y);
//#endif
			return;
		}
		s_releaseX = -1;
		s_releaseY = -1;
		s_pressX = x;
		s_pressY = y;
		s_dragX = -1;
		s_dragY = -1;
		if(EnableDragged){
			return;
		}
		if (TargetCanvas ==null) return;
		
		int i;
		int[] r;
		int[] keycode;
		for (i = R.size()- 1;i>=0;i--)
		{
			r = (int[])R.elementAt(i);
			keycode = (int[])KeyCodeAndStatus.elementAt(i);
			if (Math_PointInRect(x, y, r)) {
				keycode[KC_IDX_STATUS] = 1;
//#if PLATFORM=="J2ME"
				GameMIDlet.keyPressed(keycode[KC_IDX_KEYCODE]);
//#else
				TargetCanvas.keyPressed(keycode[KC_IDX_KEYCODE]);
//#endif				
				LastPressed[PressedLen++] = keycode[KC_IDX_KEYCODE];
				//LastPressed[0] = 1;
				//System.out.println("PressedLen:"+PressedLen);
				break;
			}		
		}
//		for (int j = PressedLen;j>=0;j--){
//			System.out.println("LastPressed:"+j+"."+LastPressed[j]);
//		}
	}
	public static boolean IsRectPressed(int retCode)
	{
		for (int i = KeyCodeAndStatus.size()- 1;i>=0;i--)
		{
			int[] keycode = (int[])KeyCodeAndStatus.elementAt(i);

			if(keycode[KC_IDX_KEYCODE] == retCode)
			{
				return keycode[KC_IDX_STATUS] == 1;
			}
			
		}
		return false;
	}
	public static boolean IsRectReleased(int retCode)
	{
		for (int i = KeyCodeAndStatus.size()- 1;i>=0;i--)
		{
			int[] keycode = (int[])KeyCodeAndStatus.elementAt(i);

			if(keycode[KC_IDX_KEYCODE] == retCode)
			{
				return keycode[KC_IDX_STATUS] == 0;
			}
			
		}
		return false;
	}
	/**拖拽事件测试*/
	public static boolean VirtualDragged(int x,int y){
	
			if(cMath.Math_Abs(s_pressX-x)>=20 || cMath.Math_Abs(s_pressY-y)>=20){
				if(s_pressX <x){
	//#if PLATFORM=="J2ME"
					GameMIDlet.keyReleased(DevConfig.KEY_UP);
	//#else					
					TargetCanvas.keyReleased(DevConfig.KEY_UP);
	//#endif
				}else if(s_pressX >x){
	//#if PLATFORM=="J2ME"
					GameMIDlet.keyReleased(DevConfig.KEY_DOWN);
	//#else					
					TargetCanvas.keyReleased(DevConfig.KEY_DOWN);
	//#endif
				}
				
			}else{
				int i;
				int[] r;
				int[] keycode;
				for (i = R.size()- 1;i>=0;i--)
				{
					r = (int[])R.elementAt(i);
					keycode = (int[])KeyCodeAndStatus.elementAt(i);
					if (Math_PointInRect(x, y, r)) {
//						keycode[KC_IDX_STATUS] = 1;
		//#if PLATFORM=="J2ME"
						GameMIDlet.keyReleased(keycode[KC_IDX_KEYCODE]);
		//#else
						TargetCanvas.keyReleased(keycode[KC_IDX_KEYCODE]);
		//#endif				
//						LastPressed[PressedLen++] = keycode[KC_IDX_KEYCODE];
						//LastPressed[0] = 1;
						//System.out.println("PressedLen:"+PressedLen);
						break;
					}		
				}
			}
			return true;
	}
	public static void pointerReleased(int x, int y) {
		if(EnableTouh){
			//#if PLATFORM=="Android"
						TargetCanvas.pointerReleased(x,y);
			//#endif
						return;
					}
		if(EnableDragged){
			VirtualDragged(x,y);
			return;
		}
		s_pressX = -1;
		s_pressY = -1;
		s_dragX = -1;
		s_dragY = -1;
		s_releaseX = x;
		s_releaseY = y;
		
		if (TargetCanvas ==null) return;
		int[] keycode;
		int i;
		{
			for (i = R.size()- 1;i>=0;i--)
			{
				keycode = (int[])KeyCodeAndStatus.elementAt(i);
				if (keycode[KC_IDX_STATUS]!=0)
					{
//#if PLATFORM=="J2ME"
				GameMIDlet.keyReleased(keycode[KC_IDX_KEYCODE]);
//#else					
				TargetCanvas.keyReleased(keycode[KC_IDX_KEYCODE]);
//#endif		
				if(R.size() == 0 || R.size()<=i)
					return;
				keycode[KC_IDX_STATUS] = 0;
					};
			}
			PressedLen = 0;
			
		}
	}
	
	public static boolean Math_Rect2PointXYWHIntersect (int Ax0, int Ay0, int Aw0, int Ah0, int Bx0, int By0, int Bw0, int Bh0)
	{
		int Ax1 = Ax0 + Aw0;
		int Ay1 = Ay0 + Ah0;
		int Bx1 = Bx0 + Bw0;
		int By1 = By0 + Bh0;
		
		if(!(Ax0 <= Ax1))return false;
		if(!(Ay0 <= Ay1))return false;
		if(!(Bx0 <= Bx1))return false;
		if(!(By0 <= By1))return false;

		if (Ax1 < Bx0)	return false;
		if (Ax0 > Bx1)	return false;
		if (Ay1 < By0)	return false;
		if (Ay0 > By1)	return false;
		return true;
	}	
	public static boolean Math_PointInRect(int x, int y, int[] rect) {
		return Math_Rect2PointXYWHIntersect(x,y,1,1,rect[0],rect[1],rect[2],rect[3]);
	}	
}
