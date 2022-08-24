package ru.netology.nmedia

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PostDao {
    @Query("SELECT * FROM PostEntity ORDER BY id DESC")
    fun getAll(): LiveData<List<PostEntity>>

    @Insert
    fun insert(postEntity: PostEntity)

    @Query("UPDATE PostEntity SET content= :content WHERE id= :id")
    fun updateContentById(id: Int, content: String)

    @Query(
        """
                UPDATE PostEntity SET
                likeCount = likeCount + CASE WHEN likedByMe THEN -1 ELSE 1 END,
                likedByMe = CASE WHEN likedByMe THEN 0 ELSE 1 END
                WHERE id = :id;
            """
    )
    fun likeDislike(id: Int)

    @Query(
        """
                UPDATE PostEntity SET
                repostCount = repostCount + 1
                WHERE id = :id;
            """
    )
    fun repost(id: Int)

    @Query("DELETE FROM PostEntity WHERE id= :id")
    fun removeById(id: Int)


    fun save(post: PostEntity) =
        if (post.id == 0) insert(post) else updateContentById(post.id, post.content)

    @Query("SELECT * FROM PostEntity WHERE id= :id")
    fun showPost(id: Int): Post
}