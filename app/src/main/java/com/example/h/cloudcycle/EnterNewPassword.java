package com.example.h.cloudcycle;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;

public class EnterNewPassword extends AppCompatActivity {

    Button submitNewPassword;
    EditText passwordText;
    EditText repeatedPasswordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_new_password);

        submitNewPassword = findViewById(R.id.submit_new_password);
        passwordText = findViewById(R.id.password);
        repeatedPasswordText = findViewById(R.id.repeat_password);


    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void signup(View view) {

        if (!validate()) {

            onSignupFailed();
            return;
        }

        onSignupSuccess();
    }

    public void onSignupSuccess() {
        submitNewPassword.setEnabled(true);
        setResult(RESULT_OK, null);
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        finish();
        startActivity(intent);
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Sign-Up Failed", Toast.LENGTH_LONG).show();

        submitNewPassword.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String password = passwordText.getText().toString();
        String repeatdPassword = repeatedPasswordText.getText().toString();


        if (password.isEmpty() || passwordText.length() < 6 || passwordText.length() > 32) {

            passwordText.setError("between 6 and 32 alphanumeric characters");
            valid = false;

        } else if (!password.equals(repeatdPassword)) {

            passwordText.setError("Password Dose Not Match");
            repeatedPasswordText.setError("Password Dose Not Match");
            valid = false;

        } else {

            passwordText.setError(null);

        }

        return valid;
    }

}
