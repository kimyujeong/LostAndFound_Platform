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

        $lost_num=$_POST['lost_num'];
        $i_state=$_POST['inquirystate'];
        $l_state=$_POST['loststate'];

        if(empty($lost_num)){
            $errMSG = "lost_num를 입력하세요.";
        }
        else if(empty($i_state)){
            $errMSG = "inquirystate를 입력하세요.";
        }
        else if(empty($l_state)){
            $errMSG = "loststate를 입력하세요.";
        }

        if(!isset($errMSG)) // 이름과 나라 모두 입력이 되었다면 
        {
            try{
                // SQL문을 실행하여 데이터를 MySQL 서버의 person 테이블에 저장합니다. 
                $stmt = $con->prepare('UPDATE lost inner join inquiry on inquiry.lost_num=lost.lost_num SET inquiry.processing_state=:i_state,lost.processing_state=:l_state WHERE lost.lost_num=:lost_num and inquiry.lost_num=:lost_num');
                $stmt->bindParam(':lost_num', $lost_num);
                $stmt->bindParam(':i_state', $i_state);
                $stmt->bindParam(':l_state', $l_state);

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
