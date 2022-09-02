package idv.hsu.media.downloader

import android.Manifest
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import idv.hsu.media.downloader.databinding.ActivityMainBinding
import idv.hsu.media.downloader.utils.changeDarkMode
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainViewModel by viewModels()

    private val storagePermissions = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        navView.setupWithNavController(navController)

        if (!viewModel.writeFilePermission(this)) {
            actionRequestPermission().launch(storagePermissions)
        }

        lifecycleScope.launch {
            viewModel.darkMode.collectLatest {
                if (it != null) {
                    changeDarkMode(it)
                }
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        // TODO ask user really want to leave
    }

    private fun actionRequestPermission() =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissions.entries.forEach {
                Timber.d("${it.key} -- ${it.value}")
            }
        }
}