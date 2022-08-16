package alex.pakshin.ru.netology.nmedia.data.impl

import alex.pakshin.ru.netology.nmedia.data.Post
import alex.pakshin.ru.netology.nmedia.data.PostRepository
import androidx.lifecycle.MutableLiveData
import java.lang.RuntimeException

class InMemoryPostRepository : PostRepository {

    private var nextId = GENERATED_POST_AMOUNT.toLong()

    override val data =
        MutableLiveData(
            List(GENERATED_POST_AMOUNT) {
                Post(
                    it + 1L,
                    "Author",
                    "Very important message $it",
                    "18.05.22",
                    false,
                    999,
                    3

                )
            })

    private val posts
        get() = checkNotNull(data.value) {
            "Data value should not be null"
        }

    override fun like(postId: Long) {
        data.value =
            posts.map {
                if (it.id == postId)
                    it.copy(
                        liked = !it.liked,
                        likeCount = if (!it.liked) it.likeCount + 1 else it.likeCount - 1
                    ) else it
            }
    }

    override fun share(postId: Long) {
        data.value = posts.map {
            if (it.id == postId)
                it.copy(shareCount = it.shareCount + 1) else it
        }
    }

    override fun save(post: Post) {
        if (post.id == PostRepository.NEW_POST_ID) insert(post) else update(post)

    }

    private fun insert(post: Post) {
        data.value = listOf(post.copy(id = ++nextId)) + posts
    }

    private fun update(post: Post) {
        data.value = posts.map {
            if (it.id == post.id) post else it
        }
    }

    override fun delete(postId: Long) {
        data.value =
            posts.filter { it.id != postId }
    }

    private companion object {
        const val GENERATED_POST_AMOUNT = 10
    }
}