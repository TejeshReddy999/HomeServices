package com.example.homeservices;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class EmployerAdapter extends RecyclerView.Adapter<EmployerAdapter.MyViewHolder> {

    private final List<String> pinCodeChilds;
    private final Context context;

    public EmployerAdapter(List<String> pinCodeChilds, Context context) {
        this.pinCodeChilds = pinCodeChilds;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.employer_list_frame, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.employerName.setText(pinCodeChilds.get(position));
        holder.callNow.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + holder.employerName.getText()));
            v.getContext().startActivity(intent);
        });

        holder.pinCodeDatarefer = holder.firebaseDatabase.getReference("PinCode")
                .child(context.getSharedPreferences("Home_Services", MODE_PRIVATE)
                        .getString("pinCode", ""));
        holder.phoneNumberDatarefer = holder.pinCodeDatarefer.child(holder.employerName.getText().toString());
        holder.sendRequest.setOnClickListener(v ->
                holder.phoneNumberDatarefer.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            holder.address = snapshot.getValue(Address.class);
                            if (holder.address != null) {
                                holder.address.setCustomerPhoneNumber(v.getContext().getSharedPreferences("Home_Services", MODE_PRIVATE)
                                        .getString("contactNumber", "Default"));
                                holder.address.setRequestService("True");
                                holder.phoneNumberDatarefer.setValue(holder.address);
                                holder.sendRequest.setText(R.string.text_Request_pending);
                                holder.sendRequest.setBackgroundColor(context.getResources().getColor(R.color.redColor));
                                holder.phoneNumberDatarefer.setValue(holder.address);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }));
    }

    @Override
    public int getItemCount() {
        return pinCodeChilds.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView employerName;
        public AppCompatButton callNow, sendRequest;
        private final FirebaseDatabase firebaseDatabase;
        private DatabaseReference pinCodeDatarefer;
        private DatabaseReference phoneNumberDatarefer;
        private Address address;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            employerName = itemView.findViewById(R.id.employer_name);
            callNow = itemView.findViewById(R.id.call_now);
            sendRequest = itemView.findViewById(R.id.request_btn);
            firebaseDatabase = FirebaseDatabase.getInstance();
        }
    }
}

