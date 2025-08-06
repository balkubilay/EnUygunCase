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
import com.bumptech.glide.Glide
import com.example.enuyguncase.databinding.FragmentProductDetailBinding
import com.example.enuyguncase.ui.viewmodel.ProductViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductDetailFragment : Fragment() {

    private var _binding: FragmentProductDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProductViewModel by viewModels()
    private var currentProduct: com.example.enuyguncase.data.model.Product? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupClickListeners()
        loadProductDetails()
        observeProductData()
        observeFavoriteState()
        observeCartState()
    }

    private fun setupClickListeners() {
        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.favoriteButton.setOnClickListener {
            currentProduct?.let { product ->
                if (viewModel.isInFavorites(product.id)) {
                    viewModel.removeFromFavorites(product.id)
                    Toast.makeText(context, "${product.title} favorilerden çıkarıldı", Toast.LENGTH_SHORT).show()
                } else {
                    viewModel.addToFavorites(product)
                    Toast.makeText(context, "${product.title} favorilere eklendi", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.addToCartButton.setOnClickListener {
            currentProduct?.let { product ->
                viewModel.addToCart(product)
                Toast.makeText(context, "${product.title} sepete eklendi", Toast.LENGTH_SHORT).show()
            }
        }

        binding.increaseButton.setOnClickListener {
            currentProduct?.let { product ->
                viewModel.addToCart(product)
            }
        }

        binding.decreaseButton.setOnClickListener {
            currentProduct?.let { product ->
                val cartItem = viewModel.getCartItem(product.id)
                if (cartItem != null) {
                    if (cartItem.quantity > 1) {
                        viewModel.updateCartQuantity(cartItem.id, cartItem.quantity - 1)
                    } else {
                        viewModel.removeFromCart(cartItem.id)
                        Toast.makeText(context, "${product.title} sepetten çıkarıldı", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun loadProductDetails() {
        val productId = arguments?.getString("productId")
        if (productId != null) {
            viewModel.getProductById(productId.toIntOrNull() ?: 0)
        }
    }

    private fun observeProductData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.filteredProducts.collectLatest { products ->
                val productId = arguments?.getString("productId")?.toIntOrNull()
                currentProduct = products.find { it.id == productId }
                currentProduct?.let { product ->
                    displayProductDetails(product)
                }
            }
        }
    }

    private fun observeFavoriteState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.favoriteItems.collectLatest { favoriteItems ->
                currentProduct?.let { product ->
                    val isInFavorites = favoriteItems.any { it.product.id == product.id }
                    binding.favoriteButton.isSelected = isInFavorites
                    updateFavoriteIconColor(isInFavorites)
                }
            }
        }
    }

    private fun observeCartState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.cartItems.collectLatest { cartItems ->
                currentProduct?.let { product ->
                    val cartItem = cartItems.find { it.product.id == product.id }
                    updateCartUI(cartItem)
                }
            }
        }
    }

    private fun updateCartUI(cartItem: com.example.enuyguncase.data.model.CartItem?) {
        if (cartItem != null) {
            binding.addToCartButton.visibility = View.GONE
            binding.quantityControls.visibility = View.VISIBLE
            binding.quantityText.text = cartItem.quantity.toString()
        } else {
            binding.addToCartButton.visibility = View.VISIBLE
            binding.quantityControls.visibility = View.GONE
        }
    }

    private fun updateFavoriteIconColor(isInFavorites: Boolean) {
        if (isInFavorites) {
            binding.favoriteIcon.setImageResource(com.example.enuyguncase.R.drawable.ic_heart_filled)
            binding.favoriteIcon.setColorFilter(android.graphics.Color.parseColor("#FF4444"))
        } else {
            binding.favoriteIcon.setImageResource(com.example.enuyguncase.R.drawable.ic_heart)
            binding.favoriteIcon.setColorFilter(android.graphics.Color.parseColor("#FFFFFF"))
        }
    }

    private fun displayProductDetails(product: com.example.enuyguncase.data.model.Product) {
        binding.productTitle.text = product.title
        binding.productTitleHeader.text = product.title

        val originalPrice = product.price
        val discountPrice = originalPrice * (1 - product.discountPercentage / 100)

        binding.originalPrice.text = "$${String.format("%.2f", originalPrice)}"
        binding.currentPrice.text = "$${String.format("%.2f", discountPrice)}"

        binding.discountBadge.text = "%${product.discountPercentage.toInt()}"

        binding.productDescription.text = product.description

        Glide.with(this)
            .load(product.thumbnail)
            .centerCrop()
            .into(binding.productImage)

        val isInFavorites = viewModel.isInFavorites(product.id)
        binding.favoriteButton.isSelected = isInFavorites
        updateFavoriteIconColor(isInFavorites)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 