package com.example.homeservices;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EmployerActivity extends AppCompatActivity {

    private TextView customer_PhoneNumber,noRequestMessg;
    private FirebaseDatabase firebaseDatabase;
    private LinearLayout customerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer);
        customer_PhoneNumber = findViewById(R.id.customer_number);
        customerLayout = findViewById(R.id.id_layoutpendingdata);
        noRequestMessg = findViewById(R.id.noRequemsg);
        firebaseDatabase = FirebaseDatabase.getInstance();
        getCustomerData();
    }

    private void getCustomerData() {
        if(new CheckNetwork(this).isNetworkAvailable()){
            DatabaseReference pinCodeDataBaseRef = firebaseDatabase.getReference("PinCode").child(HomeUtils.getPinCode(this));
            DatabaseReference phoneNumberDataBaseRef = pinCodeDataBaseRef.child(HomeUtils.getCurentUserPhoneNumber(this));
            phoneNumberDataBaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Address address = snapshot.getValue(Address.class);
                    if (address != null && address.getRequestService().equalsIgnoreCase("True") && !(address.getCustomerPhoneNumber().equalsIgnoreCase("Default"))) {
                        customerLayout.setVisibility(View.VISIBLE);
                        noRequestMessg.setVisibility(View.GONE);
                        customer_PhoneNumber.setText(address.getCustomerPhoneNumber());
                    }else {
                        customerLayout.setVisibility(View.GONE);
                        noRequestMessg.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else {
            Toast.makeText(this, "check connection", Toast.LENGTH_LONG).show();
        }
    }
}