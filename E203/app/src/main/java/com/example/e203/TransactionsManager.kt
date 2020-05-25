package com.example.e203

class TransactionsManager {

    object Singleton {
        var instance = TransactionsManager()
    }

    var transactions = mutableListOf<TransactionRecord>()
}