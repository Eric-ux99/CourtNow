package com.example.courtnowproject;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import androidx.recyclerview.widget.RecyclerView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class CourtAdapter extends RecyclerView.Adapter<CourtAdapter.MyViewHolder> {

    Context context;
    List<Court> courtList;
    List<CardView> cardViewList;


    public CourtAdapter(Context context, List<Court> courtList) {
        this.context = context;
        this.courtList = courtList;
        cardViewList = new ArrayList<>();

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.layout_court, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.courtName.setText(courtList.get(position).getName());

        if(!cardViewList.contains(holder.card_court))
            cardViewList.add(holder.card_court);

        holder.setRecyclerItemSelectedListener1(new RecyclerItemSelectedListener() {
            @Override
            public void onItemSelectedListener(View view, int pos) {
                for(CardView cardView : cardViewList)
                {
                    cardView.setCardBackgroundColor(context.getResources().getColor(R.color.yellow));
                }

                holder.card_court.setCardBackgroundColor(context.getResources().getColor(android.R.color.holo_orange_light));

                EventBus.getDefault().postSticky(new EnableNextButton(2, courtList.get(pos)));
            }
        });
    }

    @Override
    public int getItemCount() {
        return courtList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView courtName;
        CardView card_court;

        RecyclerItemSelectedListener recyclerItemSelectedListener1;

        public void setRecyclerItemSelectedListener1(RecyclerItemSelectedListener recyclerItemSelectedListener1) {
            this.recyclerItemSelectedListener1 = recyclerItemSelectedListener1;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            card_court = (CardView) itemView.findViewById(R.id.card_court);
            courtName = (TextView) itemView.findViewById(R.id.courtName);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            recyclerItemSelectedListener1.onItemSelectedListener(view, getAdapterPosition());
        }
    }
}
