package com.example.prography

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.prography.databinding.FragmentHomeBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var bookmarkRecyclerView: RecyclerView
    private lateinit var recentRecyclerView: RecyclerView
    private lateinit var photoAdapter: PhotoAdapter
    private lateinit var bookmarkAdapter: BookMarkAdapter

    private var recentPhotos: MutableList<UnsplashPhoto> = mutableListOf()
    private lateinit var bookmarkDatabase: AppDatabase

    private var isLoading = false
    private var currentPage = 1 // 초기 페이지

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bookmarkRecyclerView = binding.bookmarkList
        recentRecyclerView = binding.recentList

        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        bookmarkRecyclerView.layoutManager = layoutManager

        bookmarkAdapter = BookMarkAdapter(emptyList())  // 초기에 빈 목록으로 어댑터 설정
        bookmarkRecyclerView.adapter = bookmarkAdapter

        val gridLayoutManager = GridLayoutManager(requireContext(), 2)
        recentRecyclerView.layoutManager = gridLayoutManager

        initRecentRecyclerView()

        // 초기화할 때 데이터베이스 인스턴스 생성
        bookmarkDatabase = AppDatabase.getDatabase(requireContext())

        // 북마크 이미지 로드 및 어댑터에 설정
        loadBookmarks()
    }

    private fun initRecentRecyclerView() {
        // 스크롤 감지 및 추가 데이터 로드
        recentRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as GridLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (!isLoading) {
                    if (layoutManager.findLastCompletelyVisibleItemPosition() == totalItemCount -1) {
                        // 스크롤이 끝에 도달하면 추가 데이터 로드
                        loadRecentPhotos()
                        Log.d("my log", "스크롤 작동")
                    } else {
                        Log.d("my log", "스크롤X")
                    }
                }

//                if (!isLoading) {
//                    if (visibleItemCount + firstVisibleItemPosition >= totalItemCount
//                        && firstVisibleItemPosition >= 0
//                        && totalItemCount >= PAGE_SIZE
//                    ) {
//                        // 스크롤이 끝에 도달하면 추가 데이터 로드
//                        loadRecentPhotos()
//                        Log.d("my log", "스크롤")
//                    }
//                }
            }
        })

        // 초기 데이터 로딩
        loadRecentPhotos()
    }

    private fun loadRecentPhotos() {
        GlobalScope.launch(Dispatchers.Main) {
            isLoading = true
            val photos = withContext(Dispatchers.IO) {
                RetrofitInstance.unsplashApi.getRandomPhotos(BuildConfig.CLIENT_ID, currentPage)
            }

            if (photos.isNotEmpty()) {
                // 기존 데이터에 추가
                recentPhotos.addAll(photos)

                if (currentPage == 1) {
                    // 첫 번째 페이지일 경우에만 어댑터를 설정
                    photoAdapter = PhotoAdapter(recentPhotos)
                    recentRecyclerView.adapter = photoAdapter
                    Log.d("my log", "초기 페이지")
                } else {
                    // 다음 페이지일 경우에는 기존 어댑터에 데이터를 추가
                    photoAdapter.addPhotos(photos)
                    Log.d("my log", "다음 페이지")
                }

                // 다음 페이지를 로드할 수 있도록 현재 페이지 증가
                currentPage++
            }

            isLoading = false
        }
    }

    private fun loadBookmarks() {
        GlobalScope.launch(Dispatchers.Main) {
            // 데이터베이스에서 북마크된 이미지 불러오기
            val bookmarkPhotos = withContext(Dispatchers.IO) {
                bookmarkDatabase.bookMarkPhotoDao().getAllBookmarks()
            }

            // 북마크된 이미지가 있으면 어댑터에 설정
            if (bookmarkPhotos.isNotEmpty()) {
                bookmarkAdapter.updateData(bookmarkPhotos.map { it.imageUrl })
            } else {
                binding.bookmark.visibility = View.GONE
            }
        }
    }

    // 북마크 추가 예시 (이미지 URL이 있다고 가정)
    private fun addBookmark(photoId: String, imageUrl: String) {
        GlobalScope.launch(Dispatchers.IO) {
            // 데이터베이스에 북마크 추가
            bookmarkDatabase.bookMarkPhotoDao().insertBookmark(BookMarkPhotoEntity(photoId = photoId, imageUrl = imageUrl))
        }
    }

    companion object {
        private const val PAGE_SIZE = 10
    }

}