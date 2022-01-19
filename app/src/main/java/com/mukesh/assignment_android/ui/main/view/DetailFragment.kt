package com.mukesh.assignment_android.ui.main.view

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.mukesh.assignment_android.R
import com.mukesh.assignment_android.data.model.Animal
import com.mukesh.assignment_android.databinding.FragmentDetailBinding

/**
 * A simple [Fragment] subclass.
 */
class DetailFragment : Fragment() {
    private var animal: Animal? = null
    //
    private lateinit var detailDataBinding: FragmentDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        detailDataBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)
        return detailDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let { bundle ->
            animal = DetailFragmentArgs.fromBundle(bundle).specificAnimalData
        }

        animal?.imageUrl?.let {
            setBackgroundColor(it)
        }

        detailDataBinding.animalData = animal

    }

    private fun setBackgroundColor(url: String?) {
        Glide.with(this).asBitmap().load(url)
            .into(object : CustomTarget<Bitmap>() {
                override fun onLoadCleared(placeholder: Drawable?) {}

                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    Palette.from(resource)
                        .generate { palette ->
                            val color = palette?.lightMutedSwatch?.rgb ?: 0
                            detailDataBinding.backgroundColor = color
                        }
                }
            })
    }

}
