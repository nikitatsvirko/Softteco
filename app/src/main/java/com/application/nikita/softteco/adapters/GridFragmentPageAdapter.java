package com.application.nikita.softteco.adapters;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.application.nikita.softteco.R;
import com.application.nikita.softteco.entities.GridItem;
import com.application.nikita.softteco.fragments.GridFragment;

import java.util.ArrayList;

public class GridFragmentPageAdapter  extends FragmentStatePagerAdapter{
    private int mItemsCount = 0;
    private int mFragmentsCount = 0;
    private ArrayList<GridItem> mPostsList;

    private Resources resources;

    private static final int DEFAULT_FRAGMENTS_COUNT = 1;
    private static final int DEFAULT_ITEMS_COUNT = 1;

    public GridFragmentPageAdapter(FragmentManager fm, ArrayList<GridItem> list, Resources res) {
        super(fm);
        mPostsList = list;
        resources = res;
        setUp();
    }

    @Override
    public Fragment getItem(int position) {
        Bundle args = new Bundle();
        args.putInt("firstPost", position * mItemsCount);

        int postCount = mItemsCount;
        if (position == (mFragmentsCount - 1)) {
            int numTopics = mPostsList.size();
            int rem = numTopics % mItemsCount;
            if (rem > 0) {
                postCount = rem;
            }
        }
        args.putInt("postsCount", postCount);
        args.putSerializable("postsList", mPostsList);

        GridFragment fragment = new GridFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public int getCount() {
        return mFragmentsCount;
    }

    private void setUp() {
        if (mPostsList == null) {
            mItemsCount = DEFAULT_ITEMS_COUNT;
            mFragmentsCount = DEFAULT_FRAGMENTS_COUNT;
        } else {
            int postsCount = mPostsList.size();
            int rowsCount = resources.getInteger(R.integer.num_rows);
            int columnsCount = resources.getInteger(R.integer.num_cols);
            int topicsPerPageCount = rowsCount * columnsCount;
            int fragmentsCount = postsCount / topicsPerPageCount;

            if (postsCount % topicsPerPageCount != 0)
                fragmentsCount++;

            mFragmentsCount = fragmentsCount;
            mItemsCount = topicsPerPageCount;
        }
    }
}
