package com.example.homeservices;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class HomeAdapter extends BaseExpandableListAdapter {

    private final Activity context;
    private final Map<String, List<String>> ParentListItems;
    private final List<String> Items;

    public HomeAdapter(Activity context, Map<String, List<String>> parentListItems, List<String> items) {
        this.context = context;
        ParentListItems = parentListItems;
        Items = items;
    }

    // Group
    @Override
    public int getGroupCount() {
        return Items.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return Items.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String str = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert layoutInflater != null;
            convertView = layoutInflater.inflate(R.layout.parent_item, null);
        }
        TextView item = convertView.findViewById(R.id.textParent);
        item.setText(str);
        return convertView;
    }


    //Child

    @Override
    public int getChildrenCount(int groupPosition) {
        return Objects.requireNonNull(ParentListItems.get(Items.get(groupPosition))).size();
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return Objects.requireNonNull(ParentListItems.get(Items.get(groupPosition))).get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final String childName = (String) getChild(groupPosition, childPosition);
        LayoutInflater layoutInflater = context.getLayoutInflater();

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.child_item, null);
        }

        TextView item = convertView.findViewById(R.id.textChild);
        item.setText(childName);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
