package ru.netology.nmedia

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import java.text.SimpleDateFormat
import java.util.*

private val empty = Post(
    id = 0,
    author = "",
    content = "",
    published = "",
    likeCount = 0,
    repostCount = 0,
    watchesCount = 0,
    likedByMe = false,
    video = ""
)

class PostViewModel(application: Application) : AndroidViewModel(application) {
    //private val repository: PostRepository = PostRepositoryInMemoryImpl()
    //private val repository: PostRepository = PostRepositoryInFileImpl(application)
    //private val repository: PostRepository = PostRepositorySQLiteImpl(
    private val repository: PostRepository = PostRepositoryImpl(
        AppDb.getInstance(application).postDao()
    )
    val data = repository.getAll()
    var draft: String = ""
    private val edited = MutableLiveData(empty)
    fun likeDislike(id: Int) = repository.likeDislike(id)
    fun repost(id: Int) = repository.repost(id)
    fun removeById(id: Int) = repository.removeById(id)
    fun showPost(id: Int): Post = repository.showPost(id)

    fun save() {
        edited.value?.let {
            val dateFormatter = SimpleDateFormat("dd MMMM yyyy hh:mm", Locale.getDefault())
            val date = dateFormatter.format(Date())
            val post = it.copy(published = date, author = getCurrentUser())
            repository.save(post)
        }
        edited.value = empty
        draft = ""
    }

    fun saveDraft(string: String) {
        draft = string
    }


    fun changeContent(content: String) {
        edited.value?.let {
            val text = content.trim()
            if (it.content == text) {
                return
            }
            edited.value = it.copy(content = text)
        }
    }

    fun edit(post: Post) {
        edited.value = post
    }
}