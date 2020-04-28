package com.example.e203.sessionData

import com.example.e203.Utility.FileReadUtils.Singleton.instance as FileReadUtilsInstance
import com.example.e203.appData.FileManagerUtil.Singleton.instance as FileManagerInstance

class fetchedMetaData {

    val APP_DOWNLOAD_LINK= "app_download_link"
    val APP_DOWNLOAD_VERSION= "app_versCode"
    val PAYMENT_UPI_PAY_LINK= "upi_paymentID"
    val PAYMENT_UPI_PAY_DESCRIPTION= "upi_paymentDescription"

    val TAG_CURRENT_OUTSTANDING = "currentOutstanding_"
    val TAG_PENDING_BILL = "pendingBill_"

    private var fetchedDataMap: MutableMap<String, String> = mutableMapOf()
    object Singleton {
        var instance = fetchedMetaData()
    }

    fun getValue(key: String): String? {
        FileReadUtilsInstance.readPairCSVnPopulateMap(
            fetchedDataMap,
            FileManagerInstance.fetchedMetadataStorage)
        return fetchedDataMap[key]
    }

    fun getValueByLabel(pre: String, post: String): String {
        return getValue((pre + post))!!
    }
}