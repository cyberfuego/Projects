package com.example.githubapi.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.githubapi.R;
import com.example.githubapi.model.GitHubRepo;
import com.example.githubapi.rest.APIClient;
import com.example.githubapi.rest.GithubRepoEndPoint;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RepoFragment extends Fragment {
    public static final String REPOFRAGMENT = "REPO_FRAGMENT";
    RecyclerView list ;
    List<GitHubRepo> repoList ;
    String username;
    RepoAdpater adapter;
    TextView usernametv;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_layout, null, false);
        username = getArguments().getString("username");
        list = view.findViewById(R.id.list);
        repoList = new ArrayList<>();
        adapter = new RepoAdpater(repoList, getActivity());
        usernametv = view.findViewById(R.id.username);
        usernametv.setText(username);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        list.setLayoutManager(llm);
        list.setAdapter(adapter);
        loadRepo();
        return view;
    }

    private void loadRepo() {
        final GithubRepoEndPoint apiService = APIClient.getClient().create(GithubRepoEndPoint.class);
        Call<List<GitHubRepo>> call = apiService.getRepo(username);
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMax(100);
        progressDialog.setMessage("Its loading....");
        progressDialog.setTitle("Fetching Data");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        // show it
        progressDialog.show();
        call.enqueue(new Callback<List<GitHubRepo>>() {
            @Override
            public void onResponse(Call<List<GitHubRepo>> call, Response<List<GitHubRepo>> response) {
                progressDialog.dismiss();
                repoList.clear();
                repoList.addAll(response.body());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<GitHubRepo>> call, Throwable t) {

            }
        });
    }
}
