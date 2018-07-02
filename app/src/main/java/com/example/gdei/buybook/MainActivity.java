package com.example.gdei.buybook;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class MainActivity extends Activity {

    private RecyclerView mBookList;

    private ArrayList<Book> bookList;
    private double sumMoney = 0;
    private TextView sumMoneyText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initData();

       MyRVAdapter adapter = new MyRVAdapter(bookList, this, new MyRVAdapter.OnBookARClickListener() {
            @Override
            public void addNum(MyRVAdapter.MyHolder myHolder,Book book) {
                if (book.getNum() <= 100000){
                    book.setNum(book.getNum()+1);
                    myHolder.mNum.setText(book.getNum()+"");
                    sumMoney += book.getPrice();
                    sumMoneyText.setText("￥"+sumMoney);
                } else {
                    Toast.makeText(MainActivity.this,"书籍数量不能大于100000哦", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void reduceNum(MyRVAdapter.MyHolder myHolder,Book book) {
                if (book.getNum() > 0){
                    book.setNum(book.getNum()-1);
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
    private void initView(){
        mBookList = findViewById(R.id.main_rv);
        sumMoneyText = findViewById(R.id.main_sum_money);

    }
    private void initData(){
        bookList = new ArrayList<>();
        bookList.add(new Book("haha",10,"123"));
        bookList.add(new Book("haha",100,"123"));
        bookList.add(new Book("haha",25.5,"123"));
        bookList.add(new Book("haha",25.5,"123"));
        bookList.add(new Book("haha",25.5,"123"));
        bookList.add(new Book("haha",25.5,"123"));
    }

    private ArrayList<Book> getBookList() {
        ArrayList<Book> arrayList;
        GetBookListThread getBookListThread = new GetBookListThread();
        FutureTask<ArrayList<Book>> task = new FutureTask<ArrayList<Book>>(getBookListThread);
        new Thread(task,"用返回值").start();
        try {
            arrayList = task.get();
            return arrayList;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return null;
    }

}
