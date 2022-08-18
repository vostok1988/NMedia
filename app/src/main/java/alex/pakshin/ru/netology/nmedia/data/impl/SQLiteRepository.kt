package alex.pakshin.ru.netology.nmedia.data.impl

import alex.pakshin.ru.netology.nmedia.data.Post
import alex.pakshin.ru.netology.nmedia.data.PostRepository
import alex.pakshin.ru.netology.nmedia.db.PostDao
import androidx.lifecycle.MutableLiveData

class SQLiteRepository(
    private val dao: PostDao
) : PostRepository {

    override val data = MutableLiveData(dao.getAll())

    private val posts
        get() = checkNotNull(data.value) {
            "Data value should not be null"
        }

    override fun like(postId: Long) {
        dao.likeById(postId)
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
        dao.shareById(postId)
        data.value = posts.map {
            if (it.id == postId)
                it.copy(shareCount = it.shareCount + 1) else it
        }
    }

    override fun save(post: Post) {
        val id = post.id
        val saved = dao.save(post)
        data.value = if (id == PostRepository.NEW_POST_ID)
            listOf(saved) + posts
        else {
            posts.map {
                if (it.id == id) saved else it
            }
        }
    }


    override fun delete(postId: Long) {
        dao.removeById(postId)
        data.value =
            posts.filter { it.id != postId }
    }

}