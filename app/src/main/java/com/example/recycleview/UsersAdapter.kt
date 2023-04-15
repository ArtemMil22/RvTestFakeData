package com.example.recycleview

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recycleview.databinding.ItemUserBinding
import com.example.recycleview.model.User

class UsersAdapter : RecyclerView.Adapter<UsersAdapter.UsersViewHolder>() {

    var users: List<User> = emptyList()

    @SuppressLint("NotifyDataSetChanged")
    set(newValue){
        field = newValue
        notifyDataSetChanged()
    }

    class UsersViewHolder(
        val binding: ItemUserBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemUserBinding.inflate(inflater,parent,false)
        return UsersViewHolder(binding)
    }

    override fun getItemCount(): Int = users.size


    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        val user = users[position]
        with(holder.binding){
            userNameTextView.text = user.name
            userCompanyTextView.text = user.company
            if(user.photo.isNotBlank()){
                Glide.with(photoImageView.context)
                    .load(user.photo)
                    .circleCrop()
                    .placeholder(R.drawable.ic_user_avatar)
                    .error(R.drawable.ic_user_avatar)
                    .into(photoImageView)
            }else{
                  photoImageView.setImageResource(R.drawable.ic_user_avatar)
            }
            
        }
    }


}