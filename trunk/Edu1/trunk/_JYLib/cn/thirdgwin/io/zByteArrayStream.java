package cn.thirdgwin.io;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

import cn.thirdgwin.lib.Utils;
/**
 * 这个类貌似不能用,因为，创建了这个类，原始数据没能释放掉，那么将造成双倍内存占用
 */

public class zByteArrayStream {
	public static final int SEEK_SET = 0;
	public static final int SEEK_CUR = 1;
	public static final int SEEK_END = 2;
	private byte[] Bytes;
	private int Offset = 0;
	
	public zByteArrayStream(byte[] data)
	{
		Bytes = data; 
	}
	public zByteArrayStream(InputStream is)
	{
		LoadFromStream(is);
	}
	private void LoadFromStream (InputStream is)
	{
		int len;
		try {
			len = is.available();
			Bytes = new byte[len];
			is.read(Bytes);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public zByteArrayStream(String resname)
	{
		InputStream is = Utils.GetResourceAsStream(resname);
		if (is!=null)
		{
			LoadFromStream(is);
		}
	}
	public final void SetData(byte[] data, int startOffset) {
		Bytes = data;
		if (startOffset < 0) {
			startOffset = 0;
		}
		Offset = startOffset;
	}
	public final void ResetData() {
		SetData(null,0);
	}
	public final int Seek(int offset,int Seekpos)
	{
		byte[] Bytes = this.Bytes;
		switch (Seekpos)
		{
		case SEEK_SET:
			Offset = offset;
			break;
		case SEEK_CUR:
			Offset +=offset;
			break;
		case SEEK_END:
			Offset = Bytes.length - offset;
			break;
		}
		if (Offset>Bytes.length)Offset = Bytes.length;
		if (Offset<0)Offset = 0;		
		return Offset;
	}
	public final byte[] GetData() {
		return Bytes;
	}

	public int GetOffset() {
		return Offset;
	}

	public final boolean EOF() {
		return (Offset >= Bytes.length);
	}
	public boolean GetBoolean() {
		return ((Bytes[Offset++] & 0xff) == 1);
	}
	public int GetUInt8() {
		return (Bytes[Offset++] & 0xff);
	}
	public int GetUInt16() {
		byte[] Bytes = this.Bytes;
		return ((Bytes[Offset++] & 0xFF) + ((Bytes[Offset++] & 0xFF) << 8));
	}
	public byte GetByte() {
		return (Bytes[Offset++]);
	}
	public short GetShort() {
		byte[] Bytes = this.Bytes;
		return (short) ((Bytes[Offset++] & 0xFF) + ((Bytes[Offset++] & 0xFF) << 8));
	}
	public int GetInt() {
		byte[] Bytes = this.Bytes;
		int Offset = this.Offset;
		int retV =  (((Bytes[Offset++] & 0xFF))
				| ((Bytes[Offset++] & 0xFF) << 8)
				| ((Bytes[Offset++] & 0xFF) << 16) | ((Bytes[Offset++] & 0xFF) << 24));
		this.Offset = Offset;
		return retV;
	}
	public long GetLong() {
		byte[] Bytes = this.Bytes;
		int Offset = this.Offset;
		long retV =  (((long)(Bytes[Offset++] & 0xFF))
				| ((long)(Bytes[Offset++] & 0xFF) << 8)
				| ((long)(Bytes[Offset++] & 0xFF) << 16)
				| ((long)(Bytes[Offset++] & 0xFF) << 24)
				| ((long)(Bytes[Offset++] & 0xFF) << 32)
				| ((long)(Bytes[Offset++] & 0xFF) << 40)
				| ((long)(Bytes[Offset++] & 0xFF) << 48) | ((long)(Bytes[Offset++] & 0xFF) << 56));
		this.Offset = Offset;
		return retV;
	}

	public void GetArray(byte[] dest, int d_offset, int size) {
		System.arraycopy(Bytes, Offset, dest, d_offset, size);
		Offset += size;
	}

	public void GetArray(int[] data, int s_offset, int size) {
		for (int i = 0; i < size; i++) {
			data[s_offset + i] = GetInt();
		}
	}
	public void GetAArray(int[][] data, int s_offset, int size) {
		for (int i = 0; i < size; i++) {
			GetArray(data[i], 0, data[i].length);
		}
	}
	public String GetStr()
			{
				String str = null;
				short msgLen = GetShort();
				if (msgLen <= 0)
					return null;
				//#if BRAND=="Motorola"
				{
					int EndPos = Offset+msgLen;
					try
					{
						StringBuffer sb = new StringBuffer();
						while (Offset<EndPos)
						{
							sb.append((char)getCharacter());
						}
						str = sb.toString();
						;
					}
					catch (Exception e)
					{
						str = "FuckMoto";
					}
				}
				//#else
				byte[] utfMsg = new byte[msgLen];
				GetArray(utfMsg, 0, msgLen);
				try
				{
					str = new String(utfMsg, "UTF-8");
				}
				catch (Exception e)
				{
					str = null;
				}
				//#endif

				return str;

			}

	//#if CHECK_UTF8_ENCODE
		public static final String	INVALID_UTF8_FORMAT	= "Wrong UTF-8";
	//#endif
		/**
		 * UTF8==> Unicode 转码函数。自主实现，不依赖系统
		 */
		public int getCharacter() throws IOException
			{
				int actualValue = -1;
				int inputValue = GetUInt8();
				if (inputValue == -1)
					return -1;
				inputValue &= 0xff;
				/** 如果是E文字 */
				if ((inputValue & 0x80) == 0)
				{
					actualValue = inputValue;
				}
				else if ((inputValue & 0xF8) == 0xF0)	// 4 个字节
				{
					actualValue = (inputValue & 0x7) << 18;
					int nextByte = GetUInt8() & 0xff;
					//#if CHECK_UTF8_ENCODE
					if ((nextByte & 0xC0) != 0x80)
						throw new IOException(INVALID_UTF8_FORMAT);
					//#endif
					actualValue += (nextByte & 0x3F) << 12;

					nextByte = GetUInt8() & 0xff;
					//#if CHECK_UTF8_ENCODE
					if ((nextByte & 0xC0) != 0x80)
						throw new IOException(INVALID_UTF8_FORMAT);
					//#endif
					actualValue += (nextByte & 0x3F) << 6;

					nextByte = GetUInt8() & 0xff;
					//#if CHECK_UTF8_ENCODE
					if ((nextByte & 0xC0) != 0x80)
						throw new IOException(INVALID_UTF8_FORMAT);
					//#endif
					actualValue += (nextByte & 0x3F);
				}
				else if ((inputValue & 0xF0) == 0xE0)	// 3 个字节
				{
					actualValue = (inputValue & 0xf) << 12;

					int nextByte = GetUInt8() & 0xff;
					//#if CHECK_UTF8_ENCODE
					if ((nextByte & 0xC0) != 0x80)
						throw new IOException(INVALID_UTF8_FORMAT);
					//#endif
					actualValue += (nextByte & 0x3F) << 6;

					nextByte = GetUInt8() & 0xff;
					//#if CHECK_UTF8_ENCODE
					if ((nextByte & 0xC0) != 0x80)
						throw new IOException(INVALID_UTF8_FORMAT);
					//#endif
					actualValue += (nextByte & 0x3F);
				}
				else if ((inputValue & 0xE0) == 0xC0)	// 2 个字节
				{
					actualValue = (inputValue & 0x1f) << 6;

					int nextByte = GetUInt8() & 0xff;
					//#if CHECK_UTF8_ENCODE
					if ((nextByte & 0xC0) != 0x80)
						throw new IOException(INVALID_UTF8_FORMAT);
					//#endif
					actualValue += (nextByte & 0x3F);
				}
				return actualValue;
			}

	// /////////////////////
	public int ensureCapacity(int size)
	{
		if (Bytes.length<size)
		{
			byte[] newBytes = new byte[size];
			System.arraycopy(Bytes,0,newBytes,0,Bytes.length);
			Bytes = newBytes;
		}
		return Bytes.length;
	}
	public void SetBoolean(boolean bl) {
		Bytes[Offset++] = (byte) (bl ? 1 : 0);
	}

	public void SetByte(int b) {
		Bytes[Offset++] = (byte) b;
	}

	public void SetShort(int s) {
		Bytes[Offset++] = (byte) (s & 0xFF);
		Bytes[Offset++] = (byte) ((s >> 8) & 0xFF);
	}

	public void SetInt(int i) {
		byte[] Bytes = this.Bytes;
		int Offset = this.Offset;
		Bytes[Offset++] = (byte) ((i >> 0) & 0xFF);
		Bytes[Offset++] = (byte) ((i >> 8) & 0xFF);
		Bytes[Offset++] = (byte) ((i >> 16) & 0xFF);
		Bytes[Offset++] = (byte) ((i >> 24) & 0xFF);
		this.Offset = Offset;
	}

	public void SetArray(byte[] data, int s_offset, int size) {
		System.arraycopy(data, s_offset, Bytes, Offset, size);
		Offset += size;
	}
	public void SetArray(int[] data, int s_offset, int size) {
		for (int i = 0; i < size; i++) {
			SetInt(data[s_offset + i]);
		}
	}
	public void SetAArray(int[][] data, int s_offset, int size) {
		for (int i = 0; i < size; i++) {
			SetArray(data[i], 0, data[i].length);
		}
	}
	public void SetStr(String str)
	{
		int len =str.length();
		byte[] bytes =str.getBytes();
		SetShort(len);
		SetArray(bytes,0,len);
	}
	public byte[] GetBin()
	{
		return Bytes;
	}
}
