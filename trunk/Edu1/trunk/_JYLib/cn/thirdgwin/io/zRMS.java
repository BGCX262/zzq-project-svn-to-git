package cn.thirdgwin.io;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;

import cn.thirdgwin.lib.DevConfig;
import cn.thirdgwin.lib.Utils;


public class zRMS {
	/// Current recordstore handler. Null if none is opened
	private static RecordStore		s_rs;

	/// Used to store the binary data loaded from rms
	private static byte[]			s_rms_buffer;


	/// Stores the vendor name for shared rms access
	private static String			s_rms_vendor;

	/// Stores the midlet name for shared rms access
	private static String			s_rms_midletName;
	
	public static void Rms_cleanBuffer() {
		s_rms_buffer = null;
	}

	//--------------------------------------------------------------------------------------------------------------------
	///!brief Closing the current opened recordstore, if any
	//--------------------------------------------------------------------------------------------------------------------
	private static void Rms_Close ()
	{
		// it's already closed?
		if(s_rs == null)
		{
			return;
		}

		// closing recordstore...
		try
		{
			s_rs.closeRecordStore();
		}
	

		catch(RecordStoreException e)

		{
			Utils.Dbg("ERROR! Failed closing RMS: " + e);;
		}

		s_rs = null;
	}

	//--------------------------------------------------------------------------------------------------------------------
	///!brief Opens a recordstore file
	///!param strName Filename of the recordstore to open.
	//--------------------------------------------------------------------------------------------------------------------

	
		private static void Rms_Open (String strName) throws RecordStoreException

	{
		
		if(DevConfig.rms_useSharing && (s_rms_vendor != null) && (s_rms_midletName != null))
		{
			Utils.Dbg(" Open recordstore with name : " + strName + ", vendor : " + s_rms_vendor + ", suite : " + s_rms_midletName);;

			// open shared recordstore
			s_rs = RecordStore.openRecordStore(strName, s_rms_vendor, s_rms_midletName);
		}
		else

		{
			Utils.Dbg(" Open recordstore : " + strName);;

			// open own recordstore
			s_rs = RecordStore.openRecordStore(strName, true);
		}
	}

	//--------------------------------------------------------------------------------------------------------------------
	///!brief Reads data from the recordstore
	///!param strName Filename of recordstore to read from
	///!return A byte array, containing the recordstore data
	///
	///!see zJYLibConfig.rms_maxRecordSize
	//--------------------------------------------------------------------------------------------------------------------
	static byte[] Rms_Read (String strName)
    {
		if (DevConfig.rms_maxRecordSize > 0)
		{
			boolean bDone = false;
			int count = 0;
			int size  = 0;

			try
			{
				// Compute how large the TOTAL size is
				while (!bDone)
				{
					Rms_Open(strName + count);

					// get the specified record
					if(s_rs.getNumRecords() > 0)
					{
						if ((1 == 0))
						{
							if (s_rs.getRecordSize(1) > DevConfig.rms_maxRecordSize)
							{
								Utils.Dbg("Rms_Read: Using rms_maxRecordSize and while reading a records had size larger than this!");;
							}
						}

						size += s_rs.getRecordSize(1);
						count++;
					}
					else
					{
						bDone = true;
					}

					Rms_Close ();
				}
			}
			catch(Exception e)
			{
				Utils.Err("ERROR! Failed reading from RMS: " + e);
			}

			// If there is something to read lets do it
			if (size > 0)
			{
				// Allocate enough for all data
				byte[] data = new byte[size];
				int offset = 0;

				// Read and copy 1 by 1
				for( int i=0; i<count; i++)
				{
					byte[] dataRaw = Rms_Read(strName + i);
					System.arraycopy(dataRaw, 0, data, offset, dataRaw.length);
					offset += dataRaw.length;

					if ((1 == 0))
					{
						if (i==(count-1) && dataRaw.length != DevConfig.rms_maxRecordSize)
						{
							Utils.Dbg("Rms_Read: Using rms_maxRecordSize and while reading a none-last records had size other than this!");;
						}
					}
				}

				return data;
			}
			else
			{
				Utils.Dbg("ERROR! Failed reading from RMS!");;
				return null;
			}
		}
		else
		{
			return Rms_Read_Single(strName);
		}
    }

	//--------------------------------------------------------------------------------------------------------------------
	///!brief Reads data from the recordstore
	///!param strName Filename of recordstore to read from
	///!return A byte array, containing the recordstore data
	//--------------------------------------------------------------------------------------------------------------------
	private static byte[] Rms_Read_Single (String strName)
	{
		byte[] data = null;

		try
		{
			// open the recordstore
			Rms_Open(strName);

			// get the specified record
			if(s_rs.getNumRecords() > 0)
			{
				data = s_rs.getRecord(1);
			}
		}
	

		catch (Exception e)

		{
			Utils.Err("Failed reading from RMS: " + strName);
			data = null;
		}

		// close recordstore in any case
		Rms_Close();

		// return the data
		return data;
	}
	
	public static void Rms_Delete(String strName) {
		try
		{
			RecordStore.deleteRecordStore(strName);
		}
		catch(Exception e)
		{
			Utils.Dbg("ERROR! Failed delete RMS: " + e);;
		}
	}

	//--------------------------------------------------------------------------------------------------------------------
	///!brief Writes data to the recordstore
	///!param strName Filename of recordstore to write to
	///!param data Data array for saving into the recordstore
	///
	///!see zJYLibConfig.rms_maxRecordSize
	//--------------------------------------------------------------------------------------------------------------------
	public static void Rms_Write (String strName, byte[] data)
    {
        if (DevConfig.rms_maxRecordSize > 0)
		{
			int offset   = 0;
			int sizeLeft = data.length;
			int count    = 0;

			while (sizeLeft > 0)
			{
				Rms_Write_Single(strName + count, data, offset, ((DevConfig.rms_maxRecordSize)<(sizeLeft)?(DevConfig.rms_maxRecordSize):(sizeLeft)));
				offset   += DevConfig.rms_maxRecordSize;
				sizeLeft -= DevConfig.rms_maxRecordSize;
				count++;
			}
		}
		else
		{
			Rms_Write_Single(strName, data, 0, data.length);
		}
    }

	//--------------------------------------------------------------------------------------------------------------------
	///!brief Writes data to the recordstore
	///!param strName Filename of recordstore to write to
	///!param data Data array for saving into the recordstore
	//--------------------------------------------------------------------------------------------------------------------
	private static void Rms_Write_Single (String strName, byte[] data, int offset, int size)
	{
		try
		{
			// open the recordstore
			Rms_Open(strName);

			// save the specified record
			if(s_rs.getNumRecords() > 0)
			{
				s_rs.setRecord(1, data, offset, size);
			}
			else
			{
				s_rs.addRecord(data, offset, size);
			}
		}
	

		catch(RecordStoreException e)

		{
			Utils.Dbg("ERROR! Failed writing into RMS: " + e);;
		}

		// close recordstore in any case
		Rms_Close();
	}


	//--------------------------------------------------------------------------------------------------------------------
	///!brief Reads data from rms into buffer, and returns input stream on the buffer
	///!param strName Recordstore named string
	//--------------------------------------------------------------------------------------------------------------------
	public static InputStream GetRmsInputStream (String strName)
	{
		s_rms_buffer = null;

		try
		{
			// read data from recordstore into the buffer
			s_rms_buffer = Rms_Read(strName);
		}
		catch(Exception e)
		{
			Utils.Dbg("   Exception while reading from rms : " + strName);;
		}

		if(s_rms_buffer != null)
		{
			// constuct a stream on the buffer
			return new ByteArrayInputStream(s_rms_buffer);
		}

		return null;
	}

	//--------------------------------------------------------------------------------------------------------------------
	///!brief Sets vendor name and midlet names - used for shared rms read/write
	///!note Use only if zJYLibConfig.rms_useSharing == true
	///!param strVendor Value of the field MIDlet-Vendor in jad/manifest of the application which is the owner of the rms
	///!param strMidletName Valuer of the field MIDlet-Name in jad/manifest of the application which is the owner of the rms
	//--------------------------------------------------------------------------------------------------------------------
	static void InitSharedRms (String strVendor, String strMidletName)
	{
		s_rms_vendor = strVendor;
		s_rms_midletName = strMidletName;
	}

	//--------------------------------------------------------------------------------------------------------------------
	///!brief Write data to shared recordstore
	///!note Use only if zJYLibConfig.rms_useSharing == true
	///!param strName Rms name
	///!param strVendor Value of the field MIDlet-Vendor in jad/manifest of the application which is the owner of the rms
	///!param strMidletName Valuer of the field MIDlet-Name in jad/manifest of the application which is the owner of the rms
	///!param data Byte array data to write in the rms
	//--------------------------------------------------------------------------------------------------------------------
	static void Rms_WriteShared (String strName, String strVendor, String strMidletName, byte[] data)
	{
		// set names to identify rms
		InitSharedRms(strVendor, strMidletName);

		// write into the recordstore
		Rms_Write(strName, data);

		// reset names
		InitSharedRms(null, null);
	}

	//--------------------------------------------------------------------------------------------------------------------
	///!brief Read data from shared recordstore
	///!note Use only if zJYLibConfig.rms_useSharing == true
	///!param strName Rms name
	///!param strVendor Value of the field MIDlet-Vendor in jad/manifest of the application which is the owner of the rms
	///!param strMidletName Valuer of the field MIDlet-Name in jad/manifest of the application which is the owner of the rms
	//--------------------------------------------------------------------------------------------------------------------
	static byte[] Rms_ReadShared (String strName, String strVendor, String strMidletName)
	{
		// set names to identify rms
		InitSharedRms(strVendor, strMidletName);

		// read data from rms
		byte[] data = Rms_Read(strName);

		// reset names
		InitSharedRms(null, null);

		return data;
	}

}
