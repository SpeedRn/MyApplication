package com.example.sdpc.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.os.Looper;
import android.os.StrictMode;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.MApplication;
import com.stv.launcher.dev.watcher.RefWatcher;

import java.util.concurrent.Executor;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.schedulers.ExecutorScheduler;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by sdpc on 16-10-31.
 */

public class TestStateListDrawActivity extends Activity {
    private Button btnStartRx;
    private TextView tvContent;
    Thread thread;

    @Override
    protected void onResume() {
        super.onResume();
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {

                }
            }
        });
        thread.start();
    }
    @Override
    protected void onPause() {
        super.onPause();
        thread = null;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_state_list_draw);

        btnStartRx = (Button) findViewById(R.id.btn_start_rx);
        tvContent = (TextView) findViewById(R.id.tv_content);

//        btnStartRx.setOnClickListener(v -> {
//            Observable<String> observable = Observable.just("hello world");
//            observable
//                    .doOnSubscribe(disposable -> {
//                        for (int i = 0; i < 10; i++) {
//                            Thread.sleep(1000);
//                            System.out.println(i);
//                        }
//                    })
//                    .observeOn(Schedulers.from(MApplication.INSTANCE.uiThread))
//                    .subscribeOn(Schedulers.io())
//                    .doOnComplete(() -> {
//                        for (int i = 0; i < 10; i++) {
//                            Thread.sleep(1000);
//                            System.out.println("doOnComplete :" + i);
//                        }
//                    })
//                    .doOnNext(s -> {
//                        for (int i = 0; i < 10; i++) {
//                            Thread.sleep(1000);
//                            System.out.println("doOnNext :" + i);
//                        }
//                    })
//                    .subscribe(new Observer<String>() {
//                        @Override
//                        public void onSubscribe(Disposable d) {
//                            System.out.println("onSubscribe" + "Thread : " + Thread.currentThread());
//                        }
//
//                        @Override
//                        public void onNext(String value) {
//                            System.out.println("onNext :" + value + "Thread : " + Thread.currentThread());
//                            tvContent.setText("onNext :" + value + "Thread : " + Thread.currentThread());
//                        }
//
//                        @Override
//                        public void onError(Throwable e) {
//                            System.out.println("onError :" + e);
//                        }
//
//                        @Override
//                        public void onComplete() {
//                            System.out.println("onComplete" + "Thread : " + Thread.currentThread());
////                            Toast.makeText(getApplicationContext(), "onComplete", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
