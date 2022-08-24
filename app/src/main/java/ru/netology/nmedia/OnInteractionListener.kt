package ru.netology.nmedia

interface OnInteractionListener {
    fun onLikeListener(id: Int)
    fun onRepostListener(post: Post)
    fun onRemoveListener(id: Int)
    fun onEditItem(post: Post)
    fun launchVideoLink(link: String)
    fun onShowPost(id: Int)
}