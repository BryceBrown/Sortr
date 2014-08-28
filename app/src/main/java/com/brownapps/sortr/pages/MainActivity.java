package com.brownapps.sortr.pages;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.Toast;

import com.brownapps.sortr.R;
import com.brownapps.sortr.data.ClickableItem;
import com.brownapps.sortr.data.ClickableItem.Type;
import com.brownapps.sortr.data.DataLayer;
import com.brownapps.sortr.data.PlayerList;
import com.brownapps.sortr.data.TeamNames;
import com.brownapps.sortr.utility.ClickableItemAdapter;
import com.brownapps.sortr.utility.Consts;

public class MainActivity extends Activity {
	
	private Button addButton, addTeamsButton, sortButton;
	private TabHost host;
	private ListView personListView, teamListView;
	private DataLayer layer;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//Load UI Elements
		addButton = (Button) findViewById(R.id.AddButton);
		addTeamsButton = (Button) findViewById(R.id.AddTeamsButton);
		personListView = (ListView) findViewById(R.id.PersonList);
		teamListView = (ListView) findViewById(R.id.TeamList);
		host = (TabHost) findViewById(R.id.tab_host);
		sortButton = (Button) findViewById(R.id.sort_button);
		
		host.setup();
		
		TabSpec peopleTab = host.newTabSpec("People");
		peopleTab.setIndicator("People");
		peopleTab.setContent(R.id.People);
	    host.addTab(peopleTab);

	    TabSpec teamTab = host.newTabSpec("Teams");
	    teamTab.setIndicator("Teams");
	    teamTab.setContent(R.id.TeamList);
	    host.addTab(teamTab);
		
		host.setCurrentTabByTag("People");
		
		layer = new DataLayer(this);
		
		//Set up list adapters
		personListView.setAdapter(new ClickableItemAdapter(this, R.layout.clickable_item, 	
				PlayerList.getClickableItemsFromList(layer.getAllPeopleLists())));
		
		teamListView.setAdapter(new ClickableItemAdapter(this, R.layout.clickable_item, 
				TeamNames.getClickableItemsFromList(layer.getAllTeamNames())));
	
		OnItemClickListener listener = new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> adpt, View view, int pos,
					long arg3) {
				ClickableItem item = (ClickableItem) view.getTag();
				if(item.type == Type.TeamNames){
					launchTeamsPage(item.Id);
				}else if(item.type == Type.PersonList){
					launchPersonsPage(item.Id);
				}
				
			}
			
		};
		
		personListView.setOnItemClickListener(listener);
		teamListView.setOnItemClickListener(listener);
		
		View.OnClickListener clickHandler = new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int currentTab = host.getCurrentTab();
				if(currentTab == 0){
					MainActivity.this.launchPersonsPage(0);
				}else{
					Toast.makeText(MainActivity.this, "You can only add new team names in the paid version!", Toast.LENGTH_LONG).show();
					//MainActivity.this.launchTeamsPage(0);
				}
				
				
			}
		}; 
		
		addButton.setOnClickListener(clickHandler);
		addTeamsButton.setOnClickListener(clickHandler);
		
		sortButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(layer.getAllPeopleLists().size() == 0){
					Toast.makeText(MainActivity.this, "You must have at least one list of people to create teams", Toast.LENGTH_LONG).show();
				}else{
					startSort();	
				}
			}
		});
		
	}
	
	private void startSort(){
		Intent intent = new Intent(this, SortrPage.class);
		startActivity(intent);
	}
	
	private void launchTeamsPage(long id){
		Intent intent = new Intent(this, TeamsPage.class);
		intent.putExtra(Consts.TEAM_ID, id);
		startActivityForResult(intent, 0);
	}
	
	private void launchPersonsPage(long id){
		//On the free Version, you can only have one list of people
		if(id == 0 && layer.getAllPeopleLists().size() == 1){
			Toast.makeText(this, "You can only have one list of players saved on the free version. Upgrade to store multiple lists", Toast.LENGTH_LONG).show();
			return;
			
		}
		Intent intent = new Intent(this, PersonsListPage.class);
		intent.putExtra(Consts.PERSON_ID, id);
		startActivityForResult(intent, 0);
	}
	
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		personListView.setAdapter(new ClickableItemAdapter(this, R.layout.clickable_item, 	
				PlayerList.getClickableItemsFromList(layer.getAllPeopleLists())));
		
		teamListView.setAdapter(new ClickableItemAdapter(this, R.layout.clickable_item, 
				TeamNames.getClickableItemsFromList(layer.getAllTeamNames())));
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
