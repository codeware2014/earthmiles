package com.earthmileslftr.earthmiles;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class RunKeeperDetailsActivity extends Activity{
	private TextView id;
	private String access_token,email;
	private JSONParser jParser = new JSONParser();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_runkeeper_details);
		id = (TextView)findViewById(R.id.rkId);
		access_token=getIntent().getExtras().getString("access_token");
		email=getIntent().getExtras().getString("email");
		new LoadUserId().execute();
	}
	
	public void onClick(View v){
		finish();
	}

	private class LoadUserId extends AsyncTask<String,Integer,JSONObject>{

		@Override
		protected JSONObject doInBackground(String... params) {
			// TODO Auto-generated method stub
			ArrayList<NameValuePair> nvp=new ArrayList<NameValuePair>();
			JSONObject id = jParser.makeHttpRequest("http://nameless-beach-3331.herokuapp.com/emusers/getRunkeeperInfo/"+email+"/"+access_token+"/", "GET", nvp);
			return id;
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(result==null){
				Toast.makeText(RunKeeperDetailsActivity.this, "An error has ocurres", Toast.LENGTH_LONG).show();
				finish();
				return;
			}
			try {
				id.setText(result.getString("userID"));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Toast.makeText(RunKeeperDetailsActivity.this, "Your details are saved in the server side. Please wait for the next release in order to see your activites and to get amazing rewards. Thank you for using Earthmiles.", Toast.LENGTH_LONG).show();
		}
	}
}
