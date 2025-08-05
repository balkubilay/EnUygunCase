package com.example.enuyguncase.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.enuyguncase.databinding.FragmentCartBinding
import com.example.enuyguncase.ui.adapter.CartAdapter
import com.example.enuyguncase.ui.viewmodel.ProductViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

@AndroidEntryPoint
class CartFragment : Fragment() {

    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProductViewModel by viewModels()
    private lateinit var cartAdapter: CartAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupObservers()
        setupButtons()
    }

    private fun setupRecyclerView() {
        cartAdapter = CartAdapter(
            onQuantityChange = { cartItem, newQuantity ->
                viewModel.updateCartQuantity(cartItem.id, newQuantity)
            },
            onRemoveItem = { cartItem ->
                viewModel.removeFromCart(cartItem.id)
                Toast.makeText(context, "${cartItem.product.title} sepetten çıkarıldı", Toast.LENGTH_SHORT).show()
            }
        )

        binding.cartRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = cartAdapter
        }
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.cartItems.collectLatest { cartItems ->
                cartAdapter.updateCartItems(cartItems)
                updateEmptyState(cartItems.isEmpty())
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.totalCartItems.collectLatest { totalItems ->
                binding.totalItemsText.text = "Toplam $totalItems ürün"
            }
        }

        // Observe cart summary for dynamic price calculations
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.cartSummary.collectLatest { summary ->
                updateCartSummary(summary)
            }
        }
    }

    private fun updateCartSummary(summary: ProductViewModel.CartSummary) {
        // Update price (subtotal)
        binding.priceText.text = String.format("%.2f", summary.price)
        
        // Update discount
        binding.discountText.text = String.format("%.2f", summary.discount)
        
        // Update total
        binding.totalPriceText.text = String.format("%.2f", summary.total)
        
        // Show/hide discount section based on whether there's a discount
        val discountContainer = binding.discountText.parent as? android.widget.LinearLayout
        discountContainer?.visibility = if (summary.discount > 0) View.VISIBLE else View.GONE
    }

    private fun setupButtons() {
        binding.checkoutButton.setOnClickListener {
            // Navigate to checkout
            findNavController().navigate(com.example.enuyguncase.R.id.action_cartFragment_to_checkoutFragment)
        }
    }

    private fun updateEmptyState(isEmpty: Boolean) {
        if (isEmpty) {
            binding.emptyCartLayout.visibility = View.VISIBLE
            binding.cartContentLayout.visibility = View.GONE
        } else {
            binding.emptyCartLayout.visibility = View.GONE
            binding.cartContentLayout.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 