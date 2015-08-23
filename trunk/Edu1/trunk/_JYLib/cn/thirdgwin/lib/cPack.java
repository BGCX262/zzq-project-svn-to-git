package cn.thirdgwin.lib;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Hashtable;

import cn.thirdgwin.io.zRMS;

public class cPack {

	/// \defgroup GLLib_file GLLib : File/Package System
	///	handle data file access (read only)
	///
	///	Packages are composed of at least one data file (subpack).
	///
	///	The first data file (first subpack) contain extra information about
	///	all the data stored within the pack.
	///
	/// Extra information for 1st subpack, at the very beginning of the file:
	///
	///	- 2 byte : Number of data within the pack.
	///			How many data are store within this pack (including all subpack)).
	///
	///	- 2 byte : Number of subpacks.
	///			How many subpacks compose this pack.
	///
	/// - 2 byte * number of subpack (fat table):
	/// 		List the starting data number of each subpack.<br>
	/// 		Example with 3 sub pack we could have : 0, 8, 15 :
	/// 			- Subpack0 contain data(0) to data(7).
	/// 			- Subpack1 contain data(8), data(14).
	/// 			- Subpack2 contain data(15) to data(nb of data).
	///
	///	Then each subpack (including the first one) contain:
	/// 	- 4 byte * number of data in this subpack + 1 : offset table.
	///			This offset is the offset of each data from the begining of this file to the data.
	///			The +1 is for an extra value which represent the end of the file, this
	///			is used to calulate the size of each data within the subpack
	///			(size of data) = (offset of next data) - (offset of the data).
	///
	///	Finaly, for each data inside the subpack:
	///		- 1 byte : mime type of the data except if file is a dummy file (eg size = 0)
	///		- x byte : the data
	///
	///
	/// There is also one "special" pack, which contain the MIME type if some were defined:
	///	- 1 byte : Mime type count.
	///	- For each mime type:
	///		- 1 byte : length of mime type byte array.
	///		- length of mime type byte : byte array encoded as UTF-8 representing the mime type.
	///!ingroup GLLibMain
	///!{


	final public static int ARRAY_BYTE 	= 0;
	final public static int ARRAY_SHORT 	= 1;
	final public static int ARRAY_INT 		= 2;


	/// Current pack file filename. Null if none is open.
	private static String		s_pack_filename;

	/// Current pack input stream. Null if none is open.
	private static InputStream  s_pack_is;

	/// Current offset in current Stream. 0 if none is open.
	private static int 			s_pack_curOffset;

	/// Number of data in data file.
	private static short 		s_pack_nbData;

	/// Array which contains offsets of each data inside it's subpack file.
	private static int[] 		s_pack_offset;

	/// Number of subpacks for this package.
	private static short 		s_pack_subPack_nbOf;

	/// Subpack division of the package (FAT).
	private static short[] 		s_pack_subPack_fat;

	/// Current subpack file filename. Null if none is open.
	private static String 		s_pack_subPack_filename;

	/// Currently opened subpack.
	private static int 			s_pack_subPack_curSubPack;

	/// Mime type of last data that have been read through Pack_Read.
	static int 					s_pack_lastDataReadMimeType;

	/// Compression status of last data that have been read through Pack_Read.
	private static boolean		s_pack_lastDataIsCompress;

	/// Skip buffer. Used only if needed.
	private static byte 		s_Pack_SkipBuffer[];

	/// MIME type array. Stored as UTF-8.
	static byte[][]MIME_type;

	///!{
	/// Used to store the binary data loaded, only if pack_keepLoaded=true.
	/// This system need an index file called : 999.
	/// Its format should be :
	///		- 1 x int : pack and subpack count
	///		- foreach:
	///					- int    : filename length
	///					- byte[] : filename
	///					- int    : file size
	private static byte[][]		s_pack_BinaryCache;
	private static Hashtable	s_pack_HashIndex;
	private static Hashtable	s_pack_HashSize;
	///!}

	//--------------------------------------------------------------------------------------------------------------------
	// Wrapper of midp getResourceAsStream for alow to override it in others platforms that dont support it
	//!param filename Filename of the package to open.
	//--------------------------------------------------------------------------------------------------------------------
	public static InputStream GetResourceAsStream(String s)
	{


		InputStream is = s.getClass().getResourceAsStream(s);

	  return is;
	}


	//--------------------------------------------------------------------------------------------------------------------
	/// Open a pack given its path and filename. Example: Pack_Open("/0");
	///!param filename Filename of the package to open.
	///!note This load the whole pack in memory watch for phone constraint.
	//--------------------------------------------------------------------------------------------------------------------
	public static void Pack_Open(String filename)
	{
		if(!(filename!= null))Utils.DBG_PrintStackTrace(false, "Pack_Open.filename is null");;

		if (true && (DevConfig.pack_dbgDataAccess))
		{
			Utils.Dbg("Pack_Open("+filename+")");
		}

		// if we are opening an already opened data file, return imediately
		if ((s_pack_filename != null) && (filename.compareTo(s_pack_filename) == 0))
		{
			if (true && (DevConfig.pack_dbgDataAccess))
			{
				Utils.Dbg("Pack_Open("+filename+").file already open");
			}
			return;
		}

		// if a pack file was previously opened, close it
		//if (s_pack_is != null) -. if we get here reset pack value, or need at least to reset s_pack_curOffset
		{
			Pack_Close();
		}

		// get pack name
		s_pack_filename = filename;


		// get input stream
		s_pack_is = Pack_GetInputStreamFromName(s_pack_filename);
		if(!(s_pack_is!= null))Utils.DBG_PrintStackTrace(false, "Pack_Open.unable to find file");;

		// get nb data total
		s_pack_nbData = (short) Pack_Read16();

		if (true && (DevConfig.pack_dbgDataAccess))
		{
			Utils.Dbg("   nb of data in pack : "+s_pack_nbData);
		}

		// get nb of sub-pack for this pack
		s_pack_subPack_nbOf = (short) Pack_Read16();

		if (true && (DevConfig.pack_dbgDataAccess))
		{
			Utils.Dbg("   nb of subpack for this pack : "+s_pack_subPack_nbOf);
		}

		// get sub-pack division
		s_pack_subPack_fat = new short[s_pack_subPack_nbOf];

		for (int i=0; i<s_pack_subPack_nbOf; i++)
		{
			s_pack_subPack_fat[i] = (short) Pack_Read16();
			if (true && (DevConfig.pack_dbgDataAccess))
			{
				Utils.Dbg("   subpack "+i+" start with data : "+s_pack_subPack_fat[i]);
			}

		}

		// set current subpack as 0 . main subpack and get data offset for this pack
		s_pack_subPack_curSubPack = 0;
		Pack_GetDataOffset();
	}


	//--------------------------------------------------------------------------------------------------------------------
	/// Really open a pack or subpack given its path and filename.
	/// Since a filename can be constructed, or changed if there is need to change subpack,
	/// this function can be called from many places.
	///!param strName Filename of the package to open.
	//--------------------------------------------------------------------------------------------------------------------
	private static InputStream Pack_GetInputStreamFromName(String strName)
	{
		InputStream pStream;

		// if the pack filename doesn't start with "/", then we're not loading from regular packs . we're loading from rms instead
		if(DevConfig.rms_usePackRead && (strName.startsWith("/") == false))
		{
			pStream = zRMS.GetRmsInputStream(strName);
		}
		else if(DevConfig.pack_keepLoaded == true)
		{
			if(s_pack_HashIndex == null)
			{
				s_pack_HashIndex 	= new Hashtable();
				s_pack_HashSize 	= new Hashtable();

				try
				{
					//read file index

					s_pack_is = Utils.GetResourceAsStream("/all"+DevConfig.JY_DATA_SPECIAL_END);


					int 	nCount = Pack_Read32();

					s_pack_BinaryCache = new byte[nCount][];

					int 	nStrLength;
					byte[] 	pStr;
					String 	strFileName;
					int 	nSize;

					for(int i = 0; i < nCount; i++)
					{
						nStrLength 	= Pack_Read32();
						pStr 		= new byte[nStrLength];

						s_pack_is.read(pStr);
						strFileName	= new String(pStr);

						nSize 		= Pack_Read32();

						s_pack_HashIndex.put("/"+strFileName, new Integer(i));
						s_pack_HashSize.put("/"+strFileName, new Integer(nSize));
					}

					Pack_Close();
				}
				catch(Exception e)
				{
					Utils.Dbg("   Exception while reading file INDEX");
				}
			}

			int nDataIndex = ((Integer)s_pack_HashIndex.get(strName)).intValue();

			if(s_pack_BinaryCache[nDataIndex] == null)
			{
				//read file
				int nSize = ((Integer)s_pack_HashSize.get(strName)).intValue();

				s_pack_BinaryCache[nDataIndex] = new byte[nSize];

				try
				{
					s_pack_is = Utils.GetResourceAsStream(strName);
					s_pack_is.read(s_pack_BinaryCache[nDataIndex]);
					s_pack_is.close();
					s_pack_is = null;
				}
				catch(Exception e)
				{
					Utils.Dbg("   Exception while reading file to cache : " + strName);
				}
			}

			if(!(s_pack_BinaryCache[nDataIndex] != null))Utils.DBG_PrintStackTrace(false, "   s_pack_BinaryCache[nDataIndex]==null error");;

			pStream = new ByteArrayInputStream(s_pack_BinaryCache[nDataIndex]);
		}
		else
		{
			// get input stream
			

				{
					pStream = Utils.GetResourceAsStream(strName);
				}
		}

		return pStream;
	}

	//--------------------------------------------------------------------------------------------------------------------
	/// Release cache data of a pack and subpack
	///!param filename Filename of the package to release.
	//--------------------------------------------------------------------------------------------------------------------
	static void Pack_ReleaseBinaryCache(String filename)
	{
		if(DevConfig.pack_keepLoaded)
		{
			int nDataIndex = ((Integer)s_pack_HashIndex.get(filename)).intValue();

			if (s_pack_BinaryCache[nDataIndex] != null)
			{
				short nbSubPack = Mem_GetShort(s_pack_BinaryCache[nDataIndex], 2);
				s_pack_BinaryCache[nDataIndex] = null;
				for (int i = 1; i < nbSubPack; i++)
				{

					String tempname = filename;
					if(filename.endsWith(DevConfig.JY_DATA_SPECIAL_END)){
						tempname = filename.substring(0,filename.length()-DevConfig.JY_DATA_SPECIAL_END.length());
					}
					String strSubPack = tempname +"_"+ i+DevConfig.JY_DATA_SPECIAL_END;


					nDataIndex = ((Integer)s_pack_HashIndex.get(strSubPack)).intValue();
					s_pack_BinaryCache[nDataIndex] = null;
				}
			}
		}
	}

	//--------------------------------------------------------------------------------------------------------------------
	/// Read the offsets of each data of this subpack.
	//--------------------------------------------------------------------------------------------------------------------
	private static void Pack_GetDataOffset()
	{
		// nb data in this subpack
		int nbData;
		if (s_pack_subPack_curSubPack == s_pack_subPack_nbOf-1)
		{
			nbData = s_pack_nbData - s_pack_subPack_fat[s_pack_subPack_curSubPack];
		}
		else
		{
			nbData = s_pack_subPack_fat[s_pack_subPack_curSubPack + 1] - s_pack_subPack_fat[s_pack_subPack_curSubPack];
		}

		if (true && (DevConfig.pack_dbgDataAccess))
		{
			Utils.Dbg("   nb of data in this subpack : "+nbData);
		}

		// get data offset
		s_pack_offset = new int[nbData+1];

		for (int i = 0; i < nbData+1; i++)
		{
			s_pack_offset[i] = Pack_Read32();

			if (true && (DevConfig.pack_dbgDataAccess))
			{
				Utils.Dbg("   data "+i+" is & offset : "+s_pack_offset[i]);
			}
		}
	}

	//--------------------------------------------------------------------------------------------------------------------
	/// Close a pack file. Nothing will be done if there are no pack currently open.
	///!note This is the only way to free the memory comsumed by a pack loaded in memory.
	/// GLLib.Gc() will be called by this function.
	//--------------------------------------------------------------------------------------------------------------------
	public static void Pack_Close()
	{
		if (s_pack_is != null)
		{
			try
			{
				s_pack_is.close();
			}
			catch (Exception e)
			{
			}

			s_pack_is = null;
		}

		s_pack_curOffset 	= 0;

		// free rms buffer
		if(DevConfig.rms_usePackRead)
		{
			zRMS.Rms_cleanBuffer();
		}

		GLLib.Gc();
	}


	//--------------------------------------------------------------------------------------------------------------------
	/// Position the pack pointer to the begining of data idx. If the data requested is not in this subpack,
	/// the correct subpack will be opened. This can be usefull if you want to read/stream/parse the pack-stream by yourself.
	///!return Length of the requested data.
	///!note s_pack_lastDataReadMimeType is now valid for the requested data.
	//--------------------------------------------------------------------------------------------------------------------
	public static int Pack_PositionAtData(int idx)
	{
		if(!(idx >= 0))Utils.DBG_PrintStackTrace(false, "Pack_PositionAtData.idx is invalid");;
		if(!(idx < s_pack_nbData))Utils.DBG_PrintStackTrace(false, "Pack_PositionAtData.idx is invalid");;

		if (true && (DevConfig.pack_dbgDataAccess))
		{
			Utils.Dbg("Pack_PositionAtData("+idx+")  current sub : " + s_pack_subPack_curSubPack);
		}

		//get subpack number
		int subpack;

		for (subpack=s_pack_subPack_nbOf-1; subpack>=0; subpack--)
		{
			if (s_pack_subPack_fat[subpack] <= idx)
			{
				break;
			}
		}

		if (true && (DevConfig.pack_dbgDataAccess))
		{
			Utils.Dbg("   data "+idx+" is in subpack : "+subpack);
		}

		// open subpack if not already opened
		if (s_pack_subPack_curSubPack != subpack)
		{
			if (true && (DevConfig.pack_dbgDataAccess))
			{
				Utils.Dbg("   opening subpack : "+subpack);
			}

			s_pack_subPack_curSubPack = subpack;

			// get input stream
			Pack_Close();

			if (s_pack_subPack_curSubPack == 0)
			{
				String name = s_pack_filename;
				s_pack_filename = null;
				Pack_Open(name);
			}
			else
			{

				String tempname = s_pack_filename;
				if(s_pack_filename.endsWith(DevConfig.JY_DATA_SPECIAL_END)){
					tempname = s_pack_filename.substring(0,s_pack_filename.length()-DevConfig.JY_DATA_SPECIAL_END.length());
				}
				s_pack_is = Pack_GetInputStreamFromName(tempname +"_"+ s_pack_subPack_curSubPack+DevConfig.JY_DATA_SPECIAL_END);


				Pack_GetDataOffset();
			}
		}
		else if (s_pack_is == null)
		{
			if (true && (DevConfig.pack_dbgDataAccess))
			{
				Utils.Dbg("   reopening subpack : "+subpack);
			}

			if (s_pack_subPack_curSubPack == 0)
			{
				String name = s_pack_filename;
				s_pack_filename = null;
				Pack_Open(name);
			}
			else
			{

				String tempname = s_pack_filename;
				if(s_pack_filename.endsWith(DevConfig.JY_DATA_SPECIAL_END)){
					tempname = s_pack_filename.substring(0,s_pack_filename.length()-DevConfig.JY_DATA_SPECIAL_END.length());
				}
				s_pack_is = Pack_GetInputStreamFromName(tempname +"_"+ s_pack_subPack_curSubPack+DevConfig.JY_DATA_SPECIAL_END);


			}
		}

		// get index of the data to read in this subpack
		idx = idx - s_pack_subPack_fat[s_pack_subPack_curSubPack];
		int offset = s_pack_offset[idx];
		int size = s_pack_offset[idx+1] - s_pack_offset[idx];

		if (true && (DevConfig.pack_dbgDataAccess))
		{
			Utils.Dbg("   data is at index "+idx+" in this subpack & offset "+offset+" ("+size+" byte)");
		}

		// position the file pointer to data offset
		Pack_Seek(offset);

		s_pack_lastDataIsCompress = false;

		// get mime type of the data
		if (size > 0) //load only mime type if not dummy entry
		{
			s_pack_lastDataReadMimeType = (Pack_Read() & 0XFF);

			if(DevConfig.pack_supportLZMADecompression && (s_pack_lastDataReadMimeType >= 127))
			{
				s_pack_lastDataReadMimeType -= 	127;
				s_pack_lastDataIsCompress 	= 	true;
			}

			size--;
		}

		if (true && (DevConfig.pack_dbgDataAccess))
		{
			if (MIME_type != null)
				Utils.Dbg("   data mime type is "+ s_pack_lastDataReadMimeType+" ("+GetMIME(s_pack_lastDataReadMimeType)+")");
		}

		// return length of the data
		return size;
	}

	//--------------------------------------------------------------------------------------------------------------------
	/// Read and return the data at idx. If needed, the system, will seek to the data, or even open another subpack.
	///!param idx Index of the data to read in this package.
	///!return A byte array containing the data requested.
	//--------------------------------------------------------------------------------------------------------------------
	public static byte[] Pack_ReadData(int idx)
	{
		if(!(idx >= 0))Utils.DBG_PrintStackTrace(false, "Pack_ReadData.idx is invalid");;
		if(!(idx < s_pack_nbData))Utils.DBG_PrintStackTrace(false, "Pack_ReadData.idx is invalid");;

		if (true && (DevConfig.pack_dbgDataAccess))
		{
			Utils.Dbg("Pack_ReadData("+idx+")");
		}

		int 	size = Pack_PositionAtData(idx);
		byte[] 	data = null;

		if(DevConfig.pack_supportLZMADecompression && s_pack_lastDataIsCompress)
		{
			try
			{
				
					LZMA_Inflate(s_pack_is, size);

					//Adjust our stream
					s_pack_curOffset += size;

					data 		= m_outStream;
					m_outStream = null;
				

			}
			catch(Exception e)
			{
				if(!(false))Utils.DBG_PrintStackTrace(false, "LZMA decompression failed : " + e.toString());;
			}
		}
		else
		{
			// allocate buffer for the data
			data = new byte[size];

			// read the data
			Pack_ReadFully(data, 0, data.length);
		}


		if (true && (DevConfig.pack_dbgDataAccess))
		{
			Utils.Dbg("     Pack_ReadData("+idx+")  DONE");
		}

		// return the data
		return data;
	}

	//--------------------------------------------------------------------------------------------------------------------
	/// Set the current offset to addr byte from the beginning.
	///!param addr Offset to seek to from the begining of the current stream.
	//--------------------------------------------------------------------------------------------------------------------
	public static void Pack_Seek(int addr)
	{
		// if (ISDEBUG && (zJYLibConfig.pack_dbgDataAccess))
		// {
			// Dbg("     Pack_Seek("+addr+") curOffset is "+s_pack_curOffset);
		// }

		if (s_pack_curOffset == addr)
			return;

		// if the current offset is past the destination offset
		if (s_pack_curOffset > addr)
		{
			// reset the current offset
			if (true && (DevConfig.pack_dbgDataAccess))
			{
				Utils.Dbg("Pack Close BAD READING ORDER");
			}

			Pack_Close();

			if (s_pack_subPack_curSubPack == 0)
			{
				s_pack_is = Pack_GetInputStreamFromName(s_pack_filename);
			}
			else
			{

				String tempname = s_pack_filename;
				if(s_pack_filename.endsWith(DevConfig.JY_DATA_SPECIAL_END)){
					tempname = s_pack_filename.substring(0,s_pack_filename.length()-DevConfig.JY_DATA_SPECIAL_END.length());
				}
				s_pack_is = Pack_GetInputStreamFromName(tempname + "_" + s_pack_subPack_curSubPack+DevConfig.JY_DATA_SPECIAL_END);


			}
			if(!(s_pack_is != null))Utils.DBG_PrintStackTrace(false, "Pack_Seek.internal error");;
		}
		// current offset is before the destination offset
		else
		{
			// calculate the nb of byte to skip
			addr -= s_pack_curOffset;
		}

		// skip directly to the correct offset
		Pack_Skip(addr);

		// if (ISDEBUG && (zJYLibConfig.pack_dbgDataAccess))
		// {
			// Dbg("        curOffset is now "+s_pack_curOffset);
		// }
	}


	//--------------------------------------------------------------------------------------------------------------------
	/// Skip ahead in the current stream. Move forward the current offset in the stream.
	///!param nb Number of byte to skip in data file.
	///!throw Exception if error occured.
	//--------------------------------------------------------------------------------------------------------------------
	public static void Pack_Skip(int nb)
	{
		// if (ISDEBUG && (zJYLibConfig.pack_dbgDataAccess))
		// {
			// Dbg("     Pack_Skip("+nb+")");
		// }

		if (nb == 0)
			return;

		// if no skipbuffer should be use, use the regular java.io.InputStream.skip() function
		if (DevConfig.pack_skipbufferSize == 0)
		{
			s_pack_curOffset += nb;

			try
			{
				while(nb > 0)
				{
					nb -= s_pack_is.skip(nb);
				}
			}
			catch (Exception e)
			{
				if(!(false))Utils.DBG_PrintStackTrace(false, "Pack_Skip.IO exception occured");;
			}
		}
		// if skipbuffer should be use, skip using java.io.InputStream.read()
		else
		{
			// allocate skip buffer the first time
			if (s_Pack_SkipBuffer == null)
			{
				s_Pack_SkipBuffer = new byte[DevConfig.pack_skipbufferSize];
			}

			//skip multiple of k_file_skipbufferSize bytes
			while (nb > DevConfig.pack_skipbufferSize)
			{
				Pack_ReadFully(s_Pack_SkipBuffer, 0, DevConfig.pack_skipbufferSize);
				nb -= DevConfig.pack_skipbufferSize;
			}

			// skip remaining bytes
			if (nb > 0)
			{
				Pack_ReadFully(s_Pack_SkipBuffer, 0, nb);
			}
		}
	}

	//--------------------------------------------------------------------------------------------------------------------
	/// Read a single byte from the current stream.
	///!return one byte as a int, or -1 if EOF.
	///!throw Exception if error occured.
	//--------------------------------------------------------------------------------------------------------------------
	public static int Pack_Read()
	{
		int read = 0;
		try
		{
			read = s_pack_is.read();
		}
		catch (Exception e)
		{
			if(!(false))Utils.DBG_PrintStackTrace(false, "Pack_Read.IO exception occured");;
		}
		if(!(read >= 0))Utils.DBG_PrintStackTrace(false, "Pack_Read.EOF");;
		// we read something
		s_pack_curOffset++;
		return read;
	}

	//--------------------------------------------------------------------------------------------------------------------
	/// Read one unsigned short from the current stream. LittleEndian Format.
	///!return Return unsigned short value as an int.
	///!throw Exception if error occured.
	//--------------------------------------------------------------------------------------------------------------------
	public static int Pack_Read16()
	{
		return ((Pack_Read() & 0xFF) | ((Pack_Read() & 0xFF) << 8));
	}

	//--------------------------------------------------------------------------------------------------------------------
	/// Read one int from the current stream. LittleEndian Format.
	///!return Return int value.
	///!throw Exception if error occured.
	//--------------------------------------------------------------------------------------------------------------------
	public static int Pack_Read32()
	{
		return (((Pack_Read() & 0xFF) | ((Pack_Read() & 0xFF) << 8)) | (((Pack_Read() & 0xFF) << 16) | ((Pack_Read() & 0xFF) << 24)));
	}

	//--------------------------------------------------------------------------------------------------------------------
	/// Read into a byte array.
	///!param array Array to store data read, array must be non null.
	///!param offset Offset to store byte in array.
	///!param length Number of byte to read.
	///!return Number of byte read, must be length or exception.
	//--------------------------------------------------------------------------------------------------------------------
	public static int Pack_ReadFully(byte[] array, int offset, int length)
	{
		if(!(array != null))Utils.DBG_PrintStackTrace(false, "Pack_ReadFully.array is null");;
		if(!(offset >= 0))Utils.DBG_PrintStackTrace(false, "Pack_ReadFully.offset is negative");;
		if(!(length >= 0))Utils.DBG_PrintStackTrace(false, "Pack_ReadFully.length is negative");;
		if(!(offset+length <= array.length))Utils.DBG_PrintStackTrace(false, "Pack_ReadFully.offset+length is bigger than array size");;

		int off = offset;
		int len = length;
		int read = 0;
		try
		{
			while (len > 0)
			{
				read = s_pack_is.read(array, off, len);
				if(!(read >= 0))Utils.DBG_PrintStackTrace(false, "Pack_ReadFully.EOF");;
				len -= read;
				off += read;
			}
		}
		catch (Exception e)
		{
			if(!(false))Utils.DBG_PrintStackTrace(false, "Pack_Read.IO exception occured");;
		}

		s_pack_curOffset += length;

		return length;
	}


	//--------------------------------------------------------------------------------------------------------------------
	/// Read an array or multiarray from the stream.<br>
	/// ID is encoded as:
	/// 	- bit 0-1   = type of data array (ARRAY_BYTE, ARRAY_SHORT, ARRAY_INT).
	///		- bit 2 	= 1 if multidimension array, 0 otherwise.
	///		- bit 3 	= encoding for nb of component in array (0=byte, 1 = short).
	///		- bit 4-5   = if multidimensional array . dimension of array else real padding of data inside array.
	///		- bit 6-7   = unused.
	/// <p>
	///!param idx Index of the array/multiarray to read.
	///!return Array or multiarray as an Object.
	///!throw Exception if error occured.
	//--------------------------------------------------------------------------------------------------------------------
	public static Object Pack_ReadArray(int idx)
	{
		if(!(idx >= 0))Utils.DBG_PrintStackTrace(false, "Pack_ReadArray.idx is invalid");;
		if(!(idx < s_pack_nbData))Utils.DBG_PrintStackTrace(false, "Pack_ReadArray.idx is invalid");;

		if (true && (DevConfig.pack_dbgDataAccess))
		{
			Utils.Dbg("Pack_ReadArray("+idx+")");
		}

		int size = Pack_PositionAtData(idx);
		if (true && (DevConfig.pack_dbgDataAccess))
		{
			Utils.Dbg("size of data is "+size);
		}

		Stream_readOffset = 0;
		Object array;

		if(DevConfig.pack_supportLZMADecompression && s_pack_lastDataIsCompress)
		{
			byte[] data = Pack_ReadData(idx);

			ByteArrayInputStream bis =  new ByteArrayInputStream(data);

			array = Mem_ReadArray(bis);

			bis 	= null;
			data 	= null;
		}
		else
		{
			array = Mem_ReadArray(s_pack_is);
			s_pack_curOffset += Stream_readOffset;
		}

		return array;
	}

	//--------------------------------------------------------------------------------------------------------------------
	/// Load the MIME type from a MIME Pack.
	///!param filename Filename of the pack containing the MIME type.
	//--------------------------------------------------------------------------------------------------------------------
	public static void Pack_LoadMIME(String filename)
	{
		if(!(filename != null))Utils.DBG_PrintStackTrace(false, "Pack_LoadMIME.filename is null");;

		// get mime type if not loaded yet (only the very first time)
		if (MIME_type == null)
		{
			//hack to use s_pack_is input stream
			InputStream is = s_pack_is;

			// get input stream
			s_pack_is = Pack_GetInputStreamFromName(filename);

			// store nb of mime type
			int nbOfMime = Pack_Read();
			if (true && (DevConfig.pack_dbgDataAccess))
			{
				Utils.Dbg("nb Of MIME type : "+nbOfMime);
			}

			MIME_type = new byte[nbOfMime][];

			for (int i=0; i<nbOfMime; i++)
			{
				int len = Pack_Read();
				MIME_type[i] = new byte[len];
				Pack_ReadFully(MIME_type[i], 0, len);
			}

			try
			{
				s_pack_is.close();
			}
			catch (Exception e)
			{
				if(!(false))Utils.DBG_PrintStackTrace(false, "Pack_LoadMIME.IO Error");;
			}

			// restore s_pack_is
			s_pack_is = is;
		}
	}

	//--------------------------------------------------------------------------------------------------------------------
	/// Get the MIME type as a String for a MIME index.
	///!param idx Idex to get MIME type.
	///!return String containing the requested MIME type or empty string.
	//--------------------------------------------------------------------------------------------------------------------
	public static String GetMIME(int idx)
	{
		if(!(MIME_type != null))Utils.DBG_PrintStackTrace(false, "GetMIME. MIME type not loaded yet, use Pack_LoadMIME first");;

		if (idx >= MIME_type.length)
		{
			return "";
		}
		else
		{
			try
			{
				return new String(MIME_type[idx], "UTF-8");
			}
			catch (Exception e)
			{
				return "";
			}
		}
	}

	//--------------------------------------------------------------------------------------------------------------------
	/// Get the MIME Index based on MIME type string.
	///
	///!param sMIME - The string representing the MIME type to find
	///!param bAdd - If TRUE then if the type is not found it will be added.
	///!return The index of this MIME type. -1 if not found and not added.
	///
	///!note Used for Sprint URL loading where MIME type is needed but is not packaged.
	//--------------------------------------------------------------------------------------------------------------------
	public static int GetMIMEIndex (String sMIME, boolean bAdd)
	{
		if(!(MIME_type != null))Utils.DBG_PrintStackTrace(false, "GetMIMEIndex: MIME type not loaded yet, use Pack_LoadMIME first");;

		// Search existing MIME list
		for (int i=0; i<MIME_type.length; i++)
		{
			if( GetMIME(i).compareTo(sMIME) == 0)
			{
				Utils.Dbg("GetMIMEIndex: Found["+sMIME+"] & index " + i);;
				return i;
			}
		}

		Utils.Dbg("GetMIMEIndex: MIME type " + sMIME + " not found");;

		// Type not found, should we add it?
		if (bAdd)
		{
			Utils.Dbg("GetMIMEIndex: Adding new MIME type to list!");;

			// Get current list size
			int size = MIME_type.length;

			// Allocate new double array
			byte[][] newList = new byte[size+1][];

			// Copy all existing MIME types (byte[]'s)
			for (int i=0; i<size-2; i++)
			{
				newList[i] = MIME_type[i];
			}

			try
			{
				// Add new one to the end
				newList[size-1] = sMIME.getBytes("UTF-8");
			}
			catch (Exception e)
			{
				Utils.Dbg("GetMIMEType: UTF-8 not supported!");;
			}

			// Override ref to old list with new one
			MIME_type = newList;

			// Return index
			return size -1;
		}

		return -1;
	}


	//--------------------------------------------------------------------------------------------------------------------
	/// Get the number of data in current data file.
	///!return the number of data
	//--------------------------------------------------------------------------------------------------------------------
	public static int GetNBData()
	{
		return s_pack_nbData;
	}


	///!}


	//lzma
	static byte[] m_Buffer;
	static int m_inSize;
	static long	m_Range;
	static long	m_Code;
	static int m_ExtraBytes;
	static byte[] m_outStream;
	static int inputIndex;

	private	static short[] m_lzmaInternalData;


	static final int kNumBitModelTotalBits = 11;
	static final int kBitModelTotal	= (1 <<	kNumBitModelTotalBits);
	static final int tkNumMoveBits = 5;

	static final int  LZMA_BASE_SIZE = 1846;
	static final int  LZMA_LIT_SIZE	= 768;

	static final int  LZMA_RESULT_OK = 0;
	static final int  LZMA_RESULT_DATA_ERROR = 1;
	static final int  LZMA_RESULT_NOT_ENOUGH_MEM = 2;

	static final int kNumStates	= 12;
	static final int kNumPosBitsMax	= 4;
	static final int kNumLenToPosStates	= 4;
	static final int kNumPosSlotBits = 6;
	static final int kStartPosModelIndex = 4;
	static final int kEndPosModelIndex = 14;
	static final int kNumFullDistances = (1	<< (kEndPosModelIndex >> 1));
	static final int kNumAlignBits = 4;
	static final int kAlignTableSize = (1 << kNumAlignBits);
	static final int IsMatch = 0;
	static final int IsRep = IsMatch + (kNumStates << kNumPosBitsMax);
	static final int IsRepG0 = IsRep + kNumStates;
	static final int IsRepG1 = IsRepG0 + kNumStates;
	static final int IsRepG2 = IsRepG1 + kNumStates;
	static final int IsRep0Long	= IsRepG2 +	kNumStates;
	static final int PosSlot = IsRep0Long +	(kNumStates	<< kNumPosBitsMax);
	static final int SpecPos = PosSlot + (kNumLenToPosStates <<	kNumPosSlotBits);
	static final int Align = SpecPos + kNumFullDistances - kEndPosModelIndex;
	static final int LenCoder =	Align +	kAlignTableSize	;
	static final int kNumPosStatesMax =	(1 << kNumPosBitsMax);
	static final int kLenNumLowBits	= 3;
	static final int kLenNumLowSymbols = (1	<< kLenNumLowBits);
	static final int kLenNumMidBits	= 3;
	static final int kLenNumMidSymbols = (1	<< kLenNumMidBits);
	static final int kLenNumHighBits = 8;
	static final int kLenNumHighSymbols	= (1 <<	kLenNumHighBits);
	static final int kMatchMinLen =	2;
	static final int LenChoice = 0;
	static final int LenChoice2	= LenChoice	+ 1;
	static final int LenLow	= LenChoice2 + 1;
	static final int LenMid	= LenLow + (kNumPosStatesMax <<	kLenNumLowBits);
	static final int LenHigh = LenMid +	(kNumPosStatesMax << kLenNumMidBits);
	static final int kNumLenProbs =	LenHigh	+ kLenNumHighSymbols;
	static final int RepLenCoder = LenCoder	+ kNumLenProbs;
	static final int Literal = RepLenCoder + kNumLenProbs;
	static final int kNumMoveBits =	5;
	static final int kNumTopBits = 24;
	static final int kTopValue = (1	<< kNumTopBits);


	private	static int LZMA_RangeDecoderReadByte()
	{
		if (inputIndex == m_inSize )
		{
			m_ExtraBytes = 1;

			return 0xFF;
		}

		return m_Buffer[inputIndex++]&0xff;
	}

	private static void	LZMA_RangeDecoderInit( byte[] stream, int bufferSize	)
	{
		m_Buffer 		= stream;
		m_inSize 		= bufferSize;
		inputIndex 		= 0;
		m_ExtraBytes	= 0;
		m_Code 			= 0;
		m_Range			= 4294967295L;//(0xFFFFFFFF);
		
		for(int i =	0; i < 5; i++)
		{
			m_Code = (m_Code <<	8) | LZMA_RangeDecoderReadByte();
		}
	}

	private static int LZMA_RangeDecoderBitDecode( int prob )
	{
		long bound = (m_Range >> kNumBitModelTotalBits)	* m_lzmaInternalData[prob];
		if (m_Code < bound)
		{
			m_Range	= bound;
			m_lzmaInternalData[prob] +=	(kBitModelTotal	- m_lzmaInternalData[prob])	>> kNumMoveBits;
			
			if (m_Range	< kTopValue)
			{
				m_Code = (m_Code <<	8) | LZMA_RangeDecoderReadByte();
				m_Range	<<=	8;
			}
			
			return 0;
		}
		else
		{
			m_Range	-= bound;
			m_Code -= bound;
			m_lzmaInternalData[prob] -=	(m_lzmaInternalData[prob]) >> kNumMoveBits;
			if (m_Range	< kTopValue)
			{
				m_Code = (m_Code <<	8) | LZMA_RangeDecoderReadByte();
				m_Range	<<=	8;
			}
			
			return 1;
		}
	}

	private static int LZMA_LiteralDecodeMatch( int prob, byte matchByte)
	{
		int	symbol = 1;

		do
		{
			int	bit;
			int	matchBit	= (matchByte >> 7)	& 1;
			matchByte 		<<= 1;
			bit				= LZMA_RangeDecoderBitDecode(prob + ((1 + matchBit) << 8) + symbol);
			symbol 			= (symbol << 1) | bit;
			
			if (matchBit !=	bit)
			{
				while (symbol <	0x100)
				{
					symbol = (symbol <<	1) | LZMA_RangeDecoderBitDecode(prob+symbol);//(symbol +	symbol)	| LZMA_RangeDecoderBitDecode(prob+symbol);
				}
				break;
			}
		}
		while (symbol <	0x100);

		return symbol;
	}

	private static int LZMA_LiteralDecode(int probs)
	{
		int	symbol = 1;

		do
		{
			symbol = (symbol <<	1) | LZMA_RangeDecoderBitDecode(probs + symbol);//(symbol + symbol) | LZMA_RangeDecoderBitDecode(probs + symbol);
		}
		while (symbol <	0x100);

		return symbol;
	}

	private static int LZMA_RangeDecoderBitTreeDecode(int probs, int numLevels)
	{
		int	mi = 1;

		for(int i =	numLevels; i > 0; i--)
		{
			mi = (mi <<	1) + LZMA_RangeDecoderBitDecode(probs + mi);//(mi + mi) + LZMA_RangeDecoderBitDecode(probs + mi);
		}

		return mi -	(1 << numLevels);
	}

	private static int LZMA_LenDecode(int p,	int	posState)
	{
		if(LZMA_RangeDecoderBitDecode(p + LenChoice) == 0)
		{
			return LZMA_RangeDecoderBitTreeDecode(p + LenLow + (posState << kLenNumLowBits), kLenNumLowBits);
		}
			
		if(LZMA_RangeDecoderBitDecode(p + LenChoice2) == 0)
		{
			return kLenNumLowSymbols + LZMA_RangeDecoderBitTreeDecode(p + LenMid + (posState << kLenNumMidBits), kLenNumMidBits);
		}
		
		return kLenNumLowSymbols + kLenNumMidSymbols + LZMA_RangeDecoderBitTreeDecode(p	+ LenHigh, kLenNumHighBits);
	}

	private static int LZMA_RangeDecoderReverseBitTreeDecode(int probs, int numLevels)
	{
		int	mi 		= 1;
		int	symbol 	= 0;
		
		int bit;
		
		for(int i =	0; i < numLevels; i++)
		{
			bit		= LZMA_RangeDecoderBitDecode(probs + mi);
			mi 		= (mi << 1) + bit;
			symbol 	|= (bit << i);
		}
		
		return symbol;
	}

	private static int LZMA_RangeDecoderDecodeDirectBits(int	numTotalBits)
	{
		long range 	= m_Range;
		long code 	= m_Code;
		int	result 	= 0;

		for(int i = numTotalBits; i > 0; i--)
		{
			range >>= 1;

			result <<= 1;
			
			if (code >=	range)
			{
				code -=	range;
				result |= 1;
			}
			
			if (range <	kTopValue)
			{
				range <<= 8;
				code = (code <<	8) | LZMA_RangeDecoderReadByte();
			}
		}
		
		m_Range	= range; m_Code	= code;
		
		return result;
	}

	private static void	LZMA_Decode(	int	bufferSize,
										int	lc,	
										int	lp,	
										int	pb,
										byte[] inStream,
										int	outSize)
	{
		int		inSize 			= inStream.length;
		int		numProbs 		= Literal + (LZMA_LIT_SIZE	<< (lc + lp));
		short[]	buffer 			= m_lzmaInternalData	;
		int		state 			= 0;
		boolean	previousIsMatch	= false;
		int		previousByte 	= 0;
		int		rep0 			= 1;
		int		rep1 			= 1;
		int		rep2 			= 1;
		int		rep3 			= 1;
		int		nowPos 			= 0;
		int		posStateMask 	= (1 << pb) - 1;
		int		literalPosMask 	= (1 << lp) - 1;
		int		len				= 0;
		
		if (bufferSize < (numProbs << 1))
		{
			return ;//LZMA_RESULT_NOT_ENOUGH_MEM;
		}
			
		for(int i = 0; i < numProbs; i++)
		{
			buffer[i] =	1024;//(byte)((kBitModelTotal >> 1)&0xff);
		}
			
		LZMA_RangeDecoderInit( inStream,	inSize );

		while(nowPos < outSize)
		{
			int	posState = nowPos&posStateMask;

			if (LZMA_RangeDecoderBitDecode( IsMatch + (state	<< kNumPosBitsMax) + posState )	== 0)
			{
				int	probs =	Literal	+ (LZMA_LIT_SIZE *
									   ((( nowPos &	literalPosMask)	<< lc) + ((previousByte	& 0xFF)>> (8 - lc))));

				if(state <	4) 
					state = 0;
				else if(state < 10) 
					state -= 3;
				else 
					state -= 6;
					
					
				if (previousIsMatch)
				{
					byte matchByte;
					matchByte 		= m_outStream[nowPos - rep0];
					previousByte 	= LZMA_LiteralDecodeMatch(probs, matchByte)	& 0xFF;
					previousIsMatch	= false;
				}
				else
				{
					previousByte 	= LZMA_LiteralDecode(probs)	& 0xFF;
				}
				
				m_outStream[nowPos++] =	(byte)previousByte;
			}
			else
			{
				previousIsMatch	= true;
				
				if (LZMA_RangeDecoderBitDecode( IsRep + state ) == 1)
				{
					if (LZMA_RangeDecoderBitDecode( IsRepG0 + state ) ==	0)
					{
						if (LZMA_RangeDecoderBitDecode(IsRep0Long + (state << kNumPosBitsMax) + posState) ==	0)
						{
							state 					= state < 7 ? 9 : 11;
							previousByte 			= m_outStream[nowPos -	rep0] &	0xFF;
							m_outStream[nowPos++] 	= (byte)previousByte;
							continue;
						}
					}
					else
					{
						int	distance;
						
						if(LZMA_RangeDecoderBitDecode(IsRepG1 + state) == 0)
						{
							distance = rep1;
						}
						else
						{
							if(LZMA_RangeDecoderBitDecode(IsRepG2 + state) == 0)
							{
								distance 	= rep2;
							}
							else
							{
								distance 	= rep3;
								rep3 		= rep2;
							}
							
							rep2 = rep1;
						}
						
						rep1 = rep0;
						rep0 = distance;
					}
					
					len		= LZMA_LenDecode(RepLenCoder, posState);
					state 	= state < 7 ? 8 : 11;
				}
				else
				{
					int	posSlot;
					
					rep3 	= rep2;
					rep2 	= rep1;
					rep1 	= rep0;
					state 	= state < 7 ? 7 : 10;
					len		= LZMA_LenDecode(LenCoder, posState);
					
					posSlot	= LZMA_RangeDecoderBitTreeDecode(PosSlot + ((len < kNumLenToPosStates ? len : kNumLenToPosStates - 1) << kNumPosSlotBits), kNumPosSlotBits);
					
					if (posSlot	>= kStartPosModelIndex)
					{
						int	numDirectBits =	((posSlot >> 1)	- 1);
						
						rep0 = ((2 | ((int)posSlot & 1)) <<	numDirectBits);
						
						if (posSlot	< kEndPosModelIndex)
						{
							rep0 +=	LZMA_RangeDecoderReverseBitTreeDecode(SpecPos + rep0 - posSlot - 1, numDirectBits);
						}
						else
						{
							rep0 +=	LZMA_RangeDecoderDecodeDirectBits(numDirectBits - kNumAlignBits) << kNumAlignBits;
							rep0 +=	LZMA_RangeDecoderReverseBitTreeDecode(Align,	kNumAlignBits );
						}
					}
					else
					{
						rep0 = posSlot;
					}
					
					rep0++;
				}
				
				len	+= kMatchMinLen;
				
				do
				{
					previousByte 			= m_outStream[nowPos -	rep0] &	0xFF;
					m_outStream[nowPos++] 	= (byte)previousByte;
					len--;
				}
				while(len >	0 && nowPos	< outSize);
			}
		}

		return ;
	}


	private static void LZMA_Inflate(InputStream is, int size) throws Exception
	{
		if(DevConfig.pack_supportLZMADecompression)
		{
			byte[] header, data;
			
			header 	= new byte[13];
			data 	= new byte[size - 13];
			
			Stream_ReadFully(is, header, 0, 13);
			Stream_ReadFully(is, data,   0, size - 13);
			
			int[] properties = new int[5];
		
			for(int i = 0; i < 5; i++)
			{
				properties[i] =	(header[i] & 0xff);
			}
		
			int	outSize	= 0;
			int	b;
			
			for	(int i = 0; i < 4; i++)
			{
				outSize	+= ((header[i + 5]&0xff)) << (i << 3);
			}
			
			int	prop0 =	(int)properties[0];
		
			int	lc;
			int lp;
			int pb;
		
			pb 		= prop0 / 45;
			prop0 	%= 45;
			lp 		= prop0 / 9;
			lc 		= prop0 % 9;
		
			int	lzmaInternalSize = (LZMA_BASE_SIZE + (LZMA_LIT_SIZE	<< (lc + lp)));
		
			m_outStream			= new byte[outSize];
			m_lzmaInternalData 	= new short[lzmaInternalSize];
		
			LZMA_Decode(lzmaInternalSize * 2, lc, lp, pb, data, outSize);
			
			m_lzmaInternalData 	= null;
			m_Buffer 			= null;
			properties 			= null;
			header 				= null;
			data 				= null;
			GLLib.Gc();
		}
	}


	public static void LZMA_Inflate(byte[] compressDat) throws Exception
	{
		if(DevConfig.pack_supportLZMADecompression)
		{
			byte[] header, data;
			
			header 	= new byte[13];
			data 	= new byte[compressDat.length - 13];
			
			System.arraycopy(compressDat, 0,	header,	0, 13);
			System.arraycopy(compressDat, 13, 	data, 	0, data.length);
		
			int[] properties = new int[5];
		
			for(int i = 0; i < 5; i++)
			{
				properties[i] =	(header[i] & 0xff);
			}
		
			int	outSize	= 0;
			int	b;
			
			for	(int i = 0; i < 4; i++)
			{
				outSize	+= ((header[i + 5]&0xff)) << (i << 3);
			}
		
			int	prop0 =	(int)properties[0];
		
			int	lc;
			int lp;
			int pb;
		
			pb 		= prop0 / 45;
			prop0 	%= 45;
			lp 		= prop0 / 9;
			lc 		= prop0 % 9;
		
			int	lzmaInternalSize = (LZMA_BASE_SIZE + (LZMA_LIT_SIZE	<< (lc + lp)));
		
			m_outStream			= new byte[outSize];
			m_lzmaInternalData 	= new short[lzmaInternalSize];
		
			LZMA_Decode(lzmaInternalSize * 2, lc, lp, pb, data, outSize);
			
			m_lzmaInternalData 	= null;
			m_Buffer 			= null;
			properties 			= null;
			header 				= null;
			data 				= null;
			GLLib.Gc();
		}
	}


	//-----------------------------------------------------------------------------
	/// Aux function for inserting only 1 string
	///
	///!param pattern is the string we want to insert into
	///!param arg     is the string we want to insert
	//-----------------------------------------------------------------------------
	final public static String StringFormat (String pattern, String arg)
	{
	    return StringFormat(pattern, new String[] {arg});
	}

	//-----------------------------------------------------------------------------
	/// Given a string and a list of arguments it will insert the arguments
	///
	/// Note: String must contain insertion chars, %0, %1, etc.
	///
	///!param pattern is the string we want to insert the args into
	///!param args    is the list of strings to insert
	//-----------------------------------------------------------------------------
	public static String StringFormat(String pattern, String[] args)
	{
	    int index;
	    int startIndex;
	    int argIndex;
	    String result = "";

	    //fast check
	    index = pattern.indexOf('%');
	    if (index < 0)
	    {
	        return pattern;
	    }

	    startIndex = 0;
	    index = 0;
	    do
	    {
	        index = pattern.indexOf('%', index);
	        if (index < 0 || index == pattern.length() - 1)
	        {
	            result += pattern.substring(startIndex);
	            index = -1;
	        }
	        else
	        {
	            if (pattern.charAt(index + 1) == 's')
	            {
					argIndex = -1;

					if (index + 2 < pattern.length())
					{
						argIndex = pattern.charAt(index + 2) - '0';
					}

					if (argIndex >= 0 && argIndex <= 9)
					{
						result += pattern.substring(startIndex, index);
						result += args[argIndex];
						startIndex = index + 3;
						index = startIndex;
					}
					else
					{
						Utils.Dbg("Invalid string format pattern '" + pattern + "'");;
						index ++;
					}
				}
				else
				{
					index++;
				}
	        }
	    }
	    while (index >= 0);

	    return result;
	}

	
	/// \defgroup GLLib_text GLLib : Text
	/// text decoding and caching function
	/// \ingroup GLLibMain
	///!{
	// basic function for text handling ie load a text package and get a string


	/// number of string in this text pack
	static int text_nbString;

	/// encoding for text (UTF-8 by default)
	static String text_encoding = "UTF-8";

	/// array to store the text
	static private byte [] text_array;

	/// offset of the string in the text array
	static private int [] text_arrayOffset;

	/// array to cache String if
	static private String [] text_stringCacheArray;


	//*** Vars to use when using multiple text packs in arrays
	//*** Used when zJYLibConfig.text_useMultipleTextPacks is true

	//array to store map between textPack ID and text_array index
	static private int[] text_multiple_array_map;

	//arrays to store the multiple text packs in
	static private byte[][] text_multiple_arrays;

	//number of strings in this pack array
	static int[] text_multiple_nbStrings;

	// offset of the string in the text arrays
	static private int[][] text_multiple_arrayOffsets;

	// array to cache String if using casched strings
	static private String[][] text_multiple_stringCacheArrays;


	static private String StrEN = "EN";
	static private String StrDE = "DE";
	static private String StrFR = "FR";
	static private String StrIT = "IT";
	static private String StrES = "ES";
	static private String StrBR = "BR";
	static private String StrPT = "PT";
	static private String StrJP = "JP";
	static private String StrCN = "CN";
	static private String StrKR = "KR";
	static private String StrRU = "RU";
	static private String StrTR = "TR";
	static private String StrPL = "PL";
	static private String StrCZ = "CZ";
	static private String StrNL = "NL";


	//--------------------------------------------------------------------------
	/// get phone default langage, if unable to get it, return GLLang.EN (considered as default language)
	///!return phone default language as defined in GLLang interface
	///!see GLLang
	// refer to ISO-639-1 for language codes 	-.   http://en.wikipedia.org/wiki/List_of_ISO_639-1_codes
	// refer to ISO-3166-1 for country codes    -.   http://en.wikipedia.org/wiki/ISO_3166-1
	//--------------------------------------------------------------------------
	int Text_GetPhoneDefaultLangage ()
	{
		try
		{
			String lang = System.getProperty("microedition.locale");
			if (lang == null)
				return GLLang.EN;

			lang = lang.toUpperCase();

			if (lang.indexOf(StrEN) >= 0)	return GLLang.EN;
			if (lang.indexOf(StrDE) >= 0)	return GLLang.DE;
			if (lang.indexOf(StrFR) >= 0)	return GLLang.FR;
			if (lang.indexOf(StrIT) >= 0)	return GLLang.IT;
			if (lang.indexOf(StrES) >= 0)	return GLLang.ES;

			if (lang.indexOf(StrBR) >= 0)	return GLLang.BR;// must test brasil (by country code) before portugues since they use the same language
			if (lang.indexOf(StrPT) >= 0)	return GLLang.PT;// must test brasil before portugues since they use the same language

			if (lang.indexOf("JA") >= 0)	return GLLang.JP;// test japan by language code
			if (lang.indexOf(StrJP) >= 0)	return GLLang.JP;// test japan by country code

			if (lang.indexOf("ZH") >= 0)	return GLLang.CN;// test china by language code
			if (lang.indexOf(StrCN) >= 0)	return GLLang.CN;// test china by country code

			if (lang.indexOf("KO") >= 0)	return GLLang.KR;// korea by language code
			if (lang.indexOf("KP") >= 0)	return GLLang.KR;// korea by country code (Korea, Democratic People's Republic of)
			if (lang.indexOf(StrKR) >= 0)	return GLLang.KR;// korea by country code (Korea, Republic of)

			if (lang.indexOf(StrRU) >= 0)	return GLLang.RU;
			if (lang.indexOf(StrPL) >= 0)	return GLLang.PL;
			if (lang.indexOf(StrTR) >= 0)	return GLLang.TR;
			if (lang.indexOf(StrCZ) >= 0)	return GLLang.CZ;
			if (lang.indexOf(StrNL) >= 0)	return GLLang.NL;

		}
		catch(Exception e)
		{
		}
		return GLLang.EN;
	}

	//--------------------------------------------------------------------------
	/// get language code as a string
	///!param languageCode language code to get as a string
	///!return language code as string, or null if invalid language code
	//--------------------------------------------------------------------------
	String Text_GetLanguageAsString(int languageCode)
	{
		switch(languageCode)
		{
		case GLLang.EN:		return StrEN;
		case GLLang.DE:		return StrDE;
		case GLLang.FR:		return StrFR;
		case GLLang.IT:		return StrIT;
		case GLLang.ES:		return StrES;
		case GLLang.BR:		return StrBR;
		case GLLang.PT:		return StrPT;
		case GLLang.JP:		return StrJP;
		case GLLang.CN:		return StrCN;
		case GLLang.KR:		return StrKR;
		case GLLang.RU:		return StrRU;
		case GLLang.PL:		return StrPL;
		case GLLang.TR:		return StrTR;
		case GLLang.CZ:		return StrCZ;
		case GLLang.NL:     return StrNL;
		}
		return null;
	}

	/// \defgroup GLLib_memoryHelper GLLib : Memory
	/// memory access function (byte array manipulation)
	/// \ingroup GLLibMain
	///!{


	//--------------------------------------------------------------------------------------------------------------------
	/// set a byte value in a byte array at a given offset
	///!param dst - byte array to modify
	///!param dst_off - offset in the byte array to modify
	///!param src - byte value to set in the byte array
	///!return new offset value
	//--------------------------------------------------------------------------------------------------------------------
	public static int Mem_SetByte(byte[] dst, int dst_off, byte src)
	{
		dst[dst_off++] = (byte)src;
		return dst_off;
	}

	//--------------------------------------------------------------------------------------------------------------------
	/// set a short value in a byte array at a given offset
	///!param dst - byte array to modify
	///!param dst_off - offset in the byte array to modify
	///!param src - short value to set in the byte array
	///!return new offset value
	//--------------------------------------------------------------------------------------------------------------------
	public static int Mem_SetShort(byte[] dst, int dst_off, short src)
	{
		dst[dst_off++] = (byte)((src       ) & 0xFF);
		dst[dst_off++] = (byte)((src >>>  8) & 0xFF);
		return dst_off;
	}

	//--------------------------------------------------------------------------------------------------------------------
	/// set a int value in a byte array at a given offset
	///!param dst - byte array to modify
	///!param dst_off - offset in the byte array to modify
	///!param src - int value to set in the byte array
	///!return new offset value
	//--------------------------------------------------------------------------------------------------------------------
	public static int Mem_SetInt(byte[] dst, int dst_off, int src)
	{
		dst[dst_off++] = (byte)((src       ) & 0xFF);
		dst[dst_off++] = (byte)((src >>>  8) & 0xFF);
		dst[dst_off++] = (byte)((src >>> 16) & 0xFF);
		dst[dst_off++] = (byte)((src >>> 24) & 0xFF);
		return dst_off;
	}

	//--------------------------------------------------------------------------------------------------------------------
	/// set a long value in a byte array at a given offset
	///!param dst - byte array to modify
	///!param dst_off - offset in the byte array to modify
	///!param src - long value to set in the byte array
	///!return new offset value
	//--------------------------------------------------------------------------------------------------------------------
	public static int Mem_SetLong(byte[] dst, int dst_off, long src)
	{
		dst[dst_off++] = (byte)((src       ) & 0xFF);
		dst[dst_off++] = (byte)((src >>>  8) & 0xFF);
		dst[dst_off++] = (byte)((src >>> 16) & 0xFF);
		dst[dst_off++] = (byte)((src >>> 24) & 0xFF);
		dst[dst_off++] = (byte)((src >>> 32) & 0xFF);
		dst[dst_off++] = (byte)((src >>> 40) & 0xFF);
		dst[dst_off++] = (byte)((src >>> 48) & 0xFF);
		dst[dst_off++] = (byte)((src >>> 56) & 0xFF);
		return dst_off;
	}

	//--------------------------------------------------------------------------------------------------------------------
	/// get a byte value from the specified array at specified offset
	///!param src - byte array to read value from
	///!param src_off - offset in the byte array
	///!return byte value
	//--------------------------------------------------------------------------------------------------------------------
	public static byte Mem_GetByte(byte[] src, int src_off)
	{
		return src[src_off];
	}

	//--------------------------------------------------------------------------------------------------------------------
	/// get a short value from the specified array at specified offset
	///!param src - byte array to read value from
	///!param src_off - offset in the byte array
	///!return short value
	//--------------------------------------------------------------------------------------------------------------------
	public static short Mem_GetShort(byte[] src, int src_off) 				// 2 bytes
	{
		return (short)((src[src_off++] & 0xFF) |
					  ((src[src_off++] & 0xFF) << 8));
	}

	//--------------------------------------------------------------------------------------------------------------------
	/// get an int value from the specified array at specified offset
	///!param src - byte array to read value from
	///!param src_off - offset in the byte array
	///!return int value
	//--------------------------------------------------------------------------------------------------------------------
	public static int Mem_GetInt(byte[] src, int src_off) 					// 4 bytes
	{
		return (((src[src_off++] & 0xFF)     )  |
				((src[src_off++] & 0xFF) <<  8) |
				((src[src_off++] & 0xFF) << 16) |
				((src[src_off++] & 0xFF) << 24));
	}

	//--------------------------------------------------------------------------------------------------------------------
	/// get a long value from the specified array at specified offset
	///!param src - byte array to read value from
	///!param src_off - offset in the byte array
	///!return long value
	//--------------------------------------------------------------------------------------------------------------------
	static long Mem_GetLong(byte[] src, int src_off) 				// 8 bytes
	{
		return (((long)(src[src_off++] & 0xFF)     )  |
				((long)(src[src_off++] & 0xFF) <<  8) |
				((long)(src[src_off++] & 0xFF) << 16) |
				((long)(src[src_off++] & 0xFF) << 24) |
				((long)(src[src_off++] & 0xFF) << 32) |
				((long)(src[src_off++] & 0xFF) << 40) |
				((long)(src[src_off++] & 0xFF) << 48) |
				((long)(src[src_off++] & 0xFF) << 56));
	}

	//--------------------------------------------------------------------------------------------------------------------
	/// Copies an array from the specified source array, beginning at the specified position, to the specified position of the destination array.
	///!param src - the source array.
	///!param src_position - start position in the source array.
	///!param dst - the destination array.
	///!param dst_position - pos start position in the destination data.
	///!param length - the number of array elements to be copied.
	//--------------------------------------------------------------------------------------------------------------------
	static void Mem_ArrayCopy(Object src, int src_position, Object dst, int dst_position, int length)
		throws Exception
	{
		System.arraycopy(src, src_position, dst, dst_position, length);
	}

	//--------------------------------------------------------------------------------------------------------------------
	/// copies whole content of src array in dst array at specified offset
	///!param dst - byte array to modify
	///!param dst_off - offset in the destination byte array array
	///!param src - source byte array
	///!return new offset value
	//--------------------------------------------------------------------------------------------------------------------
	static int Mem_SetArray(byte[] dst, int dst_off, byte[] src)
		throws Exception
	{
		Mem_ArrayCopy(src, 0, dst, dst_off, src.length);
		return dst_off + src.length;
	}

	//--------------------------------------------------------------------------------------------------------------------
	/// fill destination array with content of src array at specified offset
	///!param src - byte array to copy values from
	///!param src_off - offset in the source array to find values at
	///!param dst - destination byte array
	///!return new src offset value
	//--------------------------------------------------------------------------------------------------------------------
	static int Mem_GetArray(byte[] src, int src_off, byte[] dst)	// dst.length bytes
		throws Exception
	{
		Mem_ArrayCopy(src, src_off, dst, 0, dst.length);
		return src_off + dst.length;
	}


	//--------------------------------------------------------------------------------------------------------------------
	/// Read an array/multiarray, from an inputStream
	///!param is inputstream to read array from
	///!return array or multiarray as an Object.
	///!throw Exception if error occured.
	//--------------------------------------------------------------------------------------------------------------------
	static Object Mem_ReadArray(InputStream is)
	{
		if(!(is != null))Utils.DBG_PrintStackTrace(false, "Mem_ReadArray.is is null");;

		Object array = null;
		try
		{
			int ID = Stream_Read(is);
			int nbComponent;
			int dataPadding = ID >> 4;

			int type = ID & 0x7;

			if ((ID & 0x8) != 0)
			{
				nbComponent = Stream_Read16(is);
			}
			else
			{
				nbComponent = Stream_Read(is);
			}

			switch (type)
			{
				case cPack.ARRAY_BYTE:
				{
					byte[] array2 = new byte[nbComponent];
					for (int i=0; i<nbComponent; i++)
					{
						array2[i] = (byte) Stream_Read(is);
					}
					array = (Object) array2;
					break;
				}
				case cPack.ARRAY_SHORT:
				{
					short[] array2 = new short[nbComponent];
					if (dataPadding == cPack.ARRAY_BYTE)
						for (int i=0; i<nbComponent; i++)
						{
							array2[i] = (byte) Stream_Read(is);
						}
					else
						for (int i=0; i<nbComponent; i++)
						{
							array2[i] = (short) Stream_Read16(is);
						}
					array = (Object) array2;
					break;
				}
				case cPack.ARRAY_INT:
				{
					int[] array2 = new int[nbComponent];
					if (dataPadding == cPack.ARRAY_BYTE)
						for (int i=0; i<nbComponent; i++)
						{
							array2[i] = (byte)Stream_Read(is);
						}
					else if (dataPadding == cPack.ARRAY_SHORT)
						for (int i=0; i<nbComponent; i++)
						{
							array2[i] = (short)Stream_Read16(is);
						}
					else
						for (int i=0; i<nbComponent; i++)
						{
							array2[i] = Stream_Read32(is);
						}
					array = (Object) array2;
					break;
				}

				default:
				{
					Object[] genArray;
					byte[][] arrayB;
					short[][] arrayS;
					int[][] arrayI;
					byte[][][] arrayB2;
					short[][][] arrayS2;
					int[][][] arrayI2;

					type &= 0x3;
					switch(type)
					{
					case cPack.ARRAY_BYTE:
						{
							if (dataPadding == 2)
							{
								arrayB = new byte[nbComponent][];
								genArray = (Object[]) arrayB;
							}
							else
							{
								arrayB2 = new byte[nbComponent][][];
								genArray = (Object[]) arrayB2;
							}
						}
					break;
					case cPack.ARRAY_SHORT:
						{
							if (dataPadding == 2)
							{
								arrayS = new short[nbComponent][];
								genArray = (Object[]) arrayS;
							}
							else
							{
								arrayS2 = new short[nbComponent][][];
								genArray = (Object[]) arrayS2;
							}
						}
					break;
					default:
						{
							if (dataPadding == 2)
							{
								arrayI = new int[nbComponent][];
								genArray = (Object[]) arrayI;
							}
							else
							{
								arrayI2 = new int[nbComponent][][];
								genArray = (Object[]) arrayI2;
							}
						}
					break;
					}

					for (int i=0; i<nbComponent; i++)
					{
						genArray[i] = Mem_ReadArray(is);
					}
					array = (Object) genArray;
					break;
				}
			}
		}
		catch (Exception e)
		{
			if(!(false))Utils.DBG_PrintStackTrace(false, "Mem_ReadArray.IO error");;
		}
		return array;
	}

	static private int Stream_readOffset = 0;
	static int Stream_Read (InputStream is) throws Exception
	{
		int read = is.read();
		if (read >= 0)	// we read something
		{
			Stream_readOffset++;
		}
		return read;
	}

	static int Stream_Read16 (InputStream is) throws Exception
	{
		return ((Stream_Read(is) & 0xFF) | ((Stream_Read(is) & 0xFF) << 8));
	}

	static int Stream_Read32 (InputStream is) throws Exception
	{
		return (((Stream_Read(is) & 0xFF) | ((Stream_Read(is) & 0xFF) << 8)) | (((Stream_Read(is) & 0xFF) << 16) | ((Stream_Read(is) & 0xFF) << 24)));
	}

	static int Stream_ReadFully( InputStream is, byte[] array, int offset, int length)
	{
		if(!(array != null))Utils.DBG_PrintStackTrace(false, "Stream_ReadFully.array is null");;
		if(!(offset >= 0))Utils.DBG_PrintStackTrace(false, "Stream_ReadFully.offset is negative");;
		if(!(length >= 0))Utils.DBG_PrintStackTrace(false, "Stream_ReadFully.length is negative");;
		if(!(offset+length <= array.length))Utils.DBG_PrintStackTrace(false, "Stream_ReadFully.offset+length is bigger than array size");;

		int off = offset;
		int len = length;
		int read = 0;
		try
		{
			while (len > 0)
			{
				read = is.read(array, off, len);
				if(!(read >= 0))Utils.DBG_PrintStackTrace(false, "Pack_ReadFully.EOF");;
				len -= read;
				off += read;
			}
		}
		catch (Exception e)
		{
			if(!(false))Utils.DBG_PrintStackTrace(false, "Stream_ReadFully.IO exception occured");;
		}

		Stream_readOffset += length;

		return length;
	}
	
}
