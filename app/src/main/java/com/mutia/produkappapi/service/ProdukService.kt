package com.mutia.produkappapi.service

import com.mutia.produkappapi.model.ResponProduk
import retrofit2.Call
import retrofit2.http.GET

interface ProdukService {

    @GET("products")
    fun getAllProduk() : Call<ResponProduk>
}