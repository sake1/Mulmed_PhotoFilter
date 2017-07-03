package com.filter.photo.photofilter;

import android.graphics.Bitmap;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.widget.ImageView;

/**
 * Created by sake on 23/06/17.
 */



public class FilterRifqi extends Filter{

    private ImageView image;
    private Bitmap bitmap;

    public FilterRifqi(ImageView image, Bitmap bitmap) {
        this.image = image;
        this.bitmap = bitmap;
    }


    @Override
    public Bitmap filter(Bitmap origin) {
        int bitOffset = 64;

        // get image size
        int width = origin.getWidth();
        int height = origin.getHeight();
        Bitmap bmOut = Bitmap.createBitmap(width, height, origin.getConfig());

        // layers
        Bitmap framelayer = Bitmap.createScaledBitmap(bitmap, width, height, true);
        framelayer.setHeight(height);
        framelayer.setWidth(width);
        // make greyscale
        for(int x = 0; x < width; ++x) {
            for(int y = 0; y < height; ++y) {
                // get pixel color
                int pixel = origin.getPixel(x, y);
                int alpha = Color.alpha(pixel);
                int r = Color.red(pixel);
                int g = Color.green(pixel);
                int b = Color.blue(pixel);

                bmOut.setPixel(x, y, Color.argb(alpha, r, g, b));
            }
        }

        return setFrameFilter(bmOut, framelayer);
    }

    public static Bitmap setFrameFilter(Bitmap bmp1, Bitmap bmp2) {
        Bitmap bmFilter = Bitmap.createBitmap(bmp1.getWidth(), bmp1.getHeight(), bmp1.getConfig());
        Paint paint = new Paint();
        paint.setAlpha(1000);
        Canvas canvas = new Canvas(bmFilter);
        canvas.drawBitmap(bmp1, new Matrix(), null);
        canvas.drawBitmap(bmp2, 0, 0, paint);
        return bmFilter;
    }
}
