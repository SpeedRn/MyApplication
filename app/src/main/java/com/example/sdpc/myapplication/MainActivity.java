package com.example.sdpc.myapplication;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
<<<<<<< HEAD
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.LinearLayout;

import static android.support.v7.widget.LinearLayoutManager.HORIZONTAL;


public class MainActivity extends AppCompatActivity {
    private static String TAG = MainActivity.class.getSimpleName();
    RecyclerView rlvInUse;
    RecyclerView rlvToAdd;

=======
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;


public class MainActivity extends FragmentActivity {
    private static String TAG = MainActivity.class.getSimpleName();

>>>>>>> e7f4ebae383a114aab647db02350e6fe96aac1a1

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
<<<<<<< HEAD
        rlvInUse = (RecyclerView) findViewById(R.id.rlv_in_use);
        rlvInUse = (RecyclerView) findViewById(R.id.rlv_to_add);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        lm.setOrientation(LinearLayout.HORIZONTAL);
        rlvInUse.setLayoutManager(lm);

        rlvInUse.setAdapter();
    }

    @Override
    protected void  onPause() {
        super.onPause();
        Log.d(TAG,"onpause");
    }

    @Override
=======

    }

    @Override
    protected void  onPause() {
        super.onPause();
        Log.d(TAG,"onpause");
    }

    @Override
>>>>>>> e7f4ebae383a114aab647db02350e6fe96aac1a1
    protected void onStop() {
        super.onStop();
        Log.d(TAG,"onStop");
    }

}
