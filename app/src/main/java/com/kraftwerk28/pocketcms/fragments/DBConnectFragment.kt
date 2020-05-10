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
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.kraftwerk28.pocketcms.Database
import com.kraftwerk28.pocketcms.R
import com.kraftwerk28.pocketcms.databinding.FragmentDbconnectBinding
import com.kraftwerk28.pocketcms.viewmodels.ConnectCredentials
import kotlinx.coroutines.*

class DBConnectFragment : Fragment() {

    private lateinit var binding: FragmentDbconnectBinding
    private lateinit var viewModel: ConnectCredentials
    private val TAG = "DBConnectFragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = requireActivity().intent?.data?.run {
            ConnectCredentials(
                getQueryParameter("host") ?: "10.0.2.2",
                getQueryParameter("port") ?: 5432.toString(),
                getQueryParameter("username") ?: "kraftwerk28",
                getQueryParameter("password") ?: "271828",
                getQueryParameter("dbName") ?: "postgres"
            )
        } ?: run {
            savedInstanceState?.run {
                ConnectCredentials(
                    getString("host", "10.0.2.2"),
                    getString("port", "5432"),
                    getString("username", "kraftwerk28"),
                    getString("password", "271828"),
                    getString("dbName", "postgres")
                )
            }
        } ?: ConnectCredentials(
            "",
            "5432",
            "kraftwerk28",
            "271828",
            ""
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_dbconnect,
            container,
            false
        )
        binding.run {
            setLifecycleOwner(this@DBConnectFragment)
            credentials = viewModel
            connectButton.setOnClickListener {
                hideKeyboard()
                goToTableView()
            }
            dbSelectButton.setOnClickListener {
                hideKeyboard()
                goToDBView()
            }
            goToTable.setOnClickListener {
                findNavController().navigate(R.id.action_tmp_toTableView)
            }
            connectionTitle.setOnClickListener {
                copyToClipboard(binding.connectionTitle.text.toString())
            }

            restoreChooseVariants(hostAutoCompleteTextView)
            restoreChooseVariants(dbNameInputAutoComplete)
        }

        return binding.root
    }

    private fun copyToClipboard(text: String) {
        val clipboard =
            activity?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("connection_string", text)
        clipboard.setPrimaryClip(clipData)
        showToast("Copied to clipboard")
    }

    private fun hideKeyboard() {
        requireActivity().run {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as
                    InputMethodManager
            currentFocus?.let {
                imm.hideSoftInputFromWindow(it.windowToken, 0)
            }
        }
    }

    fun validate(): Boolean {
        var correct = true
        if ((viewModel.port.value!!.length == 0) or
            (viewModel.port.value!!.toInt() > 65536)
        ) {
            binding.portInput.error = "Wrong port"
            correct = false
        }
        if (Character.isDigit(viewModel.dbName.value!!.get(0))) {
            binding.dbNameInput.error = "Bad DB name"
            correct = false
        }
        return correct
    }

    private suspend fun performConnection(): Boolean {

        if (!validate()) {
            Log.i(TAG, "Failed to validate")
            return false
        }
        val credentials = createCredentials()
        Database.credentials = credentials
        val r = Database.connect().await()
        return withContext(Dispatchers.Main) {
            r?.let {
                saveChooseVariants(
                    binding.dbNameInputAutoComplete,
                    credentials.dbName
                )
                saveChooseVariants(
                    binding.hostAutoCompleteTextView,
                    credentials.host
                )
                true
            } ?: run {
                showToast("DB connection error")
                false
            }
        }
    }

    private fun goToTableView() {
        GlobalScope.launch(Dispatchers.IO) {
            val connected = performConnection()
            if (!connected) return@launch
            withContext(Dispatchers.Main) {
                findNavController().navigate(
                    R.id.action_DBConnectFragment2_to_DBTablesViewFragment
                )
            }
        }
    }

    private fun goToDBView() {
        GlobalScope.launch(Dispatchers.IO) {
            val connected = performConnection()
            if (!connected) return@launch
            withContext(Dispatchers.Main) {
                findNavController().navigate(
                    R.id.action_DBConnectFragment2_to_DBViewFragment
                )
            }
        }
    }

    private fun showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
        Toast
            .makeText(activity, message, duration)
            .show()
    }

    private fun saveChooseVariants(actv: AutoCompleteTextView, value: String) {
        // Save database name for autocompleteview
        val prefs = requireActivity().getSharedPreferences(
            getString(R.string.sharedPrefsFileKey),
            Context.MODE_PRIVATE
        )
        prefs?.run {
            val key = "choose_variants.${actv.id}"
            val savedDbList = HashSet(getStringSet(key, hashSetOf())!!)
            savedDbList.add(value)
            val editor = edit()
            editor.putStringSet(key, savedDbList).apply()
        }
    }

    private fun restoreChooseVariants(actv: AutoCompleteTextView) {
        val prefs = requireActivity().getSharedPreferences(
            getString(R.string.sharedPrefsFileKey),
            Context.MODE_PRIVATE
        )
        prefs?.run {
            val key = "choose_variants.${actv.id}"
            val savedList = getStringSet(key, hashSetOf())!!
            actv.setAdapter(
                ArrayAdapter<String>(
                    context!!,
                    android.R.layout.simple_dropdown_item_1line,
                    savedList.toList()
                )
            )
        }
    }

    private fun createCredentials() = viewModel.run {
        Database.Credentials(
            host.value!!,
            port.value!!,
            username.value!!,
            password.value!!,
            dbName.value!!
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.i(javaClass.simpleName, "Fragment destroyed")
    }
}
