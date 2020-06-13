package com.example.e203

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Environment
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.Gravity
import android.view.Gravity.END
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.e203.Utility.FileReadUtil
import com.example.e203.appData.FileManagerUtil
import com.example.e203.portable_utils.DownloadableFiles
import com.example.e203.sessionData.AppContext
import com.example.e203.sessionData.FetchedMetaData
import com.example.e203.sessionData.LocalConfig
import kotlinx.android.synthetic.main.activity_transactions_listing.*
import java.lang.Exception
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

    val priceType_CREDIT = "priceType_CREDIT"
    val priceType_DEBIT = "priceType_DEBIT"
    val priceType_TOTAL = "priceType_TOTAL"
    val priceType_NONE = "priceType_NONE"

    val label_DownloadingData = "Downloading Data..."


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transactions_listing)
        TransactionsManager.Singleton.instance.transactions.clear()
        setSupportActionBar(toolbar)
        setActionbarTextColor()
        AppContext.Singleton.instance.initialContext = this

        var breakdownSheet = DownloadableFiles(
            AppContext.Singleton.instance.initialContext,
            FetchedMetaData.Singleton.instance.getValue(FetchedMetaData.Singleton.instance.TAG_BREAKDOWN_URL)!!,
            AppContext.Singleton.instance.initialContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString(), "", "calculatingSheet.csv",
            "E203", "fetching metedata"
        )

        val linearLayout = findViewById<LinearLayout>(R.id.cardContainers)
        val sharedBy = TextView(this)
        sharedBy.text = label_DownloadingData
        sharedBy.setTextColor(resources.getColor(R.color.tabs_text_inactive))
        sharedBy.gravity = Gravity.CENTER
        sharedBy.setPadding(0,150,0,10)
        linearLayout.addView(sharedBy)

        println("breakdown sheet: " + breakdownSheet.serverURL)
        breakdownSheet.download(this, ::startDisplay)
    }

    var displayStarted = false
    private fun startDisplay() {
        TransactionsManager.Singleton.instance.transactions = mutableListOf()
        FileReadUtil.Singleton.instance.printCSVfile(fm.downloadLink_CalculatingSheet)
        TransactionsManager.Singleton.instance.transactions.reverse()
        Tabs.Singleton.instance.activeTab = Tabs.Singleton.instance.Tab_MyTransaction
        changeTab_MyTransaction(findViewById(R.id.cardContainers))
        displayStarted = true
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
                val linearLayout = findViewById<LinearLayout>(R.id.cardContainers)
                val sharedBy = TextView(this)
                if(displayStarted)
                    sharedBy.text = "No Transactions Found!"
                else
                    sharedBy.text = label_DownloadingData
                sharedBy.setTextColor(resources.getColor(R.color.tabs_text_inactive))
                sharedBy.gravity = Gravity.CENTER
                sharedBy.setPadding(0, 150, 0, 10)
                linearLayout.addView(sharedBy)
        }
            val totalField2 = findViewById<TextView>(R.id.totalView)
            totalField2.text = "Total :    ₹ ${round2Decimal(sum.toString())}    |    ${i-1} items"
            if(tabType == Tabs.Singleton.instance.Tab_MyTransaction)
                totalField2.text = "Total :    ₹ ${roundInt(sum.toString())}    |    ${i-1} items"


        var backgroundColor = resources.getColor(R.color.breakdown_tabsBackground)
        var textColor = resources.getColor(R.color.cardsColor_credit)
        when (tabType) {
            Tabs.Singleton.instance.Tab_showAll -> {
                textColor = resources.getColor(R.color.tabs_text_active)
            }
            Tabs.Singleton.instance.Tab_MyExpenses -> {
                textColor = resources.getColor(R.color.cardsColor_debit)
            }
            Tabs.Singleton.instance.Tab_MySpent -> {
                textColor = resources.getColor(R.color.cardsColor_credit)
            }
            Tabs.Singleton.instance.Tab_MyTransaction -> {
                textColor = if (sum > 0)
                    resources.getColor(R.color.cardsColor_debit)
                else
                    resources.getColor(R.color.cardsColor_credit)
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

        var textColor = R.color.cardsColor_debit

        val displayAmount: String = if (Tabs.Singleton.instance.activeTab == Tabs.Singleton.instance.Tab_MySpent) {
            transaction.price
        } else {
            transaction.userDebit
        }

        if(tabType == Tabs.Singleton.instance.Tab_MySpent)
            textColor = R.color.cardsColor_credit

        if(!isDebitTransaction(transaction))
            textColor = R.color.notInvolvedTextColorRow1

        val linearLayout = findViewById<LinearLayout>(R.id.cardContainers)

        val llv0 = LinearLayout(applicationContext)
        llv0.orientation = LinearLayout.VERTICAL
        llv0.setPadding(20, 10, 20, 10)

        val llv1 = LinearLayout(applicationContext)
        llv1.orientation = LinearLayout.VERTICAL
        llv1.setBackgroundResource(R.drawable.rounded_layout_red)

        llv0.addView(llv1)


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

        val price1 = TextView(this)
        price1.width=407
        price1.textSize=15F
        price1.gravity = END
//        price1.setTextColor(resources.getColor(textColor))
        price1.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        price1.setPadding(20, 0, 20, 0)

        val sharedBy = TextView(this)
        sharedBy.textSize = 12F
        sharedBy.width = 800
        sharedBy.alpha = 0.6F
        sharedBy.setTextColor(resources.getColor(R.color.textColorCreator))
        sharedBy.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        sharedBy.setPadding(20, 10, 50, 0)

        val recordOriginDetailsField = TextView(this)
        recordOriginDetailsField.textSize = 12F
        recordOriginDetailsField.setTextColor(resources.getColor(R.color.notInvolvedTextColorRow1))
        recordOriginDetailsField.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        recordOriginDetailsField.setPadding(20, 10, 0, 20)

        val price2 = TextView(this)
        price2.textSize = 12F
        price2.width=400
//        price2.setTextColor(resources.getColor(R.color.textColorCreator))
        price2.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        price2.gravity = END
        price2.alpha = 0.85F
        price2.setPadding(0, 0, 0, 0)

        serialNoField.text = "$serialNo."
        itemNameField.text = transaction.item
        showPrices_textNColor(price1, transaction, price1_getText(tabType, transaction))
        showPrices_textNColor(price2, transaction, price2_getText(tabType, transaction))
        sharedBy.text = transaction.time + " . " + get1word(transaction.sharedBy)
//        price2.text = price2_getText(tabType, transaction)
        recordOriginDetailsField.text = "+ " + get1word(transaction.name) + " . " + transaction.createTime.split(" ")[0]

//        if (isCreditTransaction(transaction) && tabType==Tabs.Singleton.instance.Tab_MySpent) {
//            price1.setTextColor(resources.getColor(R.color.cardsColor_credit))
//        }

        val llh1 = LinearLayout(applicationContext)
        val llh2 = LinearLayout(applicationContext)
        val llh3 = LinearLayout(applicationContext)
        llh1.orientation = LinearLayout.HORIZONTAL
        llh2.orientation = LinearLayout.HORIZONTAL
        llh3.orientation = LinearLayout.HORIZONTAL

        llh1.addView(serialNoField)
        llh1.addView(itemNameField)
        llh1.addView(price1)

        llh2.addView(sharedBy)
        llh2.addView(price2)

        llh3.addView(recordOriginDetailsField)

        llh3.gravity = END
        llh2.setPadding(20, 0, 20, 0)
        llh3.setPadding(20, 0, 20, 0)

        llv1.addView(llh1)
        llv1.addView(llh2)
        llv1.addView(llh3)

        llv0.setOnClickListener {
            if(isCreditTransaction(transaction)) {
                Toast.makeText(this, "editable", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Non - editable", Toast.LENGTH_LONG).show()
            }
        }
        linearLayout.addView(llv0)

        serialNoField.setTextColor(getColor_text1(tabType, transaction))
        itemNameField.setTextColor(getColor_text1(tabType, transaction))

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

    private val username = LocalConfig.Singleton.instance.getValue("username")!!

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

    private fun getColor_text1(tabType: String, transaction: TransactionRecord): Int {
        if(!isCreditTransaction(transaction) && !isDebitTransaction(transaction)) {
            return resources.getColor(R.color.notInvolvedTextColorRow1)
        }
        when (tabType) {
            Tabs.Singleton.instance.Tab_showAll -> {
                return resources.getColor(R.color.cardsColor_white)
            }
            Tabs.Singleton.instance.Tab_MyExpenses -> {
                return resources.getColor(R.color.cardsColor_debit)
            }
            Tabs.Singleton.instance.Tab_MySpent -> {
                return resources.getColor(R.color.cardsColor_credit)
            }
            Tabs.Singleton.instance.Tab_MyTransaction -> {
                return resources.getColor(R.color.cardsColor_white)
            }
        }
        return resources.getColor(R.color.cardsColor_white)
    }

    private fun price1_getText(tabType: String, transaction: TransactionRecord): String {
        when (tabType) {
            Tabs.Singleton.instance.Tab_showAll -> {
                if(isCreditTransaction(transaction))
                    return priceType_CREDIT
                return priceType_TOTAL
            }
            Tabs.Singleton.instance.Tab_MyExpenses -> {
                return priceType_DEBIT
            }
            Tabs.Singleton.instance.Tab_MySpent -> {
                return priceType_CREDIT
            }
            Tabs.Singleton.instance.Tab_MyTransaction -> {
                return priceType_DEBIT
            }
        }
        return priceType_TOTAL
    }

    private fun price2_getText(tabType: String, transaction: TransactionRecord): String {
        when (tabType) {
            Tabs.Singleton.instance.Tab_showAll -> {
                return priceType_DEBIT
            }
            Tabs.Singleton.instance.Tab_MyExpenses -> {
                if(isCreditTransaction(transaction))
                    return priceType_CREDIT
                return priceType_TOTAL
            }
            Tabs.Singleton.instance.Tab_MySpent -> {
                return priceType_DEBIT
            }
            Tabs.Singleton.instance.Tab_MyTransaction -> {
                if(isCreditTransaction(transaction))
                    return priceType_CREDIT
                return priceType_TOTAL
            }
        }
        return priceType_TOTAL
    }

    private fun showPrices_textNColor(textView: TextView, transaction: TransactionRecord, priceType: String) {
        val pre = "₹ "
        when (priceType) {
            priceType_CREDIT -> {
                textView.text = pre + round2Decimal(transaction.userCredit)
                textView.setTextColor(resources.getColor(R.color.cardsColor_credit))
            }
            priceType_DEBIT -> {
                textView.text = pre + round2Decimal(transaction.userDebit)
                if(isDebitTransaction(transaction))
                    textView.setTextColor(resources.getColor(R.color.cardsColor_debit))
                else {
                    textView.setTextColor(resources.getColor(R.color.notInvolvedTextColorRow1))
                }
            }
            priceType_TOTAL -> {
                textView.text = pre + round2Decimal(transaction.price)
                textView.setTextColor(resources.getColor(R.color.notInvolvedTextColorRow1))
            }
            priceType_NONE -> {
                textView.text = ""
            }
        }
    }

    @Suppress("DEPRECATION")
    private fun setActionbarTextColor() {
        val title = ""
        val spannableTitle: Spannable = SpannableString("")
        spannableTitle.setSpan(
            ForegroundColorSpan(Color.BLACK),
            0,
            spannableTitle.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        supportActionBar!!.title = title
        window.statusBarColor = resources.getColor(R.color.colorPrimaryDark)
        supportActionBar!!.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.colorPrimary)))

        findViewById<TextView>(R.id.toolbar_Text1).text = "Transactions"
        try {
            var user = LocalConfig.Singleton.instance.getValue(LocalConfig.Singleton.instance.USERNAME)
            if (user!!.isNotEmpty())
                findViewById<TextView>(R.id.toolbar_Text2).text = "- " + user
        } catch (e: Exception) {
            findViewById<TextView>(R.id.toolbar_Text2).text = "Anonymous"
        }
    }
}
