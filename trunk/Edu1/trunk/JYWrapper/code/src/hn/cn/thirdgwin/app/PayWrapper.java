package cn.thirdgwin.app;

import javax.microedition.rms.RecordStore;

import hn.MsgPay;



public class PayWrapper extends payWrapperSuper implements Runnable{
	private MsgPay msgPay = new MsgPay();
	
	private static int sendMsgAmount;
	//��ȡ�Է���˾�����jar���еĵ�SmsInfoNo��Ϣ��Ϊ�����շ���ʾ��Ϣ
//	public static int smsInfoNo = -1;
	
	public static final byte GS_SENDING = 0;// ������
    public static final byte GS_MAINMENU = 1;// ��ʾ����
    public static final byte GS_SENDFAIL = 27;// ����ʧ��
//  private static final byte GS_SENDFAILMORE = 28;// ����ʧ�ܸ���
    public static final byte GS_USERCANCEL = 29;// ������ʾʱ�û�ȡ��
    public static final byte GS_SENDSUCCESS = 30;// ���ͳɹ�
    public static byte iCurrState = GS_MAINMENU;
    
//    public static boolean retryPay = false;  
//Properͨ�ñ���
    private static boolean m_isComplete = false;
    private static boolean m_isRunning = false;
    
    private static int s_payPointCount = 0;//һ�������Ʒѵ�
    private static boolean m_isCancel = false;
//    private boolean m_sendSuccess =false;

    private static boolean[] m_isPay;//���ּƷѵ�Ĺ�������������浵
    private static byte[]     m_smsLefts;//ÿ���Ʒѵ㻹ʣ���ٶ���
    private static byte[]     m_smsTotals;//ÿ���Ʒѵ㹲Ҫ���ٶ���
// ��ʾ��Ϣ
	private static String s_hint1;
	private static String s_hint2;
	private static String s_hint3;
	private static String s_hint4;
    /*
     * �浵��
     */
    private static String s_rmsName;
    private static boolean s_payOnce;
	public PayWrapper(int payIndex){
        super(null);
        m_isComplete = false;
        m_isRunning = false;
        m_currentPayPoint = payIndex - 1;
        
		msgPay.getMsgInfo(payIndex);//���ò����Ƕ�ȡ�ڼ���������ʾ��.(�ڼ����Ʒѵ�)
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
                 //���������ʾ����                
            	 String str = strTurnPage(getStartPrompt(), currentPageNo);
            	 if(turnPageTotal == 1){   //����ֻ��һҳ
            		 APICanvas.PromptSimple(null, str, null, "\u53d1\u9001", "\u53d6\u6d88");
            	 }else{
            		 if((currentPageNo == 1) && (turnPageTotal != 1)){  //������ʾ��Ϣ��ֹһҳʱ����ǰҳ��Ϊ��һҳ
        			 	APICanvas.PromptSimple(null, str, "< " + currentPageNo + " / " + turnPageTotal + " >", "\u53d6\u6d88", "\u4e0b\u4e00\u9875"); //��ťΪȡ������һҳ
            		 }else{
						if((currentPageNo == turnPageTotal) && (turnPageTotal != 1))   //��Ϣ��ʾ��Ϣ��ֹһҳʱ����ǩҳ��Ϊ���һҳ
						{
							APICanvas.PromptSimple(null, str, "< " + currentPageNo + " / " + turnPageTotal + " >", "\u53d1\u9001", "\u4e0a\u4e00\u9875"); //��ťΪ���͡���һҳ
						}else{
							APICanvas.PromptSimple(null, str, "< " + currentPageNo + " / " + turnPageTotal + " >", "\u4e0a\u4e00\u9875", "\u4e0b\u4e00\u9875"); //��ťΪ��һҳ����һҳ
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
        	//TODO:�˳���Ϸ
             break;
         case GS_SENDSUCCESS:
        	 //iCurrState = GS_MAINMENU;
        	 APICanvas.PromptSimple(null, getSuccessPrompt(), null, null, "\u786e\u5b9a");   
             break;
		}
	}
	
    /** ���� ��keyPressed����*/
    public void sendPayMessage() {      
    	//����Ϣ���ڷ����У�ֱ�ӷ���    	
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
                
                	
            	sendMsgAmount = sendMsgAmount + 1; //�ѷ��Ͷ��ŵ�������1
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
		if(confirm()){   //confirm()����true��ʾ�������������false��ʾ�����Ҽ�		
			if(((iCurrState == GS_MAINMENU) && turnPageTotal == 1) || (iCurrState == GS_SENDFAIL)){   //��ʾ��Ϣֻ��һҳ����Ϊ����ʧ�ܽ���ʱ�����������Ͷ���
				sendPayMessage();
			}else{
				if((iCurrState == GS_MAINMENU) && (turnPageTotal != 1) && (currentPageNo == 1)){      //��ʾ��Ϣ��ֹһҳʱ����ǰҳ��Ϊ��һҳ,�����ȡ����
					m_isCancel = true;
					currentPageNo = 1;
				}else{
					if((iCurrState == GS_MAINMENU) && (turnPageTotal != 1) && (currentPageNo == turnPageTotal)){ //��ʾ��ϸ��ֹһҳ,��ǰҳ��ʱ���һҳ�����"����"
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
				if(turnPageTotal == 1){   //��ʾ��Ϣҳ��ֻ��һҳʱ�����������˳�
					m_isCancel = true;
					currentPageNo = 1;
				}else{
					if(turnPageTotal != 1 && (currentPageNo == turnPageTotal)){ //��ʾ��Ϣ���һҳ
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
			//TODO:: ����˵�
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
	
	//��ʾ�ʷ���ʾ����ж��Ƿ������Ҽ�
	private boolean confirm() 
	{	
		synchronized (this)
		{
			//�ȴ����������,����ֵ1,2����ini�ļ��ж���İ����������ķ���ֵ
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
		APICanvas.SetIntResult(APICanvas.RESULT_TYPE_KEY_CODE, 0);//���ֵ
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
    
	/**��ȡ��Ϸ�ʷ���ʾ��ķ���*/
	private String getStartPrompt(){
		String strContent;
		
   		strContent = msgPay.strPrompt + s_hint1 + sendMsgAmount + s_hint2 + (msgPay.SEND_AMOUNT - sendMsgAmount) + s_hint3 + msgPay.strPrompt2;        	
		return strContent;
	}
	
	/**��ȡ�������ڷ����е���ʾ��Ϣ�ķ���*/
	private String getSendPrompt(){
		String strContent = s_hint4;
		return strContent;
	}
	
	/**��ȡ���ͳɹ���ʾ��ķ���*/
	private String getSuccessPrompt(){
		String strContent = msgPay.strSucceResult;
		return strContent;
	}
	
	/**��ȡ����ʧ����ʾ��ķ���*/
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
	/**ͨ����Ļ�Ĵ�С�ж�����ʾ�������Ƿ���Ҫ��ҳ������Ҫ ����з�ҳ����*/
	private static int turnPageTotal = 1; //��ʾ��Ϣ�����Ϊ����ҳ����ʼֵΪ0
	private static int currentPageNo = 1; //��ǰҳ���ǵڼ�ҳ
	private String strTurnPage(String str, int i){
		String string = "";
		String[] strBuffer = APICanvas.wrapText(str, JYWrapper.SCR_WIDTH-5);
		int strCount = strBuffer.length;
		int line = 10;
		if(JYWrapper.SCR_WIDTH < 240)
			line = 5;
		if(strCount % line == 0){   //�ж�һҳ��ʾ7�� ��Ҫ��ʾ��ҳ
			turnPageTotal = strCount / line;
		}else{
			turnPageTotal = strCount / line + 1;
		}
		int lineNum = 0;		
		while((lineNum < line) && (lineNum + line * (currentPageNo - 1) < strCount)){   //��һҳ7�л��ߵ�ǰҳ�����һҳ�����ݸ�ֵ��string
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
