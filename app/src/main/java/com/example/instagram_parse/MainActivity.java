package com.example.instagram_parse;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.ActionBar;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.instagram_parse.fragment.AccountFragment;
import com.example.instagram_parse.fragment.ComposeFragment;
import com.example.instagram_parse.fragment.PostsFragment;
import com.example.instagram_parse.fragment.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity {



    private BottomNavigationView bottomNavigationView;


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.Account_btn:
                FragmentManager fragmentManager2 = getSupportFragmentManager();
                Fragment fragment = null;
                fragment = new AccountFragment();
                fragmentManager2.beginTransaction().replace(R.id.flContainer, fragment).commit();
                return true;
            case R.id.logout_btn:
                logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void logout() {
        ParseUser.logOutInBackground();
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
        finish();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_top_navigation,menu);
        return true;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final FragmentManager fragmentManager = getSupportFragmentManager();


        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                switch (item.getItemId()) {
                    case R.id.btn_homeScreen:
                        item.setIcon(R.drawable.instagram_home_filled_24);
                        bottomNavigationView.getMenu().findItem(R.id.btn_addScreen).setIcon(R.drawable.instagram_new_post_outline_24);
                        bottomNavigationView.getMenu().findItem(R.id.btn_profile).setIcon(R.drawable.instagram_user_outline_24);
                        fragment = new PostsFragment();
                        break;
                    case R.id.btn_addScreen:
                        item.setIcon(R.drawable.instagram_new_post_filled_24);
                        bottomNavigationView.getMenu().findItem(R.id.btn_homeScreen).setIcon(R.drawable.instagram_home_outline_24);
                        bottomNavigationView.getMenu().findItem(R.id.btn_profile).setIcon(R.drawable.instagram_user_outline_24);
                        fragment = new ComposeFragment();
                        break;
                    case R.id.btn_profile:
                        item.setIcon(R.drawable.instagram_user_filled_24);
                        bottomNavigationView.getMenu().findItem(R.id.btn_homeScreen).setIcon(R.drawable.instagram_home_outline_24);
                        bottomNavigationView.getMenu().findItem(R.id.btn_addScreen).setIcon(R.drawable.instagram_new_post_outline_24);
                        fragment = new ProfileFragment();
                        break;
                    default:
                        fragment = new PostsFragment();
                        Toast.makeText(MainActivity.this, "default!",Toast.LENGTH_SHORT).show();
                        break;
                }
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                return true;
            }
        });

        // Set default selection
        bottomNavigationView.setSelectedItemId(R.id.btn_homeScreen);






    }


    }
