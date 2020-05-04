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
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.kraftwerk28.pocketcms.Database
import com.kraftwerk28.pocketcms.R
import com.kraftwerk28.pocketcms.databinding.FragmentDbconnectBinding
import com.kraftwerk28.pocketcms.viewmodels.ConnectCredentials
import kotlinx.coroutines.runBlocking

class DBConnectFragment : Fragment() {

    private lateinit var binding: FragmentDbconnectBinding
    private lateinit var viewModel: ConnectCredentials
    private val TAG = "DBConnectFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = ConnectCredentials(
            savedInstanceState?.getString("host") ?: "10.0.2.2",
            savedInstanceState?.getInt("port") ?: 5432,
            savedInstanceState?.getString("username") ?: "kraftwerk28",
            savedInstanceState?.getString("password") ?: "271828",
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

        binding.connectButton.setOnClickListener {
            val connected = performConnection()
            if (connected) {
                findNavController().navigate(
                    R.id.action_DBConnectFragment2_to_DBTablesViewFragment
                )
            }
        }
        binding.dbSelectButton.setOnClickListener {
            val connected = performConnection()
            val bundle = bundleOf("creds" to createCredentials())
            if (connected) {
                findNavController().navigate(
                    R.id.action_DBConnectFragment2_to_DBViewFragment,
                    bundle
                )
            }
        }

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
        showToast("Copied to clipboard")
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

    fun performConnection(): Boolean {
        if (!validate()) {
            Log.i(TAG, "Failed to validate")
            return false
        }
        val r = runBlocking {
            Database.connect(createCredentials()).await()
        }
        return r?.let {
            true
        } ?: run {
            showToast("DB connection error")
            false
        }
    }

    fun showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
        Toast
            .makeText(activity, message, duration)
            .show()
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

    fun createCredentials() = Database.Credentials(
        viewModel.host.value!!,
        viewModel.port.value!!,
        viewModel.username.value!!,
        viewModel.password.value!!,
        viewModel.dbName.value!!
    )
}
