package bensalcie.payhero.mpesa.mpesa.services

import bensalcie.payhero.mpesa.mpesa.interceptor.AccessTokenInterceptor
import bensalcie.payhero.mpesa.mpesa.interceptor.AuthInterceptor
import bensalcie.payhero.mpesa.mpesa.services.Constants.BASE_URL
import bensalcie.payhero.mpesa.mpesa.services.Constants.CONNECT_TIMEOUT
import bensalcie.payhero.mpesa.mpesa.services.Constants.READ_TIMEOUT
import bensalcie.payhero.mpesa.mpesa.services.Constants.WRITE_TIMEOUT
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class DarajaApiClient(private val consumerkey:String,private val consumersecret:String) {
    private var retrofit: Retrofit? = null
    private var isDebug = false
    private var isGetAccessToken = false
    private var mAuthToken: String? = null
    private val httpLoggingInterceptor = HttpLoggingInterceptor()

    fun setIsDebug(isDebug: Boolean): DarajaApiClient? {
        this.isDebug = isDebug
        return this
    }

    fun setAuthToken(authToken: String?): DarajaApiClient? {
        mAuthToken = authToken
        return this
    }

    fun setGetAccessToken(getAccessToken: Boolean): DarajaApiClient? {
        isGetAccessToken = getAccessToken
        return this
    }

    private fun okHttpClient(): OkHttpClient.Builder {
        val okHttpClient = OkHttpClient.Builder()
        okHttpClient
            .connectTimeout(CONNECT_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .addInterceptor(httpLoggingInterceptor)
        return okHttpClient
    }

    private fun getRestAdapter(): Retrofit? {
        val builder = Retrofit.Builder()
        builder.baseUrl(BASE_URL)
        builder.addConverterFactory(GsonConverterFactory.create())
        if (isDebug) {
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        }
        val okhttpBuilder = okHttpClient()
        if (isGetAccessToken) {
            okhttpBuilder.addInterceptor(AccessTokenInterceptor(consumerkey,consumersecret))
        }
        if (mAuthToken != null && !mAuthToken!!.isEmpty()) {
            okhttpBuilder.addInterceptor(AuthInterceptor(mAuthToken!!))
        }
        builder.client(okhttpBuilder.build())
        retrofit = builder.build()
        return retrofit
    }

    fun mpesaService(): STKPushService? {
        return getRestAdapter()!!.create(STKPushService::class.java)
    }
}