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
 * @author ZZQ û������ʱ����
 */
public class WeatherMain extends Activity {

	private Button okButton;
	private TextView tv;
	private EditText cityName;
	private ProgressDialog progressDialog;
	private static final int START = 0;// ����Բ�ν�����
	private static final int END = 1;// Բ�ν�������ʧ

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
		// ���ý���������ʽΪԲ��
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setTitle("��ʾ");
		progressDialog.setMessage("���ݼ����У����Ժ�....");
		// ���ý������Ƿ�Ϊ����ȷ
		progressDialog.setIndeterminate(false);
		// ���ý������Ƿ񰴷��ؼ�ȡ��
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

	// WebService�ĵ�ַ
	private static String URL = "http://webservice.webxml.com.cn/WebServices/WeatherWebService.asmx";

	private static final String METHOD_NAME = "getWeatherbyCityName";

	private static String SOAP_ACTION = "http://WebXml.com.cn/getWeatherbyCityName";

	private String weatherToday;

	private SoapObject detail;

	public void getWeather(String cityName) {
		try {
			// cityName = "�ɶ�";
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
		if (s.contains("��ѯ���Ϊ��")) {
			Toast.makeText(this, "��������ȷ\n�ĳ�����!", Toast.LENGTH_LONG).show();
		} else {
			String date = detail.getProperty(6).toString();
			weatherToday = "���죺" + date.split(" ")[0];
			weatherToday = weatherToday + "\n������" + date.split(" ")[1];
			weatherToday = weatherToday + "\n���£�"
					+ detail.getProperty(5).toString();
			weatherToday = weatherToday + "\n������"
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