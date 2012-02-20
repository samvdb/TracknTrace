package com.essers.tracking.ui.fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.essers.tracking.R;
import com.essers.tracking.model.service.ServiceHelper;
import com.essers.tracking.model.service.SyncService;
import com.essers.tracking.ui.BaseActivity;
import com.essers.tracking.ui.RecentOrdersActivity;
import com.essers.tracking.util.MyResultReceiver;

public class LoginFragment extends Fragment implements MyResultReceiver.Receiver {
	
	private static final String TAG = "LoginFragment";
	private MyResultReceiver mReceiver;
	private ProgressDialog mProgressDialog;
	public static final int LOGIN_TOKEN = 800;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
		final Button button = (Button) getActivity().findViewById(R.id.login_submit);
		button.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				submitLogin();
			}
		});

		registerReceiver();
		

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.fragment_login, container);
		return v;
	}
	
	private void submitLogin() {
		
		Log.d(TAG, "submitLogin()");
		
		final EditText userField = (EditText)getActivity().findViewById(R.id.login_username);
		final EditText passwordField = (EditText)getActivity().findViewById(R.id.login_password);
		
		Log.d(TAG, userField.getText().toString() + ", " + passwordField.getText().toString());
		// Save login data
		((BaseActivity)getActivity()).saveLogin(userField.getText().toString(), passwordField.getText().toString());
		
		String url = this.getString(R.string.remote_login);
		ServiceHelper.execute(this.getActivity(), mReceiver, LOGIN_TOKEN, url);
	}

	public void onReceiveResult(int resultCode, Bundle resultData) {
		Log.d(TAG, "onReceiveResult(resultCode=" + resultCode + ", resultData=" + resultData.toString());  
		
		switch(resultCode) {
		case SyncService.STATUS_RUNNING:
			mProgressDialog = ProgressDialog.show(getActivity(),"", getString(R.string.progress_login_message), true);
			break;
		case SyncService.STATUS_FINISHED:
			Log.d(TAG, "Login was OK!");
			mProgressDialog.dismiss();
			Intent intent = new Intent(getActivity(), RecentOrdersActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			getActivity().finish();
			break;
		case SyncService.STATUS_ERROR:
			Log.d(TAG, "Login did not succeed.");
			((BaseActivity)getActivity()).saveLogin(null, null);
			mProgressDialog.dismiss();
			Toast.makeText(getActivity(), getString(R.string.login_error), Toast.LENGTH_LONG).show();
			break;
		}
		
	}

	public void registerReceiver() {
		mReceiver = new MyResultReceiver(new Handler());
		mReceiver.setReceiver(this);
		
	}

}
