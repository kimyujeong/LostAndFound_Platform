<?php
    $con=mysqli_connect("localhost","root","dbwjd2860","yujung");
    mysqli_query($con,'SET NAMES utf8');
 
    $staff_num = $_POST["staff_num"];

    $password = $_POST["password"];

 

    $statement = mysqli_prepare($con, "SELECT staff_num, password, cinema_num FROM STAFF WHERE staff_num = ? AND password = ?");

    mysqli_stmt_bind_param($statement, "ss", $staff_num, $password);
    mysqli_stmt_execute($statement);
     
    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $staff_num, $password, $cinema_num);

    $response = array();

    $response["success"] = false;

    while(mysqli_stmt_fetch($statement)){

        $response["success"] = true;
        $response["staff_num"] = $staff_num;
        $response["password"] = $password;
        $response["cinema_num"] = $cinema_num;
        
    }
    echo json_encode($response);

    /*$success = false;
    while($row=mysqli_fetch_array($statement)){
        $success = true;
        array_push($response,
            array('success'=>$row[0]=$success, 'sno'=>$row[1],'pw'=>$row[2],'site'=>$row[3]));
    }
    echo json_encode(array("response"=>$response));*/
?>
