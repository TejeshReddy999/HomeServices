package com.example.homeservices;

import static com.example.homeservices.R.color.errorColor;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

public class RegisterActivity extends AppCompatActivity {

    EditText name;
    EditText surName;
    EditText email;
    EditText countryCode;
    EditText phoneNumber;
    EditText town;
    EditText district;
    EditText state;
    EditText pinCode;
    EditText password;
    EditText conformPassword;

    RadioGroup usertypeRadGrp;
    RadioButton customerRadBtn;
    RadioButton employerRadBtn;

    Button register;
    ProgressDialog progressDialog;

    FirebaseAuth mAuth;
    DatabaseReference pinCodeDataRef;
    DatabaseReference phoneDataRef;
    SharedPreferences.Editor editor;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    SharedPreferences sharedPreferences;
    PhoneAuthProvider.ForceResendingToken force;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    boolean b = false;
    private String otp;
    private String sent;
    private String phone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        registerViews();
        progressDialog = new ProgressDialog(this);
        initiateOtpProcedure();
        sharedPreferences = getSharedPreferences("Home_Services", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        register.setOnClickListener(this::userRegistration);
    }

    private void registerViews() {

        register = findViewById(R.id.register);
        name = findViewById(R.id.name);
        surName = findViewById(R.id.surname);
        email = findViewById(R.id.email);
        countryCode = findViewById(R.id.country_code);
        phoneNumber = findViewById(R.id.phonenumber);
        password = findViewById(R.id.password);
        conformPassword = findViewById(R.id.confirmpassword);
        town = findViewById(R.id.town_village);
        district = findViewById(R.id.district);
        state = findViewById(R.id.state);
        pinCode = findViewById(R.id.pincode);
        customerRadBtn = findViewById(R.id.id_customerUser);
        employerRadBtn = findViewById(R.id.id_employerUser);
        usertypeRadGrp = findViewById(R.id.usertype);
    }

    private void initiateOtpProcedure() {

        firebaseDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                Toast.makeText(RegisterActivity.this, "OTP SENT", Toast.LENGTH_LONG).show();
                b = true;
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(RegisterActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                Log.e("OTP Error ", e.getLocalizedMessage());
            }

            @Override
            public void onCodeSent(@NonNull final String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                sent = s;
                force = forceResendingToken;
            }
        };
    }

    private void userRegistration(View view) {

        EditText[] customerDetails = {name, surName, email, countryCode, phoneNumber, password, conformPassword, town, district, state, pinCode};

        if (validate(customerDetails)) {

            phone = "+" + countryCode.getText().toString() + phoneNumber.getText().toString();
            if (new CheckNetwork(RegisterActivity.this).isNetworkAvailable()) {
                showProgressDialog();

                databaseReference = firebaseDatabase.getReference(HomeUtils.getUserType(customerRadBtn, employerRadBtn))
                                                    .child(phoneNumber.getText().toString().replace("/", "AaAa"));
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            progressDialog.dismiss();
                            Toast.makeText(RegisterActivity.this, "User Already Taken", Toast.LENGTH_LONG).show();
                        } else {
                            PhoneAuthProvider.getInstance().verifyPhoneNumber(phone, 60,
                                    TimeUnit.SECONDS, RegisterActivity.this, mCallbacks);
                            LayoutInflater li = LayoutInflater.from(RegisterActivity.this);
                            View promptsView = li.inflate(R.layout.otp, null);
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                    RegisterActivity.this);
                            alertDialogBuilder.setView(promptsView);
                            alertDialogBuilder.setMessage("OTP sent to the registered number");
                            final EditText userInput = promptsView
                                    .findViewById(R.id.editTextDialogUserInput);
                            userInput.setInputType(InputType.TYPE_CLASS_NUMBER);
                            progressDialog.dismiss();
                            alertDialogBuilder.setCancelable(false).setNeutralButton("Resend OTP", (dialog, which) -> {
                                        PhoneAuthProvider.getInstance().verifyPhoneNumber(phone, 60, TimeUnit.SECONDS,
                                                RegisterActivity.this, mCallbacks, force);
                                        userRegistration(view);
                                    })
                                    .setPositiveButton("OK",
                                            (dialog, id) -> {
                                                if (!TextUtils.isEmpty(userInput.getText().toString())) {
                                                    progressDialog.show();
                                                    otp = userInput.getText().toString();
                                                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(sent, otp);
                                                    mAuth.signInWithCredential(credential)
                                                            .addOnCompleteListener(task -> {
                                                                if (task.isSuccessful()) {
                                                                    Log.i("success", "success");
                                                                    Customer customer = new Customer(
                                                                            name.getText().toString(),
                                                                            surName.getText().toString(),
                                                                            email.getText().toString(),
                                                                            phone,
                                                                            password.getText().toString(),
                                                                            town.getText().toString(),
                                                                            district.getText().toString(),
                                                                            state.getText().toString(),
                                                                            pinCode.getText().toString(),
                                                                            HomeUtils.getUserType(customerRadBtn, employerRadBtn));
                                                                    databaseReference.setValue(customer);
                                                                    Gson gson = new Gson();
                                                                    String json = gson.toJson(customer);
                                                                    editor.putString("data", json);
                                                                    editor.putString("contactNumber", phoneNumber.getText().toString());
                                                                    editor.putString("userType",HomeUtils.getUserType(customerRadBtn,employerRadBtn));
                                                                    editor.apply();
                                                                    storePinCodeDatabase();
                                                                    progressDialog.dismiss();
                                                                    Toast.makeText(RegisterActivity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                                                                    proceedToMainActivity();
                                                                } else {
                                                                    progressDialog.dismiss();
                                                                    Toast.makeText(RegisterActivity.this, "Incorrect otp", Toast.LENGTH_SHORT).show();
                                                                }
                                                            });
                                                }
                                            });
                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            } else
                Toast.makeText(RegisterActivity.this, "check connection", Toast.LENGTH_LONG).show();
        } else
            Toast.makeText(RegisterActivity.this, "Enter all fields correctly", Toast.LENGTH_LONG).show();
    }

    private void storePinCodeDatabase() {
        pinCodeDataRef = firebaseDatabase.getReference("PinCode")
                .child(pinCode.getText().toString().replace("/", "AaAa"));
        pinCodeDataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(HomeUtils.getUserType(customerRadBtn,employerRadBtn).equalsIgnoreCase("Employer")) {
                    phoneDataRef = pinCodeDataRef.child(phoneNumber.getText().toString());
                    Address address = new Address("Default",phoneNumber.getText().toString(),"false","false");
                    phoneDataRef.setValue(address);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void showProgressDialog() {
        progressDialog.setMessage("Loading");
        progressDialog.setCancelable(false);
        progressDialog.show();
        new Timer(progressDialog, RegisterActivity.this).count();
    }

    private boolean validate(EditText[] fields) {

        for (EditText currentField : fields) {
            if (currentField.getText().toString().length() <= 0) {
                return false;
            }
        }
        if (!password.getText().toString().equals(conformPassword.getText().toString())) {
            conformPassword.setText("");
            conformPassword.setHint("Renter confirm password");
            conformPassword.setHintTextColor(getResources().getColor(errorColor));
            return false;
        } else return customerRadBtn.isChecked() || employerRadBtn.isChecked();
    }

    private void proceedToMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }
}