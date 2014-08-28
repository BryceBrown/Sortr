package com.brownapps.sortr.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataLayer {
	
	private static final String TAG = "DataLayer";
	
	private static final String DB_NAME = "Sortr";
	private final static int DB_VERSION = 1;
	
	private DbHandler handler;
	
	private class DbHandler extends SQLiteOpenHelper{

		public DbHandler(Context context) {
			super(context, DB_NAME, null, DB_VERSION);
		}
		
		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL("DROP TABLE IF EXISTS " + DB_NAME);
			
			db.execSQL("CREATE TABLE Persons(" +
					"PersonId INTEGER PRIMARY KEY," +
					"PersonListId INTEGER," +
					"Name TEXT);");
			
			db.execSQL("CREATE TABLE PersonList(" +
					"PersonListId INTEGER PRIMARY KEY," +
					"ListName TEXT);");
			
			db.execSQL("CREATE TABLE TeamNames(" +
					"TeamId INTEGER PRIMARY KEY," +
					"MainName TEXT);");
			
			db.execSQL("CREATE TABLE TeamElements(" +
					"TeamElementId INTEGER PRIMARY KEY," +
					"TeamId INTEGER," +
					"TeamElementName TEXT);");
		
			db.execSQL("CREATE TABLE GeneratedSessions(" +
					"GeneratedSessionId INTEGER PRIMARY KEY," +
					"GeneratedSessionName TEXT," +
					"NumTeams INTEGER," +
					"PersonListId INTEGER," +
					"TeamId INTEGER);");
			
			db.execSQL("CREATE TABLE GeneratedTeams(" +
					"GeneratedTeamId INTEGER PRIMARY KEY," +
					"GeneratedTeamName TEXT);");
			
			db.execSQL("CREATE TABLE GeneratedTeamMembers(" +
					"GeneratedTeamId INTEGER," +
					"PersonId INTEGER," +
					"PRIMARY KEY(GeneratedTeamId, PersonId));");
			
			db.execSQL("CREATE TABLE GeneratedSessionTeams(" +
					"GeneratedSessionId INTEGER," +
					"GeneratedTeamId INTEGER," +
					"PRIMARY KEY(GeneratedSessionId, GeneratedTeamId));");
			
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			
	
		}
	
	}
	
	public DataLayer(Context cont){
		handler = new DbHandler(cont);
	}
	/**
	 * Returns a list of all TeamNames
	 * @return
	 * @throws Exception
	 */
	public List<TeamNames> getAllTeamNames(){
		
		List<TeamNames> names = new ArrayList<TeamNames>();
		names.addAll(TeamNames.getStaticTeamNames());
		
		SQLiteDatabase db = handler.getReadableDatabase();
		//first, get the list of Team Names and ID's
		Cursor tCursor = db.query("TeamNames", null, null, null, null, null, "MainName");
		if(tCursor.moveToFirst()){
			//Loop through the TeamNames Lists, and build an object from the elements
			do{
				String tName = tCursor.getString(tCursor.getColumnIndex("MainName"));
				int tNamesID = tCursor.getInt(tCursor.getColumnIndex("TeamId"));
				String[] args = {tNamesID + ""};
				
				//Query and loop through team names and add them to element
				Cursor nameCursor = db.query("TeamElements", null, "TeamId=?", args, null, null, "TeamElementName");
				if(nameCursor.moveToFirst()){
					TeamNames element = new TeamNames(nameCursor);
					element.Name = tName;
					element.Id = tNamesID;
					names.add(element);
				}
				nameCursor.close();
				
			}while(tCursor.moveToNext());
		}
		
		tCursor.close();
		db.close();
		
		return names;
	}
	
	public TeamNames getTeamNameById(long teamId){
		
		
		List<TeamNames> names = new ArrayList<TeamNames>();
		names.addAll(TeamNames.getStaticTeamNames());
		
		SQLiteDatabase db = handler.getReadableDatabase();
		//first, get the list of Team Names and ID's
		Cursor tCursor = db.query("TeamNames", null, "TeamId=" + teamId, null, null, null, "MainName");
		if(tCursor.moveToFirst()){
			//Loop through the TeamNames Lists, and build an object from the elements
			
				String tName = tCursor.getString(tCursor.getColumnIndex("MainName"));
				int tNamesID = tCursor.getInt(tCursor.getColumnIndex("TeamId"));
				String[] args = {tNamesID + ""};
				
				//Query and loop through team names and add them to element
				Cursor nameCursor = db.query("TeamElements", null, "TeamId=?", args, null, null, "TeamElementName");
				if(nameCursor.moveToFirst()){
					TeamNames element = new TeamNames(nameCursor);
					element.Name = tName;
					element.Id = tNamesID;
					return element;
				}
				nameCursor.close();
			
		}
		
		tCursor.close();
		db.close();
		
		return null;
	}
	
	public List<PlayerList> getAllPeopleLists(){
		
		List<PlayerList> pList = new ArrayList<PlayerList>();
		SQLiteDatabase db = handler.getWritableDatabase();
		Cursor pListCursor = db.query("PersonList", null, null, null, null, null, "ListName");
		if(pListCursor.moveToFirst()){
			do{
				String name = pListCursor.getString(pListCursor.getColumnIndex("ListName"));
				int listId  = pListCursor.getInt(pListCursor.getColumnIndex("PersonListId"));
				String[] args =  {listId + ""};
				
				Cursor names = db.query("Persons", null, "PersonListId=?", args, null, null, "");
				if(names.moveToFirst()){
					PlayerList list = new PlayerList(names);
					list.Name = name;
					list.Id = listId;
					pList.add(list);
				}
				names.close();
			}while(pListCursor.moveToNext());
		}
		
		pListCursor.close();
		db.close();
		
		return pList;
	}
	
	public PlayerList getPeopleListById(long personsId){
		
		SQLiteDatabase db = handler.getReadableDatabase();
		Cursor pListCursor = db.query("PersonList", null, "PersonListId=" + personsId, null, null, null, "ListName");
		if(pListCursor.moveToFirst()){
				String name = pListCursor.getString(pListCursor.getColumnIndex("ListName"));
				int listId  = pListCursor.getInt(pListCursor.getColumnIndex("PersonListId"));
				String[] args =  {listId + ""};
				
				Cursor names = db.query("Persons", null, "PersonListId=?", args, null, null, "");
				if(names.moveToFirst()){
					PlayerList list = new PlayerList(names);
					list.Name = name;
					list.Id = listId;
					return list;
				}
				names.close();
		}
		
		
		//close all databases and cursors
		pListCursor.close();
		db.close();
		
		return null;
	}
	
	public void savePlayerList(PlayerList list) throws Exception{
		
		SQLiteDatabase db = handler.getReadableDatabase();
		//first, delete the previous person list if there is one
		db.delete("Persons", "PersonListId=" + list.Id, null);
		db.delete("PersonList", "PersonListId=" + list.Id, null);
		
		long newId = db.insertOrThrow("PersonList", null, list.getContentValues());
		if(newId != -1){
			list.Id = newId;
			
			//now, loop through the people in the list and save them
			for(String per : list.list){ 
				savePerson(per, list.Id, db);
			}
		}
		
		db.close();
	}
	
	private void savePerson(String argName, long PersonListId, SQLiteDatabase db){
		ContentValues values = new ContentValues();
		values.put("PersonListId", PersonListId);
		values.put("Name", argName);
		db.insert("Persons", null, values);
		
	}
	
	public void saveTeamNamesList(TeamNames names) throws Exception{
		
		SQLiteDatabase db = handler.getReadableDatabase();
		
		//Remove previous entries
		db.delete("TeamNames", "TeamId=" + names.Id, null);
		db.delete("TeamElements", "TeamId=" + names.Id, null);
		
		long newTeamNameId = db.insertOrThrow("TeamNames", null, names.getContentValues());
		if(newTeamNameId != -1){
			names.Id = newTeamNameId;
			
			//now save all team names
			for(String name : names.Names){
				ContentValues values = new ContentValues();
				values.put("TeamElementName", name);
				values.put("TeamId", newTeamNameId);
				
				db.insertOrThrow("TeamElements", null, values);
			}
		}
		
		db.close();
	}
	
	/**
	 * Returns the id of the newley generated team list
	 * @param numTeams
	 * @param tNamesList
	 * @param pList
	 * @return
	 */
	public long generateRandomTeams(int numTeams, TeamNames tNamesList, PlayerList pList){
		Random rand = new Random();

		GeneratedTeams teams  = new GeneratedTeams();
		teams.NumberOfTeams = numTeams;
		teams.PersonListId = pList.Id;
		teams.TeamsListId = tNamesList.Id;
		for(int i=0;i<numTeams;i++){
			Team team = new Team();
			team.Name = tNamesList.GetUnusedName(rand.nextLong());
			teams.Teams.add(team);
		}
		
		//now we have a list of teams, start adding people
		do{
			for(Team team: teams.Teams){
				if(!pList.areAllPlayersUsed()){
					team.teamMembers.add(pList.GetUnusedPlayer(rand.nextLong()));
				}else{
					break;
				}
			}
		}while(!pList.areAllPlayersUsed());
		
		//Now that we have a list of teams, save it, get and ID, and return it
		
		
		return saveGeneratedTeams(teams);
	}
	
	private long saveGeneratedTeams(GeneratedTeams teams){
		
		//first, save a new session
		SQLiteDatabase db = handler.getWritableDatabase();
		
		teams.Id = db.insert("GeneratedSessions", null, teams.getContentValues());
		//Now, save each team
		for(Team team: teams.Teams){
			team.Id = db.insert("GeneratedTeams", null, team.getContentValues());
			
			//Now save the member of each team
			for(String player: team.teamMembers){
				ContentValues values = new ContentValues();
				int personId = this.getPersonIdByName(db, player, teams.PersonListId);
				values.put("PersonId", personId);
				values.put("GeneratedTeamId", team.Id);
				db.insert("GeneratedTeamMembers", null, values);
			}
			
			//now save the relation
			ContentValues relationValues = new ContentValues();
			relationValues.put("GeneratedSessionId", teams.Id);
			relationValues.put("GeneratedTeamId", team.Id);
			db.insert("GeneratedSessionTeams", null, relationValues);
			
			
			
		}
		
		return teams.Id;
	}
	
	public int getPersonIdByName(SQLiteDatabase db, String name, long personListId){
		Cursor personCursor = db.query("Persons", new String[] {"PersonId"}, "Name=? AND PersonListId=?", 
				new String[] { name, "" + personListId }, "", "", "");
		if(personCursor != null){
			if(personCursor.moveToFirst()){
				return personCursor.getInt(personCursor.getColumnIndex("PersonId"));
			}
		}
		
		
		return -1;
	}
	
	public void updateGeneratedSession(GeneratedTeams teams){
		
	}
	
	public GeneratedTeams getGeneratedTeam(long argId){
		
		//first, save a new session
		SQLiteDatabase db = handler.getReadableDatabase();
		
		Cursor sessionCursor = db.query("GeneratedSessions", null, "GeneratedSessionId=?", new String[] {"" + argId}, null, null, null);
		if(sessionCursor != null && sessionCursor.moveToFirst()){
			
			GeneratedTeams team = new GeneratedTeams();
			team.Id = sessionCursor.getInt(sessionCursor.getColumnIndex("GeneratedSessionId"));
			team.NumberOfTeams = sessionCursor.getInt(sessionCursor.getColumnIndex("NumTeams"));
			team.TeamsListId = sessionCursor.getLong(sessionCursor.getColumnIndex("TeamId"));
			team.PersonListId  = sessionCursor.getInt(sessionCursor.getColumnIndex("PersonListId"));
			sessionCursor.close();
			
			Cursor teamCursor = db.query("GeneratedSessionTeams", null, "GeneratedSessionId=?", new String[] {argId + ""}, null, null, null);
			List<Team> teams = new ArrayList<Team>();
			if(teamCursor != null && teamCursor.moveToFirst()){
				do{
					Team itm = new Team();
					itm.Id = teamCursor.getInt(teamCursor.getColumnIndex("GeneratedTeamId"));
					teams.add(itm);
				}while(teamCursor.moveToNext());
				teamCursor.close();
				for(Team genTeam : teams){
					Cursor tCursor = db.query("GeneratedTeams", null, "GeneratedTeamId=?", new String[] {genTeam.Id + ""}, null, null, null);
					if(tCursor != null && tCursor.moveToFirst()){
						genTeam.Name = tCursor.getString(tCursor.getColumnIndex("GeneratedTeamName"));
					}
					tCursor.close();
					//Now get all people associated with each team
					List<Integer> personIds = new ArrayList<Integer>();
					Cursor peopleInTeams = db.query("GeneratedTeamMembers", null, "GeneratedTeamId=?", new String[] {"" + genTeam.Id}, null, null, null);
					if(peopleInTeams != null && peopleInTeams.moveToFirst()){
						do{
							personIds.add(peopleInTeams.getInt(peopleInTeams.getColumnIndex("PersonId")));
						}while(peopleInTeams.moveToNext());
					}
					if(peopleInTeams != null){
						peopleInTeams.close();
					}
					for(int perId : personIds){
						Cursor person = db.query("Persons", null, "PersonId=?", new String[] {perId + ""}, null, null, null);
						if(person != null && person.moveToFirst()){
							genTeam.teamMembers.add(person.getString(person.getColumnIndex("Name")));
						}
					}
					team.Teams.add(genTeam);
				}
			}
			
			return team;
		}
		
		return null;
	}
	
	
}
