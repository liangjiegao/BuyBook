package com.example.gdei.buybook;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;


public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";
    private RecyclerView mBookList;
    private Button submit;

    private ArrayList<Book> bookList;
    private double sumMoney = 0;
    private TextView sumMoneyText;

    private HashSet<Book> selectBook;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initData();
        setAdapter();

    }

    private void initView(){
        mBookList = findViewById(R.id.main_rv);
        sumMoneyText = findViewById(R.id.main_sum_money);
        submit = findViewById(R.id.main_submit_select);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(){

                    @Override
                    public void run() {

                        boolean isSuccessSub = SocketUtil.sendSubmitRequest(selectBook);
                        if (isSuccessSub){
                            Log.i(TAG, "run: ");
                            handler.sendEmptyMessage(0x123);
                        }
                    }
                }.start();


            }
        });

    }

    private void initData(){
        selectBook = new HashSet<>();
        bookList = getBookList();
        /*bookList.add(new Book("haha",10,"123"));
        bookList.add(new Book("haha",100,"123"));
        bookList.add(new Book("haha",25.5,"123"));
        bookList.add(new Book("haha",25.5,"123"));
        bookList.add(new Book("haha",25.5,"123"));
        bookList.add(new Book("haha",25.5,"123"));
        */
    }
    private MyRVAdapter adapter;
    private void setAdapter(){
        if (adapter != null){
            adapter = null;
        }
        adapter = new MyRVAdapter(bookList, this, new MyRVAdapter.OnBookARClickListener() {
            @Override
            public void addNum(MyRVAdapter.MyHolder myHolder,Book book,int pos) {
                if (book.getNum() <= 100000){
                    book.setNum(book.getNum()+1);
                    if (book.getNum() >0 ){
                        selectBook.add(book);
                    }

                    myHolder.mNum.setText(book.getNum()+"");
                    sumMoney += book.getPrice();
                    sumMoneyText.setText("￥"+sumMoney);
                } else {
                    Toast.makeText(MainActivity.this,"书籍数量不能大于100000哦", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void reduceNum(MyRVAdapter.MyHolder myHolder,Book book, int pos) {
                if (book.getNum() > 0){
                    book.setNum(book.getNum()-1);
                    if (book.getNum() > 0){
                        selectBook.add(book);
                    }
                    myHolder.mNum.setText(book.getNum()+"");
                    sumMoney -= book.getPrice();
                    sumMoneyText.setText("￥"+sumMoney);
                }else {
                    Toast.makeText(MainActivity.this,"书籍数量不能小于0哦", Toast.LENGTH_SHORT).show();
                }
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mBookList.setLayoutManager(layoutManager);
        DividerItemDecoration decoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST);
        mBookList.addItemDecoration(decoration);
        mBookList.setAdapter(adapter);
    }
    private ArrayList<Book> getBookList() {
        ArrayList<Book> arrayList = Store.getBooks();
        return arrayList;
    }
    private void cleanData(){
        sumMoneyText.setText("￥0.0");
        selectBook.clear();

        LoginThread loginThread = new LoginThread(User.getUserName(), User.getPassword());
        FutureTask<ArrayList<Book>> task = new FutureTask<ArrayList<Book>>(loginThread);
        new Thread(task).start();
        try {
            if (task.get() == null){
                Toast.makeText(MainActivity.this, "刷新失败！！", Toast.LENGTH_LONG).show();
            }else {
                Store.setBooks(task.get());
                handler.sendEmptyMessage(0x123);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0x123){
                Toast.makeText(MainActivity.this, "购买成功!",Toast.LENGTH_SHORT).show();
                cleanData();    //刷新
            }

        }
    };

}
