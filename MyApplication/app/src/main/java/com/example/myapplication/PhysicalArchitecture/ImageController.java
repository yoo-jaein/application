package com.example.myapplication.PhysicalArchitecture;

import android.graphics.Bitmap;

/**
 * Created by jm on 2017-10-30.
 * 액티비티마다 비트맵 크기 조절하는 등 비트맵 관련 함수가 좀 필요할거같아서
 * 관련한거 다 모아서 여기 만들자
 */

public class ImageController {

    public static Bitmap resizeBitmap(Bitmap bitmap, int resizeHeight){
        double aspectRatio = (double) bitmap.getHeight() / (double) bitmap.getWidth();
        int targetWidth = (int) (resizeHeight * aspectRatio);
        Bitmap result = Bitmap.createScaledBitmap(bitmap, targetWidth, resizeHeight, false);

        if (result != bitmap) {
            bitmap.recycle();
        }
        return result;
    }
}
