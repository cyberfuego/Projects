package com.example.githubapi.rest;

import com.example.githubapi.model.GitHubUser;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GitHubUserEndPoint {
    @GET("/users/{user}")
    Call<GitHubUser> getUser(@Path("user") String user);
}
