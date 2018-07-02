package com.example.gdei.buybook;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by gdei on 2018/7/2.
 */

public class MyRVAdapter extends RecyclerView.Adapter<MyRVAdapter.MyHolder>{


    private ArrayList<Book> bookList;
    private Context mContext;
    private OnBookARClickListener mListener;
    public ArrayList<MyHolder> myHolders;
    public MyRVAdapter(ArrayList<Book> bookList, Context context, OnBookARClickListener listener){
        this.bookList = bookList;
        mContext = context;
        mListener = listener;
        myHolders = new ArrayList<>();
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        MyHolder holder = new MyHolder
                (LayoutInflater.from(mContext).inflate(R.layout.item_book,parent,false));

        myHolders.add(holder);

        return holder;
    }

    @Override
    public void onBindViewHolder(final MyHolder holder, final int position) {
        final Book book = bookList.get(position);
        byte[] imgbyte = DecodeImageUtil.decodeStringToBitmap(book.getImg());
        Bitmap bitmap = BitmapFactory.decodeByteArray(imgbyte,0,imgbyte.length);
        //holder.mBookImg.setImageBitmap(bitmap);
        holder.mBookName.setText(book.getBookName());
        holder.mNewPrice.setText("￥"+book.getPrice());
        holder.mOldPrice.setText("￥"+(book.getPrice()+book.getPrice()*0.1));
        holder.mNum.setText("0");
        holder.mReduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.reduceNum(holder, book);
            }
        });
        holder.mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.addNum(holder,book);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }


    class MyHolder extends RecyclerView.ViewHolder{

        public ImageView mBookImg;
        public TextView mBookName;
        public ImageButton mReduce, mAdd;
        public TextView mNewPrice, mOldPrice;
        public TextView mNum;

        public MyHolder(View itemView) {
            super(itemView);
            mBookImg = itemView.findViewById(R.id.item_book_img);
            mBookName = itemView.findViewById(R.id.item_book_title);
            mReduce = itemView.findViewById(R.id.item_book_reduce);
            mAdd = itemView.findViewById(R.id.item_book_add);
            mNewPrice = itemView.findViewById(R.id.item_book_newprice);
            mOldPrice = itemView.findViewById(R.id.item_book_oldprice);
            mNum = itemView.findViewById(R.id.item_book_num);
        }
    }
    interface OnBookARClickListener{

        void addNum(MyHolder myHolder,Book book);

        void reduceNum(MyHolder myHolder, Book book);

    }
}
