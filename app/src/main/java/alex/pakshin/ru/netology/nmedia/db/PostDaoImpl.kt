package alex.pakshin.ru.netology.nmedia.db

import alex.pakshin.ru.netology.nmedia.data.Post
import alex.pakshin.ru.netology.nmedia.data.PostRepository
import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase

class PostDaoImpl(
    private val db: SQLiteDatabase
) : PostDao {
    override fun getAll() = db.query(
        PostsTable.NAME,
        PostsTable.ALL_COLUMNS_NAMES,
        null, null, null, null,
        "${PostsTable.Column.ID.columnName} DESC"
    ).use { cursor ->
        List(cursor.count) {
            cursor.moveToNext()
            cursor.toPost()
        }
    }

    override fun removeById(id: Long) {
        db.delete(
            PostsTable.NAME,
            "${PostsTable.Column.ID.columnName} = ?",
            arrayOf(id.toString())
        )
    }

    override fun likeById(id: Long) {
        db.execSQL(
            """
            UPDATE ${PostsTable.NAME} SET
                likeCount = likeCount + CASE WHEN liked THEN -1 ELSE 1 END,
                liked = CASE WHEN liked THEN 0 ELSE 1 END
                WHERE id = ?;
        """.trimIndent(), arrayOf(id)
        )
    }

    override fun shareById(id: Long) {
        db.execSQL(
            """
            UPDATE ${PostsTable.NAME} SET
                shareCount = shareCount + 1 
                WHERE id = ?;
        """.trimIndent(), arrayOf(id)
        )
    }

    override fun save(post: Post): Post {
        val values = ContentValues().apply {
            put(PostsTable.Column.Author.columnName, post.author)
            put(PostsTable.Column.Content.columnName, post.content)
            put(PostsTable.Column.Published.columnName, post.published)
            put(PostsTable.Column.URL.columnName, post.url)
        }

        val id = if (post.id != PostRepository.NEW_POST_ID) {
            db.update(
                PostsTable.NAME,
                values,
                "${PostsTable.Column.ID.columnName} = ?",
                arrayOf(post.id.toString())
            )
            post.id
        } else {
            db.insert(
                PostsTable.NAME,
                null,
                values
            )
        }

        return db.query(
            PostsTable.NAME,
            PostsTable.ALL_COLUMNS_NAMES,
            "${PostsTable.Column.ID.columnName} = ?",
            arrayOf(id.toString()),
            null, null, null
        ).use { cursor ->
            cursor.moveToNext()
            cursor.toPost()
        }
    }
}