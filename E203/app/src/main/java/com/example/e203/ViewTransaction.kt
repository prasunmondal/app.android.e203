package com.example.e203

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.e203.sessionData.LocalConfig.Singleton.instance as lc

import kotlinx.android.synthetic.main.activity_view_transaction.*
import java.math.RoundingMode
import java.text.DecimalFormat

class ViewTransaction : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_transaction)
        setSupportActionBar(toolbar)
        setActionbarTextColor()

        findViewById<TextView>(R.id.details_itemname).text = "Item Name: " + lc.viewTransaction.item

        findViewById<TextView>(R.id.details_qty).text = "Quantity: " + lc.viewTransaction.qty

        findViewById<TextView>(R.id.details_totalPrice).text = "Total Price: ₹ " + lc.viewTransaction.price

        findViewById<TextView>(R.id.details_sharedBy).text = "Shared By: " + get1word(lc.viewTransaction.sharedBy)

        findViewById<TextView>(R.id.details_addedBy).text = "added: " + lc.viewTransaction.name + "  (" + lc.viewTransaction.createTime + ")"

        findViewById<TextView>(R.id.details_credit).text = "Your Credit: ₹ " + lc.viewTransaction.userCredit
        findViewById<TextView>(R.id.details_credit).setTextColor(resources.getColor(R.color.cardsColor_credit))

        findViewById<TextView>(R.id.details_debit).text = "Your Debit: ₹ " + round2Decimal(lc.viewTransaction.userDebit)
        findViewById<TextView>(R.id.details_debit).setTextColor(resources.getColor(R.color.cardsColor_debit))
    }

    private fun round2Decimal(st: String): String {
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.CEILING
        return df.format(st.toDouble())
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

        findViewById<TextView>(R.id.toolbar_Text1).text = "E203"
        findViewById<TextView>(R.id.toolbar_Text2).text = "Transaction details"
    }
}