package com.softdesign.devintensive.ui.activities;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.utils.ConstantManager;

/**
 * Created by Alex on 09.07.2016.
 */

public class AuthActivity extends AppCompatActivity implements View.OnClickListener {

    private FrameLayout mFrameLayout;
    private Button mButtonLogin;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.autorization);

        mButtonLogin = (Button) findViewById(R.id.auth_login_button);
        mFrameLayout = (FrameLayout) findViewById(R.id.auth_frame_layout);

        mButtonLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.auth_login_button:
                showSnackBar("Click button");
                intent = new Intent();
                intent.putExtra(ConstantManager.REQUEST_LOGIN_KEY, "return from login activity");
                setResult(RESULT_OK, intent);
                super.onBackPressed();
                break;
        }
    }

    private void showSnackBar (String message) {
        Snackbar.make(mFrameLayout, message, Snackbar.LENGTH_LONG).show();
    }
}
