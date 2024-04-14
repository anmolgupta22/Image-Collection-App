package com.example.imagecollectionapp.fragment

import android.nfc.tech.MifareUltralight.PAGE_SIZE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.imagecollectionapp.adapter.ImageAdapter
import com.example.imagecollectionapp.databinding.FragmentImageBinding
import com.example.imagecollectionapp.model.Urls
import com.example.imagecollectionapp.util.Constants
import com.example.imagecollectionapp.util.ViewState
import com.example.imagecollectionapp.viewmodel.ImageViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ImageFragment : Fragment() {

    private var _binding: FragmentImageBinding? = null
    private val binding get() = _binding!!
    private lateinit var imageAdapter: ImageAdapter
    private var isLoading = false
    private var currentPage = 1

    private val viewModel: ImageViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentImageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up RecyclerView with GridLayoutManager
        val gridLayoutManager = GridLayoutManager(requireContext(), 2)
        imageAdapter = ImageAdapter()
        binding.rvImage.apply {
            layoutManager = gridLayoutManager
            adapter = imageAdapter
        }

        // Initial API call to fetch data
        viewModel.getAllPhotos(currentPage)

        // Set up RecyclerView scroll listener for pagination
        binding.rvImage.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val visibleItemCount = gridLayoutManager.childCount
                val totalItemCount = gridLayoutManager.itemCount
                val firstVisibleItemPosition = gridLayoutManager.findFirstVisibleItemPosition()

                // Check if not currently loading and reached end of list
                if (!isLoading) {
                    if (currentPage < 10000) { // Arbitrary max page limit
                        if (visibleItemCount + firstVisibleItemPosition >= totalItemCount
                            && firstVisibleItemPosition >= 0
                            && totalItemCount >= PAGE_SIZE // PAGE_SIZE should be defined
                        ) {
                            // Load more data by incrementing the page number
                            currentPage++
                            // Call API to fetch next page
                            viewModel.getAllPhotos(currentPage)
                            isLoading = true // Set loading state to prevent duplicate calls
                        }
                    } else {
                        // Display message when max page limit reached
                        Toast.makeText(
                            requireContext(),
                            "No more images available",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        })

        // Observe changes in view state from ViewModel
        setObservers()
    }

    // Set up observers for LiveData from ViewModel
    private fun setObservers() {
        viewModel.getAllPhotosFlow.asLiveData().observe(viewLifecycleOwner) { viewState ->
            when (viewState) {
                is ViewState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }

                is ViewState.Success -> {
                    binding.progressBar.visibility = View.GONE
                    if (viewState.data.isNotEmpty()) {
                        // Parse JSON data into Urls objects and update adapter
                        val urlsList = viewState.data.map {
                            Gson().fromJson(it.get("urls"), Urls::class.java)
                        }
                        imageAdapter.setItems(urlsList.toMutableList())
                        isLoading = false // Reset loading state
                    }
                }

                is ViewState.NetworkFailed -> {
                    binding.progressBar.visibility = View.GONE
                    // Display network error message
                    Toast.makeText(
                        requireContext(),
                        Constants.NO_INTERNET_MESSAGE,
                        Toast.LENGTH_LONG
                    ).show()
                }

                is ViewState.ExceptionError -> {
                    binding.progressBar.visibility = View.GONE
                    // Display error message from ViewState
                    Toast.makeText(
                        requireContext(),
                        viewState.message,
                        Toast.LENGTH_LONG
                    ).show()
                }

                else -> {
                    // Handle other states if necessary
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Avoid memory leaks
    }
}
