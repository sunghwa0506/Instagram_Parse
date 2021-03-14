package com.example.instagram_parse;

import android.content.Context;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;
import java.util.Stack;

import static android.content.ContentValues.TAG;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder> {

    private Context context;
    private List<ParseObject> comments;



    public CommentsAdapter(Context context, List<ParseObject> comments)
    {
        this.context = context;
        this.comments = comments;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.comment_item,parent,false);
        return new CommentsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ParseObject comment = comments.get(position);
        holder.bind(comment);


    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView User;
        private TextView comment;
        private ImageView profile;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            User = itemView.findViewById(R.id.User);
            comment = itemView.findViewById(R.id.comment);
            profile = itemView.findViewById(R.id.profile);
        }

        public void bind(ParseObject comments) {
            comment.setText(comments.getString("Comments"));
            ParseObject comuser = comments.getParseUser("User");

            
            ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
            query.getInBackground(comuser.getObjectId(), (object, e) -> {
                if (e == null) {
                    User.setText(object.getString("username"));
                    ParseFile temp = object.getParseFile("profile");
                    Glide.with(context).load(temp.getUrl()).into(profile);
                    Log.d("asdf","biding: "+User.getText().toString());
                } else {
                    Log.d("asdf","biding: ");

                }
            });

        }


    }

}
