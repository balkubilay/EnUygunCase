package com.example.enuyguncase.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.enuyguncase.databinding.FragmentFavoriteBinding
import com.example.enuyguncase.ui.adapter.FavoriteAdapter
import com.example.enuyguncase.ui.viewmodel.ProductViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoriteFragment : Fragment() {
    
    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: ProductViewModel by viewModels()
    private lateinit var favoriteAdapter: FavoriteAdapter
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupRecyclerView()
        setupObservers()
    }
    
    private fun setupRecyclerView() {
        favoriteAdapter = FavoriteAdapter(
            onProductClick = { product ->
                // Navigate to product detail
                Toast.makeText(context, "Product clicked: ${product.title}", Toast.LENGTH_SHORT).show()
            },
            onAddToCartClick = { product ->
                viewModel.addToCart(product)
                Toast.makeText(context, "${product.title} sepete eklendi", Toast.LENGTH_SHORT).show()
            },
            onRemoveFromFavorites = { favoriteItem ->
                viewModel.removeFromFavoritesByItemId(favoriteItem.id)
                Toast.makeText(context, "${favoriteItem.product.title} favorilerden çıkarıldı", Toast.LENGTH_SHORT).show()
            },
            onIncreaseQuantity = { product ->
                val cartItem = viewModel.getCartItem(product.id)
                if (cartItem != null) {
                    viewModel.updateCartQuantity(cartItem.id, cartItem.quantity + 1)
                    Toast.makeText(context, "${product.title} miktarı artırıldı", Toast.LENGTH_SHORT).show()
                }
            },
            onDecreaseQuantity = { product ->
                val cartItem = viewModel.getCartItem(product.id)
                if (cartItem != null) {
                    android.util.Log.d("FavoriteFragment", "Decreasing quantity for product: ${product.title}, current quantity: ${cartItem.quantity}, cartItem.id: ${cartItem.id}")
                    if (cartItem.quantity > 1) {
                        viewModel.updateCartQuantity(cartItem.id, cartItem.quantity - 1)
                        Toast.makeText(context, "${product.title} miktarı azaltıldı", Toast.LENGTH_SHORT).show()
                    } else {
                        viewModel.removeFromCart(cartItem.id)
                        Toast.makeText(context, "${product.title} sepetten çıkarıldı", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    android.util.Log.d("FavoriteFragment", "Cart item not found for product: ${product.title}")
                    Toast.makeText(context, "Ürün sepette bulunamadı", Toast.LENGTH_SHORT).show()
                }
            },
            getCartItemQuantity = { productId ->
                val cartItem = viewModel.getCartItem(productId)
                cartItem?.quantity ?: 0
            }
        )
        
        binding.favoriteRecyclerView.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = favoriteAdapter
        }
    }
    
    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.favoriteItems.collectLatest { favoriteItems ->
                favoriteAdapter.updateFavoriteItems(favoriteItems)
                updateEmptyState(favoriteItems.isEmpty())
            }
        }
        
        // Observe cart changes to update favorite items UI
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.cartItems.collectLatest { cartItems ->
                // Refresh the adapter to update cart UI for all favorite items
                favoriteAdapter.notifyDataSetChanged()
            }
        }
    }
    
    private fun updateEmptyState(isEmpty: Boolean) {
        if (isEmpty) {
            binding.emptyFavoritesLayout.visibility = View.VISIBLE
            binding.favoritesContentLayout.visibility = View.GONE
        } else {
            binding.emptyFavoritesLayout.visibility = View.GONE
            binding.favoritesContentLayout.visibility = View.VISIBLE
        }
    }
    
    private fun showClearFavoritesDialog() {
        androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle("Favorileri Temizle")
            .setMessage("Tüm favorileri silmek istediğinizden emin misiniz?")
            .setPositiveButton("Evet") { _, _ ->
                viewModel.clearFavorites()
                Toast.makeText(context, "Favoriler temizlendi", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Hayır", null)
            .show()
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 