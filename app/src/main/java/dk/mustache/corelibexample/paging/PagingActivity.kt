package dk.mustache.corelibexample.paging

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dk.mustache.corelibexample.R

class PagingActivity : AppCompatActivity() {
    private val viewModel: PagingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paging)
        initializeAdapter()
    }

    private fun initializeAdapter() {
        val recyclerView = findViewById<RecyclerView>(R.id.airlines_recycler)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = viewModel.adapter
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadData()
    }
}