package com.junova.huizhong.fragment;

import com.junova.huizhong.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SettingFragment extends Fragment{
	private View v;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		 v = inflater.inflate(R.layout.fragment_set, container, false);
		initViews(v);
		// Inflate the layout for this fragment
		return v;
	}

	public void initViews(View v) {}
}
