<?php 

    error_reporting(E_ALL); 
    ini_set('display_errors',1); 

    $host = 'localhost';
    $username = 'root'; # MySQL 계정 아이디
    $password = 'dbwjd2860'; # MySQL 계정 패스워드
    $dbname = 'yujung';  # DATABASE 이름


    $options = array(PDO::MYSQL_ATTR_INIT_COMMAND => 'SET NAMES utf8');
    
    try {

        $con = new PDO("mysql:host={$host};dbname={$dbname};charset=utf8",$username, $password);
    } catch(PDOException $e) {

        die("Failed to connect to the database: " . $e->getMessage()); 
    }


    $con->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
    $con->setAttribute(PDO::ATTR_DEFAULT_FETCH_MODE, PDO::FETCH_ASSOC);

    
 
    header('Content-Type: text/html; charset=utf-8'); 


    $android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");


    if( (($_SERVER['REQUEST_METHOD'] == 'POST') && isset($_POST['submit'])) || $android )
    {

        // 안드로이드 코드의 postParameters 변수에 적어준 이름을 가지고 값을 전달 받습니다.

        $customerNum=$_POST['customerNum'];
        $lostNum=$_POST['lostNum'];
        $detail=$_POST['detail'];
        $date=$_POST['date'];
        $time=$_POST['time'];

        if(empty($customerNum)){
            $errMSG = "customerNum 입력하세요.";
        }
        else if(empty($lostNum)){
            $errMSG = "lostNum 입력하세요.";
        }
        else if(empty($detail)){
            $errMSG = "detail 입력하세요.";
        }
        else if(empty($date)){
            $errMSG = "date 입력하세요.";
        }
        else if(empty($time)){
            $errMSG = "time 입력하세요.";
        }

        if(!isset($errMSG)) // 이름과 나라 모두 입력이 되었다면 
        {
            try{
                // SQL문을 실행하여 데이터를 MySQL 서버의 person 테이블에 저장합니다. 
                #$stmt = $con->prepare('UPDATE lost inner join customer on customer.lno=lost.lno SET customer.state=:cphonestate,lost.state=:lnostate WHERE customer.lno=:lno and customer.cphone=:cphone');

                $stmt = $con->prepare('INSERT into inquiry (customer_num,lost_num,receiving_date,receiving_time,object_detail) VALUES (:customerNum,:lostNum,:date,:time,:detail)');

                $stmt->bindParam(':customerNum', $customerNum);
                $stmt->bindParam(':lostNum', $lostNum);
                $stmt->bindParam(':detail', $detail);
                $stmt->bindParam(':date', $date);
                $stmt->bindParam(':time', $time);

                if($stmt->execute())
                {
                    $successMSG = "새로운 사용자를 추가했습니다.";
                }
                else
                {
                    $errMSG = "사용자 추가 에러";
                }

            } catch(PDOException $e) {
                die("Database error: " . $e->getMessage()); 
            }
        }

    }

?>

