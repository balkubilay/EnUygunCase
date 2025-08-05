package com.example.enuyguncase.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import com.example.enuyguncase.databinding.BottomSheetFilterBinding
import com.example.enuyguncase.ui.viewmodel.ProductViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FilterBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: BottomSheetFilterBinding? = null
    private val binding get() = _binding!!

    private var onFilterApplied: ((String?) -> Unit)? = null
    private var onResetApplied: (() -> Unit)? = null
    private var currentFilter: String? = null
    private var availableCategories: List<String> = emptyList()
    private val categoryRadioButtons = mutableListOf<RadioButton>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetFilterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupFilterOptions()
        setupInitialSelections()
        setupListeners()
    }

    private fun setupInitialSelections() {
        // Set initial filter selection
        if (currentFilter == null) {
            // "All Products" is the first item in the list
            if (categoryRadioButtons.isNotEmpty()) {
                categoryRadioButtons[0].isChecked = true
            }
        } else {
            // Find the matching category radio button (skip "All Products" which is at index 0)
            val categoryIndex = availableCategories.indexOf(currentFilter)
            if (categoryIndex >= 0 && categoryIndex + 1 < categoryRadioButtons.size) {
                categoryRadioButtons[categoryIndex + 1].isChecked = true
            } else {
                // Default to "All Products"
                if (categoryRadioButtons.isNotEmpty()) {
                    categoryRadioButtons[0].isChecked = true
                }
            }
        }
    }

    private fun setupFilterOptions() {
        // Clear existing category radio buttons
        val filterGroup = binding.filterRadioGroup
        for (i in filterGroup.childCount - 1 downTo 0) {
            filterGroup.removeViewAt(i)
        }
        categoryRadioButtons.clear()
        
        // Create all filter options including "All Products"
        val allFilterOptions = mutableListOf<String>()
        allFilterOptions.add("ALL PRODUCTS") // Add "All Products" as first item
        allFilterOptions.addAll(availableCategories.map { it.uppercase() }) // Add categories in uppercase
        
        // Add all filter options dynamically
        allFilterOptions.forEach { filterOption ->
            val containerLayout = LinearLayout(requireContext()).apply {
                id = View.generateViewId()
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                orientation = LinearLayout.HORIZONTAL
                gravity = android.view.Gravity.CENTER_VERTICAL
                setBackgroundResource(com.example.enuyguncase.R.drawable.selectable_item_background)
                isClickable = true
                isFocusable = true
                setPadding(16, 16, 16, 16)
                (layoutParams as? LinearLayout.LayoutParams)?.bottomMargin = 8
                
                val radioButton = RadioButton(requireContext()).apply {
                    id = View.generateViewId()
                    setButtonDrawable(com.example.enuyguncase.R.drawable.radio_button_selector)
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    background = null
                    setPadding(0, 0, 0, 0)
                    isClickable = false
                    isFocusable = false
                }
                categoryRadioButtons.add(radioButton)
                
                val textContainer = LinearLayout(requireContext()).apply {
                    layoutParams = LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1f
                    )
                    orientation = LinearLayout.VERTICAL
                    (layoutParams as? LinearLayout.LayoutParams)?.marginStart = 16
                    
                    val titleTextView = TextView(requireContext()).apply {
                        layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                        text = filterOption
                        setTextColor(resources.getColor(com.example.enuyguncase.R.color.black, null))
                        textSize = 16f
                        setTypeface(null, android.graphics.Typeface.BOLD)
                    }
                    
                    val subtitleTextView = TextView(requireContext()).apply {
                        layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                        text = if (filterOption == "ALL PRODUCTS") {
                            "Show all available products"
                        } else {
                            "Show products from $filterOption category"
                        }
                        setTextColor(resources.getColor(com.example.enuyguncase.R.color.medium_gray, null))
                        textSize = 14f
                        (layoutParams as? LinearLayout.LayoutParams)?.topMargin = 2
                    }
                    
                    addView(titleTextView)
                    addView(subtitleTextView)
                }
                
                addView(radioButton)
                addView(textContainer)
                
                // Add click listener to container
                setOnClickListener {
                    selectFilterOption(radioButton)
                }
            }
            filterGroup.addView(containerLayout)
        }
    }

    private fun setupListeners() {
        // Reset button
        binding.resetButton.setOnClickListener {
            android.util.Log.d("Filter", "Reset button clicked")
            resetSelections()
            onResetApplied?.invoke()
            Toast.makeText(context, "Filters reset", Toast.LENGTH_SHORT).show()
            dismiss()
        }

        // Apply button
        binding.applyButton.setOnClickListener {
            android.util.Log.d("Filter", "Apply button clicked")
            applyFilter()
        }
        
        android.util.Log.d("Filter", "Listeners setup completed")
    }

    private fun selectFilterOption(selectedRadioButton: RadioButton) {
        // Uncheck all radio buttons
        categoryRadioButtons.forEach { it.isChecked = false }

        // Check the selected radio button
        selectedRadioButton.isChecked = true
    }

    private fun resetSelections() {
        categoryRadioButtons.forEach { it.isChecked = false }
        if (categoryRadioButtons.isNotEmpty()) {
            categoryRadioButtons[0].isChecked = true
        }
        currentFilter = null
    }

    private fun applyFilter() {
        android.util.Log.d("Filter", "applyFilter called")
        
        // Get selected filter
        val selectedFilter = when {
            categoryRadioButtons.isEmpty() -> null
            categoryRadioButtons[0].isChecked -> null // "All Products" selected
            else -> {
                val checkedIndex = categoryRadioButtons.indexOfFirst { it.isChecked }
                if (checkedIndex > 0 && checkedIndex - 1 < availableCategories.size) {
                    availableCategories[checkedIndex - 1] // Get original category name
                } else null
            }
        }
        
        android.util.Log.d("Filter", "Selected filter: $selectedFilter")
        onFilterApplied?.invoke(selectedFilter)
        
        val filterText = if (selectedFilter == null) "All categories" else selectedFilter
        Toast.makeText(context, "Filter applied: $filterText", Toast.LENGTH_SHORT).show()
        
        dismiss()
    }

    fun setOnFilterAppliedListener(listener: (String?) -> Unit) {
        onFilterApplied = listener
    }
    
    fun setOnResetAppliedListener(listener: () -> Unit) {
        onResetApplied = listener
    }

    fun setCurrentSelections(filter: String?, categories: List<String>) {
        currentFilter = filter
        availableCategories = categories
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "FilterBottomSheet"
    }
} 