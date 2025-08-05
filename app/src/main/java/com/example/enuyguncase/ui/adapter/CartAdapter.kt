package com.example.enuyguncase.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.enuyguncase.data.model.CartItem
import com.example.enuyguncase.databinding.ItemCartBinding

class CartAdapter(
    private val onQuantityChange: (CartItem, Int) -> Unit,
    private val onRemoveItem: (CartItem) -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {
    
    private var cartItems = listOf<CartItem>()
    
    fun updateCartItems(newCartItems: List<CartItem>) {
        cartItems = newCartItems
        notifyDataSetChanged()
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = ItemCartBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CartViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(cartItems[position])
    }
    
    override fun getItemCount(): Int = cartItems.size
    
    inner class CartViewHolder(
        private val binding: ItemCartBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(cartItem: CartItem) {
            binding.apply {
                productTitle.text = cartItem.product.title
                
                // Format prices
                val originalPrice = cartItem.product.price
                val discountPrice = originalPrice * (1 - cartItem.product.discountPercentage / 100)
                
                // Set current price
                productPrice.text = "$${String.format("%.2f", discountPrice)}"
                
                // Set original price with strikethrough
                productOriginalPrice.text = "$${String.format("%.2f", originalPrice)}"
                productOriginalPrice.paintFlags = productOriginalPrice.paintFlags or android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
                
                // Set quantity
                quantityText.text = cartItem.quantity.toString()
                
                // Load image
                Glide.with(productImage.context)
                    .load(cartItem.product.thumbnail)
                    .centerCrop()
                    .into(productImage)
                
                // Set click listeners
                increaseButton.setOnClickListener {
                    onQuantityChange(cartItem, cartItem.quantity + 1)
                }
                
                decreaseButton.setOnClickListener {
                    if (cartItem.quantity > 1) {
                        onQuantityChange(cartItem, cartItem.quantity - 1)
                    }
                }
                
                removeButton.setOnClickListener {
                    onRemoveItem(cartItem)
                }
            }
        }
    }
} 