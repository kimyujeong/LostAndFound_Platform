<?php
    $con=mysqli_connect("localhost","root","dbwjd2860","yujung");
    mysqli_query($con,'SET NAMES utf8');
 
    $phone_num = $_POST["phone_num"];

    $password = $_POST["password"];

 

    $statement = mysqli_prepare($con, "SELECT phone_num, password, customer_num FROM CUSTOMER WHERE phone_num = ? AND password = ?");

    mysqli_stmt_bind_param($statement, "ss", $phone_num, $password);
    mysqli_stmt_execute($statement);
     
    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $phone_num, $password, $customer_num);

    $response = array();

    $response["success"] = false;

    while(mysqli_stmt_fetch($statement)){
        $response["success"] = true;
        $response["phone_num"] = $phone_num;
        $response["password"] = $password;
        $response["customer_num"] = $customer_num;
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
