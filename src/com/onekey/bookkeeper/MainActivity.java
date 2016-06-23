package com.onekey.bookkeeper;

import org.kymjs.kjframe.KJActivity;

import android.os.Bundle;

public class MainActivity extends KJActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

	@Override
	public void setRootView() {
        setContentView(R.layout.activity_main);
	}

}
