package com.earthmileslftr.earthmiles;

import java.util.Arrays;
import java.util.List;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.LoginButton;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

@SuppressLint("NewApi")
public class FbFragment extends Fragment{

	private static final String TAG = "FbFragment";
	private UiLifecycleHelper uiHelper;
	private String access_token;
	private Button continueBtn;
	
	
	private Session.StatusCallback callback = new Session.StatusCallback() {
	    @Override
	    public void call(Session session, SessionState state, Exception exception) {
	        onSessionStateChange(session, state, exception);
	    }
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    uiHelper = new UiLifecycleHelper(getActivity(), callback);
	    uiHelper.onCreate(savedInstanceState);
	}
	
	@Override
	public void onResume() {
	    super.onResume();
	 // For scenarios where the main activity is launched and user
	    // session is not null, the session state change notification
	    // may not be triggered. Trigger it if it's open/closed.
	    Session session = Session.getActiveSession();
	    if (session != null &&
	           (session.isOpened() || session.isClosed()) ) {
	        onSessionStateChange(session, session.getState(), null);
	    }
	    uiHelper.onResume();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    uiHelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onPause() {
	    super.onPause();
	    uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
	    super.onDestroy();
	    uiHelper.onDestroy();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);
	    uiHelper.onSaveInstanceState(outState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view=inflater.inflate(R.layout.activity_login, container, false);
		LoginButton authButton = (LoginButton) view.findViewById(R.id.loginButton1);
		continueBtn= (Button)view.findViewById(R.id.button1);
		authButton.setFragment(this);
		authButton.setReadPermissions(Arrays.asList("user_location","user_friends","email"));
		return view;

	}
	
	private void onSessionStateChange(Session session, SessionState state, Exception exception) {
	    if (state.isOpened()) {
	        Log.i(TAG, "Logged in...");
	        
	        List<String> prems=session.getPermissions();
	        Log.e("Permissions",prems.toString());
	        if(prems.size()<3){
	        	Toast.makeText(getActivity(), "You did not give permission to access your public profile, try again.", Toast.LENGTH_LONG).show();
	        	return;
	        }
	        access_token = session.getAccessToken();
	        Log.e("Access Token", access_token);
	        continueBtn.setVisibility(View.VISIBLE);
	        continueBtn.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent i = new Intent(FbFragment.this.getActivity(),UserDetailsActivity.class);
			        i.putExtra("access_token", access_token);
			        startActivity(i);
				}
			});
	        
	    } else if (state.isClosed()) {
	        Log.i(TAG, "Logged out...");
	        continueBtn.setVisibility(View.INVISIBLE);
	    }
	}
}
