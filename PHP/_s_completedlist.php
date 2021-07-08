<?php

$con=mysqli_connect("localhost","root","dbwjd2860","yujung");

if(mysqli_connect_errno($con))
{
	echo "Failed to connect to MySQL: ".mysqli_connect_error();
}

mysqli_set_charset($con,"utf8");

$cinema_num = $_GET['cinema_num']; ######추가

$res=mysqli_query($con,"(select L.lost_num, L.lost_object, L.lost_object_detail, L.processing_state, substr(L.registered_time,1,10), I.receiving_date, C.phone_num, C.customer_name, Ci.branch, S.staff_name, L.mark from lost L, staff S, inquiry I, customer C, cinema Ci where L.processing_state = '수령완료' and L.cinema_num = '$cinema_num' and L.lost_num = I.lost_num and C.customer_num = I.customer_num and Ci.cinema_num = L.cinema_num and L.staff_num=S.staff_num) union all (select A.ask_num, A.ask_object, A.ask_object_detail, A.ask_state, substr(A.registered_date,1,10), A.receiving_date, C.phone_num, C.customer_name, Ci.branch, S.staff_name, A.mark from ask_lost A, staff S, customer C, cinema Ci where A.ask_state = '수령완료' and A.cinema_num = '$cinema_num' and A.customer_num = C.customer_num and Ci.cinema_num = A.cinema_num and A.staff_num=S.staff_num) order by mark desc, lost_num asc");

$result=array();

while($row=mysqli_fetch_array($res)){

	array_push($result,
		array('branch'=>$row[8],'lost_object'=>$row[1],'lost_object_detail'=>$row[2],'processing_state'=>$row[3],'registered_time'=>$row[4],'staff_num'=>$row[9],'phone_num'=>$row[6],'customer_name'=>$row[7],'receiving_date'=>$row[5], 'mark'=>$row[10]));
}

echo json_encode(array("result"=>$result));

mysqli_close($con);

?>
