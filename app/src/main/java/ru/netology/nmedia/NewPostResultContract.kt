package ru.netology.nmedia

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract

class NewPostResultContract : ActivityResultContract<String?, String?>() {
    override fun createIntent(context: Context, input: String?): Intent {
        return Intent(context, NewPostFragment::class.java).putExtra("content", input)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): String? =
        if (resultCode == Activity.RESULT_OK) {
            intent?.getStringExtra(Intent.EXTRA_TEXT)
        } else {
            null
        }
}