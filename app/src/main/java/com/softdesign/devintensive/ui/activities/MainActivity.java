package com.softdesign.devintensive.ui.activities;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.softdesign.devintensive.Data.network.ServiceGenerator;
import com.softdesign.devintensive.Data.network.UploadPhoto;

import com.softdesign.devintensive.utils.UserInfoValidator;
import com.squareup.picasso.Picasso;

import com.softdesign.devintensive.Data.managers.DataManager;
import com.softdesign.devintensive.R;
import com.softdesign.devintensive.utils.ConstantManager;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends BaseActivity implements View.OnClickListener {

    public static final String TAG = ConstantManager.TAG_PREFIX + "Main Activity";

    private DataManager mDataManager;
    private int mCurrentEditMode = 0;

    private EditText mUserPhone, mUserMail, mUserVk, mUserGit, mUserBio;
    private TextInputLayout mPhone_til, mEmail_til, mVk_til, mGit_til;

    private TextView mUserValuesRating, mUserValuesCodeLines, mUserValuesProjects;
    private List<TextView> mUserValueViews;

    private ImageView mCalling;
    private ImageView mUserAvatar;
    private NavigationView mNavigationView;
    private ImageView mProfileImage;

    private ImageView mToCall;
    //private ImageView mToSendSms;
    private ImageView mToSendEmail;
    private ImageView mToViewVk;
    private ImageView mToViewGit;

    private CoordinatorLayout mCoordinatorLayout;
    private DrawerLayout mNavigationDrawer;
    private RelativeLayout mProfilePlaceholder;
    private AppBarLayout.LayoutParams mAppBarParams = null;
    private AppBarLayout mAppBarLayout;

    private Toolbar mToolbar;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;

    private FloatingActionButton mFloatingActionButton;

    private List<EditText> mUserInfo;
    private File mPhotoFile = null;
    private Uri mSelectedImage = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate");

        mDataManager = DataManager.getInstance();

        mCalling = (ImageView) findViewById(R.id.call_img);

        mToCall = (ImageView) findViewById(R.id.to_call_img);
        mToSendEmail = (ImageView) findViewById(R.id.to_email_img);
        mToViewVk = (ImageView) findViewById(R.id.to_vk_img);
        mToViewGit = (ImageView) findViewById(R.id.to_git_img);


        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_coordinator_container);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mNavigationDrawer = (DrawerLayout) findViewById(R.id.navigation_drawer);
        mFloatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        mProfilePlaceholder = (RelativeLayout) findViewById(R.id.profile_placeholder);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.appbar_layout);

        mUserPhone = (EditText) findViewById(R.id.phone_et);
        mUserMail = (EditText) findViewById(R.id.email_et);
        mUserVk = (EditText) findViewById(R.id.profile_vk_et);
        mUserGit = (EditText) findViewById(R.id.repository_et);
        mUserBio = (EditText) findViewById(R.id.about_me_et);

        mUserValuesRating = (TextView) findViewById(R.id.rating_tv);
        mUserValuesCodeLines = (TextView) findViewById(R.id.codelines_tv);
        mUserValuesProjects = (TextView) findViewById(R.id.projects_tv);


        mPhone_til  = (TextInputLayout) findViewById(R.id.phone_til);
        UserInfoValidator mUIV1= new UserInfoValidator(mUserPhone, mPhone_til);
        mUserPhone.addTextChangedListener(mUIV1);

        mEmail_til  = (TextInputLayout) findViewById(R.id.email_til);
        UserInfoValidator mUIV2= new UserInfoValidator(mUserMail, mEmail_til);
        mUserMail.addTextChangedListener(mUIV2);

        mVk_til  = (TextInputLayout) findViewById(R.id.vk_til);
        UserInfoValidator mUIV3= new UserInfoValidator(mUserVk, mVk_til);
        mUserVk.addTextChangedListener(mUIV3);

        mGit_til  = (TextInputLayout) findViewById(R.id.git_til);
        UserInfoValidator mUIV4= new UserInfoValidator(mUserGit, mGit_til);
        mUserGit.addTextChangedListener(mUIV4);

        mUserAvatar = (ImageView) findViewById(R.id.user_avatar);
        mProfileImage = (ImageView) findViewById(R.id.user_photo_img);

        mUserInfo = new ArrayList<>();
        mUserInfo.add(mUserPhone);
        mUserInfo.add(mUserMail);
        mUserInfo.add(mUserVk);
        mUserInfo.add(mUserGit);
        mUserInfo.add(mUserBio);

        mUserValueViews = new ArrayList<>();
        mUserValueViews.add(mUserValuesRating);
        mUserValueViews.add(mUserValuesCodeLines);
        mUserValueViews.add(mUserValuesProjects);

        mCalling.setOnClickListener(this);
        mFloatingActionButton.setOnClickListener(this);
        mProfilePlaceholder.setOnClickListener(this);

        mToCall.setOnClickListener(this);
        mToSendEmail.setOnClickListener(this);
        mToViewVk.setOnClickListener(this);
        mToViewGit.setOnClickListener(this);

        setupToolbar();
        setupDrawer();
        loadUserInfoValue();
        loadUserInfoValues();
        Picasso.with(this)
                .load(mDataManager.getPreferencesManager().loadUserPhoto())
                .placeholder(R.drawable.userphoto)  ////todo placeholder+transform+crop
                .into(mProfileImage);

        if (savedInstanceState == null) {
//            showSnackbar("активити запускается впервые");
//            showToast("активити запускается впервые");

        } else {
//            showSnackbar("активити уже создавалось");
//            showToast("активити уже создавалось");
            mCurrentEditMode = savedInstanceState.getInt(ConstantManager.EDIT_MODE_KEY, 0);
            changeEditMode(mCurrentEditMode);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*if (item.getItemId() == android.R.id.home) {
            mNavigationDrawer.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
        */
        switch (item.getItemId()) {
            case android.R.id.home:
                mNavigationDrawer.openDrawer(GravityCompat.START);
                return true;

            case R.id.logout:
                logout();
                return true;

            case R.id.team_menu:
                startActivity(new Intent(getApplicationContext(), UserListActivity.class));
                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return false;

    }

    @Override
    protected void onStart() {

        super.onStart();
        Log.d(TAG, "onStart");
    }

    @Override
    protected void onResume() {

        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    protected void onPause() {

        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart");
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.call_img:
                /*showProgress();
                runWithDelay();*/
                break;

            case R.id.fab:
                if (mCurrentEditMode == 0) {
                    changeEditMode(1);
                    mCurrentEditMode = 1;
                } else {
                    changeEditMode(0);
                    mCurrentEditMode = 0;
                }
                //showSnackbar("click");
                break;

            case R.id.profile_placeholder:
                showDialog(ConstantManager.LOAD_PROFILE_PHOTO);
                break;

            case R.id.to_call_img:
                toCall(mUserPhone.getText().toString());
                break;

            case R.id.to_email_img:
                toSendEmail(mUserMail.getText().toString());
                break;

            case R.id.to_vk_img:
                toOpenWebPage(mUserVk.getText().toString());
                break;
            case R.id.to_git_img:
                toOpenWebPage(mUserGit.getText().toString());
                break;

        }
    }



    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(ConstantManager.EDIT_MODE_KEY, mCurrentEditMode);
    }

    @Override
    public void onBackPressed() {
        if (mNavigationView != null && mNavigationDrawer.isDrawerOpen(GravityCompat.START)) {
            mNavigationDrawer.closeDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed();
        }
    }

    /*public void runWithDelay() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                hideProgress();
            }
        }, 5000);
    }*/

    private void showSnackbar(String message) {
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }

    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();

        mAppBarParams = (AppBarLayout.LayoutParams) mCollapsingToolbarLayout.getLayoutParams();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setupDrawer() {

        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
        if (mNavigationDrawer != null) {
            mNavigationView.setNavigationItemSelectedListener(
                    new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(MenuItem item) {
                    /*showSnackbar(item.getTitle().toString());
                    item.setChecked(true);
                    mNavigationDrawer.closeDrawer(GravityCompat.START);*/
                    switch (item.getItemId()) {
                        case R.id.options:
                            startActivity(new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName())));
                            break;

                        case R.id.logout:
                            logout();
                            break;

                        default:
                            showSnackbar(item.getTitle().toString());
                            item.setChecked(true);
                            break;

                        case R.id.team_menu:
                            startActivity(new Intent(getApplicationContext(), UserListActivity.class));
                            break;
                    }
                    mNavigationDrawer.closeDrawer(GravityCompat.START);
                    return false;
                }
            });

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ConstantManager.REQUEST_GALLERY_PICTURE:
                if (resultCode == RESULT_OK && data != null) {
                    mSelectedImage = data.getData();

                    insertProfileImage(mSelectedImage);
                }
                break;
            case ConstantManager.REQUEST_CAMERA_PICTURE:
                if (resultCode == RESULT_OK && mPhotoFile != null) {
                    mSelectedImage = Uri.fromFile(mPhotoFile);

                    insertProfileImage(mSelectedImage);
                }
                break;

            case R.id.logout:
                logout();
                break;

            case R.id.team_menu:
                startActivity(new Intent(getApplicationContext(), UserListActivity.class));
                break;
        }
    }


    private void changeEditMode(int mode) {
        if (mode == 1) {
            mFloatingActionButton.setImageResource(R.drawable.ic_check_black_24dp);
            for (EditText userValue : mUserInfo) {
                userValue.setEnabled(true);
                userValue.setFocusable(true);
                userValue.setFocusableInTouchMode(true);

                showProfilePlaceholder();
                lockTolbar();
                mCollapsingToolbarLayout.setExpandedTitleColor(Color.TRANSPARENT);
            }
        } else {
            mFloatingActionButton.setImageResource(R.drawable.ic_edit_black_24dp);
            for (EditText userValue : mUserInfo) {
                userValue.setEnabled(false);
                userValue.setFocusable(false);
                userValue.setFocusableInTouchMode(false);

                hideProfilePlaceholder();
                unlockToolbar();
                mCollapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.white));

                saveUserInfoValue();
            }
        }


    }

    private void loadUserInfoValue() {
        List<String> userData = mDataManager.getPreferencesManager().loadUserProfileData();
        for (int i = 0; i < userData.size(); i++) {
            mUserInfo.get(i).setText(userData.get(i));
        }

        setupRoundedAvatar();
    }

    private void saveUserInfoValue() {
        List<String> userData = new ArrayList<>();
        for (EditText field : mUserInfo) {
            userData.add(field.getText().toString());
        }
        mDataManager.getPreferencesManager().saveUserProfileData(userData);
    }

    private void loadUserInfoValues() {
        List<String> userData = mDataManager.getPreferencesManager().loadUserProfileValues();
        for (int i = 0; i < userData.size(); i++) {
            mUserValueViews.get(i).setText(userData.get(i));
        }

    }

    public void setupRoundedAvatar() {
        //mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
        if (mNavigationDrawer != null) {
            ImageView mRoundedAvatar_img = (ImageView) mNavigationView.getHeaderView(0).findViewById(R.id.user_avatar);
            Bitmap src = BitmapFactory.decodeResource(getResources(), R.drawable.ava);
            RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), src);
            roundedBitmapDrawable.setCornerRadius(Math.max(src.getWidth(), src.getHeight()) / 2.0f);
            mRoundedAvatar_img.setImageDrawable(roundedBitmapDrawable);
        }
    }

    private void loadPhotoGallery() {

        Intent takeGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        takeGalleryIntent.setType("image/*");
        /*не рабочий вариант третий параметр не int, а IntentSender
        startActivityForResult(Intent.createChooser(takeGalleryIntent,

                getString(R.string.user_profile_choose_message),
                ConstantManager.REQUEST_GALLERY_PICTURE));
        */
        takeGalleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(takeGalleryIntent, ConstantManager.REQUEST_GALLERY_PICTURE);

    }

    private void loadPhotoFromCamera() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Intent takeCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            try {
                mPhotoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (mPhotoFile != null) {
                //send file to intent
                takeCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mPhotoFile));
                startActivityForResult(takeCaptureIntent, ConstantManager.REQUEST_CAMERA_PICTURE);

                uploadPhoto(mPhotoFile.toURI());
            }
        } else {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, ConstantManager.CAMERA_REQUEST_PERMISSION_CODE);

            Snackbar.make(mCoordinatorLayout,
                    "Необходимо дать требуемые разрешения для корректной работы приложения",
                    Snackbar.LENGTH_LONG).setAction("Разрешить", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openApplicationSettings();
                }
            }).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == ConstantManager.CAMERA_REQUEST_PERMISSION_CODE
                && grantResults.length == 2) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //all is ok
            }

            if (grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                //all is ok
            }
        }
    }

    private void hideProfilePlaceholder() {
        mProfilePlaceholder.setVisibility(View.GONE);
    }

    private void showProfilePlaceholder() {
        mProfilePlaceholder.setVisibility(View.VISIBLE);
    }

    private void lockTolbar() {
        mAppBarLayout.setExpanded(true, true);
        mAppBarParams.setScrollFlags(0);
        mCollapsingToolbarLayout.setLayoutParams(mAppBarParams);
    }

    private void unlockToolbar() {
        mAppBarParams.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED);
        mCollapsingToolbarLayout.setLayoutParams(mAppBarParams);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case ConstantManager.LOAD_PROFILE_PHOTO:
                String[] selectItems = {getString(R.string.user_profile_dialog_gallery),
                        getString(R.string.user_profile_dialog_camera),
                        getString(R.string.user_profile_dialog_cancel)};
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(getString(R.string.user_profile_dialog_title));
                builder.setItems(selectItems, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int choiceItem) {
                        switch (choiceItem) {
                            case 0:
                                //from gallery
                                //showSnackbar("из галереи");
                                loadPhotoGallery();
                                break;
                            case 1:
                                //from camera
                                //showSnackbar("с камеры");
                                loadPhotoFromCamera();
                                break;
                            case 2:
                                //cancel
                                //showSnackbar("отмена");
                                dialog.cancel();
                                break;

                        }
                    }
                });
                return builder.create();

            default:
                return null;

        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.MIME_TYPE, "images/jpeg");
        values.put(MediaStore.MediaColumns.DATA, image.getAbsolutePath());

        this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        return image;
    }

    private void insertProfileImage(Uri selectedImage) {
        Picasso.with(this)
                .load(selectedImage)
                .into(mProfileImage);
        ////todo placeholder+transform+crop
        mDataManager.getPreferencesManager().saveUserPhoto(selectedImage);
    }

    public void openApplicationSettings() {
        Intent appSettingsIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.parse("package:" + getPackageName()));
        startActivityForResult(appSettingsIntent, ConstantManager.PERMISSION_REQUEST_SETTINGS_CODE);
    }

    private void toOpenWebPage(String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(url.contains("http://") ? url : ("http://" + url)));
        startActivity(browserIntent);
    }

    private void toSendEmail(String email) {
        Intent sendEmailIntent = new Intent(Intent.ACTION_SEND);
        sendEmailIntent.setType("text/plain");
        sendEmailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
        sendEmailIntent.putExtra(Intent.EXTRA_SUBJECT, "Тема");
        sendEmailIntent.putExtra(Intent.EXTRA_TEXT, "Тело");
        startActivity(Intent.createChooser(sendEmailIntent, "Отправка"));
    }

    private void toCall(String phoneNumber) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            Intent dialIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" +
                    additionalValidatingOfPhoneNumber(phoneNumber)));
            startActivity(dialIntent);
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    ConstantManager.CALL_PHONE_REQUEST_PERMISSIION_CODE);
        }
    }

    private String additionalValidatingOfPhoneNumber(String phoneNum) {
        return phoneNum.replace(" ", "")
                .replaceAll("\\(", "")
                .replaceAll("\\)", "");
    }

    private void uploadPhoto(URI uri) {
        /*
        // create upload service client
        UploadPhoto service = ServiceGenerator.createService(UploadPhoto.class);
        // https://github.com/iPaulPro/aFileChooser/blob/master/aFileChooser/src/com/ipaulpro/afilechooser/utils/FileUtils.java
        // use the FileUtils to get the actual file by uri
        File file = new File(uri);
        // create RequestBody instance from file
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body = MultipartBody.Part.createFormData("picture", file.getName(), requestFile);
        // add another part within the multipart request
        String descriptionString = "hello, this is description speaking";
        RequestBody description = RequestBody.create(MediaType.parse("multipart/form-data"), descriptionString);
        // finally, execute the request
        Call<ResponseBody> call = service.upload(description, body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.v("Upload", "success");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Upload error:", t.getMessage());
            }
        });*/
    }

    private void logout() {
        mDataManager.getPreferencesManager().saveAuthToken("");
        startActivity(new Intent(this, AuthActivity.class));
    }

}
