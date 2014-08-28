package com.brownapps.sortr.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.brownapps.sortr.data.ClickableItem.Type;

import android.content.ContentValues;
import android.database.Cursor;

public class PlayerList {
	
	public String Name = "";
	public long Id  = -1;
	
	public List<String> list = new ArrayList<String>();
	private List<String> usedPlayer = new ArrayList<String>();
	
	public PlayerList(Cursor c){
		if(c.moveToFirst()){
			do{
				list.add(c.getString(c.getColumnIndex("Name")));
			}while(c.moveToNext());
		}
	}
	
	public PlayerList(){
		
	}
	
	public String GetUnusedPlayer(long seed){
		if(usedPlayer.size() >= list.size()){
			return "";
		}
		
		Random rand = new Random(seed);
		List<String> unusedNames = new ArrayList<String>();
		//first, create a temp list with all unused names
		for(String name: list){
			boolean isUsed = false;
			for(String usedName: usedPlayer){
				if(name.matches(usedName)){
					isUsed = true;
					break;
				}
			}
			if(!isUsed){
				unusedNames.add(name);
			}
		}
		
		//Ok, now grab a random name
		String unusedName = unusedNames.get(rand.nextInt(unusedNames.size()));
		usedPlayer.add(unusedName);
		return unusedName;
	}
	
	public boolean areAllPlayersUsed(){
		return usedPlayer.size() >= list.size();
	}
	
	public static List<ClickableItem> getClickableItemsFromList(List<PlayerList> names){
		List<ClickableItem> items = new ArrayList<ClickableItem>();
		
		for(PlayerList name: names){
			ClickableItem item = new ClickableItem();
			item.displayName = name.Name;
			item.Id = name.Id;
			item.type = Type.PersonList;
			items.add(item);
		}
		
		return items;
	}
	
	public static String[] getPlayerListsAsString(List<PlayerList> names){
		String[] nameList = new String[names.size()];
		
		for(int i=0;i<names.size();i++){
			nameList[i] = names.get(i).Name;
		}
		
		return nameList;
	}
	
	public ContentValues getContentValues(){
		ContentValues values = new ContentValues();
		values.put("ListName", Name);
		if(Id != -1){
			values.put("PersonListId", Id);
		}
		
		return values;
	}

}
