package alex.pakshin.ru.netology.nmedia.db

import alex.pakshin.ru.netology.nmedia.data.Post

interface PostDao {
    fun getAll():List<Post>
    fun save(post:Post):Post
    fun likeById(id:Long)
    fun shareById(id: Long)
    fun removeById(id: Long)
}