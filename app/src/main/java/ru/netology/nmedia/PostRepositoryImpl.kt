package ru.netology.nmedia

import androidx.lifecycle.Transformations

class PostRepositoryImpl(private val postDao: PostDao) : PostRepository {


    override fun getAll() = Transformations.map(postDao.getAll()) { list ->
        list.map {
            Post(
                it.id,
                it.author,
                it.content,
                it.published,
                it.likedByMe,
                it.likeCount,
                it.repostCount,
                it.watchesCount,
                it.video
            )
        }
    }

    override fun likeDislike(id: Int) {
        postDao.likeDislike(id)

    }

    override fun repost(id: Int) {
        postDao.repost(id)
    }

    override fun removeById(id: Int) {
        postDao.removeById(id)
    }

    override fun save(post: Post) {
        postDao.save(PostEntity.fromDto(post))
    }

    override fun showPost(id: Int): Post {
        return postDao.showPost(id)
    }

}