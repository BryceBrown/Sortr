package com.brownapps.sortr.data;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;

public class Team {
	
	public String Name = "";
	public long Id = -1;
	public List<String> teamMembers = new ArrayList<String>();
		
	public ContentValues getContentValues(){
		ContentValues values = new ContentValues();
		values.put("GeneratedTeamName", Name);
		
		return values;
	}
	
}
