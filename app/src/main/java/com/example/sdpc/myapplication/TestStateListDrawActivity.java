package com.example.sdpc.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.os.Looper;
import android.os.MessageQueue;
import android.os.StrictMode;
import android.text.SpanWatcher;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.MApplication;
import com.stv.launcher.dev.watcher.RefWatcher;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.schedulers.ExecutorScheduler;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.ResourceSubscriber;

/**
 * Created by sdpc on 16-10-31.
 */

public class TestStateListDrawActivity extends Activity {
    private Button btnStartRx;
    private TextView tvContent;
    private TextView tvSpannble;
    private ProgressBar mProgressBar;
    Thread thread;
    private MIdleHandler idleHandler = new MIdleHandler();



    @Override
    protected void onResume() {
        super.onResume();
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
        tvSpannble = (TextView) findViewById(R.id.tv_content_title);
        mProgressBar = (ProgressBar) findViewById(R.id.progress);
        mProgressBar.setVisibility(View.VISIBLE);
        String name = "lLlkajsdfl发达奥就j" + "Desktop";
        SpannableString s = new SpannableString(name + "   V 2.8.4.5.2.3.4");
        AbsoluteSizeSpan sizeSpan = new AbsoluteSizeSpan(40);
        AbsoluteSizeSpan sizeSpan1 = new AbsoluteSizeSpan(20);
        s.setSpan(sizeSpan,0,name.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        s.setSpan(sizeSpan1,name.length(),s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvSpannble.setText(s);
        tvSpannble.setSelected(true);




        btnStartRx.setOnClickListener(v -> {
            Looper.myQueue().addIdleHandler(idleHandler);
//            Observable<Integer> observable = Observable.create(value -> {
//                System.out.println("onSubscribe" + "Thread : " + Thread.currentThread());
//                for (int i = 0; i < 10; i++) {
//                    try {
//                        Thread.sleep(1000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    System.out.println("doOnSubscribe :" + i);
//                }
//                value.onNext(1000);
//                value.onComplete();
//            });
//            observable.observeOn(Schedulers.from(MApplication.INSTANCE.uiThread))
//                    .subscribeOn(Schedulers.io())
//                    .subscribe(new Observer<Integer>() {
//                        @Override
//                        public void onSubscribe(Disposable d) {
//                        }
//
//                        @Override
//                        public void onNext(Integer value) {
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

//            Flowable<Long> flowable = Flowable.create(e -> Observable.interval(10, TimeUnit.SECONDS)
//                    .take(3)
//                    .subscribe(new Observer<Long>() {
//                        @Override
//                        public void onSubscribe(Disposable d) {
//                        }
//
//                        @Override
//                        public void onNext(Long value) {
//                            for (int i = 0; i < 10; i++) {
//                                try {
//                                    Thread.sleep(1000);
//                                } catch (InterruptedException e) {
//                                    e.printStackTrace();
//                                }
//                                System.out.println("doOnSubscribe :" + i);
//                            }
//                            e.onNext(value);
//                        }
//
//                        @Override
//                        public void onError(Throwable e) {
//
//                        }
//
//                        @Override
//                        public void onComplete() {
//
//                        }
//                    }), BackpressureStrategy.DROP);
//            flowable.observeOn(Schedulers.from(MApplication.INSTANCE.uiThread))
//                    .subscribeOn(Schedulers.io())
//                    .subscribeWith(new ResourceSubscriber<Long>() {
//
//                        @Override
//                        public void onNext(Long aLong) {
//                            System.out.println("onNext :" + aLong + "Thread : " + Thread.currentThread());
//                            tvContent.setText("onNext :" + aLong + "Thread : " + Thread.currentThread());
//                        }
//
//                        @Override
//                        public void onError(Throwable t) {
//
//                        }
//
//                        @Override
//                        public void onComplete() {
//
//                        }
//                    });
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private class MIdleHandler implements MessageQueue.IdleHandler{

        @Override
        public boolean queueIdle() {
            System.out.println("queue is idle!!!!!");
            return true;
        }
    }
}
