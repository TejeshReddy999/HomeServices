package com.example.homeservices;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class EmployerListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView noEmployer;
    FirebaseDatabase firebaseDatabase;
    private Map<String, Object> pinCodeChilds;
    private EditText searchPinCode;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer_list);
        noEmployer = findViewById(R.id.no_Employers_avliable);
        searchPinCode = findViewById(R.id.search_pincode);
        AppCompatButton searchBtn = findViewById(R.id.search_btn);
        recyclerView = findViewById(R.id.id_employerlistrecycleview);
        progressDialog = new ProgressDialog(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        prepareData();
        searchBtn.setOnClickListener(v -> {
            showProgressDialog();
            prepareData();
        });
    }

    private void prepareData() {
        DatabaseReference pincode = firebaseDatabase.getReference("PinCode")
                .child(getPinCode());
        pincode.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    recyclerView.setVisibility(View.VISIBLE);
                    noEmployer.setVisibility(View.GONE);
                    pinCodeChilds = (HashMap<String, Object>) snapshot.getValue();
                    List<String> phone = new ArrayList<>(Objects.requireNonNull(pinCodeChilds).keySet());
                    setAdapter(phone);
                } else {
                    recyclerView.setVisibility(View.GONE);
                    noEmployer.setVisibility(View.VISIBLE);
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("error", error.getMessage());
            }
        });

    }

    private String getPinCode() {
        if (searchPinCode.getText().toString().equalsIgnoreCase("")) {
            return HomeUtils.getPinCode(this);
        } else {
            return searchPinCode.getText().toString();
        }
    }

    private void setAdapter(List<String> phone) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        EmployerAdapter employerAdapter = new EmployerAdapter(phone,getApplicationContext());
        recyclerView.scheduleLayoutAnimation();
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(employerAdapter);
        progressDialog.dismiss();
    }

    private void showProgressDialog() {
        progressDialog.setMessage("Loading");
        progressDialog.setCancelable(false);
        progressDialog.show();
        new Timer(progressDialog, EmployerListActivity.this).count();
    }
}