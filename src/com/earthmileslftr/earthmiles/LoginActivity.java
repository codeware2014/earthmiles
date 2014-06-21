package com.earthmileslftr.earthmiles;

//import android.app.Activity;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Base64;
import android.util.Log;

public class LoginActivity extends FragmentActivity{

	private FbFragment fragment;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		try {
	        PackageInfo info = getPackageManager().getPackageInfo(
	                "com.earthmileslftr.earthmiles", 
	                PackageManager.GET_SIGNATURES);
	        for (Signature signature : info.signatures) {
	            MessageDigest md = MessageDigest.getInstance("SHA");
	            md.update(signature.toByteArray());
	            Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
	            }
	    } catch (NameNotFoundException e) {

	    } catch (NoSuchAlgorithmException e) {

	    }
		if (savedInstanceState == null) {
	        // Add the fragment on initial activity setup
	        fragment = new FbFragment();
	        getSupportFragmentManager()
	        .beginTransaction()
	        .add(android.R.id.content, fragment)
	        .commit();
	    } else {
	        // Or set the fragment from restored state info
	        fragment = (FbFragment) getSupportFragmentManager()
	        .findFragmentById(android.R.id.content);
	    }
	}
	
}
