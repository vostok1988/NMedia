package alex.pakshin.ru.netology.nmedia.adapter

import alex.pakshin.ru.netology.nmedia.data.Post
import alex.pakshin.ru.netology.nmedia.R
import alex.pakshin.ru.netology.nmedia.databinding.PostBinding
import alex.pakshin.ru.netology.nmedia.util.getDecimalFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView


internal class PostAdapter(
    private val interactionListener: PostInteractionListener
) : ListAdapter<Post, PostAdapter.ViewHolder>(DiffCallback) {

    class ViewHolder(
        private val binding: PostBinding,
        listener: PostInteractionListener
    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var post: Post

        private val popupMenu by lazy {
            PopupMenu(itemView.context, binding.options).apply {
                inflate(R.menu.options_post)
                setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.remove -> {
                            listener.onRemoveClicked(post)
                            true
                        }
                        R.id.edit -> {
                            listener.onEditClicked(post)
                            true
                        }
                        else -> false
                    }
                }
                setOnDismissListener { binding.options.isChecked = false }
            }
        }

        init {
            with(binding){
               like.setOnClickListener {
                    listener.onLikeClicked(post)
                }
                share.setOnClickListener {
                    listener.onShareClicked(post)
                }
                options.setOnClickListener {
                    popupMenu.show()
                }
                playButton.setOnClickListener {
                    listener.onPlayClicked(post)
                }
                videoPreview.setOnClickListener {
                    listener.onPlayClicked(post)
                }
                postCard.setOnClickListener{
                    listener.onPostClicked(post)
                }
            }
        }

        fun bind(post: Post) {
            this.post = post
            with(binding) {
                authorName.text = post.author
                date.text = post.published
                content.text = post.content
                like.isChecked = post.liked
                like.text =  getDecimalFormat(post.likeCount)
                share.text = getDecimalFormat(post.shareCount)
                if (!post.url.isNullOrBlank()) videoView.visibility = View.VISIBLE
            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = PostBinding.inflate(inflater, parent, false)
        return ViewHolder(binding, interactionListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private object DiffCallback : DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean =
            oldItem == newItem
    }
}