package com.example.homeservices;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ExpandableListView;


import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    List<String> childList;
    Map<String, List<String>> parentListItems;
    ExpandableListView expandableListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        parentListItems = new LinkedHashMap<>();
        for (String HoldItem : ParentList) {
            switch (HoldItem) {
                case "Salon For Women":
                    loadChild(subWomenSalon);
                    break;
                case "Salon For men":
                    loadChild(subMenSalon);
                    break;
                case "Cleaning":
                    loadChild(subCleaning);
                    break;
                case "Plumbing":
                    loadChild(subPlumbing);
                    break;
                case "Wood Work":
                    loadChild(subWoodWork);
                    break;
                case "Appliance Repair":
                    loadChild(subApplianceRepair);
                    break;
                default:
                    loadChild(defaultList);
            }
            parentListItems.put(HoldItem, childList);
        }
        expandableListView = findViewById(R.id.expandedListView);
        final HomeAdapter expandableListAdapter = new HomeAdapter(this, parentListItems, ParentList);
        expandableListView.setAdapter(expandableListAdapter);
        expandableListView.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {
            Intent i = new Intent(this, EmployerListActivity.class);
            startActivity(i);
            return true;

        });
    }

    private void loadChild(String[] parentElementName) {
        childList = new ArrayList<>();
        Collections.addAll(childList, parentElementName);
    }

    // Static Data: :

    List<String> ParentList = new ArrayList<>();

    {
        ParentList.add("Salon For Women");
        ParentList.add("Salon For men");
        ParentList.add("Cleaning");
        ParentList.add("Plumbing");
        ParentList.add("Wood Work");
        ParentList.add("Appliance Repair");
    }

    String[] subWomenSalon = {"Hair Care", "skin Care", "waxing"};
    String[] subMenSalon = {"Hair styling", "skin Care", "Massage therapy"};
    String[] subCleaning = {"Disinfection Service", "Pest Control", "Full Home Cleaning", "Bathroom and Kitchen Cleaning"};
    String[] subPlumbing = {"Water-pipe Connection", "Bath Fitting", "Drainage pipes"};
    String[] subWoodWork = {"Cupboard & Drawer", "Drill & Hang", "Furniture Repair",};
    String[] subApplianceRepair = {"Air-Conditioner(AC)", "Micro Wave", "Refrigerator", "Washing Machine"};
    String[] defaultList = {"No Sub Services Found"};

}