package com.example.courtnowproject;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import android.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import dmax.dialog.SpotsDialog;

public class Step4B extends Fragment {

    SimpleDateFormat simpleDateFormat;
    Unbinder unbinder;

    AlertDialog dialog;

    @BindView(R.id.bookingCourt)
    TextView bookingCourt;
    @BindView(R.id.bookingTime)
    TextView bookingTime;
    @BindView(R.id.complexAddress)
    TextView complexAddress;
    @BindView(R.id.complexName)
    TextView complexName;
    @BindView(R.id.complexTime)
    TextView complexTime;
    @BindView(R.id.complexPhone)
    TextView complexPhone;

    @OnClick(R.id.btn_confirm)
    void confirmBooking() {

        dialog.show();

        String startTime = Common.convertTimeSlotToString(Common.currentTimeSlot);
        String[] convertTime = startTime.split("-");
        String[] startTimeConvert = convertTime[0].split(":");
        int startHourInt = Integer.parseInt(startTimeConvert[0].trim());
        int startMinInt = Integer.parseInt(startTimeConvert[1].trim());

        Calendar bookingDateWithoutHouse = Calendar.getInstance();
        bookingDateWithoutHouse.setTimeInMillis(Common.bookingDate.getTimeInMillis());
        bookingDateWithoutHouse.set(Calendar.HOUR_OF_DAY,startHourInt);
        bookingDateWithoutHouse.set(Calendar.MINUTE, startMinInt);

        Timestamp timestamp = new Timestamp(bookingDateWithoutHouse.getTime());

        BookingInformation bookingInformation = new BookingInformation();

        bookingInformation.setTimestamp(timestamp);
        bookingInformation.setDone(false);
        bookingInformation.setCourtId(Common.currentCourt.getCourtId());
        bookingInformation.setCourtName(Common.currentCourt.getName());
        bookingInformation.setArea(Common.area);

        bookingInformation.setUsername(Common.currentUser.getUsername());
        bookingInformation.setUserId(Common.currentUser.getUserID());
        bookingInformation.setEmail(Common.currentUser.getEmail());

        bookingInformation.setComplexId(Common.currentComplex.getComplexId());
        bookingInformation.setComplexName(Common.currentComplex.getName());
        bookingInformation.setComplexAddress(Common.currentComplex.getAddress());
        bookingInformation.setDate(Common.simpleDateFormat.format(bookingDateWithoutHouse.getTime()));

        bookingInformation.setTime(new StringBuilder(Common.convertTimeSlotToString(Common.currentTimeSlot))
                .append(" at ")
                .append(simpleDateFormat.format(Common.bookingDate.getTime())).toString());
        bookingInformation.setSlot(Long.valueOf(Common.currentTimeSlot));

        bookingInformation.setBookingId(bookingInformation.getComplexName() + " (" + bookingInformation.getCourtName() + " - " + Common.convertTimeSlotToString(Common.currentTimeSlot) + " on " + bookingInformation.getDate() + ")");

        DocumentReference bookingDate =  FirebaseFirestore.getInstance()
                .collection("AllCourts")
                .document(Common.area)
                .collection("Complex")
                .document(Common.currentComplex.getComplexId())
                .collection("Courts")
                .document(Common.currentCourt.getCourtId())
                .collection(Common.simpleDateFormat.format(bookingDateWithoutHouse.getTime()))
                .document(String.valueOf(Common.currentTimeSlot));

        bookingDate.set(bookingInformation)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        addToUser(bookingInformation);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void addToUser(BookingInformation bookingInformation) {

       // String temp = bookingInformation.getComplexName() + " (" + bookingInformation.getCourtName() + " - " + Common.convertTimeSlotToString(Common.currentTimeSlot) + " on " + bookingInformation.getDate() + ")";

        CollectionReference userBooking = FirebaseFirestore.getInstance()
                .collection("User")
                .document(Common.currentUser.getUserID())
                .collection("Booking");

        CollectionReference allBooking = FirebaseFirestore.getInstance()
                        .collection("Booking");

        userBooking.whereEqualTo("done", true).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.getResult().isEmpty())
                        {
                            userBooking.document(bookingInformation.getBookingId())
                                    .set(bookingInformation)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {

                                            allBooking.document(bookingInformation.getBookingId())
                                                    .set(bookingInformation)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            if(dialog.isShowing())
                                                                dialog.dismiss();

                                                            reset();
                                                            getActivity().finish();
                                                            Toast.makeText(getContext(), "Court Booked Successfully!", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            if(dialog.isShowing())
                                                                dialog.dismiss();

                                                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                            if(dialog.isShowing())
                                                dialog.dismiss();

                                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                        else
                        {
                            if(dialog.isShowing())
                                dialog.dismiss();

                            reset();
                            getActivity().finish();
                            Toast.makeText(getContext(), "Court Booked Successfully!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    private void reset() {
        Common.step = 0;
        Common.currentTimeSlot = -1;
        Common.currentComplex = null;
        Common.currentCourt = null;
        Common.bookingDate.add(Calendar.DATE, 0);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void setDataBooking(ConfirmBookingEvent event)
    {
        if(event.isConfirm())
        {
            setData();
        }
    }


    private void setData() {
        bookingCourt.setText(Common.currentCourt.getName());
        bookingTime.setText(new StringBuilder(Common.convertTimeSlotToString(Common.currentTimeSlot))
                .append(" at ").append(simpleDateFormat.format(Common.bookingDate.getTime())));

        complexAddress.setText(Common.currentComplex.getAddress());
        complexName.setText(Common.currentComplex.getName());
        complexTime.setText(Common.currentComplex.getOpenHours());
        complexPhone.setText(Common.currentComplex.getPhone());
    }


    static Step4B instance;

    public static Step4B getInstance() {
        if(instance == null)
            instance = new Step4B();
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        dialog = new SpotsDialog.Builder().setContext(getContext()).setCancelable(false).build();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        super.onCreateView(inflater, container, savedInstanceState);

        View itemView = inflater.inflate(R.layout.fragment_step4,container,false);
        unbinder = ButterKnife.bind(this, itemView);

        return itemView;
    }

}
