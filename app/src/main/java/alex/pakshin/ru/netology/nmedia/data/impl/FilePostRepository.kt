package alex.pakshin.ru.netology.nmedia.data.impl

import alex.pakshin.ru.netology.nmedia.data.Post
import alex.pakshin.ru.netology.nmedia.data.PostRepository
import android.app.Application
import android.content.Context
import androidx.core.content.edit
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.RuntimeException
import kotlin.properties.Delegates

class FilePostRepository(private val application: Application) : PostRepository {

    private val gson = Gson()
    private val type = TypeToken.getParameterized(List::class.java, Post::class.java).type

    private val prefs = application.getSharedPreferences(
        "repo", Context.MODE_PRIVATE
    )
    private var nextId: Long by Delegates.observable(
        prefs.getLong(NEXT_ID_PREFS_KEY, 0L)
    ) { _, _, newValue ->
        prefs.edit { putLong(NEXT_ID_PREFS_KEY, newValue) }
    }


    override val data: MutableLiveData<List<Post>>

    private var posts
        get() = checkNotNull(data.value) {
            "Data value should not be null"
        }
        set(value) {
            application.openFileOutput(FILE_NAME, Context.MODE_PRIVATE).bufferedWriter().use {
                it.write(gson.toJson(value))
            }
            data.value = value
        }

    init {
        val postsFile = application.filesDir.resolve(FILE_NAME)
        val posts: List<Post> = if (postsFile.exists()) {
            val inputStream = application.openFileInput(FILE_NAME)
            val reader = inputStream.bufferedReader()
            reader.use {
                gson.fromJson(it, type)
            }
        } else emptyList()

        data = MutableLiveData(posts)
    }

    override fun like(postId: Long) {
        posts =
            posts.map {
                if (it.id == postId)
                    it.copy(
                        liked = !it.liked,
                        likeCount = if (!it.liked) it.likeCount + 1 else it.likeCount - 1
                    ) else it
            }
    }

    override fun share(postId: Long) {
        posts = posts.map {
            if (it.id == postId)
                it.copy(shareCount = it.shareCount + 1) else it
        }
    }

    override fun save(post: Post) {
        if (post.id == PostRepository.NEW_POST_ID) insert(post) else update(post)

    }

    private fun insert(post: Post) {
        posts = listOf(post.copy(id = ++nextId)) + posts
    }

    private fun update(post: Post) {
        posts = posts.map {
            if (it.id == post.id) post else it
        }
    }

    override fun delete(postId: Long) {
        posts =
            posts.filter { it.id != postId }
    }



    private companion object {
        const val NEXT_ID_PREFS_KEY = "nextId"
        const val POST_PREFS_KEY = "posts"
        const val FILE_NAME = "posts.json"
    }
}