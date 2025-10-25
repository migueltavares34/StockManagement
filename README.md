This project is a POC for a very basic Stock Management API witch manages incoming user orders, stock replacement and orders fullfilment with comunication to user by email.

To start this API:

Import project to eclipse IDE using 

    JDK-17
    latest maven version for dependencies management
	SpringTools
Configure application.properties file:

	SQL: host, username and password variables to your mail server account
	SQL: url, username and password to your postgres server
	
Run project as Spring Boot App

Endpoints routing

API swagger, where informtion and testing of API endpoints, can be found follwing this uri with application running: http://localhost:8080/swagger-ui/index.html

After populating DB with users and items, stock movements and orders can be created.
For creating stock movements "add" endpoint should be use once it checks the existence of stock movements for the item, if movement already exists it will add quantity, if not, it will create new stock movement.
As soon as there is enough items, orders will be fulffilled by beeing sent email and removing order from DB.

If stock movement turns empty of items, it is removed from DB
