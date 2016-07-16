package com.softdesign.devintensive.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.softdesign.devintensive.Data.managers.DataManager;
import com.softdesign.devintensive.Data.network.req.UserLoginReq;
import com.softdesign.devintensive.Data.network.res.UserModelRes;
import com.softdesign.devintensive.R;
import com.softdesign.devintensive.utils.NetworkStatusChecker;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Alex on 09.07.2016.
 */

public class AuthActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mButtonAuth;
    private TextView mRememberPassword;
    private EditText mLogin, mPassword;

    private DataManager mDataManager;

    private CoordinatorLayout mCoordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.autorization);

        mDataManager = DataManager.getInstance();

        mButtonAuth = (Button) findViewById(R.id.auth_button);

        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_coordinator_container);

        mRememberPassword = (TextView) findViewById(R.id.forgot_pass_button);
        mLogin = (EditText) findViewById(R.id.auth_login_et);
        mPassword = (EditText) findViewById(R.id.auth_password_et);

        mRememberPassword.setOnClickListener(this);
        mButtonAuth.setOnClickListener(this);

        if (NetworkStatusChecker.isNetworkAvailable(this)) {
            Call<UserModelRes.User> call = mDataManager.authByToken();
            call.enqueue(new Callback<UserModelRes.User>() {
                @Override
                public void onResponse(Call<UserModelRes.User> call, Response<UserModelRes.User> response) {
                    switch (response.code()){
                        case 200:
                            authUserByToken(response.body());
                            break;
                        case 401:
                            showSnackBar("Токен скончался");
                            break;
                    }
                }

                @Override
                public void onFailure(Call<UserModelRes.User> call, Throwable t) {

                }
            });
        } else {
            showSnackBar("Сеть на данный момент не дотсупна, попробуйте позже");
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.auth_button:
                signIn();
                //authSuccess();
                /*intent = new Intent();
                intent.putExtra(ConstantManager.REQUEST_LOGIN_KEY, "return from login activity");
                setResult(RESULT_OK, intent);
                super.onBackPressed();*/
                break;
            case R.id.forgot_pass_button:
                rememberPassword();
                break;
        }
    }

    private void showSnackBar (String message) {
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }

    private void rememberPassword() {
        Intent rememberIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://devintensive.softdesign-apps.ru/forgotpass"));
        startActivity(rememberIntent);
    }

    private void authSuccess(UserModelRes userModel) {
        showSnackBar(userModel.getData().getToken());
        mDataManager.getPreferencesManager().saveAuthToken(userModel.getData().getToken());
        mDataManager.getPreferencesManager().saveUserId(userModel.getData().getUser().getId());

        saveUserValues(userModel.getData().getUser());

        Intent loginIntent = new Intent(this, MainActivity.class);
        startActivity(loginIntent);
    }

    private void signIn() {
        if (NetworkStatusChecker.isNetworkAvailable(this)) {
            Call<UserModelRes> call = mDataManager.loginUser(new UserLoginReq(
                    mLogin.getText().toString(),
                    mPassword.getText().toString()));
            call.enqueue(new Callback<UserModelRes>() {
                @Override
                public void onResponse(Call<UserModelRes> call, Response<UserModelRes> response) {
                    if (response.code() == 200) {
                        authSuccess(response.body());
                    } else if (response.code() == 404) {
                        showSnackBar("Неверный логин или пароль !");
                    } else {
                        showSnackBar("Все пропало !");
                    }
                }

                @Override
                public void onFailure(Call<UserModelRes> call, Throwable t) {
                    // TODO: обработать ошибки
                }
            });
        } else {
            showSnackBar("Сеть недоступна на данный момент, попробуйте позже !");
        }
    }

    private void saveUserValues(UserModelRes.User userModel) {
        int[] userValues = {
                userModel.getProfileValues().getRating(),
                userModel.getProfileValues().getLinesCode(),
                userModel.getProfileValues().getProjects()
        };
        mDataManager.getPreferencesManager().saveUserProfileValues(userValues);
    }

    private void authUserByToken(UserModelRes.User userModel) {
        showSnackBar("Вход по токену");

        saveUserValues(userModel);
        saveUserData(userModel);
        saveUserPhotos(userModel);

        Intent loginIntent = new Intent(this, MainActivity.class);
        startActivity(loginIntent);
    }

    private void saveUserData(UserModelRes.User userModel) {
        List<String> userData = new ArrayList<>();

        userData.add(userModel.getContacts().getPhone());
        userData.add(userModel.getContacts().getEmail());
        userData.add(userModel.getContacts().getVk());
        userData.add(userModel.getRepositories().getRepo().get(0).getGit());
        userData.add(userModel.getPublicInfo().getBio());

        mDataManager.getPreferencesManager().saveUserProfileData(userData);
        saveUserName(userModel);
    }

    private void saveUserPhotos(UserModelRes.User userModel){
        mDataManager.getPreferencesManager().saveUserPhoto(Uri.parse(userModel.getPublicInfo().getPhoto()));
        mDataManager.getPreferencesManager().saveUserAvatar(Uri.parse(userModel.getPublicInfo().getAvatar()));
    }

    private void saveUserName(UserModelRes.User userModel){
        List<String> userName = new ArrayList<>();
        userName.add(userModel.getFirstName());
        userName.add(userModel.getSecondName());

        mDataManager.getPreferencesManager().saveUserName(userName);
    }


}
