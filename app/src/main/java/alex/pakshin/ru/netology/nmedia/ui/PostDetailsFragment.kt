package alex.pakshin.ru.netology.nmedia.ui

import alex.pakshin.ru.netology.nmedia.R
import alex.pakshin.ru.netology.nmedia.data.Post
import alex.pakshin.ru.netology.nmedia.databinding.PostDetailsFragmentBinding
import alex.pakshin.ru.netology.nmedia.util.getDecimalFormat
import alex.pakshin.ru.netology.nmedia.viewModel.PostViewModel
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class PostDetailsFragment : Fragment() {
    private val viewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )
    private val args by navArgs<PostDetailsFragmentArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setFragmentResultListener(
            requestKey = PostContentFragment.REQUEST_KEY
        ) { requestKey, bundle ->
            if (requestKey != PostContentFragment.REQUEST_KEY) return@setFragmentResultListener
            val newPostContent =
                bundle.getString(PostContentFragment.RESULT_KEY) ?: return@setFragmentResultListener
            viewModel.onSaveButtonClicked(newPostContent)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.navigatePostContentScreenEvent.observe(viewLifecycleOwner) { initialContent ->
            val direction =
                PostDetailsFragmentDirections.postDetaisToPostContentFragment(initialContent)
            findNavController().navigate(direction)
        }

        viewModel.sharePostContent.observe(viewLifecycleOwner) { postContent ->
            val intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, postContent)
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(intent, getString(R.string.chooser_share_post))
            startActivity(shareIntent)
        }

        viewModel.videoUrl.observe(viewLifecycleOwner) { url ->
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = PostDetailsFragmentBinding.inflate(layoutInflater, container, false).also { binding ->


        viewModel.data.observe(viewLifecycleOwner) { posts ->

            val post = posts.find { it.id == args.postId } ?: return@observe

            val popupMenu by lazy {
                PopupMenu(requireContext(), binding.postLayout.options).apply {
                    inflate(R.menu.options_post)
                    setOnMenuItemClickListener { menuItem ->
                        when (menuItem.itemId) {
                            R.id.remove -> {
                                viewModel.onRemoveClicked(post)
                                findNavController().popBackStack()
                                true
                            }
                            R.id.edit -> {
                                viewModel.onEditClicked(post)
                                true
                            }
                            else -> false
                        }
                    }
                    setOnDismissListener { binding.postLayout.options.isChecked = false }
                }
            }

            with(binding.postLayout) {
                authorName.text = post.author
                date.text = post.published
                content.text = post.content
                like.isChecked = post.liked
                like.text = getDecimalFormat(post.likeCount)
                share.text = getDecimalFormat(post.shareCount)

                if (!post.url.isNullOrBlank()) videoView.visibility = View.VISIBLE

                like.setOnClickListener {
                    viewModel.onLikeClicked(post)
                }
                share.setOnClickListener {
                    viewModel.onShareClicked(post)
                }
                options.setOnClickListener {
                    popupMenu.show()
                }
                playButton.setOnClickListener {
                    viewModel.onPlayClicked(post)
                }
                videoPreview.setOnClickListener {
                    viewModel.onPlayClicked(post)
                }
            }
        }
    }.root
}