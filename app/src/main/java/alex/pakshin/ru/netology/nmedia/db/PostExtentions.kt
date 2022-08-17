package alex.pakshin.ru.netology.nmedia.db

import alex.pakshin.ru.netology.nmedia.data.Post
import android.database.Cursor

fun Cursor.toPost() = Post(
    id = getLong(getColumnIndexOrThrow(PostsTable.Column.ID.columnName)),
    author = getString(getColumnIndexOrThrow(PostsTable.Column.Author.columnName)),
    content = getString(getColumnIndexOrThrow(PostsTable.Column.Content.columnName)),
    published = getString(getColumnIndexOrThrow(PostsTable.Column.Published.columnName)),
    liked = getInt(getColumnIndexOrThrow(PostsTable.Column.Liked.columnName)) != 0,
    likeCount = getInt(getColumnIndexOrThrow(PostsTable.Column.LikeCount.columnName)),
    shareCount = getInt(getColumnIndexOrThrow(PostsTable.Column.ShareCount.columnName)),
    url = getString(getColumnIndexOrThrow(PostsTable.Column.URL.columnName))
)