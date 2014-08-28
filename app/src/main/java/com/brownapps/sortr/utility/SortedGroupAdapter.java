package com.brownapps.sortr.utility;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.brownapps.sortr.R;
import com.brownapps.sortr.utility.SortedGroupAdapter.GroupObject;

public class SortedGroupAdapter  extends ArrayAdapter<GroupObject>{
	
	public SortedGroupAdapter(Context context, int textViewResourceId,
			List<GroupObject> objects) {
		super(context, textViewResourceId, objects);
	}

	
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View tView = convertView;
		if(tView == null){
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			tView = inflater.inflate(R.layout.clickable_item, null);
		}
		
		RelativeLayout layout = (RelativeLayout) tView.findViewById(R.id.clickable_bg);
		TextView title = (TextView) tView.findViewById(R.id.display_text);
		GroupObject obj = this.getItem(position);
		
		title.setText(obj.display);
		if(obj.isGroup){
			layout.setBackgroundColor(Color.GRAY);
		}
		
		// TODO Auto-generated method stub
		return tView;
	}



	public static class GroupObject{
		public boolean isGroup = false;
		public String display = "";
	}
	
	public static List<GroupObject> listToGroupableObjects(){
		List<GroupObject> items = new ArrayList<GroupObject>();
	
		return items;
	}
}
