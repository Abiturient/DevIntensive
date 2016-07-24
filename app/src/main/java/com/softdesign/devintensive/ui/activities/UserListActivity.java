package com.softdesign.devintensive.ui.activities;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.softdesign.devintensive.Data.storage.models.User;
import com.softdesign.devintensive.R;
import com.softdesign.devintensive.Data.managers.DataManager;
import com.softdesign.devintensive.Data.network.res.UserListRes;
import com.softdesign.devintensive.Data.storage.models.UserDTO;
import com.softdesign.devintensive.ui.adapters.UsersAdapter;
import com.softdesign.devintensive.ui.fragments.RetainedFragment;
import com.softdesign.devintensive.utils.ConstantManager;
import com.softdesign.devintensive.utils.RoundImageTransformation;
import com.squareup.picasso.Picasso;

import com.softdesign.devintensive.utils.ActivityCallback;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserListActivity extends BaseActivity implements SearchView.OnQueryTextListener {

    private static final String TAG = ConstantManager.TAG_PREFIX + "UserListActivity";
    private static final String FRAGMENT_TAG = "UserData";


    /*@BindView(R.id.main_coordinator_list)
    CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.toolbar_list)
    Toolbar mToolbar;
    @BindView(R.id.navigation_drawer_list)
    DrawerLayout mNavigationDrawer;
    @BindView(R.id.user_list_list)
    RecyclerView mRecyclerView;*/

    private CoordinatorLayout mCoordinatorLayout;
    private Toolbar mToolbar;
    private DrawerLayout mNavigationDrawer;
    private RecyclerView mRecyclerView;
    private NavigationView mNavigationView;

    private DataManager mDataManager;
    private UsersAdapter mUserAdapter;
    private List<User> mUsers;

    private RetainedFragment dataFragment;

    private ActivityCallback mCallback;
    private Context mContext;
    private String mQuery;

    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        //ButterKnife.bind(this);
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_coordinator_list);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_list);
        mNavigationDrawer = (DrawerLayout) findViewById(R.id.navigation_drawer_list);
        mRecyclerView = (RecyclerView) findViewById(R.id.user_list_list);

        mContext    = mCoordinatorLayout.getContext();

        mHandler = new Handler();

        setTitle("Команда");

        showProgress();

        mDataManager = DataManager.getInstance();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        //GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        setupToolbar();
        setupDrawer();
        //loadUsers();

        FragmentManager fragmentManager = getFragmentManager();
        dataFragment = (RetainedFragment) fragmentManager.findFragmentByTag(FRAGMENT_TAG);


        if (dataFragment == null) {
            dataFragment = new RetainedFragment();
            fragmentManager.beginTransaction().add(dataFragment, FRAGMENT_TAG).commit();
            //loadUsers();
            loadUsersFromDb();
        } else {
            //mUsers = dataFragment.getData();
            mUsers = dataFragment.getDataFromDb();
            onDataLoaded();
        }
    }

    private void onDataLoaded() {
        mUserAdapter = new UsersAdapter(mUsers,
                new UsersAdapter.UserViewHolder.CustomClickListener() {
                    @Override
                    public void onUserItemClickListener(int position) {
                        UserDTO userDTO = new UserDTO(mUserAdapter.getUser(position));

                        Intent profileIntent = new Intent(UserListActivity.this,
                                ProfileUserActivity.class);
                        profileIntent.putExtra(ConstantManager.PARCELABLE_KEY, userDTO);

                        startActivity(profileIntent);
                    }
                });
        mRecyclerView.setAdapter(mUserAdapter);
        hideProgress();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //dataFragment.setData(mUsers);
        dataFragment.setDataToDb(mUsers);
    }

    @Override
    public void onBackPressed() {
        if (mNavigationView != null && mNavigationDrawer.isDrawerOpen(GravityCompat.START)) {
            mNavigationDrawer.closeDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed();
        }
    }


//    private List<UserListRes.UserData> loadUsers() {
//        Call<UserListRes> call = mDataManager.getUserListFromNetwork();
//
//        call.enqueue(new Callback<UserListRes>() {
//            @Override
//            public void onResponse(Call<UserListRes> call, Response<UserListRes> response) {
//                try {
//                    mUsers = response.body().getData();
//                    dataFragment.setData(mUsers);
//                    onDataLoaded();
//
//                } catch (NullPointerException e) {
//                    Log.e(TAG, e.toString());
//                    showSnackbar("Что-то пошло не так");
//                }
//                /*if (response.code() == 200) {
//                    mUsers = (ArrayList<UserListRes.UserData>) response.body().getData();
//                    mUserAdapter = new UsersAdapter(mUsers,
//                            new UsersAdapter.UserViewHolder.CustomClickListener() {
//                                @Override
//                                public void onUserItemClickListener(int position) {
//                                    UserDTO userDTO=new UserDTO(mUsers.get(position));
//                                    Intent profileIntent=
//                                            new Intent(UserListActivity.this,
//                                                    ProfileUserActivity.class);
//                                    profileIntent.putExtra(ConstantManager.PARCELABLE_KEY,
//                                            userDTO);
//                                    startActivity(profileIntent);
//                                }
//                            });
//                    mRecyclerView.setAdapter(mUserAdapter);
//                } else if (response.code() == 404) {
//                    showSnackbar("Неверный логин или пароль");
//                } else {
//                    showSnackbar("Ошибка..");
//                }*/
//            }
//
//            @Override
//            public void onFailure(Call<UserListRes> call, Throwable t) {
//                //TODO обработать ошибки
//            }
//        });
//        return mUsers;
//    }

    private void loadUsers() {
        /*
        Call<UserListRes> call = mDataManager.getUserListFromNetwork();

        call.enqueue(new Callback<UserListRes>() {
            @Override
            public void onResponse(Call<UserListRes> call, Response<UserListRes> response) {
                try {
                    mUsers = response.body().getData();
                    dataFragment.setData(mUsers);
                    onDataLoaded();

                } catch (NullPointerException e) {
                    Log.e(TAG, e.toString());
                    showSnackbar("Что-то пошло не так");
                }
            }

            @Override
            public void onFailure(Call<UserListRes> call, Throwable t) {
                //TODO обработать ошибки
            }
        });*/
    }

    private void loadUsersFromDb() {
        mUsers = mDataManager.getUserListFromDb();
        if (mUsers.size() == 0) {
            showSnackbar("Список пользователей не может быть загружен");
        } else {
            //dataFragment.setDataToDb(mUsers);
            //onDataLoaded();
            showUsers(mUsers);
        }
    }

    private void setupDrawer() {

        mNavigationView = (NavigationView) findViewById(R.id.navigation_view_list);
        if (mNavigationView != null) {

            TextView textViewEmail = (TextView)
                    mNavigationView.getHeaderView(0).findViewById(R.id.user_email_text);
            TextView textViewName = (TextView)
                    mNavigationView.getHeaderView(0).findViewById(R.id.user_name_text);
            textViewEmail.setText(mDataManager.getPreferencesManager().loadUserProfileData().get(1));
            String name = mDataManager.getPreferencesManager().loadUserName();
            textViewName.setText(name);
            this.setTitle(name);
            setupMenuAvatar(mDataManager.getPreferencesManager().loadUserPhoto());

            mNavigationView.setNavigationItemSelectedListener(
                    new NavigationView.OnNavigationItemSelectedListener() {
                        @Override
                        public boolean onNavigationItemSelected(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.options:
                                    startActivity(new Intent(
                                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                            Uri.parse("package:" + getPackageName())));
                                    break;

                                case R.id.logout:
                                    mDataManager.getPreferencesManager().saveAuthToken("");
                                    startActivity(new Intent(getApplicationContext(), AuthActivity.class));

                                    break;

                                case R.id.user_profile_menu:
                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                    mNavigationDrawer.closeDrawer(GravityCompat.START);
                                    break;

                                case R.id.team_menu:
                                    if (mNavigationDrawer.isDrawerOpen(GravityCompat.START)) {
                                        mNavigationDrawer.closeDrawer(GravityCompat.START);
                                    }
                                    break;

                                default:
                                    break;
                            }
                            return false;
                        }
                    });
        }
    }

    private void setupMenuAvatar(Uri image) {
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        if (navigationView != null) {
            ImageView mRoundedAvatar_img = (ImageView)
                    navigationView.getHeaderView(0).findViewById(R.id.user_avatar);
            Picasso.with(this)
                    .load(image)
                    .resize(getResources().getDimensionPixelSize(R.dimen.profile_image_size_256),
                            getResources().getDimensionPixelSize(R.dimen.profile_image_size_256))
                    .transform(new RoundImageTransformation())
                    .into(mRoundedAvatar_img);
        }
    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu");

        getMenuInflater().inflate(R.menu.toolbar_search, menu);

        MenuItem searchItem = menu.findItem(R.id.toolbar_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);

        return true;
    }*/

    private void showUsers(List<User> users) {
        mUsers = users;
        dataFragment.setDataToDb(mUsers);
        onDataLoaded();
    }

    private void showUserByQuery(String query) {
        mQuery = query;

        Runnable searchUsers = new Runnable() {
            @Override
            public void run() {
                showUsers(mDataManager.getUserListByName(mQuery));
            }
        };

        mHandler.removeCallbacks(searchUsers);
        mHandler.postDelayed(searchUsers, ConstantManager.SEARCH_DELAY);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_search, menu);

        MenuItem searchItem = menu.findItem(R.id.toolbar_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setQueryHint("Введите имя пользователя");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                showUserByQuery(newText);
                return false;
            }
        });

        return super.onPrepareOptionsMenu(menu);
    }

    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void showSnackbar(String message) {
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            mNavigationDrawer.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);

        /*switch (item.getItemId()) {
            case android.R.id.home:
                mCallback.openNavDrawer();
                return true;
        }
        return super.onOptionsItemSelected(item);*/

        /*switch (item.getItemId()) {
            case R.id.user_profile_menu:
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                return true;

            case R.id.options:
                startActivity(new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.parse("package:" + getPackageName())));
                break;

            case R.id.logout:
                logout();
                return true;

            case R.id.team_menu:
                if (mNavigationDrawer.isDrawerOpen(GravityCompat.START)) {
                    mNavigationDrawer.closeDrawer(GravityCompat.START);
                }
                break;

            case android.R.id.home:
                mNavigationDrawer.openDrawer(GravityCompat.START);
                return super.onOptionsItemSelected(item);

            default:
                return super.onOptionsItemSelected(item);
        }*/

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        List<User> queryUsers = new ArrayList<>();
        /*List<UserListRes.UserData> queryUsers = new ArrayList<>();
        for (UserListRes.UserData user : mUsers) {
            if (user.getFullName().toLowerCase().contains(newText.toLowerCase())) {
                queryUsers.add(user);
            }
        }*/
        for (User user : mUsers) {
            if (user.getFullName().toLowerCase().contains(newText.toLowerCase())) {
                queryUsers.add(user);
            }
        }
        mUserAdapter.setUsers(queryUsers);
        mUserAdapter.notifyDataSetChanged();
        return true;
    }

    private void logout() {
        mDataManager.getPreferencesManager().saveAuthToken("");
        startActivity(new Intent(this, AuthActivity.class));
    }


}