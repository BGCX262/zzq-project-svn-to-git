package cn.thirdgwin.app;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import javax.microedition.rms.InvalidRecordIDException;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreNotOpenException;

import cn.thirdgwin.app.APICanvas;
import cn.thirdgwin.app.JYWrapper;
import cn.thirdgwin.app.payWrapperSuper;

import uc.payment.PayPlatform;

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
	private final String m_cpid = "184";
	private String m_gameid;
	private final String m_opobject = "01";
	/**
	 * 计费点总数。
	 */
	private static int s_pointCount = 0;
	private static String m_MsgPrice;
	private static String m_messageUnitPrice;
	private static String m_messageContent;
	private static String m_messageNum;
	private static int s_totalMessageCount;
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
		m_messageUnitPrice = JYWrapper.INI.GetStr("PAY"+s_index, "PERM");
		m_messageContent = getMSGContent();
		m_messageNum = getNumbers();
		s_totalMessageCount = getMSGCount();
		int rms[] = APICanvas.RMSLoadAsInts(m_RMSName);
		if(rms != null && rms.length > 0){
			s_sendMessageCount = rms[RMS_COUNT_SEND];//getSendMessage(m_RMSName, RMS_COUNT_SEND);
//			s_totalMessageCount = getSendMessage(m_RMSName, RMS_TOTAL_COUNT);
//			m_messageNum = getSendMessage(m_RMSName, RMS_MSG_NUMBER);
//			m_messageContent = getSendMessage(m_RMSName, RMS_MSG_CONTENT);
//			if(s_index > 1) {
//				if(s_sendMessageCount>=s_totalMessageCount) {
//					s_sendMessageCount = 0;
//					m_messageContent = getMSGContent();
//					m_messageNum = getNumbers();
//					s_totalMessageCount = getMSGCount();
//				}
//			}			
		} else {
			s_sendMessageCount = 0;
		}	
	//	if (s_opCode == null) {
	//		s_opCode = "00";
	//	}

	}
	/**
	 * 获得相对RMS的第i条信息
	 *本程序中使用两个RMS一个储存激活游戏已经发送的短信条数与总共需要发送的条数
	 *	另一个储存记录计费点上次使用的指令内容、上行号码及SP提示语 （在第一次进入计费界面就保存在RMS中）
	 */
//	private static String getSendMessage(String strRecordName, int i) {
//		String result = "";
//		try {
//			RecordStore rs = RecordStore.openRecordStore(strRecordName, true);
//			if (rs.getNumRecords() > (i - 1)) {
//				byte temp[] = rs.getRecord(i);
//				ByteArrayInputStream bais = new ByteArrayInputStream(temp);
////				DataInputStream dis = new DataInputStream(bais);
////				result = dis.readUTF();
////				dis.close();
//				bais.close();
//			}
//			if (rs != null)
//				rs.closeRecordStore();
//		} catch (RecordStoreNotOpenException ex) {
//			ex.printStackTrace();
//		} catch (RecordStoreException ex) {
//			ex.printStackTrace();
//		} catch (IOException ex) {
//			ex.printStackTrace();
//		}
//		return result;
//	}
//	private static boolean RmsHasMessage(String strRecordName){
//		boolean hasMessage = false;
//		try{
//			RecordStore rs = RecordStore.openRecordStore(strRecordName, true);
//			if (rs.getNumRecords() >= 1) {  //查看RMS中是否有 所需要记录的6条信息
//				hasMessage = true;
//			}
//		}catch(Exception ex){			
//		}
//		return hasMessage;
//	}
	/**
	 * 付款后保存一条信息到到RMS，表示已经付款 可对一性付款游戏或按关卡收费
	 * 记录响应的收费信息
	 */
//	private static void saveToRMS(String value, String strRecordName, int i) {
//		ByteArrayOutputStream bos = new ByteArrayOutputStream();
////		DataOutputStream dos = new DataOutputStream(bos);
//		try {
////			dos.writeUTF(value);
////			dos.flush();
//			byte temp[] = bos.toByteArray();
//			RecordStore rs = RecordStore.openRecordStore(strRecordName, true);
//			if (rs.getNumRecords() > (i - 1)) {
//				rs.setRecord(i, temp, 0, temp.length);
//			} else {
//				rs.addRecord(temp, 0, temp.length);
//			}
//			bos.close();
////			dos.close();
//			rs.closeRecordStore();
//		} catch (RecordStoreNotOpenException ex) {
//			ex.printStackTrace();
//		} catch (InvalidRecordIDException ex) {
//			ex.printStackTrace();
//		} catch (RecordStoreException ex) {
//			ex.printStackTrace();
//		} catch (IOException ex) {
//			ex.printStackTrace();
//		}
//	}
	private static String getMSGContent() {
		return "01";
	}

	private static int getMSGCount() {
		return (Integer.parseInt(m_MsgPrice)/Integer.parseInt(m_messageUnitPrice));
	}

	private static String getNumbers() {
		return "123456879";
	}
//	public static void RMS_PayInit() {
//		s_rmsName = JYWrapper.INI.GetStr("PAY0", "RN");
//		if(s_rmsName == null) {
//			System.out.println("有问题啊,得到的rms名字为空。");
//			return;
//		}
//		int[] data = APICanvas.RMSLoadAsInts(s_rmsName);
//		if(data != null)
//			s_isPay = data[RMS_IS_BUY]==1?true:false;
//	}

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
//			getSendMessage(
//					JYWrapper.INI.GetStr("PAY" + (i + 1), "RMS"),
//					RMS_COUNT_SEND);
//			if ((s_sendMessageCount.equals(""))
//					|| (Integer.parseInt(s_sendMessageCount) < 0)) {
//				s_sendMessageCount = "0";
//			}
			int totalC = Integer.parseInt(JYWrapper.INI.GetStr("PAY" + (i + 1), "PRI"))
					/ Integer.parseInt(JYWrapper.INI.GetStr("PAY" + (i + 1), "PERM"));
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


	private boolean DealMessage(String msg, String cpid, String gameID,
			String opcode, String opobject, int U) {
		boolean temp_result = false;

		temp_result = PayPlatform.launchPay(JYWrapper._self, // MIDlet
				JYWrapper.splash, // 付费后要恢复的屏幕
				msg, // 付费内容提示信息
				cpid, // cpid CP编号
				gameID, // gameid 游戏编号
				opcode, // opcode 计费点ID
				opobject, // opobject 购买对象
				U // U点数量
				);
		return temp_result;

	}

//	private static String s_rmsName = "U";
//	private final static int RMS_IS_BUY = 0;
//	private final static int RMS_NUMBER = RMS_IS_BUY + 1;
		
	private String desc = null;
	private String opCode = "00";
//	private int value = 0;
	//说明：DESC1在config.ini文件中进行配置
	public void run() {
		m_isRunning = true;
		m_isComplete = false;
		m_gameid = JYWrapper.INI.GetStr("PAY1", "GAMEID");
		
		if(s_index > 0 && s_index <= s_pointCount) {
//			boolean sendMsg = true;
//			String temp = JYWrapper.INI.GetStr("PAY"+s_index, "PCODE"+s_index);
//			int operate = temp.indexOf(",");
			desc = JYWrapper.INI.GetStr("PAY"+s_index, "DESC");
			opCode = JYWrapper.INI.GetStr("PAY"+s_index, "OPCODE");
//			if (opCode.equals(BUY_ONCE) && s_isPay) {
//				sendMsg = false;
//				m_isSuccess = false;
//			}
//			if(sendMsg) {
//			value = JYWrapper.INI.GetStr("PAY"+s_index, "DESC");
			int value = Integer.parseInt(JYWrapper.INI.GetStr("PAY"+s_index, "PRI"));
			m_isSuccess = DealMessage(desc, m_cpid, m_gameid, opCode, m_opobject, value);
//			}
//			System.out.println("opCode:"+opCode+" value:"+value+" ");
		} else {
			m_isSuccess = false;
		}
//		m_isSuccess = true;
		if (m_isSuccess) {
			m_isComplete = true;
			s_sendMessageCount += 1;
			
			if (s_sendMessageCount >= s_totalMessageCount) {
				m_isPay[s_index - 1] = true;
			} else {
				m_isPay[s_index - 1] = false;
			}
			
//			saveToRMS(s_sendMessageCount,m_RMSName,RMS_COUNT_SEND);
			APICanvas.RMSSave(m_RMSName, new int[]{s_sendMessageCount});
			
			if (!opCode.equals(BUY_ONCE)) {
				PayPlatform.updatePaidMoney(opCode, // op 付费类型
						0 // 这个计费点已付U点
						);
//				s_isPay = true;
//				int[] data = new int[RMS_NUMBER];
//				data[RMS_IS_BUY] = s_isPay?1:0;
//				APICanvas.RMSSave(s_rmsName, data);
				
//			} else if (opCode.equals(BUY_ITEMS)) {
//				PayPlatform.updatePaidMoney(opCode, // op 付费类型
//						0 // 这个计费点已付U点
//						);
//
//			} else {
			}
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
//    	if(JYWrapper.INI.GetStr("PAY"+index, "OPCODE").equals(BUY_ONCE))
    	return m_isPay[index - 1];

//    	return false;
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
    
	private final static String BUY_ITEMS = "01";
	private final static String UPLOAD = "02";
	private final static String BUY_ONCE = "03";
	private final static String DOWNLOAD_MAP = "04";
	private final static String PASS_MISSION = "05";
	private final static String REBORN = "06";
	private final static String OTHER = "99";

	
}
