package com.example.soundmaze;

import java.util.ArrayList;
import java.util.List;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import android.content.ContentValues;
import android.content.Context;


public class TableHelper {
	Context _ctxt;
	boolean delete=true;
	protected boolean update;
	public TableHelper(Context context) { 
	
		_ctxt=context;
		Parse.initialize(_ctxt, "jt3CvTBVI3BUluWcxL8AgBzMtrhJEWwhnRSTqwrD", "x82a1ESNRRwLBDsRdmkd0P9N7aq2OOevxehB2vGx");
	}
	public boolean insert(User user) { 
		if(user.getName()==null){
			return false;
		}
		ParseObject parseObject = new ParseObject("users");
		parseObject.put("name", user.getName());
		
		
		ContentValues values = new ContentValues();
		values.put("name", user.getName());
			parseObject.put("score", user.getScore());
			values.put("name", user.getScore());
		parseObject.saveInBackground();
		return true;
	}

	public boolean update(User user) {

		final User t=user;
		ParseQuery<ParseObject> query = new ParseQuery("users");
		query.whereEqualTo("name", user.getName());
		query.addAscendingOrder("score");
		update=true;
		query.findInBackground(new FindCallback() {


			@Override
			public void done(List objects, ParseException arg1) {
				if (arg1!=null){
					arg1.printStackTrace();
				}
				else{
					if(objects.isEmpty()){
						update=false;
						return ;
					}
			
					for(Object obj : objects){
					
						ParseObject obj1=(ParseObject) obj;
						obj1.put("score",t.getScore());
						obj1.saveInBackground();
						update=true;
					}
				}
			}
		});

		return update;

	}
	
	public List<User> all() { 
		ParseQuery query = new ParseQuery("users");
		List<User> list = new ArrayList<User>();
		query.addDescendingOrder("score");
		try {
			List res=query.find();
			for (int j = 0; j < 5; j++) {
				ParseObject i=(ParseObject)res.get(j);
				list.add(new User(i.getString("name"),i.getInt("score")));
			}
			

		} catch (ParseException e) {
			e.printStackTrace();
		}

		return list;
	}
}
