package ru.netology.nmedia

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.*

class PostRepositoryInFileImpl(private val context: Context) : PostRepository {

    private val gson = Gson()
    private val type = TypeToken.getParameterized(List::class.java, Post::class.java).type
    private val fileName = "posts.json"
    private var nextId = 1
    private var posts = emptyList<Post>()
    private val data = MutableLiveData(posts)

    init {
        val file = context.filesDir.resolve(fileName)
        if (file.exists()) {
            context.openFileInput(fileName).bufferedReader().use {
                try {
                    posts = gson.fromJson(it, type)
                    data.value = posts
                    nextId = posts[0].id + 1
                } catch (exception: JsonSyntaxException) {
                    Toast.makeText(
                        context,
                        "Open data from file failed. Create new empty data file",
                        Toast.LENGTH_SHORT
                    ).show()
                    sync()
                }
            }
        } else {
            sync()
        }
    }

    private fun sync() {
        context.openFileOutput(fileName, Context.MODE_PRIVATE).bufferedWriter().use {
            it.write(gson.toJson(posts))
        }
    }

    override fun getAll(): LiveData<List<Post>> = data

    override fun likeDislike(id: Int) {

        posts = posts.map {
            if (it.id != id) {
                it
            } else {
                if (it.likedByMe) {
                    it.copy(likedByMe = !it.likedByMe, likeCount = it.likeCount - 1)
                } else {
                    it.copy(likedByMe = !it.likedByMe, likeCount = it.likeCount + 1)
                }
            }
        }
        data.value = posts
        sync()
    }

    override fun repost(id: Int) {
        posts = posts.map {
            if (it.id != id) it else it.copy(repostCount = it.repostCount + 1)
        }
        data.value = posts
        sync()
    }

    override fun removeById(id: Int) {
        posts = posts.filter { it.id != id }
        data.value = posts
        sync()
    }

    override fun save(post: Post) {
        if (post.id == 0) {
            val dateFormatter = SimpleDateFormat("dd MMMM yyyy hh:mm", Locale.getDefault())
            val date = dateFormatter.format(Date())

            posts = listOf(
                post.copy(
                    id = nextId++,
                    author = getCurrentUser(),
                    published = date
                )
            ) + posts
            data.value = posts
            return
        }
        posts = posts.map {
            if (it.id != post.id) it else it.copy(content = post.content)
        }
        data.value = posts
        sync()
    }

    override fun showPost(id: Int): Post {
        return posts.filter { it.id == id }[0]
    }

}