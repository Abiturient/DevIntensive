package com.softdesign.devintensive.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.softdesign.devintensive.Data.network.res.UserListRes;
import com.softdesign.devintensive.R;
import com.softdesign.devintensive.ui.views.AspectRatioImageView;
import com.softdesign.devintensive.utils.ConstantManager;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Alex on 16.07.2016.
 */
public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder> {

    private static final String TAG = ConstantManager.TAG_PREFIX + "UserAdapter";

    private Context mContext;
    private List<UserListRes.UserData> mUsers;
    private UserViewHolder.CustomClickListener mCustomClickListener;

    public UserListRes.UserData getUser(int id){
        return mUsers.get(id);
    }

    public void setUsers(List<UserListRes.UserData> users) {
        mUsers = users;
    }

    @Override
    public UsersAdapter.UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.item_user_list, parent, false);
        return new UserViewHolder(contentView, mCustomClickListener);
    }


    public UsersAdapter(List<UserListRes.UserData> users, UserViewHolder.CustomClickListener customClickListener) {
        mUsers = users;
        this.mCustomClickListener = customClickListener;
    }

    @Override
    public void onBindViewHolder(UsersAdapter.UserViewHolder holder, int position) {
        //assert position <= getItemCount();
        UserListRes.UserData user = mUsers.get(position);
        try {
            int width = mContext.getResources().getDisplayMetrics().widthPixels;
            int height = (int) (width/1.5f);//картинка приходит размером 768х512, 512*1.5 = 768
            Picasso.with(mContext)
                    .load(user.getPublicInfo().getPhoto())
                    .resize(width,height)
                    .placeholder(mContext.getResources().getDrawable(R.drawable.userphoto))
                    .error(mContext.getResources().getDrawable(R.drawable.userphoto))
                    .into(holder.mUserPhoto);
        } catch (IllegalArgumentException e) {
            Log.e(TAG, "Не загружена картинку !");
            holder.mUserPhoto.setImageResource(R.drawable.userphoto);
        }
        holder.mFullName.setText(user.getFullName());
        holder.mRating.setText(String.valueOf(user.getProfileValues().getRating()));
        holder.mCodeLines.setText(String.valueOf(user.getProfileValues().getLinesCode()));
        holder.mProjects.setText(String.valueOf(user.getProfileValues().getProjects()));

        if (user.getPublicInfo().getBio() == null || user.getPublicInfo().getBio().isEmpty()) {
            holder.mAbout.setVisibility(View.GONE);
        } else {
            holder.mAbout.setVisibility(View.VISIBLE);
            holder.mAbout.setText(user.getPublicInfo().getBio());
        }
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.user_photo_img)
        AspectRatioImageView mUserPhoto;
        @BindView(R.id.user_full_name_txt)
        TextView mFullName;
        @BindView(R.id.rating_txt)
        TextView mRating;
        @BindView(R.id.code_lines_txt)
        TextView mCodeLines;
        @BindView(R.id.project_count_txt)
        TextView mProjects;
        @BindView(R.id.about_txt)
        TextView mAbout;
        @BindView(R.id.more_info_btn)
        Button mShowMore;

        private CustomClickListener mListener;

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onUserItemClickListener(getAdapterPosition());
            }
        }

        public UserViewHolder(View itemView, CustomClickListener customClickListener) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            this.mListener = customClickListener;

            mShowMore.setOnClickListener(this);
        }

        public interface CustomClickListener {
            void onUserItemClickListener(int position);
        }
    }
}
