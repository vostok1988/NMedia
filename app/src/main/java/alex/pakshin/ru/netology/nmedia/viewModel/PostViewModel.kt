package alex.pakshin.ru.netology.nmedia.viewModel

import alex.pakshin.ru.netology.nmedia.adapter.PostInteractionListener
import alex.pakshin.ru.netology.nmedia.data.Post
import alex.pakshin.ru.netology.nmedia.data.PostRepository
import alex.pakshin.ru.netology.nmedia.data.impl.SQLiteRepository
import alex.pakshin.ru.netology.nmedia.db.AppDb
import alex.pakshin.ru.netology.nmedia.util.SingleLiveEvent
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

class PostViewModel(
    application: Application
) : AndroidViewModel(application), PostInteractionListener {
    private val repository: PostRepository = SQLiteRepository(
        dao = AppDb.getInstance(
            context = application
        ).postDao
    )

    val data by repository::data

    val sharePostContent = SingleLiveEvent<String>()

    val videoUrl = SingleLiveEvent<String?>()

    val navigatePostContentScreenEvent = SingleLiveEvent<String>()

    val navigatePostDetails = SingleLiveEvent<Long>()

    private var currentPost : Post? = null

    fun onSaveButtonClicked(content: String) {
        if (content.isBlank()) return

        val post = currentPost?.copy(
            content = content
        ) ?: Post(
            id = PostRepository.NEW_POST_ID,
            author = "me",
            content = content,
            published = "today",
            url = "https://www.youtube.com/watch?v=5qap5aO4i9A"
        )
        repository.save(post)
        currentPost = null
    }

    //region PostInteractionListener
    override fun onLikeClicked(post: Post) = repository.like(post.id)

    override fun onShareClicked(post: Post) {
        repository.share(post.id)
        sharePostContent.value = post.content
    }

    override fun onRemoveClicked(post: Post) = repository.delete(post.id)

    override fun onEditClicked(post: Post) {
        currentPost = post
        navigatePostContentScreenEvent.value = post.content
    }

    override fun onPlayClicked(post: Post) {
        videoUrl.value = post.url
    }

    override fun onPostClicked(post: Post) {
        navigatePostDetails.value = post.id
    }

    //endregion

    fun onAddButtonClicked() {
        navigatePostContentScreenEvent.call()
    }
}