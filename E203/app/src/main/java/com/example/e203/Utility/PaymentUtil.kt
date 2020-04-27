package com.example.e203.Utility

import com.example.e203.sessionData.fetchedMetaData
import com.example.e203.sessionData.localConfig

class PaymentUtil {

    object Singleton {
        var instance = PaymentUtil()
    }

    fun isPayOptionEnabled(): Boolean {
        val username = localConfig.Singleton.instance.getValue("username")!!.toLowerCase()
        val payBill = fetchedMetaData.Singleton.instance.getValue("pendingBill_" + username)
        return (payBill != null && payBill.toInt()>0)
    }
}