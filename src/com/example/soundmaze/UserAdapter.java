package com.example.soundmaze;


import java.util.List;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class UserAdapter extends ArrayAdapter<User> {
	public UserAdapter(
			TopScoresActivity activity, List<User> courses) {
		super(activity, android.R.layout.simple_list_item_1, courses);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		User user = getItem(position);
		LayoutInflater inflater = LayoutInflater.from(getContext());
		View view = inflater.inflate(R.layout.userlayout, null);
		TextView txtName = (TextView)view.findViewById(R.id.txtName);
		txtName.setText(user.getName());
		TextView txtScore = (TextView)view.findViewById(R.id.txtScore);
		txtScore.setText(String.valueOf(user.getScore()));
		return view;
	}
}