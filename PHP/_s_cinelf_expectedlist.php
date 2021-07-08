<?php

$con=mysqli_connect("localhost","root","dbwjd2860","yujung");

if(mysqli_connect_errno($con))
{
   echo "Failed to connect to MySQL: ".mysqli_connect_error();
}

mysqli_set_charset($con,"utf8");

$cinema_num = $_GET['cinema_num']; ######추가

$res=mysqli_query($con,"(select L.lost_num, L.lost_object, L.lost_object_detail, C.customer_name, C.phone_num, receiving_date,receiving_time,mark from lost L, inquiry I, customer C where L.lost_num = I.lost_num and C.customer_num = I.customer_num and L.cinema_num='$cinema_num' and I.processing_state='수령예정') union all (select A.ask_num, A.ask_object, A.ask_object_detail, C.customer_name, C.phone_num, receiving_date, receiving_time, mark from ask_lost A, customer C where C.customer_num = A.customer_num and A.cinema_num='$cinema_num' and A.ask_state='수령예정') order by mark desc, lost_num asc");

$result=array();

while($row=mysqli_fetch_array($res)){

   array_push($result,array('lost_num'=>$row[0],'lost_object'=>$row[1],'lost_object_detail'=>$row[2],'customer_name'=>$row[3],'phone_num'=>$row[4],'receiving_date'=>$row[5],'receiving_time'=>$row[6],'mark'=>$row[7]));
}

echo json_encode(array("result"=>$result));

mysqli_close($con);

?>

