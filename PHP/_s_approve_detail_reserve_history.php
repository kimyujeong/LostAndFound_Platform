<?php

$con=mysqli_connect("localhost","root","dbwjd2860","yujung");

if(mysqli_connect_errno($con))
{
   echo "Failed to connect to MySQL: ".mysqli_connect_error();
}

mysqli_set_charset($con,"utf8");

$customer_num = $_GET['customer_num'];

$res=mysqli_query($con,"select T.screening_date,I.branch, F.film_name,T.screening_site,substr(T.screening_time,1,5), substr(date_Add(T.screening_time, interval 2 hour),1,5), V.viewing_history_num as screening_time2 from viewing_history V, timetable T, cinema I, film F where T.timetable_num=V.timetable_num and I.cinema_num=T.cinema_num and F.film_num=T.film_num and V.customer_num= '$customer_num' and V.flag='Y'");

$result=array();

while($row=mysqli_fetch_array($res)){

   array_push($result,
      array('screening_date'=>$row[0],'branch'=>$row[1],'film_name'=>$row[2],'screening_site'=>$row[3],'screening_time1'=>$row[4],'screening_time2'=>$row[5],'num'=>$row[6]));
}

echo json_encode(array("result"=>$result));

mysqli_close($con);

?>