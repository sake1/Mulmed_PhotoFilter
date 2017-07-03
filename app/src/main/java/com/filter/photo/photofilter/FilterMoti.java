package com.filter.photo.photofilter;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

/**
 * Created by sake on 23/06/17.
 */

public class FilterMoti extends Filter {

    public static final int CIRCLE_SHADOW_FRAME_FILTER = 3;
    public static final int SINGLE_COLOR_FILTER_RED = 0;
    public static final int SINGLE_COLOR_FILTER_GREEN = 1;
    public static final int SINGLE_COLOR_FILTER_BLUE = 2;

    private int code;

    public FilterMoti(int code) {
        this.code = code;
    }

    private Bitmap circleShadowFrame(Bitmap origin) {
        int width = origin.getWidth();
        int height = origin.getHeight();
        Bitmap bmOut = Bitmap.createBitmap(width, height, origin.getConfig());

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int pixel = origin.getPixel(x, y);
                int alpha = Color.alpha(pixel);
                int r = Color.red(pixel);
                int g = Color.green(pixel);
                int b = Color.blue(pixel);

                int[] filteredRGB = CircleShadowFrame.f(r, g, b, x, y, width, height);
                bmOut.setPixel(x, y, Color.argb(alpha, filteredRGB[0], filteredRGB[1], filteredRGB[2]));
            }
        }
        return bmOut;
    }

    private Bitmap redHighliterFilter(Bitmap origin) {
        int width = origin.getWidth();
        int height = origin.getHeight();
        Bitmap bmOut = Bitmap.createBitmap(width, height, origin.getConfig());

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int pixel = origin.getPixel(x, y);
                int alpha = Color.alpha(pixel);
                int r = Color.red(pixel);
                int g = Color.green(pixel);
                int b = Color.blue(pixel);

                int[] filteredRGB = RedHighliterFilter.f(r, g, b);
                bmOut.setPixel(x, y, Color.argb(alpha, filteredRGB[0], filteredRGB[1], filteredRGB[2]));
            }
        }
        return bmOut;
    }

    private Bitmap greenHighliterFilter(Bitmap origin) {
        int width = origin.getWidth();
        int height = origin.getHeight();
        Bitmap bmOut = Bitmap.createBitmap(width, height, origin.getConfig());

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int pixel = origin.getPixel(x, y);
                int alpha = Color.alpha(pixel);
                int r = Color.red(pixel);
                int g = Color.green(pixel);
                int b = Color.blue(pixel);

                int[] filteredRGB = GreenHighliterFilter.f(r, g, b);
                bmOut.setPixel(x, y, Color.argb(alpha, filteredRGB[0], filteredRGB[1], filteredRGB[2]));
            }
        }
        return bmOut;
    }

    private Bitmap blueHighliterFilter(Bitmap origin) {
        int width = origin.getWidth();
        int height = origin.getHeight();
        Bitmap bmOut = Bitmap.createBitmap(width, height, origin.getConfig());

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int pixel = origin.getPixel(x, y);
                int alpha = Color.alpha(pixel);
                int r = Color.red(pixel);
                int g = Color.green(pixel);
                int b = Color.blue(pixel);

                int[] filteredRGB = BlueHighliterFilter.f(r, g, b);
                bmOut.setPixel(x, y, Color.argb(alpha, filteredRGB[0], filteredRGB[1], filteredRGB[2]));
            }
        }
        return bmOut;
    }

    @Override
    public Bitmap filter(Bitmap origin) {
        if (code == CIRCLE_SHADOW_FRAME_FILTER) {
            return circleShadowFrame(origin);
        } else if(code == SINGLE_COLOR_FILTER_RED) {
            return redHighliterFilter(origin);
        } else if(code == SINGLE_COLOR_FILTER_GREEN) {
            return greenHighliterFilter(origin);
        } else {
            return blueHighliterFilter(origin);
        }
    }
}

class ExtremeRGBFilter {

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
        return Math.abs(r - g) <= RGB_TOLERANCE ? YELLOW :
                (Math.abs(r - b) <= RGB_TOLERANCE ? PURPLE : TEAL);
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
        return r + g + b <= 255 * 3 / 2 ? BLACK : WHITE;
    }

    private static int t(int r, int g, int b) {
        return Math.abs(r - g) + Math.abs(r - b) + Math.abs(b - g);
    }

    public static int f(int r, int g, int b) {
        return t(r, g, b) <= BLACK_WHITE_TOLERANCE ? g(r, g, b) : h(r, g, b);
    }

    public static int[] translate(int colorCode) {
        if (colorCode == RED) {
            return new int[]{255, 0, 0};
        } else if (colorCode == GREEN) {
            return new int[]{0, 255, 0};
        } else if (colorCode == BLUE) {
            return new int[]{0, 0, 255};
        } else if (colorCode == YELLOW) {
            return new int[]{255, 255, 0};
        } else if (colorCode == PURPLE) {
            return new int[]{255, 0, 255};
        } else if (colorCode == TEAL) {
            return new int[]{0, 255, 255};
        } else if (colorCode == BLACK) {
            return new int[]{0, 0, 0};
        } else {
            ///colorCode == WHITE
            return new int[]{255, 255, 255};
        }
    }
}

class DarkLightInstensifierFilter {
    private static final int TRESHOLD = 256*3/2;
    private static final double ADD_PERCENTAGE = 1.2;
    private static final double REDUCE_PERCENTAGE = 0.8;

    private static int g2(int c) {
        return (int) Math.round(c*REDUCE_PERCENTAGE);
    }

    private static int g1(int c) {
        return (int) Math.round(c*ADD_PERCENTAGE);
    }

    private static boolean f_cond(int r, int g, int b) {
        return r+g+b < TRESHOLD;
    }

    public static int[] f(int r, int g, int b) {
        return f_cond(r, g, b) ? new int[] {g1(r), g1(g), g1(b)} : new int[] {g2(r), g2(g), g2(b)};
    }
}

class CircleShadowFrame {

    private static final double MAX_INTESIFIER = 1.7;
    private static final double REDUCTION_PER_Z = 0.7;

    private static double g(double r, double z) {
        return MAX_INTESIFIER - REDUCTION_PER_Z * (r / z);
    }

    private static double fx(int x, int y, int w, int h) {
        /// 2 * sqrt( | x - w/2 |^2 + | y - h/2 |^2 )
        return 2 * Math.sqrt(Math.pow(Math.abs(x - (w/2)), 2) + Math.pow(Math.abs(y - (h/2)),2));
    }

    public static int[] f(int r, int g, int b, int x, int y, int w, int h) {
        double z = Math.min(w, h);
        double t = g(fx(x, y, w, h), z);
        return new int[] {Math.min((int) Math.round(r*t), 255),
                Math.min((int) Math.round(g*t), 255),
                Math.min((int) Math.round(b*t), 255)};
    }
}

class RedHighliterFilter {

    private static final double TRESHOLD = 1.7;
    private static final double ADD_PERCENTAGE = 1.2;
    private static final int BLACK_WHITE_TOLERANCE = 30;

    private static int[] g2(int r, int g, int b) {
        int gray = (r+g+b) / 3;
        return new int[] {gray, gray, gray};
    }

    private static int[] g1(int r, int g, int b) {
        return new int[] {r, g, b};
    }

    private static boolean f_cond(int r, int g, int b) {
        return Math.max(g, b) * TRESHOLD < r
                && Math.abs(r - g) + Math.abs(r - b) + Math.abs(b - g) >= BLACK_WHITE_TOLERANCE;
    }
    public static int[] f(int r, int g, int b) {
        return f_cond(r, g, b) ? g1(r, g, b) : g2(r, g, b);
    }
}

class GreenHighliterFilter {

    private static final double TRESHOLD = 1.15;
    private static final double ADD_PERCENTAGE = 1.2;
    private static final int BLACK_WHITE_TOLERANCE = 30;

    private static int[] g2(int r, int g, int b) {
        int gray = (r+g+b) / 3;
        return new int[] {gray, gray, gray};
    }

    private static int[] g1(int r, int g, int b) {
        return new int[] {r, g, b};
    }

    private static boolean f_cond(int r, int g, int b) {
        return Math.max(r, b) * TRESHOLD < g
                && Math.abs(r - g) + Math.abs(r - b) + Math.abs(b - g) >= BLACK_WHITE_TOLERANCE;
    }
    public static int[] f(int r, int g, int b) {
        return f_cond(r, g, b) ? g1(r, g, b) : g2(r, g, b);
    }
}

class BlueHighliterFilter {

    private static final double TRESHOLD = 1.3;
    private static final double ADD_PERCENTAGE = 1.2;
    private static final int BLACK_WHITE_TOLERANCE = 30;

    private static int[] g2(int r, int g, int b) {
        int gray = (r+g+b) / 3;
        return new int[] {gray, gray, gray};
    }

    private static int[] g1(int r, int g, int b) {
        return new int[] {r, g, b};
    }

    private static boolean f_cond(int r, int g, int b) {
        return r * TRESHOLD < b && g <= b
                && Math.abs(r - g) + Math.abs(r - b) + Math.abs(b - g) >= BLACK_WHITE_TOLERANCE;
    }
    public static int[] f(int r, int g, int b) {
        return f_cond(r, g, b) ? g1(r, g, b) : g2(r, g, b);
    }
}