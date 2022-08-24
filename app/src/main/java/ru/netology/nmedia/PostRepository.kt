package ru.netology.nmedia

import androidx.lifecycle.LiveData

interface PostRepository {
    fun getAll(): LiveData<List<Post>>
    fun likeDislike(id: Int)
    fun repost(id: Int)
    fun removeById(id: Int)
    fun save(post: Post)
    fun showPost(id: Int):Post
}