<?php

$con=mysqli_connect("localhost","root","dbwjd2860","yujung");

if(mysqli_connect_errno($con))
{
	echo "Failed to connect to MySQL: ".mysqli_connect_error();
}

mysqli_set_charset($con,"utf8");

//$site = $_GET['site']; ######추가

$res=mysqli_query($con,"select branch,lost_object,processing_state,left(registered_time,10) as registered_time, left(expired_date,10) as expired_date,city,lost_num from lost l,cinema c where l.cinema_num=c.cinema_num and l.processing_state not in ('수령완료','폐기완료')");

$result=array();

while($row=mysqli_fetch_array($res)){

	array_push($result,
		array('branch'=>$row[0],'lost_object'=>$row[1],'processing_state'=>$row[2],'registered_time'=>$row[3],'expired_date'=>$row[4],'city'=>$row[5],'lost_num'=>$row[6]));
}

echo json_encode(array("result"=>$result));

mysqli_close($con);

?>
