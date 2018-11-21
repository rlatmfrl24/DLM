package com.soulkey.mdlm.View;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pnikosis.materialishprogress.ProgressWheel;
import com.soulkey.mdlm.Presenter.LinkAdapter;
import com.soulkey.mdlm.R;

import java.util.ArrayList;
import java.util.List;

public class PlaceholderFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private ProgressWheel progressWheel;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private LinkAdapter mAdapter;
    private List<String> list = new ArrayList<>();

    public PlaceholderFragment() {
    }

    public static PlaceholderFragment newInstance(int sectionNumber) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("muta", "Call onCreateView");
        View rootView = inflater.inflate(R.layout.fragment_recycler, container, false);
        progressWheel = rootView.findViewById(R.id.progress_wheel);
        mRecyclerView = rootView.findViewById(R.id.recyclerview);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        //mRecyclerView.setHasFixedSize(true);
        mAdapter = new LinkAdapter(list);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        hideLoading();
        return rootView;
    }

    public void UpdateList(List<String> updated_list){
        this.list = updated_list;
        mAdapter = new LinkAdapter(list);
        mRecyclerView.setAdapter(mAdapter);
    }

    public void showLoading(){
        mRecyclerView.setVisibility(View.GONE);
        progressWheel.setVisibility(View.VISIBLE);
    }
    public void hideLoading(){
        mRecyclerView.setVisibility(View.VISIBLE);
        progressWheel.setVisibility(View.GONE);
    }
}