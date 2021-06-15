package com.kolo.redditclone.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kolo.redditclone.R
import com.kolo.redditclone.databinding.ImagePostItemBinding
import com.kolo.redditclone.databinding.LoadingItemBinding
import com.kolo.redditclone.databinding.TextPostItemBinding
import com.kolo.redditclone.models.MainModel
import com.kolo.redditclone.utils.AppUtils.getTimeAgo
import com.kolo.redditclone.utils.AppUtils.prettyCount


class PostAdapter(val context: Context, private val mainModel: List<MainModel>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        const val TYPE_TEXT = 0
        const val TYPE_IMAGE = 1
        const val TYPE_LOADING = 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_TEXT -> TextViewHolder(
                TextPostItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            TYPE_IMAGE -> ImageViewHolder(
                ImagePostItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            else -> LoadingViewHolder(
                LoadingItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (mainModel[position].type) {
            TYPE_TEXT -> (holder as TextViewHolder).bind(position)
            TYPE_IMAGE -> (holder as ImageViewHolder).bind(position)
            TYPE_LOADING -> (holder as LoadingViewHolder).bind(position)
        }
    }

    override fun getItemCount(): Int {
        return mainModel.size
    }

    override fun getItemViewType(position: Int): Int {
        return mainModel[position].type
    }

    inner class TextViewHolder(private val binding: TextPostItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(position: Int) {
            binding.txtName.text = mainModel[position].name
            binding.txtDetails.text =
                "Posted by ${mainModel[position].author} • ${getTimeAgo(mainModel[position].timestamp)}"
            binding.selfText.text = mainModel[position].title

            binding.txtThumbUp.text = prettyCount(mainModel[position].likes.toInt())
            binding.txtComment.text = prettyCount(mainModel[position].comments.toInt())


            binding.imgMore.setOnClickListener {
                val popupMenu = PopupMenu(it.rootView.context,it)
                val inflater: MenuInflater = popupMenu.menuInflater
                inflater.inflate(R.menu.more_item, popupMenu.menu)
                popupMenu.show()
            }

        }
    }

    inner class ImageViewHolder(private val binding: ImagePostItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(position: Int) {
            binding.txtName.text = mainModel[position].name
            binding.txtDetails.text =
                "Posted by ${mainModel[position].author} • ${getTimeAgo(mainModel[position].timestamp)}"
            binding.selfText.text = mainModel[position].title

            binding.txtThumbUp.text = prettyCount(mainModel[position].likes.toInt())
            binding.txtComment.text = prettyCount(mainModel[position].comments.toInt())

            Glide.with(context).load(mainModel[position].thumbnail)
                .into(binding.imgPost)

            binding.imgMore.setOnClickListener {
                val popupMenu = PopupMenu(it.rootView.context,it)
                val inflater: MenuInflater = popupMenu.menuInflater
                inflater.inflate(R.menu.more_item, popupMenu.menu)
                popupMenu.show()
            }
        }
    }

    inner class LoadingViewHolder(private val binding: LoadingItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {

        }
    }
}