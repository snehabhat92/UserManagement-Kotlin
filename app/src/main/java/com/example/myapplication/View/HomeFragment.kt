package com.example.myapplication.View

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Adapter.UserAdapter
import com.example.myapplication.Model.User
import com.example.myapplication.Network.Retrofit
import com.example.myapplication.R
import com.example.myapplication.Repository.UserRepository
import com.example.myapplication.ViewModel.UserViewModel
import com.example.myapplication.ViewModel.ViewModelFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class HomeFragment : Fragment() {
    private lateinit var view1 : View
    private lateinit var userViewModel: UserViewModel
    private lateinit var editSearch: EditText
    private var userList: List<User>? = null
    private lateinit var userAdapter: UserAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        view1 = inflater.inflate(R.layout.fragment_home, container, false)
        if (activity != null) {
            activity!!.title = resources.getString(R.string.app_name)
        }
        initialiseUI()
        return view1
    }

    private fun initialiseUI() {
        editSearch = view1.findViewById(R.id.edit_search)
        val recyclerView = view1.findViewById<RecyclerView>(R.id.recyclerview)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        userList = ArrayList()
        networkRequest()
        userAdapter = UserAdapter(activity, userList as ArrayList)
        userViewModel = ViewModelProvider(this, ViewModelFactory(UserRepository(Retrofit.getInstance(), activity?.application))).get(UserViewModel::class.java)
        userViewModel.getAllUsers()?.observe(this, Observer {
            userList = it as List<User>?
            recyclerView.adapter = userAdapter
            Collections.sort(it, User.SortByName())
            userAdapter.setItems(it as List<User>)
        })
        setUpSearchView()
    }

    private fun setUpSearchView() {
        editSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
                if (!TextUtils.isEmpty(s)) {
                    val list: List<User?>? = userViewModel.query(s.toString())
                    if (list == null || list.isEmpty()) {
                        Toast.makeText(
                            activity,
                            resources.getString(R.string.no_data_found),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    list?.let { userAdapter.setItems(it as List<User>) }
                    userAdapter.notifyDataSetChanged()
                } else {
                    userList?.let { userAdapter.setItems(it) }
                    userAdapter.notifyDataSetChanged()
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })
    }

    private val isInternetConnected: Boolean
        get() {
            if (activity == null) {
                return false
            }
            val conMgr =
                activity!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            conMgr.activeNetworkInfo ?: return false
            return true
        }

    private fun networkRequest() {
        if (!isInternetConnected) {
            Toast.makeText(
                activity,
                resources.getString(R.string.no_internet_connection),
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        val retrofit = Retrofit.getInstance()
        val call: Call<List<User>> = retrofit.allUsers()
        call.enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                response.body()?.let { userViewModel.insert(it) }

            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                Log.e("UserViewModel" , t.message.toString())
                Toast.makeText(activity, "something went wrong...", Toast.LENGTH_SHORT).show()

            }
        })


    }
}