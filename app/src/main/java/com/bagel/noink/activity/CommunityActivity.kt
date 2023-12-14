package com.bagel.noink.activity
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bagel.noink.R
import com.bagel.noink.adapter.CommunityAdapter
import com.bagel.noink.bean.CommunityItemBean
import com.bagel.noink.databinding.ActivityCommunityBinding


class CommunityActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityCommunityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommunityBinding.inflate(layoutInflater)

        setContentView(binding.root)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_community)
        val navController = navHostFragment?.findNavController()

        appBarConfiguration = AppBarConfiguration(navController!!.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_community)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}
