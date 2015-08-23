package cn.thirdgwin.app;


import java.io.UnsupportedEncodingException;

import javax.microedition.io.Connector;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreNotOpenException;
import javax.wireless.messaging.MessageConnection;
import javax.wireless.messaging.TextMessage;


class PayWrapper extends payWrapperSuper implements CommandListener
{
    public static final byte GS_SENDING = 0;// 发送中
    public static final byte GS_MAINMENU = 1;// 提示画面
    public static final byte GS_SENDFAIL = 27;// 发送失败
    public static final byte GS_USERCANCEL = 29;// 发送提示时用户取消
    public static final byte GS_SENDSUCCESS = 30;// 发送成功
    public static byte iCurrState = GS_MAINMENU;
    
    public static boolean m_isReBorn = false;
    private static boolean[] m_isPay;
    public static boolean m_isComplete = false;
    public static boolean m_isRunning = false;
    public static boolean m_isCancel = false;   
    public static String smsMsgPrice = "";

    //计费点
	public static final int PAY_POINT_STORY = 1;
	public static final int PAY_POINT_BUY_MONEY_NO_MONEY = 2;
	public static final int PAY_POINT_BUY_MONEY_IN_SHOP = 3;
	public static final int PAY_POINT_REBORN = 4;
	public static final int PAY_POINT_LEVEL_UP_5 = 5;
	public static final int PAY_POINT_AUTO_REVERT = 6;
	public static final int PAY_POINT_5_REDUCTION = 7;
	

    private int m_sendMsgAmount = 0;
    
    
    private Command cmdBack = new Command("\u8fd4\u56de", Command.BACK, 1); //返回按钮
    private Command cmdSend = new Command("\u70b9\u64ad", Command.ITEM, 1);  //点播 按钮
    private Command cmdSure = new Command("\u786e\u5b9a", Command.OK, 1);   // 确定 按钮
    private Command cmdRetry = new Command("\u91cd\u8bd5", Command.ITEM, 1);  // 重试 按钮
    private Command cmdPay = new Command("\u4ed8\u8d39", Command.ITEM, 1);  // 付费 按钮
    private Command cmdExit = new Command("\u9000\u51fa", Command.EXIT, 1);  //退出 按钮
    
    private Form sendFailForm = new Form("");
    private Form sendingForm = new Form("");
    private Form sendSuccessForm = new Form("");
    private Form exiteForm = new Form("");
    private Form sendFreeMsgForm = new Form("");

    private boolean m_isActivate = false;
    
    private String m_messageNum = "";   //上行号码
    private String m_messageContent = "";  //上行内容    
    private String m_messageUnitPrice = ""; //短信单价 可能可以不要
    private String m_promptMessage = ""; //收费提示信息
    
    public static int s_gameGold = 0;
    public static boolean s_payGameGold = false; 
    private static String m_RMSName = "";
    
    /**
	 * 当前是哪个计费点。
	 */
	private static int m_currentPayPoint = 0;
	/**
	 * 是否发送成功。
	 */
	private static boolean m_isSuccess = false;
	
	
	/**
	 * 计费点总数。
	 */
	private static int m_payPointCount;
//	private static int m_pointCount = 0;
    public PayWrapper(int Pay_Idx) {
        super("\u53d1\u9001\u77ed\u4fe1");    //form标题 发送短信
        m_currentPayPoint = Pay_Idx;  
		m_isComplete = false;
		m_isRunning = false;
		m_isCancel = false;	
//		m_hasPay = false;		
		m_RMSName = JYWrapper.INI.GetStr("PAY"+m_currentPayPoint, "RMS");//RMS
		smsMsgPrice = JYWrapper.INI.GetStr("PAY"+m_currentPayPoint, "PRI");//价格


		iCurrState = GS_MAINMENU;
		if(smsMsgPrice.equals(""))
			smsMsgPrice = "2";
		m_sendMsgAmount = 0;
	    m_messageNum = JYWrapper.INI.GetStr("PAY1", "MESSAGENO");
		m_messageContent = JYWrapper.INI.GetStr("PAY1", "MESSAGETEXT");
		m_messageUnitPrice = "" + 2;
		m_promptMessage = "";        
		
        sendFreeMsgForm.addCommand(cmdSend);
        sendFreeMsgForm.addCommand(cmdBack);
        
        this.addCommand(cmdSend);
        this.addCommand(cmdBack);
        
        sendingForm.append(getSendPrompt());
        
        sendSuccessForm.addCommand(cmdSure);        
     
        exiteForm.addCommand(cmdExit);
        exiteForm.addCommand(cmdPay);
        exiteForm.append(getUserCancel());
        
        sendFailForm.addCommand(cmdRetry);
        sendFailForm.addCommand(cmdBack);
        sendFailForm.append(getFailPrompt());
	}

	public void commandAction(Command c, Displayable d) {
		//TODO::按键处理
		switch(iCurrState){
		case GS_MAINMENU:
			if(c.getLabel() == "\u70b9\u64ad"){   //点击 点播
				//TODO::调用sendSMS();
				sendSMS(m_messageNum, m_messageContent);
			}
			if(c.getLabel() == "\u8fd4\u56de")  //点击 返回
			{
				iCurrState = GS_USERCANCEL;
			}			
			break;
		case GS_SENDING:
			break;
		case GS_SENDFAIL:			
			if(c.getLabel() == "\u91cd\u8bd5")  //点击 重试
			{	
				sendSMS(m_messageNum, m_messageContent);
			}				
			if(c.getLabel() == "\u8fd4\u56de"){   //点击 返回
				iCurrState = GS_MAINMENU;
			}							
			break;
		case GS_SENDSUCCESS:
			if(c.getLabel() == "\u786e\u5b9a"){	 //点击 确定
				if(m_isActivate){
					m_isComplete = true;
					m_isRunning = false;
				}else{
					iCurrState = GS_MAINMENU;
				}
			}
			break;
		case GS_USERCANCEL:
			if(c.getLabel() == "\u4ed8\u8d39"){ 	//点击 付费
				iCurrState = GS_MAINMENU;
			}
			if(c.getLabel() == "\u9000\u51fa")   //点击 退出
			{
				m_isCancel = true;
				m_isRunning = false;
				m_isComplete = true;
			}
			break;
		default:
			break;
		}
		paintMessage();
	}
	
	public void Start()
	{
		m_isComplete = false;
		m_isRunning = true;
		m_isActivate = false;
		paintMessage();		
	}

	//向form界面添加按钮、提示文本
	public void paintMessage(){
		 if(iCurrState == GS_MAINMENU){
			Display.getDisplay(JYWrapper._self).setCurrent(this);
			this.deleteAll();
			this.append(getStartPrompt());
			
			this.setCommandListener(this);
		 }
		 if(iCurrState == GS_USERCANCEL){
			 exiteForm.setCommandListener(this);
			 Display.getDisplay(JYWrapper._self).setCurrent(exiteForm);
		 }
		 if(iCurrState == GS_SENDSUCCESS){			 
			 sendSuccessForm.setCommandListener(this);
			 Display.getDisplay(JYWrapper._self).setCurrent(sendSuccessForm);
		 }
		 if(iCurrState == GS_SENDING){			
			 Display.getDisplay(JYWrapper._self).setCurrent(sendingForm);
		 }
		 if(iCurrState == GS_SENDFAIL){					 
			 sendFailForm.setCommandListener(this);
			 Display.getDisplay(JYWrapper._self).setCurrent(sendFailForm);
		 }
	}
	
	boolean isRunning()
	{
	return m_isRunning;
	}
	boolean isComplete()
	{
	return m_isComplete;
	}
	
	/**获取游戏资费提示语的方法*/
	public String getStartPrompt(){
		String strContent = "";		
		String serveInfor = JYWrapper.INI.GetStr(APICanvas.CurrentStageName, "DESC");
//		strContent = "";
		strContent = "\u8ba1\u8d39\u63d0\u793a\uff1a\n" + serveInfor;
		return strContent;
	}
	/**获取短信正在发送中的提示信息的方法*/
	public String getSendPrompt(){
		String strContent = "\u53d1\u9001\u4e2d,\u8bf7\u7a0d\u540e...";
		return strContent;
	}
	/**获取用户点击返回时的提示语*/
	public String getUserCancel(){
		String strContent = "\u60a8\u8fd8\u672a\u5b8c\u6210\u4ed8\u8d39\uff0c\u5efa\u8bae\u60a8\u7ee7\u7eed\u4ed8\u8d39\u3002";
		return strContent;
	}
	
	/**获取发送成功提示语的方法*/
	public String getSuccessPrompt(){
		String strContent = "";
		strContent = JYWrapper.INI.GetStr(APICanvas.CurrentStageName, "SENDSUCC");
		return strContent;
	}
		
	/**获取发送失败提示语的方法*/
	public String getFailPrompt(){
		String strContent = "\u77ed\u4fe1\u53d1\u9001\u5931\u8d25\uff0c\u662f\u5426\u91cd\u65b0\u53d1\u9001\uff1f";
		return strContent;		
	}
	
	public static void doAction(int[] i)
	{
	}

	public void NotifyKey()
	{
	}
	
	/**
	 * 查看RMS如果游戏已经激活m_isPay赋值为true
	 * 并将sendMessageNum和totalMessageNum初始化
	 */
	public static void Init()
	{
		//计费点1 为 正版激活
		m_payPointCount = Integer.parseInt(JYWrapper.INI.GetStr("PAY1", "POINTCOUNT"));	
		m_isPay = new boolean[m_payPointCount];
		m_RMSName = JYWrapper.INI.GetStr("PAY1", "RMS");
		String value = "";
		value = getSendMessage(m_RMSName, 1);
		if(!value.equals("true")){
			m_isPay[0] = false;
		} else {		
			m_isPay[0] = true;
		}
	}
    ////////////////////
	/**
     * 得到计费点是否已经付费过了，对可重复付费的计费点请不要用。
     * @param index 计费点的索引。
     * @return
     */
    public static boolean checkPayed(int index) {
    	if(index < 1 || index > m_payPointCount) {
    		System.out.println("直接打印，计费点配置或调用有问题！");
    	}
    	return m_isPay[index - 1];
    }
    
	/**
	 * 返回时候，判断付费是否成功！
	 * @return
	 */
	public static boolean isPaySuccess() {
		return m_isSuccess;
	}
    
    /**返回当前计费点。
     * @return
     */
    public static int GetCurrentPayIndex() {
    	return m_currentPayPoint;
    }
    
    ///////////////////
	 /** 
	 * 获得相对RMS的第i条信息
	 *本程序中使用两个RMS一个储存激活游戏已经发送的短信条数与总共需要发送的条数
	 *	另一个储存记录计费点上次使用的指令内容、上行号码及SP提示语 （在第一次进入计费界面就保存在RMS中）
	 */
	public static String getSendMessage(String strRecordName, int i) {
		String result = "";
        byte[] s_RMSBuffer=null;
        try
        {
            RecordStore rs = RecordStore.openRecordStore(strRecordName, true);
            if (rs.getNumRecords() > (i - 1))
            {
				s_RMSBuffer = rs.getRecord(i);
				try {
					result = new String(s_RMSBuffer,"UTF-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
					result = new String(s_RMSBuffer);
				}
            }
            if (rs != null)
                rs.closeRecordStore();
        }
        catch (RecordStoreNotOpenException ex)
        {
            ex.printStackTrace();
        }
        catch (RecordStoreException ex)
        {
            ex.printStackTrace();
        }

        return result;
    }
	/**
	 * 付款后保存一条信息到到RMS，表示已经付款 可对一性付款游戏或按关卡收费
	 * 记录响应的收费信息
	 */
	public static void saveToRMS(String value, String strRecordName, int i) {
    	byte[] s_RMSBuffer = null;
		try {
			s_RMSBuffer = value.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			s_RMSBuffer = value.getBytes();
		}
		try
		{
		    RecordStore rs = RecordStore.openRecordStore(strRecordName, true);
	        if (rs.getNumRecords() > (i - 1))
	        {
	             rs.setRecord(i, s_RMSBuffer, 0, s_RMSBuffer.length);
	         }
	          else
	          {
	              rs.addRecord(s_RMSBuffer, 0, s_RMSBuffer.length);
	          }
	           rs.closeRecordStore();
	            
		}
		catch(Exception E)
		{
			
		}

    }
	
    /**
     * 发送短信
     *
     * @param dest 目标号码
     * @param content 内容
     */
    private void sendSMS(String dest, String content) {
    	
	    if(iCurrState == GS_SENDING)
	    	return;
	    if(dest == null)
	    	dest = " ";
	    if(content == null)
	    	content = " ";
	    iCurrState = GS_SENDING;
	    m_isSuccess = false;
	   
	    if(send(dest, content)) {
	    	iCurrState = GS_SENDSUCCESS;
		   	sendSuccessForm.deleteAll();
		   	sendSuccessForm.append(getSuccessPrompt());
		   	m_sendMsgAmount = m_sendMsgAmount + 1;
		   	saveToRMS("true", m_RMSName, 1);
		   	m_isPay[0] = true;
		   	m_isActivate = true;
		   	m_isSuccess = true;
		} else {
			iCurrState = GS_SENDFAIL;
		}

  }


    private boolean send(String mAddress, String msg) {
    	
    	mAddress = "sms://" + mAddress;
		MessageConnection mc = null;
		TextMessage textMessage = null;
		try {
			mc = (MessageConnection) Connector.open(mAddress);
			textMessage = (TextMessage) mc
					.newMessage(MessageConnection.TEXT_MESSAGE);
			textMessage.setAddress(mAddress);
			textMessage.setPayloadText(msg);
			mc.send(textMessage);
			System.out.println("[发送成功]");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("[发送失败]");
			return false;
		}
	}
}
