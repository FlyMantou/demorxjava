package com.example.demorxjava;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private Observable<String> observable;
    private Subscriber<String> subscriber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //1.可观察对象产生数据
        observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try{
                    Thread.sleep(3000);//io
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                printThread("OnSubscribe-call");
                subscriber.onNext("this is message");
                subscriber.onCompleted();
            }
        });
        subscriber = new Subscriber<String>() {
            @Override
            public void onCompleted() {
                Toast.makeText(MainActivity.this,"接收完成",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(MainActivity.this,"接收异常",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(String data) {
                Toast.makeText(MainActivity.this,"数据："+data,Toast.LENGTH_SHORT).show();
            }
        };

    }

    public void start(View v){
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }
    private void printThread(String message){
        System.out.println("method:"+message+"current Thread:"+Thread.currentThread().getName());
    }
}
