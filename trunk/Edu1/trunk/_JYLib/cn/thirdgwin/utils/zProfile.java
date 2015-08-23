package cn.thirdgwin.utils;
/** 性能测试类，用于测定次数和用时 */
import java.util.Hashtable;
import java.util.Vector;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

import cn.thirdgwin.lib.DevConfig;
import cn.thirdgwin.lib.GLLib;

/**
 * 
 * @author Guijun
 *
 */
public class zProfile {
	/** 字段定义 */
	/** 次数统计 */
	public final static int PF_COUNT	= 0;
	/** 开始时间 */
	public final static int PF_START	= 1 + PF_COUNT;
	/** 结束时间 */
	public final static int PF_END		= 1 + PF_START;
	/** 时间差异 */
	public final static int PF_DIFF		= 1 + PF_END;
	public final static int PF_MAXDIFF 	= 1 + PF_DIFF;
	public final static int PF_MAX		= 1 + PF_MAXDIFF;
	
	public static Hashtable Counters;	//计时器名称数组
	public static Vector CountersData;	// 计时器数组	
	public static Vector CountersName;	// 计时器名称数组	

	/** 显示 */
	private final static int  LINEHEIGHT	= 12;
	private final static int  LINEWIDTH	= 120;
	private static int	PageNo	= 0;
	private static int	LinesPerPage	= 10;
	private static Font font;
	private static boolean Visible = false;
	/** 作弊码系统 */
	private final static int CHEAT_PAGEUP	= 1;
	private final static int CHEAT_PAGEDOWN	= 2;
	private final static int CHEAT_RESET	= 3;
	private final static int CHEAT_SHOWHIDE	= 4;
	
	/** 作弊码列表，字段：指令/当前索引/按键序列 */

	private final static int[][] CheatCode = new int[][] {
		{CHEAT_PAGEUP,2,DevConfig.KEY_STAR,DevConfig.KEY_NUM7},
		{CHEAT_PAGEDOWN,2,DevConfig.KEY_STAR,DevConfig.KEY_NUM9},
		{CHEAT_RESET,2,DevConfig.KEY_STAR,DevConfig.KEY_DIEZ,DevConfig.KEY_STAR,DevConfig.KEY_DIEZ},
		{CHEAT_SHOWHIDE,2,DevConfig.KEY_STAR,DevConfig.KEY_DIEZ,DevConfig.KEY_DIEZ,DevConfig.KEY_STAR},
	};
	/** 提示字符串 */
	private final static String CheatCodeStr = "*#*#,*7,*9";
	
	
	public final static int AddCounter(String name)
	{
		Integer idx = (Integer)Counters.get(name);
		int id;
		if (idx==null)
		{
			long[] data = new long[PF_MAX];
			id = CountersData.size();
			CountersData.addElement(data);
			CountersName.addElement(name);
			Counters.put(name, new Integer(id));
		}
		else
		{
			id = idx.intValue();
		}
		return id;
	}
	
	public final static void Init(int linesperpage)
	{
		if (!DevConfig.ENABLE_PROFILE) return;
		CountersData = new Vector();
		CountersName = new Vector();
		Counters = new Hashtable();
		LinesPerPage = linesperpage;
	}
	public final static void Reset(boolean resetmax)
	{
		if (!DevConfig.ENABLE_PROFILE) return;
		long[] data;
		int j;
		for (int i = Counters.size()-1;i>=0;i-- )
		{
			data = (long[])CountersData.elementAt(i);
			for (j=PF_COUNT;j<=PF_DIFF;j++)
			{
				data[j] = 0;
			}
			if (resetmax)
				data[PF_MAXDIFF] = 0;
		}
	}
	/** 开始计时 */	
	public final static void Start(String name)
	{
		if (!DevConfig.ENABLE_PROFILE) return;
		long value = System.currentTimeMillis();
		int id = AddCounter(name);
		long[] Longs = (long[])CountersData.elementAt(id);
		Longs[PF_START] = value;
	}
	/** 开始计时 */	
	public final static void End(String name,int callcountInc)
	{
		if (!DevConfig.ENABLE_PROFILE) return;
		long value = System.currentTimeMillis();
		int id = AddCounter(name);
		long[] Longs = (long[])CountersData.elementAt(id);
		Longs[PF_END] = value;
		Longs[PF_DIFF] += Longs[PF_END] - Longs[PF_START];
		Longs[PF_COUNT]+=callcountInc;
	}
	/** 开始计时 */	
	public final static void IncCallCount(String name)
	{
		if (!DevConfig.ENABLE_PROFILE) return;
		Append(name,PF_COUNT,1);
	}	
	/** 追加计数器值 */
	public final static void Append(String name,int type,long val)
	{
		if (!DevConfig.ENABLE_PROFILE) return;
		int id = AddCounter(name);
		long[] Longs = (long[])CountersData.elementAt(id);
		Longs[type] +=val;

	}	
	public final static void Paint(Graphics g,int x,int y,int w)
	{
		if (!DevConfig.ENABLE_PROFILE) return;
		if (!Visible)return;
		long latest_lastused = 0;
		long new_lastused;
		int StartLine = LinesPerPage* PageNo;
		StringBuffer sb ;
		if(font==null)
			font = Font.getFont(Font.FACE_SYSTEM,Font.STYLE_PLAIN,Font.SIZE_SMALL);
		g.setFont( font);
		g.setColor(0xffffff);
		g.fillRect(x,y,w,(1+LinesPerPage) * LINEHEIGHT);
		g.setColor(0);
		int line = 1;
		int idx;
		int Max_Counter = CountersData.size();
		long[] data;
		String name;
		for(int i = CountersData.size() - 1;i>=0;i--)
		{
			data = (long[])CountersData.elementAt(i);
			
			latest_lastused = data[PF_MAXDIFF];
			new_lastused = data[PF_DIFF];
			if (new_lastused > latest_lastused)
				{
				data[PF_MAXDIFF] = new_lastused;
				}
		}
		for (int i = 0;i<LinesPerPage;i++)
		{
			idx = i + StartLine;
			if (idx>=Max_Counter)
			{
				break;
			}
			data = (long[])CountersData.elementAt(idx);
			name = (String)CountersName.elementAt(idx);
			sb = new StringBuffer();
			sb.append(name);
			sb.append(" ");
			sb.append(data[PF_COUNT]);
			sb.append(" ");
			sb.append(data[PF_DIFF]);
			sb.append(" ");
			sb.append(data[PF_MAXDIFF]);
				
			g.drawString(sb.toString(),x, y + (line) * LINEHEIGHT - 3, 0);
			line++;
		}
	}
	public static void CheckCheat(int translatedKeyCode) {
		if (!DevConfig.ENABLE_PROFILE) return;
		int i;
		int KeyIdx;
		for (i = CheatCode.length - 1;i >=0;i--)
		{
			if ((KeyIdx=CheatCode[i][1])<CheatCode[i].length)
			{
				if (CheatCode[i][KeyIdx]!=translatedKeyCode)
				{
					KeyIdx=CheatCode[i][1] = 2;
				}
				else
				{
					CheatCode[i][1]++;
				}
			};
			if (CheatCode[i][1]>=CheatCode[i].length)
			{
				
				CheatCheat_Do(CheatCode[i][0]);
				CheatCode[i][1] = 2;
			}			
		}
	}
	private static void CheatCheat_Do(int action)
	{
		if (!DevConfig.ENABLE_PROFILE) return;
		switch (action) 
		{
		case CHEAT_PAGEUP:
			PageNo--;
			if (PageNo<0)
			{
				PageNo =   CountersName.size() / LinesPerPage;
			}
			break;
		case CHEAT_PAGEDOWN:
			PageNo++;
			if (PageNo*LinesPerPage>=CountersName.size())
			{
				PageNo =  0;
			}
			break;
		case CHEAT_RESET:
			Reset(true);
			break;
		case CHEAT_SHOWHIDE:
			Visible  = !Visible;
			break;
		}
	}
}
