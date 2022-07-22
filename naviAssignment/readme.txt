Step to run the spring boot application

Step 1) start the spring boot server

Step 2) by default it will read the orders from inputData.txt file (Path : "./src/resources/templates/inputData.txt")
        and process the trade and show the trade result.

Step 3) Provide your custom order input in console as command line input, to finish the orders input type END  and then press enter
        example command line inputs:
        #1 09:45 BAC sell 240.12 100
        #2 09:46 BAC sell 237.45 90
        #3 09:47 BAC buy 238.10 110
        END

Step 4) In console you will see the trade result of above command line inputs

Step 5) If you want to continue the trade again the type 1 and to exit type any other keys


Swagger Info : ----------------------------------------------------
I have defined few api, you can access through swagger.
swagger UI url =>  http://localhost:8080/stockExchange/swagger/index.html#
