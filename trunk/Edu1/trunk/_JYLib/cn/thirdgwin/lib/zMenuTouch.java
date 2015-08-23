package cn.thirdgwin.lib;

import java.util.Vector;

import javax.microedition.lcdui.Graphics;
import javax.microedition.midlet.MIDlet;
//#if PLATFORM =="Android"
import android.app.AlertDialog;
import android.content.DialogInterface;
//#endif

/**
 *针对touch版本做出的menu
 *时间原因，没有做相应优化，注意在gameplay中做状态切换的时候会清除相应按键注册,造成menu无注册按键（无响应）
 *注意在相应位置加上MenuChange（）方法  注册相关按键
 *
 */
public abstract class zMenuTouch  {

	/** 菜单状态堆栈 */
	private int[] m_menustack;
	private int[] m_titlestack;
	private int[] m_rootIdxstack;
	private int[] m_bgidstack;
	protected int m_stackTop = 0;
	/**保存的序号*/
	protected int m_rootIdx;
	protected int m_rootID;
	protected int ThisItemsCount;
	/** 菜单数据，Vector中保存着数组 */
	private Vector m_items;
	private int m_itemsCount = 0;
	public int m_focusIdx;
	/**选中的idx*/
	public int m_selectIdx;
	private int m_firstVisibleIdx;
	private int IdxItemsCount = 0;
//背景ID
	private int m_BG_ID		= -1;
	
	//LRSK位置
	public int[] LRSKRect;
	
	/** 菜单项行距 */
	public static int LINE_HEIGHT = 20;
	public static int LINE_WIDTH = 20;
	
	public static int[] FrameRect; 
	
	public boolean m_visible = false;
	
	/**
	 * menu排列方式
	 */
	public static boolean menuModeV = true;
	
	
	abstract public void OnInit();
	abstract public int[][] OnGetDefines();
	abstract public int[] OnGetDrawRegion(int rootid);
	abstract public void OnDrawBackground(Graphics g,int bg_id);
	abstract public void OnDrawItem(Graphics g,int[] curItem,int x,int y);
	abstract public void OnMenuChange();
	abstract public boolean OnItemSelected(int[] Itemline);
	abstract public void OnDistoryApp();
	/**左右软件
	 * @param Isneed
	 */
	abstract public void OnDrawLRSK(boolean Isneed,Graphics g);
	
	
	public final void Init(){
		if (Inited){
			return;
		} 
		m_menustack= new int[ZMENU_MAX_STACK];
		m_titlestack= new int[ZMENU_MAX_STACK];
		m_bgidstack = new int[ZMENU_MAX_STACK];
		m_rootIdxstack = new int[ZMENU_MAX_STACK];
		m_items = new Vector();
		OnInit();
		Inited = true;
	}
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
	public final void InsertItem(int idx,int[] itemdef)
		{
			m_items.insertElementAt(itemdef,idx);
			m_itemsCount ++;
		}
	public final void AppendItem(int[] itemdef)
		{
			InsertItem(m_itemsCount,itemdef);
		}
	public final int GetIdxItemsCount(){
		int res = 1;
		int lend = m_items.size();
		int rootindent = ((int[]) m_items.elementAt(m_focusIdx))[IDX_INDENT];
		int[] newItem;
		for (int i = m_focusIdx + 1; i < lend; i++){
			newItem = ((int[]) m_items.elementAt(i));
			if (newItem[IDX_INDENT] < rootindent)
				break;
			if(newItem[IDX_INDENT] > rootindent)
				continue;
			res++;
		}
		return res;
	}
	public final void SetMenu(int rootID) {
		
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
		m_focusIdx = 0;
		SetTitle(m_rootIdx);
		
	}
	/**
	 * @param g
	 */
	public void Draw(Graphics g) {
		int screenW = DevConfig.SCR_W;
		int screenH = DevConfig.SCR_H;
		GLLib.SetClip(0,0,screenW,screenH);	
		int[] m_drawRect = OnGetDrawRegion(m_rootID);
//		OnBeforeDraw(GLLib.g);
		OnDrawBackground(g,m_BG_ID);
		int LoopCount = 0;
		int Lineoffset;
		int X;
		int Y;
		if(menuModeV){
			X = m_drawRect[0];
			//item平均分布,找到第一个Y点
			Lineoffset = m_drawRect[3]/ThisItemsCount;
			Y = m_drawRect[1]-((m_drawRect[3]-Lineoffset)>>1);
		}else{
			Y = m_drawRect[1];
			//item平均分布,找到第一个Y点
			Lineoffset = m_drawRect[2]/ThisItemsCount;
			X = m_drawRect[0]-((m_drawRect[2]-Lineoffset)>>1);
		}
		
//		int Save_FocusY = -1;
;
		//用FirstVisibleIdx作为起点
		m_firstVisibleIdx = m_focusIdx;
//		m_focusIdx = m_firstVisibleIdx;
		int[] currentItem;
		Vector m_items = this.m_items;
		int FocusIndent = ((int[]) m_items.elementAt(m_focusIdx))[IDX_INDENT];
		int Focuslrskdis = ((int[]) m_items.elementAt(m_focusIdx))[IDX_LRSKDIS];
//		GLLib.SetClip(m_drawRect[0], m_drawRect[1], m_drawRect[2], m_drawRect[3]);
		int m_itemsCount = this.m_itemsCount;
//		int drawType = DRAW_NORMAL;
		while (Y < m_drawRect[1] + (m_drawRect[3]>>1) && LoopCount++<m_itemsCount) {
			// 取当前的菜单条目
			currentItem = (int[]) m_items.elementAt(m_focusIdx);
//			if (m_focusIdx == Save_CursorIdx) {
//				Save_FocusY = Y;
//				drawType = DRAW_FOCUS;
//			} else {
//				drawType = DRAW_NORMAL;
//			}
			//到上级的菜单项，结束
			if (currentItem[IDX_INDENT] < FocusIndent) {
				break;
			//到下级的菜单项，跳过
			} else if (currentItem[IDX_INDENT] > FocusIndent) {
				continue;
			}			
			OnDrawItem(g,currentItem,X,Y);
			if(menuModeV)
				Y += Lineoffset;
			else
				X += Lineoffset;
			// 如果已经是最后一个条目了,则退出
			if (!NextItem(0))
				break;
			//又回到起点，结束
//			if (m_focusIdx == m_firstVisibleIdx)
//				break;
		}
		// 恢复
		m_focusIdx = m_firstVisibleIdx;
//		m_firstVisibleIdx = Save_FirstVisibleIdx;
		// 检查Focus的Item,如果没画出来，则调整可见的第一个条目
//		if (Save_FocusY >= 0)
//			MakeLineVisible(Save_FocusY, LineHeight);
//		else {
//			NextItem(m_step, true, 0);
//		}
		if(Focuslrskdis == LRSKTRUE)
		OnDrawLRSK(true,g);
	}
	/**
	 * 标题设置
	 * @param rootIdx
	 */
	private void SetTitle(int rootIdx)
	{
		int[] def = OnGetDefines()[rootIdx];
		m_rootID = def[zMenu.IDX_ID];
//		m_titleText = def[zMenu.IDX_TEXT];
		m_BG_ID = def[zMenu.IDX_BG];
//		if (m_NeedDrawTitle)
//			m_NeedDrawTitle = (m_titleText >= 0);
		MenuChange();
	}
	private boolean NextItem(int IndentStep){
		int newIdx;
		if(IndentStep == 0){
			newIdx = m_focusIdx;
		}else{
			newIdx = m_selectIdx;
		}
		int LoopCount = 0;
		boolean FoundNew = false;	//是否找到适当的菜单项目
		Vector m_items = this.m_items;
		int[] newItem;
		int[] curItem = (int[]) (m_items.elementAt(newIdx));
		int curIndent = curItem[IDX_INDENT] + IndentStep;
		int m_itemsCount = this.m_itemsCount;
		while (LoopCount++< m_itemsCount) {
//			newIdx ++;
			if (((++newIdx) < 0) || (newIdx >= m_itemsCount))
				break;
			newItem = (int[]) m_items.elementAt(newIdx);
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
				m_focusIdx = newIdx;
				if (IndentStep!=0){//暂留
					m_firstVisibleIdx = m_focusIdx;
				}
			}
		return FoundNew;
	}
	public final void FindSelectIdx(int keyCode){
		int thevalue = (keyCode^FINALKEYVAL);
		int lend = m_items.size();
		int[] newItem;
		int[] curItem = (int[]) (m_items.elementAt(m_focusIdx));
		int curIndent = curItem[IDX_INDENT];
		for (int i = m_focusIdx; i < lend; i++) {
			newItem = (int[]) (m_items.elementAt(i));
			if (newItem[IDX_INDENT] < curIndent)
				break;
			if((newItem[IDX_INDENT] > curIndent))
				continue;
			if((--thevalue) == 0){
				//选中要选的选项，做出相应改动
				m_selectIdx = i;//要点击的选项
				int titleID = ((int[]) (m_items.elementAt(m_selectIdx)))[zMenuTouch.IDX_ID];
				Pushstate();
				if (!NextItem(1))	//如果无法进入子菜单
				{
					Popstate();
					if (OnItemSelected((int[])m_items.elementAt(i))){
						return;
					}
//						Close();
					Utils.Dbg("无法进入子菜单.");
				}else{
					Utils.Dbg("进入子菜单.");
					SetTitle(this.IndexOf(titleID));
				}
				
			}
		}
		
	}
	public boolean keyPressed(int keyCode){
		//1<<7模拟按键，针对各种item，再模拟rsk和lsk
		switch(keyCode){
			case DevConfig.KEY_LSK:
				break;
			case DevConfig.KEY_RSK://返回
			case 4:
				BackToParent();
				break;
			default:
				FindSelectIdx(keyCode);
				break;
			
		}
		
		return false;
	}
	/** 压入当前状态,并触发事件OnMenuStackChange */
	private void Pushstate()
	{
		if (m_stackTop<zMenuTouch.ZMENU_MAX_STACK - 1)
		{
			m_menustack[m_stackTop] =  ((m_focusIdx<<16) + m_firstVisibleIdx);
//			m_titlestack[m_stackTop] = m_titleText;
			m_bgidstack[m_stackTop] = m_BG_ID;
			m_rootIdxstack[m_stackTop] = m_rootID;

			m_stackTop++;
		}
//		OnMenuStackChange(m_stackTop);
	}
	
	private void Popstate(){//暂时没整理
		if (m_stackTop>0)
			{
				m_stackTop--;
//				m_titleText = m_titlestack[m_stackTop];
				m_rootID = 	m_rootIdxstack[m_stackTop];
				m_focusIdx = (m_menustack[m_stackTop]>>16);
				m_firstVisibleIdx = (m_menustack[m_stackTop]&0xFFFF);
				m_BG_ID = m_bgidstack[m_stackTop];
//				SetCurrentMenuItemNum(m_firstVisibleIdx);
				MenuChange();
			}
	}
	public final static int FINALKEYVAL = 1<<7;
	public final void MenuChange(){
 		ThisItemsCount = GetIdxItemsCount();
		int[] m_drawRect = OnGetDrawRegion(m_rootID);
		//在此注册按钮，暂时注册具体位置按键
		cTouch.ClearBtns();
		/**注册按钮*/
		cTouch.AddBtn(DevConfig.SCR_W, DevConfig.SCR_H, new int[]{0,0,DevConfig.SCR_W,DevConfig.SCR_H}, 0, null, null, DevConfig.KEY_NUM0);//注册任意键
		if(menuModeV)
			for(int i = 0;i<ThisItemsCount;i++){
				//item平均分布,找到第一个Y点
				int LineHeight = m_drawRect[3]/ThisItemsCount;
				int X = m_drawRect[0]-(LINE_WIDTH>>1);
				int Y = m_drawRect[1]-((m_drawRect[3]-LineHeight)>>1)-(LINE_HEIGHT>>1)+LineHeight*i;
				cTouch.AddBtn(DevConfig.SCR_W, DevConfig.SCR_H, new int[]{X,Y,LINE_WIDTH,LINE_HEIGHT}, 0, null, null, FINALKEYVAL|(i+1));
			}
		else
			for(int i = 0;i<ThisItemsCount;i++){
				//item平均分布,找到第一个X点
				int LineWidth = m_drawRect[2]/ThisItemsCount;
				int Y = m_drawRect[1]-(LINE_HEIGHT>>1);
				int X = m_drawRect[0]-((m_drawRect[2]-LineWidth)>>1)-(LINE_WIDTH>>1)+LineWidth*i;
				cTouch.AddBtn(DevConfig.SCR_W, DevConfig.SCR_H, new int[]{X,Y,LINE_WIDTH,LINE_HEIGHT}, 0, null, null, FINALKEYVAL|(i+1));
			}
		/**注册右软件*/
		cTouch.AddBtn(DevConfig.SCR_W, DevConfig.SCR_H, new int[]{LRSKRect[0]-(LRSKRect[2]>>1),LRSKRect[1]-(LRSKRect[3]>>1),LRSKRect[2],LRSKRect[3]}, 0, null, null, DevConfig.KEY_RSK);	
		OnMenuChange();
	}
	public final void ChangeVisible(boolean sta){
		m_visible = sta;
		if(sta == true){
			MenuChange();
		}
	}
	/**
	 * 回到上级菜单
	 * */
	protected final void BackToParent()
	{
			if (m_stackTop>0)
				Popstate();
			else
				m_visible = false;
	}
	public void Finalize(){
		m_firstVisibleIdx = 0;
		m_focusIdx = 0;
		m_stackTop = 0;
		{
			if (m_items!=null)
				m_items.removeAllElements();
			m_itemsCount = 0;
			m_rootIdx = -1;
			m_rootID = -1;
		}
		IdxItemsCount = 0;
		Inited = false;
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
	/**
	 * 菜单数据定义，管理代码
	 */
	/** MENU.INI列定义 */
	public static final int IDX_BG			= 0;
	public static final int IDX_SPRITE		= 1 + IDX_BG;
	public static final int IDX_ID			= 1 + IDX_SPRITE;
	public static final int IDX_INDENT		= 1 + IDX_ID;
	public static final int IDX_DISPLAY		= 1 + IDX_INDENT;
	public static final int IDX_LRSKDIS		= 1 + IDX_DISPLAY;
	public static final int IDX_CMD		= 1 + IDX_LRSKDIS;
	public static final int IDX_CMD2		= 1 + IDX_CMD;
	public static final int ZMENU_MAX_STACK = 5;
	
	
	public static final int LRSKTRUE = 0;
	public static final int LRSKFALSE = 1;
	public static final int STATE_NORMAL = 0;
	public static final int STATE_HID = 1;
	protected  boolean Inited;
}