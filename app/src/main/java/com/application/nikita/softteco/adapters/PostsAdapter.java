package com.application.nikita.softteco.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.application.nikita.softteco.R;
import com.application.nikita.softteco.activities.UserInfoActivity;
import com.application.nikita.softteco.entities.GridItem;

import java.util.ArrayList;

public class PostsAdapter extends BaseAdapter implements View.OnClickListener {

    private Context mContext;
    private int mPostsOffset = 0;
    private int mPostCount = -1;
    private int mNum = 0;
    private ArrayList<GridItem> mPosts;

    public PostsAdapter(Activity activity, ArrayList<GridItem> postsList, int postsOffset, int postsCount) {
        mPosts = postsList;
        mContext = activity;
        mPostsOffset = postsOffset;
        mPostCount = postsCount;
        mNum = (postsList == null) ? 0 : postsList.size();
    }

    @Override
    public int getCount() {
        int count = mPostCount;
        if (mPostsOffset + mPostCount >= mNum)
            count = mNum - mPostsOffset;
        return count;
    }

    @Override
    public Object getItem(int position) {
        return mPosts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mPostsOffset + position;
    }

    private class ViewHolder {
        TextView gridItemId;
        TextView gridItemTitle;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = ((Activity) mContext).getLayoutInflater().inflate(R.layout.item_post, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.gridItemId = convertView.findViewById(R.id.postID);
            viewHolder.gridItemTitle = convertView.findViewById(R.id.postTitle);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.gridItemId.setText(mPosts.get(mPostsOffset + position).getId());
        viewHolder.gridItemTitle.setText(mPosts.get(mPostsOffset + position).getTitle());

        viewHolder.gridItemId.setOnClickListener(this);
        viewHolder.gridItemId.setTag(mPostsOffset + position);
        viewHolder.gridItemTitle.setOnClickListener(this);
        viewHolder.gridItemTitle.setTag(mPostsOffset + position);

        convertView.setTag(viewHolder);
        return convertView;
    }

    @Override
    public void onClick(View view) {
        if(view != null) {
            if(view.getTag() != null) {
                Log.d("On Grid Item Click", "Item position" + view.getTag());
                Intent intent = new Intent();
                intent.setClass(mContext, UserInfoActivity.class);
                intent.putExtra("post_ID", mPosts.get((int) view.getTag()).getId());
                intent.putExtra("user_ID", mPosts.get((int) view.getTag()).getUserId());
                mContext.startActivity(intent);
            }
        }
    }
}
