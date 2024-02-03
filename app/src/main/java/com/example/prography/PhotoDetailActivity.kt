package com.example.prography

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bumptech.glide.Glide
import com.example.prography.databinding.ActivityPhotoDetailBinding
import kotlinx.coroutines.*

class PhotoDetailActivity : AppCompatActivity() {
    lateinit var binding: ActivityPhotoDetailBinding
    private var photoId: String ?= null
    private var imageUrl: String ?= null
    private var isBookmarked: Boolean ?= false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhotoDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        photoId = intent.getStringExtra("photoId")

        GlobalScope.launch(Dispatchers.Main) {
            try {
                val photo = withContext(Dispatchers.IO) {
                    RetrofitInstance.unsplashApi.getDetailPhoto(photoId!!, BuildConfig.CLIENT_ID)
                }

                // 세부 정보에서 이미지 URL 가져오기
                imageUrl = photo.urls.regular

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

        GlobalScope.launch(Dispatchers.IO) {
            // 해당 id의 북마크가 있는지 확인
            val bookmarkDao = AppDatabase.getDatabase(applicationContext).bookMarkPhotoDao()
            val bookmark = bookmarkDao.getBookmarkById(photoId!!)

            // 이미 북마크되어 있으면 isBookmarked를 true로 설정
            isBookmarked = (bookmark != null)
        }

        // 북마크 저장 버튼
        binding.saveBtn.setOnClickListener {
//            saveToDatabase(photoId!!, imageUrl!!)
//            Log.d("my log", "북마크 저장 완료")

            // 이미 북마크되어 있는지 확인
            //val isBookmarked = checkBookmarkStatus(photoId!!)
            Log.d("my log", "북마크 여부"+isBookmarked)

            if (isBookmarked!!) {
                // 이미 북마크되어 있으면 북마크 제거
                removeFromDatabase(photoId!!)
                // 버튼 이미지 변경
                binding.saveBtn.setImageResource(R.drawable.bookmark)
                Log.d("my log", "북마크 제거 완료")
                isBookmarked = false
            } else {
                // 북마크되어 있지 않으면 북마크 추가
                saveToDatabase(photoId!!, imageUrl!!)
                // 버튼 이미지 변경
                binding.saveBtn.setImageResource(R.drawable.bookmark_done)
                Log.d("my log", "북마크 저장 완료")
                isBookmarked = true
            }
        }

        changeBookmarkImg(photoId!!)
    }

    private fun saveToDatabase(id: String, imageUrl: String) {
        val bookmarkEntity = BookMarkPhotoEntity(id, imageUrl)

        GlobalScope.launch(Dispatchers.IO) {
            AppDatabase.getDatabase(applicationContext).bookMarkPhotoDao().insertBookmark(bookmarkEntity)
        }
    }

    // 이미 북마크된 상태인지 확인하고 버튼 이미지 설정
    private fun checkBookmarkStatus(id: String): Boolean {
        var isBookmarked = false

        GlobalScope.launch(Dispatchers.IO) {
            // 해당 id의 북마크가 있는지 확인
            val bookmarkDao = AppDatabase.getDatabase(applicationContext).bookMarkPhotoDao()
            val bookmark = bookmarkDao.getBookmarkById(id)

            // 이미 북마크되어 있으면 isBookmarked를 true로 설정
            isBookmarked = (bookmark != null)

            // UI 쓰레드에서 버튼 이미지 설정
            withContext(Dispatchers.Main) {
                if (isBookmarked) {
                    binding.saveBtn.setImageResource(R.drawable.bookmark_done)
                    Log.d("my log", "북마크 되어있음")
                } else {
                    binding.saveBtn.setImageResource(R.drawable.bookmark)
                    Log.d("my log", "북마크 안되어있음")
                }
            }
        }
//        val isBookmarkedDeferred = GlobalScope.async(Dispatchers.IO) {
//            // 해당 id의 북마크가 있는지 확인
//            val bookmarkDao = AppDatabase.getDatabase(applicationContext).bookMarkPhotoDao()
//            val bookmark = bookmarkDao.getBookmarkById(id)
//
//            // 이미 북마크되어 있으면 true, 아니면 false 반환
//            bookmark != null
//        }
//
//        val isBookmarked = isBookmarkedDeferred.await()

        return isBookmarked
    }

    private fun changeBookmarkImg(id: String) {

        GlobalScope.launch(Dispatchers.IO) {
            // 해당 id의 북마크가 있는지 확인
            val bookmarkDao = AppDatabase.getDatabase(applicationContext).bookMarkPhotoDao()
            val bookmark = bookmarkDao.getBookmarkById(id)

            // UI 쓰레드에서 버튼 이미지 설정
            withContext(Dispatchers.Main) {
                if (bookmark != null) {
                    binding.saveBtn.setImageResource(R.drawable.bookmark_done)
                    Log.d("my log", "북마크 되어있음")
                } else {
                    binding.saveBtn.setImageResource(R.drawable.bookmark)
                    Log.d("my log", "북마크 안되어있음")
                }
            }
        }
    }

    // 북마크 제거
    private fun removeFromDatabase(id: String) {
        GlobalScope.launch(Dispatchers.IO) {
            AppDatabase.getDatabase(applicationContext).bookMarkPhotoDao().deleteBookmarkById(id)
        }
    }
}