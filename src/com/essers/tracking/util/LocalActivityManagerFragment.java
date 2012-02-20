package com.essers.tracking.util;

import android.app.LocalActivityManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;

@SuppressWarnings("deprecation")
public class LocalActivityManagerFragment extends Fragment {

    private static final String KEY_STATE_BUNDLE = "localActivityManagerState";
    
    private LocalActivityManager mLocalActivityManager;
    
    
    protected LocalActivityManager getLocalActivityManager() {
        return mLocalActivityManager;
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Bundle state = null;
        if(savedInstanceState != null) {
            state = savedInstanceState.getBundle(KEY_STATE_BUNDLE);
        }
        
        mLocalActivityManager = new LocalActivityManager(getActivity(), true);
        mLocalActivityManager.dispatchCreate(state);
    }
    
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBundle(KEY_STATE_BUNDLE, mLocalActivityManager.saveInstanceState());
    }
    
    @Override
    public void onResume() {
        super.onResume();
        mLocalActivityManager.dispatchResume();
    }
    
    @Override
    public void onPause() {
        super.onPause();
        mLocalActivityManager.dispatchPause(getActivity().isFinishing());
    }    
    
    @Override
    public void onStop() {
        super.onStop();
        mLocalActivityManager.dispatchStop();
    }
    
    @Override
    public void onDestroy() {        
        super.onDestroy();        
        mLocalActivityManager.dispatchDestroy(getActivity().isFinishing());
    }
}
