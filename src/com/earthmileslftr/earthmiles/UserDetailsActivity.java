package com.earthmileslftr.earthmiles;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class UserDetailsActivity extends Activity{
	private String access_token;
	private TextView id, first_name,last_name,email,friends;
	private Button continueBtn;
	private JSONParser jParser = new JSONParser();
	private TableLayout friendsTable;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_details);
		access_token=getIntent().getExtras().getString("access_token");
		Log.e("AT",access_token);
		id=(TextView)findViewById(R.id.fb_id);
		first_name=(TextView)findViewById(R.id.fb_first_name);
		last_name=(TextView)findViewById(R.id.fb_last_name);
		email=(TextView)findViewById(R.id.fb_email);
		//friends=(TextView)findViewById(R.id.fb_friends);
		continueBtn=(Button)findViewById(R.id.fb_continue);
		friendsTable=(TableLayout)findViewById(R.id.tableLayout1);
		new LoadUserDetails().execute();
	}
	
	private class LoadUserDetails extends AsyncTask<String,Integer,JSONObject>{

		@Override
		protected JSONObject doInBackground(String... params) {
			// TODO Auto-generated method stub
			ArrayList<NameValuePair> nvp=new ArrayList<NameValuePair>();
			JSONObject details = jParser.makeHttpRequest("http://nameless-beach-3331.herokuapp.com/emusers/getFacebookInfo/"+access_token+"/", "GET", nvp);
			return details;
		}
		
		@Override
		protected void onPostExecute(JSONObject result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(result==null){
				Toast.makeText(getBaseContext(), "A error has ocurred", Toast.LENGTH_LONG).show();
				finish();
				return;
			}
			
			try {
				id.setText(result.getString("id"));
				first_name.setText(result.getString("first_name"));
				last_name.setText(result.getString("last_name"));
				email.setText(result.getString("email"));
				//friends.setText(result.getJSONObject("friends").getJSONArray("data").toString());
				JSONArray friends=result.getJSONObject("friends").getJSONArray("data");
				for(int i=0;i<friends.length();i++){
					JSONObject friend = friends.getJSONObject(i);
					TableRow row= new TableRow(UserDetailsActivity.this);
					TextView friendId= new TextView(UserDetailsActivity.this);
					friendId.setText(friend.getString("id"));
					friendId.setPadding(0, 0, 10, 0);
					TextView friendName= new TextView(UserDetailsActivity.this);
					friendName.setText(friend.getString("name"));
					row.addView(friendId);
					row.addView(friendName);
					friendsTable.addView(row);
					
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			continueBtn.setVisibility(View.VISIBLE);
		}
		
	}
}
