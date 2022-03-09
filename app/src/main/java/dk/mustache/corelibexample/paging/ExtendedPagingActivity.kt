package dk.mustache.corelibexample.paging

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dk.mustache.corelibexample.R
import dk.mustache.corelibexample.paging.ExtendedPagingViewModel.Actions.*

class ExtendedPagingActivity : AppCompatActivity() {
    private val viewModel: ExtendedPagingViewModel by viewModels()
    lateinit var adapter: ExtendedPagingAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paging)
    }

    override fun onResume() {
        super.onResume()
        initializeAdapter()
        viewModel.actions.observe(this, ::onAction)
        viewModel.startLoading()
    }

    private fun initializeAdapter() {
        adapter = ExtendedPagingAdapter()
        val recyclerView = findViewById<RecyclerView>(R.id.pokemon_recycler)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        viewModel.hookupLoader(adapter::createLoadingItems, adapter::replaceLoadingItems)
    }

    private fun onAction(action: ExtendedPagingViewModel.Actions) {
        when (action) {
            IsLoading -> findViewById<View>(R.id.poke_progress).visibility = View.VISIBLE
            FinishedLoading -> findViewById<View>(R.id.poke_progress).visibility = View.GONE
            is OnError -> {
                val errorText = action.e.localizedMessage
                Toast.makeText(this, "Error: $errorText", Toast.LENGTH_SHORT).show()
            }
        }
    }
}