package ru.netology.nmedia

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.FeedFragment.Companion.postIdArg
import ru.netology.nmedia.NewPostFragment.Companion.textArg
import ru.netology.nmedia.databinding.PostCardBinding

class ShowPostFragment : Fragment() {

    private val viewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: PostCardBinding = PostCardBinding.inflate(inflater, container, false)

        val id = arguments?.postIdArg
        if (id != null) {
            init(binding, id)
        }
        viewModel.data.observe(viewLifecycleOwner) {
            if (id != null) {
                init(binding, id)
            }
        }
        return binding.root
    }

    private fun init(binding: PostCardBinding, id: Int) {
        val post = id.let { viewModel.showPost(it) }
        binding.apply {
            authorTextView.text = post.author
            publishedTextView.text = post.published
            postText.text = post.content
            avatarView.setImageResource(R.drawable.ic_launcher_foreground)
            likeButton.text = post.likesToString()
            repostButton.text = post.repostsToString()
            watchesIcon.text = post.watchesToString()
            likeButton.isChecked = post.likedByMe

            likeButton.setOnClickListener {
                viewModel.likeDislike(post.id)
            }
            repostButton.setOnClickListener {
                viewModel.repost(post.id)

                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, post.content)
                    type = "text/plain"
                }
                val shareIntent =
                    Intent.createChooser(intent, getString(R.string.chooser_share_post))
                startActivity(shareIntent)
            }
            menuButton.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.popup_post)
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.removeItem -> {
                                findNavController().navigate(R.id.action_showPostFragment_to_feedFragment,
                                    Bundle().apply
                                    { postIdArg = post.id })
                                true
                            }
                            R.id.editItem -> {
                                viewModel.edit(post)
                                val text = post.content
                                findNavController().navigate(
                                    R.id.action_showPostFragment_to_newPostFragment,
                                    Bundle().apply {
                                        textArg = text
                                    }
                                )
                                true
                            }
                            else -> false
                        }
                    }
                }.show()
            }
            if (post.video != "") {
                videoLink.visibility = View.VISIBLE
                videoLink.setOnClickListener {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(post.video))
                    startActivity(intent)
                }
            }
        }
    }
}