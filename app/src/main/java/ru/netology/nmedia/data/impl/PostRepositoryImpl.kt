package ru.netology.nmedia.data.impl

import androidx.lifecycle.map
import ru.netology.nmedia.Post.Post
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.db.PostDao
import ru.netology.nmedia.db.toEntity
import ru.netology.nmedia.db.toModel

class PostRepositoryImpl(
    private val dao: PostDao
) : PostRepository {

    override val data = dao.getAll().map { entities ->
        entities.map { it.toModel() }
    }

    override fun save(post: Post) {
        if (post.id == PostRepository.NEW_POST_ID) dao.insert(post.toEntity())
        else dao.updateContentById(post.id, post.content)
    }

    override fun like(postId: Long) {
        dao.likedById(postId)
    }

    override fun share(postId: Long) {
        dao.share(postId)
    }

    override fun delete(postId: Long) {
        dao.removeById(postId)
    }

}