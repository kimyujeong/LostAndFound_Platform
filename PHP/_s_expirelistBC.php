<?php

$con=mysqli_connect("localhost","root","dbwjd2860","yujung");

if(mysqli_connect_errno($con))
{
   echo "Failed to connect to MySQL: ".mysqli_connect_error();
}

mysqli_set_charset($con,"utf8");

$cinema_num = $_GET['cinema_num']; ######추가

$res=mysqli_query($con,"SELECT l.lost_num, c.branch, l.lost_object, l.lost_object_detail, l.processing_state, substr(l.expired_date,1,10), s.staff_name FROM lost l, cinema c, staff s WHERE s.staff_num=l.staff_num and l.cinema_num=c.cinema_num and l.cinema_num='$cinema_num' and l.processing_state='wait' AND (l.importance = 'B' OR l.importance = 'C') order by lost_num");

$result=array();

while($row=mysqli_fetch_array($res)){

   array_push($result,
      array('lost_num'=>$row[0],'cinema_num'=>$row[1],'lost_object'=>$row[2],'lost_object_detail'=>$row[3],'processing_state'=>$row[4],'expired_date'=>$row[5],'staff_num'=>$row[6]));
}


echo json_encode(array("result"=>$result));

mysqli_close($con);

?>