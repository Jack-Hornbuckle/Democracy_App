package com.example.project;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class Adapter extends ArrayAdapter<String> {
    Context context;
    ArrayList<String> qList;

    public Adapter(Context context, ArrayList<String> questions) {
        super(context, R.layout.list_element, questions);
        this.context = context;
        this.qList = questions;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final String question = getItem(position);
        TextView viewText;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_element, parent, false);
            viewText = convertView.findViewById(R.id.textViewer);
            convertView.setTag(viewText);
        } else {
            viewText = (TextView)convertView.getTag();
        }
        viewText.setText(question);
        return convertView;
    }

}
