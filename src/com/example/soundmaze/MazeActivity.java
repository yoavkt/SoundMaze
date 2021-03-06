package com.example.soundmaze;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import android.content.ComponentName;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.graphics.Typeface;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.speech.RecognitionListener;
import android.speech.RecognitionService;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.soundmaze.Constants.State;

public class MazeActivity extends Activity {

	Maze stageMaze;


	public static final int LISTENING_TIMEOUT = 1000;
	private State mState = State.INIT;
	private SharedPreferences mPrefs;
	private MicButton mButtonMicrophone1;
	private SpeechRecognizer mSr;
	MazeMaster myMazeMaster;
	TableHelper th;
	MazeView myMazeView;
	TextView textMove;
	TextView textScore;
	ImageView life1;
	ImageView life2;
	ImageView life3;


	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_maze);
		Intent in= this.getIntent();
		Maze m1 = (Maze)in.getParcelableExtra("maze");
		myMazeView = (MazeView) this.findViewById(R.id.mazeView);
		myMazeView.setMaze(m1);
		textMove = (TextView) findViewById(R.id.textMove);
		textScore = (TextView) findViewById(R.id.textScore);
		mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		mButtonMicrophone1 = (MicButton) findViewById(R.id.buttonMicrophone1);
		life1=(ImageView)findViewById(R.id.ImageView01);
		life2=(ImageView)findViewById(R.id.ImageView02);
		life3=(ImageView)findViewById(R.id.ImageView03);
		TextView text2=(TextView)findViewById(R.id.textView1);
		Typeface face = Typeface.createFromAsset(getAssets(),
				"Chunkfive.otf");
		textMove.setTypeface(face);
		text2.setTypeface(face);
		textScore.setTypeface(face);

		Button guide=(Button)findViewById(R.id.giudebtn);
		guide.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent myIntent = new Intent(MazeActivity.this,
						InstructActivity.class);
				MazeActivity.this.startActivity(myIntent);
			}
		});

		Button hero=(Button)findViewById(R.id.buttonHero);
		hero.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				myMazeView.startMazeHeroMode();
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
				boolean listen=true;
				if (!matches.isEmpty()) {
					String result = matches.iterator().next();
					String moveResult="Dog heard:";
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
					changeLife(myMazeView.getLife());

					if (myMazeView.getLife()==0)
					{
						myMazeView.restartMaze();
						textScore.setText("Your Score: 0");
						Toast.makeText(getApplicationContext(), "Too bad Try Again!", Toast.LENGTH_LONG).show();
						changeLife(myMazeView.getLife());
						listen=false;
					}
					else{
						textScore.setText("Your Score: " +String.valueOf(myMazeView.get_score()));

						if (myMazeView.getWin()){
							int scoretmp=myMazeView.get_score();
							textScore.setText("Your Score: 0");
							myMazeView.restartMaze();
							Intent myIntent = new Intent(MazeActivity.this,
									AddUserActivity.class);
							myIntent.putExtra("score", scoretmp);
							MazeActivity.this.startActivity(myIntent);
							listen=false;

						}
					}

				}
				if(listen){
					sr.startListening(createRecognizerIntent());
				}
			}

			@Override
			public void onRmsChanged(float rmsdB) {
				mButtonMicrophone1.setVolumeLevel(rmsdB);
			}
		});
		sr.startListening(createRecognizerIntent());
	}

	private void changeLife(int lifes){
		switch (lifes) {
		case 2:
			life3.setVisibility(View.INVISIBLE);
			life2.setVisibility(View.VISIBLE);
			life1.setVisibility(View.VISIBLE);
			break;
		case 1:
			life3.setVisibility(View.INVISIBLE);
			life2.setVisibility(View.INVISIBLE);
			life1.setVisibility(View.VISIBLE);
			break;
		case 3:
			life3.setVisibility(View.VISIBLE);
			life2.setVisibility(View.VISIBLE);
			life1.setVisibility(View.VISIBLE);
			break;
		case 0:
			life3.setVisibility(View.INVISIBLE);
			life2.setVisibility(View.INVISIBLE);
			life1.setVisibility(View.INVISIBLE);
			break;
		default:
			break;
		}

	}
}






