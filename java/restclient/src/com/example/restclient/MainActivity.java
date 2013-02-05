package com.example.restclient;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity {
	private String authToken = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new GetAuthTask(this).execute("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    public void sendRequest(View v) {
    	(new SendRequestTask()).execute("");
    }
    private class GetAuthTask extends AsyncTask<String, Void, String> {

    	private Activity act;
    	public GetAuthTask(Activity act) {
    		this.act = act;
    	}
		@Override
		protected String doInBackground(String... params) {
			AccountManager mgr = AccountManager.get(act); 
			   Account[] accts = mgr.getAccountsByType("com.google"); 
			   Account acct = accts[0];
			   AccountManagerFuture<Bundle> accountManagerFuture = mgr.getAuthToken(acct, "ah", null, act, null, null);
			   Bundle authTokenBundle;
			try {
				authTokenBundle = accountManagerFuture.getResult();
	 		    authToken = authTokenBundle.get(AccountManager.KEY_AUTHTOKEN).toString();
			} catch (OperationCanceledException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (AuthenticatorException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			return authToken;
		}
    	
    }
    private class SendRequestTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... args) {
	    	HttpContext localContext = new BasicHttpContext();
	    	HttpClient client = new DefaultHttpClient();  
	    	HttpPost post = new HttpPost("http://192.168.0.8:3000/users.json"); 
	    	post.setHeader("Content-type", "application/json");
	    	try {
	    	JSONObject obj = new JSONObject();
	    	obj.put("access_token", authToken);

	    	post.setEntity(new StringEntity(obj.toString(), "UTF-8"));

			HttpResponse response = client.execute(post,localContext);
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
    
    }

}
