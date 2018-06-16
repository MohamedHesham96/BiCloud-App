package com.example.h.cloudcycle;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.example.h.cloudcycle.WebServiceControl.Complain;

import java.util.List;

/**
 * Created by H on 31/01/2018.
 */

public class ComplainsListAdapter extends ArrayAdapter<Complain> {

    private Context context = null;

    private List<Complain> complainList = null;

    public ComplainsListAdapter(Context context, List<Complain> complainList) {
        super(context, R.layout.complain_listview_row, complainList);


        this.context = context;
        this.complainList = complainList;
    }

    public View getView(final int position, View view, final ViewGroup parent) {


        LayoutInflater inflater = LayoutInflater.from(context);

        View rowView = inflater.inflate(R.layout.complain_listview_row, parent, false);

        TextView complainContent = rowView.findViewById(R.id.complain_content_TV);

        TextView complainId = rowView.findViewById(R.id.complain_id_TV);

        complainId.setText(String.valueOf(complainList.get(position).getId()));
        complainContent.setText(complainList.get(position).getContent());

        return rowView;
    }

}
