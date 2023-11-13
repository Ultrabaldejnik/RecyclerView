package com.example.recyclerview

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.github.serpro69.kfaker.Faker

class ContactViewModel : ViewModel() {

    private val _contacts = MutableLiveData<MutableList<Contact>>()
    val contacts : LiveData<MutableList<Contact>>
        get() = _contacts

    private val _choiceClickListener = MutableLiveData<Boolean>()
    val choiceClickListener : LiveData<Boolean>
        get() = _choiceClickListener

    init {
        val faker = Faker()
        val list = mutableListOf<Contact>()
        for (i in 1..100) {
            val contact =
                Contact(
                    id = i,
                    firstName = faker.name.firstName(),
                    lastName = faker.name.lastName(),
                    phoneNumber = faker.phoneNumber.phoneNumber()
                )
            list.add(contact)
        }
        _contacts.postValue(list)
        _choiceClickListener.postValue(false)
    }

    fun addContact(contact: Contact){
        val list = _contacts.value
        list?.add(contact)
        _contacts.postValue(list)
    }

    fun editContact(index : Int, contact: Contact){
        val list = _contacts.value!!
        list[index] = contact
        _contacts.postValue(list)
    }

    fun deleteContacts(listContacts : List<Int>){
        val list = _contacts.value
        listContacts.forEach{
            list?.removeAt(it)
        }
        _contacts.postValue(list)
    }

    fun changeModeClick(){
        _choiceClickListener.value = !_choiceClickListener.value!!
    }

    fun getSize(): Int = _contacts.value?.last()?.id!!
}