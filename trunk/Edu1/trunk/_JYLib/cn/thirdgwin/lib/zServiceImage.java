package cn.thirdgwin.lib;
/**
 * 本图片管理器，目的在于管理所有图片。
 * 而，对于每个图片的Usage来说，需要实现一个机制，以便在内存不够的时候，进行清理。
 * 
 * Sprite 和 Image 的管理模式：
a: module+argb  不需要归还
b: module + png  由zServiceSprite管理，1对1的关系，释放sprite的时候释放image
c: module + pngs  由object的view系统管理。1对多的关系，释放该object外观的时候释放所有pngs
 * */
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.microedition.lcdui.Image;


public class zServiceImage {
	/** 图片装入方法， */
	private static final int TYPE_LOAD_ON_NEED	= 0;				//需要时装入(可以被释放掉)
	private static final int TYPE_LOAD_ALWAYS	= 1 + TYPE_LOAD_ON_NEED;
	/** 图片状态 */
	private static final int IDX_REF			= 0;				//当前正在使用
	private static final int IDX_MAX			= 1 + IDX_REF;		
/*************************************************
 * 资源管理器
 */
	private static Hashtable Images;
	private static Hashtable ImageNames;
	private static Hashtable ImagesState;		//指向一个short[]
	private static Image dummy;
	private static void Init()
	{
		Images = new Hashtable();
		ImagesState = new Hashtable();
		ImageNames = new Hashtable();
		dummy = Image.createImage(1,1);
		Integer key = new Integer(-1);
		Images.put(key,dummy);
		short[] state = new short[] {1};
		ImagesState.put(dummy,state);
	}
	public static final void Put(Image img,boolean freeWhenNoNeed)
	{
		if (img == null) return;
		short[] state = (short[])ImagesState.get(img);
		if ((state!=null) && (state[IDX_REF]>0))
		{
			state[IDX_REF]--;
		}
		if (freeWhenNoNeed && state[IDX_REF]<=0)
		{
			ImagesState.remove(img);
			String imgname = (String)ImageNames.get(img);
			Images.remove(imgname);
			ImageNames.remove(img);
		}
	}
	
	public static final Image Get(String str)
	{
		if (Images==null)
		{
			Init(); 
		}
		boolean Retry = true;
		short[] state;
//		Integer key = new Integer(idx);
		Image retV = (Image)Images.get(str);
		if (retV == null) {
			while (Retry) {
				try {
					retV = Image.createImage(str);
					Images.put(str, retV);
					state = new short[] { 0 };
					ImagesState.put(retV, state);
					ImageNames.put(retV, str);
					Retry = false;					
				} catch (Exception e) {
					Utils.Err("Can't load /" +str
								+ ".png");
					retV = null;
					Retry = false;
				} catch (OutOfMemoryError e) {
					zServiceSprite.ClearCacheForM();
				}
			}
			;
		}
		{
			state = (short[])ImagesState.get(retV); 
			state[IDX_REF]++;
		}

		return retV;
	}

	/** 清理引用计数为0的Image */	
	public static final void ClearCache()
	{
		if(Images==null) return;

		Enumeration E = Images.keys();
		Object Key;
		Image image;
		short[] state;
		while (E.hasMoreElements())
		{
			Key = E.nextElement();
			image = (Image)Images.get(Key);
			state = (short[]) ImagesState.get(image); 
			if (state[IDX_REF]<=0)
			{
				Images.remove(Key);
				ImagesState.remove(image);
			}
		}
	}

	public static final void Dump() {
		if (Images == null)
			return;

		Enumeration E = Images.keys();
		Object Key;
		Image image;
		short[] state;
		Utils.Dbg("============Image Cache Dump============");
		while (E.hasMoreElements()) {
			Key = E.nextElement();
			image = (Image) Images.get(Key);
			state = (short[]) ImagesState.get(image);
			if (state[IDX_REF] > 0) {
				if (Key instanceof Integer)
					{
					Utils.Dbg("Image In Heap " + ((Integer)Key).intValue());
					}
				else
				{
					Utils.Dbg("Image In Heap " + (String)Key);					
				}
			}
		}
	}
}
