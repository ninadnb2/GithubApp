package com.example.githubapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.githubapp.R
import com.example.githubapp.data.model.GitHubRepo

class RepoAdapter : ListAdapter<GitHubRepo, RepoAdapter.RepoViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        RepoViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_repo, parent, false))

    override fun onBindViewHolder(holder: RepoViewHolder, position: Int) {
        val repo = getItem(position)
        holder.bind(repo)
    }

    inner class RepoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val name = itemView.findViewById<TextView>(R.id.txt_repo_name)
        private val desc = itemView.findViewById<TextView>(R.id.txt_repo_desc)
        private val stars = itemView.findViewById<TextView>(R.id.txt_stars)
        private val forks = itemView.findViewById<TextView>(R.id.txt_forks)

        fun bind(repo: GitHubRepo) {
            name.text = repo.name
            desc.text = repo.description ?: "No description"
            stars.text = "⭐ ${repo.stargazers_count}"
            forks.text = "🍴 ${repo.forks_count}"
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<GitHubRepo>() {
        override fun areItemsTheSame(oldItem: GitHubRepo, newItem: GitHubRepo) = oldItem.name == newItem.name
        override fun areContentsTheSame(oldItem: GitHubRepo, newItem: GitHubRepo) = oldItem == newItem
    }
}
