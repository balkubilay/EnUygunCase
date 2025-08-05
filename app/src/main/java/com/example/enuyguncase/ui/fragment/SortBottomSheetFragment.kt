package com.example.enuyguncase.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.enuyguncase.databinding.BottomSheetSortBinding
import com.example.enuyguncase.ui.viewmodel.ProductViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SortBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: BottomSheetSortBinding? = null
    private val binding get() = _binding!!

    private var onSortApplied: ((ProductViewModel.SortType) -> Unit)? = null
    private var onResetApplied: (() -> Unit)? = null
    private var currentSortType: ProductViewModel.SortType = ProductViewModel.SortType.NAME_ASC

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetSortBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupInitialSelections()
        setupListeners()
    }

    private fun setupInitialSelections() {
        // Set initial sort selection
        when (currentSortType) {
            ProductViewModel.SortType.NAME_ASC -> binding.sortNameAsc.isChecked = true
            ProductViewModel.SortType.NAME_DESC -> binding.sortNameDesc.isChecked = true
            ProductViewModel.SortType.PRICE_ASC -> binding.sortPriceAsc.isChecked = true
            ProductViewModel.SortType.PRICE_DESC -> binding.sortPriceDesc.isChecked = true
            else -> binding.sortDefault.isChecked = true
        }
    }

    private fun setupListeners() {
        // Reset button
        binding.resetButton.setOnClickListener {
            android.util.Log.d("Sort", "Reset button clicked")
            resetSelections()
            onResetApplied?.invoke()
            Toast.makeText(context, "Sort reset", Toast.LENGTH_SHORT).show()
            dismiss()
        }

        // Apply button
        binding.applyButton.setOnClickListener {
            android.util.Log.d("Sort", "Apply button clicked")
            applySort()
        }

        // Container click listeners
        binding.sortDefaultContainer.setOnClickListener {
            selectSortOption(binding.sortDefault)
        }

        binding.sortNameAscContainer.setOnClickListener {
            selectSortOption(binding.sortNameAsc)
        }

        binding.sortNameDescContainer.setOnClickListener {
            selectSortOption(binding.sortNameDesc)
        }

        binding.sortPriceAscContainer.setOnClickListener {
            selectSortOption(binding.sortPriceAsc)
        }

        binding.sortPriceDescContainer.setOnClickListener {
            selectSortOption(binding.sortPriceDesc)
        }
        
        android.util.Log.d("Sort", "Listeners setup completed")
    }

    private fun selectSortOption(selectedRadioButton: android.widget.RadioButton) {
        // Uncheck all radio buttons
        binding.sortDefault.isChecked = false
        binding.sortNameAsc.isChecked = false
        binding.sortNameDesc.isChecked = false
        binding.sortPriceAsc.isChecked = false
        binding.sortPriceDesc.isChecked = false

        // Check the selected radio button
        selectedRadioButton.isChecked = true
    }

    private fun resetSelections() {
        binding.sortDefault.isChecked = true
        binding.sortNameAsc.isChecked = false
        binding.sortNameDesc.isChecked = false
        binding.sortPriceAsc.isChecked = false
        binding.sortPriceDesc.isChecked = false
        currentSortType = ProductViewModel.SortType.NAME_ASC
    }

    private fun applySort() {
        android.util.Log.d("Sort", "applySort called")
        
        // Get selected sort type
        val selectedSortType = when {
            binding.sortDefault.isChecked -> ProductViewModel.SortType.NAME_ASC
            binding.sortNameAsc.isChecked -> ProductViewModel.SortType.NAME_ASC
            binding.sortNameDesc.isChecked -> ProductViewModel.SortType.NAME_DESC
            binding.sortPriceAsc.isChecked -> ProductViewModel.SortType.PRICE_ASC
            binding.sortPriceDesc.isChecked -> ProductViewModel.SortType.PRICE_DESC
            else -> ProductViewModel.SortType.NAME_ASC
        }
        
        android.util.Log.d("Sort", "Selected sort type: $selectedSortType")
        onSortApplied?.invoke(selectedSortType)
        
        val sortText = when (selectedSortType) {
            ProductViewModel.SortType.NAME_ASC -> "Alphabetically A-Z"
            ProductViewModel.SortType.NAME_DESC -> "Alphabetically Z-A"
            ProductViewModel.SortType.PRICE_ASC -> "Price low to high"
            ProductViewModel.SortType.PRICE_DESC -> "Price high to low"
            else -> "Default"
        }
        Toast.makeText(context, "Sort applied: $sortText", Toast.LENGTH_SHORT).show()
        
        dismiss()
    }

    fun setOnSortAppliedListener(listener: (ProductViewModel.SortType) -> Unit) {
        onSortApplied = listener
    }
    
    fun setOnResetAppliedListener(listener: () -> Unit) {
        onResetApplied = listener
    }

    fun setCurrentSelections(sortType: ProductViewModel.SortType) {
        currentSortType = sortType
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "SortBottomSheet"
    }
} 