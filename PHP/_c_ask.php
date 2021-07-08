<?php 

    error_reporting(E_ALL); 
    ini_set('display_errors',1); 

    $conn = mysqli_connect("localhost", "root", "dbwjd2860" , "yujung");
    mysqli_query($conn,'SET NAMES utf8');

    if (isset($_POST['city']) && isset($_POST['branch']) && isset($_POST['ask_object']) && isset($_POST['ask_object_detail']) && isset($_POST['lost_location'])&& isset($_POST['customer_num']))

    $city = $_POST['city'];
    $branch = $_POST['branch'];
    $ask_object = $_POST['ask_object'];
    $ask_object_detail = $_POST['ask_object_detail'];
    $lost_location = $_POST['lost_location'];
    $customer_num = $_POST['customer_num'];

    if(empty($city)){
        $errMSG = "city:";
    }else if(empty($branch)){
        $errMSG = "branch:";
    }else if(empty($ask_object)){
        $errMSG = "ask_object:";
    }else if(empty($ask_object_detail)){
        $errMSG = "ask_object_detail:";
    }else if(empty($lost_location)){
        $errMSG = "lost_location:";
    }else if(empty($customer_num)){
        $errMSG = "customer_num:";
    }

    if(!isset($errMSG))
    {
        try{
            $stmt_cinema_num = "SELECT cinema_num FROM CINEMA WHERE city = '$city' AND branch = '$branch'";
            $cineam_result = mysqli_query($conn, $stmt_cinema_num);
            $row = $cineam_result->fetch_row();
            $cinema_num = (string)$row[0];

            $stmt = "INSERT INTO ASK_LOST(ask_object, ask_object_detail, lost_location, cinema_num, customer_num) VALUES ('$ask_object', '$ask_object_detail', '$lost_location', '$cinema_num', '$customer_num')";
            $result = mysqli_query($conn, $stmt);

            
        } catch(PDOException $e) {
            die("Database error: " . $e->getMessage()); 
        }
        mysqli_close($conn);
    }

?>
