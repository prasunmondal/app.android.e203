package com.example.e203

import android.graphics.Typeface
import android.os.Bundle
import android.text.Layout
import android.text.SpannableString
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.e203.Utility.FileReadUtil
import com.example.e203.sessionData.AppContext
import kotlinx.android.synthetic.main.activity_transactions_listing.*
import org.w3c.dom.Text
import com.example.e203.appData.FileManagerUtil.Singleton.instance as fm


class TransactionRecord {

    lateinit var name: String
    lateinit var item: String
    lateinit var sharedBy: String
    lateinit var qty: String
    lateinit var price: String
    lateinit var createTime: String
    lateinit var time: String
    lateinit var editLink: String
    lateinit var userDebit: String
    lateinit var userCredit: String
}

class Tabs {
    val Tab_MyTransaction = "Tab_MyTransaction"
    val Tab_MyExpenses = "Tab_MyExpenses"
    val Tab_MySpent = "Tab_MySpent"
    val Tab_showAll = "Tab_showAll"

    val tabViewID_MyTransaction = R.id.tabMyTransactions
    val tabViewID_MyExpenses = R.id.tabExpenses
    val tabViewID_MySpent = R.id.tabSpent
    val tabViewID_showAll = R.id.tabAll

    var activeTab = Tab_MyTransaction
    lateinit var underLineTab:View

    object Singleton {
        var instance = Tabs()
    }
}

class SortBy {
    val itemName = "sortBy_itemName"
    val userDebit = "sortBy_userDebit"
    val userCredit = "sortBy_userCredit"
    val amount = "sortBy_amount"
    val creationDate = "sortBy_creationDate"
    val itemDate = "sortBy_itemDate"

    var activeSortType = creationDate

    object Singleton {
        var instance = SortBy()
    }

}

class TransactionsListing : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transactions_listing)
        setSupportActionBar(toolbar)
        AppContext.Singleton.instance.initialContext = this

        if(!fm.breakdownSheet.doesExist()) {
            Toast.makeText(this, "Downloading...", Toast.LENGTH_LONG).show()
            fm.breakdownSheet.download(::doneDownloading)
        }
        else {
            Toast.makeText(this, "Data Available...", Toast.LENGTH_LONG).show()
            FileReadUtil.Singleton.instance.printCSVfile(fm.downloadLink_CalculatingSheet)
        }

        Tabs.Singleton.instance.activeTab = Tabs.Singleton.instance.Tab_MyTransaction
        changeTab_MyTransaction(findViewById(R.id.cardContainers))
    }

    private fun displayCards() {
        var i=1
        var tabType = Tabs.Singleton.instance.activeTab
        transactionSort()

        var linearLayout = findViewById<LinearLayout>(R.id.cardContainers)
        linearLayout.removeAllViews()

        var showMethod = ::showAll

        if(tabType == Tabs.Singleton.instance.Tab_showAll) {
            showMethod = ::showAll
        } else if(tabType == Tabs.Singleton.instance.Tab_MyExpenses) {
            showMethod = ::isDebitTransaction
        } else if(tabType == Tabs.Singleton.instance.Tab_MySpent) {
            showMethod = ::isCreditTransaction
        } else if(tabType == Tabs.Singleton.instance.Tab_MyTransaction) {
            showMethod = ::isMyTransactions
        }

        var sum: Double = 0.0
        for(z:Int in 0..TransactionsManager.Singleton.instance.transactions.size-1) {
            var result = addMyCreditTextBox(
                i,
                TransactionsManager.Singleton.instance.transactions[z],
                showMethod,
                tabType
            )
            if (result != null){
                i++
                sum = result!!.plus(sum)
            }
        }

        if(i==1) {
            Toast.makeText(this, "No Transaction Found", Toast.LENGTH_LONG).show()
            val linearLayout = findViewById<LinearLayout>(R.id.cardContainers)
            var sharedBy = TextView(this)
            sharedBy.text = "No Transactions Found!"
            sharedBy.setTextColor(resources.getColor(R.color.tabs_text_inactive))
            sharedBy.setGravity(Gravity.CENTER)
            sharedBy.setPadding(0,150,0,10)
            linearLayout.addView(sharedBy)
        } else {
            var totalField = findViewById<TextView>(R.id.totalView)
            totalField.text = "Total $sum"
        }
    }

    private fun addMyCreditTextBox(serialNo: Int, transaction: TransactionRecord, showConstraint: (TransactionRecord) -> Boolean, tabType: String): Double? {
        if(!showConstraint.invoke(transaction))
            return null

        var textColor = R.color.debitTextColorRow1
        var displayAmount = "0"

        if(Tabs.Singleton.instance.activeTab == Tabs.Singleton.instance.Tab_MySpent) {
            displayAmount = transaction.price
        } else {
            displayAmount = transaction.userDebit
        }

        if(tabType == Tabs.Singleton.instance.Tab_MySpent)
            textColor = R.color.creditTextColorRow1

        if(!isDebitTransaction(transaction))
            textColor = R.color.notInvolvedTextColorRow1

        val linearLayout = findViewById<LinearLayout>(R.id.cardContainers)

        val llv1 = LinearLayout(applicationContext)
        llv1.orientation = LinearLayout.VERTICAL
        llv1.setPadding(20, 10, 20, 10)

        var llv = LinearLayout(applicationContext)
        llv.orientation = LinearLayout.VERTICAL
        llv.setBackgroundResource(R.drawable.rounded_layout_red)

        llv1.addView(llv)

        var llh1 = LinearLayout(applicationContext)
        llh1.orientation = LinearLayout.HORIZONTAL

        var llh2 = LinearLayout(applicationContext)
        llh2.orientation = LinearLayout.HORIZONTAL
        llh2.gravity = Gravity.END
        llh2.setPadding(20, 0, 20, 0)

        var cardView = CardView(this)
        linearLayout.addView(cardView)

        var serialNoField = TextView(this)
        serialNoField.textSize=15F
        serialNoField.setTextColor(resources.getColor(textColor))
        serialNoField.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        serialNoField.setPadding(20, 0, 0, 0)

        var itemNameField = TextView(this)
        itemNameField.width=600
        itemNameField.textSize=15F
        itemNameField.setTextColor(resources.getColor(textColor))
        itemNameField.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        itemNameField.setPadding(20, 0, 20, 0)

        var sharedAmountField = TextView(this)
        sharedAmountField.width=407
        sharedAmountField.textSize=15F
        sharedAmountField.gravity = Gravity.END
        sharedAmountField.setTextColor(resources.getColor(textColor))
        sharedAmountField.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        sharedAmountField.setPadding(20, 0, 20, 0)


        var sharedBy = TextView(this)
        sharedBy.textSize = 12F
        sharedBy.setTextColor(resources.getColor(R.color.textColorCreator))
        sharedBy.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        sharedBy.setPadding(20, 10, 50, 0)

        var recordOriginDetailsField = TextView(this)
        recordOriginDetailsField.textSize = 12F
        recordOriginDetailsField.setTextColor(resources.getColor(R.color.textColorCreator))
        recordOriginDetailsField.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        recordOriginDetailsField.setPadding(20, 10, 0, 20)

        var priceLabel = TextView(this)
        priceLabel.textSize = 13F
        priceLabel.setTextColor(resources.getColor(R.color.textColorCreator))
        priceLabel.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        priceLabel.setPadding(0, 10, 0, 20)
        if(isCreditTransaction(transaction))
            priceLabel.setTextColor(resources.getColor(R.color.creditTextColorRow1))

        serialNoField.text = serialNo.toString() + "."
        itemNameField.text = transaction.item
        sharedAmountField.text = "Rs. " + displayAmount
        sharedBy.text = transaction.time + " . " + get1word(transaction.sharedBy)
        priceLabel.text = " . Rs " + transaction.price
        recordOriginDetailsField.text = "+ " + get1word(transaction.name) + " . " + transaction.time

        if (isCreditTransaction(transaction) && tabType==Tabs.Singleton.instance.Tab_MySpent) {
            sharedAmountField.setTextColor(resources.getColor(R.color.creditTextColorRow1))
        }

        llh1.addView(serialNoField)
        llh1.addView(itemNameField)
        llh1.addView(sharedAmountField)

        llh2.addView(recordOriginDetailsField)
        if(tabType != Tabs.Singleton.instance.Tab_MySpent)
            llh2.addView(priceLabel)


        llv.addView(llh1)
        llv.addView(sharedBy)
        llv.addView(llh2)
        linearLayout.addView(llv1)

        if(tabType == Tabs.Singleton.instance.Tab_MyTransaction)
            return transaction.userDebit.toDouble() - transaction.userCredit.toDouble()
        if(tabType == Tabs.Singleton.instance.Tab_MySpent)
            return transaction.userCredit.toDouble()
        if(tabType == Tabs.Singleton.instance.Tab_MyExpenses)
            return transaction.userDebit.toDouble()
        if(tabType == Tabs.Singleton.instance.Tab_showAll)
            return transaction.price.toDouble()
        return 0.0
    }

    fun doneDownloading() {
        println("Download Complete!")
    }

    fun get1word(str: String): String {
        var names: MutableList<String> = str.split(", ") as MutableList<String>
        var result = ""
        for(i:Int in 0..names.size-1) {
            if(i!=0)
                result+=", "
            result += names[i].split(" ")[0]
        }
        return result
    }

    val username = "Sudipta"

    private fun isCreditTransaction(transaction: TransactionRecord): Boolean {
        return  transaction.name.contains(username)
    }

    private fun isDebitTransaction(transaction: TransactionRecord): Boolean {
        return  transaction.sharedBy.contains(username) || transaction.sharedBy.contains("All")
    }

    fun showAll(transaction: TransactionRecord): Boolean {
        return true
    }

    fun isMyTransactions(transaction: TransactionRecord): Boolean {
        return  isCreditTransaction(transaction) || isDebitTransaction(transaction)
    }

    fun changeTab_showAll(view: View) {
        Tabs.Singleton.instance.activeTab = Tabs.Singleton.instance.Tab_showAll
        setTabFormatting(Tabs.Singleton.instance.Tab_showAll)
        displayCards()
    }

    fun setTabFormatting(activeTab: String) {

        var label1 = findViewById<TextView>(Tabs.Singleton.instance.tabViewID_showAll)
        var label2 = findViewById<TextView>(Tabs.Singleton.instance.tabViewID_MyTransaction)
        var label3 = findViewById<TextView>(Tabs.Singleton.instance.tabViewID_MyExpenses)
        var label4 = findViewById<TextView>(Tabs.Singleton.instance.tabViewID_MySpent)

        label1.setTextColor(resources.getColor(R.color.tabs_text_inactive))
        label2.setTextColor(resources.getColor(R.color.tabs_text_inactive))
        label3.setTextColor(resources.getColor(R.color.tabs_text_inactive))
        label4.setTextColor(resources.getColor(R.color.tabs_text_inactive))

        var activateLabel = label1
        if(activeTab == Tabs.Singleton.instance.Tab_showAll) {
            activateLabel = label1
        } else if(activeTab == Tabs.Singleton.instance.Tab_MyExpenses) {
            activateLabel = label3
        } else if(activeTab == Tabs.Singleton.instance.Tab_MySpent) {
            activateLabel = label4
        } else if(activeTab == Tabs.Singleton.instance.Tab_MyTransaction) {
            activateLabel = label2
        }

        activateLabel.setTextColor(resources.getColor(R.color.tabs_text_active))
    }

    fun changeTab_MyExpenses(view: View) {
        Tabs.Singleton.instance.activeTab = Tabs.Singleton.instance.Tab_MyExpenses
        setTabFormatting(Tabs.Singleton.instance.Tab_MyExpenses)
        displayCards()
    }

    fun changeTab_MySpent(view: View) {
        Tabs.Singleton.instance.activeTab = Tabs.Singleton.instance.Tab_MySpent
        setTabFormatting(Tabs.Singleton.instance.Tab_MySpent)
        displayCards()
    }

    fun changeTab_MyTransaction(view: View) {
        Tabs.Singleton.instance.activeTab = Tabs.Singleton.instance.Tab_MyTransaction
        setTabFormatting(Tabs.Singleton.instance.Tab_MyTransaction)
        displayCards()
    }

    fun transactionSort() {
        TransactionsManager.Singleton.instance.transactions.sortBy { t -> t.userDebit.toDouble() }
        TransactionsManager.Singleton.instance.transactions.sortBy { t -> t.userCredit.toDouble() }
        TransactionsManager.Singleton.instance.transactions.sortBy { t -> t.price.toDouble() }
        TransactionsManager.Singleton.instance.transactions.sortBy { t -> t.item.toLowerCase() }
//        TransactionsManager.Singleton.instance.transactions.sortBy { t -> t.createTime }
//        TransactionsManager.Singleton.instance.transactions.sortBy { t -> t.time }
    }
}
