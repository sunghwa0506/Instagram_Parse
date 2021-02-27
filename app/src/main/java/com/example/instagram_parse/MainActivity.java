package com.example.instagram_parse;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText decription;
    private ImageView picImg;
    private Button btn_take_pic;
    private Button post;
    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 30;
    private File photoFile;
    public String photoFileName = "photo.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        decription = findViewById(R.id.decription);
        picImg = findViewById(R.id.picImg);
        btn_take_pic = findViewById(R.id.btn_take_pic);
        post = findViewById(R.id.post);

        queryposts();

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description = decription.getText().toString();

                if(description.isEmpty())
                {
                    Toast.makeText(MainActivity.this,"Description cannot be empty",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(photoFile == null || picImg.getDrawable() == null)
                {
                    Toast.makeText(MainActivity.this,"There is no image!",Toast.LENGTH_SHORT).show();
                    return;
                }

                ParseUser currentUser = ParseUser.getCurrentUser();
                savePost(description,currentUser,photoFile);
            }
        });


        btn_take_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchCamera();
            }
        });


    }

    private void launchCamera() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create a File reference for future access
        photoFile = getPhotoFileUri(photoFileName);

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        Uri fileProvider = FileProvider.getUriForFile(MainActivity.this, "com.codepath.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // by this point we have the camera photo on disk
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                // RESIZE BITMAP, see section below
                // Load the taken image into a preview
                picImg.setImageBitmap(takenImage);
            } else { // Result was a failure
                Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public File getPhotoFileUri(String fileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "hello");

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d("hello", "failed to create directory");
        }

        // Return the file target for the photo based on filename
        return  new File(mediaStorageDir.getPath() + File.separator + fileName);

    }

    private void savePost(String description, ParseUser currentUser, File photoFile) {
        Post post = new Post();
        post.setKeyDescription((description));
        post.setKeyImage( new ParseFile(photoFile));
        post.setKeyUser(currentUser);
        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e!=null)
                {
                    Log.e("this","Error while saving",e);
                    Toast.makeText(MainActivity.this,"Error while saving",Toast.LENGTH_SHORT).show();
                }
                Log.i("this","Post save was sucessfull!!");
                decription.setText("");
                picImg.setImageResource(0);
            }
        });
    }

    private void queryposts() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {

                if(e!=null)
                {
                    Log.e("Main","Issue with getting posts",e);
                }

                for(Post post: posts)
                {
                    Log.i("Post","Post "+ post.getKeyDescription());
                }

            }
        });
    }
}