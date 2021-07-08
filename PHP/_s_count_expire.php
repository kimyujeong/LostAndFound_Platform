<?php

$con=mysqli_connect("localhost","root","dbwjd2860","yujung");

if(mysqli_connect_errno($con))
{
   echo "Failed to connect to MySQL: ".mysqli_connect_error();
}

mysqli_set_charset($con,"utf8");

$cinema_num = $_GET['cinema_num']; 
$expired_date = $_GET['expired_date'];


## a, bc 순서
$res=mysqli_query($con,"(SELECT COUNT(*) count FROM lost WHERE cinema_num='$cinema_num' and importance= 'A' and processing_state ='wait' and substr(expired_date,1,10)='$expired_date') UNION ALL (SELECT COUNT(*) ask_count FROM lost WHERE cinema_num='$cinema_num' and processing_state='wait' AND (importance = 'B' OR importance = 'C') and substr(expired_date,1,10)='$expired_date')");

$result=array();

while($row=mysqli_fetch_array($res)){

   array_push($result,array('count'=>$row[0]));
}

echo json_encode(array("result"=>$result));

mysqli_close($con);

?>