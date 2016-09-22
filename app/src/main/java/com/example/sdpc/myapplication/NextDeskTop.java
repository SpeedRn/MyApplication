package com.example.sdpc.myapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.sdpc.myapplication.adapter.GridAdapter;
import com.example.sdpc.myapplication.manager.ItemSpaceDecoration;
import com.example.sdpc.myapplication.manager.NextDeskLayoutManager;

import java.util.ArrayList;

/**
 * Created by sdpc on 16-8-25.
 */
public class NextDeskTop extends AppCompatActivity {
    ArrayList<String> data = new ArrayList<>();
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next_desktop);

        mRecyclerView = (RecyclerView) findViewById(R.id.rlv_next);
        mRecyclerView.setLayoutManager(new NextDeskLayoutManager(this,4, LinearLayoutManager.VERTICAL,false));

        mRecyclerView.setAdapter(new GridAdapter(this));
        mRecyclerView.addItemDecoration(new ItemSpaceDecoration(this));
    }

}
