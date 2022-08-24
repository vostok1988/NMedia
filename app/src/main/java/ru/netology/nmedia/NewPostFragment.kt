package ru.netology.nmedia

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.databinding.NewPostFragmentBinding

class NewPostFragment : Fragment() {
    private val viewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    companion object {
        var Bundle.textArg: String? by StringArg
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding: NewPostFragmentBinding =
            NewPostFragmentBinding.inflate(inflater, container, false)

        binding.newContent.requestFocus()
        if (viewModel.draft != "") {
            binding.newContent.setText(viewModel.draft)
        }

        arguments?.textArg
            ?.let(binding.newContent::setText)



        binding.saveButton.setOnClickListener {
            val content = binding.newContent.text.toString()
            viewModel.changeContent(content)
            viewModel.save()
            AndroidUtils.hideKeyboard(requireView())
            findNavController().navigateUp()
        }
        binding.declineButton.setOnClickListener {
            binding.newContent.text.clear()
        }

        val callback = requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            viewModel.saveDraft(binding.newContent.text.toString())
            AndroidUtils.hideKeyboard(requireView())
            findNavController().navigateUp()
        }
        callback.isEnabled

        return binding.root
    }
}