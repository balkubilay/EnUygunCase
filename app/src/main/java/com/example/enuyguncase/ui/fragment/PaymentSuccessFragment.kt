package com.example.enuyguncase.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.enuyguncase.databinding.FragmentPaymentSuccessBinding
import com.example.enuyguncase.ui.viewmodel.ProductViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Random

@AndroidEntryPoint
class PaymentSuccessFragment : Fragment() {

    private var _binding: FragmentPaymentSuccessBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProductViewModel by viewModels()
    private var timerJob: kotlinx.coroutines.Job? = null
    private var countdown = 4

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPaymentSuccessBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupOrderDetails()
        setupClickArea()
        startTimer()
    }

    private fun setupOrderDetails() {
        // Generate random order number
        val orderNumber = generateOrderNumber()
        binding.orderNumberText.text = "Sipariş No: #$orderNumber"

        // Set current date
        val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale("tr"))
        val currentDate = dateFormat.format(Date())
        binding.orderDateText.text = "Tarih: $currentDate"

        // Get total from arguments or default
        val total = arguments?.getFloat("total", 0.0f) ?: 0.0f
        binding.orderTotalText.text = "Toplam: $${String.format("%.2f", total)}"
    }

    private fun setupClickArea() {
        // Find the click area in the layout
        val clickArea = binding.root.findViewById<View>(com.example.enuyguncase.R.id.click_area)
        clickArea?.setOnClickListener {
            navigateToHome()
        }
    }

    private fun startTimer() {
        timerJob = lifecycleScope.launch {
            while (countdown > 0) {
                binding.timerText.text = "$countdown saniye sonra otomatik dönüş"
                delay(1000)
                countdown--
            }
            navigateToHome()
        }
    }

    private fun navigateToHome() {
        // Clear cart
        viewModel.clearCart()
        
        // Navigate to home
        findNavController().navigate(
            com.example.enuyguncase.R.id.action_paymentSuccessFragment_to_productListFragment
        )
    }

    private fun generateOrderNumber(): String {
        val random = Random()
        return String.format("%09d", random.nextInt(1000000000))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        timerJob?.cancel()
        _binding = null
    }
} 