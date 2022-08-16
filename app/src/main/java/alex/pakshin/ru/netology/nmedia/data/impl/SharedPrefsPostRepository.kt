package alex.pakshin.ru.netology.nmedia.data.impl

import alex.pakshin.ru.netology.nmedia.data.Post
import alex.pakshin.ru.netology.nmedia.data.PostRepository
import android.app.Application
import android.content.Context
import androidx.core.content.edit
import androidx.lifecycle.MutableLiveData
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.properties.Delegates

class SharedPrefsPostRepository(application: Application) : PostRepository {

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
            prefs.edit {
                val serializedPost = Json.encodeToString(value)
                putString(POST_PREFS_KEY, serializedPost)
            }
            data.value = value
        }

    init {
        val serializedPosts = prefs.getString(POST_PREFS_KEY, null)
        val posts: List<Post> = if (serializedPosts != null) {
            Json.decodeFromString(serializedPosts)
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
    }
}