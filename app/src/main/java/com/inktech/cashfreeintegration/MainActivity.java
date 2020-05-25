package com.inktech.cashfreeintegration;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gocashfree.cashfreesdk.CFPaymentService;
import com.google.android.material.textfield.TextInputEditText;
import com.inktech.cashfreeintegration.Model.CashFreeToken;
import com.inktech.cashfreeintegration.Services.FirebaseCloudConnection;
import com.inktech.cashfreeintegration.Services.RetrofitClient;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    TextView idText;
    TextInputEditText amtEditText;
    CardView payButton;
    String TAG = "inkTech";

    CompositeDisposable compositeDisposable = new CompositeDisposable();
    FirebaseCloudConnection firebaseCloudConnection;

    String orderID;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initializing the views
        idText = findViewById(R.id.order_id);
        amtEditText = findViewById(R.id.amount);
        payButton = findViewById(R.id.pay);

        orderID = UUID.randomUUID().toString();
        idText.setText(orderID);

        //initializing the retrofit instance
        firebaseCloudConnection = RetrofitClient.getInstance();

        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               makePaymentRequest();
            }
        });
    }

    private void makePaymentRequest() {

        String orderAmount = amtEditText.getText().toString();
        // the data that you have to send in the Map is specified in the following link
        //https://docs.cashfree.com/pg/android/request-resp-params
        final Map<String,String> dataSend = new HashMap<>();
        dataSend.put("appId","YOUR_APP_ID"); // you will get your get your app id and other credentials after creating an account in acshfree
        dataSend.put("orderId",orderID);
        dataSend.put("orderAmount",orderAmount);
        dataSend.put("orderCurrency","INR");
        dataSend.put("customerPhone","1234567890");
        dataSend.put("customerEmail","test@gmail.com");

        compositeDisposable.add(firebaseCloudConnection.getToken(orderID,orderAmount)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Consumer<CashFreeToken>() {
            @Override
            public void accept(CashFreeToken cashFreeToken) throws Exception {
                if(cashFreeToken.getStatus().equals("OK")){
                    //In production change test to production
                    CFPaymentService.getCFPaymentServiceInstance()
                            .doPayment(MainActivity.this,dataSend,cashFreeToken.getCftoken(),"TEST")
                    ;

                }else{
                    Toast.makeText(MainActivity.this,""+cashFreeToken.getMessage(),Toast.LENGTH_SHORT).show();
                }

            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Toast.makeText(MainActivity.this,""+throwable.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }));



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Same request code for all payment APIs.
        Log.d(TAG, "ReqCode : " + CFPaymentService.REQ_CODE);
        Log.d(TAG, "API Response : ");
        //Prints all extras. Replace with app logic.
        if (data != null) {
            Bundle  bundle = data.getExtras();
            if (bundle != null)
                for (String  key  :  bundle.keySet()) {
                    if (bundle.getString(key) != null) {
                        Log.d(TAG, key + " : " + bundle.getString(key));
                    }
                }
        }
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();

    }
}
