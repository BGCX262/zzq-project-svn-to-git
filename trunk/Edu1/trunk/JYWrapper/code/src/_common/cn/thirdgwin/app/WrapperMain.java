package cn.thirdgwin.app; 

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Font;
import java.util.Vector;
import javax.microedition.lcdui.Graphics;

/*
 * Main.java
 *
 * Created on 2011年11月21日, 下午5:27
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

import cn.thirdgwin.lib.DevConfig;
import cn.thirdgwin.lib.cTouch;

/**
 *
 * @author k-java
 */
public abstract class WrapperMain extends Canvas implements Runnable {
    private WrapperPay msgPay = new WrapperPay(OnGetRmsName());
    public int payType;
    private final byte GS_SENDING = 0; 
    public static final byte GS_MAINMENU = 1;
    private final byte GS_SENDFAIL = 27;//发送失败
    private final byte GS_SENDFAILMORE = 28;//发送失败更多
    private final byte GS_USERCANCEL = 29;//发送提示时用户取消
    private final byte GS_SENDSUCCESS = 30;//发送成功
    
    public byte iCurrState = GS_MAINMENU;
    private int KEY_SELECT = DevConfig.KEY_LSK;
    private int KEY_GOBACK = DevConfig.KEY_RSK;
//    private int KEY_SELECT1 = -6;
//    private int KEY_GOBACK1 = -7;
    private int WIDTH = DevConfig.SCR_W;
    private int HEIGHT = DevConfig.SCR_H;
    
    private String[] strContent;
    private int iCurrLine=0;//当前首行号
    private int iMaxLinePerPage = 8;//每页显示多少行
//    private int SAVE_AMOUNT = 2;
    
    abstract void OnCancel(int payType);
    abstract void OnSentsuc(int payType);
    abstract String[] OnGetString(int payType);
    abstract String[] OnGetSMS(int payType);
    abstract String OnGetRmsName();
    
    /** Creates a new instance of Main */
    public WrapperMain() {
        setFullScreenMode(true);
    }
    public void paint(Graphics g){
    	//#if MODEL=="E258"
    	g.setFont(Font.getFont(0, 0, Font.SIZE_LARGE));
    	//#else
		g.setFont(Font.getFont(0, 0, 8));
		//#endif
        switch(iCurrState){
            case GS_MAINMENU:
            	menuMainMenu(g);
            	repaint();
						try
							{
								Thread.sleep(100);
							} catch (InterruptedException e)
							{
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
            	
                break;
            case GS_SENDING:
            	menuSending(g);
                break;
            case GS_SENDFAIL:
                menuSendFail(g);
                break;
            case GS_SENDFAILMORE:
                menuSendFailMore(g);
                break;
            case GS_USERCANCEL:
                menuUserCancel(g);
                break;
            case GS_SENDSUCCESS:
                menuSendSuccess(g);
                break;
        }
    }
    
    public void run() {
    }
    
    public void keyPressed(int iKeyCode) {
        switch(iCurrState){
            case GS_MAINMENU:
            	keyMainMenuNormal(iKeyCode);
                break;
            case GS_SENDING:
//                keyMainMenu(iKeyCode);
                break;
            case GS_SENDFAIL:
                keySendFail(iKeyCode);
                break;
            case GS_SENDFAILMORE:
                keySendFailMore(iKeyCode);
                break;                
            case GS_USERCANCEL:
                keyUserCancel(iKeyCode);
                break;                
            case GS_SENDSUCCESS:
            	keySendSuccess(iKeyCode);
//            	iCurrState = GS_MAINMENU;
                break;                
        }
    }
    //获取是否有记录
    public static boolean getRecord(){
    	System.out.print(WrapperPay.isHasRecord());
    	return WrapperPay.isHasRecord();
    	
    }
    protected void saveRms(){
    	if(iCurrState == GS_SENDSUCCESS)
    	msgPay.saveToRMS(WrapperPay.SEND_SUCCESS);
    }
    /**发送*/
    private void runPay(){
//        iCurrState = GS_SENDING;
        //发送短信前，先判断内容摘要匹配是否成功
        //如果匹配不成功直接做为失败处理
        if(!WrapperInfo.isCheckSuccess){
        	iCurrState = GS_SENDFAIL;
        	repaint();
        	return;
        }
        
        byte send = 0;
        send = msgPay.pay(OnGetSMS(payType));
        switch(send){
            case WrapperPay.SEND_SUCCESS:
                iCurrState = GS_SENDSUCCESS;
//                if(payType == WRAPPER.SP_OPENALL)//一次性付费才会记录
//                	msgPay.saveToRMS(WrapperPay.SEND_SUCCESS);
                break;
            case WrapperPay.SEND_USERCANCLE:
                iCurrState = GS_USERCANCEL;
                break;
            case WrapperPay.SEND_FAIL:
                iCurrLine=0;
                iCurrState = GS_SENDFAIL;
                strContent = str2strArr(WrapperPay.strPayFailMore);
                break;
            case WrapperPay.SEND_NOREC:
                iCurrLine=0;
                iCurrState = GS_SENDFAIL;
                strContent = str2strArr(WrapperPay.strPayFailMore);
                break;
            default:
                iCurrLine=0;
                iCurrState = GS_SENDFAIL;
                strContent = str2strArr(WrapperPay.strPayFailMore);
                
        }
        repaint();
    }
    

    private void drawLR(Graphics g){
    	g.setClip(0, HEIGHT - (g.getFont().getHeight()+5), WIDTH, HEIGHT);
      g.drawString("发送",2,HEIGHT-5,Graphics.LEFT|Graphics.BOTTOM);
      g.drawString("退出",WIDTH-5,HEIGHT-2,Graphics.RIGHT|Graphics.BOTTOM);
    }
    public static int moveY = 0;
    private void menuMainMenu(Graphics g){
    	if(moveY<170){
  			moveY +=2;
  			}else{
  				moveY = 0;
  			}
      g.setColor(0);
      g.fillRect(0,0,WIDTH,HEIGHT);
      g.setColor(0xFFFFFF);
      g.setClip(0, 0, WIDTH, HEIGHT - 20);
      for(int i = 0; i < OnGetString(payType).length; i++){
      	 g.drawString(OnGetString(payType)[i],WIDTH/6 - 10,(HEIGHT>>3) + i*(g.getFont().getHeight()+5) - moveY,Graphics.TOP|Graphics.LEFT);
      }
      drawLR(g);
    }

        /**发送中界面*/
    private void menuSending(Graphics g){
        g.setColor(0);
        g.fillRect(0,0,WIDTH,HEIGHT);
        g.setColor(0xFFFFFF);
        //g.drawString("按5进入乐游网",WIDTH/2,10,g.TOP|g.HCENTER);
        g.drawString("正在发送信息...",WIDTH/2,HEIGHT/2-8,Graphics.TOP|Graphics.HCENTER);
//        g.drawString("发送",2,HEIGHT-2,g.LEFT|g.BOTTOM);
    }
    
    /**用户取消发送*/
    private void menuUserCancel(Graphics g){
        g.setColor(0);
        g.fillRect(0,0,WIDTH,HEIGHT);
        g.setColor(0xFFFFFF);
        g.drawString("用户取消发送",WIDTH/2,HEIGHT/2,Graphics.TOP|Graphics.HCENTER);
       g.drawString("返回",WIDTH-2,HEIGHT-5,Graphics.RIGHT|Graphics.BOTTOM);
    }
    
    /**发送成功*/
    private void menuSendSuccess(Graphics g){
        g.setColor(0);
        g.fillRect(0,0,WIDTH,HEIGHT);
        g.setColor(0xFFFFFF);
        g.drawString("发送成功",WIDTH/2,HEIGHT/2,Graphics.TOP|Graphics.HCENTER);
       g.drawString("返回",WIDTH-2,HEIGHT-5,Graphics.RIGHT|Graphics.BOTTOM);
    }
    
    /**发送失败*/
    private void menuSendFail(Graphics g){
        g.setColor(0);
        g.fillRect(0,0,WIDTH,HEIGHT);
        g.setColor(0xFFFFFF);
        g.drawString("发送失败",WIDTH/2,10,g.TOP|g.HCENTER);
        
        
//        String[] bug=str2strArr(msgPay.debugString);
//        for(int i=0;i<bug.length;i++){
//            g.drawString(bug[i],10,26+i*16,0);
//        }
//        g.drawString("更多",2,HEIGHT-5,g.LEFT|g.BOTTOM);
        g.drawString("返回",WIDTH-5,HEIGHT-2,g.RIGHT|g.BOTTOM);
    }
    
    /**发送失败更多*/
    private void menuSendFailMore(Graphics g){
        g.setColor(0);
        g.fillRect(0,0,WIDTH,HEIGHT);
        g.setColor(0xFFFFFF);
        g.drawString("发送更多",WIDTH/2,10,g.TOP|g.HCENTER);
        for(int i=0;i<iMaxLinePerPage;i++){
            g.drawString(strContent[i+iCurrLine],10,26+i*16,0);
        }
        g.drawString("上/下",WIDTH-2,HEIGHT-2,g.LEFT|g.BOTTOM);
        g.drawString("返回",WIDTH-2,HEIGHT-5,g.RIGHT|g.BOTTOM);
    }
    int key = 0;
    /**一般处理（整合）
     * @param iKeyCode
     */
    private void keyMainMenuNormal(int iKeyCode){
    	if(iKeyCode == KEY_SELECT){
        iCurrState = GS_SENDING;
        repaint();
        new Thread(){
            public void run(){
                runPay();
            }
        }.start();
        return;
    }
    if(iKeyCode == KEY_GOBACK){
    	
    	OnCancel(payType);
    	SCWrapper.setCanvas();
    }
    }
    /**主界面按键*/
    private void keyMainMenu(int iKeyCode){
        if(iKeyCode == KEY_SELECT){
            iCurrState = GS_SENDING;
            repaint();
            new Thread(){
                public void run(){
                    runPay();
                }
            }.start();
            return;
        }
        if(iKeyCode == KEY_GOBACK){
        	SCWrapper.setCanvas();
        }
    }
    
    /** 用户取消发送界面按键*/
    private void keyUserCancel(int iKeyCode){
        if(iKeyCode == KEY_GOBACK){//返回
            iCurrState = GS_MAINMENU;
//            cleankey();
            repaint();
        }
    }
    /**整合
     * @param iKeyCode
     */
    private void keySendSuccess(int iKeyCode){
    	if(iKeyCode == KEY_GOBACK){//返回

      	OnSentsuc(payType);
      	SCWrapper.setCanvas();
      	iCurrState = GS_MAINMENU;
     	}
    }
    /**发送失败更多界面按键*/
    private void keySendFail(int iKeyCode){
//        if(iKeyCode == KEY_SELECT){//更多
//            iCurrState = GS_SENDFAILMORE;
//            strContent = str2strArr(WrapperPay.strPayFailMore);
//            repaint();
////            cleankey();
//        }
        if(iKeyCode == KEY_GOBACK){//返回
            iCurrState = GS_MAINMENU;
            repaint();
//            cleankey();
        }
    }
    /**发送失败更多帮助界面按键*/
    private void keySendFailMore(int iKeyCode){
        switch(getGameAction(iKeyCode)){
            case UP:
                if(iCurrLine>0){
                    iCurrLine--;
                    repaint();
                }
                break;
            case DOWN:
                if((iCurrLine+iMaxLinePerPage)<strContent.length){
                    iCurrLine++;
                    repaint();
                }
                break;
        }
        if(iKeyCode == KEY_GOBACK){//返回
            iCurrState = GS_MAINMENU;
            repaint();
        }
    }
    
    /**简单的字符串转字符串数组*/
    private String[] str2strArr(String value){
        Vector vString = new Vector();
        int iEndPos=0;
        int iLinePerWords = 9;
        for(int i=0,len = value.length();i<len;i+=iLinePerWords){
            iEndPos = i+ iLinePerWords;
            if(iEndPos>len)iEndPos=len;
            vString.addElement(value.substring(i,iEndPos));
        }
        String[] result = new String[vString.size()];
        for(int i=0,len = vString.size();i<len;i++){
            result[i] = (String)vString.elementAt(i);
        }
        return result;
    }
    //ctouch比较纠结，自己写
  //#if ENABLE_TOUCH=="TRUE"		
  	public void pointerPressed(int x, int y) 
  		{
  			if(x<50 && y>DevConfig.SCR_H-30)
  				keyPressed(DevConfig.KEY_LSK);
  			if(x>DevConfig.SCR_W-50 && y>DevConfig.SCR_H-30)
  				keyPressed(DevConfig.KEY_RSK);
  		}
  	public void pointerReleased(int x, int y) 
  		{
  			
  		}	
  //#endif
}
