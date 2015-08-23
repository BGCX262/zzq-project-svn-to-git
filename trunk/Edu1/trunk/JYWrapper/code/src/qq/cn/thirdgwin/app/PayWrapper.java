package cn.thirdgwin.app;

import com.qq.sms.*;
import java.io.IOException;
import javax.microedition.io.Connector;
import javax.microedition.rms.RecordStore;
import javax.wireless.messaging.MessageConnection;
import javax.wireless.messaging.TextMessage;


class PayWrapper extends payWrapperSuper implements Runnable
{
    private static boolean[] m_isPay;//各种计费点的购买情况，将被存档
    private static byte[]     m_smsLefts;//每条计费点还剩多少短信
    private static byte[]     m_smsTotals;//每条计费点共要多少短信
    private static byte s_smsLeft;//当前计费点剩余条数
    private static byte s_smsTotal;//当前计费点总条数
    private static byte s_smsPrice;//总价
    private static byte s_smsPriceUnit;//单价

    //paywrapper通用变量
    private static boolean m_isComplete = false;
    private static boolean m_isRunning = false;
    private static boolean m_isSuccess = false;
    
    private static boolean m_isGetFail = false;

    private static int s_payPointCount = 0;//一共几个计费点

    /*
     * 发送状态
     */
    private static final int SMS_SEND_DESC       = 0;
    private static final int SMS_SENDING         = 1;
    private static final int SMS_SEND_FAIL       = 2;
    private static final int SMS_SEND_RETRY      = 3;
    private static final int SMS_SEND_SUCCESS    = 4;
    private static int s_smsSendStatus = SMS_SEND_DESC;
    /*
     * 发送时的提示
     */
    private static final int SMS_INFO_NONE       = -1;
    private static final int SMS_INFO_DESC       = 0;
    private static final int SMS_INFO_SENDING    = 1;
    private static final int SMS_INFO_FAIL       = 2;
    private static final int SMS_INFO_RETRY      = 3;
    private static final int SMS_INFO_SUCCESS    = 4;
    private static final int SMS_INFO_YES        = 5;
    private static final int SMS_INFO_NO         = 6;
    private static final int SMS_INFO_BACK       = 7;
    private static final int SMS_INFO_MONEY      = 8;
    private static final int SMS_INFO_GET_FAIL   = 9;
    private static final int SMS_SEND_INFO_NUM   = 10;
    private static final String[] SEND_TIPS = new String[SMS_SEND_INFO_NUM];
    /*
     * 存档名
     */
    private static String s_rmsName;
    /*
     * 腾讯相关
     */
    private QQSMS mQQSms;
    private String[] smsCode;//腾讯API返回的短信内容和短信号码
    private static final int CPID = 174;
    private static final int SMS_PRICE_UINT = 2;//短代单价2元
    /**
     * 初始化PayWrapper,主要是读存档
     * （会在一游戏刚启动时被APICanvas调用）,先于此类的构造方法
     */
    public static void Init() 
    {
        s_payPointCount = Integer.parseInt(JYWrapper.INI.GetStr("CNF", "POINTCOUNT"));
        m_isPay = new boolean[s_payPointCount];
        m_smsLefts = new byte[s_payPointCount];
        m_smsTotals = new byte[s_payPointCount];
        for (int i = 0;i<s_payPointCount;i++)
        {
            m_smsLefts[i] = m_smsTotals[i] = -1;
        }
        s_rmsName = JYWrapper.INI.GetStr("CNF", "RMS");
        RMS_Load();

    }
    /**
     * 检查是否已经付过费
     * @param index：存档点ID
     * @return
     */
    public static boolean checkPayed(int index) 
    {
        if (index < 1 || index > s_payPointCount || m_isPay == null)
        {
            return false;
        }
        return m_isPay[index - 1];
    }
    public static boolean isPaySuccess()
    {
        return m_isSuccess;
    }


    public PayWrapper(int payIndex) 
    {
        super(null);
        m_isComplete = false;
        m_isRunning = false;
        m_isSuccess = false;

        m_currentPayPoint = payIndex;
        s_smsSendStatus = SMS_SEND_DESC;
        mQQSms = new QQSMS(JYWrapper._self);
        getTips();
        m_isGetFail = !getSmsPayInfo();

    }
    private boolean getSmsPayInfo()
    {
        s_smsPrice = (byte)Integer.parseInt(JYWrapper.INI.GetStr(APICanvas.CurrentStageName, "PRI"));
        s_smsPriceUnit = SMS_PRICE_UINT;//单价

        int mod = mQQSms.getFeeMode();

        if (mod == 0)//短代模式
        {
            s_smsPriceUnit = SMS_PRICE_UINT;
            smsCode = mQQSms.requestSMS(s_smsPriceUnit);
            if (smsCode == null)
            {
                return false;
            }
            smsCode[0] = getSmsModeContent(smsCode[0]);
        }
        else if (mod == 1)//DO模式
        {
            s_smsPriceUnit = s_smsPrice;
            int opCode=Integer.parseInt(JYWrapper.INI.GetStr(APICanvas.CurrentStageName, "OPCODE"));
            smsCode = mQQSms.getDoFeeCode(opCode,CPID);
        }
        
        if (smsCode == null)
        {
            return false;
        }

        if (m_smsTotals[m_currentPayPoint - 1] != -1)
        {
            s_smsTotal = m_smsTotals[m_currentPayPoint - 1];
        }
        else
        {
            s_smsTotal = (byte)(s_smsPrice / s_smsPriceUnit);
        }

        if (m_smsLefts[m_currentPayPoint - 1] > 0)
        {
            s_smsLeft = m_smsLefts[m_currentPayPoint - 1]; 
        }
        else
        {
            s_smsLeft = (byte)(s_smsPrice / s_smsPriceUnit);
        }

        return true;

    }
    private String getSmsModeContent(String oriCode)
    {
    	String gameID 		= JYWrapper.INI.GetStr("CNF", "GAMEID");
    	String firstChn 	= mQQSms.getChannelFirst();
    	String secondChn 	= mQQSms.getChannelSecond();
    	String interChn 	= mQQSms.getInterChannel();
    	String term 		= mQQSms.getTerm();	
    	String QQ 			= mQQSms.getQQ();
    	String feeType 		= JYWrapper.INI.GetStr(APICanvas.CurrentStageName, "FEETYPE");
    		
    	String code = oriCode + " " + CPID + " " + gameID + " Z600 "
    				+ firstChn +" " + secondChn +" "+ interChn+" " 
    				+ term +" " + QQ + " " + feeType +" "+getNum2(m_currentPayPoint) + " Z600";
    	
    	return code;
    }
    private String getNum2(int num)
    {
    	return ""+ num/10 + num % 10;
    }
    private static void getTips()
    {
        INIConfig ini = JYWrapper.INI;

        SEND_TIPS[SMS_INFO_DESC]    = ini.GetStr(APICanvas.CurrentStageName, "DESC");//计费描述
        SEND_TIPS[SMS_INFO_SENDING] = ini.GetStr("CNF", "SENDING");//发送中
        SEND_TIPS[SMS_INFO_FAIL]    = ini.GetStr("CNF", "SENDFAIL");//发送失败
        SEND_TIPS[SMS_INFO_GET_FAIL]= ini.GetStr("CNF", "GETINFOERR");//获取jad信息失败
        SEND_TIPS[SMS_INFO_RETRY]   = ini.GetStr("CNF", "RETRY");//是否重试
        SEND_TIPS[SMS_INFO_YES]     = ini.GetStr("CNF", "YES");//是
        SEND_TIPS[SMS_INFO_NO]      = ini.GetStr("CNF", "NO");//否
        SEND_TIPS[SMS_INFO_BACK]    = ini.GetStr("CNF", "BACK");//返回
        SEND_TIPS[SMS_INFO_MONEY]   = ini.GetStr("CNF", "MONINFO");//付费信息
        SEND_TIPS[SMS_INFO_SUCCESS] = ini.GetStr(APICanvas.CurrentStageName, "SENDSUCC");//发送成功
    }
    public void Start()
    {	
    	m_isComplete = false;
        m_isRunning = true;
        m_isSuccess = false;
        new Thread(this).start();
    }

    
    public void run() 
    {  
        if(m_isGetFail)
        {
            s_smsSendStatus = SMS_SEND_FAIL;
            setTip(SMS_INFO_GET_FAIL,SMS_INFO_BACK,SMS_INFO_NONE);
            confirmOK();
            exit();        
            return;
        }
        s_smsSendStatus = SMS_SEND_DESC;
        setTip(SMS_INFO_DESC,SMS_INFO_YES,SMS_INFO_NO);
        resetKeyCode();
        boolean ret = confirm();
        if (ret)
        {
            SendPayMsg();
        }
        else
        {
            m_isSuccess = false;
            m_isPay[m_currentPayPoint - 1] = false;
            m_isComplete = true;
            m_isRunning = false;
        }
    } 
    /**
     * 设置要显示在屏幕上的信息
     */
    private void setTip(int id,int idLeft,int idRight)
    {
        String left =  (idLeft == SMS_INFO_NONE?null:SEND_TIPS[idLeft]);
        String right = (idRight == SMS_INFO_NONE?null:SEND_TIPS[idRight]);

        String content = SEND_TIPS[id];
        if (id == SMS_INFO_DESC)
        {
            String payInfo = JYWrapper.INI.GetStr("CNF", "MONINFO");
            int index1 = payInfo.indexOf('@');//总价
            int index2 = payInfo.indexOf('$');//剩余短信条数
            int index3 = payInfo.indexOf('%');//单价
            int index4 = payInfo.length();

            payInfo = payInfo.substring(0,index1) + s_smsPrice
                      + payInfo.substring(index1+1,index2) + s_smsLeft
                      + payInfo.substring(index2+1,index3) + s_smsPriceUnit
                      + payInfo.substring(index3+1,index4);

            content += payInfo;          
        }
        APICanvas.PromptSimple(null, content, null,left , right);
    }
    boolean isRunning()
    {
        return m_isRunning;
    }
    boolean isComplete()
    {
        return m_isComplete;
    }

    public static void doAction(int[] result)
    {

    }
    /**清键值**/
    private void resetKeyCode(){
    	APICanvas.SetIntResult(APICanvas.RESULT_TYPE_KEY_CODE, 0);
    }
    private boolean confirmOK() 
    {
        int keyLeft = Integer.parseInt(JYWrapper.INI.GetStr(APICanvas.CurrentStageName, "R0"));
        int keyRight = Integer.parseInt(JYWrapper.INI.GetStr(APICanvas.CurrentStageName, "R1"));
        synchronized (this)
        {
            //等待按左右软键,返回值1,2是在ini文件中定义的按左，右软键后的返回值
            while (APICanvas.GetIntResult()[APICanvas.RESULT_TYPE_KEY_CODE] != keyLeft
            		//TODO:短代模式下，遇到诡异bug暂时这样处理下。
            		|| APICanvas.GetIntResult()[APICanvas.RESULT_TYPE_KEY_CODE] == keyRight
            		|| s_smsSendStatus == SMS_SENDING)
            {             
                try
                {
                    wait();
                }
                catch (InterruptedException e)
                {
                    return false;
                }

            }
        }
        APICanvas.SetIntResult(APICanvas.RESULT_TYPE_KEY_CODE, 0);//清键值
        return true;
    }
    
    private boolean confirm() 
    {
        int keyLeft = Integer.parseInt(JYWrapper.INI.GetStr(APICanvas.CurrentStageName, "R0"));
        int keyRight = Integer.parseInt(JYWrapper.INI.GetStr(APICanvas.CurrentStageName, "R1"));

        synchronized (this)
        {
            //等待按左右软键,返回值1,2是在ini文件中定义的按左，右软键后的返回值
            while (APICanvas.GetIntResult()[APICanvas.RESULT_TYPE_KEY_CODE] != keyLeft 
                   && APICanvas.GetIntResult()[APICanvas.RESULT_TYPE_KEY_CODE] != keyRight
                   //TODO:短代模式下，遇到诡异bug暂时这样处理下。
                   || s_smsSendStatus == SMS_SENDING)
            {
                try
                {
                    wait();
                }
                catch (InterruptedException e)
                {
                    return false;
                }

            }
        }
        boolean bconfirm = (APICanvas.GetIntResult()[APICanvas.RESULT_TYPE_KEY_CODE] == keyLeft);

        APICanvas.SetIntResult(APICanvas.RESULT_TYPE_KEY_CODE, 0);//清键值
        return bconfirm;
    }
    public void NotifyKey()
    {
        synchronized(this)  
        {
            try
            {
                notify();
            }
            catch (Exception e)
            {
            }
        }
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
    private void SendPayMsg()
    {
        if (s_smsSendStatus == SMS_SENDING || s_smsLeft <=0)
        {
            return;
        }
        s_smsSendStatus = SMS_SENDING;
        setTip(SMS_INFO_SENDING,SMS_INFO_NONE,SMS_INFO_NONE);
        resetKeyCode();
        boolean shouldExit = false;
//      m_isSuccess = false;
        boolean suc = sendAMessage(smsCode[0], smsCode[1]);
        if (suc)
        {
     	 
            s_smsLeft--;
            if (s_smsLeft <= 0)
            {
                s_smsSendStatus = SMS_SEND_SUCCESS;
                setTip(SMS_INFO_SUCCESS,SMS_INFO_BACK,SMS_INFO_NONE);
                resetKeyCode();
                if(confirmOK())
                {
                	shouldExit  = true;
                }
            }
            else
            {
                s_smsSendStatus = SMS_SEND_DESC;
                setTip(SMS_INFO_DESC,SMS_INFO_YES,SMS_INFO_NO);
                resetKeyCode();          
                boolean ret = confirm();
                if (ret)
                {
                    SendPayMsg();
                }
                else
                {
                    m_isSuccess = false;
                    shouldExit  = true;
                }
            }
            saveSMS();

        }
        else
        {
            saveSMS();
            s_smsSendStatus = SMS_SEND_RETRY;
            setTip(SMS_INFO_RETRY,SMS_INFO_YES,SMS_INFO_NO);
            resetKeyCode();
            boolean ret = confirm();
            if (ret)
            {
                SendPayMsg();
            }
            else
            {
                s_smsSendStatus = SMS_SEND_FAIL;
                setTip(SMS_INFO_FAIL,SMS_INFO_BACK,SMS_INFO_NONE);
                if(confirmOK())
                {
	                m_isSuccess = false;
	                shouldExit  = true;
                }
            }

        }

        if (shouldExit)
        {
            exit(); 
        }

    }
    private void exit()
    {
        APICanvas.PromptChangeCurrent(0,null, null, null,null , null);
        m_isComplete = true;
        m_isRunning = false;
    }
    private void saveSMS()
    {
        m_smsLefts[m_currentPayPoint - 1]   = s_smsLeft;
        m_smsTotals[m_currentPayPoint - 1]  = s_smsTotal;
        m_isPay[m_currentPayPoint - 1]      = m_isSuccess;
        RMS_Save();
    }
    private boolean sendAMessage(String Content,String phoneNumber)   
    {   
    	m_isSuccess = false;
        String address="sms://"+phoneNumber; 
        MessageConnection messageConnection = null;   
        try
        {
            messageConnection =(MessageConnection) Connector.open(address);   
            TextMessage textMessage =(TextMessage) messageConnection.newMessage(MessageConnection.TEXT_MESSAGE);   
            textMessage.setPayloadText(Content); 
            messageConnection.send(textMessage); 
            m_isSuccess = true;
        }
        catch (SecurityException se)
        {
        	m_isSuccess = false;
        }
        catch (IOException e)
        {
        	m_isSuccess = false; 
        } 
        catch(Exception e)
        {
        	m_isSuccess = false;
        }
        finally
        {
            try
            {
            	if(messageConnection != null)
            	{
            		messageConnection.close(); 
            		//E680必须置空，否则当发送多条或者重试的时候，
            		//取消发送中间的某条短信时，程序将会阻塞在messageConnection.close(); 
            		messageConnection = null;
            	}
            }
            catch (IOException e)
            {
            }
        }   
        return m_isSuccess;   
    }

}
