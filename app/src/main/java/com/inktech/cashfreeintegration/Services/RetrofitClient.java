package com.inktech.cashfreeintegration.Services;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static Retrofit instance;
    private static String BASE_URL ="YOUR_URL";//enter your URL where you would produce the token

    //for generating the URL you have to utilize the firebase cloud function and deploy a NODEjs application that would generate the
    //token for your payements an the cloud server
    //REMEMBER: never generate the token in the application to avoid security threats

    public static FirebaseCloudConnection getInstance(){

        //creating the retrofit instance for accessing the token which would be generated at our backend server
        if(instance == null){
            instance = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();

        }

        return instance.create(FirebaseCloudConnection.class);

    }
}
