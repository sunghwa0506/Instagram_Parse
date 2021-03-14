package com.example.instagram_parse;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {

    private Context context;
    private List<Post> posts;



    public PostsAdapter(Context context,List<Post> posts)
    {
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.bind(post);

    }

    @Override
    public int getItemCount() {
        return posts.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView userID;
        private ImageView image_post;
        private TextView post_description;
        private ImageView imageView2;
        private TextView num_likes;
        private ImageView profile_image_for_post;
        private ImageView comments_btn;
        public TextView num_comment;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userID = itemView.findViewById(R.id.userID);
            image_post = itemView.findViewById(R.id.image_post);
            post_description = itemView.findViewById(R.id.post_description);
            imageView2 = itemView.findViewById(R.id.imageView2);
            num_likes = itemView.findViewById(R.id.num_likes);
            profile_image_for_post = itemView.findViewById(R.id.profile);
            comments_btn = itemView.findViewById(R.id.comments_btn);
            num_comment = itemView.findViewById(R.id.num_comment);

        }


        public void bind(Post post) {
            post_description.setText(post.getKeyDescription());
            userID.setText(post.getUser().getUsername());
            ParseFile image = post.getImage();
            if(image != null)
            {
             Glide.with(context).load(image.getUrl()).into(image_post);
            }
            check_likes(post);
            click_like(post);
            LeaveComment(post);
            getNumberofComment(post);


            ParseUser post_user = post.getUser();
            ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");

            query.getInBackground(post_user.getObjectId(), (object, e) -> {
                if (e == null) {
                    //Object was successfully retrieved
                    ParseFile temp = object.getParseFile("profile");
                    Glide.with(context).load(temp.getUrl()).into(profile_image_for_post);
                } else {
                    // something went wrong
                    Log.e("h1231eh", e.getMessage());
                }
            });

            }

        private void getNumberofComment(Post post) {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Comment");
            query.whereEqualTo("Post", post.getObjectId());
            Log.i("hh","comment number "+ post.getObjectId());

            query.countInBackground((count, e) -> {
                if (e == null) {
                    if(count>0)
                    {
                        num_comment.setVisibility(View.VISIBLE);
                        num_comment.setText(String.valueOf(count));
                    }
                    else
                    {
                        num_comment.setVisibility(View.INVISIBLE);
                    }


                } else {
                    Log.i("hh","comment number "+ e);
                }
            });

        }

        private void check_likes(Post post) {
            ParseUser user = ParseUser.getCurrentUser();
            String user_like = user.getObjectId().toString();
            ArrayList<String> likes = new ArrayList<>();

            if(post.getKEY_likes() == null)
            {
                imageView2.setImageResource(R.drawable.ufi_heart);
            }
            else {
                likes = post.getKEY_likes();
                if(likes.contains(user_like))
                {
                    imageView2.setImageResource(R.drawable.ufi_heart_active);
                }
                else
                {
                    imageView2.setImageResource(R.drawable.ufi_heart);
                }


            }

            update_likes(likes);

        }


        private void click_like(Post post) {

            imageView2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ParseUser user = ParseUser.getCurrentUser();
                    String user_like = user.getObjectId().toString();
                    ArrayList<String> likes = new ArrayList<>();

                    if(post.getKEY_likes() == null)
                    {
                        Log.d("this","null in likes "+user_like);
                        likes.add(user_like);
                        imageView2.setImageResource(R.drawable.ufi_heart_active);
                    }
                    else {
                        likes = post.getKEY_likes();

                        if(likes.contains(user_like))
                        {
                            imageView2.setImageResource(R.drawable.ufi_heart);
                            likes.remove(likes.indexOf(user_like));
                        }
                        else
                        {
                            imageView2.setImageResource(R.drawable.ufi_heart_active);
                            likes.add(user_like);
                        }
                    }

                    Log.d("this","sdfasdf "+ likes.toString());
                    post.setKEY_likes(likes);
                    post.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if(e!=null)
                            {
                                Log.e("this","Error while saving",e);
                            }
                            Log.i("this","Post save was sucessfull!!");

                        }
                    });


                    update_likes(likes);

                }


            });



        }

        private void update_likes(ArrayList<String> likes) {
            String numberofLikes= String.valueOf(likes.size());

            if(likes.size() !=0)
            {

                num_likes.setVisibility(View.VISIBLE);
                num_likes.setText(numberofLikes);
            }
            else
            {
                num_likes.setVisibility(View.INVISIBLE);
                num_likes.setText(numberofLikes);
            }

        }

        private void LeaveComment(Post post) {
            comments_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, CommentActivity.class);
                    i.putExtra("post",post.getObjectId());
                    context.startActivity(i);
                    Animatoo.animateSlideLeft(context);
                }

            });
        }


    }


}