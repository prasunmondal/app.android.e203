package com.example.e203.SheetUtils

import android.content.Context
import android.text.TextUtils
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Response
import com.android.volley.RetryPolicy
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import java.util.HashMap

class PostToSheet() {

    fun post(context: Context, scriptID: String, spreadsheetURL: String, sheetName: String, list: List<String>) {
        var sendString = TextUtils.join("◔", list)

        val stringRequest: StringRequest =
            object : StringRequest(
                Method.POST, scriptID,
                Response.Listener { },
                Response.ErrorListener {}
            ) {
                override fun getParams(): Map<String, String> {
                    val params: MutableMap<String, String> =
                        HashMap()
                    //here we pass params
                    params["action"] = "addItem"
                    params["spreadsheetURL"] = spreadsheetURL
                    params["sheetName"] = sheetName
                    params["text"] = sendString
                    return params
                }
            }
        val socketTimeOut = 120000 // u can change this .. here it is 120 seconds
        val retryPolicy: RetryPolicy =
            DefaultRetryPolicy(socketTimeOut, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        stringRequest.retryPolicy = retryPolicy
        val queue = Volley.newRequestQueue(context)
        queue.add(stringRequest)
    }
}



/*

Sheet Script:


var ss = SpreadsheetApp.openByUrl("https://docs.google.com/spreadsheets/d/1qacLjDP01fA5xxo1RNI9oGDyP6iknMQyIOPx24brJlA/edit#gid=0");

var sheet = ss.getSheetByName('Items'); // be very careful ... it is the sheet name .. so it should match

function doPost(e){
  var action = e.parameter.action;
  if(action == 'addItem'){
    return addItem(e);
  }
}

function addItem(e){
  var date =  new Date();
  var text = e.parameter.text;
  var texts = [{}];

  texts = [date].concat(text.split("◔"));
  sheet.appendRow(texts);

  return ContentService.createTextOutput("Success").setMimeType(ContentService.MimeType.TEXT);
}


 */