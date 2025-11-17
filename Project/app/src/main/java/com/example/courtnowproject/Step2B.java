package com.example.courtnowproject;


import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import javax.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class Step2B extends Fragment {

    Unbinder unbinder;

    @BindView(R.id.recycler_court)
    RecyclerView recycler_court;


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

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void setCourtAdapter(CourtDoneEvent event)
    {
        CourtAdapter adapter = new CourtAdapter(getContext(),event.getCourtList());
        recycler_court.setAdapter(adapter);
    }

    static Step2B instance;

    public static Step2B getInstance() {
        if(instance == null)
            instance = new Step2B();
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        super.onCreateView(inflater, container, savedInstanceState);

        View itemView = inflater.inflate(R.layout.fragment_step2,container,false);

        unbinder = ButterKnife.bind(this, itemView);

        initView();



        return itemView;
    }

    private void initView() {
        recycler_court.setHasFixedSize(true);
        recycler_court.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recycler_court.addItemDecoration(new SpacesItemDecoration(4));
    }
}
