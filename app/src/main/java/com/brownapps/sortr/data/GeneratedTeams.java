package com.brownapps.sortr.data;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;

import com.brownapps.sortr.utility.IGroupableObject;
import com.brownapps.sortr.utility.SortedGroupAdapter.GroupObject;

public class GeneratedTeams implements IGroupableObject{
	
	public long Id = -1;
	public List<Team> Teams = new ArrayList<Team>();
	public int NumberOfTeams = 0;
	public long PersonListId = 0;
	public long TeamsListId = 0;
	
	public ContentValues getContentValues(){
		ContentValues values = new ContentValues();
		
		if(Id != -1){
			values.put("GeneratedSessionId", Id);
		}
		values.put("NumTeams", NumberOfTeams);
		values.put("PersonListId", PersonListId);
		values.put("TeamId", TeamsListId);
		
		return values;
	}

	@Override
	public List<GroupObject> getGroupableObject() {
		List<GroupObject> objs = new ArrayList<GroupObject>();
		
		for(Team team : Teams){
			GroupObject teamName = new GroupObject();
			teamName.display = team.Name;
			teamName.isGroup = true;
			objs.add(teamName);
			for(String person: team.teamMembers){
				GroupObject item = new GroupObject();
				item.display = person;
				objs.add(item);
			}
		}
		
		return objs;
	}
	
	public String getAsHtml(){
		StringBuilder sb = new StringBuilder();
		
		sb.append("Your sorted teams list");
		sb.append("<table>");

		for(Team team : Teams){
			sb.append("<tr>");
			sb.append("<td><h3>");
			sb.append(team.Name);
			sb.append("</h3></td>");
			sb.append("</tr>");
			sb.append("<br />");
			for(String person : team.teamMembers){
				sb.append("<tr>");
				sb.append("<td>");
				sb.append(person);
				sb.append("</td>");
				sb.append("</tr>");
				sb.append("<br />");
			}
		}
		
		sb.append("</table>");
		
		return sb.toString();
	}
	

}
