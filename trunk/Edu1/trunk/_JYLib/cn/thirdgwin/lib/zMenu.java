package cn.thirdgwin.lib;
import java.util.Hashtable;
import java.util.Vector;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import javax.microedition.midlet.MIDlet;
//#if PLATFORM =="Android"
import android.app.AlertDialog;
import android.content.DialogInterface;
//#endif
import cn.thirdgwin.io.zByteArrayStream;

/**
 * 本菜单采用类Windows的菜单系统的数据结构
 * 菜单定义存在于menu.ini中,该定义被装入后做为字典存在。使用菜单，必须先创建
 * 使用方法：
 *  1.Create()
 *  2.Show()
 * 潜在问题：
 * 	当前的数据结构，在创建菜单的时候，存在扫描操作，而且菜单项目越多，扫描更长
 * 缺陷:
 * 为避免过于复杂的计算,子菜单,不能反绕
 *  * 分工: 桂军
 *
 */
public abstract class zMenu  {
	/** 是否允许菜单反绕 */
	public static boolean ALLOW_REWIND	= false;
	/** 菜单项行距 */
	public static int LINE_HEIGHT = 20;
	/** 0.5个行距*/
	static final int HALF_LINE_HEIGHT = LINE_HEIGHT>>1;
	/** 背景框与菜单Y方向间隔1个行距*/
	public static int BG_FRAME_TO_ITEM_HEIGHT = LINE_HEIGHT;
	/** 背景框与菜单X方向间隔1个行距*/
//	static final int BG_FRAME_TO_ITEM_WIDTH = LINE_HEIGHT;
	/** 背景框增加2个行距大小*/
	static final int BG_FRAME_WIDTH_ADD = LINE_HEIGHT<<1;

	/** 可见菜单项行数 */
	private int m_visibleLineNB = 1;
	
	/** 有效菜单项个数*/
	private int m_enabledItemNum;
	
	private boolean m_NeedDrawTitle = false;
	//背景ID
	private int m_BG_ID		= -1;
	private int m_titleText = -1;
	static final int SPRITE_MAX = 3;

	/** 菜单类型 */
	private int m_type;
	/** 菜单数据，Vector中保存着数组 */
	private Vector m_items;
	private int m_itemsCount;
	public int m_focusIdx;
	private int m_firstVisibleIdx;
	/** 根索引*/
	protected int m_rootIdx;
	protected int m_rootID;
	
	public static final int COLOR_NORMAL = 0x00FF00;
	public static final int COLOR_GRAYED  = 0x000000;
	public static final int COLOR_FOCUSED = 0xFF0000;
	
	public static final int DRAW_FOCUS	= 1;
	public static final int DRAW_NORMAL	= 1 + DRAW_FOCUS;
	public static final int DRAW_GRAY	= 1 + DRAW_NORMAL;
	/** 菜单状态堆栈 */
	private int[] m_menustack;
	private int[] m_titlestack;
	private int[] m_rootIdxstack;
	private int[] m_bgidstack;
	
	protected int m_stackTop = 0;
	public boolean m_visible = false;
	
	abstract public String OnGetText(int idx);
	abstract public int[][] OnGetDefines();
	abstract public int[] OnGetDrawRegion();
	abstract public boolean OnItemSelected(int[] Itemline);
	public abstract void OnBeforeDraw(Graphics g);
	abstract public void OnDrawBackground(Graphics g,int bg_id);
	abstract public void OnDrawCursor(Graphics g,int x,int y,int w,int h);
	abstract public void OnDrawItem(Graphics g,int[] curItem,int drawtype, int x,int y,int w,int h);
	abstract public void OnDrawTitle(Graphics g, String text,int[] R);
	abstract public void OnFocusChange(int[] oldfocus,int[] newfocus);
	abstract public void OnMenuStackChange(int m_stackTop);
	abstract public void OnFinalize();
	abstract public void OnInit();
	abstract public boolean OnClose();
	//#if PLATFORM="Android"
	abstract public void OnDistoryApp();
	//#endif
	
	public int[] GetItemData(int idx)
	{
		return (int[]) m_items.elementAt(idx);
	}
	/** 强制让子类在事件代码中去处理，规范流程 */
	public final void Init()
	{		
		if (Inited) return;
		m_menustack= new int[zMenu.ZMENU_MAX_STACK];
		m_titlestack= new int[zMenu.ZMENU_MAX_STACK];
		m_bgidstack = new int[zMenu.ZMENU_MAX_STACK];
		m_rootIdxstack = new int[zMenu.ZMENU_MAX_STACK];
		m_items = new Vector();
		m_visibleLineNB = 6;
		if(m_visibleLineNB < 1)
			m_visibleLineNB = 1;
		/** 这应在最后调用 */
		OnInit();
		Inited = true;
	};
	/** 强制让子类在事件代码中去处理，规范流程 */
	public final void Finalize(){
		/** 这应在最前调用 */
		OnFinalize();
		m_firstVisibleIdx = 0;
		m_focusIdx = 0;
		m_stackTop = 0;
		{
			if (m_items!=null)
				m_items.removeAllElements();
			m_itemsCount = 0;
			m_titleText = -1;
			m_rootIdx = -1;
			m_rootID = -1;
		}
		Inited = false;
	}
	/** 压入当前状态,并触发事件OnMenuStackChange */
	private void Pushstate()
	{
		if (m_stackTop<zMenu.ZMENU_MAX_STACK - 1)
		{
			m_menustack[m_stackTop] =  ((m_focusIdx<<16) + m_firstVisibleIdx);
			m_titlestack[m_stackTop] = m_titleText;
			m_bgidstack[m_stackTop] = m_BG_ID;
			m_rootIdxstack[m_stackTop] = m_rootID;

			m_stackTop++;
		}
		OnMenuStackChange(m_stackTop);
	}
	/** 弹出当前状态,并触发事件OnMenuStackChange */
	private void Popstate()
	{
		if (m_stackTop>0)
		{
			m_stackTop--;
			m_titleText = m_titlestack[m_stackTop];
			m_rootID = 	m_rootIdxstack[m_stackTop];
			m_focusIdx = (m_menustack[m_stackTop]>>16);
			m_firstVisibleIdx = (m_menustack[m_stackTop]&0xFFFF);
			m_BG_ID = m_bgidstack[m_stackTop];
			SetCurrentMenuItemNum(m_firstVisibleIdx);
		}
		else 
		{
			if (m_type == zMenu.TYPE_STATIC_POPUP)
			{
				this.Close();
			}
		}
		OnMenuStackChange(m_stackTop);
	}
	public zMenu()
	{

	}
	/** 确保(y->y+lineHeight的区域可见，否则调整可见项索引 */
	private void MakeLineVisible(int y,int lineHeight)
	{
		int[] m_drawRect = OnGetDrawRegion();
		if (y<m_drawRect[1])
		{
			NextItem(-1,true,0);	//上一个可见项目
		} else if(y-lineHeight>m_drawRect[1] + m_drawRect[3])
		{
			NextItem(+1,true,0);	//下一个可见项目
		}
	}
	
	private int m_step = -1;
	/** 此函数用于切换到下一个条目，并触发事件(该事件用于切换条目的动画状态) */
	private boolean NextItemWithEvent(int step, boolean MoveVisibleIdx,int IndentStep) {
		int oldidx = m_focusIdx;
		boolean retV;
		if(retV = NextItem(step, MoveVisibleIdx, 0))
			OnFocusChange((int[]) (m_items.elementAt(oldidx)),(int[]) (m_items.elementAt(m_focusIdx)));
		return retV;
	}
	/**
	 * 移动游标.
	 * @param step	(-1上移,1下移,步长可以更大)
	 * @param MoveVisibleIdx (true移动第一个可见项的游标,false移动当前选中项的游标)
	 * @param IndentStep (-1 去上级菜单,0 同级,1去下级菜单)
	 */
	private boolean NextItem(int step, boolean MoveVisibleIdx,int IndentStep) {
		int newIdx;
		int LoopCount = 0;
		if (MoveVisibleIdx) {
			newIdx = m_firstVisibleIdx;
		} else {
			newIdx = m_focusIdx;
		}
		boolean FoundNew = false;	//是否找到适当的菜单项目
		Vector m_items = this.m_items;
		int[] newItem;
		int[] curItem = (int[]) (m_items.elementAt(newIdx));
		int curIndent = curItem[IDX_INDENT] + IndentStep;
		int m_itemsCount = this.m_itemsCount;
		while (LoopCount++< m_itemsCount) {
			newIdx += step;
			// 处理反绕
			if (ALLOW_REWIND)
			{
			if (newIdx < 0)
				newIdx += m_itemsCount;
			if (newIdx >= m_itemsCount)
				newIdx -= m_itemsCount;
			}
			else
			{
				if ((newIdx < 0) || (newIdx >= m_itemsCount))
					break;
				
			}
			newItem = (int[]) m_items.elementAt(newIdx);
			
			// 检查菜单项标记
			if ((newItem[zMenu.IDX_STATE] & (MI_STATE_DISABLED | MI_STATE_HIDDEN)) != 0) {
				int[] nextItem = (int[]) m_items.elementAt(newIdx + step);
				if(nextItem[IDX_INDENT] == curIndent)
					break;
				continue;
			}
			// 跳过子菜单
			if (newItem[IDX_INDENT] > curIndent) {
				continue;
			}
			// 已经到达第一个或者最后一个子菜单,为节约计算,子菜单不能反绕
			else if (newItem[IDX_INDENT] < curIndent) {
				break;
			}
			
			
			FoundNew = true;
			break;
		}

		if (FoundNew) {
			if (MoveVisibleIdx) {
				m_firstVisibleIdx = newIdx;
			} else {
				m_focusIdx = newIdx;
				if (IndentStep!=0){
					m_firstVisibleIdx = m_focusIdx;
				}
			}
		}
		return FoundNew;
	}
		/**
	 * 回到上级菜单
	 * */
	private final void BackToParent()
	{
			if (m_stackTop>0)
				Popstate();
			else
				Close();
	}
	/**
	 * 
	 */
	public boolean keyPressed(int keyCode) {
		boolean Handled = true;
		int oldidx;
		switch (keyCode)
		{
		case DevConfig.KEY_UP:
		case DevConfig.KEY_LEFT:
		case DevConfig.KEY_NUM2:
		case DevConfig.KEY_NUM4:
			m_step = -1;
			NextItemWithEvent(m_step, false, 0);
			break;
		case DevConfig.KEY_DOWN:
		case DevConfig.KEY_RIGHT:
		case DevConfig.KEY_NUM8:
		case DevConfig.KEY_NUM6:
			m_step = 1;
			NextItemWithEvent(m_step, false, 0);
			break;
//		case DevConfig.KEY_FIRE:
		case DevConfig.KEY_LSK:
//		case Canvas.KEY_NUM5:
			ItemSelected();
			break;
		case DevConfig.KEY_RSK:
			BackToParent();
			break;
		default:
			Handled = false;
		}
		return Handled;
	}
	
	/**
	 * 条目被选中
	 */
	private final void ItemSelected()
	{
		int[] Itemline = (int[])m_items.elementAt(m_focusIdx);
		int titleID = Itemline[zMenu.IDX_ID];
		int focusIdx = m_focusIdx;
		Pushstate();
		if (!NextItem(1,false,1))	//如果无法进入子菜单
		{
			Popstate();
			if (OnItemSelected((int[])m_items.elementAt(m_focusIdx)))
				Close();
		}
		else
		{
			Utils.Dbg("进入子菜单.");
			SetTitle(this.IndexOf(titleID));
			SetCurrentMenuItemNum(focusIdx);
		}
			
	}

	
	public final void Show()
	{
		if (m_visible)return;
		m_visible = true;
		int i;
		int[] theItem;
		Vector m_items = this.m_items;
		for(i = m_itemsCount - 1; i>=0; i--)
		{
			theItem = (int[])m_items.elementAt(i);
			if ((theItem[zMenu.IDX_STATE]& zMenu.MI_STATE_ENABLE_IF_HAS_MUSIC)!=0)
			{
					theItem[zMenu.IDX_STATE] |=zMenu.MI_STATE_DISABLED;
			}
			if ((theItem[zMenu.IDX_STATE]& zMenu.MI_STATE_ENABLE_IF_HAS_SOUND)!=0)
			{
					theItem[zMenu.IDX_STATE] |=zMenu.MI_STATE_DISABLED;
			}
			if ((theItem[zMenu.IDX_STATE]& zMenu.MI_STATE_ENABLE_IF_HAS_VIBRATION)!=0)
			{
					theItem[zMenu.IDX_STATE] |=zMenu.MI_STATE_DISABLED;
			}	
			if ((theItem[zMenu.IDX_STATE]& zMenu.MI_STATE_ENABLE_IF_HAS_RMS)!=0)
			{
				Utils.Dbg("MI_STATE_ENABLE_IF_HAS_RMS 尚未实施");
			}			
		}
	}
	public final void Close()
	{
		if (OnClose())
			m_visible = false;
	}
	

	public void Draw(Graphics g) {
		int screenW = DevConfig.SCR_W;
		int screenH = DevConfig.SCR_H;
		GLLib.SetClip(0,0,screenW,screenH);
		int[] m_drawRect = OnGetDrawRegion();
		OnBeforeDraw(GLLib.g);
		OnDrawBackground(g,m_BG_ID);
		if(m_NeedDrawTitle)		
			OnDrawTitle(g,OnGetText(m_titleText),m_drawRect);
		int LoopCount = 0;
		int Save_CursorIdx = m_focusIdx;
		//只显示一行
		if(m_visibleLineNB <= 1)
			 m_firstVisibleIdx = m_focusIdx;
		
		int Save_FirstVisibleIdx = m_firstVisibleIdx;
		int X = m_drawRect[0];
		int Y = m_drawRect[1] + BG_FRAME_TO_ITEM_HEIGHT;
		int Save_FocusY = -1;
		int LineHeight = LINE_HEIGHT;
		//用FirstVisibleIdx作为起点
		m_focusIdx = m_firstVisibleIdx;
		int[] currentItem;
		Vector m_items = this.m_items;
		int FocusIndent = ((int[]) m_items.elementAt(m_focusIdx))[IDX_INDENT];
//		GLLib.SetClip(m_drawRect[0], m_drawRect[1], m_drawRect[2], m_drawRect[3]);
		int m_itemsCount = this.m_itemsCount;
		int drawType = DRAW_NORMAL;
		while (Y < m_drawRect[1] + m_drawRect[3] && LoopCount++<m_itemsCount) {
			// 取当前的菜单条目
			currentItem = (int[]) m_items.elementAt(m_focusIdx);
			if (m_focusIdx == Save_CursorIdx) {
				Save_FocusY = Y;
				drawType = DRAW_FOCUS;
			} else {
				drawType = DRAW_NORMAL;
			}
			//到上级的菜单项，结束
			if (currentItem[IDX_INDENT] < FocusIndent) {
				break;
			//到下级的菜单项，跳过
			} else if (currentItem[IDX_INDENT] > FocusIndent) {
				continue;
			}			
			OnDrawItem(g,currentItem,drawType, X,Y,m_drawRect[2],LineHeight);
			Y += LineHeight;
			// 如果已经是最后一个条目了,则退出
			if (!NextItem(1, false, 0))
				break;
			//又回到起点，结束
			if (m_focusIdx == m_firstVisibleIdx)
				break;
		}
		// 恢复
		m_focusIdx = Save_CursorIdx;
		m_firstVisibleIdx = Save_FirstVisibleIdx;
		// 检查Focus的Item,如果没画出来，则调整可见的第一个条目
		if (Save_FocusY >= 0)
			MakeLineVisible(Save_FocusY, LineHeight);
		else {
			NextItem(m_step, true, 0);
		}
	}
	public final void InsertItem(int idx,int[] itemdef)
	{
		m_items.insertElementAt(itemdef,idx);
		m_itemsCount ++;
	}
	public final void AppendItem(int[] itemdef)
	{
		InsertItem(m_itemsCount,itemdef);
	}
	public final void RemoveItem(int idx)
	{
		m_items.removeElementAt(idx);
		m_itemsCount = m_items.size();
	}
	
	public void SetMenu(int rootID) {
		
		 m_rootIdx = IndexOf(rootID);
		if (m_rootIdx < 0) {
			if (DevConfig.ENABLE_ERR_INFO) {
				Utils.Err("错误的菜单ID " + rootID);
			}
			return;
		}
		 m_rootID = rootID;
		int[][] Defines = OnGetDefines();
		int lend = Defines.length;
		int rootindent = Defines[m_rootIdx][IDX_INDENT];
		int[] newItem;
		{// 找到rootID
			for (int i = m_rootIdx + 1; i < lend; i++) {
				newItem = Defines[i];
				if (newItem[IDX_INDENT] < rootindent)
					break;
				AppendItem(newItem);
			}
		}
		SetTitle(m_rootIdx);
		SetCurrentMenuItemNum(0);
		{
			//为了触发这个事件，就这么整吧
			m_focusIdx = 0;
			NextItem(1,false,0);
			NextItemWithEvent(-1,false,0);
		}
	}
	/**
	 * 设置菜单的指定索引的携带变量。
	 * @param index
	 * @param var1
	 * @param var2
	 */
	public void SetVariable(int index, int var1, int var2) {
		int[] item = (int[])m_items.elementAt(index);
		item[IDX_V1] = var1;
		item[IDX_V2] = var2;
	}
	
	/**
	 * 标题设置
	 * @param rootIdx
	 */
	private void SetTitle(int rootIdx)
	{
		int[] def = OnGetDefines()[rootIdx];
		m_rootID = def[zMenu.IDX_ID];
		m_titleText = def[zMenu.IDX_TEXT];
		m_BG_ID = def[zMenu.IDX_BG];
		if (m_NeedDrawTitle)
			m_NeedDrawTitle = (m_titleText >= 0);
	}
	
	protected void HideTitle()
	{
		m_NeedDrawTitle = false;
	}
	
	
	/**
	 * 设置当前菜单的有效菜单项个数
	 * @param rootIdx
	 * @return
	 */
	public void SetCurrentMenuItemNum(int rootIdx)
	{
		m_enabledItemNum = 0;
		if(m_items == null)
			return;			
		int[] rootItem = (int[]) m_items.elementAt(rootIdx);	
		int[] currentItem;
		int rootIndent = rootItem[IDX_INDENT];
		int currentIdx = rootIdx;
		int currentIndent = rootIndent;
		int m_itemsCount = this.m_itemsCount;
		while(currentIdx < m_itemsCount)
		{
			currentItem = (int[]) m_items.elementAt(currentIdx++);
			currentIndent = currentItem[IDX_INDENT];
			
			// 检查菜单项标记
			if ((currentItem[zMenu.IDX_STATE] & (MI_STATE_DISABLED | MI_STATE_HIDDEN)) != 0) {
				//到上级的菜单项，结束
				if(rootIndent > currentIndent) 
					break;
				continue;
			}		
			//到上级的菜单项，结束
			if(rootIndent > currentIndent) 
				break;
			//统计同级菜单项
			else if (rootIndent == currentIndent) 
				m_enabledItemNum++;	
		}	
	}
	
	/**
	 * @param rootID 定义的菜单的id.
	 * @param type
	 * public static final int TYPE_SINGE_INSTANCE	= 0;//嵌入式菜单对象
	 * public static final int TYPE_STATIC_POPUP	= 1;//弹出式静态菜单
	 * @return
	 */
	
	public int GetLenth() {
		return m_items.size();
	}
	
	public int[] GetCurrentItem() {
		return (int[]) m_items.elementAt(m_focusIdx);
	}
	
	public int GetFocus() {
		return m_focusIdx;
	}
	
/*****************************************************************************\
 * 
 * 菜单数据定义\管理代码
 * 	
\*****************************************************************************/	
	/** MENU.INI列定义 */
	public static final int IDX_BG			= 0;
	public static final int IDX_SPRITE		= 1 + IDX_BG;
	public static final int IDX_ID			= 1 + IDX_SPRITE;
	public static final int IDX_INDENT		= 1 + IDX_ID;
	public static final int IDX_TEXT		= 1 + IDX_INDENT;
	public static final int IDX_STATE		= 1 + IDX_TEXT;
	public static final int IDX_CMD			= 1 + IDX_STATE;
	public static final int IDX_V1			= 1 + IDX_CMD;
	public static final int IDX_V2			= 1 + IDX_V1; 
	public static final int IDX_X			= 1 + IDX_V2;
	public static final int IDX_Y			= 1 + IDX_X;
	public static final int IDX_W			= 1 + IDX_Y;
	public static final int IDX_H			= 1 + IDX_W;
	public static final int IDX_MAX			= 1 + IDX_H;
	/** 菜单类型常数 */
	public static final int TYPE_SINGE_INSTANCE	= 0;//嵌入式菜单对象
	public static final int TYPE_STATIC_POPUP	= 1;//弹出式静态菜单
	
	/** 菜单条目的状态 */
	public static final int MI_STATE_NORMAL					= 0;		//1
	public static final int MI_STATE_HIDDEN					= (1<<0);	//1
	public static final int MI_STATE_DISABLED				= (1<<1);	//2
	public static final int MI_STATE_ENABLE_IF_HAS_MUSIC	= (1<<2);	//4
	public static final int MI_STATE_ENABLE_IF_HAS_SOUND	= (1<<3);	//8
	public static final int MI_STATE_ENABLE_IF_HAS_VIBRATION= (1<<4);	//16
	public static final int MI_STATE_ENABLE_IF_HAS_RMS		= (1<<5);	//32
	public static final int MI_STATE_CLOSE_MENU				= (1<<6);	//64
	public static final int MI_STATE_HIDE_SPR				= (1<<7);	//128
	public static final int MI_STATE_HIDE_TEXT				= (1<<8);	//256
	/** 菜单定义 */
	protected  boolean Inited;
	public static final int ZMENU_MAX_STACK = 5;
	
	private int IndexOf(int rootID)
	{
		int[][] Defines = OnGetDefines();
		int lend = Defines.length;
		for (int i = 0;i<lend;i++)
		{
			if (Defines[i][IDX_ID]==rootID)
				return i;
		}
		return -1;
	}
//#if PLATFORM =="Android"
	protected void quitConfirm(MIDlet midlet){
	  AlertDialog.Builder asd = new AlertDialog.Builder(midlet);
	  asd.setTitle("提示") ; 
	  asd.setMessage("确认退出游戏吗?");
	  asd.setNegativeButton("否",new DialogInterface.OnClickListener() { 
	  public void onClick(DialogInterface dialog, int which) { 
	  //here
	  } 
	  }) ;
	  asd.setPositiveButton("是",new DialogInterface.OnClickListener() { 
	  public void onClick(DialogInterface dialog, int whichButton) {
	  //here
	  	OnDistoryApp();
	  } 
	  });
	  asd.show();
	  }
	//#endif
}
