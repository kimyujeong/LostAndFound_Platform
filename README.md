# LostAndFound_Platform
 Lost and Found Platform Service For Cinema
 
 
 
# Introduction
I provided a *‘Lost and Found’* management platform as an Android Application to solve the inconvenience of customer service. By diziting customer service, which used to be an analog, I solved the customer inconvenience and improved the work efficiency of employees.
If the customer lose their belonings at a movie theater, the customer has to revisit the movie theater to ask their lost things. In addition, if customers visit at a crowded time, employees' in-store services and customer service may be delayed.
So, I developed a service technology platform that can reduce various inconveniences and enhance the convenience of both employees and customers.


# Objectives
* Employee : Save discovered lost items to Database through the App.
* Customer : Before visiting the store, check and inquiry lost items by real-time through the App. 


# Distinction
* *Real-time* - can check the Lost&Found
* *Interaction* - reduce time for restricted inquiries
* *Application* - easy to manage inquiry history using App


# System Configuration Chart
* User can use the service through the Android application. Apache and PHP used as an intermediate web server for linking Android apps and databases.
  * I could use *SQLite*, the built-in *DB* of *Android Studio*, but *SQLite* is not suitable for concurrency and large data volumes. So, I chose the *MySQL* as *DBMS*.
  * And also, *Android* does not support external *DB* access for security reasons. So, I had to get the data through the *web server*.
