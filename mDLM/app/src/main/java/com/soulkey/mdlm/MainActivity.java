package com.soulkey.mdlm;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.gson.JsonElement;
import com.soulkey.mdlm.Model.NetRetrofit;
import com.soulkey.mdlm.View.PlaceholderFragment;
import com.soulkey.mdlm.View.SectionsPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private List<String> list = new ArrayList<>();
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(2);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final Snackbar notice = Snackbar.make(view, "Loading Item from Sever..", Snackbar.LENGTH_INDEFINITE);
                mViewPager.beginFakeDrag();
                //view.setVisibility(View.GONE);
                fab.hide();
                notice.show();
                Log.d("muta", "Retrofit called on Fragment "+String.valueOf(mViewPager.getCurrentItem()));
                final PlaceholderFragment current = (PlaceholderFragment) mViewPager.getAdapter().instantiateItem(mViewPager, mViewPager.getCurrentItem());
                current.showLoading();
                Call<JsonElement> callback_Data = null;
                switch (mViewPager.getCurrentItem()){
                    case 0:
                        //callback_Data = NetRetrofit.getInstance().getService().CallData_HRM();
                        break;
                    case 1:
                        callback_Data = NetRetrofit.getInstance().getService().CallData_BP();
                        break;
                    case 2:
                        callback_Data = NetRetrofit.getInstance().getService().CallData_DD();
                        break;
                }
                if(callback_Data!=null){
                    callback_Data.enqueue(new Callback<JsonElement>() {
                        @Override
                        public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                            list.clear();
                            JsonElement body = response.body();
                            for(JsonElement je : body.getAsJsonArray()){
                                list.add(je.getAsString());
                            }
                            current.UpdateList(list);
                            current.hideLoading();
                            mViewPager.endFakeDrag();
                            //view.setVisibility(View.VISIBLE);
                            fab.show();
                            notice.dismiss();
                        }
                        @Override
                        public void onFailure(Call<JsonElement> call, Throwable t) {
                            notice.dismiss();
                            current.hideLoading();
                            mViewPager.endFakeDrag();
                            //view.setVisibility(View.VISIBLE);
                            fab.show();
                            Snackbar.make(view, "Fail to Connect Server!", Snackbar.LENGTH_SHORT);
                            Log.e("muta", t.getMessage());
                        }
                    });
                }else{
                    current.hideLoading();
                    mViewPager.endFakeDrag();
                    //view.setVisibility(View.VISIBLE);
                    fab.show();
                    notice.dismiss();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
