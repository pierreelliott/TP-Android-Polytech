package com.example.tp2.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tp2.R
import com.example.tp2.databinding.ItemViewBinding
import com.example.tp2.model.User
import java.text.SimpleDateFormat
import java.util.*

class UserAdapter(val clickListener: UserListener) : ListAdapter<User, UserAdapter.ViewHolder>(UserDiffCallback()) {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val lastnameTv: TextView = itemView.findViewById(R.id.tv_lastname_item)
        val firstnameTv: TextView = itemView.findViewById(R.id.tv_firstname_item)
        val birthdayTv: TextView = itemView.findViewById(R.id.tv_birthday_item)
        val genderTv: TextView = itemView.findViewById(R.id.tv_gender_item)
    }

    class ViewHolder private constructor(val binding: ItemViewBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(item: User, clickListener: UserListener) {
            binding.user = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemViewBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    class UserDiffCallback : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }
    }

    class UserListener(val clickListener: (userid: Long) -> Unit) {
        fun onClick(user: User) = clickListener(user.id)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val item = getItem(position)
//        holder.bind(item)
        holder.bind(getItem(position)!!, clickListener)
    }
}