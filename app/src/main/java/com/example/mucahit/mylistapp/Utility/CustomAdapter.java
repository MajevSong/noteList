package com.example.mucahit.mylistapp.Utility;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mucahit.mylistapp.Activity.MainActivity;
import com.example.mucahit.mylistapp.Activity.SecondActivity;
import com.example.mucahit.mylistapp.Data.noteList;
import com.example.mucahit.mylistapp.R;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    public List<noteList> dataSet;
    Context context;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private CardView cardView;
        TextView textViewTitle;
        TextView textViewContent;
        TextView textViewTime;
        TextView textViewDate;
        ImageView imageViewImage;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.textViewTitle = (TextView) itemView.findViewById(R.id.textTitleStatus);
            this.textViewContent = (TextView) itemView.findViewById(R.id.textContentStatus);
            this.textViewTime = (TextView)itemView.findViewById(R.id.textTimeStatus);
            this.textViewDate = (TextView)itemView.findViewById(R.id.textDateStatus);
            this.imageViewImage = (ImageView) itemView.findViewById(R.id.imageView);
            cardView = (CardView)itemView.findViewById(R.id.card_view);
        }

    }

    public CustomAdapter(List<noteList> data, Context context) {

        this.dataSet = data;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_cardview_layout, parent, false);

        view.setOnClickListener(MainActivity.myOnClickListener);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        TextView textViewTitle = holder.textViewTitle;
        TextView textViewContent = holder.textViewContent;
        TextView textViewTime = holder.textViewTime;
        TextView textViewDate = holder.textViewDate;
        ImageView imageView = holder.imageViewImage;

        textViewTitle.setText(dataSet.get(listPosition).getTitle());
        textViewContent.setText(dataSet.get(listPosition).getContent());
        textViewTime.setText(dataSet.get(listPosition).getTime());
        textViewDate.setText(dataSet.get(listPosition).getDate());

        if(dataSet.get(listPosition).getImage() != null){
            imageView.setImageBitmap(BitmapFactory.decodeByteArray(dataSet.get(listPosition).getImage(), 0, dataSet.get(listPosition).getImage().length));
        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                noteUpdate(listPosition);

            }
        });


    }

    public void noteUpdate(final int listPosition){

        Intent mintent = new Intent(context,SecondActivity.class);
        mintent.putExtra("id",dataSet.get(listPosition).getId());
        mintent.putExtra("title",dataSet.get(listPosition).getTitle());
        mintent.putExtra("content",dataSet.get(listPosition).getContent());
        mintent.putExtra("time",dataSet.get(listPosition).getTime());
        mintent.putExtra("date",dataSet.get(listPosition).getDate());
        mintent.putExtra("notification",dataSet.get(listPosition).getNotification());
        //mintent.putExtra("image",BitmapFactory.decodeByteArray(dataSet.get(listPosition).getImage(), 0, dataSet.get(listPosition).getImage().length));
        context.startActivity(mintent);

    }

    @Override
    public int getItemCount() {

        return dataSet.size();
    }

}