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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.brownapps.sortr.R;
import com.brownapps.sortr.data.DataLayer;
import com.brownapps.sortr.data.PlayerList;
import com.brownapps.sortr.utility.Consts;
import com.brownapps.sortr.utility.Logging;
import com.brownapps.sortr.utility.SimpleStringAdapter;

public class PersonsListPage extends Activity {

	private PlayerList pList;
	
	private DataLayer layer;
	
	private ListView personsList;
	private Button editButton, saveButton;
	private TextView listName;
	
	private SimpleStringAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.persons_page);
		
		personsList = (ListView) findViewById(R.id.person_names_list);
		listName = (TextView) findViewById(R.id.person_list_name_text);
		editButton = (Button) findViewById(R.id.edit_button);
		saveButton = (Button) findViewById(R.id.save_button);
		
		layer = new DataLayer(this);
		
		long personsId = 0;
		Bundle extras = getIntent().getExtras();
		if(extras != null){
			personsId = extras.getLong(Consts.PERSON_ID);
		}
		
		if(personsId != 0){
			pList = layer.getPeopleListById(personsId);
			listName.setText(pList.Name);
			
			
			
		}else{
			pList = new PlayerList();
			listName.setText("New Person List");
		}
		
		adapter = new SimpleStringAdapter(this, R.layout.clickable_item, pList.list);
		personsList.setAdapter(adapter);
		
		editButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(PersonsListPage.this);
			    builder.setTitle(R.string.edit_dialog_teams)
			    		.setItems(R.array.player_edit_options, new DialogInterface.OnClickListener() {
			               public void onClick(DialogInterface dialog, int which) {
			            	   if(which == 0){
			            		   showEditPlayerListDialog();
			            	   }else{
			            		   showAddPlayerDialog();
			            	   }
			           }
			    });
			    builder.create().show();
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
		try {
			if(pList.Name == ""){
				Toast.makeText(this, "Please enter a name", Toast.LENGTH_LONG).show();
				return;
			}else if(pList.list.size() < 2){
				Toast.makeText(this, "Please enter at least two players", Toast.LENGTH_LONG).show();
				return;
			}
			
			layer.savePlayerList(pList);
			finish();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Logging.LogDebugError("PersonListPage", e);
		}
	}
	
	private void showEditPlayerListDialog(){
		AlertDialog.Builder builder = new AlertDialog.Builder(PersonsListPage.this);
	    // Get the layout inflater
	    LayoutInflater inflater = PersonsListPage.this.getLayoutInflater();

	    final View editView = inflater.inflate(R.layout.name_dialog, null);
	    // Inflate and set the layout for the dialog
	    // Pass null as the parent view because its going in the dialog layout
	    builder.setView(editView)
	    		.setTitle("Edit List Name")
	            .setPositiveButton("Edit", new DialogInterface.OnClickListener() {
	               @Override
	               public void onClick(DialogInterface dialog, int id) {
	            	   pList.Name = ((EditText)editView.findViewById(R.id.name)).getText().toString();
	            	   listName.setText(pList.Name);
	               }
	            })
	            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int id) {
	            	   dialog.cancel();
	               }
	            });      
	    builder.create().show();
	}
	
	private void showAddPlayerDialog(){
		AlertDialog.Builder builder = new AlertDialog.Builder(PersonsListPage.this);
	    // Get the layout inflater
	    LayoutInflater inflater = PersonsListPage.this.getLayoutInflater();

	    final View editView = inflater.inflate(R.layout.name_dialog, null);
	    // Inflate and set the layout for the dialog
	    // Pass null as the parent view because its going in the dialog layout
	    builder.setView(editView)
	    		.setTitle("Add Person")
	            .setPositiveButton("Add", new DialogInterface.OnClickListener() {
	               @Override
	               public void onClick(DialogInterface dialog, int id) {
	            	   pList.list.add(((EditText)editView.findViewById(R.id.name)).getText().toString());
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
