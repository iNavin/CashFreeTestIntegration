package com.inktech.cashfreeintegration.Services;

import com.inktech.cashfreeintegration.Model.CashFreeToken;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FirebaseCloudConnection {
    @GET("token")
    Observable<CashFreeToken> getToken(@Query("orderId") String orderId,
                                       @Query("orderAmount") String orderAmount);

}
