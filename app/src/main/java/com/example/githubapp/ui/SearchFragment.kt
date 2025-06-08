package com.example.githubapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.githubapp.R
import com.example.githubapp.adapter.UserAdapter
import com.example.githubapp.adapter.UserListItem
import com.example.githubapp.viewmodel.GitHubViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {

    private val viewModel: GitHubViewModel by viewModels()
    private lateinit var userAdapter: UserAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val searchInput = view.findViewById<EditText>(R.id.input_search)
        val searchButton = view.findViewById<Button>(R.id.btn_search)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_users)

        userAdapter = UserAdapter(
            onClick = { user ->
                val fragment = ProfileFragment.newInstance(user.login)
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit()
            },
            onRetry = {
                val query = searchInput.text.toString()
                if (query.isNotBlank()) {
                    viewModel.searchUsers(query)
                }
            }
        )


        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = userAdapter
        }

        viewModel.users.observe(viewLifecycleOwner) { items ->
            if (items.isEmpty()) {
                userAdapter.submitList(listOf(UserListItem.ErrorItem("No users found")))
            } else {
                userAdapter.submitList(items)
            }
        }

        searchButton.setOnClickListener {
            val query = searchInput.text.toString()
            if (query.isNotBlank()) {
                viewModel.searchUsers(query)
            } else {
                Toast.makeText(requireContext(), "Please enter a username", Toast.LENGTH_SHORT).show()
            }
        }

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(rv: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(rv, dx, dy)
                val layoutManager = rv.layoutManager as LinearLayoutManager
                val totalItemCount = layoutManager.itemCount
                val lastVisible = layoutManager.findLastVisibleItemPosition()

                if (lastVisible >= totalItemCount - 3 && viewModel.canLoadMoreUsers()) {
                    viewModel.searchUsers(searchInput.text.toString(), loadMore = true)
                }
            }
        })

    }
}
