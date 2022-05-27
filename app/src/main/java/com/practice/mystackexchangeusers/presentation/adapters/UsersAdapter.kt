package com.practice.mystackexchangeusers.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.practice.mystackexchangeusers.databinding.ItemUsersBinding
import com.practice.mystackexchangeusers.domain.model.User


typealias OnUserClickedListener = (User) -> Unit

class UsersAdapter :
    RecyclerView.Adapter<UsersAdapter.UsersViewHolder>(), OnUserClickedListener {

    lateinit var onUserClickedListener: OnUserClickedListener

    private var userList = emptyList<User>()

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

    fun updateUserList(newUserList: List<User>) {

        val diffCallback = UsersDiffCallback(this.userList, newUserList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        diffResult.dispatchUpdatesTo(this)

        this.userList = newUserList
    }

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

class UsersDiffCallback(
    private val oldUsers: List<User>,
    private val newUsers: List<User>
): DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldUsers.size
    }

    override fun getNewListSize(): Int {
        return newUsers.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldUsers[oldItemPosition].userId == newUsers[newItemPosition].userId
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldUsers[oldItemPosition] == newUsers[newItemPosition]
    }
}