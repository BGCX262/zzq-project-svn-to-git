package cn.thirdgwin.lib;

import java.util.Hashtable;
import java.util.Vector;

import cn.thirdgwin.io.zByteArrayStream;

public class zServiceText
	{
		public static final String CATLOG_NAME		= "/tcatalog";
		public static final boolean CACHE_TEXT		= true;
		/** 全局包ID固定为0 */
		public static final int ID_SET_GLOBAL		= 0;
		public static final int ID_SET_DYNA			= 1 + ID_SET_GLOBAL;
		public static final int ID_SET_MAX			= 1 + ID_SET_DYNA;
		public static Hashtable[] _Texts = new Hashtable[ID_SET_MAX];
		public static zByteArrayStream[] _Cache 	= new zByteArrayStream[ID_SET_MAX];
		public static short[] 		_Cache_LibID 	= new short[ID_SET_MAX];
		public static String[] _Catalog;
		public zServiceText()
			{
				Init();
			}
		public static final void Init()
			{
				for (int i = 0;i<_Texts.length;i++)
					_Texts[i] = new Hashtable();
				for (int i = 0;i<_Cache_LibID.length;i++)
					_Cache_LibID[i] = -1;
				LoadCatalog(CATLOG_NAME);
			}
		private static String GetTextFromCache(Integer key,int id_set,int id)
			{
				String retV = null;
				if(CACHE_TEXT)
				{
				retV = (String) _Texts[id_set].get(key);
				if (retV!=null) return retV;
				};
				if (_Cache[id_set]!=null)
				{
				_Cache[id_set].Seek(id & 0xFFFF,zByteArrayStream.SEEK_SET);
				retV = _Cache[id_set].GetStr();
				if(CACHE_TEXT)
				{
					if (DevConfig.ENABLE_ERR_INFO)
					{
						if (retV==null)
							Utils.Err("Can't Access TEXT with ID = 0x" + Integer.toHexString(id)+"\n");
					}
					if (retV!=null)
						_Texts[id_set].put(key,retV);
				};		
				}
				return retV;
			}
		public static String GetText(int id)
			{
				Integer key = null;
				if(CACHE_TEXT)
				{
				key = new Integer(id);
				};
				int newlibid =  id >>16;
				String retV;
				if (newlibid==_Cache_LibID[ID_SET_GLOBAL])
				{
					retV = GetTextFromCache(key,ID_SET_GLOBAL,id);
					return retV;
				} else
				{
					if (newlibid!=_Cache_LibID[ID_SET_DYNA])
					{
						LoadPack(newlibid,ID_SET_DYNA);	
					}
					retV = GetTextFromCache(key,ID_SET_DYNA,id);
					return retV;
				}
			}
		public static void LoadCatalog(String resname)
			{
				if (_Catalog!=null)return;
				zByteArrayStream bas = new zByteArrayStream(Utils.GetBin(resname));
				int count = bas.GetShort();
				_Catalog = new String[count];
				int loadidx = 0;
				while (!bas.EOF() && loadidx<count)
				{
					_Catalog[loadidx] = bas.GetStr();
					loadidx++;
				}
			}
		public static void LoadPack(int packID,int target_set)
			{
				_Cache[target_set] = new zByteArrayStream(Utils.GetBin("/"+_Catalog[packID]));
				_Cache_LibID[target_set] = (short) packID;
			}		
		public static final void LoadToGlobal(int packID)
			{
				LoadPack(packID,ID_SET_GLOBAL);
			}
		public static final void LoadToDyna(int packID)
			{
				LoadPack(packID,ID_SET_DYNA);
			}	
		public static final void ClearPack(int target_set)
			{
				_Cache[target_set] = null;
				_Cache_LibID[target_set] = -1;
				_Texts[target_set].clear();
			}
		public static final void ClearGlobal()
			{
				ClearPack(ID_SET_GLOBAL);
			}
		public static final void ClearDyna()
			{
				ClearPack(ID_SET_DYNA);
			}
		public static final void BuildCache(int target_set)
			{
				zByteArrayStream bas = _Cache[target_set];
				bas.Seek(0,zByteArrayStream.SEEK_SET);
				Hashtable text = _Texts[target_set];
				int offset = 0;
				int libid = (_Cache_LibID[target_set]<<16);
				String info;
				bas.GetShort();	//跳过流的长度
				offset+=2;
				Integer key;
				text.clear();
				while (!bas.EOF())
				{
					key = new Integer(libid + offset);
					info = bas.GetStr();
					offset = bas.GetOffset();
					if (info!=null)
						text.put(key,info);
				}
				_Cache[target_set] = null;
			}
		public static final void BuildCacheGlobal()
			{
				BuildCache(ID_SET_GLOBAL);
			}
		public static final void BuildCacheDyna()
			{
				BuildCache(ID_SET_DYNA);
			}		
	}
