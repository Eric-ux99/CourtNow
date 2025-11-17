package com.example.courtnowproject;

import android.content.Context;
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

public class ComplexAdapter extends RecyclerView.Adapter<ComplexAdapter.MyViewHolder> {

    Context context;
    List<Complex> complexList;
    List<CardView> cardViewList;

    public ComplexAdapter(Context context, List<Complex> complexList){
        this.context = context;
        this.complexList = complexList;
        cardViewList = new ArrayList<>();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.layout_complex, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.txtName.setText(complexList.get(position).getName());
        holder.txtAddress.setText(complexList.get(position).getAddress());

        if(!cardViewList.contains(holder.card_complex))
            cardViewList.add(holder.card_complex);

        holder.setRecyclerItemSelectedListener1(new RecyclerItemSelectedListener() {
            @Override
            public void onItemSelectedListener(View view, int pos) {
                for(CardView cardView:cardViewList)
                    cardView.setCardBackgroundColor(context.getResources().getColor(R.color.yellow));

                holder.card_complex.setCardBackgroundColor(context.getResources().getColor(android.R.color.holo_orange_light));

                EventBus.getDefault().postSticky(new EnableNextButton(1, complexList.get(pos)));
            }

        });

    }

    @Override
    public int getItemCount() {
        return complexList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtName, txtAddress;
        CardView card_complex;

        RecyclerItemSelectedListener recyclerItemSelectedListener1;

        public void setRecyclerItemSelectedListener1(RecyclerItemSelectedListener recyclerItemSelectedListener1){
            this.recyclerItemSelectedListener1 = recyclerItemSelectedListener1;
        }


        public MyViewHolder(@NonNull View itemView){
            super(itemView);

            card_complex = (CardView)itemView.findViewById(R.id.card_complex);
            txtAddress = (TextView) itemView.findViewById(R.id.txtComplexDetails);
            txtName = (TextView) itemView.findViewById(R.id.txtComplexName);

            itemView.setOnClickListener(this);

        }


        @Override
        public void onClick(View view) {
            recyclerItemSelectedListener1.onItemSelectedListener(view,getAdapterPosition());
        }
    }
}
