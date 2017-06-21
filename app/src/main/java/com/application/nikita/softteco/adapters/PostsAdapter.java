package com.application.nikita.softteco.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.application.nikita.softteco.R;
import com.application.nikita.softteco.entities.Post;

import java.util.ArrayList;

public class PostsAdapter extends BaseAdapter {

    Context mContext;
    ArrayList<Post> mPosts;
    LayoutInflater mLayoutInflater;

    public PostsAdapter(Context context, ArrayList<Post> posts) {
        this.mContext = context;
        this.mPosts = posts;
        this.mLayoutInflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mPosts.size();
    }

    @Override
    public Object getItem(int position) {
        return mPosts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = this.mLayoutInflater.inflate(R.layout.item_post, parent, false);
        }

        Post post = (Post) getItem(position);

        ((TextView) view.findViewById(R.id.postID)).setText(String.valueOf(post.getId()));
        ((TextView) view.findViewById(R.id.postTitle)).setText(String.valueOf(post.getTitle()));

        return view;
    }
}
