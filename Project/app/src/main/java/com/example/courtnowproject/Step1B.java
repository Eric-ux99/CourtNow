package com.example.courtnowproject;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dmax.dialog.SpotsDialog;

public class Step1B extends Fragment implements AllAreaLoadListener, ComplexLoadListener {

    CollectionReference allAreaRef;
    CollectionReference allComplexRef;

    AllAreaLoadListener AllAreaLoadListener1;
    ComplexLoadListener complexLoadListener1;

    @BindView(R.id.spinner)
    MaterialSpinner spinner;
    @BindView(R.id.recycler_complex)
    RecyclerView recycler_complex;

    Unbinder unbinder;

    AlertDialog dialog;

    static Step1B instance;

    public static Step1B getInstance() {
        if(instance == null)
            instance = new Step1B();
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        allAreaRef = FirebaseFirestore.getInstance().collection("AllCourts");
        AllAreaLoadListener1 = this;
        complexLoadListener1 = this;

        dialog = new SpotsDialog.Builder().setContext(getActivity()).setCancelable(false).build();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        super.onCreateView(inflater, container, savedInstanceState);

        View itemView = inflater.inflate(R.layout.fragment_step1,container,false);
        unbinder = ButterKnife.bind(this,itemView);

        initView();
        loadAllArea();
        return itemView;
    }

    private void initView() {
        recycler_complex.setHasFixedSize(true);
        recycler_complex.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recycler_complex.addItemDecoration(new SpacesItemDecoration(4));
    }

    private void loadAllArea() {
        allAreaRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful())
                        {
                            List<String> list = new ArrayList<>();
                            list.add("Please choose area");
                            for(QueryDocumentSnapshot documentSnapshot:task.getResult())
                                list.add(documentSnapshot.getId());
                            AllAreaLoadListener1.onAllAreaLoadSuccess(list);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        AllAreaLoadListener1.onAllAreaLoadFailed(e.getMessage());
                    }
                });
    }

    @Override
    public void onAllAreaLoadSuccess(List<String> areaNameList) {
        spinner.setItems(areaNameList);
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                if(position > 0)
                {
                    loadComplexOfArea(item.toString());
                }
            }
        });
    }

    private void loadComplexOfArea(String areaName) {

        dialog.show();

        Common.area = areaName;

        allComplexRef = FirebaseFirestore.getInstance()
                .collection("AllCourts")
                .document(areaName)
                .collection("Complex");

        allComplexRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<Complex> list = new ArrayList<>();
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot documentSnapshot:task.getResult())
                    {
                        Complex complex = documentSnapshot.toObject(Complex.class);
                        complex.setComplexId(documentSnapshot.getId());
                        list.add(complex);
                    }
                    complexLoadListener1.onComplexLoadSuccess(list);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                complexLoadListener1.onComplexLoadFailed(e.getMessage());
            }
        });
    }

    @Override
    public void onAllAreaLoadFailed(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onComplexLoadSuccess(List<Complex> complexList) {
        ComplexAdapter adapter = new ComplexAdapter(getActivity(), complexList);
        recycler_complex.setAdapter(adapter);

        dialog.dismiss();
    }

    @Override
    public void onComplexLoadFailed(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        dialog.dismiss();
    }
}
