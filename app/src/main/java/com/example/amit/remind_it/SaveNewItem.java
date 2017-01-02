package com.example.amit.remind_it;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.example.amit.remind_it.model.Items;
import com.example.amit.remind_it.realm.RealmController;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import io.realm.Realm;

/**
 * Created by amit on 29/12/16.
 */

public class SaveNewItem extends AppCompatActivity {

    String mCurrentPhotoPath;
    ImageView itemImageView;
    EditText nameEditText;
    EditText locationEditText;
    Realm realm;
    static final int REQUEST_TAKE_PHOTO = 1;
    ArrayList<String> list;
    private static final String TAG = SaveNewItem.class.getSimpleName();

    private File createImageFile() throws IOException {
        // Create an image file name
        Log.d("Remind","I am here");
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp;

        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath =  image.getAbsolutePath();
        Log.d("Photo Path","["+mCurrentPhotoPath+"]");

        if(image != null){
            Log.d("SL","Image is not null");
        }
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Log.d("Remind_it","Unable to create file");
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Log.d("Hi","I am here");
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.amit.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_new_item);
        this.realm = RealmController.with(this).getRealm();
        itemImageView = (ImageView) findViewById(R.id.item_image);
        nameEditText = (EditText) findViewById(R.id.name_edit_text);
        locationEditText = (EditText) findViewById(R.id.location_edit_text);

        list = new ArrayList<>();
        itemImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT<Build.VERSION_CODES.M) {
                    dispatchTakePictureIntent();
                }
              /*  else if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(SaveNewItem.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(SaveNewItem.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(SaveNewItem.this,
                                new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 12345);
                        return;
                    }

                    dispatchTakePictureIntent();
                }*/
            }
        });
        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.M) {
            dispatchTakePictureIntent();
        }
      /*  else if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(SaveNewItem.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(SaveNewItem.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(SaveNewItem.this,
                        new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 12345);
                return;
            }
            dispatchTakePictureIntent();
        }*/

        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Items item = new Items();
                String name = nameEditText.getText().toString();
                String location = locationEditText.getText().toString();
                item.setId(RealmController.getInstance().getBooks().size() + 1);
                item.setName(name);
                item.setLocation(location);
                item.setImgPath(mCurrentPhotoPath);
                //  item.setTags(list);
                realm.beginTransaction();
                realm.copyToRealm(item);
                realm.commitTransaction();
                //    itemsHash.put(item.getItemName(),item);
                Log.d("Data Stored","Hurray");

                Toast toast = Toast.makeText(getApplicationContext(),"Item Saved Successfully!!",Toast.LENGTH_SHORT);
                toast.show();

                startActivity(new Intent(getBaseContext(),MainActivity.class));
                finish();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            setPic();
        }
    }

    private void setPic() {

        File imgFile = new File(mCurrentPhotoPath);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_4444;
         Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),options);
        myBitmap = getResizedBitmap(myBitmap, 100);
        itemImageView.setImageBitmap(myBitmap);
        itemImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);


    }



    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 0) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }
}
