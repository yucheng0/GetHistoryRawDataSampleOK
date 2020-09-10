package com.example.getclubapiexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.squareup.okhttp.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import java.lang.Exception

const val LOGINAPIURL = "http://202.88.100.249/SAWELLPlus_club/php/Login_server.php"
const val GETRAWDATAAPIURL = "http://202.88.100.249/SAWELLPlus_club/php/get_history_raw_data.php"
const val ACCOUNT1 = "w06@gmail.com"
const val PASSWORD1 = "w"
const val ACCOUNT2 = "sagym2f@sportsart.com.tw"
const val PASSWORD2 = "sagym2f"
const val ACCOUNT3 = "yjusesg02@gmail.com"
const val PASSWORD3 = "sa0000"
const val ACCOUNT4 = "yjusesg01@gmail.com"
const val PASSWORD4 = "sa1111"

class MainActivity : AppCompatActivity() {
    var phpsessid = ""
    var client = OkHttpClient()                            //要一個實例
    var JSON = MediaType.parse("application/json; charset=utf-8")
   // var body = RequestBody.create(JSON, "{\"account\":\"w06\" ,\"pw\":\"w\" , \"timezone\":8 }")
    lateinit var request: Request
    lateinit var response: Response
    lateinit var bodyData: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        radioButton1.isChecked = true
        keyProc()
    }


    //keyProc
    fun keyProc() {
        //        按鍵
        login.setOnClickListener {
            post_login()
        }
        btnGetRawData.setOnClickListener {
            post_login()
            post_3rd_get_history_raw_data()
        }

        // radio 監控
        radioButton1.setOnClickListener {
            println ("button1")
            //load init value
            editTextAccount.setText(ACCOUNT1)
            editTextPassword.setText(PASSWORD1)
        }

        radioButton2.setOnClickListener {
            println ("button2")
            //load init value
            editTextAccount.setText(ACCOUNT2)
            editTextPassword.setText(PASSWORD2)
        }

        radioButton3.setOnClickListener {
            //load init value
            println ("button3")
            editTextAccount.setText(ACCOUNT3)
            editTextPassword.setText(PASSWORD3)
        }

        radioButton4.setOnClickListener {
            //load init value
            println ("button4")
            editTextAccount.setText(ACCOUNT4)
            editTextPassword.setText(PASSWORD4)
        }

    }

//================================================================================

    //  副程式在此

    fun post_login() {
        GlobalScope.launch(Dispatchers.IO) {
            //要使用Default,unconfined,IO , 用Main會當機, 奇怪
            val url = LOGINAPIURL

            client = OkHttpClient()                            //要一個實例
            JSON = MediaType.parse("application/json; charset=utf-8")

            val ACCOUNT = editTextAccount.text.toString()
            val PASSWORD = editTextPassword.text.toString()
            val jsondata = """{  "account":"$ACCOUNT",
                                 "pw": "$PASSWORD" ,
                                 "timezone":8
                                }"""


                //          body = RequestBody.create(JSON, "{\"account\":\"w06\" ,\"pw\":\"w\" , \"timezone\":8 }")
            val body = RequestBody.create(JSON, jsondata)
            request = Request.Builder()                    //建立需求
                .url(url)
               .post(body)
                .build()

            try {
                response = client.newCall(request).execute()            // 取得回應到response 來
                phpsessid = response.header("Set-Cookie")  //PHPSESSID
                bodyData = response.body()!!.string()
                //      println(bodyData)
                //     println(phpsessid)
                runOnUiThread {
      //              textView.text = "${bodyData} \n ${phpsessid}"
                    textView.text = "${bodyData}"
                }
            } catch (e: Exception) {
                println("Error!!!!")

            }
        }
    }

    //=================================================================
    fun post_3rd_get_history_raw_data() {
        GlobalScope.launch(Dispatchers.Default) {
            delay(1000)            //不加delay 好像不行 , 網路備好時間(min=300ms)
            val url = GETRAWDATAAPIURL
            client = OkHttpClient()                            //要一個實例
            JSON = MediaType.parse("application/json; charset=utf-8")
            request = Request.Builder()                    //建立需求
                .addHeader("Cookie", phpsessid)  // （這是加檔頭傳送）
                .url(url)
                .build()
            try {
                response = client.newCall(request).execute()            // 取得回應到response 來
                bodyData = response.body()!!.string()
                println(bodyData)
                runOnUiThread {
                    textView.text = "${bodyData}"
                }
            } catch (e: Exception) {
                println("Error!!!!")
            }

        }

    }

}

