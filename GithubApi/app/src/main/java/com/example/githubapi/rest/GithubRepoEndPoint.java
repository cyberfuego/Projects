package com.example.githubapi.rest;

import com.example.githubapi.model.GitHubRepo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GithubRepoEndPoint {
    @GET("/users/{user}/repos")
    Call<List<GitHubRepo>>getRepo(@Path("user") String user);
}
