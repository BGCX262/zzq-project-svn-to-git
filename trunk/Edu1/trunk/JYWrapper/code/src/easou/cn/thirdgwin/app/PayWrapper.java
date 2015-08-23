package cn.thirdgwin.app;


import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import cn.momoba.Melone;
import cn.momoba.MeloneClient;

class PayWrapper implements MeloneClient,Runnable
{
	
	//计费提示框用户回答过与否，初始化为 false;
    public boolean confirmAnswered = false;
    //用户同意是否扣费，“确认”为true,“取消”为 false;
    public boolean confirmAnswer = false;
    //联网计费线程
   // private Thread meloneThread = null;
    Melone m_me = null;
    boolean m_isRunning = false;
    boolean m_isComplete = false;
    
	public PayWrapper()
	{   
		m_isRunning = false;
	    m_isComplete = false;
		m_me = new Melone(this);
	}
	public static void Init()
	{
	}
	public void Start()
	{
		new Thread(this).start();

	}
	public void run() 
	{
		m_isRunning = true;
		m_isComplete = false;
		
		APICanvas.PromptSimple(null, JYWrapper.INI.GetStr("CNF","WAITS"), null, null, null);
		byte ret = Melone.CHARGE_FAIL;
		try{
		   ret = m_me.pay();
		}catch(Exception e){
			ret = Melone.CHARGE_FAIL;
			alert(e.toString());
		}
		int result = -1;
		if(ret == Melone.CHARGE_COMPLETE || ret == Melone.CHARGE_NONE)
		{
			result = 0x1000;
		} else
		{
			result = 0x1001;
		}

		APICanvas.SetIntResult(APICanvas.RESULT_TYPE_PAY_RET, result);
		m_isComplete = true;
		
		m_isRunning = false;
	}
	boolean isRunning()
	{
		return m_isRunning;
	}
	boolean isComplete()
	{
		return m_isComplete;
	}

	public void alert(String arg0) 
	{
		
		APICanvas.PromptSimple(null, arg0, JYWrapper.INI.GetStr("BEG","P0"), null, null);
		
		synchronized (this) 
		{
			//等待按5键,返回值3是在ini文件中定义的按5后的返回值
            while (APICanvas.GetIntResult()[APICanvas.RESULT_TYPE_KEY_CODE] != 3)
            {
                try 
                {
                    wait();
                } catch (InterruptedException e) {}
            }
        }
	}

	public boolean confirm(String arg0) {

		
		String gameDesc = JYWrapper.INI.GetStr("CNF","DESC");
		arg0 = gameDesc+arg0;
		
		APICanvas.PromptSimple(null, arg0, null, JYWrapper.INI.GetStr("CNF","YES"), JYWrapper.INI.GetStr("CNF","NO"));

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
		if(bconfirm)
		{
			APICanvas.PromptSimple(null, JYWrapper.INI.GetStr("CNF","SEND"), null, null, null);
		}
		APICanvas.SetIntResult(APICanvas.RESULT_TYPE_KEY_CODE, 0);//清键值
		return bconfirm;
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
	public static void doAction(int[] result)
	{
		if(result[APICanvas.RESULT_TYPE_PAY_RET] == 0x1000)
		{
			APICanvas.StageEndAction = APICanvas.STAGE_END_ACTION_RETURN_GAME;
		} 
		else {
			APICanvas.StageEndAction = APICanvas.STAGE_END_ACTION_EXIT;
		}
	}
	public void debugOut(String arg0) {
		//System.out.println(arg0);
	}

	public String getIMEI() {
		 String imei = System.getProperty("phone.imei");
		 
	        if (imei == null || "".equals(imei)) 
	        {
	            imei = System.getProperty("com.motorola.IMEI");
	        }
	        if (imei == null || "".equals(imei)) 
	        {
	        	imei = System.getProperty("com.nokia.mid.imei");
	        }
	        if (imei == null || "".equals(imei)) 
	        {
	        	imei = System.getProperty("com.samsung.imei");
	        }
        return imei;
	}

	public String getProduct() {
		return JYWrapper.INI.GetStr("CNF","PNAME");
		//return "9b53363b95b13";
	}

	public String getSite() {
		String siteString = null;
        try {
            // 返回产品渠道的SName，从Melone.jar中site.txt读取
            InputStream inputStream = getClass().getResourceAsStream("/site.txt");
            DataInputStream dis = new DataInputStream(inputStream);
            byte[] temp = new byte[dis.available()];
            dis.read(temp);
            siteString = new String(temp, "GB2312");
        } catch (IOException ex) {
            ex.getMessage();
            return null;
        }
        return siteString;
		//return "9b53363b95b13b13";
		///return "2222";

	}

	public String getUserAgent() {
		String ua;
        ua = System.getProperty("microedition.platform");
        return ua;

	}
	
	
	
	
}
