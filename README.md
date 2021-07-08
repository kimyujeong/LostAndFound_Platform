# LostAndFound_Platform
 Lost and Found Platform Service For Cinema (*CineLF*)
 
 
 
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
<img src="https://user-images.githubusercontent.com/26537107/124937993-db801600-e042-11eb-8cd3-dc1f5a69cd41.png" width="400" higeht="200">

* User can use the service through the Android application. Apache and PHP used as an intermediate web server for linking Android apps and databases.
   * I could use *SQLite*, the built-in *DB* of *Android Studio*, but *SQLite* is not suitable for concurrency and large data volumes. So, I chose the *MySQL* as *DBMS*.
   * And also, *Android* does not support external *DB* access for security reasons. So, I had to get the data through the *web server*.


<img src="https://user-images.githubusercontent.com/26537107/124938297-2568fc00-e043-11eb-8901-703ce8fa1a22.png" width="400" higeht="200">

* Used *Erwin data modeler* to describe the averall table realtionship within the *Database*


# Experiments
*CineLF* provides services divided into employee and customer applications. On the login screen, employees log in with their employee number, and customers log in with their mobile phone number and password.

<img src="https://user-images.githubusercontent.com/26537107/124940372-e3d95080-e044-11eb-96f8-ad4bba8c3b6e.png" width="150" higeht="350">

* In employee applications, it performs functions of registering, inquiring, and managing lost items through 7 menus. You can check the details that need to be checked through the red notification badge on the menu screen.      
   
    
<img src="https://user-images.githubusercontent.com/26537107/124940380-e50a7d80-e044-11eb-8ab8-ecb0c5e501e4.png" width="150" higeht="350">

* In customer applications, lost items can be easily viewed, inquired, and managed through 3 menus. Through the "고객님의 문의에 대한 응답(Response to Customer's Inquiry)" in the middle of the main screen, you can check whether the employee approves the customer's request.

# Expected Effects and Future Plans
* Real-time inquiry of lost items is possible, and limited inquiry time is eased, increasing customer convenience.
* Easy management of inquiry history using apps reduces congestion of employees.
* Accessibility can be increased by adding corresponding functions to movie theater apps that are previously used for booking.
* The service can be expanded by applying it to large-scale places visited by people, such as performance halls, as well as well as movie theaters.
* User convenience can be enhanced through Kakao Talk channel linkage or chatbot.
