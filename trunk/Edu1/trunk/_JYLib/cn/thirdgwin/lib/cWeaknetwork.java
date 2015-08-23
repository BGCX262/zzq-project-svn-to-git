package cn.thirdgwin.lib;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

import cn.thirdgwin.app.GameMIDlet;
//#if PLATFORM!="Android" && MODEL!="800N97" && MODEL!="480N97" && CHANNEL_NAME=="BBX"
import com.emag.open.GameCenter;
//#if WEAKONLINE_NET_SCORESENDING=="TRUE"
import com.emag.open.ChallengeBack;
//#endif
//#endif
import javax.microedition.midlet.*;


public class cWeaknetwork {
	//#if PLATFORM!="Android" && MODEL!="800N97" && MODEL!="480N97" && CHANNEL_NAME=="BBX"
	public static boolean HasNet = true;
	public static boolean isNetWork =false;
	public static boolean FirstWork =true;
	public static int NetOnOffPaintCount = 0;
	public static int NetOnOffTotalFrame = 30;
	public static boolean BtnNetOnOffInited = false;
	public static boolean NetOnOffisFinished = false;
	public static int returnGamestate;
	public static String nimei;
	//#if WEAKONLINE_NET_SCORESENDING=="TRUE"
	public static ChallengeBack challengeBackImpl = new ChallengeBack() {
		public void doChallenge(String levelId) {
			nimei = levelId;
		}
	};
	//#endif
	//#if SCREENWIDTH <=176
	public static String[] netstr = new String[]{"需要联网才能","使用互动功能,","是否联网?","是","否"};
	//#else
	public static String[] netstr = new String[]{"需要联网才能","使用互动功能,","是否联网?","是","否"};
	//#endif
	public static String[] scoreStrings = null;
	
	
	/**
	 * 游戏初始化弱联网的设置
	 * @param GameName
	 * @param ApplicationID
	 * @param Key
	 * @param Encodekey
	 * @param GameIconPath
	 */
	public static void initOlogin(String GameName,int ApplicationID,String Key,String Encodekey,String GameIconPath,boolean falg){
		// #if REVIEW=="FALSE" && WEAKNETWORK!="FALSE"
		//#if WEAKONLINE_NET_SCORESENDING=="TRUE"
		GameCenter.setConfig( ApplicationID, Key, Encodekey);
		//#elif WEAKONLINE_MOREGAME=="TRUE" 
		GameCenter.setConfig(ApplicationID, Key, Encodekey);
		//#else
		GameCenter.setConfig( ApplicationID, Key, Encodekey);
		//#endif
		setKey();	
		//setCloseGameWhenToBrowser(falg);
	//#endif
	}
	public static void setTestPhotoNumber(String testPhotoNumber){
		//#if WEAKONLINE_NET_SCORESENDING == TRUE;
		GameCenter.setTestPhoneNum(testPhotoNumber);
		//#endif
	}
	
	/**
	 * 打印提示是否联网
	 * @param g
	 * @return
	 */
	public static final boolean NetOnOff_paint(Graphics g)
	{
//#if REVIEW=="FALSE"&& (WEAKONLINE_NET_SCORESENDING=="TRUE" || WEAKONLINE_MOREGAME == "TRUE") && WEAKNETWORK!="FALSE"
		g.setColor(0x000000);
		g.fillRect(0, 0, DevConfig.SCR_W, DevConfig.SCR_H);
		paintPQ(g);
		return false;	
//#else
		return true;
//#endif
	}
	/** 返回 true表示，本状态结束 */
	public static final boolean NetOnOff_Update(int keycode){		
		int idx = -1;
		boolean retV = false;
		if(!HasNet)
		{
			return NetOnOffisFinished;
		}
		for (int i = cCP.KeyValue.length - 1;i>=0;i--)
		{
			if (cCP.KeyValue[i] == keycode)
			{
				idx = i;
				break;
			}
		}
		if (idx<0) {
			return false;
		}

		switch (idx){
		case cCP.L:
			isNetWork = true;
//			login();
//			loadDate();
			retV = true;
			break;
		case cCP.R:
			isNetWork =false;
			retV = true;
			break;
		}
//		if(isNetWork){
//			login();
//			loadDate();
//		}
		return retV;	
	}
	
//	private static final int info1 = 0;
//	private static final int info2 = 1 + info1;
//	private static final int info3 = 1 + info2;
//	private static final int yes = 1 + info3;
//	private static final int no = 1 + yes;
	private static final int height = 20;
	
	public static final void paintPQ(Graphics g){
		int StrLen = netstr.length;
		g.setClip(0, 0, cCP.SCR_W, cCP.SCR_H);
		g.setColor(255, 255, 255);
		for(int i = 0;i<StrLen-2;i++){
			g.drawString(netstr[i], cCP.SCR_W >> 1,  (cCP.SCR_H >>2)+(height*i), Graphics.TOP|Graphics.HCENTER);
		}
//#if MODEL=="KG90n" || MODEL=="E258"
		g.drawString(netstr[StrLen-2], 0,  cCP.SCR_H-8, Graphics.LEFT|Graphics.BOTTOM);
		g.drawString(netstr[StrLen-1], cCP.SCR_W, cCP.SCR_H-8, Graphics.RIGHT|Graphics.BOTTOM);
//#else
		g.drawString(netstr[StrLen-2], 0,  cCP.SCR_H, Graphics.LEFT|Graphics.BOTTOM);
		g.drawString(netstr[StrLen-1], cCP.SCR_W, cCP.SCR_H, Graphics.RIGHT|Graphics.BOTTOM);
//#endif
//		if(!isToGameCenter){
//			g.setColor(0x000000);
//			g.fillRect(0, 0, DevConfig.SCR_W, DevConfig.SCR_H);
//			g.setColor(0xffffff);
//			g.drawString("登录中...", DevConfig.SCR_W>>1, DevConfig.SCR_H>>2,  Graphics.TOP|Graphics.HCENTER);
//		}
	}
	
	
	
	
	
	/**
	 * 显示是否提交成绩界面
	 * @param g
	 * @param score
	 */
	public static boolean isDrawUpdateScore = false;   //是否上传成功
	public static final boolean uploadScore(Graphics g,int score){
	// #if REVIEW=="FALSE" && WEAKNETWORK!="FALSE"
		if (isUpdateScore) {
			g.setColor(0x000000);
			g.fillRect(0, 0, cCP.SCR_W, cCP.SCR_H);
			g.setColor(0xffffff);
			g.drawString(uploadResult[1].substring(0,uploadResult[1].length() / 3), (cCP.SCR_W >> 1),
					(cCP.SCR_H >> 2), Graphics.TOP | Graphics.HCENTER);
			g.drawString(uploadResult[1].substring(uploadResult[1].length() / 3,uploadResult[1].length() * 2 / 3), (cCP.SCR_W >> 1),
					(cCP.SCR_H >> 2) + 20, Graphics.TOP | Graphics.HCENTER);
			g.drawString(uploadResult[1].substring(uploadResult[1].length() * 2 / 3), (cCP.SCR_W >> 1),
					(cCP.SCR_H >> 2) + 40, Graphics.TOP | Graphics.HCENTER);
			g.drawString("返回", cCP.SCR_W, cCP.SCR_H, Graphics.RIGHT
					| Graphics.BOTTOM);
		} else {
			scoreStrings = new String[] { "您当前积分为", score + "分", "是否提交成绩？",
					"选择 是 提交成绩", "选择 否 则取消", "是", "否" };
			g.setClip(0, 0, cCP.SCR_W, cCP.SCR_H);
			g.setColor(0x000000);
			g.fillRect(0, 0, cCP.SCR_W, cCP.SCR_H);
			g.setColor(0xffffff);
			for (int i = 0; i < scoreStrings.length - 2; i++) {
				g.drawString(scoreStrings[i], cCP.SCR_W >> 1, (cCP.SCR_H >> 2)
						+ i * height, Graphics.TOP | Graphics.HCENTER);
			}
			g.drawString(scoreStrings[scoreStrings.length - 2], 0, cCP.SCR_H,
					Graphics.LEFT | Graphics.BOTTOM);
			g.drawString(scoreStrings[scoreStrings.length - 1], cCP.SCR_W,
					cCP.SCR_H, Graphics.RIGHT | Graphics.BOTTOM);
		}
		return false;
		//#else
		return true;
		//#endif
	}
	
	
	
	
	
	/**
	 * 选择是否上传积分
	 * @param keycode
	 * @return
	 */
	public static boolean isUpdateScore = false;
	
	public static final boolean NetOnOff_UpdateScore(int keycode,int score){
	// #if REVIEW=="FALSE" && WEAKNETWORK!="FALSE"
		int idx = -1;
		boolean retV = false;
		for (int i = cCP.KeyValue.length - 1;i>=0;i--)
		{
			if (cCP.KeyValue[i] == keycode)
			{
				idx = i;
				break;
			}
		}
		if (idx<0) {
			return false;
		}
		if(!isUpdateScore){
		switch (idx){
		case cCP.L:
			//isUpdateScore =	true;
			//
			uploadResult[1] = "成绩上传中，请您稍等....";
			uploadScore(score);
			isUpdateScore = true;
			retV = false;
			break;
		case cCP.R:
			isUpdateScore = false;
			retV = true;
			break;
		}
		}else{
			switch(idx){
			case cCP.R:
				isUpdateScore = false;
				isDrawUpdateScore = false;
				retV = true;
				break;
			}
		}

		return retV;	
		//#else
		return true;
		//#endif
	}
	

	
	
	/**
	 * 设置左右软键值
	 */
	public static final void setKey(){
		//#if WEAKONLINE_MOREGAME=="TRUE"
		GameCenter.setKeyCode(DevConfig.KEY_LSK,DevConfig.KEY_RSK);	
		//#endif
	}
	
	/**
	 * 用户登录
	 */
	public final static void login(){
				if(!islogin()){
					//#if WEAKONLINE_MOREGAME=="TRUE"
					GameCenter.login();
					//#endif
				}
	}
	
	/**
	 * 用户是否登录
	 * @return
	 */
	public static final boolean islogin(){
		//#if WEAKONLINE_MOREGAME=="TRUE"
		return GameCenter.isLogin();
		//#else
		return false;
		//#endif
	}
	
	/**
	 * 判断弱联网平台是否可用
	 * @return
	 */
//	public static final boolean isAvailable(){
//		//#if WEAKONLINE_NET_SCORESENDING=="TRUE"
//		return GameCenter.isAvailable();
//		//#else
//		return false;
//		//#endif
//	}
	
	private static boolean isToGameCenter = true;
	/**
	 * 跳转到弱联网
	 * @param gameStatus 游戏开始状态ID
	 */
	public static final void toGameCenter(){
	
//				if(isAvailable()) {
					//#if WEAKONLINE_NET_SCORESENDING=="TRUE"
						GameCenter.call((MIDlet)GameMIDlet.GetInstance(), challengeBackImpl);
					//#endif
//				}
		
	}
	
	/**
	 *精品推荐跳转至内容嵌套接口
	 * @param midlet
	 */
	public final static void callMoreGame(){
		//#if WEAKONLINE_MOREGAME=="TRUE"
		GameCenter.call_MoreGame((MIDlet)GameMIDlet.GetInstance());
		//#endif
	}

	/**
	 * 积分上传
	 * @param score
	 */
	public static String[]uploadResult = new String[2];
	public static final void uploadScore(final int score){
		
		Runnable r = new Runnable() {
			public void run() {
				//若GameCenter.isLogin()返回false,则需要登录
//					login();
					if(!islogin()){
						//#if WEAKONLINE_MOREGAME=="TRUE"
						GameCenter.login();
						//#endif
					}
				//判断是否在挑战中
				if(isInChallenge()){
					//在挑战中，上传积分至排行榜
					//#if WEAKONLINE_NET_SCORESENDING=="TRUE"
					GameCenter.uploadScore(score);
					//#endif
					//上传挑战积分,GameCenter.getChallengeId()为挑战ID，score为积分
					uploadChallengerResult(score);
					//清除挑战状态
					clearChallenge();
					return;
				}else{
					//上传至排行榜
					//#if WEAKONLINE_NET_SCORESENDING=="TRUE"
					uploadResult = GameCenter.uploadScore(score);
					
					//#else
					uploadScore_sms(score);
					//#endif
					return;
				}
				
			}
		};
		new Thread(r).start();
	}
	
	public static String uploadResultString;
	
	public static String getLoadResultString(){
		return uploadResultString;
	}
	
	
	
	/**
	 * 挑战积分上传接口
	 * @param challenger
	 * @param score
	 * @return
	 */
	public static final boolean uploadChallengerResult(int score){
		//#if WEAKONLINE_NET_SCORESENDING=="TRUE"
		uploadResult = GameCenter.uploadChallengerResult(score);
		return "0".equals(uploadResult[0]);
		//#else
		return false;
		//#endif
	}
	
	/**
	 * 短信积分上传接口
	 * @param score
	 * @return
	 */
	public static final boolean uploadScore_sms(int score){
		//#if WEAKONLINE_SMS_SCORESENDING == "TRUE"
		if(GameCenter.uploadScore_sms(score)){
			uploadResult[0] = "0";
			uploadResult[1] = "成绩上传成功!";
		}else{
			uploadResult[0] = "1";
			uploadResult[1] = "成绩上传失败!";
		}
		return "0".equals(uploadResult[0]);
		//#else
		return false;
		//#endif
	}
	/**
	 * 是否在挑战状态
	 * @return
	 */
	public static final boolean isInChallenge(){
		//#if WEAKONLINE_NET_SCORESENDING=="TRUE"
		return GameCenter.isInChallenge();
		//#else
		return false;
		//#endif
	}
	
	/**
	 *挑战状态的清除
	 */
	public static final void clearChallenge(){
		//#if WEAKONLINE_NET_SCORESENDING=="TRUE"
		GameCenter.clearChallenge();
		//#endif
	}
	
	/**
	 * 获取挑战的ID
	 * @return
	 */
//	public static final String getChallengeID(){
//		//#if WEAKONLINE_NET_SCORESENDING=="TRUE"
//		return GameCenter.getChallengeID();	
//		//#else
//		return "";
//		//#endif
//	}
	
	/**
	 * 打开成就
	 * @return
	 */
	public static final boolean unlockAchievement(){
		//#if WEAKONLINE_NET_SCORESENDING=="TRUE"
		uploadResult= GameCenter.unlockAchievement("1");
		return "0".equals(uploadResult[0]);
		 
		//#else
		return false;
		//#endif
	}
		
//	/**
//	 *加载内容嵌套数据接口
//	 */
//	public static final void loadMoreGameData(){
//		//#if WEAKONLINE_MOREGAME=="TRUE"
//		GameCenter.loadMoreGameData();
//		//#endif
//	}
//	
//	/**
//	 * 加载内容嵌套图片接口
//	 */
//	public static final void loadMoreGameImages(){
//		//#if WEAKONLINE_NET_SCORESENDING=="TRUE"
//		GameCenter.loadMoreGameImages();
//		//#endif
//	}
	
	/**
	 *调用手机wap浏览器的时候是否关闭游戏
	 * @param flag
	 */
	public static final void setCloseGameWhenToBrowser(boolean flag){
		//#if WEAKONLINE_MOREGAME=="TRUE"
		GameCenter. setCloseGameWhenToBrowser(flag);
		//#endif
	}
	//#else
	public static boolean isNetWork =false;
	public static boolean FirstWork =true;
	public static void initOlogin(String GameName,int ApplicationID,String Key,String Encodekey,String GameIconPath,boolean falg){}
	public static final boolean NetOnOff_paint(Graphics g){
		return true;
	}
	public static final boolean NetOnOff_Update(int keycode){
		return true;
	}
	public static final boolean uploadScore(Graphics g,int score){
		return true;
	}
	public static final boolean NetOnOff_UpdateScore(int keycode,int score){
		return true;
	}
	public static final void setKey(){}
	public final static void login(){}
	public static final boolean islogin(){
		return true;
	}
//	public static final boolean isAvailable(){
//		return true;
//	}
	public static final void toGameCenter(){}
	public final static void callMoreGame(){}
	public static final void uploadScore(final int score){}
	public static final boolean uploadChallengerResult(String challengerid,int score){
		return true;
	}
	public static final boolean uploadScore_sms(int score){
		return true;
	}
	public static final boolean isInChallenge(){
		return true;
	}
	public static final void clearChallenge(){}
//	public static final String getChallengeID(){}
	public static final boolean unlockAchievement(){
		return true;
	}
	public static final void loadMoreGameData(){}
	public static final void loadMoreGameImages(){}
	public static final void setCloseGameWhenToBrowser(boolean flag){}
	
	
	
	
	//#endif
}
