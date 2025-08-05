package com.example.enuyguncase

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import com.example.enuyguncase.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityMainBinding
    
    @Inject
    lateinit var cartRepository: com.example.enuyguncase.data.repository.CartRepository
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Full screen setup
        setupFullScreen()
        
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupNavigation()
        setupCartBadge()
    }
    
    private fun setupFullScreen() {
        // Hide status bar and navigation bar
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        
        // Make the app edge-to-edge
        enableEdgeToEdge()
        
        // Hide system bars
        WindowCompat.setDecorFitsSystemWindows(window, false)
        
        // Set up window insets controller
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.apply {
            // Hide status bar
            hide(WindowInsetsCompat.Type.statusBars())
            // Hide navigation bar
            hide(WindowInsetsCompat.Type.navigationBars())
            // Make system bars behave as hidden
            systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
        
        // Additional flags to ensure full screen
        window.decorView.systemUiVisibility = (
            View.SYSTEM_UI_FLAG_FULLSCREEN or
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        )
    }
    
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            // Re-apply full screen when window gains focus
            window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            )
        }
    }
    
    private fun setupNavigation() {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        
        // Handle tab clicks
        binding.bottomNavigation.findViewById<android.view.View>(R.id.home_tab)?.setOnClickListener {
            navController.navigate(R.id.productListFragment)
        }
        
        binding.bottomNavigation.findViewById<android.view.View>(R.id.favorites_tab)?.setOnClickListener {
            navController.navigate(R.id.favoriteFragment)
        }
        
        binding.bottomNavigation.findViewById<android.view.View>(R.id.cart_tab)?.setOnClickListener {
            navController.navigate(R.id.cartFragment)
        }
        
        // Listen to navigation destination changes to update active tab
        navController.addOnDestinationChangedListener { _, destination, _ ->
            updateActiveTab(destination)
        }
    }
    
    private fun setupCartBadge() {
        // Observe cart total items and update badge
        CoroutineScope(Dispatchers.Main).launch {
            cartRepository.totalItems.collectLatest { totalItems ->
                updateCartBadge(totalItems)
            }
        }
    }
    
    private fun updateCartBadge(totalItems: Int) {
        if (totalItems > 0) {
            binding.cartBadge.visibility = View.VISIBLE
            binding.cartBadge.text = if (totalItems > 99) "99+" else totalItems.toString()
        } else {
            binding.cartBadge.visibility = View.GONE
        }
    }
    
    private fun updateActiveTab(destination: NavDestination) {
        // Reset all tabs to inactive state
        updateTabState(R.id.home_tab, false)
        updateTabState(R.id.favorites_tab, false)
        updateTabState(R.id.cart_tab, false)
        
        // Set active tab based on current destination
        when (destination.id) {
            R.id.productListFragment -> updateTabState(R.id.home_tab, true)
            R.id.favoriteFragment -> updateTabState(R.id.favorites_tab, true)
            R.id.cartFragment -> updateTabState(R.id.cart_tab, true)
        }
    }
    
    private fun updateTabState(tabId: Int, isActive: Boolean) {
        val tab = binding.bottomNavigation.findViewById<android.view.View>(tabId)
        
        when (tabId) {
            R.id.home_tab -> {
                val icon = binding.homeIcon
                val text = binding.homeText
                updateTabColors(icon, text, isActive)
            }
            R.id.favorites_tab -> {
                val icon = binding.favoritesIcon
                val text = binding.favoritesText
                updateTabColors(icon, text, isActive)
            }
            R.id.cart_tab -> {
                val icon = binding.cartIcon
                val text = binding.cartText
                updateTabColors(icon, text, isActive)
            }
        }
    }
    
    private fun updateTabColors(icon: android.widget.ImageView?, text: android.widget.TextView?, isActive: Boolean) {
        if (isActive) {
            icon?.setColorFilter(resources.getColor(com.example.enuyguncase.R.color.black, theme))
            text?.setTextColor(resources.getColor(com.example.enuyguncase.R.color.black, theme))
        } else {
            icon?.setColorFilter(resources.getColor(com.example.enuyguncase.R.color.medium_gray, theme))
            text?.setTextColor(resources.getColor(com.example.enuyguncase.R.color.medium_gray, theme))
        }
    }
    
    // Method to hide bottom navigation
    fun hideBottomNavigation() {
        binding.bottomNavigation.visibility = View.GONE
    }
    
    // Method to show bottom navigation
    fun showBottomNavigation() {
        binding.bottomNavigation.visibility = View.VISIBLE
    }
}