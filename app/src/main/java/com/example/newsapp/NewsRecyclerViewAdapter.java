package com.example.newsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NewsRecyclerViewAdapter  extends RecyclerView.Adapter<NewsRecyclerViewAdapter.ViewHolder>{

    private List<String> mData;
    private LayoutInflater mLayoutInflater;
    private ItemClickListener mItemClickListener;

    NewsRecyclerViewAdapter(Context context, List<String> data, ItemClickListener mItemClickListener) {
        this.mData = data;
        this.mLayoutInflater = LayoutInflater.from(context);
        this.mItemClickListener = mItemClickListener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.news_recycler_view,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String data = mData.get(position);
        holder.titleTextView.setText(data);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        TextView titleTextView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.tvTitle);
            titleTextView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(mItemClickListener != null) {
                mItemClickListener.onItemClick(view, getAdapterPosition());
            }
        }
    }

    public interface ItemClickListener{
        void onItemClick(View view, int position);
    }
}
