package com.brownapps.sortr.utility;

import java.util.List;

import com.brownapps.sortr.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class SimpleStringAdapter extends ArrayAdapter<String> {

	public SimpleStringAdapter(Context context, int textViewResourceId,
			List<String> objects) {
		super(context, textViewResourceId, objects);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View view = convertView;
		
		if(view == null){
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.clickable_item, null);
		}
		
		TextView text = (TextView) view.findViewById(R.id.display_text);
		text.setText(getItem(position));
		
		// TODO Auto-generated method stub
		return view;
	}
	
	

}
