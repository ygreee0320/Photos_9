package com.example.prography

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.prography.databinding.FragmentViewPagerBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ViewPagerFragment : Fragment() {
    private lateinit var binding: FragmentViewPagerBinding
    private var currentPosition: Int = -1 // 추가: 현재 뷰페이저의 위치를 저장하는 변수


    companion object {
        fun newInstance(photo: UnsplashPhoto): ViewPagerFragment {
            val fragment = ViewPagerFragment()
            val args = Bundle()
            args.putString("id", photo.id)
            args.putString("urls", photo.urls.regular)

            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentViewPagerBinding.inflate(inflater, container, false)

        val id = arguments?.getString("id")
        val urls = arguments?.getString("urls")

        Glide.with(this)
            .load(urls)
            .into(binding.mainImg)

        binding.infoBtn.setOnClickListener {
            val intent = Intent(activity, PhotoDetailActivity::class.java)
            intent.putExtra("photoId", id)
            startActivity(intent)
        }

        binding.saveBtn.setOnClickListener {
            saveToDatabase(id!!, urls!!)
            Log.d("my log", "북마크 저장 완료")

            // 추가: 현재 위치를 기억하고, 다음 위치로 이동
            currentPosition = (parentFragment as? RandomFragment)?.viewPager?.currentItem ?: -1
            (parentFragment as? RandomFragment)?.viewPager?.setCurrentItem(currentPosition + 1, true)
        }

        return binding.root
    }

    private fun saveToDatabase(id: String, imageUrl: String) {
        val bookmarkEntity = BookMarkPhotoEntity(id, imageUrl)

        GlobalScope.launch(Dispatchers.IO) {
            AppDatabase.getDatabase(requireContext()).bookMarkPhotoDao().insertBookmark(bookmarkEntity)
        }
    }

}