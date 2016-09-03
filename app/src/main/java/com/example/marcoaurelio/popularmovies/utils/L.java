/**
 * Copyright (C) 2016 Marco Aurélio Prado dos Santos Vidoca.
 */

package com.example.marcoaurelio.popularmovies.utils;

import android.util.Log;

@SuppressWarnings("unused")
/**
 * My own Log class implementation.
 */
public class L {

    private static final String sMyAppTag = "MPRADO";
    private static boolean sEnable = true;

    public static void enable(){
        sEnable = true;
    }

    public static void disable(){
        sEnable = false;
    }

    public static void v(String s){
        if(sEnable){
            Log.v(sMyAppTag, s);
        }
    }

    public static void v(String t, String s){
        if(sEnable){
            Log.v(sMyAppTag + " - " + t, s);
        }
    }

    public static void v(Object o, String s){
        if(sEnable){
            Log.v(sMyAppTag + " - " + o.getClass().getSimpleName(), s);
        }
    }

    public static void v(Class c, String s){
        if(sEnable){
            Log.v(sMyAppTag + " - " + c.getSimpleName(), s);
        }
    }

    public static void d(String s){
        if(sEnable){
            Log.d(sMyAppTag, s);
        }
    }

    public static void d(String t, String s){
        if(sEnable){
            Log.d(sMyAppTag + " - " + t, s);
        }
    }

    public static void d(Object o, String s){
        if(sEnable){
            Log.d(sMyAppTag + " - " + o.getClass().getSimpleName(), s);
        }
    }

    public static void d(Class c, String s){
        if(sEnable){
            Log.d(sMyAppTag + " - " + c.getSimpleName(), s);
        }
    }

    public static void i(String s){
        if(sEnable){
            Log.i(sMyAppTag, s);
        }
    }

    public static void i(String t, String s){
        if(sEnable){
            Log.i(sMyAppTag + " - " + t, s);
        }
    }

    public static void i(Object o, String s){
        if(sEnable){
            Log.i(sMyAppTag + " - " + o.getClass().getSimpleName(), s);
        }
    }

    public static void i(Class c, String s){
        if(sEnable){
            Log.i(sMyAppTag + " - " + c.getSimpleName(), s);
        }
    }

    public static void w(String s){
        if(sEnable){
            Log.w(sMyAppTag, s);
        }
    }

    public static void w(String t, String s){
        if(sEnable){
            Log.w(sMyAppTag + " - " + t, s);
        }
    }

    public static void w(Object o, String s){
        if(sEnable){
            Log.w(sMyAppTag + " - " + o.getClass().getSimpleName(), s);
        }
    }

    public static void w(Class c, String s){
        if(sEnable){
            Log.w(sMyAppTag + " - " + c.getSimpleName(), s);
        }
    }

    public static void e(String s){
        if(sEnable){
            Log.e(sMyAppTag, s);
        }
    }

    public static void e(String t, String s){
        if(sEnable){
            Log.e(sMyAppTag + " - " + t, s);
        }
    }

    public static void e(Object o, String s){
        if(sEnable){
            Log.e(sMyAppTag + " - " + o.getClass().getSimpleName(), s);
        }
    }

    public static void e(Class c, String s){
        if(sEnable){
            Log.e(sMyAppTag + " - " + c.getSimpleName(), s);
        }
    }
}
