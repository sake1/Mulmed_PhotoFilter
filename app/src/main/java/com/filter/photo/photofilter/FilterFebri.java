package com.filter.photo.photofilter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;

/**
 * Created by sake on 23/06/17.
 */

public class FilterFebri extends Filter{

    @Override
    public Bitmap filter(Bitmap origin) {
        int bitOffset = 64;
        // get image size
        int width = origin.getWidth();
        int height = origin.getHeight();
        // create output bitmap
        Bitmap bmOut = Bitmap.createBitmap(width, height, origin.getConfig());

        // layers
        BitmapDrawable[] layers = new BitmapDrawable[2];
        layers[0] = new BitmapDrawable(origin);
        layers[1] = (BitmapDrawable) Resources.getSystem().getDrawable(R.mipmap.id);
        LayerDrawable layerdrawable = new LayerDrawable(layers);

        // scan through all pixels
        for(int x = 0; x < width; ++x) {
            for(int y = 0; y < height; ++y) {
                // get pixel color
                int pixel = origin.getPixel(x, y);
                int alpha = Color.alpha(pixel);
                int r = 255 - Color.red(pixel);
                int g = 255 - Color.green(pixel);
                int b = 255 - Color.blue(pixel);

                int[] filteredRGB = {r,g,b};

                bmOut.setPixel(x, y, Color.argb(alpha, filteredRGB[0], filteredRGB[1], filteredRGB[2]));
            }
        }

        return bmOut;
    }

}
