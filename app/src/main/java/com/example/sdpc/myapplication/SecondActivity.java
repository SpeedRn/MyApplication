package com.example.sdpc.myapplication;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

public class SecondActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_activity);

        WebView v = (WebView) findViewById(R.id.webview);
        assert v != null;
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("webview ");
            }
        });
        WebViewClient client = new WebViewClient();
        client.shouldOverrideUrlLoading(v,"http://www.baidu.com");
        v.setWebViewClient(client);
        v.loadUrl("http://www.baidu.com");

        MyTask task = new MyTask();
        task.execute();
    }


    class MyTask extends AsyncTask<String,String,String>{
        @Override
        protected String doInBackground(String... params) {
            SystemClock.sleep(20000);
            return null;
        }
    }

}
