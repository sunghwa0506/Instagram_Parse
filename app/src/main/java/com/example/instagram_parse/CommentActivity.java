package com.example.instagram_parse;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.instagram_parse.fragment.PostsFragment;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class CommentActivity extends AppCompatActivity {


    protected RecyclerView comment_rv;
    protected CommentsAdapter commentsAdapter;
    protected List<ParseObject> allComments;
    protected EditText post_comment;
    private Button comment_btn;
    ProgressBar progressBar;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.animateSlideRight(this);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        progressBar = findViewById(R.id.progressBar);
        String post_id = getIntent().getExtras().getString("post");
        allComments = new ArrayList<>();



        comment_rv = findViewById(R.id.comment_rv);
        comment_rv.setVisibility(View.INVISIBLE);
        comment_rv.setNestedScrollingEnabled(false);



        post_comment = findViewById(R.id.post_comment);
       comment_btn = findViewById(R.id.comment_btn);
        commentsAdapter = new CommentsAdapter(this,allComments);
        comment_rv.setAdapter(commentsAdapter);


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        comment_rv.setLayoutManager(layoutManager);


        comment_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String posting_comment = post_comment.getText().toString();
                if(posting_comment!=null)
                {
                    ParseObject entity = new ParseObject("Comment");
                    entity.put("User", ParseUser.getCurrentUser());
                    entity.put("Comments",posting_comment);
                    entity.put("Post",post_id);

                    entity.saveInBackground(e -> {
                        if (e==null){
                            progressBar.setVisibility(View.VISIBLE);
                            getComment(post_id);
                            progressBar.setVisibility(View.INVISIBLE);
                            post_comment.setText("");
                            PostsFragment.adapter.notifyDataSetChanged();


                        }else{
                            //Something went wrong
                            Toast.makeText(CommentActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else
                {
                    Toast.makeText(CommentActivity.this,"Comment cannot be empty!",Toast.LENGTH_SHORT).show();
                }
            }
        });


        getComment(post_id);


    }


    private void getComment(String post_id) {



        ParseQuery<ParseObject> query = ParseQuery.getQuery("Comment");
        query.whereEqualTo("Post",post_id);
        query.orderByDescending("createdAt");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> Comments, ParseException e) {
                if(e!=null)
                {
                    Log.e("Main","Issue with getting comment",e);
                }

                if(Comments!=null)
                {
                    allComments.clear();
                    allComments.addAll(Comments);
                    commentsAdapter.notifyDataSetChanged();
                    comment_rv.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);
                }



            }
        });


    }

}