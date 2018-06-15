package com.example.amit.remind_it;

import android.Manifest;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.amit.remind_it.dao.SampleDataBase;
import com.example.amit.remind_it.model.ItemModel;
//import com.example.amit.remind_it.model.Items;
//import com.example.amit.remind_it.realm.RealmController;
import com.nanotasks.BackgroundWork;
import com.nanotasks.Completion;
import com.nanotasks.Tasks;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

//import io.realm.Realm;

/**
 * Created by amit on 29/12/16.
 */

public class SaveNewItem extends AppCompatActivity {

    String mCurrentPhotoPath;
    ImageView itemImageView;
    EditText nameEditText;
    EditText locationEditText;
   // Realm realm;
    static final int REQUEST_TAKE_PHOTO = 1;
    SampleDataBase sampleDatabase;
    ArrayList<String> list;
    ItemModel itemModel;
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
            Log.d("RI","Image is not null");
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
        sampleDatabase = Room.databaseBuilder(SaveNewItem.this, SampleDataBase.class, "sample-db").build();
       // this.realm = RealmController.with(getApplication()).getRealm();
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
                else if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(SaveNewItem.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(SaveNewItem.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(SaveNewItem.this,
                                new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 12345);
                        return;
                    }

                    dispatchTakePictureIntent();
                }
            }
        });
        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.M) {
            dispatchTakePictureIntent();
        }
        else if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(SaveNewItem.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(SaveNewItem.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(SaveNewItem.this,
                        new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 12345);
                return;
            }
            dispatchTakePictureIntent();
        }

        Button floatingActionButton = (Button) findViewById(R.id.buttondone);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("clecked", "done button");
                itemModel = new ItemModel();
              //  Items item = new Items();
                String name = nameEditText.getText().toString();
                String location = locationEditText.getText().toString();
                //do in background
                itemModel.setName(name);
                itemModel.setLocation(location);
                itemModel.setImgPath(mCurrentPhotoPath);
                Tasks.executeInBackground(SaveNewItem.this, new BackgroundWork<Void>() {
                    @Override
                    public Void doInBackground() throws Exception {
                        sampleDatabase.daoAccess().insertOnlySingleRecord(itemModel);
                        return null;
                    }
                }, new Completion<Void>() {
                    @Override
                    public void onSuccess(Context context, Void result) {
                        Log.d("Room Data Stored","Hurray");
                    }

                    @Override
                    public void onError(Context context, Exception e) {
                        Log.d("Room Data Stored","Fail ");
                        e.printStackTrace();
                    }
                });
//                item.setId(RealmController.getInstance().getBooks().size() + 1);
//                item.setName(name);
//                item.setLocation(location);
//                item.setImgPath(mCurrentPhotoPath);
                //  item.setTags(list);
//                realm.beginTransaction();
//                realm.copyToRealm(item);
//                realm.commitTransaction();
                //    itemsHash.put(item.getItemName(),item);
                Log.d("Data Stored","Hurray");

                Toast toast = Toast.makeText(getApplicationContext(),"Item Saved Successfully!!",Toast.LENGTH_SHORT);
                toast.show();
                 if(name!=null && location!=null) {
                     /*Intent intent= new Intent(SaveNewItem.this, MainActivity.class);
                     intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK );
                     startActivity(intent);*/
                     finish();
                 }else{
                     Toast.makeText(getApplicationContext(),"Enter name and location both",Toast.LENGTH_LONG).show();
                 }
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
        Bitmap bitmap=compressImage(imgFile.getAbsolutePath());
        itemImageView.setImageBitmap(bitmap);
      //  itemImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);


    }
  public Bitmap compressImage(String filePath){
      Bitmap scaledBitmap = null;

      BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
      options.inJustDecodeBounds = true;
      Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

      int actualHeight = options.outHeight;
      int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 816x612

      float maxHeight = 616.0f;
      float maxWidth = 412.0f;
      float imgRatio = actualWidth / actualHeight;
      float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

      if (actualHeight > maxHeight || actualWidth > maxWidth) {
          if (imgRatio < maxRatio) {
              imgRatio = maxHeight / actualHeight;
              actualWidth = (int) (imgRatio * actualWidth);
              actualHeight = (int) maxHeight;
          } else if (imgRatio > maxRatio) {
              imgRatio = maxWidth / actualWidth;
              actualHeight = (int) (imgRatio * actualHeight);
              actualWidth = (int) maxWidth;
          } else {
              actualHeight = (int) maxHeight;
              actualWidth = (int) maxWidth;

          }
      }

//      setting inSampleSize value allows to load a scaled down version of the original image

      options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
      options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
      options.inPurgeable = true;
      options.inInputShareable = true;
      options.inTempStorage = new byte[16 * 1024];

      try {
//          load the bitmap from its path
          bmp = BitmapFactory.decodeFile(filePath, options);
      } catch (OutOfMemoryError exception) {
          exception.printStackTrace();

      }
      try {
          scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
      } catch (OutOfMemoryError exception) {
          exception.printStackTrace();
      }

      float ratioX = actualWidth / (float) options.outWidth;
      float ratioY = actualHeight / (float) options.outHeight;
      float middleX = actualWidth / 2.0f;
      float middleY = actualHeight / 2.0f;

      Matrix scaleMatrix = new Matrix();
      scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

      Canvas canvas = new Canvas(scaledBitmap);
      canvas.setMatrix(scaleMatrix);
      canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
      ExifInterface exif;
      try {
          exif = new ExifInterface(filePath);

          int orientation = exif.getAttributeInt(
                  ExifInterface.TAG_ORIENTATION, 0);
          Log.d("EXIF", "Exif: " + orientation);
          Matrix matrix = new Matrix();
          if (orientation == 6) {
              matrix.postRotate(90);
              Log.d("EXIF", "Exif: " + orientation);
          } else if (orientation == 3) {
              matrix.postRotate(180);
              Log.d("EXIF", "Exif: " + orientation);
          } else if (orientation == 8) {
              matrix.postRotate(270);
              Log.d("EXIF", "Exif: " + orientation);
          }
          scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                  scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                  true);
      } catch (IOException e) {
          e.printStackTrace();
      }
      ByteArrayOutputStream stream = new ByteArrayOutputStream();
      scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80,stream);
     byte[] bytearray=stream.toByteArray();
      try {
          stream.close();
          stream = null;
      } catch (IOException e) {

          e.printStackTrace();
      }
      Bitmap resbitmap=BitmapFactory.decodeByteArray(bytearray,0,bytearray.length);
      return resbitmap;
  }
    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
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
