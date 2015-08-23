package cn.thirdgwin.app;

import javax.microedition.rms.RecordStore;

import hn.MsgPay;



public class PayWrapper extends payWrapperSuper implements Runnable{
	private MsgPay msgPay = new MsgPay();
	
	private static int sendMsgAmount;
	//获取对方公司给予的jar包中的第SmsInfoNo信息作为短信收费提示信息
//	public static int smsInfoNo = -1;
	
	public static final byte GS_SENDING = 0;// 发送中
    public static final byte GS_MAINMENU = 1;// 提示画面
    public static final byte GS_SENDFAIL = 27;// 发送失败
//  private static final byte GS_SENDFAILMORE = 28;// 发送失败更多
    public static final byte GS_USERCANCEL = 29;// 发送提示时用户取消
    public static final byte GS_SENDSUCCESS = 30;// 发送成功
    public static byte iCurrState = GS_MAINMENU;
    
//    public static boolean retryPay = false;  
//Proper通用变量
    private static boolean m_isComplete = false;
    private static boolean m_isRunning = false;
    
    private static int s_payPointCount = 0;//一共几个计费点
    private static boolean m_isCancel = false;
//    private boolean m_sendSuccess =false;

    private static boolean[] m_isPay;//各种计费点的购买情况，将被存档
    private static byte[]     m_smsLefts;//每条计费点还剩多少短信
    private static byte[]     m_smsTotals;//每条计费点共要多少短信
// 提示信息
	private static String s_hint1;
	private static String s_hint2;
	private static String s_hint3;
	private static String s_hint4;
    /*
     * 存档名
     */
    private static String s_rmsName;
    private static boolean s_payOnce;
	public PayWrapper(int payIndex){
        super(null);
        m_isComplete = false;
        m_isRunning = false;
        m_currentPayPoint = payIndex - 1;
        
		msgPay.getMsgInfo(payIndex);//设置参数是读取第几条发送提示语.(第几个计费点)
		s_payOnce = JYWrapper.INI.GetStr("PAY"+payIndex, "PONCE")=="Y"?true:false;

        if (m_smsTotals[m_currentPayPoint] == -1)
        {
        	m_smsTotals[m_currentPayPoint] = (byte)msgPay.SEND_AMOUNT;
        }
        
        if (m_smsLefts[m_currentPayPoint] < 0)
        {
        	m_smsLefts[m_currentPayPoint] = m_smsTotals[m_currentPayPoint];
        }
        if (!s_payOnce && m_smsLefts[m_currentPayPoint] == m_smsTotals[m_currentPayPoint])
        {
        	m_smsTotals[m_currentPayPoint] = (byte)msgPay.SEND_AMOUNT;
        	m_smsLefts[m_currentPayPoint] = m_smsTotals[m_currentPayPoint];
        	m_isPay[m_currentPayPoint] = false;
        }
        sendMsgAmount = m_smsTotals[m_currentPayPoint] - m_smsLefts[m_currentPayPoint];
        RMS_Save();
	}
	
	public void Start(){
		new Thread(this).start();
	}
	public void paintMessage(){
		switch(iCurrState){
		case GS_MAINMENU:
			 iCurrState = GS_MAINMENU;  
             if (!m_isComplete)
             {                 
                 //传入界面显示文字                
            	 String str = strTurnPage(getStartPrompt(), currentPageNo);
            	 if(turnPageTotal == 1){   //文字只有一页
            		 APICanvas.PromptSimple(null, str, null, "\u53d1\u9001", "\u53d6\u6d88");
            	 }else{
            		 if((currentPageNo == 1) && (turnPageTotal != 1)){  //短信提示信息不止一页时，当前页面为第一页
        			 	APICanvas.PromptSimple(null, str, "< " + currentPageNo + " / " + turnPageTotal + " >", "\u53d6\u6d88", "\u4e0b\u4e00\u9875"); //按钮为取消、下一页
            		 }else{
						if((currentPageNo == turnPageTotal) && (turnPageTotal != 1))   //短息提示信息不止一页时，单签页面为最后一页
						{
							APICanvas.PromptSimple(null, str, "< " + currentPageNo + " / " + turnPageTotal + " >", "\u53d1\u9001", "\u4e0a\u4e00\u9875"); //按钮为发送、上一页
						}else{
							APICanvas.PromptSimple(null, str, "< " + currentPageNo + " / " + turnPageTotal + " >", "\u4e0a\u4e00\u9875", "\u4e0b\u4e00\u9875"); //按钮为上一页、下一页
						}            	 			
            		 }
            	 }            	                               
             }            	                                             
             break;
		 case GS_SENDING: 
			 iCurrState = GS_SENDING;
			 APICanvas.PromptSimple(null, getSendPrompt(), null, null, null);			 
             break;
         case GS_SENDFAIL:
        	 iCurrState = GS_SENDFAIL;
        	 APICanvas.PromptSimple(null, getFailPrompt(), null, "\u91cd\u8bd5", "\u8fd4\u56de");
        	 
             break;
         case GS_USERCANCEL:
        	 iCurrState = GS_SENDFAIL;
			 APICanvas.PromptSimple(null, getFailPrompt(), null, "\u91cd\u8bd5", "\u8fd4\u56de");
        	//TODO:退出游戏
             break;
         case GS_SENDSUCCESS:
        	 //iCurrState = GS_MAINMENU;
        	 APICanvas.PromptSimple(null, getSuccessPrompt(), null, null, "\u786e\u5b9a");   
             break;
		}
	}
	
    /** 发送 当keyPressed触发*/
    public void sendPayMessage() {      
    	//当短息正在发送中，直接返回    	
        if(iCurrState == GS_SENDING){
        	return;
        }
        iCurrState = GS_SENDING;
        paintMessage();
        byte send = msgPay.payTwoYuan();
        switch (send) {
            case MsgPay.SEND_SUCCESS:
                m_smsLefts[m_currentPayPoint]--;
                if(m_smsLefts[m_currentPayPoint] == 0)
                	m_isPay [m_currentPayPoint] = true;
                
                	
            	sendMsgAmount = sendMsgAmount + 1; //已发送短信的总数加1
//            	saveToRMS(sendMsgAmount, "payRecode");              	
                iCurrState = GS_SENDSUCCESS;    
                RMS_Save();
                break;
            case MsgPay.SEND_USERCANCLE:
                iCurrState = GS_SENDFAIL;
                break;
            case MsgPay.SEND_FAIL:
                iCurrState = GS_SENDFAIL;               
                break;
            case MsgPay.SEND_NOREC:
                iCurrState = GS_SENDFAIL;
                break;
            default:
            	break;            	   
        }
        currentPageNo = 1;
    }

	public void run() {	
		m_isRunning = true;
		m_isComplete = false;
		paintMessage();
		if(confirm()){   //confirm()返回true表示按下左键、返回false表示按下右键		
			if(((iCurrState == GS_MAINMENU) && turnPageTotal == 1) || (iCurrState == GS_SENDFAIL)){   //提示信息只有一页或者为发送失败界面时，点击左键发送短信
				sendPayMessage();
			}else{
				if((iCurrState == GS_MAINMENU) && (turnPageTotal != 1) && (currentPageNo == 1)){      //提示信息不止一页时，当前页面为第一页,点击“取消”
					m_isCancel = true;
					currentPageNo = 1;
				}else{
					if((iCurrState == GS_MAINMENU) && (turnPageTotal != 1) && (currentPageNo == turnPageTotal)){ //提示纤细不止一页,单前页面时最后一页，点击"发送"
						sendPayMessage();
					}else{
						iCurrState = GS_MAINMENU;
						currentPageNo --;
					}											
				}
			}			
		}else{
			switch(iCurrState){
			case GS_MAINMENU:
				if(turnPageTotal == 1){   //提示信息页面只有一页时，点击右软件退出
					m_isCancel = true;
					currentPageNo = 1;
				}else{
					if(turnPageTotal != 1 && (currentPageNo == turnPageTotal)){ //提示信息最后一页
						iCurrState = GS_MAINMENU;
						currentPageNo --;
					}else{
						iCurrState = GS_MAINMENU;
						currentPageNo ++;
					}
				}												
				break;
			case GS_SENDFAIL:
				iCurrState = GS_MAINMENU;
				break;
			case GS_SENDSUCCESS:
    			m_isPay[m_currentPayPoint] = true;
    			m_isComplete = true;    
				iCurrState = GS_MAINMENU;
				break;
			}				
			//TODO:: 进入菜单
		}
		m_isRunning = false;
		if(m_isCancel){
			m_isComplete = true;
		}
	}
	
	public static void doAction(int[] result)
	{
		APICanvas.StageEndAction = APICanvas.STAGE_END_ACTION_RETURN_GAME;
	}
	
	//显示资费提示语，并判断是否按下左右键
	private boolean confirm() 
	{	
		synchronized (this)
		{
			//等待按左右软键,返回值1,2是在ini文件中定义的按左，右软键后的返回值
	        while (APICanvas.GetIntResult()[APICanvas.RESULT_TYPE_KEY_CODE] != 1 
	        		&& APICanvas.GetIntResult()[APICanvas.RESULT_TYPE_KEY_CODE] != 2) 
	        {
	            try 
	            {
	                wait();
	            } catch (InterruptedException e) 
	            {
	                return false;
	            }
	  
	        }
	    }
		boolean bconfirm = (APICanvas.GetIntResult()[APICanvas.RESULT_TYPE_KEY_CODE] == 1);
		APICanvas.SetIntResult(APICanvas.RESULT_TYPE_KEY_CODE, 0);//清键值
		return bconfirm;
	}

    public static boolean isPaySuccess()
    {
        return m_isPay [m_currentPayPoint];
    }
    
    public static boolean checkPayed(int index) 
    {
        if (index < 1 || index > s_payPointCount || m_isPay == null)
        {
            return false;
        }
        return m_isPay[index - 1];
    }
    
	/**获取游戏资费提示语的方法*/
	private String getStartPrompt(){
		String strContent;
		
   		strContent = msgPay.strPrompt + s_hint1 + sendMsgAmount + s_hint2 + (msgPay.SEND_AMOUNT - sendMsgAmount) + s_hint3 + msgPay.strPrompt2;        	
		return strContent;
	}
	
	/**获取短信正在发送中的提示信息的方法*/
	private String getSendPrompt(){
		String strContent = s_hint4;
		return strContent;
	}
	
	/**获取发送成功提示语的方法*/
	private String getSuccessPrompt(){
		String strContent = msgPay.strSucceResult;
		return strContent;
	}
	
	/**获取发送失败提示语的方法*/
	private String getFailPrompt(){
		String strContent = msgPay.strFailSend;
		return strContent;		
	}
	

	public boolean isRunning(){
		return m_isRunning;
	}
	
	public boolean isComplete(){
		return m_isComplete;
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
	public static void Init(){
        s_payPointCount = Integer.parseInt(JYWrapper.INI.GetStr("CNF", "POINTCOUNT"));
        s_hint1 = JYWrapper.INI.GetStr("CNF", "H1");
        s_hint2 = JYWrapper.INI.GetStr("CNF", "H2");
        s_hint3 = JYWrapper.INI.GetStr("CNF", "H3");
        s_hint4 = JYWrapper.INI.GetStr("CNF", "H4");
        m_isPay = new boolean[s_payPointCount];
        m_smsLefts = new byte[s_payPointCount];
        m_smsTotals = new byte[s_payPointCount];
        for (int i = 0;i<s_payPointCount;i++)
        {
            m_smsLefts[i] = m_smsTotals[i] = -1;
            m_isPay[i] = false;
        }
        s_rmsName = JYWrapper.INI.GetStr("CNF", "RMS");
        RMS_Load();
	}
	/**通过屏幕的大小判断所显示的文字是否需要翻页，若需要 则进行翻页处理*/
	private static int turnPageTotal = 1; //显示信息总体分为多少页，初始值为0
	private static int currentPageNo = 1; //当前页面是第几页
	private String strTurnPage(String str, int i){
		String string = "";
		String[] strBuffer = APICanvas.wrapText(str, JYWrapper.SCR_WIDTH-5);
		int strCount = strBuffer.length;
		int line = 10;
		if(JYWrapper.SCR_WIDTH < 240)
			line = 5;
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
    private static void RMS_Save() 
    {
        if (m_isPay == null)
            return;
        byte[] data = new byte[m_isPay.length * 3];
        for (int i = 0;i<m_isPay.length;i++)
        {
            data[i*3] = (byte)(m_isPay[i]?1:0);
            data[i*3 + 1] = m_smsLefts[i];
            data[i*3 + 2] = m_smsTotals[i];
        }
        try
        {
            RecordStore rs = RecordStore.openRecordStore(s_rmsName, true);
            if (rs.getNumRecords() > 0)
            {
                rs.setRecord(1, data, 0, data.length);
            }
            else
            {
                rs.addRecord(data, 0, data.length);
            }

            rs.closeRecordStore();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    private static void RMS_Load()
    {
        byte[] data = null;
        try
        {
            RecordStore rs = RecordStore.openRecordStore(s_rmsName, true);
            if (rs.getNumRecords() > 0)
            {
                data = rs.getRecord(1);
            }
            rs.closeRecordStore();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        if (data != null && data.length == s_payPointCount * 3)
        {
            for (int i = 0;i<s_payPointCount;i++)
            {
                m_isPay[i]      = (data[i*3] == 1);
                m_smsLefts[i]   = data[i*3 + 1];
                m_smsTotals[i]  = data[i*3 + 2];
            }
        }
    }
}
