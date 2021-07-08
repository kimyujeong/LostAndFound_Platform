<?php

$con=mysqli_connect("localhost","root","dbwjd2860","yujung");

if(mysqli_connect_errno($con))
{
   echo "Failed to connect to MySQL: ".mysqli_connect_error();
}

mysqli_set_charset($con,"utf8");

$cinema_num = $_GET['cinema_num']; ######추가
$expired_date = $_GET['expired_date'];


## approve, ask, expected, expired 순서
$res=mysqli_query($con,"(SELECT COUNT(*) count FROM lost l, inquiry i WHERE l.lost_num=i.lost_num AND i.processing_state='승인대기중' AND l.cinema_num='$cinema_num') UNION ALL (SELECT COUNT(*) ask_count FROM ASK_LOST A WHERE A.cinema_num = '$cinema_num' AND A.ask_state = '답변대기중') UNION ALL (SELECT SUM(exp.count) expected_count FROM (
(SELECT COUNT(*) count FROM lost L, inquiry I WHERE L.lost_num = I.lost_num AND L.cinema_num='$cinema_num' AND I.processing_state='수령예정') UNION ALL (SELECT COUNT(*) count FROM ask_lost A WHERE A.cinema_num='$cinema_num' AND A.ask_state='수령예정')) exp) UNION ALL (SELECT SUM(e.count) expire_count FROM ((SELECT COUNT(*) count FROM lost WHERE cinema_num='$cinema_num' and importance= 'A' and processing_state ='wait' and substr(expired_date,1,10)='$expired_date') UNION ALL (SELECT COUNT(*) count FROM lost WHERE cinema_num='$cinema_num' and processing_state='wait' AND (importance = 'B' OR importance = 'C') and substr(expired_date,1,10)='$expired_date')) e)");

$result=array();

while($row=mysqli_fetch_array($res)){

   array_push($result,array('count'=>$row[0]));
}

echo json_encode(array("result"=>$result));

mysqli_close($con);

?>