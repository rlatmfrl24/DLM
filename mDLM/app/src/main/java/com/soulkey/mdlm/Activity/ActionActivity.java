package com.soulkey.mdlm.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.soulkey.mdlm.Fragment.BookmarkFragment;
import com.soulkey.mdlm.Fragment.SettingFragment;
import com.soulkey.mdlm.R;

public class ActionActivity extends AppCompatActivity {

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        intent = getIntent();
        String action = intent.getStringExtra("action");
        switch (action){
            case "bookmark":
                fragmentTransaction.replace(R.id.action_container, BookmarkFragment.newInstance());
                break;
            case "setting":
                fragmentTransaction.replace(R.id.action_container, SettingFragment.newInstance());
                break;
        }
        fragmentTransaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
