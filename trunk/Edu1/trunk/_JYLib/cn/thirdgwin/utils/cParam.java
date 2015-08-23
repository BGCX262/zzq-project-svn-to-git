package cn.thirdgwin.utils;
import java.io.ByteArrayOutputStream;

import cn.thirdgwin.lib.Utils;

/**
 * 一个通用的数据结构体，cActor cMsg 以及子类使用 本结构体来保存和传送数据 根据目前的情况，支持数字和字符串基本满足大部分需要
 * */
public class cParam {
	/** 整数属性 */
	private int m_data[];
	/** 文字属性 */
	private String m_str[];

	// /** 外观属性 */
	// public static final int VIDX_ANI = 0;
	// public static final int VIDX_FRAME = VIDX_ANI + 1;
	// public static final int VIDX_FRAME_TIME = VIDX_FRAME + 1;
	// public static final int VIDX_MAX = VIDX_FRAME_TIME + 1;
	// public short[] VData = new short[VIDX_MAX];
	// public ASprite View; //这个参数主要是供技能系统使用
	// public void Init(int intsize, int strsize) {
	// if (intsize > 0)
	// m_data = new int[intsize];
	// if (strsize > 0)
	// m_str = new String[strsize];
	// }
	protected cParam() {
	}
	public final void SetIntArray(int[] params, int intsize)
	{
		SetIntArray(params,intsize,true);
	}
	public final void SetIntArray(int[] params, int intsize,boolean shouldclone) {
		if (shouldclone)
			{
			m_data = new int[params.length];
			System.arraycopy(params,0,m_data,0,params.length);
			}
		else
			{
			m_data = params;
			}
	}

	public final void SetStrArray(String[] params, int strsize) {
		SetStrArray(params,strsize,true);
	}
	public final void SetStrArray(String[] params, int strsize,boolean shouldclone) {
		if (shouldclone)
		{
		m_str = new String[params.length];
		System.arraycopy(params,0,m_str,0,params.length);
		}
	else
		{
		m_str = params;
		}
	}	
	public final void SetInt(int idx,int value) {
			m_data[idx] = value;
	}

	public final int GetInt(int idx) {

		return m_data[idx];
	}
	
	public final int[] GetDataInt() {
		return m_data;
	}

	public final String GetStr(int idx) {
		return m_str[idx];
	}

	public final String[] GetDataStr() {
		return m_str;
	}
	
	/** 所有的类均不对外提供构造函数，强制让所有的对象通过Get/Release方法创造和销毁 */

	//kinba
	public static boolean USE_PARAM_POOL = true;
	public static cParam[] __param_pool= new cParam[Utils.PARAM_POOL_SIZE];
	public static int __param_pool_count = 0;
	//kinba
	public static void Pool_Init() {
		for(int i=0;i<Utils.PARAM_POOL_SIZE;i++) {
			__param_pool[i] = new cParam();
		}
		__param_pool_count = Utils.PARAM_POOL_SIZE;
	}
	
	public static cParam Pool_GetParam() {
		cParam retV;
		if (USE_PARAM_POOL) {
			if (__param_pool_count > 0) {
				retV = __param_pool[__param_pool_count - 1];
				__param_pool[__param_pool_count - 1] = null;
				__param_pool_count--;
			} else {
				retV = new cParam();
			}
		} else {
			retV = new cParam();
		}
		return retV;
	}


	public static void Pool_Return(cParam param) {
		if(param == null)
			return;
		if (USE_PARAM_POOL) {
			param.m_data = null;
			param.m_str = null;
			if (__param_pool_count < Utils.PARAM_POOL_SIZE) {
				__param_pool[__param_pool_count] = param;
				__param_pool_count++;
			}

		}
	}
	
	/** 申请和释放 */
	public static cParam Create() {
		if (USE_PARAM_POOL) {
			return Pool_GetParam();
		} else {
			return new cParam();
		}
	}

	public static void Release(cParam param) {
		if(param == null)
			return;
		if (USE_PARAM_POOL) {
			Pool_Return(param);
		} else {
			param.m_data = null;
			param.m_str = null;
		}

	}

	/** Save/Load */
	public static byte[] SaveToByteStream(cParam param) {
		// TODO:Implement SaveToByteStream
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		byte[] AAA;
		return bos.toByteArray();
	}

	public static cParam LoadFromByteStream(byte[] data) {
		// TODO:: 实现cParam的输入和导出
		cParam RetVal;

		return null;
	}
}
