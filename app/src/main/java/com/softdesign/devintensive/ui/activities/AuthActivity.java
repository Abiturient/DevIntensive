package com.softdesign.devintensive.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.softdesign.devintensive.Data.managers.DataManager;
import com.softdesign.devintensive.Data.network.req.UserLoginReq;
import com.softdesign.devintensive.Data.network.res.UserListRes;
import com.softdesign.devintensive.Data.network.res.UserModelRes;
import com.softdesign.devintensive.Data.storage.models.Repository;
import com.softdesign.devintensive.Data.storage.models.RepositoryDao;
import com.softdesign.devintensive.Data.storage.models.User;
import com.softdesign.devintensive.Data.storage.models.UserDao;
import com.softdesign.devintensive.R;
import com.softdesign.devintensive.ui.fragments.RetainedFragment;
import com.softdesign.devintensive.utils.AppConfig;
import com.softdesign.devintensive.utils.ConstantManager;
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

    private static final String TAG = ConstantManager.TAG_PREFIX + "AuthActivity";

    private Button mButtonAuth;
    private TextView mRememberPassword;
    private EditText mLogin, mPassword;

    private DataManager mDataManager;

    private CoordinatorLayout mCoordinatorLayout;

    private RetainedFragment dataFragment;

    private RepositoryDao mRepositoryDao;
    private UserDao mUserDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.autorization);

        mDataManager = DataManager.getInstance();

        mUserDao = mDataManager.getDaoSession().getUserDao();
        mRepositoryDao = mDataManager.getDaoSession().getRepositoryDao();


        mButtonAuth = (Button) findViewById(R.id.auth_button);

        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_coordinator_container);

        mRememberPassword = (TextView) findViewById(R.id.forgot_pass_button);
        mLogin = (EditText) findViewById(R.id.auth_login_et);
        mPassword = (EditText) findViewById(R.id.auth_password_et);

        mRememberPassword.setOnClickListener(this);
        mButtonAuth.setOnClickListener(this);

        if (NetworkStatusChecker.isNetworkAvailable(this)) {
            Call<UserModelRes> call = mDataManager.authByToken();
            call.enqueue(new Callback<UserModelRes>() {
                @Override
                public void onResponse(Call<UserModelRes> call, Response<UserModelRes> response) {
                    switch (response.code()){
                        case 200:
                            authUserByToken(response.body());
                            break;
                        case 401:
                            showSnackBar("Время истекло: токен");
                            break;
                    }
                }

                @Override
                public void onFailure(Call<UserModelRes> call, Throwable t) {

                }
            });
        } else {
            showSnackBar("Сеть на данный момент не дотсупна, попробуйте позже");
        }

    }

    public void onBackPressed() {
        //super.onBackPressed();
        //if (mDataManager.getPreferencesManager().getAuthToken().equals("")) {
        //    this.finish();
        //}
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
        //showSnackBar(userModel.getData().getToken());
        mDataManager.getPreferencesManager().saveAuthToken(userModel.getData().getToken());
        mDataManager.getPreferencesManager().saveUserId(userModel.getData().getUser().getId());

        saveUserValues(userModel.getData());
        saveUserInDb();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent loginIntent = new Intent(AuthActivity.this, MainActivity.class);
                startActivity(loginIntent);
            }
        }, AppConfig.START_DELAY);

        /*Intent loginIntent = new Intent(this, MainActivity.class);
        //Intent loginIntent = new Intent(this, UserListActivity.class);
        startActivity(loginIntent);*/

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

    private void saveUserValues(UserModelRes.Data userModel) {

        boolean test = false;
        int a = 0, b = 0, c = 0;

        try {
            a = userModel.getUser().getProfileValues().getRaiting();
            b = userModel.getUser().getProfileValues().getLinesCode();
            c = userModel.getUser().getProfileValues().getProjects();

        } catch (Exception e) {
            test = true;
            a = userModel.getProfileValues().getRaiting();
            b = userModel.getProfileValues().getLinesCode();
            c = userModel.getProfileValues().getProjects();
        };

        int[] userValues = { a, b, c };

        mDataManager.getPreferencesManager().saveUserProfileValues(userValues);
    }

    private void authUserByToken(UserModelRes userModel) {
        showSnackBar("Валидация по токену");

        saveUserValues(userModel.getData());
        saveUserData(userModel.getData());
        saveUserPhotos(userModel.getData());

        Intent loginIntent = new Intent(this, MainActivity.class);
        startActivity(loginIntent);
    }

    private void saveUserData(UserModelRes.Data userModel) {
        List<String> userData = new ArrayList<>();

        userData.add(userModel.getContacts().getPhone());
        userData.add(userModel.getContacts().getEmail());
        userData.add(userModel.getContacts().getVk());
        userData.add(userModel.getRepositories().getRepo().get(0).getGit());
        userData.add(userModel.getPublicInfo().getBio());

        mDataManager.getPreferencesManager().saveUserProfileData(userData);
        saveUserName(userModel);
    }

    private void saveUserPhotos(UserModelRes.Data userModel){
        mDataManager.getPreferencesManager().saveUserPhoto(Uri.parse(userModel.getPublicInfo().getPhoto()));
        mDataManager.getPreferencesManager().saveUserAvatar(Uri.parse(userModel.getPublicInfo().getAvatar()));
    }

    private void saveUserName(UserModelRes.Data userModel){
        List<String> userName = new ArrayList<>();
        userName.add(userModel.getFirstName());
        userName.add(userModel.getSecondName());

        mDataManager.getPreferencesManager().saveUserName(userName);
    }

    private void saveUserInDb() {
        Call<UserListRes> call = mDataManager.getUserListFromNetwork();

        call.enqueue(new Callback<UserListRes>() {
            @Override
            public void onResponse(Call<UserListRes> call, Response<UserListRes> response) {
                try {
                    if (response.code() == 200) {
                        List<Repository> allRepositories = new ArrayList<Repository>();
                        List<User> allUsers = new ArrayList<User>();

                        for (UserListRes.UserData userRes : response.body().getData()) {
                            allRepositories.addAll(getRepolistFromUserRes(userRes));
                            allUsers.add(new User(userRes));
                        }

                        mRepositoryDao.insertOrReplaceInTx(allRepositories);
                        mUserDao.insertOrReplaceInTx(allUsers);

                    } else {
                        showSnackBar("Список пользователей не может быть получен");
                        Log.e(TAG, "onResponse: " + String.valueOf(response.errorBody().source()));
                    }

                } catch (NullPointerException e) {
                    e.getStackTrace();
                    Log.e(TAG, e.toString());
                    showSnackBar("Что-то пошло не так");
                }
            }

            @Override
            public void onFailure(Call<UserListRes> call, Throwable t) {
                //TODO обработать ошибки
            }
        });
    }

    private List<Repository> getRepolistFromUserRes(UserListRes.UserData userData) {
        final String userID = userData.getId();

        List<Repository> repositories = new ArrayList<>();
        for (UserModelRes.Repo repositoryRes : userData.getRepositories().getRepo()) {
            repositories.add(new Repository(repositoryRes, userID));
        }

        return repositories;
    }

}
