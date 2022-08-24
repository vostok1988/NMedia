package ru.netology.nmedia

import org.junit.Assert.*
import org.junit.Test

class PostTest{
    @Test
    fun likesToStringK() {
        val expected = "1,2K"
        val post = Post(
            id = 1,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Привет, это новая Нетология! Когда-то нетология начиналасьс интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. http://netolo.gy/fyb",
            published = "27 апреля в 16:36",
            likeCount = 1260,
            repostCount = 20,
            watchesCount = 300,
            likedByMe = false
        )
        val actual = post.likesToString()
        assertEquals(expected,actual)
    }

    @Test
    fun likesToStringKAbove10() {
        val expected = "20K"
        val post = Post(
            id = 1,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Привет, это новая Нетология! Когда-то нетология начиналасьс интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. http://netolo.gy/fyb",
            published = "27 апреля в 16:36",
            likeCount = 20260,
            repostCount = 20,
            watchesCount = 300,
            likedByMe = false
        )
        val actual = post.likesToString()
        assertEquals(expected,actual)
    }

    @Test
    fun likesToStringM() {
        val expected = "2,3M"
        val post = Post(
            id = 1,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Привет, это новая Нетология! Когда-то нетология начиналасьс интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. http://netolo.gy/fyb",
            published = "27 апреля в 16:36",
            likeCount = 2360200,
            repostCount = 20,
            watchesCount = 300,
            likedByMe = false
        )
        val actual = post.likesToString()
        assertEquals(expected,actual)
    }
    @Test
    fun repostsToStringUnder1000() {
        val expected = "202"
        val post = Post(
            id = 1,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Привет, это новая Нетология! Когда-то нетология начиналасьс интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. http://netolo.gy/fyb",
            published = "27 апреля в 16:36",
            likeCount = 2360200,
            repostCount = 202,
            watchesCount = 300,
            likedByMe = false
        )
        val actual = post.repostsToString()
        assertEquals(expected,actual)
    }

}