package com.example.enuyguncase.ui.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.enuyguncase.databinding.FragmentProductListBinding
import com.example.enuyguncase.ui.adapter.ProductAdapter
import com.example.enuyguncase.ui.viewmodel.ProductViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import androidx.navigation.fragment.findNavController
import com.example.enuyguncase.ui.fragment.FilterBottomSheetFragment
import com.example.enuyguncase.ui.fragment.SortBottomSheetFragment

@AndroidEntryPoint
class ProductListFragment : Fragment() {

    private var _binding: FragmentProductListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProductViewModel by viewModels()
    private lateinit var productAdapter: ProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupObservers()
        setupSearchListener()
        setupFilterAndSortButtons()
    }

    private fun setupRecyclerView() {
        productAdapter = ProductAdapter(
            onProductClick = { product ->
                // Navigate to product detail
                val bundle = Bundle().apply {
                    putString("productId", product.id.toString())
                }
                findNavController().navigate(
                    com.example.enuyguncase.R.id.action_productListFragment_to_productDetailFragment,
                    bundle
                )
            }
        )

        binding.productRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = productAdapter
        }
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.filteredProducts.collectLatest { products ->
                productAdapter.updateProducts(products)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isLoading.collectLatest { isLoading ->
                // Show/hide loading indicator
                binding.root.isEnabled = !isLoading
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.error.collectLatest { error ->
                error?.let {
                    Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                    viewModel.clearError()
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.totalProducts.collectLatest { total ->
                // Update total count in header
                binding.countText.text = "(Toplam $total adet)"
            }
        }
    }

    private fun setupSearchListener() {
        // Add click listener to open keyboard when search field is tapped
        binding.searchEditText.setOnClickListener {
            binding.searchEditText.isFocusable = true
            binding.searchEditText.isFocusableInTouchMode = true
            binding.searchEditText.requestFocus()
        }
        
        // Add real-time search functionality
        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val query = s?.toString() ?: ""
                viewModel.searchProducts(query)
            }
        })
    }

    private fun setupFilterAndSortButtons() {
        // Filter button
        binding.filterButton.setOnClickListener {
            showFilterBottomSheet()
        }

        // Sort button
        binding.sortButton.setOnClickListener {
            showSortBottomSheet()
        }
    }

    private fun showFilterBottomSheet() {
        val filterFragment = FilterBottomSheetFragment().apply {
            setCurrentSelections(
                viewModel.selectedCategory.value,
                viewModel.availableCategories.value
            )
            setOnFilterAppliedListener { filter ->
                viewModel.filterByCategory(filter)
            }
            setOnResetAppliedListener {
                viewModel.clearAllFiltersAndSorts()
            }
        }
        filterFragment.show(childFragmentManager, FilterBottomSheetFragment.TAG)
    }

    private fun showSortBottomSheet() {
        val sortFragment = SortBottomSheetFragment().apply {
            setCurrentSelections(viewModel.sortType.value)
            setOnSortAppliedListener { sortType ->
                viewModel.sortProducts(sortType)
            }
            setOnResetAppliedListener {
                viewModel.clearAllFiltersAndSorts()
            }
        }
        sortFragment.show(childFragmentManager, SortBottomSheetFragment.TAG)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 