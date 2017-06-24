package com.filter.photo.photofilter;

import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;

import java.io.File;

/**
 * Created by sake on 23/06/17.
 */

public class FilterMoti extends Filter{

    private static final int RED = 1;
    private static final int GREEN = 2;
    private static final int BLUE = 3;
    private static final int YELLOW = 4;
    private static final int PURPLE = 5;
    private static final int TEAL = 6;
    private static final int BLACK = 7;
    private static final int WHITE = 8;
    private static final int BLACK_WHITE_TOLERANCE = 30;
    private static final int RGB_TOLERANCE = 30;

    private static int i2(int r, int g, int b) {
        return r - Math.max(g, b) > 0 ? RED :
                (g - Math.max(r, b) > 0 ? GREEN : BLUE);
    }

    private static int i1(int r, int g, int b) {
        return Math.abs(r-g) <= RGB_TOLERANCE ? YELLOW :
                (Math.abs(r-b) <= RGB_TOLERANCE ? PURPLE : TEAL);
    }

    private static boolean h_cond(int r, int g, int b) {
        return 0 <= r - Math.max(g, b) && r - Math.max(g, b) <= RGB_TOLERANCE ||
                0 <= g - Math.max(r, b) && g - Math.max(r, b) <= RGB_TOLERANCE ||
                0 <= b - Math.max(r, g) && b - Math.max(r, g) <= RGB_TOLERANCE;
    }

    private static int h(int r, int g, int b) {
        return h_cond(r, g, b) ? i1(r, g, b) : i2(r, g, b);
    }

    private static int g(int r, int g, int b) {
        return r+g+b <= 255*3/2 ? BLACK : WHITE;
    }

    private static int t(int r, int g, int b) {
        return Math.abs(r-g) + Math.abs(r-b) + Math.abs(b-g);
    }

    private static int f(int r, int g, int b) {
        return t(r, g, b) <= BLACK_WHITE_TOLERANCE ? g(r, g, b) : h(r, g, b);
    }

    private static int[] translate(int colorCode) {
        if(colorCode == RED) {
            return new int[] {255, 0, 0};
        } else if(colorCode == GREEN) {
            return new int[] {0, 255, 0};
        } else if(colorCode == BLUE) {
            return new int[] {0, 0, 255};
        } else if(colorCode == YELLOW) {
            return new int[] {255, 255, 0};
        } else if(colorCode == PURPLE) {
            return new int[] {255, 0, 255};
        } else if(colorCode == TEAL) {
            return new int[] {0, 255, 255};
        } else if(colorCode == BLACK) {
            return new int[] {0, 0, 0};
        } else {
            ///colorCode == WHITE
            return new int[] {255, 255, 255};
        }
    }

    @Override
    public Bitmap filter(Bitmap origin) {
        int bitOffset = 64;
        // get image size
        int width = origin.getWidth();
        int height = origin.getHeight();
        // create output bitmap
        Bitmap bmOut = Bitmap.createBitmap(width, height, origin.getConfig());
        // color information

        // scan through all pixels
        for(int x = 0; x < width; ++x) {
            for(int y = 0; y < height; ++y) {
                // get pixel color
                int pixel = origin.getPixel(x, y);
                int alpha = Color.alpha(pixel);
                int r = Color.red(pixel);
                int g = Color.green(pixel);
                int b = Color.blue(pixel);

                int[] filteredRGB = translate(f(r, g, b));

                bmOut.setPixel(x, y, Color.argb(alpha, filteredRGB[0], filteredRGB[1], filteredRGB[2]));
            }
        }
        return bmOut;
    }
}
