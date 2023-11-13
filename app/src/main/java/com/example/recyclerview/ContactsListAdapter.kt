package com.example.recyclerview

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.RadioGroup
import android.widget.RadioGroup.OnCheckedChangeListener
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.recyclerview.databinding.ContactItemBinding

class ContactsListAdapter(private val context: Context) :
    ListAdapter<Contact, ContactsListAdapter.ContactViewHolder>(ContactItemDiffCallback()) {


    private var selectedContact = mutableListOf<Int>()
    var modeClick = false
    var onContactItemClickListener: ((Int, Contact) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        return ContactViewHolder(
            ContactItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = getItem(position)

        with(holder.binding) {
            tvFirstName.text = context.getString(R.string.first_name, contact.firstName)
            tvLastName.text = context.getString(R.string.last_name, contact.lastName)
            tvPhoneNumber.text = context.getString(R.string.phone_number, contact.phoneNumber)
            holder.binding.chekedImg.isClickable = false
            if (modeClick) {
                chekedImg.visibility = View.VISIBLE
            } else {
                chekedImg.visibility = View.GONE
                chekedImg.isChecked = false
            }
        }

        when (modeClick) {
            false -> {
                selectedContact.clear()
                holder.itemView.setOnClickListener {
                    onContactItemClickListener?.invoke(position, contact)
                }
            }
            true -> {
                holder.itemView.setOnClickListener {
                    holder.binding.chekedImg.isChecked = !holder.binding.chekedImg.isChecked
                    updateListForDelete(position)
                }
            }
        }
    }

    fun getListForDelete(): List<Int> = selectedContact

    private fun updateListForDelete(position: Int) {
        if (position in selectedContact) {
            selectedContact.remove(position)
        } else {
            selectedContact.add(position)
        }
    }


    class ContactViewHolder(val binding: ContactItemBinding) : RecyclerView.ViewHolder(binding.root)
}

class ContactItemDiffCallback : DiffUtil.ItemCallback<Contact>() {
    override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean {
        return oldItem == newItem
    }
}