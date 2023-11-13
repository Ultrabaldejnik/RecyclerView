package com.example.recyclerview

import android.content.DialogInterface
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.recyclerview.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {


    private lateinit var viewModel: ContactViewModel
    private lateinit var binding: ActivityMainBinding
    private lateinit var contactListAdapter: ContactsListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }

        setupRecyclerView()
        initVM()
        initUI()
    }

    private fun initUI() {
        with(binding) {
            groupDeleteContacts.visibility = View.GONE
            btnDelete.setOnClickListener {
                groupDeleteContacts.visibility = View.VISIBLE
                btnAdd.visibility = View.GONE
                btnDelete.visibility = View.GONE
                viewModel.changeModeClick()
            }

            btnClose.setOnClickListener {
                groupDeleteContacts.visibility = View.GONE
                btnAdd.visibility = View.VISIBLE
                btnDelete.visibility = View.VISIBLE
                viewModel.changeModeClick()
            }

            btnDeleteSelected.setOnClickListener {
                groupDeleteContacts.visibility = View.GONE
                btnAdd.visibility = View.VISIBLE
                btnDelete.visibility = View.VISIBLE

                viewModel.deleteContacts(contactListAdapter.getListForDelete())
                viewModel.changeModeClick()
            }

            binding.btnAdd.setOnClickListener {
                onAddDialog()
            }
        }
    }

    private fun initVM() {
        viewModel = ViewModelProvider(this)[ContactViewModel::class.java]
        viewModel.contacts.observe(this) {
            contactListAdapter.submitList(it.toList())
        }
        viewModel.choiceClickListener.observe(this){
            contactListAdapter.modeClick = it
            binding.rvContactList.adapter = contactListAdapter
        }
    }

    fun setupRecyclerView() {
        with(binding.rvContactList) {
            contactListAdapter = ContactsListAdapter(this@MainActivity)
            contactListAdapter.onContactItemClickListener = { position, contact ->
                onEditDialog(index = position, contact = contact)
            }
            adapter = contactListAdapter
        }
    }

    private fun onAddDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Add element")

        val etFirstName = EditText(this)
        val etLastName = EditText(this)
        val etPhoneNumber = EditText(this)

        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL

        etFirstName.inputType = InputType.TYPE_TEXT_VARIATION_PERSON_NAME
        etFirstName.hint = "First Name"
        etLastName.inputType = InputType.TYPE_TEXT_VARIATION_PERSON_NAME
        etLastName.hint = "Last Name"
        etPhoneNumber.inputType = InputType.TYPE_CLASS_PHONE
        etPhoneNumber.hint = "Phone Number"

        layout.addView(etFirstName)
        layout.addView(etLastName)
        layout.addView(etPhoneNumber)

        builder.setView(layout)

        builder.setPositiveButton("Add") { _, _ ->
            val firstName = etFirstName.text.toString().trim()
            val lastName = etLastName.text.toString().trim()
            val phoneNumber = etPhoneNumber.text.toString().trim()

            val contact = Contact(
                id = viewModel.getSize(),
                firstName = firstName,
                lastName = lastName,
                phoneNumber = phoneNumber
            )

            viewModel.addContact(contact)
            Toast.makeText(this@MainActivity, "User Add", Toast.LENGTH_SHORT).show()
        }

        builder.setNegativeButton("Cancel") { _, _ ->
            Toast.makeText(this@MainActivity, "Cancelled", Toast.LENGTH_SHORT).show()
        }

        builder.create().show()
    }

    private fun onEditDialog(index: Int, contact: Contact) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Edit element")

        val etFirstName = EditText(this)
        val etLastName = EditText(this)
        val etPhoneNumber = EditText(this)

        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL

        etFirstName.inputType = InputType.TYPE_TEXT_VARIATION_PERSON_NAME
        etFirstName.hint = "First Name"
        etLastName.inputType = InputType.TYPE_TEXT_VARIATION_PERSON_NAME
        etLastName.hint = "Last Name"
        etPhoneNumber.inputType = InputType.TYPE_CLASS_PHONE
        etPhoneNumber.hint = "Phone Number"

        with(contact) {
            etFirstName.setText(firstName)
            etLastName.setText(lastName)
            etPhoneNumber.setText(phoneNumber)
        }

        layout.addView(etFirstName)
        layout.addView(etLastName)
        layout.addView(etPhoneNumber)

        builder.setView(layout)

        builder.setPositiveButton("Edit") { _, _ ->
            val firstName = etFirstName.text.toString().trim()
            val lastName = etLastName.text.toString().trim()
            val phoneNumber = etPhoneNumber.text.toString().trim()

            val newContact = Contact(
                id = contact.id,
                firstName = firstName,
                lastName = lastName,
                phoneNumber = phoneNumber
            )
            viewModel.editContact(index = index, contact = newContact)
            Toast.makeText(this@MainActivity, "Success Edit", Toast.LENGTH_SHORT).show()
        }

        builder.setNegativeButton("Cancel") { p0, p1 ->
            Toast.makeText(this@MainActivity, "Cancelled Edit", Toast.LENGTH_SHORT).show()
        }

        builder.create().show()
    }



}




