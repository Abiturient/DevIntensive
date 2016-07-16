package com.softdesign.devintensive.ui.activities;


import android.support.design.widget.CoordinatorLayout;

import android.app.FragmentManager;
import android.content.Intent;
import android.net.Uri;
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

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.Data.managers.DataManager;
import com.softdesign.devintensive.Data.network.res.UserListRes;
import com.softdesign.devintensive.Data.storage.models.UserDTO;
import com.softdesign.devintensive.ui.adapters.UsersAdapter;
import com.softdesign.devintensive.ui.fragments.RetainedFragment;
import com.softdesign.devintensive.utils.ConstantManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserListActivity extends BaseActivity implements SearchView.OnQueryTextListener {

    private static final String TAG = ConstantManager.TAG_PREFIX + "UserListActivity";

    @BindView(R.id.main_coordinator)
    CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.navigation_drawer)
    DrawerLayout mNavigationDrawer;
    @BindView(R.id.user_list)
    RecyclerView mRecyclerView;

    private DataManager mDataManager;
    private UsersAdapter mUserAdapter;
    private List<UserListRes.UserData> mUsers;

    private RetainedFragment dataFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        ButterKnife.bind(this);
        setTitle("Команда");

        showProgress();

        mDataManager = DataManager.getInstance();
        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(gridLayoutManager);

        setupToolbar();
        setupDrawer();

        FragmentManager fragmentManager = getFragmentManager();
        dataFragment = (RetainedFragment) fragmentManager.findFragmentByTag("mData");


        if (dataFragment == null) {
            dataFragment = new RetainedFragment();
            fragmentManager.beginTransaction().add(dataFragment, "mData").commit();
            loadUsers();
        } else {
            mUsers = dataFragment.getData();
            onDataLoaded();
        }
    }

    private void onDataLoaded() {
        mUserAdapter = new UsersAdapter(mUsers, new UsersAdapter.UserViewHolder.CustomClickListener() {
            @Override
            public void onUserItemClickListener(int position) {
                UserDTO userDTO = new UserDTO(mUserAdapter.getUser(position));

                Intent profileIntent = new Intent(UserListActivity.this, ProfileUserActivity.class);
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
        dataFragment.setData(mUsers);
    }

    private List<UserListRes.UserData> loadUsers() {
        Call<UserListRes> call = mDataManager.getUserList();

        call.enqueue(new Callback<UserListRes>() {
            @Override
            public void onResponse(Call<UserListRes> call, Response<UserListRes> response) {
                try {
                    mUsers = response.body().getData();
                    dataFragment.setData(mUsers);
                    onDataLoaded();
                } catch (NullPointerException e) {
                    Log.e(TAG, e.toString());
                    showSnackbar("Что то пошло не так");
                }
            }

            @Override
            public void onFailure(Call<UserListRes> call, Throwable t) {
                //TODO обработать ошибки
            }
        });
        return mUsers;
    }

    private void setupDrawer() {
        final NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.options:
                            startActivity(new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                    Uri.parse("package:" + getPackageName())));
                            break;
                        case R.id.logout:
                            mDataManager.getPreferencesManager().saveAuthToken("");
                            startActivity(new Intent(getApplicationContext(),
                                    AuthActivity.class));
                            break;
                        case R.id.user_profile_menu:
                            startActivity(new Intent(getApplicationContext(),
                                    AuthActivity.class));
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu");

        getMenuInflater().inflate(R.menu.toolbar_search, menu);

        MenuItem searchItem = menu.findItem(R.id.toolbar_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);

        return true;
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
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        List<UserListRes.UserData> queryUsers = new ArrayList<>();
        for (UserListRes.UserData user : mUsers) {
            if (user.getFullName().toLowerCase().contains(newText.toLowerCase())) {
                queryUsers.add(user);
            }
        }
        mUserAdapter.setUsers(queryUsers);
        mUserAdapter.notifyDataSetChanged();
        return true;
    }
}
