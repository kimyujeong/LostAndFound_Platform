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
        $staff_num=$_POST['staff_num'];
        $ask_state=$_POST['ask_state'];
        $reject_detail=$_POST['reject_detail'];
        $flag=$_POST['flag'];

        if(empty($ask_num)){
            $errMSG = "ask_num를 입력하세요.";
        }
        else if(empty($staff_num)){
            $errMSG = "staff_num를 입력하세요.";
        }
        else if(empty($ask_state)){
            $errMSG = "ask_state를 입력하세요.";
        }

        if(!isset($errMSG)) // 이름과 나라 모두 입력이 되었다면 
        {
            try{
                // SQL문을 실행하여 데이터를 MySQL 서버의 person 테이블에 저장합니다. 
                $stmt = $con->prepare('UPDATE ASK_LOST SET staff_num=:staff_num, ask_state=:ask_state WHERE ask_num=:ask_num');
                $stmt->bindParam(':ask_num', $ask_num);
                $stmt->bindParam(':staff_num', $staff_num);
                $stmt->bindParam(':ask_state', $ask_state);

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