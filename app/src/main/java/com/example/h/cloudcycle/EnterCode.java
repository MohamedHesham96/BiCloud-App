package com.example.h.cloudcycle;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class EnterCode extends AppCompatActivity {

    EditText codeET;

    String intentCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_code);

        setTitle("Enter Your Code");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        intentCode = getIntent().getStringExtra("code");

        codeET = findViewById(R.id.code_code);

    }

    public void submitCode(View view) {
        String email = getIntent().getStringExtra("email");


        if (intentCode.equals(codeET.getText().toString())) {

            Intent intent = new Intent(getApplicationContext(), EnterNewPassword.class);
            intent.putExtra("email", email);
            startActivity(intent);

        } else {

            codeET.setError("Enter Correct Code");
        }

    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == android.R.id.home) {

            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
