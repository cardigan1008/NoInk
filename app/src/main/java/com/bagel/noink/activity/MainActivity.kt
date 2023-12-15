package com.bagel.noink.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import com.bagel.noink.R
import com.bagel.noink.databinding.ActivityMainBinding
import com.bagel.noink.ui.account.AccountViewModel
import com.bagel.noink.utils.HttpRequest
import com.bagel.noink.utils.UserHttpRequest
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        binding.appBarMain.fab.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val bottomNavigationView: BottomNavigationView = binding.bottomNavView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_history, R.id.nav_community,R.id.nav_personal_account
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        bottomNavigationView.setupWithNavController(navController)
        navView.setupWithNavController(navController)
        bottomNavigationView.setupWithNavController(navController)
        navView.setupWithNavController(navController)
        
        // 进入主界面进行的操作
        val sharedPreferences =
            this@MainActivity.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        AccountViewModel.token = sharedPreferences.getString("token", "")

        val userHttpRequest = UserHttpRequest()
        userHttpRequest.getUserInfo(callbackListener = object : HttpRequest.CallbackListener {
            override fun onSuccess(responseJson: JSONObject) {
                val data = responseJson.getJSONObject("data")
                lifecycleScope.launch {
                    AccountViewModel.updateUserInfoByJson(data)
                    AccountViewModel.saveToken(this@MainActivity)
                }
            }

            override fun onFailure(errorMessage: String) {
                val intent = Intent(this@MainActivity, LoginActivity::class.java)
                startActivity(intent)
            }

        })
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}