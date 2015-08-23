package com.zzq.adapter;

import java.util.List;

import com.zzq.R;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class DateAdapter extends BaseAdapter {

	private Context context;
	private List<String> lstDate;
	private TextView txtAge;
	private int holdPosition;
	private boolean isChanged = false;
	private boolean ShowItem = false;
    int[] image = {

    		R.drawable.img1,R.drawable.img2,R.drawable.img3,
    		R.drawable.img1,R.drawable.img5,R.drawable.img6,
    		R.drawable.img7,R.drawable.img8,R.drawable.img9,
    		R.drawable.img1,R.drawable.img2,R.drawable.img3,
    		R.drawable.img1,R.drawable.img5,R.drawable.img6,
    		R.drawable.img7,R.drawable.img8,R.drawable.img9,
    		R.drawable.img1,R.drawable.img2,R.drawable.img3,
	};
    String[] httpstr={
    		"http://61.153.219.244/paperpdf/zsrb/2011/02/28/001.pdf",
    		"http://61.153.219.244/paperpdf/zsrb/2011/02/28/001.pdf",
    		"http://61.153.219.244/paperpdf/zsrb/2011/02/28/001.pdf",
    		"http://61.153.219.244/paperpdf/zsrb/2011/02/28/001.pdf",
    		"http://61.153.219.244/paperpdf/zsrb/2011/02/28/001.pdf",
    		"http://61.153.219.244/paperpdf/zsrb/2011/02/28/001.pdf",
    		"http://61.153.219.244/paperpdf/zsrb/2011/02/28/001.pdf",
    		"http://61.153.219.244/paperpdf/zsrb/2011/02/28/001.pdf",
    		"http://61.153.219.244/paperpdf/zsrb/2011/02/28/001.pdf",
    };

	public DateAdapter(Context mContext, List<String> list) {
		this.context = mContext;
		lstDate = list;
	}

	@Override
	public int getCount() {
		return lstDate.size();
	}

	@Override
	public Object getItem(int position) {
		return lstDate.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void exchange(int startPosition, int endPosition) {
		System.out.println(startPosition + "--" + endPosition);
		holdPosition = endPosition;
		Object startObject = getItem(startPosition);
		System.out.println(startPosition + "========" + endPosition);
		Log.d("ON","startPostion ==== " + startPosition );
		Log.d("ON","endPosition ==== " + endPosition );
		if(startPosition < endPosition){
		    lstDate.add(endPosition + 1, (String) startObject);
		    lstDate.remove(startPosition);
		}else{
			lstDate.add(endPosition,(String)startObject);
			lstDate.remove(startPosition + 1);
		}
		isChanged = true;
		notifyDataSetChanged();
	}
	
	public void showDropItem(boolean showItem){
		this.ShowItem = showItem;		
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = LayoutInflater.from(context).inflate(R.layout.item, null);
		//txtAge = (TextView) convertView.findViewById(R.id.txt_userAge);
		//txtAge.setText("Item" + lstDate.get(position));
		ImageView iv = (ImageView) convertView.findViewById(R.id.imageView_ItemImage);
		iv.setImageResource(image[Integer.valueOf(lstDate.get(position))]);
		if (isChanged){
		    if (position == holdPosition){
		    	if(!ShowItem){
			        convertView.setVisibility(View.INVISIBLE);
		    	}
		    }
		}
		return convertView;
	}

}