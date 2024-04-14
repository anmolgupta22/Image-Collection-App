package com.example.imagecollectionapp.adapter

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.imagecollectionapp.databinding.ImageItemsBinding
import com.example.imagecollectionapp.model.Urls
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

// RecyclerView Adapter for displaying images fetched from URLs
class ImageAdapter : RecyclerView.Adapter<ImageAdapter.ViewHolder>() {

    private var items: MutableList<Urls> = mutableListOf()

    // Method to set the list of image URLs and trigger UI update
    @SuppressLint("NotifyDataSetChanged")
    fun setItems(imageUrls: MutableList<Urls>) {
        items.addAll(imageUrls)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ImageItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    class ViewHolder(private val binding: ImageItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(url: Urls) {
            // Use CoroutineScope to load image asynchronously and set it to ImageView
            CoroutineScope(Dispatchers.Main).launch {
                binding.image.setImageBitmap(loadImageFromUrl(url.regular))
            }
        }

        // Function to load image from a URL asynchronously and return as Bitmap
        private suspend fun loadImageFromUrl(imageUrl: String?): Bitmap {
            return withContext(Dispatchers.IO) {
                val url = URL(imageUrl)
                val connection = url.openConnection() as HttpURLConnection
                connection.connect()
                val inputStream: InputStream = connection.inputStream
                val bitmap = BitmapFactory.decodeStream(inputStream)
                inputStream.close()
                bitmap ?: throw IllegalStateException("Failed to load bitmap from URL: $imageUrl")
            }
        }
    }
}
