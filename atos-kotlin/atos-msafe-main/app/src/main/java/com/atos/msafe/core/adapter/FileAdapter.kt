package com.atos.msafe.core.adapter

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.atos.msafe.R
import com.atos.msafe.databinding.ItemFileBinding
import com.atos.msafe.model.ItemFile
import com.bumptech.glide.Glide

class FileAdapter(private val itemsAdapter: List<ItemFile>, val itemClickListener: ItemClickListener, val itemOptionClickListener: ItemOptionClickListener) : RecyclerView.Adapter<FileAdapter.ViewHolder>() {

    private lateinit var context: Context

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding = ItemFileBinding.bind(itemView)

        fun bind(itemAdapter: ItemFile) {
            binding.tvNameFile.text = itemAdapter.name
            binding.tvDateFile.text = itemAdapter.updatedTimeMillis
            if (itemAdapter.imagePlaceholder != null) {
                binding.materialCardView.cardElevation = 1f
                try {
                    Glide.with(context).load(itemAdapter.getThumbnailFromUrl()).into(binding.ivThumbnailFile)
                } catch (e: Exception) {
                    Log.e(TAG, e.message.toString())
                }

            } else {
                binding.materialCardView.cardElevation = 0f
                when (itemAdapter.fileType) {
                    ".pdf" -> binding.ivThumbnailFile.setImageResource(R.drawable.ic_baseline_filetype_pdf)
                    ".zip" -> binding.ivThumbnailFile.setImageResource(R.drawable.ic_baseline_filetype_zip)
                    ".mp4" -> binding.ivThumbnailFile.setImageResource(R.drawable.ic_baseline_filetype_movie)
                    else -> binding.ivThumbnailFile.setImageResource(R.drawable.ic_baseline_insert_drive_file)
                }
            }

            if (itemAdapter.favourite){
                binding.ivFavouriteFile.visibility = View.VISIBLE
            } else {
                binding.ivFavouriteFile.visibility = View.INVISIBLE
            }

            itemView.setOnClickListener {itemClickListener.onClickItem(itemAdapter)}
            binding.imageButton.setOnClickListener {itemOptionClickListener.onClickOption(itemAdapter, binding.imageButton)}
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_file, parent, false)
        )
    }

    /**
     * Returns the size of the list
     */
    override fun getItemCount(): Int = itemsAdapter.size

    /**
     * Called by RecyclerView to display the data at the specified position.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemsAdapter[position])
    }
}

interface ItemClickListener{
    fun onClickItem(itemFile: ItemFile)
}

interface ItemOptionClickListener{
    fun onClickOption(itemFile: ItemFile, itemView: View)
}