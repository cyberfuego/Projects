package com.example.githubapi.model;

import com.google.gson.annotations.SerializedName;

public class GitHubUser {
    @SerializedName("name")
    private String name;
    @SerializedName("followers")
    private String followers;
    @SerializedName("following")
    private String following;
    @SerializedName("login")
    private String login;
    @SerializedName("avatar_url")
    private String avatar_url;

    public GitHubUser(String name, String followers, String following, String username, String avatar_url) {
        this.setName(name);
        this.setFollowers(followers);
        this.setFollowing(following);
        this.setLogin(username);
        this.setAvatar_url(avatar_url);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFollowers() {
        return followers;
    }

    public void setFollowers(String followers) {
        this.followers = followers;
    }

    public String getFollowing() {
        return following;
    }

    public void setFollowing(String following) {
        this.following = following;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }
}
