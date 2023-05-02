package com.example.recycleview.presentation

import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recycleview.R
import com.example.recycleview.data.User
import com.example.recycleview.data.UserListItem
import com.example.recycleview.databinding.ItemUserBinding
import com.example.recycleview.domain.UserActionListener

/* 2.определим список действий которые
 может выполнять пользователь над списком
 действие не одно, поэтому не typealias,
 а интерфейс в конструктор и еще наследуемся
  от View.onClickListener */

class UsersAdapter(
    private val actionListener: UserActionListener
) : RecyclerView.Adapter<UsersAdapter.UsersViewHolder>(),
    View.OnClickListener {

    class UsersViewHolder(
        val binding: ItemUserBinding
    ) : RecyclerView.ViewHolder(binding.root)

    var users: List<UserListItem> = emptyList()
        set(newValue) {
            val diffCallBack = UserDiffCallBack(field, newValue)
            val diffResult = DiffUtil.calculateDiff(diffCallBack, true)
            field = newValue
            diffResult.dispatchUpdatesTo(this)
        }

    // в качестве аргумента приходит вьюшка на которую юзер нажал
    override fun onClick(v: View) {
        //вытаскиваем пользователя из тэга и будем его хранить в тэге нашей вью
        val user = v.tag as User
        when (v.id) {
            R.id.moreImageViewButton -> {
                // реализация контекстного меню
                showPopupMenu(v)
            }  else -> {
            actionListener.onUserDetails(user)
            }
        }
    }
    // фун для popupMenu с тремя действиями и у каждого
    // должен быть свой индификатор в компаньоне обжекте
    private fun showPopupMenu(view: View) {
        //создаем сам объект попап меню
        val popupMenu = PopupMenu(view.context, view)

        val user = view.tag as User
        val position = users.indexOfFirst { it.user.id == user.id }

        // для добавления ресурса вместо стринг добавим контекст
        val context = view.context
        // добавляем действие
        popupMenu.menu.add(
            0, ID_MOVE_UP, Menu.NONE, context.getString(R.string.move_up)
        ).apply {
            //опция переместить пользователя вверх доступна только если индекс > 0
            isEnabled = position > 0
        }
        popupMenu.menu.add(
            0, ID_MOVE_DOWN, Menu.NONE, context.getString(R.string.move_down)
        ).apply { isEnabled = position < users.size - 1 }
        popupMenu.menu.add(
            0, ID_REMOVE, Menu.NONE, context.getString(R.string.remove_contact)
        )
        if (user.company.isNotBlank()) {
            popupMenu.menu.add(0, ID_FIRE, Menu.NONE, context.getString(R.string.fire))
        }

        // дальше создаем куда будет приходить пункт меню на который пользователь нажал
        popupMenu.setOnMenuItemClickListener {
            //вытаскиваем его индификатор и будем выполнять соответствующее
            // действие взависимости от идентификатора
            when (it.itemId) {
                ID_MOVE_UP -> {
                    actionListener.onUserMove(user, -1)
                }
                ID_MOVE_DOWN -> {
                    actionListener.onUserMove(user, 1)
                }
                ID_REMOVE -> {
                    actionListener.onUserDelete(user)
                }
                ID_FIRE -> {
                    actionListener.onFireUse(user)
                }
            }
            return@setOnMenuItemClickListener true
        }
        popupMenu.show()
    }

    companion object {
        private const val ID_MOVE_UP = 1
        private const val ID_MOVE_DOWN = 2
        private const val ID_REMOVE = 3
        private const val ID_FIRE = 4
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): UsersViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemUserBinding.inflate(inflater, parent, false)
        // слушатель на нажатие элемент самого списка и кнопки море и передать
        // this потому что мы наследуеся от интерфейса View.OnClickListener
        binding.moreImageViewButton.setOnClickListener(this)
        return UsersViewHolder(binding)
    }

    override fun getItemCount(): Int = users.size

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        val userListItem = users[position]
        val user = userListItem.user
        val context = holder.itemView.context

        with(holder.binding) {
            //кладем юзера в тэг, сперва на все компоненты на которые
            // пользователь может нажать мы должны этот тэг
            // проинициализировать, но уже в onCreateViewHolder
            holder.itemView.tag = user
            moreImageViewButton.tag = user

            //рефакторинг с дополнением userListItem
            if(userListItem.isInProgress){
                moreImageViewButton.visibility = View.INVISIBLE
                itemProgressBar.visibility = View.VISIBLE
                holder.binding.root.setOnClickListener(null)
            } else {
                moreImageViewButton.visibility = View.VISIBLE
                itemProgressBar.visibility = View.INVISIBLE
                holder.binding.root.setOnClickListener(this@UsersAdapter)
            }

            //обычное выполнение
            userNameTextView.text = user.name

            userCompanyTextView.text = user.company.ifBlank {
                context.getString(R.string.unemployed)
            }

            if (user.photo.isNotBlank()) {
                Glide.with(photoImageView.context)
                    .load(user.photo)
                    .circleCrop()
                    .placeholder(R.drawable.ic_user_avatar)
                    .error(R.drawable.ic_user_avatar)
                    .into(photoImageView)
            } else {
                Glide.with(photoImageView.context).clear(photoImageView)
                photoImageView.setImageResource(R.drawable.ic_user_avatar)
            }
        }
    }
}