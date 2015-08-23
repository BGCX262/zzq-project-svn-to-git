package cn.thirdgwin.app;

import javax.microedition.lcdui.Display;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreNotFoundException;

import com.downjoy.j2me.smspack.views.*;
import com.downjoy.j2me.smspack.util.*;

class PayWrapper extends payWrapperSuper implements com.downjoy.j2me.smspack.views.PayedCallback
{
	final static String INI_CNF_SEC = "CNF";
	final static String INI_FEEPOINT_SEC = "PAY";
	
	/** cp的客服电话 
	 * 将电话写在代码中的好处是，以后更换电话，直接改代码，所有游戏都直接修改了
	 */
	private static String s_cpTel = "028-85268736";
	
	/** cp的编号 
	 * 将cpid写在代码中的好处是，以后更换cpid，直接改代码，所有游戏都直接修改了
	 */
	private static String s_cpID = "122";
	
	/** game的编号*/
	private static String s_gameID;
	
	/** device的编号 */
	private static String s_deviceID;
	
	/** operation的编号 */
	private String m_opID;
	
	/** price价格 */
	private int m_price;
	
	/** 支付状态 */
	final static int PAY_STATE_INIT		= 0;	//初始化
	final static int PAY_STATE_PAYING	= 1;	//正在支付
	final static int PAY_STATE_COMPLETED 	= 2;	//完成支付（成功或者用户取消）
	
	private int s_payState = PAY_STATE_INIT;
	
	private static boolean s_isPaySucceed = false;
	
	private static int s_payPointCount = 0;
	private static boolean[] m_isPPointPaid;
	private String m_RMSName;
	
    public PayWrapper(int Pay_Idx) {
        super("");    //form标题 订购	
		
        m_currentPayPoint = Pay_Idx;
        
        s_payState = PAY_STATE_INIT;
        s_isPaySucceed = false;
        
        m_opID = JYWrapper.INI.GetStr(INI_FEEPOINT_SEC + m_currentPayPoint, "OPCODE");
        m_price = JYWrapper.INI.GetInt(INI_FEEPOINT_SEC + m_currentPayPoint, "PRI");
        
        m_RMSName = JYWrapper.INI.GetStr(INI_FEEPOINT_SEC + Pay_Idx, "RMS");
    }
    
    public void Start()
    {
    	String name = JYWrapper.INI.GetStr(INI_FEEPOINT_SEC + m_currentPayPoint, "NAME");
    	String desc = JYWrapper.INI.GetStr(INI_FEEPOINT_SEC + m_currentPayPoint, "DESC");
		
    	SMSPayCanvas payCanvas = new SMSPayCanvas(
    			JYWrapper._self,	//MIDlet 
    			JYWrapper.splash,	//付费后返回的屏幕 
    			s_cpID,				//厂商编号 	3 位数字
    			s_gameID, 			//游戏编号	3 位数字
    			m_opID, 			//动作ID(计费点ID)	2位数字，可留空
    			s_deviceID, 		//机型编号	3 位数字，可留空  
    			m_price, 			//当前计费点所需价格		小于 2 位的偶数
    			name, 				//道具或游戏名称
    			desc, 				//游戏或道具说明
    			s_cpTel				//厂商客服电话
    			);
    	
    	payCanvas.setPayedCallback(this);
    	
    	Display.getDisplay(JYWrapper._self).setCurrent(payCanvas);
    	    	
    	try {//N73机器需要在setcurrent之后sleep一段时间供界面切换，否则会导致切换失败
			Thread.sleep(100);
		} catch (InterruptedException e) {
		}
    	
    	s_payState = PAY_STATE_PAYING;
    }

    boolean isRunning()
    {
        return (s_payState == PAY_STATE_PAYING);
    }
    boolean isComplete()
    {
    	return (s_payState == PAY_STATE_COMPLETED);
    }

    public static void doAction(int[] i)
    {
    	//TODO: （付费结束之后）这里可以处理付费过程中的消息些
    }

    public void NotifyKey()
    {
    	//TODO: 按键（实时）的处理
    }

    public static void Init()
    {
		s_payPointCount = Integer.parseInt(JYWrapper.INI.GetStr(INI_CNF_SEC, "POINTCOUNT"));
		m_isPPointPaid = new boolean[s_payPointCount];
		for (int i = 0; i < s_payPointCount; i++) {
			int rms[] = APICanvas.RMSLoadAsInts(JYWrapper.INI.GetStr("PAY" + (i + 1), "RMS"));
			if(rms == null || rms.length == 0){
				m_isPPointPaid[i] = false;
			} else {
				m_isPPointPaid[i] = true;
			}
		}
		
    	String cpTel = JYWrapper.INI.GetStr(INI_CNF_SEC, "CPTEL");
        if (cpTel != null) {//如果没有单独配置，则使用默认值（可以用来进行老版本兼容）
        	s_cpTel = cpTel;
        }
        String cpID = JYWrapper.INI.GetStr(INI_CNF_SEC, "CPID");
        if (cpID != null) {//如果没有单独配置，则使用默认值（可以用来进行老版本兼容）
        	s_cpID = cpID;
        }
        s_gameID = JYWrapper.INI.GetStr(INI_CNF_SEC, "GAMEID");
        s_deviceID = JYWrapper.INI.GetStr(INI_CNF_SEC, "DEVID");
    }

    public static boolean checkPayed(int index) {
    	if(index < 1 || index > s_payPointCount) {
    		System.out.println("直接打印，计费点配置或调用有问题！");
    	}
    	
    	return m_isPPointPaid[index - 1];
//使用Dangle提供的只能检查该类道具是否购买过，而并不能实现检测    	
//    	String opCode = JYWrapper.INI.GetStr(INI_FEEPOINT_SEC + index, "OPCODE");
//    	int price = JYWrapper.INI.GetInt(INI_FEEPOINT_SEC + index, "PRI");
//    	return RmsUtil.isPayFinish(s_cpID, s_gameID, opCode, price);
    }

	public static boolean isPaySuccess() {
		return (s_isPaySucceed);
	}

	public void callback(boolean bSuccess) {
		
		s_payState = PAY_STATE_COMPLETED;
		
		s_isPaySucceed = bSuccess;//RmsUtil.isPayFinish(s_cpID, s_gameID, m_opID, m_price);
		
		if (s_isPaySucceed) {
			String buyOnce = JYWrapper.INI.GetStr(INI_FEEPOINT_SEC + m_currentPayPoint, "BUYONCE");
//			if (buyOnce != null && buyOnce.equals("1")) 
//			{//一次性道具
//			} 
//			else 
			{//可重复购买道具
//				try {
//					String opID = (m_opID == null) ? "00" : m_opID;
//					
//					RecordStore.deleteRecordStore(RmsUtil.getRecordStoreName(s_cpID, s_gameID, opID));
//				} catch (RecordStoreNotFoundException e) {
//					e.printStackTrace();
//				} catch (RecordStoreException e) {
//					e.printStackTrace();
//				}
				
				if (buyOnce != null && buyOnce.equals("1")) 
				{//一次性道具
					if (m_RMSName != null)
						APICanvas.RMSSave(m_RMSName, new int[]{1});
					
					m_isPPointPaid[m_currentPayPoint - 1] = true;
				}
			}
		}
	}
   
}
