<?php

$con=mysqli_connect("localhost","root","dbwjd2860","yujung");

if(mysqli_connect_errno($con))
{
	echo "Failed to connect to MySQL: ".mysqli_connect_error();
}

mysqli_set_charset($con,"utf8");

$customerNum = $_GET['customerNum']; ## 나중에 customer 번호로 바꾸기

## $res=mysqli_query($con,"select * from lost, multiplex, staff where lost.site='$site' and lost.site = multiplex.mno and lost.staffno=staff.sno and lost.state!='수령완료' and lost.state!='폐기완료'");

$res=mysqli_query($con,"select a.registered_date, a.ask_object, a.ask_state, a.mark, a.ask_num from ask_lost a where a.customer_num='$customerNum' union all select i.registered_date, l.lost_object, l.processing_state, l.mark, l.lost_num from lost l, inquiry i where l.lost_num=i.lost_num and i.customer_num='$customerNum'");

$result=array();

while($row=mysqli_fetch_array($res)){

	array_push($result,
		array('registered_date'=>$row[0],'ask_object'=>$row[1],'ask_state'=>$row[2],'mark'=>$row[3],'num'=>$row[4]));
}

echo json_encode(array("result"=>$result));

mysqli_close($con);

?>
