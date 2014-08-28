package com.brownapps.sortr.pages;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.brownapps.sortr.R;
import com.brownapps.sortr.data.DataLayer;
import com.brownapps.sortr.data.TeamNames;
import com.brownapps.sortr.utility.Consts;
import com.brownapps.sortr.utility.SimpleStringAdapter;

public class TeamsPage extends Activity {
	
	
	private TeamNames _names = null;
	private DataLayer layer = null;
	
	private Button editButton, saveButton;
	private ListView namesList;
	private TextView teamNameTextView;
	
	private SimpleStringAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.teams_page);
		
		editButton = (Button) findViewById(R.id.edit_button);
		namesList = (ListView) findViewById(R.id.team_names_list);
		teamNameTextView = (TextView) findViewById(R.id.team_name_text);
		saveButton = (Button) findViewById(R.id.save_button);
		
		layer = new DataLayer(this);
		Bundle extras = getIntent().getExtras();
		long teamId = 0;
		if(extras != null){
			teamId = extras.getLong(Consts.TEAM_ID);
		}

		if(teamId != 0){
			
			if(teamId < 0){
				for(TeamNames name: TeamNames.getStaticTeamNames()){
					if(name.Id == teamId){
						_names = name;
						break;
					}
				}
			}else{
				_names = layer.getTeamNameById(teamId);
			}
			
			//Check if team is static
			if(_names.IsStatic){
				editButton.setEnabled(false);
				saveButton.setEnabled(false);
			}
			
			teamNameTextView.setText(_names.Name);
			OnLongClickListener listener = new OnLongClickListener(){

				@Override
				public boolean onLongClick(View v) {
					//((ListView) v).getSelectedItem()
					//TODO Show purchase version only prompt
					return false;
				}

				
			};
			namesList.setOnLongClickListener(listener);
			
			
		}else{
			_names = new TeamNames();
			teamNameTextView.setText("New Team");
		}
		adapter = new SimpleStringAdapter(this, R.layout.clickable_item, _names.Names);
		namesList.setAdapter(adapter);
		
		editButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showEditDialog();
			}
		});
		
		saveButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Save();
			}
		});
	}
	
	private void Save(){
		
	}
	
	private void showEditDialog(){
		if(_names.IsStatic){
			Toast.makeText(TeamsPage.this, "You cannot edit a static team", Toast.LENGTH_SHORT).show();
			return;
 	   }
		
		AlertDialog.Builder builder = new AlertDialog.Builder(TeamsPage.this);
	    builder.setTitle(R.string.edit_dialog_teams)
	    		.setItems(R.array.team_edit_options, new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int which) {
	            	   if(which == 0){
	            		   showEditTeamNameDialog();
	            	   }else{
	            		   showAddTeamNameDialog();
	            	   }
	           }
	    });
	    builder.create().show();
	}

	private void showEditTeamNameDialog(){
		AlertDialog.Builder builder = new AlertDialog.Builder(TeamsPage.this);
	    // Get the layout inflater
	    LayoutInflater inflater = TeamsPage.this.getLayoutInflater();

	    final View editView = inflater.inflate(R.layout.name_dialog, null);
	    // Inflate and set the layout for the dialog
	    // Pass null as the parent view because its going in the dialog layout
	    builder.setView(editView)
	    		.setTitle("Edit List Name")
	            .setPositiveButton("Edit", new DialogInterface.OnClickListener() {
	               @Override
	               public void onClick(DialogInterface dialog, int id) {
	            	   _names.Name = ((EditText)editView.findViewById(R.id.name)).getText().toString();
	            	   teamNameTextView.setText(_names.Name);
	               }
	            })
	            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int id) {
	            	   dialog.cancel();
	               }
	            });      
	    builder.create().show();
	}
	
	private void showAddTeamNameDialog(){
		AlertDialog.Builder builder = new AlertDialog.Builder(TeamsPage.this);
	    // Get the layout inflater
	    LayoutInflater inflater = TeamsPage.this.getLayoutInflater();

	    final View editView = inflater.inflate(R.layout.name_dialog, null);
	    // Inflate and set the layout for the dialog
	    // Pass null as the parent view because its going in the dialog layout
	    builder.setView(editView)
	    		.setTitle("Add Team Name")
	            .setPositiveButton("Add", new DialogInterface.OnClickListener() {
	               @Override
	               public void onClick(DialogInterface dialog, int id) {
	            	   _names.Names.add(((EditText)editView.findViewById(R.id.name)).getText().toString());
	            	   adapter.notifyDataSetChanged();
	               }
	            })
	            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int id) {
	            	   dialog.cancel();
	               }
	            });      
	    builder.create().show();
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
