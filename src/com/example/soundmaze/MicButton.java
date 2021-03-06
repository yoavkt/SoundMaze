package com.example.soundmaze;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;

public class MicButton extends ImageButton {

	private Drawable mDrawableMic;
	private Drawable mDrawableMicTranscribing;

	private List<Drawable> mVolumeLevels;

	private Animation mAnimFadeInOutInf;

	private int mVolumeLevel = 0;

	public MicButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initAnimations(context);
	}

	public MicButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		initAnimations(context);
	}


	public MicButton(Context context) {
		super(context);
		initAnimations(context);
	}


	public void setState(Constants.State state) {
		switch(state) {
		case INIT:
			clearAnimation();
			setBackgroundDrawable(mDrawableMic);
			break;
		case RECORDING:
			setBackgroundDrawable(mVolumeLevels.get(0));
			break;
		case LISTENING:
			break;
		case TRANSCRIBING:
			setBackgroundDrawable(mDrawableMicTranscribing);
			startAnimation(mAnimFadeInOutInf);
			break;
		case ERROR:
			clearAnimation();
			setBackgroundDrawable(mDrawableMic);
			break;
		default:
			break;
		}
	}


	public void setVolumeLevel(float rmsdB) {
		float min = 15.f;
		float max = 30.f;
		final int maxLevel = mVolumeLevels.size() - 1;

		int index = (int) ((rmsdB - min) / (max - min) * maxLevel);
		int level = Math.min(Math.max(0, index), maxLevel);
		if (level != mVolumeLevel) {
			mVolumeLevel = level;
			setBackgroundDrawable(mVolumeLevels.get(level));
		}
	}


	private void initAnimations(Context context) {
		Resources res = getResources();
		mDrawableMic = res.getDrawable(R.drawable.button_mic);
		mDrawableMicTranscribing = res.getDrawable(R.drawable.button_mic_transcribing);

		mVolumeLevels = new ArrayList<Drawable>();
		mVolumeLevels.add(res.getDrawable(R.drawable.button_mic_recording_0));
		mVolumeLevels.add(res.getDrawable(R.drawable.button_mic_recording_1));
		mVolumeLevels.add(res.getDrawable(R.drawable.button_mic_recording_2));
		mVolumeLevels.add(res.getDrawable(R.drawable.button_mic_recording_3));

		AnimationUtils.loadAnimation(context, R.anim.fade_in);
		AnimationUtils.loadAnimation(context, R.anim.fade_out);
		mAnimFadeInOutInf = AnimationUtils.loadAnimation(context, R.anim.fade_inout_inf);
	}
}
