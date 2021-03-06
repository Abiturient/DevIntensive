package com.softdesign.devintensive.ui.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.softdesign.devintensive.Data.managers.DataManager;
import com.softdesign.devintensive.Data.network.res.UserListRes;
import com.softdesign.devintensive.Data.network.res.UserModelRes;
import com.softdesign.devintensive.Data.storage.models.User;
import com.softdesign.devintensive.R;
import com.softdesign.devintensive.ui.views.AspectRatioImageView;
import com.softdesign.devintensive.utils.ConstantManager;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
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

    //private List<UserListRes.UserData> mUsers;
    private List<User> mUsers;

    private UserViewHolder.CustomClickListener mCustomClickListener;

    //public UserListRes.UserData getUser(int id){
    public User getUser(int id){

        //return mUsers.get(id);
        return mUsers.get(id);
    }

    //public void setUsers(List<UserListRes.UserData> users) {
    public void setUsers(List<User> users) {
        mUsers = users;
    }

    @Override
    public UsersAdapter.UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.item_user_list,
                parent, false);
        return new UserViewHolder(contentView, mCustomClickListener);
    }


    //public UsersAdapter(List<UserListRes.UserData> users,
    public UsersAdapter(List<User> users,
                        UserViewHolder.CustomClickListener customClickListener) {
        mUsers = users;
        this.mCustomClickListener = customClickListener;
    }

    @Override
    public void onBindViewHolder(final UsersAdapter.UserViewHolder holder, int position) {
        //assert position <= getItemCount();

        //final UserListRes.UserData user = mUsers.get(position);
        final User user = mUsers.get(position);
        final String userPhoto;
        if (user.getPhoto().isEmpty()) {
            userPhoto = "null";
            Log.e(TAG, "onBindViewHolder: user with name " + user.getFullName() + " has not photo");
        } else {
            userPhoto = user.getPhoto();
        }

        DataManager.getInstance().getPicasso()
                .load(userPhoto)
                .fit()
                .centerCrop()
                .networkPolicy(NetworkPolicy.OFFLINE)
                .error(holder.mDummy)
                .placeholder(holder.mDummy)
                .into(holder.mUserPhoto, new Callback() {
                    @Override
                    public void onSuccess() {
                        Log.d(TAG, "load from cache");
                    }

                    @Override
                    public void onError() {
                        DataManager.getInstance().getPicasso()
                                .load(userPhoto)
                                .fit()
                                .centerCrop()
                                .error(holder.mDummy)
                                .placeholder(holder.mDummy)
                                .into(holder.mUserPhoto, new Callback() {
                                    @Override
                                    public void onSuccess() {

                                    }

                                    @Override
                                    public void onError() {
                                        Log.d(TAG, "Could not fetch image");
                                    }
                                });
                    }
                });

        /*try {
            int width = mContext.getResources().getDisplayMetrics().widthPixels;
            int height = (int) (width/1.5f);//картинка приходит размером 768х512, 512*1.5 = 768
            Picasso.with(mContext)
                    .load(user.getPublicInfo().getPhoto())
                    .resize(width,height)
                    .placeholder(mContext.getResources().getDrawable(R.drawable.header_bg))
                    .error(mContext.getResources().getDrawable(R.drawable.header_bg))
                    .into(holder.mUserPhoto);
        } catch (IllegalArgumentException e) {
            Log.e(TAG, "Не загружена картинку !");
            holder.mUserPhoto.setImageResource(R.drawable.userphoto);
        }*/
        holder.mFullName.setText(user.getFullName());
        holder.mRating.setText(String.valueOf(user.getRating()));
        holder.mCodeLines.setText(String.valueOf(user.getCodeLines()));
        holder.mProjects.setText(String.valueOf(user.getProjects()));

        if (user.getBio() == null || user.getBio().isEmpty()) {
            holder.mAbout.setVisibility(View.GONE);
        } else {
            holder.mAbout.setVisibility(View.VISIBLE);
            holder.mAbout.setText(user.getBio());
        }
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        /*@BindView(R.id.user_photo_img_item)
        AspectRatioImageView mUserPhoto;
        @BindView(R.id.user_full_name_txt_item)
        TextView mFullName;
        @BindView(R.id.rating_txt_item)
        TextView mRating;
        @BindView(R.id.code_lines_txt_item)
        TextView mCodeLines;
        @BindView(R.id.project_count_txt_item)
        TextView mProjects;
        @BindView(R.id.about_txt_item)
        TextView mAbout;
        @BindView(R.id.more_info_btn)
        Button mShowMore;*/
        protected TextView mFullName, mRating, mCodeLines, mProjects, mAbout;
        protected Button mShowMore;
        protected AspectRatioImageView mUserPhoto;

        protected Drawable mDummy;

        private CustomClickListener mListener;

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onUserItemClickListener(getAdapterPosition());
            }
        }

        public UserViewHolder(View itemView, CustomClickListener customClickListener) {
            super(itemView);
            //ButterKnife.bind(this, itemView);

            this.mListener = customClickListener;

            mUserPhoto = (AspectRatioImageView) itemView.findViewById(R.id.user_photo_img_item);
            mFullName = (TextView) itemView.findViewById(R.id.user_full_name_txt_item);
            mRating = (TextView) itemView.findViewById(R.id.rating_txt_item);
            mCodeLines = (TextView) itemView.findViewById(R.id.code_lines_txt_item);
            mProjects = (TextView) itemView.findViewById(R.id.project_count_txt_item);
            mAbout = (TextView) itemView.findViewById(R.id.about_txt_item);
            mShowMore =(Button)itemView.findViewById(R.id.more_info_btn);
            mShowMore.setOnClickListener(this);

            mDummy = mUserPhoto.getContext().getResources().getDrawable(R.drawable.header_bg);

            mShowMore.setOnClickListener(this);
        }

        public interface CustomClickListener {
            void onUserItemClickListener(int position);
        }
    }
}
