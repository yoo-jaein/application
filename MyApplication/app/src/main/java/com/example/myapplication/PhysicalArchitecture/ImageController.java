package com.example.myapplication.PhysicalArchitecture;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.io.ByteArrayOutputStream;

/**
 * Created by jm on 2017-10-30.
 * 액티비티마다 비트맵 크기 조절하는 등 비트맵 관련 함수가 좀 필요할거같아서
 * 관련한거 다 모아서 여기 만들자
 */

public class ImageController {

    public static Drawable ByteToDrawable(byte[] b) {
        Drawable image;
        Log.d("test", "CustomAdapter:ByteToDrawable start");
        BitmapFactory.Options options2 = new BitmapFactory.Options();

        options2.inPreferredConfig = Bitmap.Config.RGB_565;

        options2.inSampleSize = 1;

        options2.inPurgeable = true;

        Bitmap src = BitmapFactory.decodeByteArray(b, 0, b.length);
        Log.d("test", "CustomAdapter:ByteToDrawable settingOption");
        Bitmap bitmap = ImageController.resizeBitmap(src,1000);

//        Bitmap bitmap=BitmapFactory.decodeByteArray(b, 0, b.length);
        Log.d("test", "CustomAdapter:ByteToDrawable settingBitmap");
        image = new BitmapDrawable(bitmap);
        return image;
    }

    public static Bitmap resizeBitmap(Bitmap bitmap, int resizeHeight){
        double aspectRatio = (double) bitmap.getHeight() / (double) bitmap.getWidth();
        int targetWidth = (int) (resizeHeight * aspectRatio);
        Bitmap result = Bitmap.createScaledBitmap(bitmap, targetWidth, resizeHeight, false);

        if (result != bitmap) {
            bitmap.recycle();
        }
        return result;
    }

    public static byte[] bitmapToByteArray( Bitmap bitmap, int quality) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream() ;
        bitmap.compress( Bitmap.CompressFormat.JPEG, quality, stream) ;
        byte[] byteArray = stream.toByteArray() ;
        return byteArray ;
    }
}
