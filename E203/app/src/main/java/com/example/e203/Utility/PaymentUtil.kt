package com.example.e203.Utility

import com.example.e203.sessionData.fetchedMetaData.Singleton.instance as fetchedMetadatas
import com.example.e203.sessionData.localConfig.Singleton.instance as localConfigInstance

class PaymentUtil {

    object Singleton {
        var instance = PaymentUtil()
    }

    fun isPayOptionEnabled(): Boolean {
        val username = localConfigInstance.getValue("username")!!.toLowerCase()
        val payBill = fetchedMetadatas.getValue("pendingBill_" + username)
        return (payBill != null && payBill.length>0 && payBill.toInt()>0)
    }

    fun isAmountButtonVisible(): Boolean {
        return localConfigInstance.doesUsernameExists()
    }
}