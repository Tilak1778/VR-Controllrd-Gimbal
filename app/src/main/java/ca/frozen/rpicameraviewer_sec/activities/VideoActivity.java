// Copyright © 2016-2018 Shawn Baker using the MIT License.
package ca.frozen.rpicameraviewer_sec.activities;

import android.content.Intent;
import android.hardware.SensorEventListener;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import ca.frozen.library.classes.Log;
import ca.frozen.rpicameraviewer_sec.classes.Camera;
import ca.frozen.rpicameraviewer_sec.classes.Utils;
import ca.frozen.rpicameraviewer_sec.R;

import com.google.vr.ndk.base.GvrLayout;
import com.google.vr.sdk.base.*;

public class VideoActivity extends AppCompatActivity implements VideoFragment.OnFadeListener
{
	// public constants
	public final static String CAMERA = "camera";

	// instance variables
	private FrameLayout frameLayout;
	private VideoFragment videoFragment;
	private  VideoFragment videoFragment1;
	private GvrLayout gvrLayout;
	private GvrView gvrView;

	//Button vr_Button;
	Button Reset_button;

	sendData sendData_obj;

	//******************************************************************************
	// onCreate
	//******************************************************************************
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// configure the activity
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video);

		// initialize the logger
		Utils.initLogFile(getClass().getSimpleName());

		// load the settings and cameras
		Utils.loadData();

		// get the camera object
		Bundle data = getIntent().getExtras();
		Camera camera = data.getParcelable(CAMERA);
		Log.info("camera: " + camera.toString());

		// get the frame layout, handle system visibility changes
		frameLayout = findViewById(R.id.video);
		gvrView=findViewById(R.id.gvr_view);
		sendData_obj=new sendData();


/*
		vr_Button=findViewById(R.id.vr_button);
		vr_Button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				///gvrView.setStereoModeEnabled(true);
				//gvrView.setAsyncReprojectionEnabled(true);

                Intent intent=new Intent(VideoActivity.this,vr_VideoActivity.class);
                startActivity(intent);


			}
		});

*/

		Reset_button=findViewById(R.id.reset_button);
		Reset_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendData_obj.aPitch=0;
                sendData_obj.aRoll=0;
                sendData_obj.aYaw=0;

            }
        });



		frameLayout.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener()
		{
			@Override
			public void onSystemUiVisibilityChange(int visibility)
			{
				if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0)
				{
					//videoFragment.startFadeIn();
					//videoFragment1.startFadeIn();
				}
			}
		});

		// set full screen layout
		int visibility = frameLayout.getSystemUiVisibility();
		visibility |= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
		frameLayout.setSystemUiVisibility(visibility);

		// create the video fragment
		videoFragment = VideoFragment.newInstance(camera, true);
		videoFragment1 = VideoFragment.newInstance(camera,true);


		FragmentTransaction fragTran = getSupportFragmentManager().beginTransaction();
		//FragmentTransaction fragTran1 = getSupportFragmentManager().beginTransaction();

		fragTran.add(R.id.gvr_view, videoFragment);
		fragTran.add(R.id.gvr_view2,videoFragment1);
		//fragTran.add(R.id.video1,videoFragment);
		//fragTran.attach(videoFragment2);


		//fragTran.add(R.id.video,videoFragment1);
		fragTran.commit();
		//fragTran1.commit();
	}

	//******************************************************************************
	// onStartFadeIn
	//******************************************************************************
	@Override
	public void onStartFadeIn()
	{
	}

	//******************************************************************************
	// onStartFadeOut
	//******************************************************************************
	@Override
	public void onStartFadeOut()
	{
		// hide the status and navigation bars
		int visibility = frameLayout.getSystemUiVisibility();
		visibility |= View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
		frameLayout.setSystemUiVisibility(visibility);
	}

	//******************************************************************************
	// onBackPressed
	//******************************************************************************
	@Override
	public void onBackPressed()
	{
		videoFragment.stop();
		//videoFragment2.stop();
		super.onBackPressed();
	}
}
