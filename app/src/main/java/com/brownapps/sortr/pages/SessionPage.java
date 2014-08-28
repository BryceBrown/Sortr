package com.brownapps.sortr.pages;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.brownapps.sortr.R;
import com.brownapps.sortr.data.DataLayer;
import com.brownapps.sortr.data.GeneratedTeams;
import com.brownapps.sortr.utility.Consts;
import com.brownapps.sortr.utility.Logging;
import com.brownapps.sortr.utility.SortedGroupAdapter;

public class SessionPage extends Activity {
	
	private long sessionId = -1;
	private GeneratedTeams session;
	private DataLayer layer;
	private Button emailListButton;
	
	private ListView sortedList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.session_page);
		
		sortedList = (ListView) findViewById(R.id.team_list);
		emailListButton = (Button) findViewById(R.id.email_list);
		
		layer = new DataLayer(this);
		
		try{
			sessionId = getIntent().getExtras().getLong(Consts.SESSION_ID);		
		}catch(Exception e){
			Logging.LogDebugError("SessionPage", e);
		}
		
		if(sessionId != -1){
			session = layer.getGeneratedTeam(sessionId);
			
			sortedList.setAdapter(new SortedGroupAdapter(this, R.layout.clickable_item, session.getGroupableObject()));
		}
		
		emailListButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
				emailIntent.setType("text/html");
				emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Sorted Teams");
				emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, Html.fromHtml(session.getAsHtml()));
				startActivity(Intent.createChooser(emailIntent, "Email to..."));
			}
		});
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
