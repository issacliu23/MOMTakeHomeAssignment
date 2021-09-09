# MOM WINS Take Home Assignment
## Setting up database (PostgreSQL)
1. Download from PostgreSQL database server in the following link, and install according to your OS
   https://www.enterprisedb.com/downloads/postgres-postgresql-downloads
2. Connect to the server by typing the following command in command prompt
    ```sh
    psql -U postgres
    ```
3. System will prompt for password and enter the password you have entered during the installation of PostgreSQL server (Step 1)
4. Create database, momdb and confirm it has been created
    ```sh
    CREATE DATABASE momdb;
    \c momdb
    ```
5. Edit application.yml file and key in your database password
   > spring:
   > 
   > &nbsp;&nbsp;&nbsp;datasource:
   > 
   > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;url: jdbc:postgresql://localhost:5432/momdb
   > 
   > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;username: postgres
   > 
   > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;password: <YOUR_PASSWORD>


## Setting up Postman to test API
1. Install Postman in this link
   https://www.postman.com/downloads/
2. Launch Postman and import api.postman_collection.json
3. Run the code server and test using the APIs in the collection