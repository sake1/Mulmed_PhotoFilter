package com.filter.photo.photofilter;

import android.graphics.Bitmap;

/**
 * Created by sake on 24/06/17.
 */

public abstract class Filter {
    public abstract Bitmap filter(Bitmap origin);
}
