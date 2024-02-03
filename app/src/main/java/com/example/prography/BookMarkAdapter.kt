package com.example.prography

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class BookMarkAdapter(private var photos: List<BookMarkPhotoEntity>): RecyclerView.Adapter<BookMarkAdapter.BookMarkViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookMarkViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.bookmark_photo, parent, false)
        return BookMarkViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookMarkViewHolder, position: Int) {
        val photo = photos[position]
        holder.bind(photo.imageUrl)
    }

    override fun getItemCount(): Int {
        return photos.size
    }

    fun updateData(newPhotos: List<BookMarkPhotoEntity>) {
        photos = newPhotos
        notifyDataSetChanged()
    }

    inner class BookMarkViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.main_photo)

        init {
            itemView.setOnClickListener {
                val clickedPhoto = photos[adapterPosition]
                val photoId = clickedPhoto.photoId // 클릭된 사진의 Id를 가져옴
                val intent = Intent(itemView.context, PhotoDetailActivity::class.java)

                intent.putExtra("photoId", photoId)
                itemView.context.startActivity(intent)
                //itemClickListener.onItemClicked(photoId)
            }
        }

        fun bind(photo: String){
            Glide.with(itemView.context)
                .load(photo)
                .into(imageView)
        }
    }
}