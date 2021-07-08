<?php
    $con=mysqli_connect("localhost","root","dbwjd2860","yujung");

    mysqli_query($con,'SET NAMES utf8');
 
    $city = $_GET['city'];

    $res=mysqli_query($con,"SELECT branch FROM CINEMA WHERE city ='$city'");

    $result=array();

    while($row=mysqli_fetch_array($res)){
        array_push($result,
            array('branch'=>$row[0]));
    }

    echo json_encode(array("result"=>$result));

    mysqli_close($con);

?>
