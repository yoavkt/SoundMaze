package com.example.soundmaze;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;


import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.speech.RecognitionListener;
import android.speech.RecognitionService;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.soundmaze.Constants.State;





public class MazeActivity extends Activity {

	Maze stageMaze;


	public static final int LISTENING_TIMEOUT = 1000;
	private State mState = State.INIT;
	private SharedPreferences mPrefs;
	private MicButton mButtonMicrophone1;
	private SpeechRecognizer mSr;
	//	private TextView mTvFeedback;
	MazeMaster myMazeMaster;
	//tamar
	TableHelper th;



	MazeView myMazeView;
	TextView textMove;
	TextView textScore;


	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_maze);
		Intent in= this.getIntent();
		Maze m1 = (Maze)in.getParcelableExtra("maze");
		myMazeView = (MazeView) this.findViewById(R.id.mazeView);
		myMazeView.setMaze(m1);
		myMazeView.startMazeHeroMode();
		textMove = (TextView) findViewById(R.id.textMove);
		textScore = (TextView) findViewById(R.id.textScore);
		mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		mButtonMicrophone1 = (MicButton) findViewById(R.id.buttonMicrophone1);
		
		Button back=(Button)findViewById(R.id.buttonBack);
		back.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
		
		
		ComponentName serviceComponent = getServiceComponent();
		if (serviceComponent != null) {
			mSr = SpeechRecognizer.createSpeechRecognizer(this, serviceComponent);
			if (mSr != null) {
				setUpRecognizerGui(mSr);
			}
		}


	}



	/**
	 * We initialize the speech recognizer here, assuming that the configuration
	 * changed after onStop. That is why onStop destroys the recognizer.
	 */
	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onStop() {
		super.onStop();

		if (mSr != null) {
			mSr.cancel(); 
			mSr.destroy();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mSr != null) {
			mSr.destroy();
			mSr = null;
		}
	}
	@Override
	protected void onResume() {
		super.onResume();

	}

	private Intent createRecognizerIntent() {
		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getApplicationContext().getPackageName());
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
		intent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en");
		intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
		return intent;
	}

	@Override
	 public void onBackPressed() {
		 finish();
		 return;
		 }

	private void setUpRecognizerGui(final SpeechRecognizer sr) {
		mButtonMicrophone1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (mState == State.INIT || mState == State.ERROR) {
					listenSentence(sr,  true);
				} else if (mState == State.LISTENING) {
					sr.stopListening();
				}
			}
		});


	}


	private void listenSentence(SpeechRecognizer sr, boolean isStorePhrase) {
		startListening(sr, isStorePhrase);
	}


	/**
	 * Look up the default recognizer service in the preferences.
	 * If the default have not been set then set the first available
	 * recognizer as the default. If no recognizer is installed then
	 * return null.
	 */
	private ComponentName getServiceComponent() {
		String pkg = mPrefs.getString(getString(R.string.keyService), null);
		String cls = mPrefs.getString(getString(R.string.prefRecognizerServiceCls), null);
		if (pkg == null || cls == null) {
			List<ResolveInfo> services = getPackageManager().queryIntentServices(
					new Intent(RecognitionService.SERVICE_INTERFACE), 0);
			if (services.isEmpty()) {
				return null;
			}
			ResolveInfo ri = services.iterator().next();
			pkg = ri.serviceInfo.packageName;
			cls = ri.serviceInfo.name;
			SharedPreferences.Editor editor = mPrefs.edit();
			editor.putString(getString(R.string.keyService), pkg);
			editor.putString(getString(R.string.prefRecognizerServiceCls), cls);
			editor.commit();
		}
		return new ComponentName(pkg, cls);
	}


	private void startListening(final SpeechRecognizer sr, final boolean isStorePhrase) {
//		Intent intentRecognizer = createRecognizerIntent();

		final Runnable stopListening = new Runnable() {
			@Override
			public void run() {
				sr.stopListening();
			}
		};
		final Handler handler = new Handler();

		sr.setRecognitionListener(new RecognitionListener() {

			@Override
			public void onBeginningOfSpeech() {
				mState = State.LISTENING;
			}

			@Override
			public void onBufferReceived(byte[] buffer) {
			}

			@Override
			public void onEndOfSpeech() {
				mState = State.TRANSCRIBING;
				handler.removeCallbacks(stopListening);
				mButtonMicrophone1.setState(mState);
			}

			@Override
			public void onError(int error) {
				mState = State.ERROR;
				handler.removeCallbacks(stopListening);
				mButtonMicrophone1.setState(mState);
			}

			@Override
			public void onEvent(int eventType, Bundle params) {
			}

			@Override
			public void onPartialResults(Bundle partialResults) {

			}

			@Override
			public void onReadyForSpeech(Bundle params) {
				mState = State.RECORDING;
				mButtonMicrophone1.setState(mState);
				handler.postDelayed(stopListening, LISTENING_TIMEOUT);
			}

			@Override
			public void onResults(Bundle results) {
				sr.stopListening();
				ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
				mState = State.INIT;
				if (!matches.isEmpty()) {
					String result = matches.iterator().next();
					String moveResult="Ball heard:";
					int dir=Utils.phraseDistances(result);
					int move=0;
					switch (dir) {
					case 1:
						moveResult+="up";
						move=KeyEvent.KEYCODE_DPAD_UP;
						break;
					case 2:
						moveResult+="down";
						move=KeyEvent.KEYCODE_DPAD_DOWN;
						break;
					case 3:
						moveResult+="left";
						move=KeyEvent.KEYCODE_DPAD_LEFT;
						break;
					case 4:
						moveResult+="right";
						move=KeyEvent.KEYCODE_DPAD_RIGHT;
						break;
					default:
						moveResult+="illegal move";
						move=999;
						break;
					}
					textMove.setText(moveResult);
					myMazeView.movementUpdater(move);
					//TODO here is set the score
					textScore.setText(myMazeView.get_score());
					
				}
				sr.startListening(createRecognizerIntent());
			}

			@Override
			public void onRmsChanged(float rmsdB) {
				mButtonMicrophone1.setVolumeLevel(rmsdB);
			}
		});
		sr.startListening(createRecognizerIntent());
	}
}






