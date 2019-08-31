package com.example.githubapi.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.githubapi.R;
import com.example.githubapi.model.GitHubRepo;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RepoAdpater extends RecyclerView.Adapter<RepoAdpater.ViewHolder> {

    List<GitHubRepo> repoList;
    static Context mContext;

    public RepoAdpater(List<GitHubRepo> repoList, Context context) {
        this.repoList = repoList;
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.repos_adapter_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (repoList.get(position).getLanguage()!=null)
            holder.repolanguage.setText("Language: "+repoList.get(position).getLanguage());
        else
            holder.repolanguage.setText("No language available");
        holder.reponametv.setText(repoList.get(position).getName());
        holder.repolinktv.setText(repoList.get(position).getHtml_url());
    }

    @Override
    public int getItemCount() {
        Log.d("Size", " " + repoList.size());
        return repoList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView reponametv, repolinktv, repolanguage;
        ImageView share ;

        public ViewHolder(@NonNull View view) {
            super(view);
            reponametv = view.findViewById(R.id.repo_name);
            repolinktv = view.findViewById(R.id.repo_url);
            repolanguage = view.findViewById(R.id.language);
            share = view.findViewById(R.id.share);
            share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, repolinktv.getText().toString());
                    sendIntent.setType("text/plain");
                    Intent chooser =  Intent.createChooser(sendIntent, "Share Link");
                    mContext.startActivity(chooser);
                }
            });
        }
    }

}
