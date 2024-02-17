package com.keithsmyth.androidshowcase.view.search

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.keithsmyth.androidshowcase.R
import com.keithsmyth.androidshowcase.databinding.FragmentSearchBinding
import com.keithsmyth.androidshowcase.view.BindingAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentSearchBinding.bind(view)
        val viewModel: SearchViewModel by viewModels()

        val resultsAdapter = BindingAdapter()

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    binding.progressBar.isVisible = state.isLoading
                    resultsAdapter.submitList(state.results)
                }
            }
        }

        binding.searchRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = resultsAdapter
        }

        binding.searchEditText.doOnTextChanged { text, _, _, _ ->
            viewModel.updateSearchTerm(text.toString())
        }
    }
}
