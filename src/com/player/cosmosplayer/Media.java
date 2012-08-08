package com.player.cosmosplayer;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class Media extends ListActivity
{
	Cursor cursor;
	final int STATE_MEDIA = 0;
	final int STATE_SONG  = 1;
	
	
	int currentState = STATE_MEDIA;
	int key;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.place);
		
		Bundle bundle = getIntent().getExtras();
		key = bundle.getInt("KEY");
		String[] columns = null;
		String[] displayField = null;
		int[] displayViews = new int[] {android.R.id.text1};
		
		if(key == 0)
		{
					columns = new String[] {MediaStore.Audio.Media.DATA,
					MediaStore.Audio.Media.TITLE,
					MediaStore.Audio.Media._ID,
					MediaStore.Audio.Media.DISPLAY_NAME,
					MediaStore.Audio.Media.MIME_TYPE,
					MediaStore.Audio.Media.ALBUM,
					MediaStore.Audio.Media.DURATION
					};
					displayField = new String[] {MediaStore.Audio.Media.DISPLAY_NAME};
					cursor = managedQuery(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,columns,null,null,null);
					currentState = STATE_SONG;
		}
		if(key == 1)
		{
			columns = new String[] {MediaStore.Audio.Media._ID,
					MediaStore.Audio.Media.ALBUM};
			displayField = new String[] {MediaStore.Audio.Albums.ALBUM};
			cursor = managedQuery(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,columns, null,null,null);
		}
		
		if(key == 2)
		{
			columns = new String[]
					{
						MediaStore.Audio.Media._ID,
						MediaStore.Audio.GenresColumns.NAME};
			displayField = new String[] {MediaStore.Audio.Genres.NAME};
			cursor = managedQuery(MediaStore.Audio.Genres.EXTERNAL_CONTENT_URI,columns,null,null,null);
		}
		
		if(key == 3)
		{
			columns = new String[]{MediaStore.Audio.Media._ID,
					MediaStore.Audio.Media.ARTIST};
			displayField = new String[] {MediaStore.Audio.Artists.ARTIST};
			cursor = managedQuery(MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI,columns, null,null,null);
		}
		setListAdapter(new SimpleCursorAdapter(this,android.R.layout.simple_list_item_1, cursor, displayField, displayViews));
	}
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) 
	{
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		if(currentState == STATE_MEDIA)
		{
			if(cursor.moveToPosition(position))
			{
				String[] columns = new String[] {
						  MediaStore.Audio.Media.DATA,
						  MediaStore.Audio.Media._ID,
						  MediaStore.Audio.Media.TITLE,
						  MediaStore.Audio.Media.DISPLAY_NAME,
						  MediaStore.Audio.Media.MIME_TYPE,
						  MediaStore.Audio.Media.ALBUM,
						  MediaStore.Audio.Media.DURATION
						  };
				String where = null;
				String [] whereArgs = null;
				if(key == 1)
				{
					where = android.provider.MediaStore.Audio.Media.ALBUM + "=?";
					whereArgs = new String[] {cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM))};
				}
				if(key == 2)
				{
					where = MediaStore.Audio.Genres.Members._ID + "=?";
					whereArgs = new String[] {cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Genres.Members._ID))};
				}
				if(key == 3)
				{
					where = android.provider.MediaStore.Audio.Media.ARTIST+ "=?";
					whereArgs = new String[] {cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Artists.ARTIST))};
				}		
				String orderBy = MediaStore.Audio.Media.TITLE;
				cursor = managedQuery(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,columns, where, whereArgs,orderBy);
				String[] fieldDisplay = new String[] {MediaStore.Audio.Media.DISPLAY_NAME};
				int[] fieldView = {android.R.id.text1};						
				setListAdapter(new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, cursor, fieldDisplay,fieldView));
				currentState = STATE_SONG;
			}
		}else
			if(currentState == STATE_SONG)
			{
				
				if(cursor.moveToPosition(position))
				{	
					playMedia(position);
				}
			}
	}
	public void playMedia(int position)
	{
		int count = cursor.getCount();
		String[] data = new String[count];
		String[] song = new String[count];
		String[] albums = new String[count];
		cursor.moveToFirst();
		do
		{
			int pos = cursor.getPosition();
			data [pos] = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
			song [pos] = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
			albums[pos] = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
		}while(cursor.moveToNext());
		
		Intent intent = new Intent(this,AudioActivity.class);
		intent.putExtra("key",position);
		intent.putExtra("display", song);
		intent.putExtra("data",data);
		intent.putExtra("albums", albums);
		startActivity(intent);				
	}
}
