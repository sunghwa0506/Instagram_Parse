package com.example.instagram_parse;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputLayout;
import com.parse.GetCallback;
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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userID = itemView.findViewById(R.id.userID);
            image_post = itemView.findViewById(R.id.image_post);
            post_description = itemView.findViewById(R.id.post_description);
            imageView2 = itemView.findViewById(R.id.imageView2);
            num_likes = itemView.findViewById(R.id.num_likes);
            profile_image_for_post = itemView.findViewById(R.id.profile_image_for_post);
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
    }

}