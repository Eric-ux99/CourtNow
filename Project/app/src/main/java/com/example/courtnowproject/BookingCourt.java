package com.example.courtnowproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.shuhart.stepview.StepView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dmax.dialog.SpotsDialog;

public class BookingCourt extends AppCompatActivity {

    CollectionReference CourtRef;
    AlertDialog dialog;

    @BindView(R.id.step_view)
    StepView stepView;
    @BindView(R.id.view_pager)
    NonSwipe viewPager;
    @BindView(R.id.btn_previous)
    Button btn_previous;
    @BindView(R.id.btn_next)
    Button btn_next;

    @OnClick(R.id.btn_previous)
    void previousClick(){
        if(Common.step == 3 || Common.step > 0)
        {
            Common.step--;
            viewPager.setCurrentItem(Common.step);
            if(Common.step < 3)
            {
                btn_next.setEnabled(true);
                setColorButton();
            }
        }
    }


    @OnClick(R.id.btn_next)
    void nextClick(){
       if(Common.step < 3 || Common.step == 0)
       {
           Common.step++;
           if(Common.step == 1)
           {
               if(Common.currentComplex != null)
                   loadCourtByComplex(Common.currentComplex.getComplexId());
           }
           else if(Common.step == 2)
           {
               if(Common.currentCourt != null)
                   loadTimeSlotOfCourt(Common.currentCourt.getCourtId());
           }
           else if(Common.step == 3)
           {
               if(Common.currentTimeSlot != -1)
                   confirmBooking();
           }
           viewPager.setCurrentItem(Common.step);
       }
    }

    private void confirmBooking() {

        EventBus.getDefault().postSticky(new ConfirmBookingEvent(true));
    }

    private void loadTimeSlotOfCourt(String courtId) {

        EventBus.getDefault().postSticky(new DisplayTimeSlotEvent(true));
    }

    private void loadCourtByComplex(String complexId) {
        dialog.show();

        if ((!TextUtils.isEmpty(Common.area)))
        {
            CourtRef = FirebaseFirestore.getInstance()
                    .collection("AllCourts")
                    .document(Common.area)
                    .collection("Complex")
                    .document(complexId)
                    .collection("Courts");

            CourtRef.get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            ArrayList<Court> courts = new ArrayList<>();
                            for(QueryDocumentSnapshot courtSnapShot:task.getResult())
                            {
                                Court court = courtSnapShot.toObject(Court.class);
                                court.setCourtId(courtSnapShot.getId());

                                courts.add(court);
                            }

                            EventBus.getDefault().postSticky(new CourtDoneEvent(courts));

                            dialog.dismiss();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            dialog.dismiss();
                        }
                    });
        }


    }


    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void buttonNextReceiver(EnableNextButton event){

        int step = event.getStep();
        if(step == 1)
            Common.currentComplex = event.getComplex();
        else if(step == 2)
            Common.currentCourt = event.getCourt();
        else if(step == 3)
            Common.currentTimeSlot = event.getTimeSlot();


        btn_next.setEnabled(true);
        setColorButton();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_court);

        getSupportActionBar().setTitle("CourtNow Court Booking");

        ButterKnife.bind(BookingCourt.this);

        dialog = new SpotsDialog.Builder().setContext(this).setCancelable(false).build();

        setupStepView();
        setColorButton();

        viewPager.setAdapter(new MyViewPagerAdapter(getSupportFragmentManager()));
        viewPager.setOffscreenPageLimit(4);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                stepView.go(position, true);
                if( position == 0)
                    btn_previous.setEnabled(false);
                else
                    btn_previous.setEnabled(true);


                btn_next.setEnabled(false);
                setColorButton();

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    private void setColorButton() {
        if(btn_next.isEnabled())
        {
            btn_next.setBackgroundColor(getResources().getColor(R.color.yellow));
        }else{
            btn_next.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
        }

        if(btn_previous.isEnabled())
        {
            btn_previous.setBackgroundColor(getResources().getColor(R.color.yellow));
        }else{
            btn_previous.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
        }
    }

    private void setupStepView() {
        List<String> stepList = new ArrayList<>();
        stepList.add("Complex");
        stepList.add("Courts");
        stepList.add("Time");
        stepList.add("Confirm");
        stepView.setSteps(stepList);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}