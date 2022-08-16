package alex.pakshin.ru.netology.nmedia.ui

import alex.pakshin.ru.netology.nmedia.databinding.PostContentFragmentBinding
import alex.pakshin.ru.netology.nmedia.util.hideKeyboard
import alex.pakshin.ru.netology.nmedia.util.showKeyboard
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs

class PostContentFragment : Fragment() {

    private val  args by navArgs<PostContentFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = PostContentFragmentBinding.inflate(layoutInflater, container, false).also { binding ->
        binding.edit.setText(args.initialContent)
        binding.edit.requestFocus()
        binding.edit.showKeyboard()
        binding.ok.setOnClickListener {
            onOkButtonClicked(binding)
        }
    }.root

    private fun onOkButtonClicked(binding: PostContentFragmentBinding) {
        val text = binding.edit.text
        if (!text.isNullOrBlank()) {
            val resultBundle = Bundle(1)
            resultBundle.putString(RESULT_KEY, text.toString())
            setFragmentResult(REQUEST_KEY, resultBundle)
            binding.edit.hideKeyboard()
        }
        findNavController().popBackStack()
    }


    companion object {
        const val REQUEST_KEY = "requestKey"
        const val RESULT_KEY = "postContent"

        private const val INITIAL_CONTENT_ARGUMENTS = "initialContent"
    }

}

