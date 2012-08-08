package com.player.cosmosplayer;

import java.io.IOException;
import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class AudioActivity extends Activity 
{
	ImageButton play;
	String[] data;
	String[] display;
	String[] albums;
	TextView song;
	TextView album;
	MediaPlayer player = new MediaPlayer();
	Chronometer chr1,chr2;
	SeekBar seek;
	int key;
	private final Handler handler = new Handler();
	boolean PLAY = false;
	boolean REPEAT = false;
	

	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.window);
		
		
		key = getIntent().getIntExtra("key", 0);
		data = getIntent().getStringArrayExtra("data");
		display = getIntent().getStringArrayExtra("display");
		albums = getIntent().getStringArrayExtra("albums");
		
		song = (TextView)findViewById(R.id.song);
		song.setText(display[key]);
		album = (TextView)findViewById(R.id.album);
		album.setText(albums[key]);
		play = (ImageButton)findViewById(R.id.play);
		try {
			player.setDataSource(data[key]);
			player.prepare();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		seek = (SeekBar)findViewById(R.id.seek);
		chr1 = (Chronometer)findViewById(R.id.chr1);
		chr2 = (Chronometer)findViewById(R.id.chr2);
		chr2.setBase(SystemClock.elapsedRealtime()-player.getDuration());	
		seek.setMax(player.getDuration());
		seek.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if(player.isPlaying())
				{
					SeekBar bar = (SeekBar)v;
					player.seekTo(bar.getProgress());
					chr1.setBase(SystemClock.elapsedRealtime() - player.getCurrentPosition());
				}
				return false;
			}
		});
		
		
		play.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!player.isPlaying())
				{
					play.setImageResource(R.drawable.pause);
					player.start();
					PLAY = true;
				}
				else
				{
					play.setImageResource(R.drawable.play);
					player.pause();
					PLAY = false;
				}
				playProgressUpdater();
					
			}
		});
		ImageButton previouse = (ImageButton)findViewById(R.id.prev);
		previouse.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onPreviouse();
			}
		});
		ImageButton next = (ImageButton)findViewById(R.id.next);
		next.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onNext();
			}
		});
		ImageButton rev = (ImageButton)findViewById(R.id.rev);
		rev.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ImageButton res = (ImageButton)v;
				if(!REPEAT)
				{
					
					res.setImageResource(R.drawable.repeat);
					REPEAT = !REPEAT;
					Toast.makeText(getApplicationContext(),"Repeat track: on",Toast.LENGTH_SHORT).show();
				}
				else
				{
					res.setImageResource(R.drawable.socialforward);
					REPEAT = !REPEAT;
					Toast.makeText(getApplicationContext(),"Repeat track: off",Toast.LENGTH_SHORT).show();
				}
				
			}
		});
		
	}
	protected void onNext()
	{
		if(!REPEAT)
		{
			if(key!=data.length-1)
			{
				key++;
				playMedia();
			}else
			{
				stopTrack();
			}
		}
		else
		{
			repeatTrack();
		}
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		player.release();
		player=null;
	}
	protected void onPreviouse()
	{
		if(!REPEAT)
		{
			if(key!=0)
			{
				key--;
				playMedia();
				
			}else
			{
				stopTrack();
			}
		}
		else
		{
			repeatTrack();
		}
			
	}
	protected void repeatTrack()
	{
		player.seekTo(0);
		if(PLAY)
		{
			player.start();
			play.setImageResource(R.drawable.pause);
			playProgressUpdater();
		}
		else
		{
			seek.setProgress(0);
			chr1.setBase(SystemClock.elapsedRealtime());
		}
	}
	protected void stopTrack()
	{
		player.seekTo(0);
		seek.setProgress(0);
		chr1.setBase(SystemClock.elapsedRealtime());
		if(player.isPlaying())
		{
			play.setImageResource(R.drawable.pause);
		}else
		{
			play.setImageResource(R.drawable.play);
		}
	}
	protected void playProgressUpdater()
	{
		if(player!=null)
		{
		
			if(player.isPlaying())
			{
				seek.setProgress(player.getCurrentPosition());
				chr1.setBase(SystemClock.elapsedRealtime() - player.getCurrentPosition());
				Runnable notif = new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					playProgressUpdater();
				}
			};
				handler.postDelayed(notif,1000);
			}
			else
			{
				if(PLAY)
				{
					onNext();
				}
			}
		}
	}
	protected void playMedia()
	{
		player.reset();
		try {
			player.setDataSource(data[key]);
			player.prepare();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		seek.setMax(player.getDuration());	
		chr2.setBase(SystemClock.elapsedRealtime()-player.getDuration());	
		song.setText(display[key]);
		album.setText(albums[key]);
		if(PLAY)
		{
			player.start();
			play.setImageResource(R.drawable.pause);
			playProgressUpdater();
		}
		else
		{
			seek.setProgress(0);
			chr1.setBase(SystemClock.elapsedRealtime());
		}
	}
}