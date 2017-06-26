package com.application.nikita.softteco.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.GridView;

import com.application.nikita.softteco.R;
import com.application.nikita.softteco.adapters.PostsAdapter;
import com.application.nikita.softteco.entities.GridItem;

import java.util.ArrayList;

public class GridFragment extends Fragment {

    int mNum;
    int mFirstPost = 0;
    int mPostsCount = -1;
    ArrayList<GridItem> mPostsList;

    @SuppressWarnings("unchecked")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        mNum = ((args != null) ? args.getInt("postsCount") : 0);

        if (args != null) {
            mPostsList = (ArrayList<GridItem>) args.getSerializable("postsList");
            mFirstPost = args.getInt("firstPost");
        }
        int numRows = getResources().getInteger(R.integer.num_rows);
        int numColumns = getResources().getInteger(R.integer.num_cols);
        int numPostsPerPage = numRows * numColumns;
        mPostsCount = numPostsPerPage;

        mFirstPost = (mFirstPost / numPostsPerPage) * numPostsPerPage;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        View rootView = getView();
        final GridView gridView = rootView.findViewById(R.id.gridView);

        ViewTreeObserver treeObserver = gridView.getViewTreeObserver();
        treeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                gridView.setColumnWidth(gridView.getWidth() / gridView.getNumColumns());
                gridView.setAdapter(new PostsAdapter(getActivity(), mPostsList, mFirstPost, mPostsCount));
                gridView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.grid_view, container, false);
        return view;
    }
}
