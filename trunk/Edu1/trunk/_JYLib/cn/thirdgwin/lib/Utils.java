package cn.thirdgwin.lib;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

import javax.microedition.lcdui.Image;

import cn.thirdgwin.app.GameMIDlet;

public class Utils {
	public static Class theClass = (new Integer(0)).getClass();
	public static final InputStream GetResourceAsStream(String s)
	{
		//#if PLATFORM=="Android"
		s = "/assets" + s;	
//#endif
		return getResourceAsStream(s);
	}
	public static final InputStream getResourceAsStream(String s) {
		return GameMIDlet.GetInstance().getClass().getResourceAsStream(s);
	}
	public static final void Dbg_CallStack(boolean bThrow,String info) throws Exception
	{
//#if _DEBUG=="TRUE"		
		Exception e = new Exception(info);
		e.printStackTrace();
		if (bThrow)
			throw e;
//#endif		
	}
	public static final void DBG_PrintStackTrace(boolean b, String string) {
//#if _DEBUG=="TRUE"		
		DBG_PrintStackTrace(b,string);
//#endif
	}
	public static final void DBG_PrintStackTrace(String string) {
		//#if _DEBUG=="TRUE"		
				DBG_PrintStackTrace(true,string);
		//#endif
			}
	
	
		public static int[] Math_QuickSortIndices(int[] data, int nbItemPerValue,
			int itemNb, int left, int right) {
		if (false) {
			int len = right - left;
			int[] retv = new int[len];
			for (int i = 0; i < len; i++)
				retv[i] = i;
			return retv;
		}
		else
		{
			return cMath.Math_QuickSortIndices(data,nbItemPerValue,itemNb,left,right);
		}
	}
	/*************************************************************************
	 * Debug相关函数
	 * @param info
	 *************************************************************************/
	public static final void Dbg(String info)
	{
		if (DevConfig.ENABLE_DBG_INFO)
		{
			System.out.println("NO " +GLLib.s_game_currentFrameNB + " " + System.currentTimeMillis()+ " Dbg: " + info);
		}
	}
	public static final void Err(String Info)
	{
		if(DevConfig.ENABLE_ERR_INFO)
		{
			System.out.println("NO " + GLLib.s_game_currentFrameNB + " " + System.currentTimeMillis()+ " Err: " + Info);
		};
	}
	/*************************************************************************
	 * 删除文字两头的引号啥的
	 * @param str
	 *************************************************************************/	
	public static final String removeQuotes(String str) {
		String newStr = str.trim();
		int len = newStr.length();
		return newStr.substring(1, len - 1);
	}
	/*************************************************************************
	 * 释放内存、并出让一次CPU时间
	 * @param ShouldGC
	 * @param ms
	 *************************************************************************/	
	public final static void FreeCPU(boolean ShouldGC,int ms)
	{
		try {
			if(ShouldGC)GLLib.Gc();
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public final static String CombinStringInt(String s, int i) {
		StringBuffer sb = new StringBuffer(s);
		return (sb.append(i)).toString();
	}
	
	/**
	 * 载入文件，返回数组。
	 * 
	 * @param resName
	 * @return
	 */
	public static final byte[] GetBin(String resName) {
		byte[] data = null;
		try {
			InputStream is = GetResourceAsStream(resName);
			if(is==null)
				{	
				Utils.Dbg("Error while accessing " + resName);				
				return null;
				};
			byte[] buffer = new byte[Utils.STREAM_BUFFER_SIZE];
			int readcount;
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			while((readcount=is.read(buffer))>0)
			{
				bos.write(buffer,0,readcount);
			}
			is.close();
			is = null;
			buffer = null;
			data = bos.toByteArray();
			bos.close();
			bos = null;
			}catch(Exception e){
				Err("Can't Open " + resName );
		}
		return data;
	}
	/** 流读取缓冲 */
	public static final int STREAM_BUFFER_SIZE 		= 64;
	/** cParam 对象池参数 */
	public static final int PARAM_POOL_SIZE 		= 200;
	/** cParam 对象池，是否开启 */
	public static final boolean USE_PARAM_POOL 			= true;
	/** ZINI 最大的字段数量 */
	public static final int ZINI_MAX_FIELD_COUNT = 64;
	
	
	//////////////////////BOX&BAR/////////////////////////////
	/**
	 * 用指定sprite的4条边，4个角和底板的frame，绘制矩形框。
	 * 其中xLineLen和yLineLen需要从外面传入，所有这两个参数要在使用前计算好。
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 * @param boxViewSprite
	 * @param xLineLen
	 * @param yLineLen
	 * @param leftLineFrame
	 * @param rightLineFrame
	 * @param topLineFrame
	 * @param bottomLineFrame
	 * @param cornerLeftTopFrame
	 * @param cornerRightTopFrame
	 * @param cornerLeftBottomFrame
	 * @param cornerRightBottomFrame
	 * @param floorFrame
	 * @param corlor
	 * @param useAlpha
	 */
	public static void DrawBox(int x, int y, int w, int h,
			zSprite boxViewSprite, int xLineLen, int yLineLen,
			int leftLineFrame, int rightLineFrame, int topLineFrame,
			int bottomLineFrame, int cornerLeftTopFrame,
			int cornerRightTopFrame, int cornerLeftBottomFrame,
			int cornerRightBottomFrame, int floorFrame, int corlor,
			boolean useAlpha)
	{
		GLLib.SetClip(x, y, w, h);
		if(boxViewSprite == null)
			return;
		int xLineNum = 0;
		int yLineNum = 0;
		//背景填充,corlor为-1时不填充
		if(corlor != -1)
		{
			if(useAlpha) 
			{
				GLLib.AlphaRect_SetColor(corlor);
				GLLib.AlphaRect_Draw(GLLib.g, x, y, w, h);
				
			} 
			else 
			{
				GLLib.SetColor(corlor);
				GLLib.FillRect(x, y, w, h);
			}
		}
//		m_xLineNum = (w / m_xLineLen) + (w % m_xLineLen >0?1:0);
//		m_yLineNum = (h / m_yLineLen) + (h % m_yLineLen >0?1:0);
		xLineNum = w / xLineLen;
		yLineNum = h / yLineLen;
		//上下两行
		for(int i = 0; i < xLineNum; i++)
		{
			boxViewSprite.PaintFrame(topLineFrame, x + i * xLineLen, y, 0);
			boxViewSprite.PaintFrame(bottomLineFrame, x + i * xLineLen, y + h, 0);
		} 
		//整除后剩余宽度
		int remaindW = w % xLineLen;
		if(remaindW > 0)
		{
			boxViewSprite.PaintFrame(topLineFrame, x + w - xLineLen, y, 0);
			boxViewSprite.PaintFrame(bottomLineFrame, x + w - xLineLen, y + h, 0);
		}
		//左右两列
		for(int j = 0; j < yLineNum; j++)
		{
			boxViewSprite.PaintFrame(leftLineFrame, x, y + j * yLineLen, 0);
			boxViewSprite.PaintFrame(rightLineFrame, x + w, y + j * yLineLen, 0);
		}
		//整除后剩余高度
		int remaindH = h % yLineLen;
		if(remaindH > 0)
		{
			boxViewSprite.PaintFrame(leftLineFrame, x, y + h - yLineLen, 0);
			boxViewSprite.PaintFrame(rightLineFrame, x + w, y + h - yLineLen, 0);
		}
		
		GLLib.SetClip(0,0,GLLib.GetScreenWidth(), GLLib.GetScreenHeight());
		//4Corner
		if(cornerLeftTopFrame > 0)
			boxViewSprite.PaintFrame(cornerLeftTopFrame, x, y, 0);
		if(cornerRightTopFrame > 0)
			boxViewSprite.PaintFrame(cornerRightTopFrame, x + w, y, 0);
		if(cornerLeftBottomFrame > 0)
			boxViewSprite.PaintFrame(cornerLeftBottomFrame, x, y + h, 0);
		if(cornerRightBottomFrame > 0)
			boxViewSprite.PaintFrame(cornerRightBottomFrame, x + w, y + h, 0);
		
	}
	
	/**
	 * 用指定sprite的独立的左部，中部和右部的frame，绘制XX条。
	 * 其中leftFrameLen，midFrameLen和rightFrameLen需要从外面传入，所有这三个参数要在使用前计算好。
	 * @param x
	 * @param y
	 * @param w
	 * @param barViewSprite
	 * @param leftFrame
	 * @param midFrame
	 * @param rightFrame
	 * @param leftFrameLen
	 * @param midFrameLen
	 * @param rightFrameLen
	 */
	public static final void DrawBar(int x, int y, int w,zSprite barViewSprite,int leftFrame, int midFrame, int rightFrame,int leftFrameLen, int midFrameLen, int rightFrameLen)
	{
		if(barViewSprite == null)
			return;
		int midNum = (w - leftFrameLen - rightFrameLen) / midFrameLen;
		for(int i = 0; i < midNum; i++)
		{
			barViewSprite.PaintFrame(midFrame, x + leftFrameLen + i * midFrameLen, y, 0);
		}
		//整除后剩余宽度
		int remaindW = (w - leftFrameLen - rightFrameLen) % midFrameLen;
		if(remaindW > 0)
			barViewSprite.PaintFrame(midFrame, x + w - rightFrameLen - midFrameLen, y, 0);
		barViewSprite.PaintFrame(leftFrame, x, y, 0);
		barViewSprite.PaintFrame(rightFrame,  x + w - rightFrameLen, y, 0);
	
	}
	
	
}
