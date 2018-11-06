package com.example.dominic.cameraintentactivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class CameraIntentActivity extends AppCompatActivity {
    // define a request code, this is to identify which activity called the intent in takePhoto
    private static final int ACTIVITY_START_CAMERA_APP = 0;
    private ImageView mPhotoCapturedImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_intent);

        mPhotoCapturedImageView = findViewById(R.id.photoImageView);
    }

    public void takePhoto(View view)
    {
        // send msg to another application to take photo for us : intent
        Intent callCameraAppIntent = new Intent();
        callCameraAppIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);

        // start another utility: any other app, please take a photo for me!
        startActivityForResult(callCameraAppIntent, ACTIVITY_START_CAMERA_APP); // starts another activity, then return result to our activity
    }

    // view results of what camera did, set up another function
    // Overwrites default return in startActivityForResult?
    protected void onActivityResult(int requestCode, int resultCode, Intent data) // capture what happened in startActivityForResult
    // NOTE!! : Intent data that is returned to us is actually a small bitmap that is returned to us.
    {
        // check 2 things
        // check 1: check if the request code matches what we requested
        // check 2: when the result is going to be returned, check if process was successful
        if(requestCode == ACTIVITY_START_CAMERA_APP && resultCode == RESULT_OK) // this works, meaning request code and result code are correct
        {
//            Toast.makeText(this, "Picture taken successfully", Toast.LENGTH_LONG).show();
            // Think of bundle as a way of collecting data in android
            Bundle extras = data.getExtras();
            Bitmap photoCaptureBitmap = (Bitmap) extras.get("data");
            // set bitmap to image view
            mPhotoCapturedImageView.setImageBitmap(photoCaptureBitmap);
        }
    }
}
