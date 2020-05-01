package com.kraftwerk28.pocketcms.fragments

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.persistableBundleOf
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.*
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.kraftwerk28.pocketcms.R
import com.kraftwerk28.pocketcms.databinding.FragmentDbconnectBinding

class DBConnectFragment : Fragment() {

    private lateinit var binding: FragmentDbconnectBinding
    private lateinit var viewModel: ConnectCredentials
    private val TAG = "DBConnectFragment"

    class ConnectCredentials(
        _host: String,
        _port: Int,
        _username: String,
        _password: String,
        _dbName: String
    ) : ViewModel() {

        val host = MutableLiveData<String>(_host)

        val port = MutableLiveData<Int>(_port)

        val username = MutableLiveData<String>(_username)

        val password = MutableLiveData<String>(_password)

        val dbName = MutableLiveData<String>(_dbName)

        val connString = MediatorLiveData<String>()

        init {
            val cb = Observer<Any> {
                connString.value =
                    "postgresql://${host.value}:${port.value}/${dbName.value}"
            }
            connString.run {
                addSource(host, cb)
                addSource(port, cb)
                addSource(dbName, cb)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = ConnectCredentials(
            savedInstanceState?.getString("host") ?: "10.0.2.2",
            savedInstanceState?.getInt("port") ?: 5432,
            savedInstanceState?.getString("username") ?: "kraftwerk28",
            savedInstanceState?.getString("password") ?: "",
            savedInstanceState?.getString("dbName") ?: "postgres"
        )
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_dbconnect,
            container,
            false
        )
        binding.setLifecycleOwner(this)
        binding.credentials = viewModel

        binding.connectButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                if (!validate()) {
                    Log.i(TAG, "Validated successfully")
                    return
                }
                findNavController().navigate(R.id.action_DBConnectFragment2_to_DBViewFragment)
            }
        })

        binding.connectionTitle.setOnClickListener {
            copyToClipboard(binding.connectionTitle.text.toString())
        }

        return binding.root
    }

    fun copyToClipboard(text: String) {
        val clipboard =
            activity?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("connection_string", text)
        clipboard.setPrimaryClip(clipData)
        Toast.makeText(
            this@DBConnectFragment.activity,
            "Copied to clipboard",
            Toast.LENGTH_SHORT
        ).show()
    }

    fun validate(): Boolean {
        var correct = true
        if (viewModel.port.value!! > 65536) {
            binding.portInput.error = "Wrong port"
            correct = false
        }
        if (Character.isDigit(viewModel.dbName.value!![0])) {
            binding.dbNameInput.error = "Bad DB name"
            correct = false
        }
        return correct
    }

    fun performConnection() {

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.run {
            putString("host", viewModel.host.value)
            putInt("port", viewModel.port.value!!)
            putString("host", viewModel.username.value)
            putString("host", viewModel.password.value)
            putString("dbName", viewModel.dbName.value)
        }
    }
}
