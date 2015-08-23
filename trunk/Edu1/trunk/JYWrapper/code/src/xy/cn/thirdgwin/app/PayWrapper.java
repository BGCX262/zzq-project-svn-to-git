package cn.thirdgwin.app;


//import uc.payment.PayPlatform;

class PayWrapper extends payWrapperSuper implements Runnable {
	private static boolean[] m_isPay;
	
	private static boolean m_isRunning = false;
	/**
	 * ������ɣ�����ʧ�ܻ��ǳɹ���
	 */
	private static boolean m_isComplete = false;
	/**
	 * ��ǰ���ĸ��Ʒѵ㡣
	 */
	private static int s_index = 0;
	/**
	 * �Ƿ�ȡ�����͡�
	 */
	private static boolean m_isCancel = false;
	/**
	 * �Ƿ����ɹ���
	 */
	private static boolean m_isSuccess = false;

	/**
	 * �Ʒѵ�������
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
			// ��ΪѲ���ò��Ϸ���������Ϊ�˲�����Ķ����룬����ֱ��дΪ1
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
				JYWrapper._self,	//MIDletʵ��
				JYWrapper.splash,	//Displayable ʵ��
				price, 				//�۸�
				msg,				//�Ʒѵ������
				index);				//�Ʒѵ���
		return temp_result;
	}
	
	private String desc = null;

	//˵����DESC1��config.ini�ļ��н�������
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
    		System.out.println("ֱ�Ӵ�ӡ���Ʒѵ����û���������⣡");
    	}
    	return m_isPay[index - 1];

   }
    

	public static boolean isPaySuccess() {
		return m_isSuccess;
	}
    
   
    public static int GetCurrentPayIndex() {
    	return s_index;
    }

     /**���õ�ǰ�Ʒѵ�������(����)
     * @return
     */
	public static void ResetPayIndex() {
            s_index = 0;
	}
	
}
