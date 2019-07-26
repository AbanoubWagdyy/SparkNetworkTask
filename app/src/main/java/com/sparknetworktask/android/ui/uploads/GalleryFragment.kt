package com.sparknetworktask.android.ui.uploads

import androidx.lifecycle.ViewModelProviders

import android.os.Bundle
import androidx.fragment.app.Fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.sparknetworktask.android.model.ImageModel
import com.sparknetworktask.android.ui.uploads.adapter.ImagesAdapter
import com.sparknetworktask.android.viewModel.UploadImagesViewModel
import kotlinx.android.synthetic.main.fragment_upload_image.*

import java.util.ArrayList
import androidx.recyclerview.widget.GridLayoutManager
import com.sparknetworktask.android.R


class GalleryFragment : Fragment() {

    private lateinit var mViewModel: UploadImagesViewModel

    private val imagesList = ArrayList<ImageModel>()
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
        initViewModelObserver()
        initRecyclerView()
        getImages()
    }

    private fun getImages() {
        val observer = Observer<List<ImageModel>> { images ->
            progressBar.visibility = View.GONE
            if (images != null) {
                if (images.isNotEmpty()) {
                    imagesList.addAll(images)
                    mAdapter!!.notifyDataSetChanged()
                } else {
                    relativeNoItems.visibility = View.VISIBLE
                }
            } else {
                relativeNoItems.visibility = View.VISIBLE
            }
        }

        mViewModel.getImages().observe(this, observer)
    }

    private fun initRecyclerView() {
        recyclerImages.layoutManager = GridLayoutManager(activity, 3)
        recyclerImages.setHasFixedSize(true)
        mAdapter = ImagesAdapter(context, imagesList)
        recyclerImages.adapter = mAdapter
    }

    private fun initViewModelObserver() {
        mViewModel = ViewModelProviders.of(this).get(UploadImagesViewModel::class.java)
    }

    companion object {
        fun newInstance(): GalleryFragment {
            return GalleryFragment()
        }
    }
}