<?php
 
    header("Content-Type:text/html; charset=UTF-8");
 
   // $name= $_POST['name'];
    //$msg= $_POST['msg'];
    $file= $_FILES['img'];
 
    //이미지 파일을 영구보관하기 위해
    //이미지 파일의 세부정보 얻어오기
    $srcName= $file['name'];
    $tmpName= $file['tmp_name']; //php 파일을 받으면 임시저장소에 넣는다. 그곳이 tmp
 
    //임시 저장소 이미지를 원하는 폴더로 이동
    $dstName= "uploads/".date('Ymd_his').$srcName;
    $result=move_uploaded_file($tmpName, $dstName);
    if($result){
        echo "upload success\n";
    }else{
        echo "upload fail\n";
    }
 
   // echo "$name\n";
    //echo "$msg\n";
    echo "$dstName\n";
 
    //글 작성 시간 변수
    //$now= date('Y-m-d H:i:s');

 
    // $name, $msg, $dstName, $now DB에 저장
    // MySQL에 접속
    $conn= mysqli_connect("localhost","root","dbwjd2860","yujung");
 
    //한글 깨짐 방지
    mysqli_query($conn, "set names utf8");
    //mysqli_set_charset($con,"utf8");

    $sql_s = "SELECT lost_num FROM lost ORDER BY lost_num DESC LIMIT 1";
    $sql_s_result =mysqli_query($conn, $sql_s);
    $row=$sql_s_result->fetch_row();
    $lost_num = (string)$row[0];

    //insert하는 쿼리문
    $sql_i="insert into lost_photo(lost_num,photo) values ('$lost_num','$dstName')";
    $result =mysqli_query($conn, $sql_i); //쿼리를 요청하다. 
 
    if($result) echo "insert success \n";
    else echo "insert fail \n";
 
    mysqli_close($conn);
 
    
?>