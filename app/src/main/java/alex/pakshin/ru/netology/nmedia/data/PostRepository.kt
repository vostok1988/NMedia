package alex.pakshin.ru.netology.nmedia.data

import androidx.lifecycle.LiveData

interface PostRepository {
    val data: LiveData<List<Post>>

    fun like(postId:Long)

    fun share(postId:Long)

    fun save(post: Post)

    fun delete(postId: Long)



    companion object{
        const val NEW_POST_ID = 0L
    }
}