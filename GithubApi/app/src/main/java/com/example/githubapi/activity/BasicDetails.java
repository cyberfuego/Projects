package com.example.githubapi.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTabHost;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.githubapi.R;
import com.example.githubapi.model.GitHubUser;
import com.example.githubapi.rest.APIClient;
import com.example.githubapi.rest.GitHubUserEndPoint;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BasicDetails extends AppCompatActivity implements View.OnClickListener {
    String username;
    TextView usernameTv, followersTv, followingTv, nameTv;
    ImageView avatarIv;
    Button repoBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.basic_detial);
        username = getIntent().getStringExtra("Username");
        Log.d("connection user", "Username " + username);
        usernameTv = findViewById(R.id.login);
        followersTv = findViewById(R.id.followers);
        followingTv = findViewById(R.id.following);
        nameTv = findViewById(R.id.name);
        avatarIv = findViewById(R.id.avatar);
        repoBtn = findViewById(R.id.repos);
        repoBtn.setOnClickListener(this);
        loadData();
    }

    private void loadData() {
        final GitHubUserEndPoint apiService = APIClient.getClient().create(GitHubUserEndPoint.class);
        Call<GitHubUser> call = apiService.getUser(username);
        call.enqueue(new Callback<GitHubUser>() {
            @Override
            public void onResponse(Call<GitHubUser> call, Response<GitHubUser> response) {
                usernameTv.setText("Username: " + response.body().getLogin());
                followersTv.setText("Followers: " + response.body().getFollowers());
                followingTv.setText("Following: " + response.body().getFollowing());
                if (!response.body().getName().isEmpty()) {
                    nameTv.setText("Name: " + response.body().getName());
                } else {
                    nameTv.setText("No name provided");
                }
                if (!response.body().getAvatar_url().isEmpty()) {
                    Glide.with(BasicDetails.this).load(response.body().getAvatar_url()).into(avatarIv);
                }
            }

            @Override
            public void onFailure(Call<GitHubUser> call, Throwable t) {
            }
        });
    }

    @Override
    public void onClick(View view) {
        Bundle bundle = new Bundle();
        bundle.putString("username", username);
        RepoFragment repoFragment = new RepoFragment();
        repoFragment.setArguments(bundle);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.fragment_container, repoFragment, repoFragment.REPOFRAGMENT).addToBackStack(null).commit();
    }
}
