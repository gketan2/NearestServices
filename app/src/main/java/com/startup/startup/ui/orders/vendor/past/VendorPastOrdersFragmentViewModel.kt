package com.startup.startup.ui.orders.vendor.past

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.startup.startup.SessionManager
import com.startup.startup.datamodels.Order
import com.startup.startup.network.Database
import com.startup.startup.ui.main.DataResource
import com.startup.startup.util.Constants.userType_VENDOR
import javax.inject.Inject

class VendorPastOrdersFragmentViewModel
    @Inject
    constructor(
        private val sessionManager: SessionManager
    ): ViewModel(){

    private var pastOrders: MediatorLiveData<DataResource<ArrayList<Order>>> = MediatorLiveData()

    fun getLiveData(): LiveData<DataResource<ArrayList<Order>>> {
        return pastOrders
    }

    fun getPastOrderOrders(){
        Database().getPastOrder(userType_VENDOR, sessionManager.getUserId())
            .addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    pastOrders.value = DataResource.error(error.message)
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.value != null){
                        val orderIds = snapshot.value as HashMap<String, String>
                        if(orderIds.size == 0){
                            pastOrders.value = DataResource.error("No Orders")
                        }else{
                            getOrderDetails(orderIds.keys)
                        }
                    }else{
                        pastOrders.value = DataResource.error("No Orders")
                    }
                }
            })
    }

    private fun getOrderDetails(orderId: Set<String>){
        for(key in orderId){
            Database().getOrderDetails(key).addListenerForSingleValueEvent(object:
                ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val order = snapshot.getValue<Order>()!!
                    if(pastOrders.value == null){
                        val arrayList: ArrayList<Order> = ArrayList()
                        arrayList.add(order)
                        pastOrders.value = DataResource.success(arrayList)
                    }else{
                        val list = pastOrders.value!!.data
                        list.add(order)
                        pastOrders.value = DataResource.success(list)
                    }
                }
            })
        }
    }
}