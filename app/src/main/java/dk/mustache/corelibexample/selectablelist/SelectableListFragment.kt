package dk.mustache.corelibexample.selectablelist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import dk.mustache.corelibexample.databinding.FragmentSelectableListBinding

class SelectableListFragment : Fragment() {
    private lateinit var binding: FragmentSelectableListBinding

    private lateinit var adapter: SelectableExampleAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSelectableListBinding.inflate(inflater)
        return binding.root
    }

    override fun onResume() {
        super.onResume()

        val items = listOf(
            SelectableExampleItem("1", "Item 1"),
            SelectableExampleItem("2", "Item 2"),
            SelectableExampleItem("3", "Item 3"),
            SelectableExampleItem("4", "Item 4"),
            SelectableExampleItem("5", "Item 5"),
            SelectableExampleItem("6", "Item 6"),
            SelectableExampleItem("7", "Item 7"),
            SelectableExampleItem("8", "Item 8"),
            SelectableExampleItem("9", "Item 9"),
            SelectableExampleItem("10", "Item 10")
        )

        adapter = SelectableExampleAdapter(items) { item, selected ->
            Toast.makeText(
                requireContext(),
                "${item.selectableText}: ${item.selected}",
                Toast.LENGTH_LONG
            ).show()
        }

        binding.demoList.layoutManager = LinearLayoutManager(requireActivity())
        binding.demoList.adapter = adapter
    }
}