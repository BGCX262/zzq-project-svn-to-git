package cn.thirdgwin.app;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.microedition.io.Connector;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.rms.InvalidRecordIDException;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreNotOpenException;
import com.samsung.util.SM;
import com.samsung.util.SMS;

class PayWrapper extends payWrapperSuper implements CommandListener
{
    public static final byte GS_SENDING = 0;// 发送中
    public static final byte GS_MAINMENU = 1;// 提示画面
    public static final byte GS_SENDFAIL = 27;// 发送失败
//  private static final byte GS_SENDFAILMORE = 28;// 发送失败更多
    public static final byte GS_USERCANCEL = 29;// 发送提示时用户取消
    public static final byte GS_SENDSUCCESS = 30;// 发送成功
    public static byte iCurrState = GS_MAINMENU;
    
    public static boolean m_isReBorn = false;
//    public static boolean m_isPay = false;
    private static boolean[] m_isPay;
    public static boolean m_isComplete = false;
    public static boolean m_isRunning = false;
    public static boolean m_isCancel = false;
//    public static boolean m_isFuzzyOrder = false;   //false表示不是模糊指令
//    public static boolean m_hasSendFree = false;    //判断免费短信是否发送，true不需要发送
    
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
    
//    public static String sendMessageNum = "";
//    public static String totalMessageNum = "";
    private boolean m_isActivate = false;
    
    private String m_messageNum = "";   //上行号码
    private String m_messageContent = "";  //上行内容    
//  private String m_freeMessageNum = "";   //免费短息号码
//  private String m_freeMessageContent = "";  //免费短信内容
    private String m_messageUnitPrice = ""; //短信单价 可能可以不要
    private String m_promptMessage = ""; //收费提示信息
    
    public static int s_gameGold = 0;
    public static boolean s_payGameGold = false; 
    private int m_exchangeRate = 0;   //游戏币兑换率
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
        super("\u8ba2\u8d2d");    //form标题 订购	
        m_currentPayPoint = Pay_Idx;  
		m_isComplete = false;
		m_isRunning = false;
		m_isCancel = false;	
//		m_hasPay = false;		
		m_RMSName = JYWrapper.INI.GetStr("PAY"+m_currentPayPoint, "RMS");//RMS
		smsMsgPrice = JYWrapper.INI.GetStr("PAY"+m_currentPayPoint, "PRI");//价格


		iCurrState = GS_MAINMENU;
		if(smsMsgPrice.equals(""))
			smsMsgPrice = "3";
		m_sendMsgAmount = 0;
        //通过当乐配置参数得到上行sms信息详情，参数含义参考SMSPackInfoReader的javadoc。
    	//前面4个参数为当乐给cp分配的11游戏分配代码，第五个参数为价格
//		if(RmsHasMessage(m_RMSName)){
//	    sendMessageNum = "0";
//	    saveToRMS(sendMessageNum, m_RMSName, 1);
//	    totalMessageNum = "" + 1;
//	    saveToRMS(totalMessageNum, m_RMSName, 2);
	    m_messageNum = JYWrapper.INI.GetStr("PAY1", "MESSAGENO");
		// saveToRMS(m_messageNum, m_RMSName, 3);
		m_messageContent = JYWrapper.INI.GetStr("PAY1", "MESSAGETEXT");
		// saveToRMS(m_messageContent, m_RMSName, 4);
		m_messageUnitPrice = "" + 1;//3; //Malean: 三星的计费都是只发一条消息。每条消息3元
		// saveToRMS(m_messageUnitPrice, m_RMSName, 5);
		m_promptMessage = "";        
	        //saveToRMS(m_promptMessage, m_RMSName, 6);			
//			}	
//        if(!m_isReBorn && !APICanvas.CurrentStageName.equals("PAY1")){
//        	m_sendMsgAmount = Integer.parseInt(sendMessageNum);
//        }

        sendFreeMsgForm.addCommand(cmdSend);
        sendFreeMsgForm.addCommand(cmdBack);
//        sendFreeMsgForm.append(getFreeMsgPrompt());
        
        this.addCommand(cmdSend);
        this.addCommand(cmdBack);
        
        sendingForm.append(getSendPrompt());
        
        sendSuccessForm.addCommand(cmdSure);        
//      sendSuccessForm.append(getSuccessPrompt());
        
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
		strContent = serveInfor + "\u60a8\u5c06\u9009\u62e9\u4f7f\u7528\u7531\u5317\u4eac\u96f7\u9706\u4e07\u94a7\u516c\u53f8\u63d0\u4f9b\u7684\u7cbe" + 
																"\u54c1\u5f69\u4fe1\u4e1a\u52a1\uff0c\u4fe1\u606f\u8d393\u5143/\u6761\uff0c\u7ee7\u7eed\u70b9\u51fb\u70b9\u64ad\u5f00" + 
																"\u59cb\u4eab\u53d7\u670d\u52a1\uff0c\u8fd4\u56de\u5219\u4e0d\u6263\u8d39\u3002\u5ba2\u670d\u7535\u8bdd\uff1a010-67868800";//"您将选择使用由北京雷霆万钧公司提供的精品彩信业务，信息费3元/条，继续点击点播开始享受服务，返回则不扣费。客服电话：010-67868800";
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
//		sendMessageNum = getSendMessage(m_RMSName, 1);		 
//		if((sendMessageNum.equals("")) || (Integer.parseInt(sendMessageNum) < 0)){
//			sendMessageNum = "0".toString();
//		}
//		totalMessageNum = getSendMessage(JYWrapper.INI.GetStr("PAY0", "RMS"), 2);	
//		if((totalMessageNum.equals("")) || (Integer.parseInt(totalMessageNum) < 0)){
//			totalMessageNum = "0".toString();
//		}
//		if(Integer.parseInt(sendMessageNum) >= Integer.parseInt(totalMessageNum) && (totalMessageNum != "0")){
//			m_isPay = true;
//		}
		String value = "";
		value = loadRMS(m_RMSName, 1);
		if(!value.equals("true")){
			m_isPay[PAY_POINT_STORY - 1] = false;
		} else {		
			m_isPay[PAY_POINT_STORY - 1] = true;
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
	 */
	public static String loadRMS(String strRecordName, int i) {
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
//                byte temp[] = rs.getRecord(i);
//                ByteArrayInputStream bais = new ByteArrayInputStream(temp);
//                DataInputStream dis = new DataInputStream(bais);
//                result = dis.readUTF();
//                dis.close();
//                bais.close();
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
//        catch (IOException ex)
//        {
//            ex.printStackTrace();
//        }
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
		
		
    	
//        ByteArrayOutputStream bos = new ByteArrayOutputStream();
//        DataOutputStream dos = new DataOutputStream(bos);
//        try
//        {
//            dos.writeUTF(value); 
//            dos.flush();
//            byte temp[] = bos.toByteArray();
//            RecordStore rs = RecordStore.openRecordStore(strRecordName, true);
//            if (rs.getNumRecords() > (i - 1))
//            {
//                rs.setRecord(i, temp, 0, temp.length);
//            }
//            else
//            {
//                rs.addRecord(temp, 0, temp.length);
//            }
//            bos.close();
//            dos.close();
//            rs.closeRecordStore();
//        }
//        catch (RecordStoreNotOpenException ex)
//        {
//            ex.printStackTrace();
//        }
//        catch (InvalidRecordIDException ex)
//        {
//            ex.printStackTrace();
//        }
//        catch (RecordStoreException ex)
//        {
//            ex.printStackTrace();
//        }
//        catch (IOException ex)
//        {
//            ex.printStackTrace();
//        }
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
	    	iCurrState = GS_SENDING;
	    	 m_isSuccess = false;
//    	paintMessage();
        //String addr="sms://" + dest;
        SM sm = new SM();
        try
        {
            sm.setDestAddress(dest);
            sm.setData(content);
            SMS.send(sm);
	        iCurrState = GS_SENDSUCCESS;
	        //获取发送成功提示语
			sendSuccessForm.deleteAll();
			sendSuccessForm.append(getSuccessPrompt());
			m_sendMsgAmount = m_sendMsgAmount + 1;
	//		if (!m_isReBorn && !APICanvas.CurrentStageName.equals("PAY1")) {
	//				sendMessageNum = "" + (Integer.parseInt(sendMessageNum) + 1);
				saveToRMS("true", m_RMSName, 1);
	//				if (Integer.parseInt(sendMessageNum) >= Integer
	//						.parseInt(totalMessageNum)) {
					m_isPay[PAY_POINT_STORY - 1] = true;
	//				}
	//		}
//			if (APICanvas.CurrentStageName.equals("PAY1")) {
//				s_gameGold = s_gameGold + m_exchangeRate
//						* Integer.parseInt(m_messageUnitPrice);
//			}
			m_isActivate = true;
			m_isSuccess = true;
        }
        catch(Exception ex)
        {
        	iCurrState = GS_SENDFAIL;
            ex.printStackTrace();
        }
/*
        MessageConnection conn;
        try {
            conn=(MessageConnection) Connector.open(addr);
            TextMessage msg=(TextMessage) conn.newMessage(MessageConnection.TEXT_MESSAGE);
            msg.setPayloadText(content);
            conn.send(msg);
            conn.close();
            iCurrState = GS_SENDSUCCESS;
            //获取发送成功提示语
			sendSuccessForm.deleteAll();
			sendSuccessForm.append(getSuccessPrompt());
			m_sendMsgAmount = m_sendMsgAmount + 1;
			if (!m_isReBorn && !APICanvas.CurrentStageName.equals("PAY1")) {
//				sendMessageNum = "" + (Integer.parseInt(sendMessageNum) + 1);
				saveToRMS("true", m_RMSName, 1);
//				if (Integer.parseInt(sendMessageNum) >= Integer
//						.parseInt(totalMessageNum)) {
					m_isPay = true;
//				}
			}
			if (APICanvas.CurrentStageName.equals("PAY1")) {
				s_gameGold = s_gameGold + m_exchangeRate
						* Integer.parseInt(m_messageUnitPrice);
			}
			m_isActivate = true;
        } catch(Exception ex) {
        	iCurrState = GS_SENDFAIL;
            ex.printStackTrace();
        }
*/
     }
  
}
