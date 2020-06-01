package com.example.e203

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import android.view.Gravity
import android.view.Gravity.END
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.e203.Utility.FileReadUtil
import com.example.e203.sessionData.AppContext
import kotlinx.android.synthetic.main.activity_transactions_listing.*
import java.math.RoundingMode
import java.text.DecimalFormat
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

@Suppress("DEPRECATION")
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

        TransactionsManager.Singleton.instance.transactions.reverse()
        Tabs.Singleton.instance.activeTab = Tabs.Singleton.instance.Tab_MyTransaction
        changeTab_MyTransaction(findViewById(R.id.cardContainers))
    }

    @SuppressLint("SetTextI18n")
    private fun displayCards() {
        var i=1
        val tabType = Tabs.Singleton.instance.activeTab
        transactionSort()

        val linearLayout = findViewById<LinearLayout>(R.id.cardContainers)
        linearLayout.removeAllViews()

        var showMethod = ::showAll

        when (tabType) {
            Tabs.Singleton.instance.Tab_showAll -> {
                showMethod = ::showAll
            }
            Tabs.Singleton.instance.Tab_MyExpenses -> {
                showMethod = ::isDebitTransaction
            }
            Tabs.Singleton.instance.Tab_MySpent -> {
                showMethod = ::isCreditTransaction
            }
            Tabs.Singleton.instance.Tab_MyTransaction -> {
                showMethod = ::isMyTransactions
            }
        }

        var sum = 0.0
        for(z:Int in 0 until TransactionsManager.Singleton.instance.transactions.size) {
            val result = addMyCreditTextBox(
                i,
                TransactionsManager.Singleton.instance.transactions[z],
                showMethod,
                tabType
            )
            if (result != null){
                i++
                sum = result.plus(sum)
            }
        }

        if(i==1) {
            Toast.makeText(this, "No Transaction Found", Toast.LENGTH_LONG).show()
            val linearLayout = findViewById<LinearLayout>(R.id.cardContainers)
            val sharedBy = TextView(this)
            sharedBy.text = "No Transactions Found!"
            sharedBy.setTextColor(resources.getColor(R.color.tabs_text_inactive))
            sharedBy.gravity = Gravity.CENTER
            sharedBy.setPadding(0,150,0,10)
            linearLayout.addView(sharedBy)
        } else {
            val totalField = findViewById<TextView>(R.id.totalView)
            totalField.text = "Total :    Rs. ${round2Decimal(sum.toString())}      (${i-1} items)"
            if(tabType == Tabs.Singleton.instance.Tab_MyTransaction)
                totalField.text = "Total :    Rs. ${roundInt(sum.toString())}      (${i-1} items)"
        }

        var backgroundColor = resources.getColor(R.color.breakdown_tabsBackground)
        var textColor = resources.getColor(R.color.creditTextColorRow1)
        when (tabType) {
            Tabs.Singleton.instance.Tab_showAll -> {
                textColor = resources.getColor(R.color.debitTextColorRow1)
            }
            Tabs.Singleton.instance.Tab_MyExpenses -> {
                textColor = resources.getColor(R.color.debitTextColorRow1)
            }
            Tabs.Singleton.instance.Tab_MySpent -> {
                textColor = resources.getColor(R.color.creditTextColorRow1)
            }
            Tabs.Singleton.instance.Tab_MyTransaction -> {
                textColor = if (sum >= 0)
                    resources.getColor(R.color.creditTextColorRow1)
                else
                    resources.getColor(R.color.debitTextColorRow1)
            }
        }
        val totalField = findViewById<TextView>(R.id.totalView)
        val spanString = SpannableString(totalField.text)
        spanString.setSpan(StyleSpan(Typeface.BOLD), 0, spanString.length, 0)
        totalField.text = spanString
        totalField.setTextColor(textColor)
        totalField.setBackgroundColor(backgroundColor)
    }

    @SuppressLint("SetTextI18n")
    private fun addMyCreditTextBox(serialNo: Int, transaction: TransactionRecord, showConstraint: (TransactionRecord) -> Boolean, tabType: String): Double? {
        if(!showConstraint.invoke(transaction))
            return null

        var textColor = R.color.debitTextColorRow1

        val displayAmount: String = if (Tabs.Singleton.instance.activeTab == Tabs.Singleton.instance.Tab_MySpent) {
            transaction.price
        } else {
            transaction.userDebit
        }

        if(tabType == Tabs.Singleton.instance.Tab_MySpent)
            textColor = R.color.creditTextColorRow1

        if(!isDebitTransaction(transaction))
            textColor = R.color.notInvolvedTextColorRow1

        val linearLayout = findViewById<LinearLayout>(R.id.cardContainers)

        val llv1 = LinearLayout(applicationContext)
        llv1.orientation = LinearLayout.VERTICAL
        llv1.setPadding(20, 10, 20, 10)

        val llv = LinearLayout(applicationContext)
        llv.orientation = LinearLayout.VERTICAL
        llv.setBackgroundResource(R.drawable.rounded_layout_red)

        llv1.addView(llv)

        val llh1 = LinearLayout(applicationContext)
        llh1.orientation = LinearLayout.HORIZONTAL

        val llh2 = LinearLayout(applicationContext)
        llh2.orientation = LinearLayout.HORIZONTAL
        llh2.gravity = END
        llh2.setPadding(20, 0, 20, 0)

        val cardView = CardView(this)
        linearLayout.addView(cardView)

        val serialNoField = TextView(this)
        serialNoField.textSize=15F
        serialNoField.setTextColor(resources.getColor(textColor))
        serialNoField.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        serialNoField.setPadding(20, 0, 0, 0)

        val itemNameField = TextView(this)
        itemNameField.width=600
        itemNameField.textSize=15F
        itemNameField.setTextColor(resources.getColor(textColor))
        itemNameField.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        itemNameField.setPadding(20, 0, 20, 0)

        val sharedAmountField = TextView(this)
        sharedAmountField.width=407
        sharedAmountField.textSize=15F
        sharedAmountField.gravity = END
        sharedAmountField.setTextColor(resources.getColor(textColor))
        sharedAmountField.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        sharedAmountField.setPadding(20, 0, 20, 0)

        val sharedBy = TextView(this)
        sharedBy.textSize = 12F
        sharedBy.setTextColor(resources.getColor(R.color.textColorCreator))
        sharedBy.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        sharedBy.setPadding(20, 10, 50, 0)

        val recordOriginDetailsField = TextView(this)
        recordOriginDetailsField.textSize = 12F
        recordOriginDetailsField.setTextColor(resources.getColor(R.color.textColorCreator))
        recordOriginDetailsField.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        recordOriginDetailsField.setPadding(20, 10, 0, 20)

        val priceLabel = TextView(this)
        priceLabel.textSize = 13F
        priceLabel.setTextColor(resources.getColor(R.color.textColorCreator))
        priceLabel.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        priceLabel.setPadding(0, 10, 0, 20)
        if(isCreditTransaction(transaction))
            priceLabel.setTextColor(resources.getColor(R.color.creditTextColorRow1))

        serialNoField.text = "$serialNo."
        itemNameField.text = transaction.item
        sharedAmountField.text = "Rs. " + round2Decimal(displayAmount)
        sharedBy.text = transaction.time + " . " + get1word(transaction.sharedBy)
        priceLabel.text = " . Rs " + transaction.price
        recordOriginDetailsField.text = "+ " + get1word(transaction.name) + " . " + transaction.createTime

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

    private fun doneDownloading() {
        println("Download Complete!")
    }

    private fun get1word(str: String): String {
        val names: MutableList<String> = str.split(", ") as MutableList<String>
        var result = ""
        for(i:Int in 0 until names.size) {
            if(i!=0)
                result+=", "
            result += names[i].split(" ")[0]
        }
        return result
    }

    private val username = "Prasun"

    private fun isCreditTransaction(transaction: TransactionRecord): Boolean {
        return  transaction.name.contains(username)
    }

    private fun isDebitTransaction(transaction: TransactionRecord): Boolean {
        return  transaction.sharedBy.contains(username) || transaction.sharedBy.contains("All")
    }

    private fun showAll(transaction: TransactionRecord): Boolean {
        return true
    }

    private fun isMyTransactions(transaction: TransactionRecord): Boolean {
        return  isCreditTransaction(transaction) || isDebitTransaction(transaction)
    }

    fun changeTab_showAll(view: View) {
        Tabs.Singleton.instance.activeTab = Tabs.Singleton.instance.Tab_showAll
        setTabFormatting(Tabs.Singleton.instance.Tab_showAll)
        displayCards()
    }

    private fun setTabFormatting(activeTab: String) {
        val label1 = findViewById<TextView>(Tabs.Singleton.instance.tabViewID_showAll)
        val label2 = findViewById<TextView>(Tabs.Singleton.instance.tabViewID_MyTransaction)
        val label3 = findViewById<TextView>(Tabs.Singleton.instance.tabViewID_MyExpenses)
        val label4 = findViewById<TextView>(Tabs.Singleton.instance.tabViewID_MySpent)

        label1.setTextColor(resources.getColor(R.color.tabs_text_inactive))
        label2.setTextColor(resources.getColor(R.color.tabs_text_inactive))
        label3.setTextColor(resources.getColor(R.color.tabs_text_inactive))
        label4.setTextColor(resources.getColor(R.color.tabs_text_inactive))

        var activateLabel = label1
        when (activeTab) {
            Tabs.Singleton.instance.Tab_showAll -> {
                activateLabel = label1
            }
            Tabs.Singleton.instance.Tab_MyExpenses -> {
                activateLabel = label3
            }
            Tabs.Singleton.instance.Tab_MySpent -> {
                activateLabel = label4
            }
            Tabs.Singleton.instance.Tab_MyTransaction -> {
                activateLabel = label2
            }
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

    private fun transactionSort() {
//        TransactionsManager.Singleton.instance.transactions.sortBy { t -> t.userDebit.toDouble() }
//        TransactionsManager.Singleton.instance.transactions.sortBy { t -> t.userCredit.toDouble() }
//        TransactionsManager.Singleton.instance.transactions.sortBy { t -> t.price.toDouble() }
//        TransactionsManager.Singleton.instance.transactions.sortBy { t -> t.item.toLowerCase() }
//        TransactionsManager.Singleton.instance.transactions.sortBy { t -> t.createTime }
//        TransactionsManager.Singleton.instance.transactions.sortBy { t -> t.time }
    }

    private fun round2Decimal(st: String): String {
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.CEILING
        return df.format(st.toDouble())
    }

    private fun roundInt(st: String): String {
        val df = DecimalFormat("#")
        df.roundingMode = RoundingMode.CEILING
        return df.format(st.toDouble())
    }
}
