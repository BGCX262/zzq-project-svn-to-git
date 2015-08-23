package cn.thirdgwin.app;

import java.io.IOException;
import java.io.InputStream;

/**
 * 在以前的计费系统基础做了以下修改：
 * 1.以前读取短信内容的时候，直接通过WRAPPER接口读取短信内容，现在改成从JAR包的根目录读取charge.cfg文件中的信息
 * 来组成短信内容。短信内容放在CHARGE中。
 * 
 * 2.以前WRAPPER接口中的SPDATA中存放的是发送短信的号码和短信内容，现在SPDATA中存放的是发送短信的号码和购买道具的
 * 道具ID。（注：因为激活游戏的时候不需要道具ID，所以现在激活游戏的道具写成空的字符串）
 * 
 * 3.在以前的计费公共类中加入了WrapperInfo类和SHA1类。
 * 
 * 4.该WrapperInfo类的构造方法中，就是将charge.cfg配置文件中的信息读取出来后组成短信内容，保存在CHARGE字符串数组
 * 中，以便发送短信的时候调用。
 * 
 * 5.SHA1类是一个SHA1摘要算法类。
 * 
 * 6.isCheckSuccess变量是用来存储读取配置文件时检测短信激活码是否配对。在WrapperMain的runPay()方法中使用到了这个
 * 变量。如果配对才可发送短信，如果不配对直接作为失败处理。
 * 
 * 7.在SCWrapper类的init方法中new了WrapperInfo类，主要用于生成CHARGE短信内容，因为读取/charge.cfg文件的时候
 * 静态方法调用不了getClass()这个方法。所以暂时这样处理了。
 * 
 * 8.在GameWrapperMain类中的OnGetSMS方法中，将以前的	return WRAPPER.SPDATA[payType];	
 * 修改为了现在的 return WrapperInfo.getSMS(WRAPPER.SPDATA[payType]);
 * 
 * @author 刘志刚
 *
 */
public class WrapperInfo {
	/**
	 * sms和smsCode进行摘要加密后与checkString配对结果
	 * true 表示配对成功，false 表示配对不成功
	 */
	public static boolean isCheckSuccess=false;
	
	public static String sms;
	public static String smsCode;
	public static String checkString;
	public static String IMSI;
	public static String PI;
	public WrapperInfo(){
		InputStream is = getClass().getResourceAsStream("/Charge.cfg");
		byte[] b = new byte[512];
		String fileString="";
		int length = 0;
		try {
			length = is.read(b, 0, 512);
			fileString = new String(b, 0, length, "UTF-8");
			
			
			sms=getParameter(fileString, "USR-BUY-SMS: ");
			smsCode=getParameter(fileString, "USR-BUY-SMSCODE: ");
			checkString=getParameter(fileString, "SMS-CHECK-STRING: ");
			IMSI=getMobileIMSI();
//			isCheckSuccess=true;
			//摘要加密
			String shaShtring=SHA1.hex_sha1(sms+smsCode);
			if(shaShtring.equals(checkString)){
				isCheckSuccess=true;
			}else {
				isCheckSuccess=false;
			}
		} catch (Exception e) {
			isCheckSuccess=false;
			e.printStackTrace();
		}finally{
			try {
				is.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 获取短信内容
	 * @param data
	 * @return
	 */
	public static String[] getSMS(String[] data){
		String[] smsString=new String[2];
		smsString[0]=data[0];
		
		//如果道具ID为空，短信内容只为sms
		//如果不为空，则sms和道具id用空格隔开
		if("".equals(data[1])){
			smsString[1]=sms;
		}else {
			smsString[1]=sms+" "+data[1];
		}			

		return smsString;
	}
	
	
	public String read(){
		InputStream is = getClass().getResourceAsStream("/Charge.cfg");
		byte[] b = new byte[1024];
		String fileString="";
		int length = 0;
		try {
			length = is.read(b, 0, 1024);
			fileString = new String(b, 0, length, "UTF-8");
		} catch (Exception e) {

			e.printStackTrace();
		}finally{
			try {
				is.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return fileString;
	}
	
	/**
	 * 获取手机的IMSI号，有些型号手机不一定支持。
	 * @return
	 */
	public static String getMobileIMSI(){
		String imsi="";
		imsi = System.getProperty("phone.IMSI");
		//#if BRAND=="Motorola"
		if (imsi == null || "".equals(imsi)) {
			imsi = System.getProperty("com.motorola.IMSI");
		}
		//#elif BRAND=="Nokia"
		if (imsi == null || "".equals(imsi)) {
			imsi = System.getProperty("com.nokia.IMSI");
		}
		if (imsi == null || "".equals(imsi)) {
			imsi=System.getProperty("com.nokia.mid.imsi");
		}
		//#elif BRAND=="SonyEricsson"
		imsi = System.getProperty("com.sonyericsson.imsi");
		//#elif BRAND=="Samsung"
		imsi=System.getProperty("system.imsi");
		//#elif BRAND=="LG"
		imsi=System.getProperty("com.lg.imsi");
		//#endif
		if(imsi == null || "".equals(imsi)){
			imsi = System.getProperty("IMSI");
		}
		if(imsi == null || "".equals(imsi)){
			imsi = "";
		}
		return imsi;
	}
	
	public static String getParameter(String input, String keyword) {
		if (input != null && !input.equals("") && keyword != null
				&& !keyword.equals("")) {
			String result = "";
			int i = input.indexOf(keyword);
			result = input.substring(i + keyword.length());
			i = result.indexOf("\n");
			if (i != -1) {
				result = result.substring(0, i);
			}
			i = result.indexOf("\r");
			if (i != -1) {
				result = result.substring(0, i);
			}
			return result;
		} else {
			return "";
		}
	}


	
}
