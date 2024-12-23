package com.mutia.produkappapi

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.mutia.produkappapi.adapter.ProdukAdapter
import com.mutia.produkappapi.model.ModelProduk
import com.mutia.produkappapi.model.ResponProduk
import com.mutia.produkappapi.service.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var swipeRefresh : SwipeRefreshLayout
    private lateinit var recycleView : RecyclerView
    private  lateinit var call : Call<ResponProduk>
    private lateinit var productAdapter : ProdukAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        swipeRefresh = findViewById(R.id.refresh_layout)
        recycleView = findViewById(R.id.rv_products)

        productAdapter = ProdukAdapter { modelProduk: ModelProduk -> productOnClick(modelProduk) }
        recycleView.adapter = productAdapter
        recycleView.layoutManager = LinearLayoutManager(
            applicationContext, LinearLayoutManager.VERTICAL,
            false
        )

        swipeRefresh.setOnRefreshListener {
            getData()
        }
        getData()

    }
    //ttps://dummyjson.com/products
//https://dummyjson.com/docs/products
    private fun productOnClick(produk: ModelProduk){
        Toast.makeText(applicationContext, produk.description, Toast.LENGTH_SHORT).show()

    }

    private fun getData(){
        swipeRefresh.isRefreshing = true
        call = ApiClient.produkService.getAllProduk()
        call.enqueue(object : Callback<ResponProduk>{
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<ResponProduk>,
                response: Response<ResponProduk>
            ) {

                swipeRefresh.isRefreshing = false
                if(response.isSuccessful){
                    productAdapter.submitList(response.body()?.products)
                    productAdapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<ResponProduk>, t: Throwable) {
                swipeRefresh.isRefreshing = false
                Toast.makeText(applicationContext, t.localizedMessage, Toast.LENGTH_SHORT).show()
            }

        })

    }

}