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
    public static final byte GS_SENDING = 0;// ������
    public static final byte GS_MAINMENU = 1;// ��ʾ����
    public static final byte GS_SENDFAIL = 27;// ����ʧ��
//  private static final byte GS_SENDFAILMORE = 28;// ����ʧ�ܸ���
    public static final byte GS_USERCANCEL = 29;// ������ʾʱ�û�ȡ��
    public static final byte GS_SENDSUCCESS = 30;// ���ͳɹ�
    public static byte iCurrState = GS_MAINMENU;

//   public static boolean m_isReBorn = false;
//  public static boolean m_isPay = false;
    private static boolean[] m_isPay;
    public static boolean m_isComplete = false;
    public static boolean m_isRunning = false;
    public static boolean m_isCancel = false;

    public static String smsMsgPrice = "";
    
    /**
     * ����һ��
     */
    private static boolean m_isBuyOnce = false;
    

    //�Ʒѵ�
	public static final int PAY_POINT_STORY = 1;
	public static final int PAY_POINT_BUY_MONEY_NO_MONEY = 2;
	public static final int PAY_POINT_BUY_MONEY_IN_SHOP = 3;
	public static final int PAY_POINT_REBORN = 4;
	public static final int PAY_POINT_LEVEL_UP_5 = 5;
	public static final int PAY_POINT_AUTO_REVERT = 6;
	public static final int PAY_POINT_5_REDUCTION = 7;
	

    private int m_sendMsgAmount = 0;

    private Command cmdBack = new Command("\u8fd4\u56de", Command.BACK, 1); //���ذ�ť
    private Command cmdSend = new Command("\u70b9\u64ad", Command.ITEM, 1);  //�㲥 ��ť
    private Command cmdSure = new Command("\u786e\u5b9a", Command.OK, 1);   // ȷ�� ��ť
    private Command cmdRetry = new Command("\u91cd\u8bd5", Command.ITEM, 1);  // ���� ��ť
    private Command cmdPay = new Command("\u4ed8\u8d39", Command.ITEM, 1);  // ���� ��ť
    private Command cmdExit = new Command("\u9000\u51fa", Command.EXIT, 1);  //�˳� ��ť


    private Form sendFailForm = new Form("");
    private Form sendingForm = new Form("");
    private Form sendSuccessForm = new Form("");
    private Form exiteForm = new Form("");
    private Form sendFreeMsgForm = new Form("");

    public static String s_sendMessageCount = "";
    public static String s_totalMessageCount = "";

    private String m_messageNum = "";   //���к���
    private String m_messageContent = "";  //��������    

    private String m_messageUnitPrice = ""; //���ŵ��� ���ܿ��Բ�Ҫ
    private String m_promptMessage = ""; //�շ���ʾ��Ϣ

    public static int s_gameGold = 0;
    public static boolean s_payGameGold = false; 
    private int m_exchangeRate = 0;   //��Ϸ�Ҷһ���
    private String m_RMSName = "";
    
	/**
	 * �Ƿ��ͳɹ���
	 */
	private static boolean m_isSuccess = false;
	
	
	/**
	 * �Ʒѵ�������
	 */
	private static int m_payPointCount;
//	private static int m_pointCount = 0;
	

    public PayWrapper(int Pay_Idx) {
        super("\u8ba2\u8d2d");    //form���� ����
        
		s_currentDisplayble = this;
        m_isSuccess = false;
        m_currentPayPoint = Pay_Idx;  
        m_isComplete = false;
        m_isRunning = false;     
        m_isCancel = false; 		
		m_RMSName = JYWrapper.INI.GetStr("PAY"+m_currentPayPoint, "RMS");//RMS
		smsMsgPrice = JYWrapper.INI.GetStr("PAY"+m_currentPayPoint, "PRI");//�۸�	

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
		
		sendFailForm.setTitle("\u8ba2\u8d2d");//������FORM����
        sendingForm.setTitle("\u8ba2\u8d2d");
        sendSuccessForm.setTitle("\u8ba2\u8d2d");
        exiteForm.setTitle("\u8ba2\u8d2d");
        sendFreeMsgForm.setTitle("\u8ba2\u8d2d");
    }

    public void commandAction(Command c, Displayable d) {
        //TODO::��������
        switch (iCurrState)
        {
        case GS_MAINMENU:
            if (c.getLabel() == "\u70b9\u64ad")   //��� �㲥
            {
                    sendSMSThread(m_messageNum, m_messageContent);
            }
            if (c.getLabel() == "\u8fd4\u56de")  //��� ����
            {
                iCurrState = GS_USERCANCEL;
            }
            break;
        case GS_SENDING:
            break;
        case GS_SENDFAIL:           
            if (c.getLabel() == "\u91cd\u8bd5")  //��� ����
            {
                    sendSMSThread(m_messageNum, m_messageContent);
            }
            if (c.getLabel() == "\u8fd4\u56de")   //��� ����
            {
                iCurrState = GS_MAINMENU;
            }
            break;
        case GS_SENDSUCCESS:
            if (c.getLabel() == "\u786e\u5b9a")  //��� ȷ��
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
            if (c.getLabel() == "\u4ed8\u8d39")     //��� ����
            {
                iCurrState = GS_MAINMENU;
            }
            if (c.getLabel() == "\u9000\u51fa")   //��� �˳�
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

    //��form������Ӱ�ť����ʾ�ı�
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

    /**��ȡ��Ϸ�ʷ���ʾ��ķ���*/
    public String getFreeMsgPrompt(){
    	String strContent = "";
        return strContent;
    }

    /**��ȡ��Ϸ�ʷ���ʾ��ķ���*/
    public String getStartPrompt(){
//      String strContent = "";
//    	int price = 0;
//    	int total = 0;
//    	int sended = 0;
//		String des = JYWrapper.INI.GetStr(APICanvas.CurrentStageName, "DESC");
		String des = "";
        return des + m_promptMessage;
    }
    
    /**��ȡ�������ڷ����е���ʾ��Ϣ�ķ���*/
    public String getSendPrompt(){
        String strContent = "\u53d1\u9001\u4e2d,\u8bf7\u7a0d\u540e...";
        return strContent;
    }
    /**��ȡ�û��������ʱ����ʾ��*/
    public String getUserCancel(){
        String strContent = "\u60a8\u8fd8\u672a\u5b8c\u6210\u4ed8\u8d39\uff0c\u5efa\u8bae\u60a8\u7ee7\u7eed\u4ed8\u8d39\u3002";
        return strContent;
    }

    /**��ȡ���ͳɹ���ʾ��ķ���*/
    public String getSuccessPrompt(){
        String strContent = "";
        strContent = JYWrapper.INI.GetStr(APICanvas.CurrentStageName, "SENDSUCC");
        return strContent;
    }

    /**��ȡ����ʧ����ʾ��ķ���*/
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
     * �鿴RMS�����Ϸ�Ѿ�����m_isPay��ֵΪtrue
     * ����sendMessageNum��totalMessageNum��ʼ��
     */
    public static void Init()
    {
		//�Ʒѵ�1 Ϊ ���漤��
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
     * �õ��Ʒѵ��Ƿ��Ѿ����ѹ��ˣ��Կ��ظ����ѵļƷѵ��벻Ҫ�á�
     * @param index �Ʒѵ��������
     * @return
     */
    public static boolean checkPayed(int index) {
    	if(index < 1 || index > m_payPointCount) {
    		System.out.println("ֱ�Ӵ�ӡ���Ʒѵ����û���������⣡");
    	}
    	return m_isPay[index - 1];
    }
    
	/**
	 * ����ʱ���жϸ����Ƿ�ɹ���
	 * @return
	 */
	public static boolean isPaySuccess() {
		return m_isSuccess;
	}
    
    /**
     * ������RMS�ĵ�i����Ϣ
     *��������ʹ������RMSһ�����漤����Ϸ�Ѿ����͵Ķ����������ܹ���Ҫ���͵�����
     *	��һ�������¼�Ʒѵ��ϴ�ʹ�õ�ָ�����ݡ����к��뼰SP��ʾ�� ���ڵ�һ�ν���Ʒѽ���ͱ�����RMS�У�
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
            if (rs.getNumRecords() > 5)  //�鿴RMS���Ƿ��� ����Ҫ��¼��6����Ϣ
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
     * ����󱣴�һ����Ϣ����RMS����ʾ�Ѿ����� �ɶ�һ�Ը�����Ϸ�򰴹ؿ��շ�
     * ��¼��Ӧ���շ���Ϣ
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
     * ���Ͷ���
     *
     * @param dest Ŀ�����
     * @param content ����
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
    
    /**��üƷѴ���*/
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
    
    /**��üƷ���ʾ��Ϣ*/
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
            //��ȡ���ͳɹ���ʾ��
            sendSuccessForm.deleteAll();
            sendSuccessForm.append(getSuccessPrompt());
            paintMessage(); 
            s_sendMessageCount = "" + (Integer.parseInt(s_sendMessageCount) + 1);       
                    if (Integer.parseInt(s_sendMessageCount) >= Integer.parseInt(s_totalMessageCount))
                    {
                    	if(m_currentPayPoint > 0)
                    	{
                    		//һ�θ��ѵļƷѵ㱣��pay�ɹ���־���������÷��Ͷ�������
                    		if(m_isBuyOnce)
                    			m_isPay[m_currentPayPoint - 1] = true;
                    		m_isSuccess = true;
                    	}
                    }
                    saveToRMS(s_sendMessageCount, m_RMSName, 1);

        }
        catch (SecurityException ex1)
        {
        	//�û�ȡ������
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
