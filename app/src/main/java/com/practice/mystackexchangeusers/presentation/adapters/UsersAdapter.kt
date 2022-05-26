package com.practice.mystackexchangeusers.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practice.mystackexchangeusers.databinding.ItemUsersBinding
import com.practice.mystackexchangeusers.domain.model.User


typealias OnUserClickedListener = (User) -> Unit

class UsersAdapter :
    RecyclerView.Adapter<UsersAdapter.UsersViewHolder>(), OnUserClickedListener {

    lateinit var onUserClickedListener: OnUserClickedListener

    var userList = emptyList<User>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        UsersViewHolder(
            ItemUsersBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), this
        )

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        holder.bind(userList[position])
    }

    override fun getItemCount() = userList.size

    class UsersViewHolder(
        private val binding: ItemUsersBinding,
        val onItemClickListener: OnUserClickedListener
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            with(binding) {
                tvUserId.text = user.userId.toString()
                tvUserName.text = user.userName
                cvUsers.setOnClickListener {
                    onItemClickListener(user)
                }
            }
        }
    }

    override fun invoke(user: User) = onUserClickedListener(user)
}