package com.soulkey.mdlm.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.soulkey.mdlm.APICall.JsonGenerator;
import com.soulkey.mdlm.APICall.NetRetrofit;
import com.soulkey.mdlm.Activity.ActionActivity;
import com.soulkey.mdlm.Adapter.LinkAdapter;
import com.soulkey.mdlm.R;

import java.net.MalformedURLException;
import java.net.URL;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookmarkFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private LinkAdapter mAdapter;
    private JsonArray item_array = new JsonArray();

    public BookmarkFragment(){
    }

    public static BookmarkFragment newInstance(){
        return new BookmarkFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookmark, container, false);
        ActionBar actionbar = ((ActionActivity)getActivity()).getSupportActionBar();
        actionbar.setTitle("Bookmark");
        actionbar.setDisplayShowHomeEnabled(true);

        mRecyclerView = view.findViewById(R.id.recyclerview_bookmark);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new LinkAdapter(item_array);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_refresh:
                item.setEnabled(false);
                Log.d("muta", "refresh button called");
                Call<JsonElement> callback_data = NetRetrofit.getInstance().getService().Insert_Query(new JsonGenerator().makeSelectJSON("tb_bookmark_info", "link", ""));
                callback_data.enqueue(new Callback<JsonElement>() {
                    @Override
                    public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                        for(JsonElement element : response.body().getAsJsonArray()){
                            JsonObject obj = element.getAsJsonObject();
                            try {
                                URL domain = new URL(obj.get("link").getAsString());
                                obj.addProperty("title", domain.getAuthority());
                                item_array.add(obj);
                            } catch (MalformedURLException e) {
                                obj.addProperty("title", "Undefined");
                                item_array.add(obj);
                                e.printStackTrace();
                            }
                        }
                        mAdapter = new LinkAdapter(item_array, true);
                        mRecyclerView.setAdapter(mAdapter);
                    }

                    @Override
                    public void onFailure(Call<JsonElement> call, Throwable t) {
                        Log.d("muta", t.getMessage());
                    }
                });
                item.setEnabled(true);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.menu_action, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
}
