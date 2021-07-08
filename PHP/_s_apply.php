<?php 

    error_reporting(E_ALL); 
    ini_set('display_errors',1); 

    include('dbcon.php');


    $android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");


    if( (($_SERVER['REQUEST_METHOD'] == 'POST') && isset($_POST['submit'])) || $android )
    {

        $lost_object= !empty($_POST['lost_object']) ? $_POST['lost_object'] : '';
        $importance=!empty($_POST['importance']) ? $_POST['importance'] : '';
        $discovery_location=!empty($_POST['discovery_location']) ? $_POST['discovery_location'] : '';
        $lost_object_detail=!empty($_POST['lost_object_detail']) ? $_POST['lost_object_detail'] : '';
        $staff_num=!empty($_POST['staff_num']) ? $_POST['staff_num'] : '';
        $cinema_num=!empty($_POST['cinema_num']) ? $_POST['cinema_num'] : '';

        if(empty($lost_object)){
            $errMSG = "lost_object:";
        }else if(empty($importance)){
            $errMSG = "importance:";
        }else if(empty($discovery_location)){
            $errMSG = "discovery_location:";
        }else if(empty($lost_object_detail)){
            $errMSG = "lost_object_detail:";
        }else if(empty($staff_num)){
            $errMSG = "staff_num:";
        }else if(empty($cinema_num)){
            $errMSG = "cinema_num:";
        }

        if(!isset($errMSG))
        {
            try{
                $stmt = $con->prepare('INSERT INTO lost(lost_object, cinema_num, discovery_location, staff_num, lost_object_detail, importance) VALUES(:lost_object, :cinema_num, :discovery_location, :staff_num, :lost_object_detail, :importance)');
                $stmt->bindParam(':lost_object', $lost_object);
                $stmt->bindParam(':importance', $importance);
                $stmt->bindParam(':discovery_location', $discovery_location);
                $stmt->bindParam(':lost_object_detail', $lost_object_detail);
                $stmt->bindParam(':staff_num', $staff_num);
                $stmt->bindParam(':cinema_num', $cinema_num);

                if($stmt->execute()){
                    $successMSG = "새로운 사용자를 추가했습니다.";
                }else{
                    $errMSG = "사용자 추가 에러";
                }
            } catch(PDOException $e) {
                die("Database error: " . $e->getMessage()); 
            }
        }

    }

?>


<?php 
    if (isset($errMSG)) echo $errMSG;
    if (isset($successMSG)) echo $successMSG;

    $android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");
   
    if( !$android )
    {
?>
    <html>
       <body>

            <form action="<?php $_PHP_SELF ?>" method="POST">
                Category: <input type = "text" category = "category" />
                Grade: <input type = "text" grade = "grade" />
                Location: <input type = "text" location = "location" />
                Info: <input type = "text" info = "info" />
                Sno: <input type = "text" info = "sno" />
                Site: <input type = "text" info = "site" />
                <input type = "submit" name = "submit" />
            </form>
       
       </body>
    </html>

<?php 
    }
?>