package com.brownapps.sortr.utility;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.brownapps.sortr.R;
import com.brownapps.sortr.data.ClickableItem;

public class ClickableItemAdapter extends ArrayAdapter<ClickableItem> {

	public ClickableItemAdapter(Context context, int textViewResourceId,
			List<ClickableItem> objects) {
		super(context, textViewResourceId, objects);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View nView = convertView;
		
		
		if (nView == null) {
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			nView = inflater.inflate(R.layout.clickable_item, null);
		}
		
		ClickableItem item = this.getItem(position);
		if(item != null){
			TextView text = (TextView) nView.findViewById(R.id.display_text);
			text.setText(item.displayName);
			nView.setTag(item);
		}
		
		return nView;
	}

	
}
