/**
 * Copyright (C) 2016 Marco Aurélio Prado dos Santos Vidoca.
 */

package com.example.marcoaurelio.popularmovies.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

@SuppressWarnings("unused")
public class Utils {

    public static void saveBitmapToFile(Context context, Bitmap bitmap, String fileName) throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        FileOutputStream fo = context.openFileOutput(fileName, Context.MODE_PRIVATE);
        fo.write(bytes.toByteArray());
        fo.close();
    }

    public static Bitmap getBitmapFromFile(Context context, String fileName) throws FileNotFoundException {
        return BitmapFactory.decodeStream(context.openFileInput(fileName));
    }

    public static Bitmap getBitmapFromResources(Context context, int drawableId) {
        return BitmapFactory.decodeResource(context.getResources(), drawableId);
    }

    public static byte[] bitmapToByteArray(Bitmap bitmap){
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
        return bos.toByteArray();
    }

    public static Bitmap byteArrayToBitmap(byte[] byteArray){
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
    }

    // Approximation
    public static int dpToPx(Context context, int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(dp * ((float)displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    // Approximation
    public static int pxToDp(Context context, int px) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(px / ((float)displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT));
    }
}
