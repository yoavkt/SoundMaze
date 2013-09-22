package com.example.soundmaze;


import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class UserAdapter extends ArrayAdapter<User> {
	Context cnt;
	public UserAdapter(
			TopScoresActivity activity, List<User> courses) {
		
		super(activity, android.R.layout.simple_list_item_1, courses);
		cnt=activity;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Typeface face = Typeface.createFromAsset(cnt.getAssets(),
				"Chunkfive.otf");
		
		User user = getItem(position);
		LayoutInflater inflater = LayoutInflater.from(getContext());
		View view = inflater.inflate(R.layout.userlayout, null);
		TextView txtName = (TextView)view.findViewById(R.id.txtName);
		txtName.setTypeface(face);
		txtName.setText(user.getName());
		TextView txtScore = (TextView)view.findViewById(R.id.txtScore);
		txtScore.setTypeface(face);
		txtScore.setText(String.valueOf(user.getScore()));
		return view;
	}
}