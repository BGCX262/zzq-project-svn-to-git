package com.test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

/**
 * @author ZZQ 没处理延时问题
 */
public class WeatherMain extends Activity {

	private Button okButton;
	private TextView tv;
	private EditText cityName;
	private ProgressDialog progressDialog;
	private static final int START = 0;// 出现圆形进度条
	private static final int END = 1;// 圆形进度条消失

	/** Called when the activity is first created. */
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		okButton = (Button) this.findViewById(R.id.btn_Search);
		tv = (TextView) findViewById(R.id.result);
		cityName = (EditText) findViewById(R.id.et);

		progressDialog = new ProgressDialog(WeatherMain.this);
		// 设置进度条的样式为圆形
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setTitle("提示");
		progressDialog.setMessage("数据加载中，请稍后....");
		// 设置进度条是否为不明确
		progressDialog.setIndeterminate(false);
		// 设置进度条是否按返回键取消
		progressDialog.setCancelable(true);

		okButton.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				String city = cityName.getText().toString();
				Message msg = new Message();
				msg.what = START;
				handler.sendMessage(msg);
				handler.post(runa);
				getWeather(city);
			}
		});
	}

	private static final String NAMESPACE = "http://WebXml.com.cn/";

	// WebService的地址
	private static String URL = "http://webservice.webxml.com.cn/WebServices/WeatherWebService.asmx";

	private static final String METHOD_NAME = "getWeatherbyCityName";

	private static String SOAP_ACTION = "http://WebXml.com.cn/getWeatherbyCityName";

	private String weatherToday;

	private SoapObject detail;

	public void getWeather(String cityName) {
		try {
			// cityName = "成都";
			SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);

			System.out.println("cityName is " + cityName);

			rpc.addProperty("theCityName", cityName);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.bodyOut = rpc;
			envelope.dotNet = true;
			envelope.setOutputSoapObject(rpc);
			HttpTransportSE ht = new HttpTransportSE(URL);
			ht.debug = true;
			ht.call(SOAP_ACTION, envelope);
			detail = (SoapObject) envelope.getResponse();
			System.out.println("detail" + detail);
			parseWeather(detail);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return;
	}

	Message msgww = new Message();

	private void parseWeather(SoapObject detail)
			throws UnsupportedEncodingException {
		String s = detail.getProperty(0).toString();
		msgww.what = END;
		System.out.println(s);
		if (s.contains("查询结果为空")) {
			Toast.makeText(this, "请输入正确\n的城市名!", Toast.LENGTH_LONG).show();
		} else {
			String date = detail.getProperty(6).toString();
			weatherToday = "今天：" + date.split(" ")[0];
			weatherToday = weatherToday + "\n天气：" + date.split(" ")[1];
			weatherToday = weatherToday + "\n气温："
					+ detail.getProperty(5).toString();
			weatherToday = weatherToday + "\n风力："
					+ detail.getProperty(7).toString() + "\n";
			System.out.println("weatherToday is " + weatherToday);
			tv.setText(weatherToday);
			Toast.makeText(this, weatherToday, Toast.LENGTH_LONG).show();
		}

	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			Log.i("we", "wearther");
			super.handleMessage(msg);
			if (msg.what == START) {
				progressDialog.show();
			} else if (msg.what == END) {
				progressDialog.dismiss();
			}

		}

	};
	Runnable runa = new Runnable() {

		public void run() {
			System.out.println("sffsd");
			if (msgww.what!= START) {
				handler.sendMessage(msgww);
			}
			handler.post(runa);
		}
	};

}