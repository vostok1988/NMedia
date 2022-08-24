package ru.netology.nmedia

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_feed.*
import ru.netology.nmedia.NewPostFragment.Companion.textArg
import ru.netology.nmedia.databinding.FragmentFeedBinding

class FeedFragment : Fragment() {

    private val viewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    companion object {
        var Bundle.postIdArg: Int
            set(value) = putInt("POST_ID", value)
            get() = getInt("POST_ID")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentFeedBinding = FragmentFeedBinding.inflate(inflater, container, false)

        val id = arguments?.postIdArg
        if (id != null) {
            viewModel.removeById(id)
        }

        val adapter = PostAdapter(object : OnInteractionListener {
            override fun onLikeListener(id: Int) {
                viewModel.likeDislike(id)
            }

            override fun onRepostListener(post: Post) {
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

            override fun onRemoveListener(id: Int) {
                viewModel.removeById(id)
            }

            override fun onEditItem(post: Post) {
                viewModel.edit(post)
                val text = post.content
                findNavController().navigate(
                    R.id.action_feedFragment_to_newPostFragment,
                    Bundle().apply {
                        textArg = text
                    }
                )
            }

            override fun onShowPost(id: Int) {
                findNavController().navigate(R.id.action_feedFragment_to_showPostFragment,
                    Bundle().apply {
                        postIdArg = id
                    })
            }

            override fun launchVideoLink(link: String) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                startActivity(intent)
            }

        })

        binding.postRecycler.adapter = adapter
        binding.newPost.setOnClickListener {
            findNavController().navigate(R.id.action_feedFragment_to_newPostFragment)
        }

        viewModel.data.observe(viewLifecycleOwner) { posts ->
            adapter.submitList(posts)
        }
        return binding.root
    }
}
