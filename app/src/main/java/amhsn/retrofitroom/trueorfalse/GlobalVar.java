package amhsn.retrofitroom.trueorfalse;

import java.util.Arrays;
import java.util.Collections;

import android.app.Application;

public class GlobalVar extends Application {

	public static int LEVEL_NUMBER = 8;
	public static int TOTAL_QUESTION = 3010;
	public static int QUESTION_FOR_SINGLE_LEVEL = 50;
	
	public static int i = 0, temp = 0, k = 0, player1Score = 0,
			player2Score = 0, level = 1, playerScore = 0, playerLife = 0, skipCounter=0;
	public static Integer[] array, aryLevel1, aryTotalLevel;
	public static int levelID = 0, levelLives = 0, levelAnswer = 0,
			levelSkip = 0, levelsSecond = 0;
	public static String[] aryScoreList; 
	
	public static boolean isSound = true;

	@Override
	public void onCreate() {
		super.onCreate();

		// --- Score Array
		aryScoreList = new String[LEVEL_NUMBER];
		// --- Score Array
		
		// --- MultiPlayer
		array = new Integer[TOTAL_QUESTION];

		for (int j = 0; j < array.length; j++) {
			GlobalVar.array[j] = j;
		}

		Collections.shuffle(Arrays.asList(array));
		// --- MultiPlayer

		// --- Single Player
		aryLevel1 = new Integer[QUESTION_FOR_SINGLE_LEVEL];

		for (int j = 0; j < aryLevel1.length; j++) {
			GlobalVar.aryLevel1[j] = j;
		}

		Collections.shuffle(Arrays.asList(aryLevel1));
		// --- Single Player
		
		// --- Level
		aryTotalLevel = new Integer[LEVEL_NUMBER];

		for (int j = 0; j < aryTotalLevel.length; j++) {
			GlobalVar.aryTotalLevel[j] = j+1;
		}
		// --- Level
	}

}
