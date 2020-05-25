const functions = require('firebase-functions');
const fetch = require('node-fetch');
const express = require('express');
const cors = require('cors')

const app = express();

app.use(cors({origin:true}));

app.get('/token',async(req,res)=>{
    let orderId = req.query.orderId;
    let orderAmount = req.query.orderAmount;

    if(orderId === undefined || orderId === null)
       res.status(401).send("Error id not found");
    else if(orderAmount === undefined || orderAmount === null)
       res.status(401).send("Error order Amount not found");
    else
    {
        var url = "https://test.cashfree.com/api/v2/cftoken/order";
        var headers = {
            "Content-Type":"application/json",
            "X-Client-Id":"YOUR_ID",
            "X-Client-Secret":"YOUR_SECRET_CLIENT_ID"
        }
        var data = {
            "orderId":orderId,
            "orderAmount":orderAmount,
            "orderCurrency":"INR"
        }
        try{
            const response  = await fetch(url,{method:'POST',headers:headers,body:JSON.stringify(data)});
            const json = await response.json();
            res.send(json);

        }catch(error){
            res.send(error);
        }
    }
});

exports.widgets = functions.https.onRequest(app);
