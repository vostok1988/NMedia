package alex.pakshin.ru.netology.nmedia.data

import kotlinx.serialization.Serializable

@Serializable
data class Post(
    val id:Long,
    val author:String,
    val content:String,
    val published:String,
    val liked:Boolean = false,
    val likeCount:Int = 0,
    val shareCount:Int = 0,
    val url:String? = null
)
