package com.player.cosmosplayer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Db {
	
	public static final String ID  = "_id";
	public static final String IMG = "img";
	public static final String DATA = "txt";
	
	String [] names = new String[] {"All tracks","Albums","Genres","Artists"};
	int [] images = {R.drawable.refresh,R.drawable.important,R.drawable.mic,R.drawable.group};
	
	
	MyOpenHelper openHelp;
	SQLiteDatabase db;
	Context ctx;
	
	public Db(Context _ctx) {
		// TODO Auto-generated constructor stub
		ctx = _ctx;
	}
	void open()
	{
		openHelp = new MyOpenHelper(ctx);
		db = openHelp.getWritableDatabase();
	}
	void close()
	{
		openHelp.close();
	}
	public Cursor getAllData()
	{
		return db.query("myDataBase",null,null,null,null,null,null);
	}
	class MyOpenHelper extends SQLiteOpenHelper
	{

		public MyOpenHelper(Context context) 
		{
			super(context,"myDataBase", null, 1);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onCreate(SQLiteDatabase db) 
		{
			// TODO Auto-generated method stub
			db.execSQL("create table myDataBase ( _id integer primary key autoincrement, img integer, txt text );");
			for(int i=0;i<names.length;i++)
			{
				ContentValues cv = new ContentValues();
				cv.put(IMG, images[i]);
				cv.put(DATA, names[i]);
				db.insert("myDataBase",null, cv);
			}
			
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			
		}
		
	}

}
