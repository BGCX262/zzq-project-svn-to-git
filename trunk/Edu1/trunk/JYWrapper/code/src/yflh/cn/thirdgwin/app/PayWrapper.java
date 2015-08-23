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
	public static final byte GS_SENDING = 0;// ������
    public static final byte GS_MAINMENU = 1;// ��ʾ����
    public static final byte GS_SENDFAIL = 27;// ����ʧ��
//  private static final byte GS_SENDFAILMORE = 28;// ����ʧ�ܸ���
    public static final byte GS_USERCANCEL = 29;// ������ʾʱ�û�ȡ��
    public static final byte GS_SENDSUCCESS = 30;// ���ͳɹ�
    public static byte iCurrState = GS_MAINMENU;
    
    public static boolean m_isPay = false;
    public static boolean m_isComplete = false;
    public static boolean m_isRunning = false;
    public static boolean m_isCancel = false;
    
    public static String s_sendMsgContent = "";
    public static String s_sendMsgNumber = "";
    
//  private int m_sendMsgAmount = 0; //��Ҫ���Ͷ��ŵ�������
    private static String m_hasSendMsg = "0";  //��Ҫ���͵�����
    
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
	/**��ȡ��Ϸ�ʷ���ʾ��ķ���*/
	public String getStartPrompt(){
		String strContent = "";
		if(Integer.parseInt(m_hasSendMsg) <= 0){
			strContent = JYWrapper.INI.GetStr("PAY0", "DESC1");
		} else {
			strContent = JYWrapper.INI.GetStr("PAY0", "DESC2");
		}		
		return strContent;
	}
	
	/**��ȡ�������ڷ����е���ʾ��Ϣ�ķ���*/
	public String getSendPrompt(){
		String strContent = JYWrapper.INI.GetStr("PAY0", "DESC3");
		return strContent;
	}
	
	/**��ȡ���ͳɹ���ʾ��ķ���*/
	public String getSuccessPrompt(){
		String strContent = JYWrapper.INI.GetStr("PAY0", "SENDSUCC");
		return strContent;
	}
	
	/**��ȡ����ʧ����ʾ��ķ���*/
	public String getFailPrompt(){
		String strContent = JYWrapper.INI.GetStr("PAY0", "DESC4");
		return strContent;		
	}
	
	/**
	 * ������RMS�ĵ�i����Ϣ
	 *��������ʹ������RMSһ�����漤����Ϸ�Ѿ����͵Ķ����������ܹ���Ҫ���͵�����
	 *	��һ�������¼�Ʒѵ��ϴ�ʹ�õ�ָ�����ݡ����к��뼰SP��ʾ�� ���ڵ�һ�ν���Ʒѽ���ͱ�����RMS�У�
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
	 * ����󱣴�һ����Ϣ����RMS����ʾ�Ѿ����� �ɶ�һ�Ը�����Ϸ�򰴹ؿ��շ�
	 * ��¼��Ӧ���շ���Ϣ
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
     * ���Ͷ���
     *
     * @param dest Ŀ�����
     * @param content ����
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
    
	/**ͨ����Ļ�Ĵ�С�ж�����ʾ�������Ƿ���Ҫ��ҳ������Ҫ ����з�ҳ����*/
	public static int turnPageTotal = 1; //��ʾ��Ϣ�����Ϊ����ҳ����ʼֵΪ0
	public static int currentPageNo = 1; //��ǰҳ���ǵڼ�ҳ
	public String strTurnPage(String str, int i){
		String string = "";
		String[] strBuffer = APICanvas.wrapText(str, JYWrapper.SCR_WIDTH-5);
		int strCount = strBuffer.length;
		int line = 10;
		if(JYWrapper.SCR_WIDTH < 240)
			line = 8;
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

	public void run() {
		m_isRunning = true;
		m_isComplete = false;
		sendPaintMesg();		
		if(confirm()){
			//���û���menu����ͷ���ʧ�ܽ��� �������������͡��͡����ԡ�ʱ
			if((iCurrState == GS_SENDFAIL) || (iCurrState == GS_MAINMENU)){			
				sendSMS(s_sendMsgNumber, s_sendMsgContent);					
			} else {
				//�ڷ��ͳɹ���ʾ��������ȷ����
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
			case GS_MAINMENU:  //������˳���
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
	
	//��ʾ�ʷ���ʾ����ж��Ƿ������Ҽ�
	public boolean confirm() 
	{	
//			String gameDesc = JYWrapper.INI.GetStr("CNF","DESC");
//			info = gameDesc+info;
//			
//			APICanvas.PromptSimple(null, info, null, JYWrapper.INI.GetStr("CNF","YES"), JYWrapper.INI.GetStr("CNF","NO"));

			synchronized (this)
			{
				//�ȴ����������,����ֵ1,2����ini�ļ��ж���İ����������ķ���ֵ
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
			APICanvas.SetIntResult(APICanvas.RESULT_TYPE_KEY_CODE, 0);//���ֵ
			return bconfirm;
	}
}
