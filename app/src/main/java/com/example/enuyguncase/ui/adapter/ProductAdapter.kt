package com.example.enuyguncase.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.enuyguncase.data.model.Product
import com.example.enuyguncase.databinding.ItemProductBinding

class ProductAdapter(
    private val onProductClick: (Product) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {
    
    private var products = listOf<Product>()
    
    fun updateProducts(newProducts: List<Product>) {
        products = newProducts
        notifyDataSetChanged()
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ProductViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(products[position])
    }
    
    override fun getItemCount(): Int = products.size
    
    inner class ProductViewHolder(
        private val binding: ItemProductBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(product: Product) {
            // Set product name
            binding.productName.text = product.title
            
            // Set product description
            binding.productDescription.text = product.description
            
            // Format price with discount
            val originalPrice = product.price
            val discountPrice = originalPrice * (1 - product.discountPercentage / 100)
            
            // Set prices
            binding.currentPrice.text = "$${String.format("%.2f", discountPrice)}"
            binding.originalPrice.text = "$${String.format("%.2f", originalPrice)}"
            
            // Load image with Glide
            Glide.with(binding.productImage.context)
                .load(product.thumbnail)
                .centerCrop()
                .into(binding.productImage)
            
            // Set click listener for the entire item
            binding.root.setOnClickListener {
                onProductClick(product)
            }
        }
    }
} 