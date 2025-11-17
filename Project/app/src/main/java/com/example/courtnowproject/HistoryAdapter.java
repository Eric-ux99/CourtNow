package com.example.courtnowproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder> {

    Context context;
    List<BookingInformation> bookingInformationList;

    public HistoryAdapter(Context context, List<BookingInformation> bookingInformationList) {
        this.context = context;
        this.bookingInformationList = bookingInformationList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.layout_history, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.textviewBookingCourtText.setText(bookingInformationList.get(position).getCourtName());
        holder.textviewBookingTimeText.setText(bookingInformationList.get(position).getTime());
        holder.textviewComplexAddress.setText(bookingInformationList.get(position).getComplexAddress());
        holder.textviewComplexName.setText(bookingInformationList.get(position).getComplexName());
        holder.textviewBookingDate.setText(bookingInformationList.get(position).getDate());

    }

    @Override
    public int getItemCount() {

        return bookingInformationList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        Unbinder unbinder;

        @BindView(R.id.textviewComplexName)
        TextView textviewComplexName;
        @BindView(R.id.textviewComplexAddress)
        TextView textviewComplexAddress;
        @BindView(R.id.textviewBookingTimeText)
        TextView textviewBookingTimeText;
        @BindView(R.id.textviewBookingCourtText)
        TextView textviewBookingCourtText;
        @BindView(R.id.textviewBookingDate)
        TextView textviewBookingDate;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            unbinder = ButterKnife.bind(this, itemView);
        }
    }
}
