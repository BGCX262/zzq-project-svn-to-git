package cn.thirdgwin.app;

/**
 *                                  开 发 注 意
 *
 *     1.需要用户短信付款前，请在游戏中以明文形式向用户说明收费，（参考）："本游戏收
 * 费**元，如果支付，请选“是”，稍候将会收到收费提示短信。"，如果是通过电话费买游戏
 * 币的，可以提示为“一元可换得游戏币50，如果支付，请选“是”，稍候将会收到收费提示短
 * 信。”并可以给用户选择是否发送这操作。发送信息的时间因手机的不同，所用时间会有长短，
 * 必要时请用界面形式说明，如“发送中...”。
 *
 *     2.发送信息后,请根据返回值作相应的处理，如发送成功，用户取消了，或发送失败。
 *
 *	   3.发送方式,代码,界面可以不一样,但发送信息必需读取w.w文件和保存已成功发送信息.
 ,w.w文件里面保存有游戏编号.u.u文件保存的是发送指令和号码,可以读取也可以写在代码里,
 不读取.即发送内容为:指令加上w.w文件内容.(如完整的发送内容:ZFMG100000W100000PS0PM12)
 PS为手机机型,现在不统计可以为:0.
 *
 *								主要几种付费方式
 *
 * 一、按关收费
 *
 * 二、道具/技能收费
 *
 * 三、复活/时间收费
 *
 * 四、游戏币收费
 *
 * 五、一次性收费
 *
 * 六、共同商议
 */

//import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
//import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
//import java.io.InputStream;
import javax.microedition.io.Connector;
import javax.microedition.rms.InvalidRecordIDException;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreNotOpenException;
import javax.wireless.messaging.MessageConnection;
import javax.wireless.messaging.TextMessage;

/*
 * MsgPay.java
 * 用于通过短信的形式,用户把指令发送SP的端口号上进行付款
 *
 * 测试通过的手机型号包括
 * Moto E398 V300
 * Nokia 3230 7260 6101 3100 (QD,7650不支持)
 * 索爱 W550C  (K700C,K300C不支持)
 * 详细说明,请看最底部的文字说明
 */

public final class WrapperPay {
	/**
	 * 端口号 即发送SP在移动上所注册的端口号,由SP规定 暂时有两个，如有新的，日后补充
	 * 测试时,程序员可以修改为任意中国移动的手机号码,如"13888888888"、自己或同事的号码
	 */

	/**
	 * 指令内容 即所发信息的内容,由SP规定 暂时有一个，如有新的，日后补充 测试时可以任意内容,如"你好"
	 */

	/** 是否已经付款的记录名 */
	private static String strRecordName = "payRecode";

	// 发送消息结果
	/** 发送成功 */
	public static final byte SEND_SUCCESS = 1;
	/** 发送失败，发送时用户按了取消 */
	public static final byte SEND_USERCANCLE = 2;
	/** 发送失败，手机卡过期，非移动的电话卡，或余额不足订购费用其它失败原因 */
	public static final byte SEND_FAIL = 3;
	/** 没有记录 */
	public static final byte SEND_NOREC = 4;

	public WrapperPay(String rmsname) {
		strRecordName = rmsname;
	}

	public byte pay(String[] _temp) {
		return sendMessage(_temp[0], _temp[1]);
	}

	/**
	 * 发送信息 String strPort为端口号，请在以PORT_开头的常量里选 String
	 * strDictate为指令号，请在以DICTATE_开头的常量里选 调用时，要放到线程里使用 返回TRUE即发送成功, 返回FALSE即发送失败,
	 * 当手机系统询问用户是否发信息时，用户选择“否”或“不发送”，也会返回FALSE
	 */
	private byte sendMessage(String strPort, String strDictate) {
		// 返回值
		byte result = SEND_FAIL;
		MessageConnection conn = null;
		// strDictate = "测试"; //读取指令.
		// strPort = "15982335502"; //读取发送号码

		try {
			// 地址
			String address = "sms://" + strPort;
			// 建立连接
			conn = (MessageConnection) Connector.open(address);
			// 设置短信息类型为文本，短信息有文本和二进制两种类型
			TextMessage msg = (TextMessage) conn
					.newMessage(MessageConnection.TEXT_MESSAGE);
			msg.setAddress(address);
			// 设置信息内容
			msg.setPayloadText(strDictate);
			// 发送
			conn.send(msg);
			result = SEND_SUCCESS;
			// System.out.println("getPassedLevel()==="+getPassedLevel());
		} catch (SecurityException ex1) {
			// 当手机系统询问用户是否发出信息，用户选择“否”时，会抛出这个异常
			// 不想显示时，可以把 ex1.printStackTrace() 语句注释
			// 未处理
			result = SEND_USERCANCLE;
			System.out.println("SEND_USERCANCLE");
			// ex1.printStackTrace();
		} catch (IOException ex) {
			System.out.println("SEND_FAIL");
			// 未处理
			result = SEND_FAIL;
			// ex.printStackTrace();
		} catch (IllegalArgumentException ex) {
        	result = SEND_FAIL;
		}catch (Exception ex) {
			System.out.println("SEND_NOREC");
			result = SEND_NOREC;
			// ex.printStackTrace();
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * RMS中是否有已缴费的记录 只要针对一性付款游戏
	 */
	public static boolean isHasRecord() {
		boolean result = false;
		try {
			RecordStore rs = RecordStore.openRecordStore(strRecordName, true);
			result = (rs.getNumRecords() > 0);
			rs.closeRecordStore();
		} catch (RecordStoreNotOpenException ex) {
			ex.printStackTrace();
		} catch (RecordStoreException ex) {
			ex.printStackTrace();
		}
		return result;
	}

	// /**
	// * 已过关卡号
	// * 按关卡收费
	// */
	// public int getPassedLevel(){
	// int result = SEND_NOREC;
	// try {
	// RecordStore rs = RecordStore.openRecordStore(strRecordName,true);
	// if(rs.getNumRecords()>0){
	// byte temp[] = rs.getRecord(1);
	// ByteArrayInputStream bais = new ByteArrayInputStream(temp);
	// DataInputStream dis = new DataInputStream(bais);
	// result = dis.readInt();
	// dis.close();
	// bais.close();
	// }
	// if (rs != null) rs.closeRecordStore();
	// } catch (RecordStoreNotOpenException ex) {
	// ex.printStackTrace();
	// } catch (RecordStoreException ex) {
	// ex.printStackTrace();
	// }catch(IOException ex){
	// ex.printStackTrace();
	// }
	// return result;
	// }
	//
	/**
	 * 付款后保存一条信息到到RMS，表示已经付款 可对一性付款游戏或按关卡收费
	 */
	public void saveToRMS(int value) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);
		try {
			dos.writeInt(value);
			dos.flush();
			byte temp[] = bos.toByteArray();
			System.out.println("strRecordName====" + strRecordName);
			RecordStore rs = RecordStore.openRecordStore(strRecordName, true);
			if (rs.getNumRecords() > 0) {
				rs.setRecord(1, temp, 0, temp.length);
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

	static final String strPayFailMore = "短信发送失败";// 发送失败内容待定
}
