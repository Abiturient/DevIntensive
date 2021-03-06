package com.softdesign.devintensive.Data.network.res;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alex on 13.07.2016.
 */

public class UserModelRes {

    @SerializedName("success")
    @Expose
    private boolean success;

    @SerializedName("data")
    @Expose
    private Data data;

    public Data getData() {
        return data;
    }

    public class Data {
        @SerializedName("_id")
        @Expose
        private String id;
        @SerializedName("first_name")
        @Expose
        private String firstName;
        @SerializedName("second_name")
        @Expose
        private String secondName;
        @SerializedName("__v")
        @Expose
        private int v;
        @SerializedName("repositories")
        @Expose
        private Repositories repositories;
        @SerializedName("contacts")
        @Expose
        private Contacts contacts;
        @SerializedName("profileValues")
        @Expose
        private ProfileValues profileValues;
        @SerializedName("publicInfo")
        @Expose
        private publicInfo publicInfo;
        @SerializedName("specialization")
        @Expose
        private String specialization;

        @SerializedName("role")
        @Expose
        private String role;

        @SerializedName("updated")
        @Expose
        private String updated;

        @SerializedName("token")
        @Expose
        private String token;

        @SerializedName("user")
        @Expose
        private UserModelRes.Data user;

        public Data() {
        }

        public ProfileValues getProfileValues() {
            return profileValues;
        }

        public String getFirstName() {
            return firstName;
        }

        public String getSecondName() {
            return secondName;
        }

        public Repositories getRepositories() {
            return repositories;
        }

        public Contacts getContacts() {
            return contacts;
        }

        public UserModelRes.publicInfo getPublicInfo() {
            return publicInfo;
        }

        public String getId() {
            return id;
        }

        public String getToken() {
            return token;
        }

        public UserModelRes.Data getUser() {
            return user;
        }

    }


    public class Repositories {
        @SerializedName("repo")
        @Expose
        private List<Repo> repo = new ArrayList<Repo>();

        @SerializedName("updated")
        @Expose
        private String updated;

        public List<Repo> getRepo() {
            return repo;
        }

    }

    public class Repo {
        @SerializedName("_id")
        @Expose
        private String id;
        @SerializedName("git")
        @Expose
        private String git;

        @SerializedName("title")
        @Expose
        private String title;

        public String getGit() {
            return git;
        }

        public String getId() {
            return id;
        };

    }

    public class publicInfo {
        @SerializedName("bio")
        @Expose
        private String bio;
        @SerializedName("avatar")
        @Expose
        private String avatar;
        @SerializedName("photo")
        @Expose
        private String photo;

        @SerializedName("updated")
        @Expose
        private String updated;

        public String getAvatar() {
            return avatar;
        }

        public String getBio() {
            return bio;
        }

        public String getPhoto() {
            return photo;
        }

    }

    public class ProfileValues {
        @SerializedName("homeTask")
        @Expose
        private int homeTask;
        @SerializedName("projects")
        @Expose
        private int projects;
        @SerializedName("linesCode")
        @Expose
        private int linesCode;
        @SerializedName("rait")
        @Expose
        private int rait;

        @SerializedName("updated")
        @Expose
        private String updated;

        public int getProjects() {
            return projects;
        }

        public int getLinesCode() {
            return linesCode;
        }

        public int getRaiting() {
            return rait;
        }

    }

    public class Contacts {

        @SerializedName("vk")
        @Expose
        private String vk;
        @SerializedName("phone")
        @Expose
        private String phone;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("updated")
        @Expose
        private String updated;

        public String getVk() {
            return vk;
        }

        public String getPhone() {
            return phone;
        }

        public String getEmail() {
            return email;
        }
    }
}
