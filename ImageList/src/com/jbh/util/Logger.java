package com.jbh.util;

import android.util.Log;


public class Logger {

    	private static boolean CHECK_LOGGER = true;    //  LOGGER의 출력 여부를 설정 (LOGGER를 출력하지 않음  : false / LOGGER 출력 : true)
    //	private static boolean CHECK_LOGGER = false;

    public static final String TAG = "JBH Logger";
    public static final String IDENTIFIER = " =====> ";
    public static final int I = 0;
    public static final int D = 1;
    public static final int E = 2;
    public static final int V = 3;
    public static final int W = 4;

    public static void p(Object msg) {
        p(D, IDENTIFIER + msg.toString());
    }

    public static void p(Object msg, Exception e) {
        p(E, msg.toString());
        e.printStackTrace();
    }

    public static void p(int type, Object msg) {
        if (CHECK_LOGGER)
            switch (type) {
                case I:
                    Log.i(TAG, msg.toString());
                    break;
                case D:
                    Log.d(TAG, msg.toString());
                    break;
                case E:
                    Log.e(TAG, msg.toString());
                    break;
                case V:
                    Log.v(TAG, msg.toString());
                    break;
                case W:
                    Log.w(TAG, msg.toString());
                    break;
            }
    }

    public static void i(String tag, Object msg) {
        if (CHECK_LOGGER)
            Log.i(tag, msg.toString());
    }

    public static void d(String tag, Object msg) {
        if (CHECK_LOGGER)
            Log.d(tag, msg.toString());
    }

    public static void e(String tag, Object msg) {
        if (CHECK_LOGGER)
            Log.e(tag, msg.toString());
    }

    public static void v(String tag, Object msg) {
        if (CHECK_LOGGER)
            Log.v(tag, msg.toString());
    }

    public static void w(String tag, Object msg) {
        if (CHECK_LOGGER)
            Log.w(tag, msg.toString());
    }
}
