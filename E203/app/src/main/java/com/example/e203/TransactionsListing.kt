package com.example.e203

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.e203.Utility.FileReadUtil
import com.example.e203.appData.FileManagerUtil.Singleton.instance as fm
import com.example.e203.sessionData.AppContext

import kotlinx.android.synthetic.main.activity_transactions_listing.*

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

class TransactionsListing : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transactions_listing)
        setSupportActionBar(toolbar)
        AppContext.Singleton.instance.initialContext = this

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        if(!fm.breakdownSheet.doesExist())
            fm.breakdownSheet.download(::doneDownloading)
        else
            FileReadUtil.Singleton.instance.printCSVfile(fm.downloadLink_CalculatingSheet)
        var i = 1

        for(z:Int in 0..TransactionsManager.Singleton.instance.transactions.size-1) {
            if(TransactionsManager.Singleton.instance.transactions[z].userDebit.isNotEmpty()) {
                addDebitTextBox(i++, TransactionsManager.Singleton.instance.transactions[z])
            }
        }

        i = 1

        for(z:Int in 1..TransactionsManager.Singleton.instance.transactions.size-1) {
            if(TransactionsManager.Singleton.instance.transactions[z].userCredit.isNotEmpty()) {
                addCreditTextBox(i++, TransactionsManager.Singleton.instance.transactions[z])
            }
        }
    }

    private fun addCreditTextBox(serialNo: Int, transaction: TransactionRecord) {
        var backgroundColor =  "#b7e1cd"
        var textColor = "#134f5c"

        var linearLayout = findViewById<LinearLayout>(R.id.cardContainers)

        var llv1 = LinearLayout(applicationContext)
        llv1.orientation = LinearLayout.VERTICAL
        llv1.setPadding(20, 10, 20, 10)

        var llv = LinearLayout(applicationContext)
        llv.orientation = LinearLayout.VERTICAL
        llv.setBackgroundResource(R.drawable.rounded_layout_green)

        llv1.addView(llv)

        var llh1 = LinearLayout(applicationContext)
        llh1.orientation = LinearLayout.HORIZONTAL
        llh1.setBackgroundColor(Color.parseColor("#f4cccc"))
        llh1.setBackgroundColor(Color.parseColor(backgroundColor))

        var llh2 = LinearLayout(applicationContext)
        llh2.setBackgroundColor(Color.parseColor("#f4cccc"))
        llh2.setBackgroundColor(Color.parseColor(backgroundColor))
        llh2.orientation = LinearLayout.HORIZONTAL
        llh2.gravity = Gravity.END

        var cardView = CardView(this)
        linearLayout.addView(cardView)

        var serialNoField = TextView(this)
        serialNoField.text = serialNo.toString() + "."
        serialNoField.textSize=15F
        serialNoField.setTextColor(Color.parseColor(textColor))

        serialNoField.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        serialNoField.setPadding(20, 20, 0, 0)// in pixels (left, top, right, bottom)

        var itemNameField = TextView(this)
        itemNameField.text = transaction.item
        itemNameField.width=600
        itemNameField.textSize=15F
        itemNameField.setTextColor(Color.parseColor(textColor))

        itemNameField.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        itemNameField.setPadding(20, 20, 20, 0)// in pixels (left, top, right, bottom)

        var sharedAmountField = TextView(this)
        sharedAmountField.text = "Rs. " + transaction.userCredit
        sharedAmountField.width=407
        sharedAmountField.textSize=15F
        sharedAmountField.gravity = Gravity.END
        sharedAmountField.setTextColor(Color.parseColor(textColor))

        sharedAmountField.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        sharedAmountField.setPadding(20, 20, 20, 0)// in pixels (left, top, right, bottom)

        var recordOriginDetailsField = TextView(this)
        recordOriginDetailsField.text = "+" + transaction.name + " - " + transaction.time + " - Rs. " + transaction.price
        recordOriginDetailsField.textSize = 12F

        recordOriginDetailsField.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        recordOriginDetailsField.setPadding(20, 20, 20, 20)// in pixels (left, top, right, bottom)



        llh1.addView(serialNoField)
        llh1.addView(itemNameField)
        llh1.addView(sharedAmountField)

        llh2.addView(recordOriginDetailsField)

        llv.addView(llh1)
        llv.addView(llh2)
        linearLayout.addView(llv1)
    }

    private fun addDebitTextBox(serialNo: Int, transaction: TransactionRecord) {
        var backgroundColor =  "#f4cccc"
        var textColor = "#990000"

        var linearLayout = findViewById<LinearLayout>(R.id.cardContainers)

        var llv1 = LinearLayout(applicationContext)
        llv1.orientation = LinearLayout.VERTICAL
        llv1.setPadding(20, 10, 20, 10)

        var llv = LinearLayout(applicationContext)
        llv.orientation = LinearLayout.VERTICAL
        llv.setBackgroundResource(R.drawable.rounded_layout_red)

        llv1.addView(llv)

        var llh1 = LinearLayout(applicationContext)
        llh1.orientation = LinearLayout.HORIZONTAL
        llh1.setBackgroundColor(Color.parseColor(backgroundColor))

        var llh2 = LinearLayout(applicationContext)
        llh2.setBackgroundColor(Color.parseColor(backgroundColor))
        llh2.orientation = LinearLayout.HORIZONTAL
        llh2.gravity = Gravity.END

        var cardView = CardView(this)
        linearLayout.addView(cardView)

        var serialNoField = TextView(this)
        serialNoField.text = serialNo.toString() + "."
        serialNoField.textSize=15F
        serialNoField.setTextColor(Color.parseColor(textColor))

        serialNoField.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        serialNoField.setPadding(20, 20, 0, 0)// in pixels (left, top, right, bottom)

        var itemNameField = TextView(this)
        itemNameField.text = transaction.item
        itemNameField.width=600
        itemNameField.textSize=15F
        itemNameField.setTextColor(Color.parseColor(textColor))

        itemNameField.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        itemNameField.setPadding(20, 20, 20, 0)// in pixels (left, top, right, bottom)

        var sharedAmountField = TextView(this)
        sharedAmountField.text = "Rs. " + transaction.userDebit
        sharedAmountField.width=400
        sharedAmountField.textSize=15F
        sharedAmountField.gravity = Gravity.END
        sharedAmountField.setTextColor(Color.parseColor(textColor))

        sharedAmountField.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        sharedAmountField.setPadding(20, 20, 20, 0)// in pixels (left, top, right, bottom)

        var recordOriginDetailsField = TextView(this)
        recordOriginDetailsField.text = "+" + transaction.name + " - " + transaction.time + " - Rs. " + transaction.price
        recordOriginDetailsField.textSize = 12F

        recordOriginDetailsField.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        recordOriginDetailsField.setPadding(20, 20, 20, 20)// in pixels (left, top, right, bottom)



        llh1.addView(serialNoField)
        llh1.addView(itemNameField)
        llh1.addView(sharedAmountField)

        llh2.addView(recordOriginDetailsField)

        llv.addView(llh1)
        llv.addView(llh2)
        linearLayout.addView(llv1)
    }

    fun doneDownloading() {
        println("Download Complete!")
    }
}
