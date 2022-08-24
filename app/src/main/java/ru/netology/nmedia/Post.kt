package ru.netology.nmedia

data class Post(
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
    fun likesToString(): String {
        return shortCountOut(likeCount)
    }

    fun repostsToString(): String {
        return shortCountOut(repostCount)
    }

    fun watchesToString(): String {
        return shortCountOut(watchesCount)
    }

    private fun shortCountOut(count: Int): String {
        return when (count) {
            in 0..999 -> {
                count.toString()
            }
            in 1000..9999 -> {
                val s = (count / 1000).toString()
                val h = (count % 1000 / 100).toString()
                "$s,$h" + "K"
            }
            in 10000..999999 -> {
                val s = (count / 1000).toString()
                s + "K"
            }
            else -> {
                val m = (count / 1000000).toString()
                val s = (count % 1000000 / 100000)
                "$m,$s" + "M"
            }
        }
    }
}