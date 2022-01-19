package com.mukesh.assignment_android.ui.main.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager

import com.mukesh.assignment_android.R
import com.mukesh.assignment_android.data.model.Animal
import com.mukesh.assignment_android.ui.main.adapter.AnimalListAdapter
import com.mukesh.assignment_android.ui.main.viewmodel.ListViewModel
import kotlinx.android.synthetic.main.fragment_list.*

/**
 * A simple [Fragment] subclass.
 */
class ListFragment : Fragment() {

    private lateinit var viewModel: ListViewModel
    private val listAdapter =
        AnimalListAdapter(
            arrayListOf()
        )

    private val animalListDataObserver = Observer<List<Animal>> { it ->
        it?.let {
            listAdapter.updateAnimalList(it)
            rv_animal_list.visibility = View.VISIBLE
            pb_loading_view.visibility = View.GONE
            tv_list_error.visibility = View.GONE
        }

    }

    private val loadingDataObserver = Observer<Boolean> {
        if (it) {
            pb_loading_view.visibility = View.VISIBLE
        } else {
            pb_loading_view.visibility = View.GONE
        }

    }

    private val loadingErrorObserver = Observer<Boolean> {
        if (it) {
            tv_list_error.visibility = View.VISIBLE
        } else {
            tv_list_error.visibility = View.GONE
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ListViewModel::class.java)
        viewModel.animals.observe(this, animalListDataObserver)
        viewModel.loading.observe(this, loadingDataObserver)
        viewModel.loadError.observe(this, loadingErrorObserver)
        viewModel.refresh()

        rv_animal_list.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = listAdapter
        }

        sr_layout_swipe_animal.setOnRefreshListener {
            rv_animal_list.visibility = View.GONE
            tv_list_error.visibility = View.GONE
            pb_loading_view.visibility = View.VISIBLE
            viewModel.hardRefresh()
            sr_layout_swipe_animal.isRefreshing = false
        }
    }


}
