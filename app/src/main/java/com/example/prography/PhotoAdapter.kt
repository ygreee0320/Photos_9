package com.example.prography

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class PhotoAdapter(private val photos: MutableList<UnsplashPhoto>) :
    RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder>() {

    private val heights = listOf(400, 700, 500, 450, 300, 420, 500, 600, 400, 350)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recent_photo, parent, false)
        return PhotoViewHolder(view)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        //val photo = photos[position % photos.size]
        val photo = photos[position]
        holder.bind(photo)

        // 높이를 주기적으로 반복하여 설정
//        val height = heights[position % heights.size]
//
//        // 높이를 동적으로 설정
//        val layoutParams = holder.imageView.layoutParams
//        layoutParams.height = height
//        holder.imageView.layoutParams = layoutParams

    }

    override fun getItemCount(): Int {
        return photos.size
    }

    fun addPhotos(newPhotos: List<UnsplashPhoto>) {
        photos.addAll(newPhotos)
        notifyDataSetChanged()
    }

    inner class PhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.main_photo)
        val imageLayout: FrameLayout = itemView.findViewById(R.id.photo_layout)

        init {
            itemView.setOnClickListener {
                val clickedPhoto = photos[adapterPosition]
                val photoId = clickedPhoto.id // 클릭된 사진의 Id를 가져옴
                val intent = Intent(itemView.context, PhotoDetailActivity::class.java)

                intent.putExtra("photoId", photoId)
                itemView.context.startActivity(intent)
                //itemClickListener.onItemClicked(photoId)
            }
        }

        fun bind(photo: UnsplashPhoto){
            Glide.with(itemView.context)
                .load(photo.urls.regular)
                .into(imageView)
        }
    }
}