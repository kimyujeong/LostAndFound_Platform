<?php

$con=mysqli_connect("localhost","root","dbwjd2860","yujung");

if(mysqli_connect_errno($con))
{
	echo "Failed to connect to MySQL: ".mysqli_connect_error();
}

mysqli_set_charset($con,"utf8");

$cinema_num = $_GET['cinema_num']; ## 나중에 customer 번호로 바꾸기

## $res=mysqli_query($con,"select * from lost, multiplex, staff where lost.site='$site' and lost.site = multiplex.mno and lost.staffno=staff.sno and lost.state!='수령완료' and lost.state!='폐기완료'");

$res=mysqli_query($con,"select l.lost_object_detail, i.object_detail, l.lost_num, i.receiving_date, i.receiving_time, i.registered_date, l.lost_object, i.customer_num from lost l, inquiry i where l.lost_num=i.lost_num and i.processing_state='승인대기중' and l.cinema_num='$cinema_num' order by lost_num");

$result=array();

while($row=mysqli_fetch_array($res)){

	array_push($result,
		array('lost_object_detail'=>$row[0],'object_detail'=>$row[1],'lost_num'=>$row[2],'receiving_date'=>$row[3],'receiving_time'=>$row[4],'registered_date'=>$row[5],'lost_object'=>$row[6],'customer_num'=>$row[7]));
}

echo json_encode(array("result"=>$result));

mysqli_close($con);

?>
