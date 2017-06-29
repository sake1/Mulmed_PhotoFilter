package com.filter.photo.photofilter;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int PICK_PICTURE = 1;
    private static final int REQUEST_STORAGE_READ = 2;
    private static final int REQUEST_STORAGE_WRITE = 3;

    @BindView(R.id.output_image) ImageView image;
    @BindView(R.id.output_waiting) ProgressBar waitingBuffer;

    private Bitmap originalImage;
    private Bitmap tempFilteredImage;

    private boolean validate() {
        if(originalImage == null) {
            Toast.makeText(MainActivity.this, "Pick an image first!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(image.getVisibility() == View.GONE) {
            Toast.makeText(MainActivity.this, "A filter is currently in process...", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @OnClick(R.id.trigger_filter1)
    public void filter1() {
        filterImage(new FilterFebri());
    }

    @OnClick(R.id.trigger_filter2)
    public void filter2() {
        filterImage(new FilterMoti());
    }

    @OnClick(R.id.trigger_filter3)
    public void filter3() {
        filterImage(new FilterRifqi());
    }

    private void filterImage(final Filter filter) {
        final Bitmap[] filterResult = {null};
        if(!validate()) {
            return;
        }
        waitingBuffer.setVisibility(View.VISIBLE);
        image.setVisibility(View.GONE);
        new AsyncTask<Void, Void, Boolean>() {
            protected Boolean doInBackground(Void... params) {
                filterResult[0] = filter.filter(originalImage);
                return null;
            }
            protected void onPostExecute(Boolean result) {
                image.setImageBitmap(filterResult[0]);
                waitingBuffer.setVisibility(View.GONE);
                image.setVisibility(View.VISIBLE);
            }
        }.execute();
    }

    private void setTempImageNotify(Bitmap image) {
        tempFilteredImage = image;
    }

    @OnClick(R.id.trigger_clear)
    public void clearFilter() {
        if(!validate()) {
            return;
        }
        image.setImageBitmap(originalImage);
    }

    @OnClick(R.id.trigger_upload)
    public void uploadImage() {
        if(image.getVisibility() == View.GONE) {
            Toast.makeText(MainActivity.this, "A filter is currently in process...", Toast.LENGTH_SHORT).show();
            return;
        }
        /**
         * check for read storage permission, ask permission if needed.
         * Proceed to accessing gallery otherwise
         * requestPermissions method will trigger onRequestPermissionsResult method
         * after the user do something at the prompt dialog
         */
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_STORAGE_READ);
                return;
            }
        }
        pickImageFromGallery();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            if(requestCode == PICK_PICTURE) {
                try {
                    Uri imageUri = data.getData();
                    InputStream imageStream = getContentResolver().openInputStream(imageUri);
                    originalImage = BitmapFactory.decodeStream(imageStream);
                    Log.d(TAG, "width: " + originalImage.getWidth() + ", height: " + originalImage.getHeight());
                    image.setImageBitmap(originalImage);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Fail to retrieve image data", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @OnClick(R.id.trigger_save)
    public void saveImage() {
        if(!validate()) {
            return;
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_STORAGE_WRITE);
                return;
            }
        }
        saveImageToGallery();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_STORAGE_READ) {
            if(permissions[0].equals(Manifest.permission.READ_EXTERNAL_STORAGE) && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pickImageFromGallery();
            } else {
                Toast.makeText(MainActivity.this, "Need storace access permission to pick an image", Toast.LENGTH_LONG).show();
            }
        } else if(requestCode == REQUEST_STORAGE_WRITE) {
            if(permissions[0].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE) && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                saveImageToGallery();
            } else {
                Toast.makeText(MainActivity.this, "Need storace access permission to save the image", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void pickImageFromGallery() {
        /**
         * startActivityForResult method will trigger onActivityResult method
         * after the user finished picking the picture from gallery
         * The Image data from gallery will then be handled by onActivityResult method
         */
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, PICK_PICTURE);
    }

    private void saveImageToGallery() {
        Bitmap imageBMP = ((BitmapDrawable) image.getDrawable()).getBitmap();
        File myDir = new File(Environment.getExternalStorageDirectory().toString() + "/Pictures/FilteredImage");
        if (!myDir.exists()){
            myDir.mkdirs();
        }
        File file;
        do {
            Random generator = new Random();
            file = new File(myDir, "Image-"+ generator.nextInt(999999) +".jpg");
        } while(file.exists());
        try {
            FileOutputStream out = new FileOutputStream(file);
            imageBMP.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            Toast.makeText(MainActivity.this, "Image saved sucessfully", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }
}
