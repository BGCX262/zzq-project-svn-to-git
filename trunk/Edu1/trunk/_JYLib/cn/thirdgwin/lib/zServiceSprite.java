package cn.thirdgwin.lib;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.microedition.lcdui.Image;


public class zServiceSprite {
	// ///////////////////////////////////////////////////////////////
	// ////////sprite的代码
	
	public final static int SPR_COL_CACHE = 0;
	public final static int SPR_COL_LOAD_TYPE = 1;
	public final static int SPR_COL_NAME = 2;	
	public final static int SPR_COL_LOAD_IMAGE = 3;
	
	public final static int SPR_LOAD_TYPE_NO = 0;
	public final static int SPR_LOAD_TYPE_ALWAYS = 1;
	public final static int SPR_LOAD_TYPE_DEMADE = 2;
	/**
	 * 游戏中用到的所有的sprite的定义
	 */
	private static final zSprite DUMMY_SPRITE = new zSprite();
	private static Hashtable s_Sprites;
	private static Hashtable s_SpritesState;
	/**
	 * key是字符串xx.bsprite，value是对应的数值，用于从名字得到索引
	 */
	public static final String SPRITE_FOLDER = "/";
	public static final String SPRITE_FILE_NAME = "/sprite.ini";
	public static final String SPRITE_SECTION_NAME = "SPR";

	public static final String SPRITE_SUFFIX = ".bmp";

	/** Sprite的状态 */
	private static final int IDX_REF			= 0;				//当前正在使用
	private static final int IDX_MAX			= 1 + IDX_REF;	
	
	private static boolean _inited = false;
	public static void Init()
	{
		Utils.Dbg("Init()");
		if(!_inited)
		{
		s_Sprites = new Hashtable();
		s_SpritesState = new Hashtable();
		_inited = true;
		};

	}
	public static zSprite SprCreate(String sprname,String[] pngnames)
	{
		int i=0;
			byte[] bytes = Utils.GetBin(sprname);
			if (bytes==null)
				{
				Utils.Err("Can't Load " + sprname);
				return null;
				}
			zSprite spr = new zSprite();
			spr.Load(bytes,0);bytes = null;
			if (pngnames==null)
				return spr;
			Image[] imgs = new Image[pngnames.length];
			for(i = pngnames.length - 1;i>=0;i--)
			{
				imgs[i] = zServiceImage.Get(pngnames[i]);
			}
			spr.SetImages(imgs);
			return spr;
	}
	public final static String GenKey(String sprname,String[] pngnames)
		{
			StringBuffer sb = new StringBuffer();
			sb.append(sprname);
			if(pngnames!=null)
			for(int i=pngnames.length - 1;i>=0;i--)
				sb.append(pngnames[i]);
			String retV = sb.toString();
			sb = null;
			return retV;
		}
	/**
	 * 获取Sprite,如果该资源未加载，则尝试加载。但只会尝试一次
	 * @param strName
	 * @return
	 */
	public static zSprite Get(String sprname,String[] pngnames,boolean Forceload) {
		if(!_inited)
			Init();
		int i;
		short[] state;
		String newKey =GenKey(sprname,pngnames);
		zSprite retV = (zSprite)s_Sprites.get(newKey);
		if(retV==null)
		{
			retV = SprCreate(sprname,pngnames);
			if(retV==null)
				retV = DUMMY_SPRITE;
			retV.Filename = newKey;
			s_Sprites.put(newKey,retV);
			s_SpritesState.put(retV,new short[] {0});
		}
		else
		{
			if (retV.GetImages()==null && pngnames!=null)
			{
				Image[] imgs = new Image[pngnames.length];
				for(i = pngnames.length - 1;i>=0;i--)
				{
					imgs[i] = zServiceImage.Get(pngnames[i]);
				}
				retV.SetImages(imgs);
			}
		}
		if (retV != DUMMY_SPRITE)
		{
			state = (short[])s_SpritesState.get(retV);	
			state[IDX_REF]++;
		}
		else
		{
			retV = null;
		}
		return retV;
	}
	/**
	 * 释放指定的sprite
	 * 
	 * @param spriteID
	 */
	public static void Put(zSprite spr,boolean freeWhenNoNeed,boolean freeImageWhenNoNeed) {
		short[] state;
		Image[] imgs;
		if ((spr !=null) && (spr != zServiceSprite.DUMMY_SPRITE))
		{
			state = (short[])s_SpritesState.get(spr);
			if(state!=null)
				{
				if(state[IDX_REF]>0)
					state[IDX_REF] --;

				/** 注意应该在没有人在用的情况下，才归还图像 */				
				if (state[IDX_REF]<=0)
					{
						imgs = spr.GetImages();
						if (imgs!=null)
						{
							Image img;
							for(int i = imgs.length - 1;i>=0;i--)
							{
								img = imgs[i];
								if(img!=null)
								{
									zServiceImage.Put(img,freeImageWhenNoNeed);
								}
							}
						}
						spr.SetImages(null);					
					}
		
				if (freeWhenNoNeed && state[IDX_REF]<=0)
					{
					s_SpritesState.remove(spr);
					s_Sprites.remove(spr.Filename);
					}
				};
		};
	}
	public static void UnloadAllSprite() {
		s_Sprites.clear();
		s_SpritesState.clear();
	}
	/** 清理引用计数为0的Sprite */
	public static final void ClearCache()
	{
		if(!_inited) return;
		Enumeration E = s_Sprites.keys();
		Object Key;
		zSprite spr;
		short[] state;
		while (E.hasMoreElements())
		{
			Key = E.nextElement();
			spr = (zSprite)s_Sprites.get(Key);
			state = (short[]) s_SpritesState.get(spr); 
			if (state[IDX_REF]<=0)
			{
				s_Sprites.remove(Key);
				s_SpritesState.remove(spr);
			}
		}
	}	

	public static final void Dump() {

		if (!_inited)
			return;
		Enumeration E = s_Sprites.keys();
		Integer Key;
		zSprite spr;
		short[] state;
		Utils.Dbg("============Sprite Cache Dump============");
		while (E.hasMoreElements()) {
			Key = (Integer) E.nextElement();
			spr = (zSprite) s_Sprites.get(Key);
			state = (short[]) s_SpritesState.get(spr);
			if (state[IDX_REF] > 0) {
				Utils.Dbg("Sprite " + Key.intValue() + " " + spr.Filename);
			}
		}
	}
	public static void ClearCacheForM()
	{
		ClearCache();
		zServiceImage.ClearCache();
		Utils.FreeCPU(true,1);
	}
}
