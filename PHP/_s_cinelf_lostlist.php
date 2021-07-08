<?php

$con=mysqli_connect("localhost","root","dbwjd2860","yujung");

if(mysqli_connect_errno($con))
{
	echo "Failed to connect to MySQL: ".mysqli_connect_error();
}

mysqli_set_charset($con,"utf8");

$cinema_num = $_GET['cinema_num']; ######추가

$res=mysqli_query($con,"select branch, importance,lost_object, lost_object_detail, processing_state,substr(registered_time,1,10),substr(expired_date,1,10), staff.staff_name from lost, cinema, staff where lost.cinema_num='$cinema_num' and lost.cinema_num = cinema.cinema_num and lost.staff_num=staff.staff_num and lost.processing_state!='수령완료' and lost.processing_state!='폐기완료' order by lost_num asc");

$result=array();

while($row=mysqli_fetch_array($res)){

	array_push($result,
		array('branch'=>$row[0],'importance'=>$row[1],'lost_object'=>$row[2],'lost_object_detail'=>$row[3],'processing_state'=>$row[4],'registered_time'=>$row[5],'expired_date'=>$row[6],'staff_name'=>$row[7]));
}

echo json_encode(array("result"=>$result));

mysqli_close($con);

?>
