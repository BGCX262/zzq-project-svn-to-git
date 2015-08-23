package cn.thirdgwin.app;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
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
//  private static final byte GS_SENDFAILMORE = 28;// 发送失败更多
    public static final byte GS_USERCANCEL = 29;// 发送提示时用户取消
    public static final byte GS_SENDSUCCESS = 30;// 发送成功
    public static byte iCurrState = GS_MAINMENU;

//   public static boolean m_isReBorn = false;
//  public static boolean m_isPay = false;
    private static boolean[] m_isPay;
    public static boolean m_isComplete = false;
    public static boolean m_isRunning = false;
    public static boolean m_isCancel = false;

    public static String smsMsgPrice = "";
    
    /**
     * 购买一次
     */
    private static boolean m_isBuyOnce = false;
    

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

    public static String s_sendMessageCount = "";
    public static String s_totalMessageCount = "";

    private String m_messageNum = "";   //上行号码
    private String m_messageContent = "";  //上行内容    

    private String m_messageUnitPrice = ""; //短信单价 可能可以不要
    private String m_promptMessage = ""; //收费提示信息

    public static int s_gameGold = 0;
    public static boolean s_payGameGold = false; 
    private int m_exchangeRate = 0;   //游戏币兑换率
    private String m_RMSName = "";
    
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
        
		s_currentDisplayble = this;
        m_isSuccess = false;
        m_currentPayPoint = Pay_Idx;  
        m_isComplete = false;
        m_isRunning = false;     
        m_isCancel = false; 		
		m_RMSName = JYWrapper.INI.GetStr("PAY"+m_currentPayPoint, "RMS");//RMS
		smsMsgPrice = JYWrapper.INI.GetStr("PAY"+m_currentPayPoint, "PRI");//价格	

		m_isBuyOnce = (JYWrapper.INI.GetInt("PAY"+m_currentPayPoint, "BUYONCE") == 1);	
        iCurrState = GS_MAINMENU;
        
        if (smsMsgPrice.equals(""))
            smsMsgPrice = "4";
        
        m_sendMsgAmount = 0;
        
        if (RmsHasMessage(m_RMSName))
        {
            s_sendMessageCount = getSendMessage(m_RMSName, 1);
			s_totalMessageCount = getSendMessage(m_RMSName, 2);
            m_messageNum = getSendMessage(m_RMSName, 3);
            m_messageContent = getSendMessage(m_RMSName, 4);
            m_messageUnitPrice = getSendMessage(m_RMSName, 5);          
            m_promptMessage = getSendMessage(m_RMSName, 6);            
        }
        else
        {
            getYichaTxt();
            m_promptMessage = getYcTips();
            s_sendMessageCount = "0";
            m_messageUnitPrice = "2";
            saveToRMS(s_sendMessageCount, m_RMSName, 1);
            saveToRMS(s_totalMessageCount, m_RMSName, 2);
            saveToRMS(m_messageNum, m_RMSName, 3);
            saveToRMS(m_messageContent, m_RMSName, 4);
            saveToRMS(m_messageUnitPrice, m_RMSName, 5);        
            saveToRMS(m_promptMessage, m_RMSName, 6);              
        }   
        {
            m_sendMsgAmount = Integer.parseInt(s_sendMessageCount);
        }

        sendFreeMsgForm.addCommand(cmdSend);
        sendFreeMsgForm.addCommand(cmdBack);
        sendFreeMsgForm.append(getFreeMsgPrompt());

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
		
		sendFailForm.setTitle("\u8ba2\u8d2d");//设置子FORM标题
        sendingForm.setTitle("\u8ba2\u8d2d");
        sendSuccessForm.setTitle("\u8ba2\u8d2d");
        exiteForm.setTitle("\u8ba2\u8d2d");
        sendFreeMsgForm.setTitle("\u8ba2\u8d2d");
    }

    public void commandAction(Command c, Displayable d) {
        //TODO::按键处理
        switch (iCurrState)
        {
        case GS_MAINMENU:
            if (c.getLabel() == "\u70b9\u64ad")   //点击 点播
            {
                    sendSMSThread(m_messageNum, m_messageContent);
            }
            if (c.getLabel() == "\u8fd4\u56de")  //点击 返回
            {
                iCurrState = GS_USERCANCEL;
            }
            break;
        case GS_SENDING:
            break;
        case GS_SENDFAIL:           
            if (c.getLabel() == "\u91cd\u8bd5")  //点击 重试
            {
                    sendSMSThread(m_messageNum, m_messageContent);
            }
            if (c.getLabel() == "\u8fd4\u56de")   //点击 返回
            {
                iCurrState = GS_MAINMENU;
            }
            break;
        case GS_SENDSUCCESS:
            if (c.getLabel() == "\u786e\u5b9a")  //点击 确定
            {
                 if (Integer.parseInt(s_sendMessageCount) >= Integer.parseInt(s_totalMessageCount))
                {
                    m_isComplete = true;
                    m_isRunning = false;
                    if(!m_isBuyOnce)
                    {
                    	s_sendMessageCount = "0";
                    	saveToRMS(s_sendMessageCount, m_RMSName, 1);
                    }
                }
                else
                {
                    iCurrState = GS_MAINMENU;
                }
            }
            break;
        case GS_USERCANCEL:
            if (c.getLabel() == "\u4ed8\u8d39")     //点击 付费
            {
                iCurrState = GS_MAINMENU;
            }
            if (c.getLabel() == "\u9000\u51fa")   //点击 退出
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
        paintMessage();     
    }

    //向form界面添加按钮、提示文本
    public void paintMessage(){
        if (iCurrState == GS_MAINMENU)
        {
                Display.getDisplay(JYWrapper._self).setCurrent(this);
                this.deleteAll();
                this.append(getStartPrompt()); 
                this.setCommandListener(this);                     
        }
        if (iCurrState == GS_USERCANCEL)
        {
            exiteForm.setCommandListener(this);
            Display.getDisplay(JYWrapper._self).setCurrent(exiteForm);
        }
        if (iCurrState == GS_SENDSUCCESS)
        {
            sendSuccessForm.setCommandListener(this);
            Display.getDisplay(JYWrapper._self).setCurrent(sendSuccessForm);
        }
        if (iCurrState == GS_SENDING)
        {
            Display.getDisplay(JYWrapper._self).setCurrent(sendingForm);
        }
        if (iCurrState == GS_SENDFAIL)
        {
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
    public String getFreeMsgPrompt(){
    	String strContent = "";
        return strContent;
    }

    /**获取游戏资费提示语的方法*/
    public String getStartPrompt(){
//      String strContent = "";
//    	int price = 0;
//    	int total = 0;
//    	int sended = 0;
//		String des = JYWrapper.INI.GetStr(APICanvas.CurrentStageName, "DESC");
		String des = "";
        return des + m_promptMessage;
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
		////////////////////
		m_payPointCount = Integer.parseInt(JYWrapper.INI.GetStr("PAY1", "POINTCOUNT"));	
		m_isPay = new boolean[m_payPointCount];
		for (int i = 0; i < m_payPointCount; i++) {
			s_sendMessageCount = getSendMessage(JYWrapper.INI.GetStr("PAY" + (i + 1), "RMS"), 1);	
			if((s_sendMessageCount.equals("")) || (Integer.parseInt(s_sendMessageCount) < 0)){
				s_sendMessageCount = "0";
			}
			s_totalMessageCount = getSendMessage(JYWrapper.INI.GetStr("PAY" + (i + 1), "RMS"), 2);	
			if((s_totalMessageCount.equals("")) || (Integer.parseInt(s_totalMessageCount) < 0)){
				s_totalMessageCount = "0";
			}
			if(Integer.parseInt(s_sendMessageCount) >= Integer.parseInt(s_totalMessageCount) && (s_totalMessageCount != "0")){
				m_isPay[i] = true;
			}
		}
		////////////////////
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
        catch(Exception e)
        {
            //null pointer exception etc.
        }
//        catch (IOException ex)
//        {
//            ex.printStackTrace();
//        }
        return result;
    }

    public static boolean RmsHasMessage(String strRecordName){
        boolean hasMessage = false;
        try
        {
            RecordStore rs = RecordStore.openRecordStore(strRecordName, true);
            if (rs.getNumRecords() > 5)  //查看RMS中是否有 所需要记录的6条信息
            {
                hasMessage = true;
            }
        }
        catch (Exception ex)
        {
        }
        return hasMessage;
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
    private void sendSMSThread(final String dest, final String content)
    {
        if (iCurrState == GS_SENDING)
            return;
        iCurrState = GS_SENDING;

        new Thread()
        {
            public void run()
            {
                sendSMS(dest, content); 
            }
        }.start();
    }
    
    /**获得计费代码*/
    private void getYichaTxt()
    {
    	InputStream is = "".getClass().getResourceAsStream("/yicha.txt");
    	try
			{
				byte[] allbyte = new byte[is.available()];
				is.read(allbyte);
				is.close();
    	  String str = new String(allbyte);
    	  int deline = str.indexOf("|");
    	  m_messageContent = str.substring(0, deline);
    	  str = str.substring(deline + 1, str.length());
    	  deline = str.indexOf("|");
    	  m_messageNum = str.substring(0, deline);
    	  String c = str.substring(deline + 1, str.length());
//		  s_totalMessageCount = ""+Integer.parseInt(c) * Integer.parseInt(smsMsgPrice);
		  s_totalMessageCount = ""+Integer.parseInt(c);
		  System.out.println("s_totalMessageCount = " + s_totalMessageCount);
			} catch(Exception e) {

			}
    }
    
    /**获得计费提示消息*/
    private static String getYcTips()
	{
		InputStream is = "".getClass().getResourceAsStream("/yichaTip.txt");
		String str = "";
		try
		{
				byte[] allbyte = new byte[is.available() - 3];
				is.skip(3);
				is.read(allbyte);
				is.close();
				str = new String(allbyte, "UTF-8");
		} catch(Exception e) {
				str = "READ TIPS ERROR!";
		}
			
		return str;
	}
    
    private void sendSMS(String dest, String content) {

    	
    	m_isSuccess = false;
        String addr="sms://" + dest;
        MessageConnection conn = null;
        try
        {
            conn=(MessageConnection) Connector.open(addr);
            TextMessage msg=(TextMessage) conn.newMessage(MessageConnection.TEXT_MESSAGE);
            msg.setPayloadText(content);
            conn.send(msg);
//           conn.close();
            iCurrState = GS_SENDSUCCESS;
            //获取发送成功提示语
            sendSuccessForm.deleteAll();
            sendSuccessForm.append(getSuccessPrompt());
            paintMessage(); 
            s_sendMessageCount = "" + (Integer.parseInt(s_sendMessageCount) + 1);       
                    if (Integer.parseInt(s_sendMessageCount) >= Integer.parseInt(s_totalMessageCount))
                    {
                    	if(m_currentPayPoint > 0)
                    	{
                    		//一次付费的计费点保存pay成功标志，否则重置发送短信条数
                    		if(m_isBuyOnce)
                    			m_isPay[m_currentPayPoint - 1] = true;
                    		m_isSuccess = true;
                    	}
                    }
                    saveToRMS(s_sendMessageCount, m_RMSName, 1);

        }
        catch (SecurityException ex1)
        {
        	//用户取消发送
            iCurrState = GS_USERCANCEL;
            paintMessage();
            ex1.printStackTrace();

        }
        catch (Exception ex)
        {
            iCurrState = GS_SENDFAIL;
            paintMessage(); 
            ex.printStackTrace();
        }
        finally
        {
            try
            {
                if (conn != null)
                    conn.close();
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
            }
        }
    } 
}
