package com.example.homeservices;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.widget.RadioButton;

public class HomeUtils {

    public static boolean isCustomer = true;

    public static void setIsCustomer(boolean isCustomer) {
        HomeUtils.isCustomer = isCustomer;
    }

    public static boolean getIsCustomer() {
        return isCustomer;
    }

    public static String getUserType(RadioButton button, RadioButton button2) {
        if (button.isChecked()) {
            return button.getText().toString();
        } else {
            return button2.getText().toString();
        }
    }

    public static String getPinCode(Context c) {
        return c.getSharedPreferences("Home_Services", MODE_PRIVATE)
                .getString("pinCode", "");
    }

    public static String getCurentUserPhoneNumber(Context c) {
        return c.getSharedPreferences("Home_Services", MODE_PRIVATE)
                .getString("contactNumber", "");
    }
}
