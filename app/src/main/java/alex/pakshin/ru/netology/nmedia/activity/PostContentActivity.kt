package alex.pakshin.ru.netology.nmedia.activity

import alex.pakshin.ru.netology.nmedia.databinding.PostContentActivityBinding
import alex.pakshin.ru.netology.nmedia.util.showKeyboard
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContract
import androidx.appcompat.app.AppCompatActivity

class PostContentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = PostContentActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val content = intent.getStringExtra(RESULT_KEY)
        if (content!= null){
            binding.edit.setText(content)
        }

        binding.edit.requestFocus()
        binding.edit.showKeyboard()
        binding.ok.setOnClickListener {
            val intent = Intent()
            val text = binding.edit.text
            if (text.isNullOrBlank()) {
                setResult(Activity.RESULT_CANCELED, intent)
            } else {
                intent.putExtra(RESULT_KEY, text.toString())
                setResult(Activity.RESULT_OK, intent)
            }
            finish()
        }
    }

    object ResultContract : ActivityResultContract<String?, String?>() {
        override fun createIntent(context: Context, input: String?) =
            Intent(context, PostContentActivity::class.java).apply {
                putExtra(RESULT_KEY,input)
            }

        override fun parseResult(resultCode: Int, intent: Intent?) =
            if (resultCode == Activity.RESULT_OK) {
                intent?.getStringExtra(RESULT_KEY)
            } else null

    }

    private companion object{
        private const val RESULT_KEY = "postContent"
    }

}

