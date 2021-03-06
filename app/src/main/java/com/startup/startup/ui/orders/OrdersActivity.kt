package com.startup.startup.ui.orders

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.startup.startup.R
import com.startup.startup.ui.BaseActivity
import com.startup.startup.ui.orders.customer.CustomerOrdersFragment
import com.startup.startup.ui.orders.vendor.past.VendorPastOrdersFragment
import com.startup.startup.util.Constants.USER_TYPE
import com.startup.startup.util.Constants.userType_CUSTOMER
import com.startup.startup.util.Constants.userType_VENDOR

class OrdersActivity: BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orders)

        val userType = intent.getStringExtra(USER_TYPE)

        if(userType == userType_CUSTOMER){
            startFragment(CustomerOrdersFragment())
        }
        else if(userType == userType_VENDOR){
            startFragment(VendorPastOrdersFragment())
        }
    }

    private fun startFragment(fragment: Fragment){
        val ft = supportFragmentManager.beginTransaction()
        ft.add(R.id.orders_container, fragment)
        ft.addToBackStack(fragment.tag)
        ft.commit()
    }

    override fun onBackPressed() {
        if(supportFragmentManager.backStackEntryCount == 1)
            finish()
        else
            super.onBackPressed()
    }
}