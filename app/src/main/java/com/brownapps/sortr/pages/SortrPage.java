package com.brownapps.sortr.pages;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.brownapps.sortr.R;
import com.brownapps.sortr.data.DataLayer;
import com.brownapps.sortr.data.PlayerList;
import com.brownapps.sortr.data.TeamNames;
import com.brownapps.sortr.utility.Consts;

public class SortrPage extends Activity {
	
	private Spinner personListSpinner, teamsListSpinner;
	private EditText numberOfTeams;
	private Button saveButton;
	
	private DataLayer layer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sortr_page);
	
		personListSpinner = (Spinner) findViewById(R.id.person_list_spinner);
		teamsListSpinner = (Spinner) findViewById(R.id.team_list_spinner);
		numberOfTeams = (EditText) findViewById(R.id.team_number_edit_text);
		saveButton = (Button) findViewById(R.id.sort_button);
		
		layer = new DataLayer(this);
		
		ArrayAdapter<String> personArr = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, 
				PlayerList.getPlayerListsAsString(layer.getAllPeopleLists()));
		personListSpinner.setAdapter(personArr);
		
		ArrayAdapter<String> teamArr = new ArrayAdapter<String>(this, 
				android.R.layout.simple_spinner_item, TeamNames.getTeamNamesAsArray(layer.getAllTeamNames()));
		teamsListSpinner.setAdapter(teamArr);
		
		saveButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Save();
			}
		});
		
	} 
	
	private void Save(){
		PlayerList playerList = layer.getAllPeopleLists().get(personListSpinner.getSelectedItemPosition());
		TeamNames teamList = layer.getAllTeamNames().get(teamsListSpinner.getSelectedItemPosition());	
		
		if(numberOfTeams.getText().length() == 0){
			Toast.makeText(this, "Please type in a number of teams", Toast.LENGTH_LONG).show();
			return;
		}
		
		int numTeams = Integer.parseInt(numberOfTeams.getText().toString());
		if(numTeams <= 0){
			Toast.makeText(this, "The number of teams needs to be greater than 0", Toast.LENGTH_LONG).show();
			return;
		}
		
		if(numTeams > playerList.list.size()){
			Toast.makeText(this, "There must be fewer teams than players in the player list (" + playerList.list.size() + ")", Toast.LENGTH_LONG).show();
			return;
		}
		
		if(numTeams > teamList.Names.size()){
			Toast.makeText(this, "There must be fewer teams than teams in the team list (" + teamList.Names.size() + ")", Toast.LENGTH_LONG).show();
			return;
		}
		
		long sessionId = layer.generateRandomTeams(numTeams, teamList, playerList);
		this.navigateToViewSessionPage(sessionId);
		
	}
	
	private void navigateToViewSessionPage(long id){
		Intent intent = new Intent(this, SessionPage.class);
		intent.putExtra(Consts.SESSION_ID, id);
		startActivity(intent);
		finish();
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	    case R.id.menu_purchase_app:
	    	Intent intent = new Intent(Intent.ACTION_VIEW);
	    	intent.setData(Uri.parse("market://details?id=com.brownapps.sortr.payed"));
	    	startActivity(intent);
	    	return true;
	    }
	    return false;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
