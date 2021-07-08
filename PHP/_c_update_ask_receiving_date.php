<?php 

    error_reporting(E_ALL); 
    ini_set('display_errors',1); 

###########dbcon이랑 똑같음

    include('dbcon.php');
##############################
    
    $android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");


    if( (($_SERVER['REQUEST_METHOD'] == 'POST') && isset($_POST['submit'])) || $android )
    {

        // 안드로이드 코드의 postParameters 변수에 적어준 이름을 가지고 값을 전달 받습니다.

        $ask_num=$_POST['ask_num'];
        $ask_state=$_POST['ask_state'];
        $receiving_date=$_POST['receiving_date'];
        $receiving_time=$_POST['receiving_time'];


        if(empty($ask_num)){
            $errMSG = "ask_num를 입력하세요.";
        }
        else if(empty($ask_state)){
            $errMSG = "ask_state를 입력하세요.";
        }
        else if(empty($receiving_date)){
            $errMSG = "receiving_date를 입력하세요.";
        }
        else if(empty($receiving_time)){
            $errMSG = "receiving_time를 입력하세요.";
        }

        if(!isset($errMSG)) // 이름과 나라 모두 입력이 되었다면 
        {
            try{
                // SQL문을 실행하여 데이터를 MySQL 서버의 person 테이블에 저장합니다. 
                $stmt = $con->prepare('UPDATE ASK_LOST SET ask_state=:ask_state, receiving_date=:receiving_date, receiving_time=:receiving_time WHERE ask_num=:ask_num');
                $stmt->bindParam(':ask_num', $ask_num);
                $stmt->bindParam(':ask_state', $ask_state);
                $stmt->bindParam(':receiving_date', $receiving_date);
                $stmt->bindParam(':receiving_time', $receiving_time);

                if($stmt->execute())
                {
                    $successMSG = "ASK_LOST를 갱신했습니다.";
                }
                else
                {
                    $errMSG = "갱신 에러";
                }

            } catch(PDOException $e) {
                die("Database error: " . $e->getMessage()); 
            }
        }

    }


?>
