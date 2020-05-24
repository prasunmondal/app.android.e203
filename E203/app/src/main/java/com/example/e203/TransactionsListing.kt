package com.example.e203

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.marginBottom

import kotlinx.android.synthetic.main.activity_transactions_listing.*

class TransactionsListing : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transactions_listing)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        var i: Int = 1
        addDebitTextBox(i++,
            "Chicken (16-May)",
            (Math.random()*10000).toInt(),
            "Prasun",
            8907,
            "15/03/2020",
            "12/05/2020", "credit"
        )

        addDebitTextBox(i++,
            "Mutton",
            (Math.random()*10000).toInt(),
            "Prasun",
            8907,
            "15/03/2020",
            "12/05/2020", "debit"
        )

        addDebitTextBox(i++,
            "Hair Band",
            (Math.random()*10000).toInt(),
            "Prasun",
            8907,
            "15/03/2020",
            "12/05/2020", "debit"
        )

        addDebitTextBox(i++,
            "Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name",
            (Math.random()*10000).toInt(),
            "Prasun",
            8907,
            "15/03/2020",
            "12/05/2020", "debit"
        )

        addDebitTextBox(i++,
            "Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name",
            (Math.random()*10000).toInt(),
            "Prasun",
            8907,
            "15/03/2020",
            "12/05/2020", "debit"
        )

        addDebitTextBox(i++,
            "Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name",
            (Math.random()*10000).toInt(),
            "Prasun",
            8907,
            "15/03/2020",
            "12/05/2020", "debit"
        )

        addDebitTextBox(i++,
            "Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name",
            (Math.random()*10000).toInt(),
            "Prasun",
            8907,
            "15/03/2020",
            "12/05/2020", "debit"
        )
        addDebitTextBox(i++,
            "Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name",
            (Math.random()*10000).toInt(),
            "Prasun",
            8907,
            "15/03/2020",
            "12/05/2020", "debit"
        )

        addDebitTextBox(i++,
            "Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name",
            (Math.random()*10000).toInt(),
            "Prasun",
            8907,
            "15/03/2020",
            "12/05/2020", "debit"
        )

        addDebitTextBox(i++,
            "Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name",
            (Math.random()*10000).toInt(),
            "Prasun",
            8907,
            "15/03/2020",
            "12/05/2020", "debit"
        )
        addDebitTextBox(i++,
            "Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name",
            (Math.random()*10000).toInt(),
            "Prasun",
            8907,
            "15/03/2020",
            "12/05/2020", "debit"
        )

        addDebitTextBox(i++,
            "Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name",
            (Math.random()*10000).toInt(),
            "Prasun",
            8907,
            "15/03/2020",
            "12/05/2020", "debit"
        )

        addDebitTextBox(i++,
            "Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name",
            (Math.random()*10000).toInt(),
            "Prasun",
            8907,
            "15/03/2020",
            "12/05/2020", "debit"
        )
        addDebitTextBox(i++,
            "Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name",
            (Math.random()*10000).toInt(),
            "Prasun",
            8907,
            "15/03/2020",
            "12/05/2020", "debit"
        )

        addDebitTextBox(i++,
            "Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name",
            (Math.random()*10000).toInt(),
            "Prasun",
            8907,
            "15/03/2020",
            "12/05/2020", "debit"
        )

        addDebitTextBox(i++,
            "Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name",
            (Math.random()*10000).toInt(),
            "Prasun",
            8907,
            "15/03/2020",
            "12/05/2020", "debit"
        )
        addDebitTextBox(i++,
            "Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name",
            (Math.random()*10000).toInt(),
            "Prasun",
            8907,
            "15/03/2020",
            "12/05/2020", "debit"
        )

        addDebitTextBox(i++,
            "Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name",
            (Math.random()*10000).toInt(),
            "Prasun",
            8907,
            "15/03/2020",
            "12/05/2020", "debit"
        )

        addDebitTextBox(i++,
            "Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name Item Name",
            (Math.random()*10000).toInt(),
            "Prasun",
            8907,
            "15/03/2020",
            "12/05/2020", "debit"
        )
    }

    private fun addDebitTextBox(serialNo: Int, itemName: String, sharedAmount: Int, addedBy: String, totalAmount: Int, addedOn: String, addedForDate: String, transactionType: String) {
        var linearLayout = findViewById<LinearLayout>(R.id.cardContainers)

        var llv1 = LinearLayout(applicationContext)
        llv1.orientation = LinearLayout.VERTICAL
        llv1.setPadding(20, 30, 20, 30)

        var llv = LinearLayout(applicationContext)
        llv.orientation = LinearLayout.VERTICAL
        llv.setBackgroundResource(R.drawable.rounded_layout)
        llv.setBackgroundColor(Color.parseColor("#000000"))

        llv1.addView(llv)

        var llh1 = LinearLayout(applicationContext)
        llh1.orientation = LinearLayout.HORIZONTAL
        llh1.setBackgroundColor(Color.parseColor("#f4cccc"))

        var llh2 = LinearLayout(applicationContext)
        llh2.setBackgroundColor(Color.parseColor("#f4cccc"))
        llh2.orientation = LinearLayout.HORIZONTAL
        llh2.gravity = Gravity.END

        var cardView = CardView(this)
        linearLayout.addView(cardView)

        var serialNoField = TextView(this)
        serialNoField.text = serialNo.toString() + "."
        serialNoField.textSize=15F
        serialNoField.setTextColor(Color.parseColor("#990000"))

        serialNoField.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        serialNoField.setPadding(20, 20, 0, 0)// in pixels (left, top, right, bottom)

        var itemNameField = TextView(this)
        itemNameField.text = itemName
        itemNameField.width=707
        itemNameField.textSize=15F
        itemNameField.setTextColor(Color.parseColor("#990000"))

        itemNameField.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        itemNameField.setPadding(20, 20, 20, 0)// in pixels (left, top, right, bottom)

        var sharedAmountField = TextView(this)
        sharedAmountField.text = "Rs. " + sharedAmount.toString()
        sharedAmountField.width=307
        sharedAmountField.textSize=15F
        sharedAmountField.gravity = Gravity.END
        sharedAmountField.setTextColor(Color.parseColor("#990000"))

        sharedAmountField.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        sharedAmountField.setPadding(20, 20, 20, 0)// in pixels (left, top, right, bottom)

        var recordOriginDetailsField = TextView(this)
        recordOriginDetailsField.text = "+" + addedBy + " - " + addedForDate + " - Rs. " + totalAmount
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
}
