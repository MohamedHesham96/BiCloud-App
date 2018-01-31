package com.example.h.cloudcycle;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ListView;

public class HistoryActivity extends AppCompatActivity {

    String[] priceArray = {"2$","4$","3.5$","6$","7.2$","5.7$","6$","7.2$","5.7$" ,"6$","7.2$","5.7$" };
    String[] dateArray = {"14/10/2018","17/11/2018","14/12/2018","28/12/2018","29/12/2018","31/12/2018","28/12/2018","29/12/2018","31/12/2018","28/12/2018","29/12/2018","31/12/2018" };
    String[] bikeIDArray = {"#2012","#2012","#2012","#2012","#2012","#2012" ,"#2012","#2012","#2012" ,"#2012","#2012","#2012" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        CustomListAdapter customListAdapter = new CustomListAdapter(this, priceArray, bikeIDArray, dateArray);
        ListView listView = (ListView) findViewById(R.id.historyList);
        listView.setAdapter(customListAdapter);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if(id == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}

