package com.example.e203.Utility

import com.example.e203.appData.FileManagerUtil.Singleton.instance as fm
import com.example.e203.sessionData.FetchedMetaData.Singleton.instance as fetchedMetadatas
import com.example.e203.sessionData.LocalConfig.Singleton.instance as localConfigs

class PaymentUtil {

    object Singleton {
        var instance = PaymentUtil()
    }

    fun isPayOptionEnabled(): Boolean {
        if (localConfigs.doesUsernameExists() && fm.metadata.doesExist()) {
            val currentUser = localConfigs.getValue(localConfigs.USERNAME)!!.toLowerCase()
            val payBill =
                fetchedMetadatas.getValueByLabel(fetchedMetadatas.TAG_PENDING_BILL, currentUser)
            return payBill.isNotEmpty() && payBill.toInt() > 0
        }
        return false
    }

    fun isAmountButtonVisible(): Boolean {
        return localConfigs.doesUsernameExists()
    }

    fun getOutstandingAmount(currentUser: String): Int? {
        val outstandingBal = fetchedMetadatas.getValueByLabel(fetchedMetadatas.TAG_CURRENT_OUTSTANDING, currentUser)
        if(outstandingBal.isNotEmpty())
            return outstandingBal.toInt()
        return null
    }

    fun getPendingBill(currentUser: String): Int? {
        val payBill = fetchedMetadatas.getValueByLabel(fetchedMetadatas.TAG_PENDING_BILL, currentUser)
        if(payBill.isNotEmpty())
            return payBill.toInt()
        return null
    }

    fun isDisplayButtonEnabled(): Boolean {
        if(localConfigs.doesUsernameExists() && fm.metadata.doesExist()) {
            val currentUser = localConfigs.getValue(localConfigs.USERNAME)!!
            return (localConfigs.doesUsernameExists()
                    && (getOutstandingAmount(currentUser) != null
                    || getPendingBill(currentUser) != null))
        }
        return false
    }
}