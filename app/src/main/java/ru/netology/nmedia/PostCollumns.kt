package ru.netology.nmedia

object PostColumns {
    const val TABLE = "posts"
    const val COLUMN_ID = "id"
    const val COLUMN_AUTHOR = "author"
    const val COLUMN_CONTENT = "content"
    const val COLUMN_PUBLISHED = "published"
    const val COLUMN_LIKED_BY_ME = "likedByMe"
    const val COLUMN_LIKE_COUNT = "likeCount"
    const val COLUMN_REPOST_COUNT = "repostCount"
    const val COLUMN_WATCHES_COUNT = "watchesCount"
    const val COLUMN_VIDEO = "video"
    val ALL_COLUMNS = arrayOf(
        COLUMN_ID,
        COLUMN_AUTHOR,
        COLUMN_CONTENT,
        COLUMN_PUBLISHED,
        COLUMN_LIKED_BY_ME,
        COLUMN_LIKE_COUNT,
        COLUMN_REPOST_COUNT,
        COLUMN_WATCHES_COUNT,
        COLUMN_VIDEO
    )
}