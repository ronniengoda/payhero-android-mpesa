# Payhero Android MPESA Library [![](https://jitpack.io/v/bensalcie/payhero-android-mpesa.svg)](https://jitpack.io/#bensalcie/payhero-android-mpesa)

Android MPESA library to request STK Push using MPESA Daraja API.
# Screenshots
  <p float="center">
	  <img src="https://github.com/bensalcie/payhero-android-mpesa/blob/main/screen.jpg" width="200" />
	  <img src="https://github.com/bensalcie/payhero-android-mpesa/blob/main/screentwo.jpg" width="200" /> 
  </p>

  
  
##  How to use the library
 To get a Git project into your build:

## Step 1. Add the JitPack repository to your build file

Add it in your root build.gradle at the end of repositories:
```
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
  ```
## Step 2. Add the dependency
```
	dependencies {
	        implementation 'com.squareup.retrofit2:retrofit:2.5.0'
	        implementation 'com.github.bensalcie:payhero-android-mpesa:0.1.3'
	}
```

## Step 3. Add this in onCreate() method.
```
    private var mApiClient: DarajaApiClient? = null //Intitialization before on create
    
    mApiClient = DarajaApiClient("xxxxxconsumerkeyxxxx", "xxxxconsumersecretxxxx")
    //get consumerkey and secret from https://developer.safaricom.co.ke/user/me/apps
        mApiClient!!.setIsDebug(true) //Set True to enable logging, false to disable.
        getAccessToken()//make request availabe and ready for processing.
```
## Step 3.Define access token method.
```
	//Access token Method being called.
	   private fun getAccessToken() {
		mApiClient!!.setGetAccessToken(true)
		mApiClient!!.mpesaService()!!.getAccessToken().enqueue(object : Callback<AccessToken> {
		    override fun onResponse(call: Call<AccessToken?>, response: Response<AccessToken>) {
			if (response.isSuccessful) {
			    mApiClient!!.setAuthToken(response.body()?.accessToken)
			}
		    }
		    override fun onFailure(call: Call<AccessToken?>, t: Throwable) {}
		})
	    }
  ```
   
## Step 4. Initiate STK Push
```
 btnDeposit.setOnClickListener {
            val amount = etAmount.text.toString()
            val phone =etPhone.text.toString()
                if (amount.isNotEmpty() && phone.isNotEmpty()) {
                        btnDeposit.text = getString(R.string.processing)
                    performSTKPush(phone, amount)
                } else {
                    etPhone.error = getString(R.string.errorm)
                    etAmount.error = getString(R.string.errorm)
                }
            }
  private fun performSTKPush(amount: String, phone_number: String) {
        //Handle progresss here
	//credentials here are test credentials
        val timestamp = Utils.getTimestamp()
        val stkPush = STKPush("MPESA Android Test",amount,"174379","http://mpesa-requestbin.herokuapp.com/1ajipzt1",
            Utils.sanitizePhoneNumber(phone_number)!!,"174379",Utils.getPassword("174379", 
	    "bfb279f9aa9bdbcf158e97dd71a467cd2e0c893059b10f78e6b72ada1ed2c919", timestamp!!)!!
            , Utils.sanitizePhoneNumber(phone_number)!!,timestamp,"Testing","CustomerPayBillOnline")
        mApiClient!!.setGetAccessToken(false)
        mApiClient!!.mpesaService()!!.sendPush(stkPush).enqueue(object : Callback<STKPush> {
            override fun onResponse(call: Call<STKPush?>, response: Response<STKPush>) {
	    //dismiss progress when running
                try {
                    if (response.isSuccessful) {
                        Log.d("MPESA", "onResponse:${response.body()} ")
			//Handle when request is sucessful
			//Get response from variable 'response' and manipulate as you want using response.body()
                    } else {
                        Log.d("MPESA", "onResponse: ${response.errorBody()}")
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } through
            override fun onFailure(call: Call<STKPush>, t: Throwable) {
                mProgressDialog!!.dismiss()
                Log.d("MPESA", "onFailure: $t")
            }
        })
    }
```

<script data-name="BMC-Widget" src="https://cdnjs.buymeacoffee.com/1.0.0/widget.prod.min.js" data-id="bensalcie" data-description="Support me on Buy me a coffee!" data-message="Thank you for visiting. You can now buy me a coffee!" data-color="#5F7FFF" data-position="" data-x_margin="18" data-y_margin="18"></script>
