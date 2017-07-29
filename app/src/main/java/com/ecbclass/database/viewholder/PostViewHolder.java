package com.ecbclass.database.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecbclass.database.models.Post;
import com.google.firebase.quickstart.database.R;
import com.squareup.picasso.Picasso;

public class PostViewHolder extends RecyclerView.ViewHolder {
    public TextView titleView;
    public TextView authorView;
    public ImageView starView;
    public TextView numStarsView;
    public TextView bodyView;
    public ImageView authorImage;
    public TextView streamView;
    public TextView subjectView;

    public PostViewHolder(View itemView) {
        super(itemView);
        titleView = (TextView) itemView.findViewById(R.id.post_title);
        authorView = (TextView) itemView.findViewById(R.id.post_author);
        authorImage = (ImageView) itemView.findViewById(R.id.post_author_photo);
        starView = (ImageView) itemView.findViewById(R.id.star);
        numStarsView = (TextView) itemView.findViewById(R.id.post_num_stars);
        bodyView = (TextView) itemView.findViewById(R.id.post_body);
        streamView = (TextView) itemView.findViewById(R.id.post_stream);
        subjectView = (TextView) itemView.findViewById(R.id.post_subject);
    }

    public void bindToPost(Post post, View.OnClickListener starClickListener) {
        titleView.setText(post.title);
        authorView.setText(post.author);
        numStarsView.setText(String.valueOf(post.starCount));
        if (post.userImage.equalsIgnoreCase("phone")) {
            authorImage.setImageResource(R.drawable.blue_call_icon);

        } else {
            Picasso.with(itemView.getContext()).load(post.userImage)
                    .placeholder(R.drawable.google)
                    .error(R.drawable.ic_action_account_circle_40)
                    .into(authorImage);
        }
        bodyView.setText(post.body);
        streamView.setText(post.stream);
        subjectView.setText(post.subject);
        starView.setOnClickListener(starClickListener);
    }

}
