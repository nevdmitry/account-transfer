# account-transfer
Simple Spark application. 

To start server from IDEA run AccountTransfer with main method

By default service available at http://localhost:8877

Initial data in in memory storage. All operations in USD
- account 123 amount 10000
- account 321 amount 20000
- account 111 amount 30000
- account 222 amount 40000

* **URL**

  http://localhost:8877/transfer/

* **Method:**

  `POST`
  
*  **Payload**

   `{"sourceAccId":123, "targetAccId": 321, "amount" : 0.1}`


* **Success Response:**

  * **Code:** 200 <br />
    **Content:** `{ status : ok }`
 
* **Error Response:**

  * **Code:** 404 NOT FOUND <br />
    **Content:** `{ error : "NO_ENOUGH_MONEY" }`


