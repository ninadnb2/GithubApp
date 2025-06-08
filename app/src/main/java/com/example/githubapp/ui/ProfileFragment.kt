package com.example.githubapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubapp.R
import com.example.githubapp.adapter.RepoAdapter
import com.example.githubapp.viewmodel.GitHubViewModel

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private lateinit var viewModel: GitHubViewModel
    private lateinit var repoAdapter: RepoAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[GitHubViewModel::class.java]

        val username = requireArguments().getString(ARG_USERNAME) ?: return

        val avatar = view.findViewById<ImageView>(R.id.img_avatar)
        val name = view.findViewById<TextView>(R.id.txt_username)
        val bio = view.findViewById<TextView>(R.id.txt_bio)
        val stats = view.findViewById<TextView>(R.id.txt_stats)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_repos)

        repoAdapter = RepoAdapter()

        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = repoAdapter
        }

        viewModel.profile.observe(viewLifecycleOwner) { user ->
            if (user != null) {
                Glide.with(this).load(user.avatar_url).into(avatar)
                name.text = user.login
                bio.text = user.bio ?: "No bio available"
                stats.text = "Followers: ${user.followers} | Repos: ${user.public_repos}"
            }
        }


        viewModel.repos.observe(viewLifecycleOwner) {
            repoAdapter.submitList(it)
        }

        viewModel.loading.observe(viewLifecycleOwner) { loading ->
            view.findViewById<ProgressBar>(R.id.progress_profile).visibility =
                if (loading) View.VISIBLE else View.GONE
        }

        viewModel.fetchProfile(username)
        viewModel.fetchRepos(username)

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(rv: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(rv, dx, dy)
                val layoutManager = rv.layoutManager as LinearLayoutManager
                val totalItemCount = layoutManager.itemCount
                val lastVisible = layoutManager.findLastVisibleItemPosition()

                if (lastVisible >= totalItemCount - 3 && viewModel.canLoadMoreRepos()) {
                    val username1 = requireArguments().getString("username") ?: return
                    viewModel.fetchRepos(username1, loadMore = true)
                }
            }
        })

    }

    companion object {
        private const val ARG_USERNAME = "username"

        fun newInstance(username: String): ProfileFragment {
            return ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_USERNAME, username)
                }
            }
        }
    }
}
