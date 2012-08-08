package com.player.cosmosplayer;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class PlayerActivity extends Activity 
	implements OnItemClickListener


{
	Db db;
	SimpleCursorAdapter adapter;
	ListView list;
	Cursor c;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cosmos_player);
        
        list = (ListView)findViewById(R.id.list1);
        
        db = new Db(this);
        db.open();
        c = db.getAllData();
        
        String [] from = new String[] {Db.DATA, Db.IMG};
        int [] to = {R.id.text,R.id.img};
        
        list.setOnItemClickListener(this);
        adapter = new SimpleCursorAdapter(this,R.layout.item, c, from, to);
        list.setAdapter(adapter);
        
    } 
    @Override
    protected void onDestroy() {
    	// TODO Auto-generated method stub
    	super.onDestroy();
    	c.close();
    	db.close();
    }
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.setClass(this,Media.class);
		intent.putExtra("KEY",arg2);
		startActivity(intent);
	}


}


