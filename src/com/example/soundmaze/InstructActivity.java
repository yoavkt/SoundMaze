package com.example.soundmaze;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.speech.RecognitionListener;
import android.speech.RecognitionService;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.View;
import android.widget.TextView;

import com.example.soundmaze.Constants.State;



public class InstructActivity extends Activity {



	public static final int LISTENING_TIMEOUT = 1000;
	private State mState = State.INIT;
	private SharedPreferences mPrefs;
	private MicButton mButtonMicrophone;
	private SpeechRecognizer mSr;
	private TextView mTvFeedback;
	MazeMaster myMazeMaster;
	TableHelper th;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_instruct);
		TextView text1=(TextView)findViewById(R.id.colorshelp);
		TextView text3=(TextView)findViewById(R.id.textViewInstruct);
		Typeface face = Typeface.createFromAsset(getAssets(),
				"Chunkfive.otf");
		text3.setTypeface(face);
		text1.setTypeface(face);
		mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		mTvFeedback = (TextView) findViewById(R.id.tvFeedback);
		mButtonMicrophone = (MicButton) findViewById(R.id.buttonMicrophone);

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
				sr.stopListening();
				ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
				mState = State.INIT;
				int dir;
				if (!matches.isEmpty()) {
					String result = matches.iterator().next();
					dir=Utils.phraseDistances(result);
				}
				else 
					dir=0;
					mTvFeedback.setText( trainigDialogue.correctProgress(dir));
					mTvFeedback.setVisibility(View.VISIBLE);
				
			}

			@Override
			public void onRmsChanged(float rmsdB) {
				mButtonMicrophone.setVolumeLevel(rmsdB);
			}
		});
		sr.startListening(intentRecognizer);
	}
	static class trainigDialogue {
		private static int ups=2;
		private static int rights=2;
		private static int downs=2;
		private static int lefts=2;
		
		private static String nextMessage(String dir,int val)
		{
			switch(val){
			case 1:
				return "Very good ! say one more time";
			case 0:
				return "O.K now say "+dir;
			default:
				return "go & play!";
			}
		}
		public static String correctProgress(int code){
			if (ups>0)
				if (code==1)
				{
					ups=ups-1;
					return nextMessage("down",ups);
					
				}
				else return "Try to say up again!";
			if (downs>0)
				if (code==2)
				{
					downs=downs-1;
					return nextMessage("left",downs);
				}
				else return "Try to say down";
			if (lefts>0)
				if (code==3)
				{
					lefts=lefts-1;
					return nextMessage("right",lefts);
				}
				else return "Try to say left";
			if (rights>0)
				if (code==4)
				{
					rights=rights-1;
					return nextMessage("hh",8);
				}
				else return "Try to say right";
			return "Now you are ready!";
		}
	}
	
}





