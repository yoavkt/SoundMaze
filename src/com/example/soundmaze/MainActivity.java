package com.example.soundmaze;

import java.io.IOException;
import java.util.Date;

import org.json.JSONException;

import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseAnalytics;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognitionService;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.View;
import android.widget.TextView;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.preference.PreferenceManager;
import com.example.soundmaze.Constants.State;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {

	public static final int LISTENING_TIMEOUT = 2000;
	private State mState = State.INIT;
	private SharedPreferences mPrefs;
	private MicButton mButtonMicrophone;
	private SpeechRecognizer mSr;
	private TextView mTvFeedback;
	MazeMaster myMazeMaster;
	//tamar
	TableHelper th;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);


		th=new TableHelper(this);
		//	Intent intent = new Intent(this, AddUserActivity.class);
		//	startActivityForResult(intent, 1337);
		//


		try {
			myMazeMaster=new MazeMaster(this.getApplicationContext());

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		mTvFeedback = (TextView) findViewById(R.id.tvFeedback);
		mButtonMicrophone = (MicButton) findViewById(R.id.buttonMicrophone);

		final Button btnMaze1 = (Button) findViewById(R.id.btnMainMaze1);
		btnMaze1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent myIntent = new Intent(MainActivity.this,
						MazeActivity.class);
				//Bundle bundle= new Bundle();

				//bundle.putSerializable("maze", myMazeMaster.getMaze("Maze 1"));
				//myIntent.putExtras(bundle);
				MainActivity.this.startActivity(myIntent);
			}
		});


		final Button btnMaze2 = (Button) findViewById(R.id.btnMainMaze2);
		btnMaze2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent myIntent = new Intent(MainActivity.this,
						MazeActivity.class);
				//Bundle bundle= new Bundle();
				//Maze m=myMazeMaster.getMaze("Maze 2");
				//bundle.putSerializable("maze", myMazeMaster.getMaze("Maze 2"));
				//myIntent.putExtras(bundle);
				MainActivity.this.startActivity(myIntent);


			}
		});

		final Button btnMainTopScores = (Button) findViewById(R.id.btnMainTopScores);
		btnMainTopScores.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent myIntent = new Intent(MainActivity.this,
						TopScoresActivity.class);
				MainActivity.this.startActivity(myIntent);
			}
		});
		final Button btnMainInst = (Button) findViewById(R.id.btnMainInst);
		btnMainInst.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent myIntent = new Intent(MainActivity.this,
						InstructActivity.class);
				MainActivity.this.startActivity(myIntent);
			}
		});

	}
	//tamar
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1337 && resultCode == RESULT_OK) {
			String userName = data.getStringExtra("name");
			th.insert(new User(userName,222));

		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}







	/**
	 * We initialize the speech recognizer here, assuming that the configuration
	 * changed after onStop. That is why onStop destroys the recognizer.
	 */
	@Override
	public void onStart() {
		super.onStart();

		ComponentName serviceComponent = getServiceComponent();

		if (serviceComponent != null) {
			mSr = SpeechRecognizer.createSpeechRecognizer(this, serviceComponent);
			if (mSr != null) {
				setUpRecognizerGui(mSr);
			}
		}


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

	private Intent createRecognizerIntent() {
		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getApplicationContext().getPackageName());
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
		intent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en");
		intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
		return intent;
	}


	private void setUpRecognizerGui(final SpeechRecognizer sr) {
		mButtonMicrophone.setOnClickListener(new View.OnClickListener() {
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
		Intent intentRecognizer = createRecognizerIntent();

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
				mButtonMicrophone.setState(mState);
			}

			@Override
			public void onError(int error) {
				mState = State.ERROR;
				handler.removeCallbacks(stopListening);
				mButtonMicrophone.setState(mState);
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
				mButtonMicrophone.setState(mState);
				handler.postDelayed(stopListening, LISTENING_TIMEOUT);
			}

			@Override
			public void onResults(Bundle results) {
				handler.removeCallbacks(stopListening);
				ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
				mState = State.INIT;
				if (!matches.isEmpty()) {
					String result = matches.iterator().next();

					int dir=Utils.phraseDistances(result);

					mTvFeedback.setText( "going "+dir);
					mTvFeedback.setVisibility(View.VISIBLE);
				}
//				Intent intentRecognizer = createRecognizerIntent();
//				sr.startListening(intentRecognizer);
			}

			@Override
			public void onRmsChanged(float rmsdB) {
				mButtonMicrophone.setVolumeLevel(rmsdB);
			}
		});
		sr.startListening(intentRecognizer);
	}
}


