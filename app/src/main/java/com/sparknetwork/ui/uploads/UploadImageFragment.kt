package com.sparknetwork.ui.uploads

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

import android.os.Bundle
import androidx.fragment.app.Fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sparknetwork.R
import com.sparknetwork.model.DataModel
import com.sparknetwork.ui.uploads.adapter.ImagesAdapter
import com.sparknetwork.viewModel.UploadImagesViewModel

import java.util.ArrayList

class UploadImageFragment : Fragment() {

    private var mViewModel: UploadImagesViewModel? = null
    private val movieList = ArrayList<DataModel>()
    private var mAdapter: ImagesAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_upload_image, container, false)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initRecyclerView()

        initViewModelObserver()
    }

    private fun initRecyclerView() {
        mAdapter = ImagesAdapter(movieList)
    }

    private fun initViewModelObserver() {
        mViewModel = ViewModelProviders.of(this).get(UploadImagesViewModel::class.java)
        mViewModel!!.init()

        mViewModel!!.images.observe(this, Observer { movieModels ->
            movieList.addAll(movieModels!!.data)
            mAdapter!!.notifyDataSetChanged()
        })
    }

    companion object {
        fun newInstance(): UploadImageFragment {
            return UploadImageFragment()
        }
    }
}