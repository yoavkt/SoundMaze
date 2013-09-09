package com.example.soundmaze;

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
import android.widget.TextView;

import com.example.soundmaze.Constants.State;



public class InstructActivity extends Activity {



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
		setContentView(R.layout.activity_instruct);
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
				handler.removeCallbacks(stopListening);
				ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
				mState = State.INIT;
				if (!matches.isEmpty()) {
					String result = matches.iterator().next();

					int dir=Utils.phraseDistances(result);

					mTvFeedback.setText( "going "+dir);
					mTvFeedback.setVisibility(View.VISIBLE);
				}
				//						Intent intentRecognizer = createRecognizerIntent();
				//						sr.startListening(intentRecognizer);
			}

			@Override
			public void onRmsChanged(float rmsdB) {
				mButtonMicrophone.setVolumeLevel(rmsdB);
			}
		});
		sr.startListening(intentRecognizer);
	}
}





