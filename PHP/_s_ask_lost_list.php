<?php

$con=mysqli_connect("localhost","root","dbwjd2860","yujung");

if(mysqli_connect_errno($con))
{
	echo "Failed to connect to MySQL: ".mysqli_connect_error();
}

mysqli_set_charset($con,"utf8");

$cinema_num = $_GET['cinema_num']; ######추가

$res=mysqli_query($con,"(SELECT A.ask_num, A.ask_object, A.ask_object_detail, A.lost_location, A.registered_date, C.customer_name, C.phone_num FROM ASK_LOST A, CUSTOMER C WHERE A.cinema_num = '$cinema_num' AND A.ask_state = '답변대기중' AND A.customer_num = C.customer_num)");

$result=array();

while($row=mysqli_fetch_array($res)){

	array_push($result,
		array('ask_num'=>$row[0], 'ask_object'=>$row[1], 'ask_object_detail'=>$row[2], 'lost_location'=>$row[3], 'registered_date'=>$row[4], 'customer_name'=>$row[5], 'phone_num'=>$row[6]));
}

echo json_encode(array("result"=>$result));

mysqli_close($con);

?>
