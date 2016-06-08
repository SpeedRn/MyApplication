package com.example.sdpc.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println("SD has update this File");
        System.out.println("SD has update this File AGAIG！！！But this time he will use commond line to commit this file  hohoho!!!");
        System.out.println("Third time just for fun!");
    }
}
