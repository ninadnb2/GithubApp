package com.example.githubapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubapp.R
import com.example.githubapp.data.model.GitHubUser

sealed class UserListItem {
    data class UserItem(val user: GitHubUser) : UserListItem()
    data class ErrorItem(val message: String) : UserListItem()
}

class UserAdapter(
    private val onClick: (GitHubUser) -> Unit,
    private val onRetry: () -> Unit
) : ListAdapter<UserListItem, RecyclerView.ViewHolder>(DiffCallback()) {

    companion object {
        private const val VIEW_TYPE_USER = 0
        private const val VIEW_TYPE_ERROR = 1
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is UserListItem.UserItem -> VIEW_TYPE_USER
            is UserListItem.ErrorItem -> VIEW_TYPE_ERROR
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_USER -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_user, parent, false)
                UserViewHolder(view)
            }
            VIEW_TYPE_ERROR -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_error, parent, false)
                ErrorViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is UserListItem.UserItem -> (holder as UserViewHolder).bind(item.user)
            is UserListItem.ErrorItem -> (holder as ErrorViewHolder).bind(item.message)
        }
    }

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val avatar = itemView.findViewById<ImageView>(R.id.img_avatar)
        private val username = itemView.findViewById<TextView>(R.id.txt_username)

        fun bind(user: GitHubUser) {
            username.text = user.login
            Glide.with(itemView).load(user.avatar_url).into(avatar)
            itemView.setOnClickListener { onClick(user) }
        }
    }

    inner class ErrorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val errorText = itemView.findViewById<TextView>(R.id.text_error)
        fun bind(message: String) {
            errorText.text = message
            itemView.setOnClickListener {
                onRetry()
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<UserListItem>() {
        override fun areItemsTheSame(oldItem: UserListItem, newItem: UserListItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: UserListItem, newItem: UserListItem): Boolean {
            return oldItem == newItem
        }
    }
}
