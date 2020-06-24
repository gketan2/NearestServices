package com.startup.startup.ui.main.customer.itemsInShop

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.startup.startup.datamodels.Item
import com.startup.startup.network.Database
import com.startup.startup.ui.main.DataResource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class ItemsInShopFragmentViewModel
    @Inject
    constructor(): ViewModel() {

    var items: MutableLiveData<DataResource<ArrayList<Item>>> = MutableLiveData()

    fun getItems(shopId: String): LiveData<DataResource<ArrayList<Item>>> {
        items.value = DataResource.loading()

        Database().getItems(shopId).addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                items.value = DataResource.error(error.message)
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                println(snapshot.value)
                if(snapshot.value != null){
                    CoroutineScope(Dispatchers.Default).launch{
                        var data: ArrayList<Item> = ArrayList()
                        for (item in snapshot.children) {
                            val i = Item(item.key)
                            val v = item.value as HashMap<String, String>
                            i.itemName = v["itemname"]
                            i.itemPrice = v["itemprice"]
                            i.itemQuantity = v["quantity"]
                            i.isAvailable = v["isavailable"] == "true"
                            data.add(i)
                        }
                        items.postValue(DataResource.success(data))
                    }
                }else{
                    items.postValue(DataResource.error("No items in this Shop"))
                }
            }
        })
        return items
    }

    fun getItems(): LiveData<DataResource<ArrayList<Item>>>{
        return items
    }
}