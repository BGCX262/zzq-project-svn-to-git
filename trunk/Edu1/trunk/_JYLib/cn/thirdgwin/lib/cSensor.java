package cn.thirdgwin.lib;

import java.io.IOException;
//#if ENABLE_SENSOR=="TRUE"
import javax.microedition.io.Connector;
import javax.microedition.sensor.Data;
import javax.microedition.sensor.DataListener;
import javax.microedition.sensor.SensorConnection;
import javax.microedition.sensor.SensorInfo;
import javax.microedition.sensor.SensorManager;
import javax.microedition.sensor.SensorInfoImpl;
//#endif
//#if PLATFORM=="Android"
import android.util.Log;
//#endif
//#if ENABLE_SENSOR=="TRUE"
public class cSensor implements DataListener
//#else
public class cSensor
//#endif
{
	public static cSensor _self;
	public cSensor()
	{
//#if ENABLE_SENSOR=="TRUE"
		_self = this; 
		Init();
//#endif		
	}
//#if ENABLE_SENSOR=="TRUE"
	public static String SENSOR_ACCELERATION = "acceleration";
	/** 各方向上的重力加速度 */
	public static int x_int, y_int, z_int; 
	public static float x_float, y_float, z_float;
	/** 传感器连接 */
	private static SensorConnection sensorConnection = null;
	private static SensorInfo infos[];   
	/** 是否获得正确的传感器 */
	private static int ChannelIndex = -1;
	/** 求平均的传感器取值的数量 */
	private static final int BUFFER_SIZE = 3;   
	public void dataReceived(SensorConnection sensor, Data[] data, boolean isDataLost) {
	   int[] directions = getIntegerDirections(data);
	         x_int = directions[0];
	         y_int = directions[1];
	         z_int = directions[2];
	         Log.e("Sensor","X " +x_int+"Y " +x_int+"Y " +x_int);
	    }
	
	public void dataReceived(float[] fdata) {
		 		x_float = fdata[0];
		 		y_float = fdata[1];
		 		z_float = fdata[2];
//		 		Log.d("SensorLDW","X " +x_float+"Y " +y_float+"z " +z_float);
		    }
	  /****************************************************************
	   * 获得个方向上的重力加速度
	   * @param data
	   * @return int[]
	   */

	  private static int[] getIntegerDirections(Data[] data) {
		  int[] retV = new int[3];
		  try
		  {
		  int [][] intValues = new int[3][BUFFER_SIZE];
		  int [] curInts;
		  int sum;
		  int j;
		  for (int i=0; i<3; i++){
			  intValues[i] = curInts = data[i].getIntValues();
			  sum = 0;
			  j = curInts.length;
			  if (j>BUFFER_SIZE)j=BUFFER_SIZE;
			  j--;
			  while(j>=0)
			  {
				  sum+=curInts[j];
				  j--;
			  }
			  retV[i] = sum/BUFFER_SIZE;
		  }
		  }
		  catch (Exception e)
		  {
			  Log.e("Error","SHIT");
		  }
		  return retV;
	  }
    /** 传感器的初始化 */
//	private synchronized void Init() {
	private void Init() {
		_self = this;
		sensorConnection = openSensor(SENSOR_ACCELERATION);
		if (sensorConnection != null) {
			sensorConnection.setDataListener(this, BUFFER_SIZE);
		}
	}
//	public synchronized void Destroy()
	public void Destroy()
	{
		if (sensorConnection!=null)
			{
			sensorConnection.removeDataListener();
			sensorConnection = null;
			};
	}
	     /**********************************
	      * 打开传感器，获得需要的传感器
	      * @return INT 数据类型的传感器
	      */
	private static SensorConnection openSensor(String sensor_type) {
/*		
		infos = SensorManager.findSensors(sensor_type, null);
		if (infos.length==0) return null;
	         int datatypes[] = new int[infos.length];
	         int i = 0;
	         String sensor_url = "";
	            while (i<datatypes.length) {
	                datatypes[i] = infos[i].getChannelInfos()[0].getDataType();
	                if (datatypes[i] ==  ChannelInfo.TYPE_INT) { 
	                    sensor_url = infos[i].getUrl();
	                    ChannelIndex = i;
	                    break;
	                }
	                else 
	                	{
	                	i++;
	                	}
	            }
*/	            
	   try {
		   String sensor_url = SensorInfoImpl.SENSORINFO_JSR256_ACCELERATION;
		   return (SensorConnection)Connector.open(sensor_url);
	   }catch (IOException ioe) {
           ioe.printStackTrace();
           return null;
	   }

	     }

//#endif
}
