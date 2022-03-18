package com.example.homeservices;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.Objects;


public class MainActivity extends AppCompatActivity {

    SharedPreferences.Editor editor;
    EditText userContactNumber, userPassword;
    Button loginBtn, newUserbtn;
    FirebaseDatabase firebaseDatabase;
    ProgressDialog progressDialog;
    private String path = "Customer";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getSharedPreferences("Home_Services", MODE_PRIVATE).getInt("registered", 0) == 1) {
            if(getSharedPreferences("Home_Services",MODE_PRIVATE).getString("userType","Customer").equalsIgnoreCase("Customer")) {
                startActivity(new Intent(MainActivity.this, HomeActivity.class));
            }
            else {
                startActivity(new Intent(MainActivity.this, EmployerActivity.class));
            }
            finish();
        }
        editor = getSharedPreferences("Home_Services", MODE_PRIVATE).edit();
        userContactNumber = findViewById(R.id.userPhoneNumber);
        userPassword = findViewById(R.id.userpassword);
        loginBtn = findViewById(R.id.id_login);
        newUserbtn = findViewById(R.id.newUser);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading");
        firebaseDatabase = FirebaseDatabase.getInstance();
        newUserbtn.setOnClickListener(v -> navigateToRegisterActivity());
        loginBtn.setOnClickListener(v -> navigateToHomeActivity(path));
    }

    private void navigateToRegisterActivity() {
        Intent in = new Intent(this, RegisterActivity.class);
        startActivity(in);
    }

    private void navigateToHomeActivity(String path) {
        if (!TextUtils.isEmpty(userContactNumber.getText().toString()) && !TextUtils.isEmpty(userPassword.getText().toString())) {
            if (new CheckNetwork(MainActivity.this).isNetworkAvailable()) {
                progressDialog.show();
                new Timer(progressDialog, MainActivity.this).count();
                DatabaseReference customerDataBaseReference = firebaseDatabase.getReference(path).child(userContactNumber.getText().toString());
                customerDataBaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        verifyDataAuthentication(dataSnapshot, path);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.i("Firebase Database", databaseError.toString());
                    }
                });
            } else {
                Toast.makeText(MainActivity.this, "check connection", Toast.LENGTH_LONG).show();
            }
        } else {
            if (TextUtils.isEmpty(userContactNumber.getText()) && TextUtils.isEmpty(userPassword.getText())) {
                Toast.makeText(MainActivity.this, "Enter all fields", Toast.LENGTH_LONG).show();

            } else if (TextUtils.isEmpty(userPassword.getText())) {
                Toast.makeText(MainActivity.this, "Enter Password", Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(MainActivity.this, "Enter phone number", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void verifyDataAuthentication(DataSnapshot dataSnapshot, String path2) {
        if (dataSnapshot.exists()) {
            Customer customer = dataSnapshot.getValue(Customer.class);
            String password = Objects.requireNonNull(customer).getNewPassword();
            if (password.equals(userPassword.getText().toString())) {
                editor.putInt("registered", 1);
                Gson gson = new Gson();
                String json = gson.toJson(customer);
                editor.putString("data", json);
                editor.putString("contactNumber", userContactNumber.getText().toString());
                editor.putString("userType",customer.getUserType());
                editor.putString("pinCode",customer.getPinCode());
                editor.putString("Address",customer.getState() +","+customer.getDistrict() + ","+customer.getTown()
                        +","+ customer.getDistrict() +"-"+customer.getPinCode());
                editor.apply();
                progressDialog.dismiss();
                Intent in;
                in = HomeUtils.getIsCustomer()
                        ? new Intent(MainActivity.this, HomeActivity.class)
                        : new Intent(MainActivity.this, EmployerActivity.class) ;
                startActivity(in);
                finish();
            } else if(Objects.equals(path2, "Customer")) {
                navigateToHomeActivity("Employer");
                HomeUtils.setIsCustomer(false);
            }else {
                progressDialog.dismiss();
                Toast.makeText(MainActivity.this, "wrong password", Toast.LENGTH_LONG).show();
                HomeUtils.setIsCustomer(true);
                path = "Customer";
            }
        } else if (Objects.equals(path2, "Customer")) {
            navigateToHomeActivity("Employer");
            HomeUtils.setIsCustomer(false);
        } else {
            progressDialog.dismiss();
            Toast.makeText(MainActivity.this, "Invalid Contact Number", Toast.LENGTH_LONG).show();
            HomeUtils.setIsCustomer(true );
            path = "Customer";
        }
    }
}