package com.startup.startup.ui.main

interface Communicator {
    fun onServiceSelected(serviceId: String)

    fun onShopSelected(shopId: String)
}