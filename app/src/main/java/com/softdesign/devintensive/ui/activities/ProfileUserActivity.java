package com.softdesign.devintensive.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.Data.storage.models.UserDTO;
import com.softdesign.devintensive.ui.adapters.RepositoriesAdapter;
import com.softdesign.devintensive.utils.ConstantManager;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Alex on 16.07.2016.
 */
public class ProfileUserActivity extends AppCompatActivity {

    /*@BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.user_photo_img)
    ImageView mProfileImage;
    @BindView(R.id.about_et)
    EditText mUserAbout;
    @BindView(R.id.rating_txt_item)
    TextView mUserRating;
    @BindView(R.id.code_lines_txt_item)
    TextView mUserCodeLines;
    @BindView(R.id.project_count_txt_item)
    TextView mUserProjects;
    @BindView(R.id.repositories_list)
    ListView mRepoListView;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    @BindView(R.id.main_coordinator)
    CoordinatorLayout mCoordinatorLayout;*/

    private Toolbar mToolbar;
    private TextView mUserRating, mUserCodeLines, mUserProjects, mUserAbout;
    private ImageView mProfileImage;
    private ListView mRepoListView;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private CoordinatorLayout mCoordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_user);
        //ButterKnife.bind(this);

        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_coordinator_container);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mProfileImage = (ImageView) findViewById(R.id.user_photo_img);
        mUserRating = (TextView) findViewById(R.id.rating_tv);
        mUserCodeLines = (TextView) findViewById(R.id.codelines_tv);
        mUserProjects = (TextView) findViewById(R.id.projects_tv);
        mUserAbout = (TextView) findViewById(R.id.bio_tv);
        mRepoListView = (ListView) findViewById(R.id.repositories_list);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);

        setupToolbar();
        initProfileData();
    }

    private void setupToolbar() {
        setSupportActionBar(mToolbar);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initProfileData() {
        UserDTO userDTO = getIntent().getParcelableExtra(ConstantManager.PARCELABLE_KEY);

        final List<String> repositories = userDTO.getRepositories();
        final RepositoriesAdapter repositoriesAdapter = new RepositoriesAdapter(this, repositories);
        mRepoListView.setAdapter(repositoriesAdapter);

        mRepoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent openGitHub = new Intent(Intent.ACTION_VIEW, Uri.parse("https://"
                        + repositories.get(position)));
                startActivity(openGitHub);
            }
        });

        mUserAbout.setText(userDTO.getAbout());
        mUserRating.setText(userDTO.getRating());
        mUserCodeLines.setText(userDTO.getCodeLines());
        mUserProjects.setText(userDTO.getProjects());

        mCollapsingToolbarLayout.setTitle(userDTO.getFullName());

        Picasso.with(this)
                .load(userDTO.getPhoto())
                .placeholder(R.drawable.userphoto)
                .error(R.drawable.userphoto)
                .into(mProfileImage);

    }


}


