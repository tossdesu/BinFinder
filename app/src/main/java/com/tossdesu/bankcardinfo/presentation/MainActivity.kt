package com.tossdesu.bankcardinfo.presentation

import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.allViews
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.tossdesu.bankcardinfo.R
import com.tossdesu.bankcardinfo.databinding.ActivityMainBinding
import com.tossdesu.bankcardinfo.domain.entity.CardInfo
import com.tossdesu.bankcardinfo.presentation.MainActivityUiState.*
import com.tossdesu.bankcardinfo.presentation.adapter.CardBinsAdapter
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var cardBinsAdapter: CardBinsAdapter

    private val component by lazy {
        (application as BinFinderApp).component
    }

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initAdapter()
        observeBinSearching()
        observeSearchView()
    }

    private fun initAdapter() {
        binding.rvBinsHistory.adapter = cardBinsAdapter
        // Set lambda for on adapter item click event
        cardBinsAdapter.onHistoryBinClick = { bin ->
            viewModel.getCardInfo(bin, true)
            binding.searchView.setQuery(bin, false)
        }
    }

    private fun observeBinSearching() {
        with(binding) {
            viewModel.uiState.observe(this@MainActivity) { uiState ->
                // Enable SearchView and RecyclerView if they were disabled when loading data
                if (!searchView.isEnabled)
                    enableSearchView()
                if (!rvBinsHistory.isEnabled)
                    enableRecyclerView()
                // Hide progressBar in all cases except when loading search history of bin numbers
                if (progressBar.isShown && uiState !is BinSearchHistoryData)
                    progressBar.visibility = View.GONE
                // Analysing what we should do with UI of activity
                when (uiState) {
                    is CardData -> {
                        startCardInfoActivity(uiState.cardInfo)
                        searchView.setQuery("", false)
                    }
                    is BinSearchHistoryData -> {
                        if (uiState.cardBins.isNotEmpty()) {
                            if (tvHistoryEmpty.isShown) {
                                tvHistoryEmpty.visibility = View.GONE
                                rvBinsHistory.visibility = View.VISIBLE
                            }
                            cardBinsAdapter.submitList(uiState.cardBins) {
                                rvBinsHistory.scrollToPosition(0)
                            }
                        }
                    }
                    is Loading -> {
                        // Disable SearchView and RecyclerView when loading data
                        enableSearchView(false)
                        enableRecyclerView(false)
                        // Show progressBar
                        progressBar.visibility = View.VISIBLE
                    }
                    is Error -> {
                        val message = resources.getText(uiState.messageStringResource).toString()
                        showError(message)
                    }
                    is NoConnectionError -> {
                        showNoConnectionError()
                    }
                    is NothingFoundNotification -> {
                        val message = resources.getText(R.string.nothing_found).toString()
                        showDialog(message = message)
                    }
                    is FatalError -> {
                        showDialog(uiState.title, uiState.message)
                    }
                }
            }
        }
    }

    private fun observeSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    hideKeyBoard()
                    viewModel.getCardInfo(query)
                    return true
                }
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    private fun startCardInfoActivity(cardInfo: CardInfo) {
        val intent = CardInfoActivity.newIntent(
            this,
            getSearchBinString(),
            cardInfo
        )
        startActivity(intent)
    }

    private fun showError(message: String) {
        Snackbar.make(
            binding.root,
            message,
            Snackbar.LENGTH_LONG
        ).show()
    }

    private fun showNoConnectionError() {
        Snackbar.make(
            binding.root,
            resources.getText(R.string.no_internet_connection).toString(),
            Snackbar.LENGTH_INDEFINITE
        )
            .setAction(resources.getText(R.string.reload_button)) {
                viewModel.getCardInfo(getSearchBinString())
            }.show()
    }

    private fun showDialog(title: String? = null, message: String) {
        val builder = AlertDialog.Builder(this)
        with(builder) {
            title?.let { setTitle(title) }
            setMessage(message)
            setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            show()
        }
    }

    private fun hideKeyBoard() {
        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(binding.root.windowToken, 0)
    }

    private fun getSearchBinString() : String = binding.searchView.query.toString()

    private fun enableSearchView(isEnable: Boolean = true) {
        binding.searchView.allViews.forEach { it.isEnabled = isEnable }
    }

    private fun enableRecyclerView(isEnable: Boolean = true) {
        binding.rvBinsHistory.allViews.forEach { it.isEnabled = isEnable }
    }
}