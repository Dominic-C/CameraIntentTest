package com.example.dominic.cameraintentactivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CameraIntentActivity extends AppCompatActivity {
    // define a request code, this is to identify which activity called the intent in takePhoto
    private static final int ACTIVITY_START_CAMERA_APP = 0;
    private ImageView mPhotoCapturedImageView;
    // mImageFileLocation stores file locations so that other functions can use the file's location
    private String mImageFileLocation = "";

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

        // add extra info to intent to store image into address specified by us

        File photoFile = null;
        try
        {
            photoFile = createImageFile();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        if(photoFile != null)
        {
            // create extra field to intent to put address into
//            callCameraAppIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile)); // tells intent where to store data to when it receives extra data


            Uri photoURI = FileProvider.getUriForFile(this, "com.example.dominic.cameraintentactivity", photoFile);
            callCameraAppIntent.putExtra(MediaStore.EXTRA_OUTPUT,photoURI);
            // start another utility: any other app, please take a photo for me!
            startActivityForResult(callCameraAppIntent, ACTIVITY_START_CAMERA_APP); // starts another activity, then return result to our activity

        }
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
//            Bundle extras = data.getExtras(); // dont need these once we have full sized image
//            Bitmap photoCaptureBitmap = (Bitmap) extras.get("data"); // dont need these once we have full sized image
            // set bitmap to image view
//            mPhotoCapturedImageView.setImageBitmap(photoCaptureBitmap);
            Bitmap mPhotoCapturedBitmap = BitmapFactory.decodeFile(mImageFileLocation);
            mPhotoCapturedImageView.setImageBitmap(mPhotoCapturedBitmap);
        }
    }

    // write a function that specifies the location of where we want to write the file to
    File createImageFile() throws IOException {
        // create a non collision file name using timestamps
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "IMAGE_" + timeStamp + "_";
        // specify location in storage
//        File StorageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES); // externalStoragePublicDirectory means file is viewable from all apps
        File StorageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        // from the line above, we see that we store our picture in the pictures directory

        File image = File.createTempFile(imageFileName, ".jpg", StorageDirectory); // unhandled exceptions
        /**
         * certain functions are quite important, such as writing and reading to memory.
         * So we want to catch when something goes wrong. This particular function needs to do that.
         * This line "File image = File.cr.." is now satisfied after we add a "throws IO exception" to the top of the function
        * */
        mImageFileLocation = image.getAbsolutePath(); // getAbs path gives string

        return image;

    }

    public static final String WARDROBE_ACTIVITY = "com.example.dominic.cameraintentactivity.WARDROBE";
    // Go to new page
    public void toWardrobe(View view) // called onClick from "to wardrobe" button
    {
        Intent intent = new Intent(this, WardrobeActivity.class); // intent calls another activity or application
        startActivity(intent);
    }
}
