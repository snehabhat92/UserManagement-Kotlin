package com.example.myapplication.Adapter

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.Model.User
import com.example.myapplication.Model.UserParcel
import com.example.myapplication.View.MainActivity
import com.example.myapplication.View.ProfileFragment

class UserAdapter(
    private val context: Context?,
    userList: List<User>
) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
    private var userList: List<User>
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.row_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user: User = userList[position]
        holder.firstName.setText(user.firstname)
        holder.lastName.setText(user.lastname)
        holder.userName.setText(user.username)
        holder.itemView.setOnClickListener(View.OnClickListener {
            if (context == null) return@OnClickListener
            val mainActivity: MainActivity = context as MainActivity
            val bundle = Bundle()
            val fragment = ProfileFragment()
            val address: User.Address = user.address
            val geo: User.Geo = user.address.geo
            bundle.putParcelable(
                "User",
                UserParcel(
                    user.id,
                    user.name,
                    user.firstname,
                    user.lastname,
                    user.username,
                    user.phone,
                    user.email,
                    user.website,
                    user.company.name,
                    buildAddress(address),
                    geo.lat,
                    geo.lng
                )
            )
            val fragmentTransaction: FragmentTransaction =
                mainActivity.supportFragmentManager.beginTransaction()
            fragment.arguments = bundle
            fragmentTransaction.replace(R.id.frame_main, fragment).addToBackStack("ProfilePage")
                .commit()
        })
    }

    private fun buildAddress(address: User.Address?): String {
        val sb = StringBuilder()
        if (address != null) {
            if (!TextUtils.isEmpty(address.street)) {
                sb.append(address.street + ", ")
            }
            if (!TextUtils.isEmpty(address.suite)) {
                sb.append(address.suite + ", ")
            }
            if (!TextUtils.isEmpty(address.city)) {
                sb.append(address.city + ", ")
            }
            if (!TextUtils.isEmpty(address.zipcode)) {
                sb.append(address.zipcode)
            }
        }
        return sb.toString()
    }

    fun setItems(userList: List<User>) {
        this.userList = userList
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    class UserViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var firstName: TextView = itemView.findViewById(R.id.txt_first_name)
        var lastName: TextView = itemView.findViewById(R.id.txt_last_name)
        var userName: TextView = itemView.findViewById(R.id.txt_user_name)
    }

    init {
        this.userList = userList
    }
}