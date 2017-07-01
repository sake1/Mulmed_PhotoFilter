package com.filter.photo.photofilter;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.widget.ImageView;


/**
 * Created by sake on 23/06/17.
 */

public class FilterFebri extends Filter{

    private ImageView image;
    private Bitmap layer;

    public FilterFebri(ImageView image, Bitmap layer) {
        this.image = image;
        this.layer = layer;
    }

    @Override
    public Bitmap filter(Bitmap origin) {
        int bitOffset = 64;

        // get image size
        int width = origin.getWidth();
        int height = origin.getHeight();
        Bitmap bmOut =  Bitmap.createBitmap(width, height, origin.getConfig());

        // layers
        Bitmap flaglayer = Bitmap.createScaledBitmap(layer, width, height, true);
        flaglayer.setHeight(height);
        flaglayer.setWidth(width);
        // make greyscale
        for(int x = 0; x < width; ++x) {
            for(int y = 0; y < height; ++y) {
                // get pixel color
                int pixel = origin.getPixel(x, y);
                int alpha = Color.alpha(pixel);
                int r = Color.red(pixel);
                int g = Color.green(pixel);
                int b = Color.blue(pixel);

                int grey = (r+g+b)/3;

                bmOut.setPixel(x, y, Color.argb(alpha, grey, g, b));
            }
        }

        return setFlagFilter(bmOut, flaglayer);
    }

    public static Bitmap setFlagFilter(Bitmap bmp1, Bitmap bmp2) {
        Bitmap bmFilter = Bitmap.createBitmap(bmp1.getWidth(), bmp1.getHeight(), bmp1.getConfig());
        Paint paint = new Paint();
        paint.setAlpha(300);
        Canvas canvas = new Canvas(bmFilter);
        canvas.drawBitmap(bmp1, new Matrix(), null);
        canvas.drawBitmap(bmp2, 0, 0, paint);
        return bmFilter;
    }

}
