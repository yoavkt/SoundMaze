package com.example.soundmaze;

import android.view.KeyEvent;


public class Utils {

	private Utils() {}

	private static final String PUNCT = "\\p{Punct}";
	private static final String SPACE = "\\p{Space}";


	
		public static int phraseDistances(String s1) {
			s1 = s1.toLowerCase().replaceAll(SPACE, "").replaceAll(PUNCT, "");
			return computeDistances(s1);
		}

	private static int computeDistances(String s1) {
		String up="up";
		String down="down";
		String left="left";
		String right="right";
		
		
		int[] costsUp = new int[up.length() + 1];
		int[] costsD = new int[down.length() + 1];
		int[] costsL = new int[left.length() + 1];
		int[] costsR = new int[right.length() + 1];
		for (int i = 0; i <= s1.length(); i++) {
			int lastValueU = i;
			int lastValueD = i;
			int lastValueL = i;
			int lastValueR = i;
			for (int j = 0; j <= up.length(); j++) {
				if (i == 0)
					costsUp[j] = j;
				else {
					if (j > 0) {
						int newValue = costsUp[j - 1];
						if (s1.charAt(i - 1) != up.charAt(j - 1))
							newValue = Math.min(Math.min(newValue, lastValueU), costsUp[j]) + 1;
						costsUp[j - 1] = lastValueU;
						lastValueU = newValue;
					}
				}
			}
			if (i > 0){
				costsUp[up.length()] = lastValueU;}
			
			if(costsUp[up.length()]==0){
				return 1;//KeyEvent.KEYCODE_DPAD_UP;
			}
			for (int k = 0; k <= down.length(); k++) {
				if (i == 0)
					costsD[k] = k;
				else {
					if (k > 0) {
						int newValue = costsD[k - 1];
						if (s1.charAt(i - 1) != down.charAt(k - 1))
							newValue = Math.min(Math.min(newValue, lastValueD), costsD[k]) + 1;
						costsD[k - 1] = lastValueD;
						lastValueD = newValue;
					}
				}
			}
			if (i > 0){
				costsD[down.length()] = lastValueD;}
			if(costsD[down.length()] ==0){
				return 2;//KeyEvent.KEYCODE_DPAD_DOWN;
			}
			for (int g = 0; g <= left.length(); g++) {
				if (i == 0)
					costsL[g] = g;
				else {
					if (g > 0) {
						int newValue = costsL[g - 1];
						if (s1.charAt(i - 1) != left.charAt(g - 1))
							newValue = Math.min(Math.min(newValue, lastValueL), costsL[g]) + 1;
						costsL[g - 1] = lastValueL;
						lastValueL = newValue;
					}
				}
			}
			if (i > 0){
				costsL[left.length()] = lastValueL;}
			if(costsL[left.length()] ==0){
				return 3;//KeyEvent.KEYCODE_DPAD_LEFT;
			}
			for (int s = 0; s <= right.length(); s++) {
				if (i == 0)
					costsR[s] = s;
				else {
					if (s > 0) {
						int newValue = costsR[s - 1];
						if (s1.charAt(i - 1) != right.charAt(s - 1))
							newValue = Math.min(Math.min(newValue, lastValueR), costsR[s]) + 1;
						costsR[s - 1] = lastValueR;
						lastValueR = newValue;
					}
				}
			}
			if (i > 0){
				costsR[right.length()] = lastValueR;}
			if(costsR[right.length()] ==0){
				return 4;//KeyEvent.KEYCODE_DPAD_RIGHT;
			}
		}
		return min(costsUp[up.length()],costsD[down.length()],costsL[left.length()],costsR[right.length()]);
	}
	private static int min(int updist, int downdist, int leftDist,
			int rigthDist) {
		if(updist<downdist&& updist<leftDist&& updist< rigthDist&& updist<4){
			return 1;// KeyEvent.KEYCODE_DPAD_UP;
		}
		if(downdist<updist&& downdist<leftDist&& downdist< rigthDist&& downdist<4){
			return 2;//KeyEvent.KEYCODE_DPAD_DOWN;
		}
		if(leftDist<downdist&& leftDist<updist&& leftDist< rigthDist&& leftDist<4){
			return 3;//KeyEvent.KEYCODE_DPAD_LEFT;
		}
		if(rigthDist<downdist&& rigthDist<leftDist&& rigthDist< updist&& rigthDist<4){
			return 4;//KeyEvent.KEYCODE_DPAD_RIGHT;
		}
		
		
		return 9999;
	}
}


