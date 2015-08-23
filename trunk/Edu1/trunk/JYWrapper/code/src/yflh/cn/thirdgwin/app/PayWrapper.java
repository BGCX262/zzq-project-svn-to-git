package cn.thirdgwin.app;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import javax.microedition.io.Connector;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import javax.microedition.rms.InvalidRecordIDException;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreNotOpenException;
import javax.wireless.messaging.MessageConnection;
import javax.wireless.messaging.TextMessage;

class PayWrapper implements Runnable
{	
	public static final byte GS_SENDING = 0;// 发送中
    public static final byte GS_MAINMENU = 1;// 提示画面
    public static final byte GS_SENDFAIL = 27;// 发送失败
//  private static final byte GS_SENDFAILMORE = 28;// 发送失败更多
    public static final byte GS_USERCANCEL = 29;// 发送提示时用户取消
    public static final byte GS_SENDSUCCESS = 30;// 发送成功
    public static byte iCurrState = GS_MAINMENU;
    
    public static boolean m_isPay = false;
    public static boolean m_isComplete = false;
    public static boolean m_isRunning = false;
    public static boolean m_isCancel = false;
    
    public static String s_sendMsgContent = "";
    public static String s_sendMsgNumber = "";
    
//  private int m_sendMsgAmount = 0; //需要发送短信的总条数
    private static String m_hasSendMsg = "0";  //需要发送的条数
    
    static String s_RMSName = ""; 
	
	public PayWrapper()
	{
		m_isComplete = false;
		m_isRunning = false;
		m_isCancel = false;	
		s_sendMsgContent = JYWrapper.INI.GetStr("PAY0", "sendcontent");
		s_sendMsgNumber = JYWrapper.INI.GetStr("PAY0", "sendnumber");
	}
	
	public void sendPaintMesg(){
		switch(iCurrState){
		case GS_MAINMENU:
			 iCurrState = GS_MAINMENU; 
			String str = strTurnPage(getStartPrompt(), 1);
			APICanvas.PromptSimple(null, str, null, "\u53d1\u9001", "\u53d6\u6d88");
			break;
		case GS_SENDING:
			 iCurrState = GS_SENDING;
			 APICanvas.PromptSimple(null, getSendPrompt(), null, null, null);	
			break;
		case GS_SENDFAIL:
			 iCurrState = GS_SENDFAIL;
        	 APICanvas.PromptSimple(null, getFailPrompt(), null, "\u91cd\u8bd5", "\u8fd4\u56de");
			break;
		case GS_SENDSUCCESS:
			APICanvas.PromptSimple(null, getSuccessPrompt(), null, "\u786e\u5b9a", null);
			break;
		case GS_USERCANCEL:
//			iCurrState = GS_SENDFAIL;
//			APICanvas.PromptSimple(null, getFailPrompt(), null, "\u91cd\u8bd5", "\u8fd4\u56de");
			break;
		default:
			break;
		}
	}
	
	public void Start()
	{
		new Thread(this).start();
	}

	boolean isRunning()
	{
		return m_isRunning;
	}
	boolean isComplete()
	{
		return m_isComplete;
	}
	public static void doAction(int[] i)
	{
		APICanvas.StageEndAction = APICanvas.STAGE_END_ACTION_RETURN_GAME;
	}

	public void NotifyKey()
	{
		synchronized(this)	
		{
			try {
				notify();
			} catch (Exception e) {}
		}
	}
	public static void Init()
	{
		s_RMSName = JYWrapper.INI.GetStr("PAY0", "RMS");
		m_hasSendMsg = getSendMessage(s_RMSName, 1);
		if(m_hasSendMsg.equals("") || (Integer.parseInt(m_hasSendMsg) < 0))
			m_hasSendMsg = "0";
		//int i = Integer.parseInt(m_hasSendMsg);
		if(Integer.parseInt(m_hasSendMsg) >= 2){
			m_isPay = true;
		}
	}
	/**获取游戏资费提示语的方法*/
	public String getStartPrompt(){
		String strContent = "";
		if(Integer.parseInt(m_hasSendMsg) <= 0){
			strContent = JYWrapper.INI.GetStr("PAY0", "DESC1");
		} else {
			strContent = JYWrapper.INI.GetStr("PAY0", "DESC2");
		}		
		return strContent;
	}
	
	/**获取短信正在发送中的提示信息的方法*/
	public String getSendPrompt(){
		String strContent = JYWrapper.INI.GetStr("PAY0", "DESC3");
		return strContent;
	}
	
	/**获取发送成功提示语的方法*/
	public String getSuccessPrompt(){
		String strContent = JYWrapper.INI.GetStr("PAY0", "SENDSUCC");
		return strContent;
	}
	
	/**获取发送失败提示语的方法*/
	public String getFailPrompt(){
		String strContent = JYWrapper.INI.GetStr("PAY0", "DESC4");
		return strContent;		
	}
	
	/**
	 * 获得相对RMS的第i条信息
	 *本程序中使用两个RMS一个储存激活游戏已经发送的短信条数与总共需要发送的条数
	 *	另一个储存记录计费点上次使用的指令内容、上行号码及SP提示语 （在第一次进入计费界面就保存在RMS中）
	 */
	public static String getSendMessage(String strRecordName, int i) {
		String result = "";
		try {
			RecordStore rs = RecordStore.openRecordStore(strRecordName, true);
			if (rs.getNumRecords() > (i - 1)) {
				byte temp[] = rs.getRecord(i);
				ByteArrayInputStream bais = new ByteArrayInputStream(temp);
				DataInputStream dis = new DataInputStream(bais);
				result = dis.readUTF();
				dis.close();
				bais.close();
			}
			if (rs != null)
				rs.closeRecordStore();
		} catch (RecordStoreNotOpenException ex) {
			ex.printStackTrace();
		} catch (RecordStoreException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 付款后保存一条信息到到RMS，表示已经付款 可对一性付款游戏或按关卡收费
	 * 记录响应的收费信息
	 */
	public static void saveToRMS(String value, String strRecordName, int i) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);
		try {
			dos.writeUTF(value);
			dos.flush();
			byte temp[] = bos.toByteArray();
			RecordStore rs = RecordStore.openRecordStore(strRecordName, true);
			if (rs.getNumRecords() > (i - 1)) {
				rs.setRecord(i, temp, 0, temp.length);
			} else {
				rs.addRecord(temp, 0, temp.length);
			}
			bos.close();
			dos.close();
			rs.closeRecordStore();
		} catch (RecordStoreNotOpenException ex) {
			ex.printStackTrace();
		} catch (InvalidRecordIDException ex) {
			ex.printStackTrace();
		} catch (RecordStoreException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
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
	    	iCurrState = GS_SENDING;
//    	paintMessage();
        String addr="sms://" + dest;
        MessageConnection conn;
        try {
            conn=(MessageConnection) Connector.open(addr);
            TextMessage msg=(TextMessage) conn.newMessage(MessageConnection.TEXT_MESSAGE);
            msg.setPayloadText(content);
            conn.send(msg);
            conn.close();
            iCurrState = GS_SENDSUCCESS;
            System.out.println(Integer.parseInt(m_hasSendMsg));
            m_hasSendMsg = "" + (Integer.parseInt(m_hasSendMsg) + 1);
            saveToRMS("" + m_hasSendMsg, s_RMSName, 1);
            if(Integer.parseInt(m_hasSendMsg) >= 2)
            	m_isPay = true;
           
        } catch(Exception ex) {
        	iCurrState = GS_SENDFAIL;
            ex.printStackTrace();
        }        
     }
    
	/**通过屏幕的大小判断所显示的文字是否需要翻页，若需要 则进行翻页处理*/
	public static int turnPageTotal = 1; //显示信息总体分为多少页，初始值为0
	public static int currentPageNo = 1; //当前页面是第几页
	public String strTurnPage(String str, int i){
		String string = "";
		String[] strBuffer = APICanvas.wrapText(str, JYWrapper.SCR_WIDTH-5);
		int strCount = strBuffer.length;
		int line = 10;
		if(JYWrapper.SCR_WIDTH < 240)
			line = 8;
		if(strCount % line == 0){   //判断一页显示7行 需要显示几页
			turnPageTotal = strCount / line;
		}else{
			turnPageTotal = strCount / line + 1;
		}
		int lineNum = 0;		
		while((lineNum < line) && (lineNum + line * (currentPageNo - 1) < strCount)){   //将一页7行或者当前页是最后一页的内容赋值给string
			string = string + strBuffer[lineNum + line * (currentPageNo - 1)];
			lineNum ++;
		}
		return string;
	}

	public void run() {
		m_isRunning = true;
		m_isComplete = false;
		sendPaintMesg();		
		if(confirm()){
			//当用户在menu界面和发送失败界面 点击左软键“发送”和“重试”时
			if((iCurrState == GS_SENDFAIL) || (iCurrState == GS_MAINMENU)){			
				sendSMS(s_sendMsgNumber, s_sendMsgContent);					
			} else {
				//在发送成功提示界面点击“确定”
				if(iCurrState == GS_SENDSUCCESS){
					if(m_isPay){
						m_isCancel = false;
						m_isComplete = true;
					} else {
						iCurrState = GS_MAINMENU;
					}
				}
			}
		} else {
			switch(iCurrState){
			case GS_MAINMENU:  //点击“退出”
				m_isCancel = true;
				break;
			case GS_SENDFAIL:
				iCurrState = GS_MAINMENU;
			default:
				break;
			}			
		}
		m_isRunning = false;
		if(m_isCancel){
			m_isComplete = true;
		}
	}
	
	//显示资费提示语，并判断是否按下左右键
	public boolean confirm() 
	{	
//			String gameDesc = JYWrapper.INI.GetStr("CNF","DESC");
//			info = gameDesc+info;
//			
//			APICanvas.PromptSimple(null, info, null, JYWrapper.INI.GetStr("CNF","YES"), JYWrapper.INI.GetStr("CNF","NO"));

			synchronized (this)
			{
				//等待按左右软键,返回值1,2是在ini文件中定义的按左，右软键后的返回值
	            while (APICanvas.GetIntResult()[APICanvas.RESULT_TYPE_KEY_CODE] != 1 
	            		&& APICanvas.GetIntResult()[APICanvas.RESULT_TYPE_KEY_CODE] != 2) 
	            {
	                try 
	                {
	                	System.out.println(APICanvas.GetIntResult()[APICanvas.RESULT_TYPE_KEY_CODE]);
	                    wait();
	                } catch (InterruptedException e) 
	                {
	                    return false;
	                }
	      
	            }
	        }
			boolean bconfirm = (APICanvas.GetIntResult()[APICanvas.RESULT_TYPE_KEY_CODE] == 1);
//			if(bconfirm)
//			{
//				APICanvas.PromptSimple(null, JYWrapper.INI.GetStr("CNF","SEND"), null, null, null);
//			}
			APICanvas.SetIntResult(APICanvas.RESULT_TYPE_KEY_CODE, 0);//清键值
			return bconfirm;
	}
}
