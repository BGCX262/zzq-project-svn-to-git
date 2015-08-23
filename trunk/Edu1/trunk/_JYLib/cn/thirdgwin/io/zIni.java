package cn.thirdgwin.io;
/**
 * @TODO: 把每一个场景涉及的所有文件，放置在一个单独的文件里面
 * 包括: scene0.ini, 物理层、标识、地面、天空等。不包括Sprite.该内容由zResManager统一管理
 * @TODO: 当前尚未拆解all.ini,因此，在每次装入的时候，会装入一次All.ini,待数据结构稳定后，必须重写。
 */
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import cn.thirdgwin.lib.DevConfig;
import cn.thirdgwin.lib.Utils;
import cn.thirdgwin.utils.cParam;

public class zIni {
	public static final int TYPE_INT = 0;
	public static final int TYPE_SHORT	= 1 + TYPE_INT;
	public static final int TYPE_BYTE	= 1 + TYPE_SHORT;
	public static final int TYPE_STRING = 1 + TYPE_BYTE;
	public static final int TYPE_PARAM	= 1 + TYPE_STRING;
	public Hashtable Content;

	private String CurrentSectionName;	
	private Hashtable CurrentSection;
	/**
	 * 逗号作为分隔符，前面为key，后面value
	 */
	public final static String SEP_COMMA = ",";
	
	private static String s_sep = "";
	// 构造函数
	public zIni() {
		Content = new Hashtable();
		setSEP(SEP_COMMA);
	}
	public zIni(String resName,int keyType,int valType,String encoding)
	{
		Content = new Hashtable();
		setSEP(SEP_COMMA);
		Load(resName,keyType,valType,encoding);
	}
	public final void setSEP(String sep) {
		s_sep = sep;
	}
	/**
	 * @param SectionName 冒号后面的关键字
	 * @param Key 分隔符前面的关键字
	 * @return
	 */
	private String 		Cache_LastSectionName;
	private Hashtable 	Cache_LastHashTable = null;
	private final void ResetCache()
	{
		Cache_LastSectionName = null;
		Cache_LastHashTable = null;
	}
	private void Reset() {
		ResetCache();
		Content.clear();
		Content = null;
	}
	private final Hashtable GetSectionByCache(String SectionName)
	{
		Hashtable Section;
		if (!SectionName.equals(Cache_LastSectionName))
			{
			Cache_LastSectionName = SectionName;
			Cache_LastHashTable = Section = (Hashtable) Content.get(SectionName);
			};
		return Cache_LastHashTable;
	}
	
	private final void Parse(String Text,int keyType,int valType)
	{
		int ContentLen = Text.length();
		int idx = 0;
		int LStart = 0;
		int LEnd = 0;
		while (idx < ContentLen) {
			idx = Text.indexOf("\r",LStart);
			if (idx==-1)
			{
				idx = Text.indexOf("\n",LStart);
				if (idx==-1)
					idx+=ContentLen;	//技巧，因为正好这里需要idx = ContentLen - 1;而idx正好为-1
			}
			switch (Text.charAt(idx)) {
			case '\n':
			case '\r': // 行结束
			{
				LEnd = idx;
				ParseLine(Text, LStart, LEnd,keyType,valType);
				idx++;
				if (ContentLen <= idx)
					break;
				if (Text.charAt(idx) == '\n') {
					idx++;
				}
				LStart = LEnd = idx;
				break;
			}
			default:
				idx++;
				break;
			}
		}
		if (LStart < ContentLen - 1)
			ParseLine(Text, LStart, ContentLen,keyType,valType);
	}
	private final void ParseLine(String Text, int start, int end,int keyType,int ValType ) {
		// 长度小于2，直接跳过
		if ((end - start) < 2)
			return;
		// 注释行跳过
		if (Text.charAt(start) == '/' && Text.charAt(start + 1) == '/')
			return;
		Object K;
		Object V;
		{
			char Char = Text.charAt(start);
			// String CurrentSectionName = "";
			// Hashtable CurrentSection = new Hashtable();
			switch (Char) {
			case ':': // Section开始
				String Sec = Text.substring(start + 1, end);
				if (Sec.equals(CurrentSectionName)) {
					return;
				}
				CurrentSectionName = Sec;
				CurrentSection = GetSectionByCache(CurrentSectionName);
				if (CurrentSection == null) {
					CurrentSection = new Hashtable();
					Content.put(CurrentSectionName, CurrentSection);
					ResetCache();
				}
				//ResetCache();
				break;
			default:
				int vOffset = Text.indexOf(s_sep, start);
				if (vOffset >= 1 && vOffset < end) // 如果找到，那么就拆解KEY和VALUE
				{
					K = Text.substring(start, vOffset);
					switch (keyType)
					{
					case TYPE_INT:
					case TYPE_SHORT:
					case TYPE_BYTE:
						try{
						K = Integer.valueOf((String)K);
						}catch (Exception E)
						{
							K = new Integer(-1);
						}
						break;
					case TYPE_STRING:
						break;
					}
					V = Text.substring(vOffset + 1, end);
					switch (ValType)
					{
					case TYPE_INT:
					case TYPE_SHORT:
					case TYPE_BYTE:
					case TYPE_STRING:
						V = zIni.Split((String)V, 0,SEP_COMMA, ValType);
						break;
					case TYPE_PARAM:
						V = zIni.SplitToParam((String)V,0,SEP_COMMA);
						break;
					}
					if (V!=null)
						CurrentSection.put(K,V);
				}
				break;
			}
		}
	}
	public final String GetStr(String SectionName, Object Key,int idx) {
		Hashtable Section = GetSectionByCache(SectionName);
		String[] RetVal = null;
		if (Section!=null)
		{
			RetVal = (String[]) Section.get(Key);
			if (RetVal == null) {
				Utils.Dbg("Error GetStr: Sec=" + SectionName + " Key=" + Key);
			}
		}
		return RetVal[idx];
	}
	public final int GetInt(String SectionName, Object Key,int idx) {
		Hashtable Section = GetSectionByCache(SectionName);
		int[] RetVal = null;
		if (Section!=null)
		{
			RetVal = (int[]) Section.get(Key);
			if (RetVal == null) {
				Utils.Dbg("Error GetStr: Sec=" + SectionName + " Key=" + Key);
			}
		}
		return RetVal[idx];
	}
	public int GetStringAsInt(String SectionName, Object Key,int idx) {
		String s = GetStr(SectionName, Key,idx);
		int retVal = 0;
		if (s != null) {
			retVal = Integer.parseInt(s);
		} else {
			Utils.Dbg("Error: GetInt: Sec=" + SectionName + " Key=" + Key);
		}
		return retVal;
	}

	public final void DelSection(String SectionName)
	{
		ResetCache();
		Content.remove(SectionName);
	}
	/**
	 * 返回在资源里面以冒号开始的哈希表。
	 * @param SectionName
	 * @return
	 */
	public final Hashtable GetSection(String SectionName) {
		return GetSectionByCache(SectionName);
	}

	public final int GetSectionSize(String SectionName) {
		return GetSectionByCache(SectionName).size();
	}


	/**
	 * 指定一个分隔符，把一个字符串分解成一个数组。
	 * @param Source	要分解的字符串
	 * @param startidx		从第 N 个字段开始
	 * @param divStr		分割符
	 * @param maxsize		最大尺寸
	 * @return
	 */
	public final static int[] SplitInt(String Source, int startidx, String divStr,
			int maxsize) {
		if (Source == null)
			return null;
		int[] tempResult = (int[]) Split(Source, startidx, divStr, zIni.TYPE_INT);
		int size = tempResult.length;
		if (maxsize == size) {
			return tempResult;
		} else {
			if (maxsize < size)
				size = maxsize;
			int[] RetVal = new int[size];
			System.arraycopy(tempResult, 0, RetVal, 0, size);
			return RetVal;
		}
	}
	
	/**
	 * 去掉指定字符串中的作为换行符的"\n"
	 * @param str
	 * @return
	 */
	public final static String RemoveNewlineCharacter(String str){
		int length = str.length();
		char[] tempChar = new char[length];
		int lastPal = 0;
		for(int i = 0; i < length; i++){
			tempChar[i] = str.charAt(i);
			if(str.charAt(i) == '\\'){
				i++;
				if(str.charAt(i) == 'n')
				{
					tempChar[i] = (char)lastPal;
				}
				else
				{
					tempChar[i] = str.charAt(i);
					lastPal = str.charAt(i);
				}
			}		
		}	
		return String.valueOf(tempChar);
	}


	private static String[] _TempSplitedString ;
	private static int _TempSplitedStringCount;

	/**
	 * 用给定的分隔符，把一个字符串分解成一个字符串数组
	 * @param str
	 * @param startidx
	 * @param divStr
	 * @return
	 */
	public static final String[] Split(String str, int startidx, String divStr)
	{
		return (String[])Split(str,startidx,divStr,TYPE_STRING);
	}
	
	
	public static final Object Split(String str, int startidx, String divStr,int valType) {
		String[] retSArray = null;
		int[]	retIArray = null;
		short[] retWArray = null;
		byte[] retBArray	= null;
			
		if (str == null || divStr == null)
			return null;
		int srcLen = str.length();
		if (srcLen == 0)
			return null;
//需要的时候，才初始化这个变量，所以移动到这里来
		_TempSplitedStringCount = 0;
		_TempSplitedString = new String[Utils.ZINI_MAX_FIELD_COUNT];

		int divLen = divStr.length();
		if (divLen == 0) {
			_TempSplitedString[_TempSplitedStringCount] = str;
			_TempSplitedStringCount++;
		} else {
			int start = 0;
			int offset = start;
			while (start <= srcLen) {
				offset = str.indexOf(divStr, start);
				if (offset < 0)
					offset = srcLen;
				_TempSplitedString[_TempSplitedStringCount] = str.substring(
						start, offset);
				_TempSplitedStringCount++;
				start = offset + divLen;
				if (_TempSplitedStringCount >= Utils.ZINI_MAX_FIELD_COUNT) {
					_TempSplitedString[_TempSplitedStringCount] = str
							.substring(offset, srcLen);
					_TempSplitedStringCount++;
					break;
				}
			}
		}
		
		switch (valType)
		{
		case TYPE_INT:
			{
				int Count = _TempSplitedStringCount - startidx;
				retIArray = new int[Count];
				//这个函数比较频繁，决定把循环放到各分支
				for (int i = Count - 1;i>=0;i--) {
					retIArray[i] = Integer.valueOf(_TempSplitedString[i + startidx]).intValue();
				}
			}
			break;
		case TYPE_SHORT:
			{
				int Count = _TempSplitedStringCount - startidx;
				retWArray = new short[Count];
				//这个函数比较频繁，决定把循环放到各分支
				for (int i = Count - 1;i>=0;i--) {
					retWArray[i] = (short) Integer.valueOf(_TempSplitedString[i + startidx]).intValue();
				}
			}			
			break;
		case TYPE_BYTE:
			{
				int Count = _TempSplitedStringCount - startidx;
				retBArray = new byte[Count];
				//这个函数比较频繁，决定把循环放到各分支
				for (int i = Count - 1;i>=0;i--) {
					retBArray[i] = (byte) Integer.valueOf(_TempSplitedString[i + startidx]).intValue();
				}
			}
			break;
		case TYPE_STRING:
			int Count = _TempSplitedStringCount - startidx;
			retSArray = new String[Count];
			//这个函数比较频繁，决定把循环放到各分支
			for (int i = Count - 1;i>=0;i--) {
				retSArray[i] = _TempSplitedString[i + startidx].trim();
				_TempSplitedString[i + startidx] = null;
			}
			break;
		}
	
		_TempSplitedString = null;
		_TempSplitedStringCount = 0;
		switch (valType)
		{
		case TYPE_INT: 			return retIArray;
		case TYPE_SHORT:		return retWArray;
		case TYPE_BYTE:			return retBArray;
		case TYPE_STRING:
		default:				return retSArray;
		}
	}
	/**
	 * 将一个字符串，拆解为一个cParam<br>
	 * 数字类型的字段，必须在前面，文字类型字段，必须在后面
	 * @param str
	 * @param startidx
	 * @param divStr
	 * @return 返回的cParam中,Int[]和String[]的长度相同(定义和访问方便,浪费了1倍的OBject引用的空间，但是使用起来更方便快捷,代码编写和字段定义更加自然，数字和字符串可以混杂)
	 */
	public final static cParam SplitToParam(String str, int startidx, String divStr) {
		String[] Strs = (String[]) zIni.Split(str, startidx, divStr,TYPE_STRING); 
		if (Strs == null)
			return null;
		int[] Ints = new int[Strs.length];
		{
			/** 将能转成整数的，转成整数，并将字符串部分置为null */
			int j = 0;
				for (j = Strs.length - 1; j >=0 ; j--) {
					try {
					Ints[j] = Integer.parseInt(Strs[j]);
					Strs[j] = null;
					} catch (Exception E) {
						/** 当上面的代码出错的时候,j表示整数的数量 */
					}
					
				}
		}
		cParam define = cParam.Create();
		define.SetIntArray(Ints, 0);
		define.SetStrArray(Strs, 0);
		return define;
	}

	public final zIni Load(String resName,int keyType,int valType,String encoding){
		try
		{
			byte[] 	bytes = Utils.GetBin(resName);
			if (bytes!=null)
			{
				String strData = new String(bytes, encoding);
				bytes=null;
				Parse(strData,keyType,valType);
			}
		}
		catch(Exception E)
		{
			
		}
		return this;
	}
	/**
	 * <b>装入一个文件中的指定section</b><br>
	 * 
	 * @param filename
	 * @param SectionName
	 */
	public final static Hashtable LoadAndPickSection(String filename,int keyType,int valType,String SectionName,String encoding)
	{
		Hashtable retV = null;
		zIni ini = new zIni(filename,keyType,valType,encoding);
		if (ini!=null)
		{
			retV = ini.GetSection(SectionName);
		};
		if (DevConfig.ENABLE_ERR_INFO)
		{
			if((ini==null) || (retV==null))
					{
					Utils.Err("Can't find " + filename + " Section " + SectionName);
					}
		}

		return retV;
	}
}

