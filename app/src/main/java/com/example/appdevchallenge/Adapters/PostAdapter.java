package com.example.appdevchallenge.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appdevchallenge.EmailDetail;
import com.example.appdevchallenge.Model.EmailModel;
import com.example.appdevchallenge.R;

import java.util.ArrayList;

public class PostAdapter  extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    private ArrayList<EmailModel> mList;
    Context mContext;

    public PostAdapter(ArrayList<EmailModel> mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public TextView mTextView1;
        public TextView mTextView2;
        public TextView mTextView3;
        CardView img_user;
        LinearLayout container;

        public PostViewHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.Image);
            mTextView1 = itemView.findViewById(R.id.productName);
            mTextView2 = itemView.findViewById(R.id.productPrice);
            mTextView3 = itemView.findViewById(R.id.productDate);
            container = itemView.findViewById(R.id.linlay);
            img_user = itemView.findViewById(R.id.card_view);

        }
    }

    public PostAdapter(ArrayList<EmailModel> exampleList) {
        mList = exampleList;
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.emailcard,
                parent, false);
        PostViewHolder p = new PostViewHolder(v);
        return p;
    }

    @Override
    public void onBindViewHolder(@NonNull PostAdapter.PostViewHolder holder, final int position) {
        final EmailModel currentItem = mList.get(position);

        holder.img_user.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_transition_animation));
        //holder.container.setAnimation(AnimationUtils.loadAnimation(mContext,R.anim.fade_scale_animation));
        holder.mTextView1.setText(currentItem.getDesc());
        holder.mTextView2.setText(currentItem.getFrom());
        holder.mTextView3.setText(currentItem.getDate());
        final String message = currentItem.getMsg();
        final String date = currentItem.getDate();
        final String from = currentItem.getFrom();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), EmailDetail.class);
                intent.putExtra("Message", message);
                intent.putExtra("Date", date);
                intent.putExtra("From", from);
                intent.putExtra("Position", Integer.parseInt(String.valueOf(position)));
                v.getContext().startActivity(intent);
                Toast.makeText(v.getContext(), position + " ", Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void filterList(ArrayList<EmailModel> filteredList) {
        mList = filteredList;
        notifyDataSetChanged();
    }
}
