package com.brownapps.sortr.utility;

import android.util.Log;

public class Logging {
	
	private static boolean DEBUG = true;
	
	public static void LogDebugMessage(String source, String message){
		if(DEBUG){
			Log.d(source, message);
		}
	}
	
	public static void LogDebugError(String source, Exception e){
		if(DEBUG){
			Log.d(source, "Error");
			e.printStackTrace();
		}
	}

}
