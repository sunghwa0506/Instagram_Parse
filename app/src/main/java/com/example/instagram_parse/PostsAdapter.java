package com.example.instagram_parse;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputLayout;
import com.parse.ParseFile;

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


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userID = itemView.findViewById(R.id.userID);
            image_post = itemView.findViewById(R.id.image_post);
            post_description = itemView.findViewById(R.id.post_description);


        }

        public void bind(Post post) {
            post_description.setText(post.getKeyDescription());
            userID.setText(post.getUser().getUsername());
            ParseFile image = post.getImage();
            if(image != null)
            {
             Glide.with(context).load(image.getUrl()).into(image_post);
            }
        }
    }
}
