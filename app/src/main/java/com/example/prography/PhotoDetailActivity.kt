package com.example.prography

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.prography.databinding.ActivityPhotoDetailBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PhotoDetailActivity : AppCompatActivity() {
    lateinit var binding: ActivityPhotoDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhotoDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val photoId = intent.getStringExtra("photoId")

        GlobalScope.launch(Dispatchers.Main) {
            try {
                val photo = withContext(Dispatchers.IO) {
                    RetrofitInstance.unsplashApi.getDetailPhoto(photoId!!, BuildConfig.CLIENT_ID)
                }

                // 세부 정보에서 이미지 URL 가져오기
                val imageUrl = photo.urls.regular

                // 이미지를 가져와서 ImageView에 설정
                Glide.with(this@PhotoDetailActivity)
                    .load(imageUrl)
                    .into(binding.image) // yourImageView는 이미지를 표시할 ImageView의 참조입니다.
            } catch (e: Exception) {
                // 오류 처리
                e.printStackTrace()
            }
        }

        // X버튼 클릭 시 상세 화면 종료
        binding.closeBtn.setOnClickListener {
            finish()
        }
    }
}