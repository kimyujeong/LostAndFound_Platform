<?php

$con=mysqli_connect("localhost","root","dbwjd2860","yujung");

if(mysqli_connect_errno($con))
{
	echo "Failed to connect to MySQL: ".mysqli_connect_error();
}

mysqli_set_charset($con,"utf8");

$num = $_GET['num']; ######추가

$res=mysqli_query($con,"select c.city, c.branch, a.ask_object_detail from cinema c, ask_lost a where c.cinema_num=a.cinema_num and ask_num='$num'");

$result=array();

while($row=mysqli_fetch_array($res)){	

	array_push($result,
		array('city'=>$row[0], 'branch'=>$row[1], 'object_detail'=>$row[2]));
}

echo json_encode(array("result"=>$result));

mysqli_close($con);

?>
