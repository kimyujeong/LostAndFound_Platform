<?php

$con=mysqli_connect("localhost","root","dbwjd2860","yujung");

if(mysqli_connect_errno($con))
{
   echo "Failed to connect to MySQL: ".mysqli_connect_error();
}

mysqli_set_charset($con,"utf8");

$customer_num = $_GET['customer_num'];

$res=mysqli_query($con,"select C.customer_num, C.customer_name, T.screening_date,I.branch, F.film_name,T.screening_site,substr(T.screening_time,1,5), substr(date_Add(T.screening_time, interval 2 hour),1,5) as screening_time2, V.viewing_history_num from customer C, viewing_history V, timetable T, cinema I, film F where C.customer_num=V.customer_num and T.timetable_num=V.timetable_num and I.cinema_num=T.cinema_num and F.film_num=T.film_num and C.customer_num= '$customer_num'");

$result=array();

while($row=mysqli_fetch_array($res)){

   array_push($result,
      array('customer_num'=>$row[0],'customer_name'=>$row[1],'screening_date'=>$row[2],'branch'=>$row[3],'film_name'=>$row[4],'screening_site'=>$row[5],'screening_time'=>$row[6],'screening_time2'=>$row[7],'viewing_history_num'=>$row[8]));
}

echo json_encode(array("result"=>$result));

mysqli_close($con);

?>