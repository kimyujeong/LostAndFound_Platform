<?php
 
    header('Content-Type:text/html; charset=utf-8');
 
    $conn= mysqli_connect("localhost","root","dbwjd2860","yujung");
    //$conn= mysqli_connect("localhost","umul","aa142536!","umul");
 
    mysqli_query($conn, "set names utf8");    //한글 깨짐 방지
 
    //쿼리문 작성
    $sql="SELECT L.lost_num, P.photo, L.cinema_num, substr(L.expired_date,1,10) as expired_date from lost_photo P, lost L WHERE (L.importance = 'B' OR L.importance = 'C') and L.processing_state ='wait' and L.lost_num=P.lost_num order by lost_num";
    $result=mysqli_query($conn, $sql);
 
    //$result : 결과 표
 
    //결과의 총 레코드 수(줄 수, 행의 개수)
    $rowCnt= mysqli_num_rows($result);
 
    //레코드 수 만큼 반복하여 한줄씩 데이터 읽어오기
    for($i=0; $i<$rowCnt; $i++){
        //데이터 한줄을 연관배열(키값으로 구분)로 받아오기
        $row= mysqli_fetch_array($result, MYSQLI_ASSOC);
        echo "$row[lost_num]&$row[photo]&$row[cinema_num]&$row[expired_date];";
    }
 
    mysqli_close($conn);
 
?>
 
