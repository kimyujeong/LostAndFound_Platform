<?php

$con=mysqli_connect("localhost","root","dbwjd2860","yujung");

if(mysqli_connect_errno($con))
{
	echo "Failed to connect to MySQL: ".mysqli_connect_error();
}

mysqli_set_charset($con,"utf8");

$customer_num = $_GET['customer_num']; ## 나중에 customer 번호로 바꾸기

## $res=mysqli_query($con,"select * from lost, multiplex, staff where lost.site='$site' and lost.site = multiplex.mno and lost.staffno=staff.sno and lost.state!='수령완료' and lost.state!='폐기완료'");

$res=mysqli_query($con,"(SELECT A.ask_num, A.ask_object, A.ask_object_detail, A.reject_detail, A.flag, 
A.lost_location, C.customer_name, A.registered_date
FROM ASK_LOST A, CUSTOMER C 
WHERE A.customer_num = '$customer_num' AND A.ask_state IN ('답변완료', '답변진행중') AND A.customer_num = C.customer_num)

UNION ALL

(SELECT l.lost_num, l.lost_object, i.object_detail, i.reject_detail, i.flag,
i.receiving_date, i.receiving_time, i.registered_date 
FROM lost l, inquiry i 
WHERE l.lost_num=i.lost_num and i.processing_state in ('수령예정','승인거부') and i.customer_num='$customer_num')");

$result=array();

while($row=mysqli_fetch_array($res)){

	array_push($result,
		array('lost_num'=>$row[0],'lost_object'=>$row[1],'object_detail'=>$row[2],'reject_detail'=>$row[3],'flag'=>$row[4],'receiving_date'=>$row[5],'receiving_time'=>$row[6],'registered_date'=>$row[7]));
}

echo json_encode(array("result"=>$result));

mysqli_close($con);

?>
