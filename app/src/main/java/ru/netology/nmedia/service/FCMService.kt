package ru.netology.nmedia.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import ru.netology.nmedia.Post
import ru.netology.nmedia.R
import kotlin.random.Random


class FCMService : FirebaseMessagingService() {
    private val action = "action"
    private val content = "content"
    private val channelId = "remote"
    private val gson = Gson()

    override fun onCreate() {
        super.onCreate()

        val name = getString(R.string.channel_remote_name)
        val descriptionText = getString(R.string.channel_remote_description)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelId, name, importance).apply {
            description = descriptionText
        }
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)

    }

    override fun onMessageReceived(message: RemoteMessage) {
        try {

            message.data[action]?.let {
                when (it) {
                    Action.LIKE.toString(), Action.REPOST.toString() -> handleLikeRepost(
                        gson.fromJson(
                            message.data[content],
                            Like::class.java
                        ), Action.valueOf(it)
                    )
                    Action.NEWPOST.toString() -> handleNewPost(
                        gson.fromJson(
                            message.data[content],
                            Post::class.java
                        )
                    )
                    else -> handleUnknown(gson.fromJson(message.data[content], String::class.java))
                }
            }
            println(Gson().toJson(message))
        } catch (e: JsonSyntaxException) {
            handleUnknown(getString(R.string.unknown_message_format))
        }
    }

    override fun onNewToken(token: String) {
        println(token)
    }

    private fun handleLikeRepost(content: Like, action: Action) {
        val actionString = when (action) {
            Action.LIKE -> R.string.notification_user_liked
            Action.REPOST -> R.string.notification_user_repost
            else -> R.string.notification_unknown
        }
        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_outline_notifications_24)
            .setContentTitle(
                getString(
                    actionString,
                    content.userName,
                    content.postAuthor,
                )
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        NotificationManagerCompat.from(this)
            .notify(Random.nextInt(100_000), notification)
    }

    private fun handleNewPost(content: Post) {
        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_baseline_message_24)
            .setContentTitle(
                getString(
                    R.string.notification_new_post,
                    content.author
                )
            )
            .setContentText(content.content.substring(0..15) + "...")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(content.content)
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        NotificationManagerCompat.from(this)
            .notify(Random.nextInt(100_000), notification)
    }

    private fun handleUnknown(content: String) {
        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_baseline_message_24)
            .setContentTitle(
                getString(R.string.notification_unknown)
            )
            .setContentText(content)
            .build()

        NotificationManagerCompat.from(this)
            .notify(Random.nextInt(100_000), notification)
    }
}

enum class Action {
    LIKE,
    REPOST,
    NEWPOST
}

data class Like(
    val userId: Long,
    val userName: String,
    val postId: Long,
    val postAuthor: String,
)

