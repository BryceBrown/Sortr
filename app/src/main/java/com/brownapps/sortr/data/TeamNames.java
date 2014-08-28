package com.brownapps.sortr.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.ContentValues;
import android.database.Cursor;

public class TeamNames {

	public String Name = "";
	public long Id = -1;
	public boolean IsStatic = false;
	
	public ArrayList<String> Names = new ArrayList<String>();
	private ArrayList<String> usedNames = new ArrayList<String>();
	
	public TeamNames(Cursor names){
		if(names.moveToFirst()){
			do{
				Names.add(names.getString(names.getColumnIndex("TeamElementName")));
			}while(names.moveToNext());
		}
	}
	
	public TeamNames(){
		
	}
	
	public ContentValues getContentValues(){
		ContentValues values = new ContentValues();
		values.put("", Name);
		if(Id != -1){
			values.put("", Id);
		}
		
		return values;
	}
	
	public String GetUnusedName(long seed){
		if(usedNames.size() >= Names.size()){
			return "";
		}
		
		Random rand = new Random(seed);
		List<String> unusedNames = new ArrayList<String>();
		//first, create a temp list with all unused names
		for(String name: Names){
			boolean isUsed = false;
			for(String usedName: usedNames){
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
		usedNames.add(unusedName);
		return unusedName;
	}
	
	public static List<ClickableItem> getClickableItemsFromList(List<TeamNames> names){
		List<ClickableItem> items = new ArrayList<ClickableItem>();
		
		for(TeamNames name: names){
			ClickableItem item = new ClickableItem();
			item.displayName = name.Name;
			item.Id = name.Id;
			item.type = ClickableItem.Type.TeamNames;
			items.add(item);
		}
		
		return items;
	}
	
	public static String[] getTeamNamesAsArray(List<TeamNames> names){
		String[] nameList = new String[names.size()];
		
		for(int i=0;i<names.size();i++){
			nameList[i] = names.get(i).Name;
		}
		
		return nameList;
	}
	
	public static List<TeamNames> getStaticTeamNames(){
		
		List<TeamNames> list = new ArrayList<TeamNames>();
		
		//Make the static Team Names list
		TeamNames cats = new TeamNames();
		cats.Id = -1;
		cats.IsStatic = true;
		cats.Name = "Cats";
		cats.Names.add("Lions");
		cats.Names.add("Tigers");
		cats.Names.add("Pumas");
		cats.Names.add("Panthers");
		cats.Names.add("House Cats");
		cats.Names.add("Grumpy Cats");
		cats.Names.add("Wild Cats");
		
		TeamNames dogs = new TeamNames();
		dogs.Name =  "Dogs";
		dogs.Id = -2;
		dogs.IsStatic = true;
		dogs.Names.add("Golden Retreivers");
		dogs.Names.add("Pitbulls");
		dogs.Names.add("Terriers");
		dogs.Names.add("Corgis");
		dogs.Names.add("Chihuahaas");
		dogs.Names.add("Labs");
		dogs.Names.add("Hyenas");
		
		list.add(cats);
		list.add(dogs);
		
		return list;
	}
	
}
