package cn.thirdgwin.app;


//import uc.payment.PayPlatform;

class PayWrapper extends payWrapperSuper implements Runnable {
	private static boolean[] m_isPay;
	
	private static boolean m_isRunning = false;
	/**
	 * 发送完成，不管失败还是成功。
	 */
	private static boolean m_isComplete = false;
	/**
	 * 当前是哪个计费点。
	 */
	private static int s_index = 0;
	/**
	 * 是否取消发送。
	 */
	private static boolean m_isCancel = false;
	/**
	 * 是否发生成功。
	 */
	private static boolean m_isSuccess = false;

	/**
	 * 计费点总数。
	 */
	private static int s_pointCount = 0;
	private static int s_totalMessageCount;
	private static String m_MsgPrice;
	//private static String m_messageUnitPrice;
	//private static int s_totalMessageCount;
	private static int s_sendMessageCount;
    private String m_RMSName = "";
    
    public final static int RMS_COUNT_SEND = 0;
    
    private static int s_payPointCount;
    
	public PayWrapper(int Pay_Idx) {
		super("");
		s_index = Pay_Idx;
		m_isRunning = false;
		m_isComplete = false;
		s_pointCount = Integer.parseInt(JYWrapper.INI.GetStr("PAY1", "POINTCOUNT"));
		
		m_RMSName = JYWrapper.INI.GetStr("PAY"+s_index, "RMS");
		m_MsgPrice = JYWrapper.INI.GetStr("PAY"+s_index, "PRI");
		//m_messageUnitPrice = JYWrapper.INI.GetStr("PAY"+s_index, "PERM");

		//s_totalMessageCount = getMSGCount();
		/*int rms[] = APICanvas.RMSLoadAsInts(m_RMSName);
		if(rms != null && rms.length > 0){
		
		} else {
			s_sendMessageCount = 0;
		}*/	

	}


/*	private static int getMSGCount() {
		return (Integer.parseInt(m_MsgPrice)/Integer.parseInt(m_messageUnitPrice));
	}*/


	public void Start() {
		new Thread(this).start();
	}

	public boolean isRunning() {
		return m_isRunning;
	}

	public boolean isComplete() {
		return m_isComplete;
	}

	public static void doAction(int[] i) {
		
	}

	public void NotifyKey() {
		
	}

	public static void Init() {
		s_payPointCount = Integer.parseInt(JYWrapper.INI.GetStr("PAY1", "POINTCOUNT"));
		m_isPay = new boolean[s_payPointCount];
		for (int i = 0; i < s_payPointCount; i++) {
			int rms[] = APICanvas.RMSLoadAsInts(JYWrapper.INI.GetStr("PAY" + (i + 1), "RMS"));
			if(rms == null || rms.length == 0){
				s_sendMessageCount = 0;
			} else {
				s_sendMessageCount = rms[RMS_COUNT_SEND];
			}
			/*int totalC = Integer.parseInt(JYWrapper.INI.GetStr("PAY" + (i + 1), "PRI"))
					/ Integer.parseInt(JYWrapper.INI.GetStr("PAY" + (i + 1), "PERM"));*/
			// 因为巡洋用不上发送条数，为了不过多改动代码，所以直接写为1
			int totalC = 1;
			if (totalC <= 0) {
				s_totalMessageCount = 0;
			} else {
				s_totalMessageCount = totalC;
			}
			if (s_sendMessageCount >= s_totalMessageCount) {
				m_isPay[i] = true;
			} else {
				m_isPay[i] = false;
			}
		}
	}


	private boolean DealMessage(int price, String msg, int index) {
		boolean temp_result = false;

		xunyangpay.inter.Pay pay = xunyang.PayFactory.getPay();
		temp_result = pay.pay(
				JYWrapper._self,	//MIDlet实例
				JYWrapper.splash,	//Displayable 实例
				price, 				//价格
				msg,				//计费点的名称
				index);				//计费点编号
		return temp_result;
	}
	
	private String desc = null;

	//说明：DESC1在config.ini文件中进行配置
	public void run() {
		m_isRunning = true;
		m_isComplete = false;
		
		if(s_index > 0 && s_index <= s_pointCount) {
			desc = JYWrapper.INI.GetStr("PAY"+s_index, "DESC");
			int price = Integer.parseInt(m_MsgPrice);
			m_isSuccess = DealMessage(price, desc, s_index);
		} else {
			m_isSuccess = false;
		}
		if (m_isSuccess) {
			m_isComplete = true;
			s_sendMessageCount += 1;
			
			if (s_sendMessageCount >= s_totalMessageCount) {
				m_isPay[s_index - 1] = true;
			} else {
				m_isPay[s_index - 1] = false;
			}
			
			APICanvas.RMSSave(m_RMSName, new int[]{s_sendMessageCount});
						
			m_isCancel = false;
		} else {
			m_isCancel = true;
		}
		if (m_isCancel) {
			m_isComplete = true;
		}

	}

	public static boolean checkPayed(int index) {
    	if(index < 1 || index > s_payPointCount) {
    		System.out.println("直接打印，计费点配置或调用有问题！");
    	}
    	return m_isPay[index - 1];

   }
    

	public static boolean isPaySuccess() {
		return m_isSuccess;
	}
    
   
    public static int GetCurrentPayIndex() {
    	return s_index;
    }

     /**重置当前计费点索引。(重载)
     * @return
     */
	public static void ResetPayIndex() {
            s_index = 0;
	}
	
}
