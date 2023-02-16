package com.franklinharper.demo.giphy.feature.main

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.franklinharper.demo.giphy.R
import com.franklinharper.demo.giphy.databinding.FragmentMainBinding
import com.laimiux.lce.fold
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainFragment : Fragment(R.layout.fragment_main) {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.loadData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentMainBinding.bind(view)
        val mainAdapter = MainAdapter()
        binding.list.apply {
            layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.VERTICAL,
                /* reverseLayout = */ false
            )
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            adapter = mainAdapter
        }
        viewModel.trendingResults.observe(viewLifecycleOwner) { event ->
            event.fold(
                onLoading = {
                    binding.spinner.isVisible = true
                    binding.errorMessage.isVisible = false
                    binding.list.isVisible = false
                },
                onError = {
                    binding.spinner.isVisible = false
                    binding.errorMessage.isVisible = true
                    binding.list.isVisible = false
                },
                onContent = { result ->
                    binding.spinner.isVisible = false
                    binding.errorMessage.isVisible = false
                    binding.list.isVisible = true
                    mainAdapter.submitList(result)
                }
            )
        }
    }
}