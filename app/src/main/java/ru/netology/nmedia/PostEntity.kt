package ru.netology.nmedia

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val author: String,
    val content: String,
    val published: String,
    val likedByMe: Boolean,
    val likeCount: Int = 0,
    val repostCount: Int = 0,
    val watchesCount: Int = 0,
    val video: String = ""
) {
    fun toDto() =
        Post(id, author, content, published, likedByMe, likeCount, repostCount, watchesCount, video)

    companion object {
        fun fromDto(dto: Post) =
            PostEntity(
                dto.id, dto.author, dto.content, dto.published, dto.likedByMe, dto.likeCount,
                dto.repostCount, dto.watchesCount, dto.video
            )

    }
}