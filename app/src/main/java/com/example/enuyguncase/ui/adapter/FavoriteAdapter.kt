package com.example.enuyguncase.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.enuyguncase.data.model.FavoriteItem
import com.example.enuyguncase.databinding.ItemFavoriteProductBinding

class FavoriteAdapter(
    private val onProductClick: (com.example.enuyguncase.data.model.Product) -> Unit,
    private val onAddToCartClick: (com.example.enuyguncase.data.model.Product) -> Unit,
    private val onRemoveFromFavorites: (FavoriteItem) -> Unit,
    private val onIncreaseQuantity: (com.example.enuyguncase.data.model.Product) -> Unit,
    private val onDecreaseQuantity: (com.example.enuyguncase.data.model.Product) -> Unit,
    private val getCartItemQuantity: (Int) -> Int
) : RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {
    
    private var favoriteItems = listOf<FavoriteItem>()
    
    fun updateFavoriteItems(newFavoriteItems: List<FavoriteItem>) {
        favoriteItems = newFavoriteItems
        notifyDataSetChanged()
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = ItemFavoriteProductBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FavoriteViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(favoriteItems[position])
    }
    
    override fun getItemCount(): Int = favoriteItems.size
    
    inner class FavoriteViewHolder(
        private val binding: ItemFavoriteProductBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(favoriteItem: FavoriteItem) {
            val product = favoriteItem.product
            
            binding.apply {
                // Always ensure price is visible
                productPrice.visibility = View.VISIBLE
                
                productName.text = product.title
                productDescription.text = product.description
                
                // Format prices
                val originalPrice = product.price
                val discountPrice = originalPrice * (1 - product.discountPercentage / 100)
                
                productPrice.text = "$${String.format("%.2f", discountPrice)}"
                
                // Load image
                Glide.with(productImage.context)
                    .load(product.thumbnail)
                    .centerCrop()
                    .into(productImage)
                
                // Check if product is in cart and update UI accordingly
                val cartQuantity = getCartItemQuantity(product.id)
                updateCartUI(cartQuantity)
                
                // Set click listeners
                root.setOnClickListener {
                    onProductClick(product)
                }
                
                addToCartButton.setOnClickListener {
                    onAddToCartClick(product)
                    // Update UI after adding to cart
                    val newQuantity = getCartItemQuantity(product.id)
                    updateCartUI(newQuantity)
                }
                
                // Remove from favorites by clicking on heart icon container
                heartIcon.setOnClickListener {
                    onRemoveFromFavorites(favoriteItem)
                }
                
                // Quantity control listeners
                increaseButton.setOnClickListener {
                    onIncreaseQuantity(product)
                    // Update UI after increasing quantity
                    val newQuantity = getCartItemQuantity(product.id)
                    updateCartUI(newQuantity)
                }
                
                decreaseButton.setOnClickListener {
                    onDecreaseQuantity(product)
                    // Update UI after decreasing quantity
                    val newQuantity = getCartItemQuantity(product.id)
                    updateCartUI(newQuantity)
                }
            }
        }
        
        private fun updateCartUI(cartQuantity: Int) {
            binding.apply {
                // Always ensure price is visible
                productPrice.visibility = View.VISIBLE
                
                if (cartQuantity > 0) {
                    // Product is in cart - show quantity controls
                    addToCartButton.visibility = View.GONE
                    quantityControls.visibility = View.VISIBLE
                    quantityText.text = cartQuantity.toString()
                } else {
                    // Product is not in cart - show add to cart button
                    addToCartButton.visibility = View.VISIBLE
                    quantityControls.visibility = View.GONE
                }
            }
        }
    }
} 