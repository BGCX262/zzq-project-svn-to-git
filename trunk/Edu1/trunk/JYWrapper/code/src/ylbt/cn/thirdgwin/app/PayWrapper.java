package cn.thirdgwin.app;

import cn.thirdgwin.lib.*;
import cn.thirdgwin.app.*;


class PayWrapper extends payWrapperSuper
{
    
    public PayWrapper(int Pay_Idx) {
        super("fuck");    //form标题 订购	
        
    }
    public void Start()
    {
        
    }

    boolean isRunning()
    {
        return false;
    }
    boolean isComplete()
    {
        return true;
    }

    public static void doAction(int[] i)
    {
    }

    public void NotifyKey()
    {
    }

    public static void Init()
    {
		
    }

    public static boolean checkPayed(int index) {
    	return true;
    }

	public static boolean isPaySuccess() {
		return true;
	}
   
}
