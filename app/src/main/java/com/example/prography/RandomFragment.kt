package com.example.prography

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.prography.databinding.FragmentRandomBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RandomFragment : Fragment() {
    private lateinit var binding: FragmentRandomBinding
    lateinit var viewPager: ViewPager2
    private lateinit var childFragMang: FragmentManager
    private val randomPhotos = mutableListOf<UnsplashPhoto>()
    private lateinit var adapter: PhotoCardAdapter

    class PhotoCardAdapter(
        fragmentManager: FragmentManager, lifecycle: Lifecycle, private val photos: List<UnsplashPhoto>
    ) : FragmentStateAdapter(fragmentManager, lifecycle) {

        override fun getItemCount(): Int {
            return photos.size
        }

        override fun createFragment(position: Int): Fragment {
            return ViewPagerFragment.newInstance(photos[position])
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRandomBinding.inflate(inflater, container, false)

        viewPager = binding.viewPager

        childFragMang = childFragmentManager

        loadViewPager()

        return binding.root
    }

    private fun loadViewPager() {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                // 랜덤 이미지 10개 불러오기
                for (i in 1..5) {
                    getRandomPhoto()
                }

                // 어댑터 초기화
                adapter = PhotoCardAdapter(childFragmentManager, lifecycle, randomPhotos)
                viewPager.adapter = adapter

            } catch (e: Exception) {
                // 오류 처리
                e.printStackTrace()
            }
        }
    }

    private suspend fun getRandomPhoto() {
        val randomPhoto = withContext(Dispatchers.IO) {
            // Unsplash API에서 랜덤 이미지 가져오기
            RetrofitInstance.unsplashApi.getRandomPhotos2(BuildConfig.CLIENT_ID)
        }
        randomPhotos.add(randomPhoto)
    }
}