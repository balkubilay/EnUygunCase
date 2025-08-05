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
import androidx.navigation.fragment.findNavController
import com.example.enuyguncase.databinding.FragmentCheckoutBinding
import com.example.enuyguncase.ui.viewmodel.ProductViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CheckoutFragment : Fragment() {
    
    private var _binding: FragmentCheckoutBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: ProductViewModel by viewModels()
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCheckoutBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // Hide bottom navigation
        hideBottomNavigation()
        
        setupClickListeners()
        setupTextWatchers()
        observeCartData()
    }
    
    private fun hideBottomNavigation() {
        val activity = activity as? com.example.enuyguncase.MainActivity
        activity?.hideBottomNavigation()
    }
    
    private fun setupClickListeners() {
        // Back button
        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }
        
        // Pay button
        binding.payButton.setOnClickListener {
            if (validateForm()) {
                // Navigate to payment success
                val total = viewModel.totalCartPrice.value
                val action = com.example.enuyguncase.R.id.action_checkoutFragment_to_paymentSuccessFragment
                findNavController().navigate(action, Bundle().apply {
                    putFloat("total", total.toFloat())
                })
            } else {
                Toast.makeText(context, "Lütfen tüm alanları doldurun", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    private fun setupTextWatchers() {
        // Name input validation
        binding.nameEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                updateInputField(binding.nameEditText, s?.toString()?.isNotEmpty() == true)
            }
        })
        
        // Email input validation
        binding.emailEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val isValid = s?.toString()?.isNotEmpty() == true && 
                             android.util.Patterns.EMAIL_ADDRESS.matcher(s.toString()).matches()
                updateInputField(binding.emailEditText, isValid)
            }
        })
        
        // Phone input validation
        binding.phoneEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val isValid = s?.toString()?.isNotEmpty() == true && s.toString().length >= 10
                updateInputField(binding.phoneEditText, isValid)
            }
        })
    }
    
    private fun updateInputField(editText: android.widget.EditText, isValid: Boolean) {
        if (isValid) {
            editText.setBackgroundResource(com.example.enuyguncase.R.drawable.input_field_background)
            editText.setTextColor(resources.getColor(com.example.enuyguncase.R.color.black, null))
            editText.setHintTextColor(resources.getColor(com.example.enuyguncase.R.color.medium_gray, null))
        } else {
            editText.setBackgroundResource(com.example.enuyguncase.R.drawable.input_field_background_error)
            editText.setTextColor(resources.getColor(com.example.enuyguncase.R.color.black, null))
            editText.setHintTextColor(resources.getColor(com.example.enuyguncase.R.color.red, null))
        }
    }
    
    private fun observeCartData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.totalCartPrice.collectLatest { totalPrice ->
                // Total price is no longer displayed in this design
            }
        }
    }
    
    private fun validateForm(): Boolean {
        val name = binding.nameEditText.text.toString().trim()
        val email = binding.emailEditText.text.toString().trim()
        val phone = binding.phoneEditText.text.toString().trim()
        
        // Basic validation
        if (name.isEmpty()) {
            binding.nameEditText.error = "İsim gerekli"
            updateInputField(binding.nameEditText, false)
            return false
        }
        
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.emailEditText.error = "Geçerli email gerekli"
            updateInputField(binding.emailEditText, false)
            return false
        }
        
        if (phone.isEmpty() || phone.length < 10) {
            binding.phoneEditText.error = "Geçerli telefon numarası gerekli"
            updateInputField(binding.phoneEditText, false)
            return false
        }
        
        return true
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        // Show bottom navigation when leaving
        val activity = activity as? com.example.enuyguncase.MainActivity
        activity?.showBottomNavigation()
        _binding = null
    }
} 