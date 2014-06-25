package com.earthmileslftr.earthmiles;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class RunKeeperActivity extends Activity{
	
	private WebView webView;
	private final static String CLIENT_ID = "b9009d8d55a54420b0310c08ddaa483d";
    private final static String CLIENT_SECRET = "cffe7ae5c2154d48bbedb338619ca873";
    private final static String CALLBACK_URL = "com.example.runkeeperapi://RunKeeperIsCallingBack";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_runkeeper);
		webView = (WebView)findViewById(R.id.webView);
		CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
        webView.getSettings().setJavaScriptEnabled(true);
		getAuthorizationCode();
	}

	private void getAuthorizationCode() {
		// TODO Auto-generated method stub
		String authorizationUrl = "https://runkeeper.com/apps/authorize?response_type=code&client_id=%s&redirect_uri=%s";
        authorizationUrl = String.format(authorizationUrl, CLIENT_ID, CALLBACK_URL);
        
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith(CALLBACK_URL)) {
                    final String authCode = Uri.parse(url).getQueryParameter("code");
                    webView.setVisibility(View.GONE);
                    getAccessToken(authCode);
                    return true;
                }
 
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
 
        webView.loadUrl(authorizationUrl);
	}
	
	private void getAccessToken(String authCode) {
        String accessTokenUrl = "https://runkeeper.com/apps/token?grant_type=authorization_code&code=%s&client_id=%s&client_secret=%s&redirect_uri=%s";
        final String finalUrl = String.format(accessTokenUrl, authCode, CLIENT_ID, CLIENT_SECRET, CALLBACK_URL);
        new LoadAccessToken().execute(finalUrl);
	}
	private class LoadAccessToken extends AsyncTask<String,Integer,JSONObject>{

		@Override
		protected JSONObject doInBackground(String... params) {
			// TODO Auto-generated method stub
			JSONObject json=null; 
			try {

	                HttpClient client = new DefaultHttpClient();
	                HttpPost post = new HttpPost(params[0]);

	                HttpResponse response = client.execute(post);

	                String jsonString = EntityUtils.toString(response.getEntity());
	                json = new JSONObject(jsonString);

	                Log.e("JSON",jsonString);
	                

	            } catch (Exception e) {
	                e.printStackTrace();
	                webView.setVisibility(View.GONE);
	            }
			return json;
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(result==null){
				Toast.makeText(RunKeeperActivity.this,"An error has ocurred", Toast.LENGTH_LONG).show();
				finish();
				return;
			}
			Intent i = new Intent(RunKeeperActivity.this,RunKeeperDetailsActivity.class);
			try {
				i.putExtra("access_token", result.getString("access_token"));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Toast.makeText(RunKeeperActivity.this,"An error has ocurred", Toast.LENGTH_LONG).show();
				finish();
				return;
			}
			i.putExtra("email", getIntent().getExtras().getString("email"));
			startActivity(i);
			finish();
		}
		
	}
}
