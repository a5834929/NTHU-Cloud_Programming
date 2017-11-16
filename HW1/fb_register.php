<?php
    include("config/mysqli.php");
    include("config/setting.php");
    $email    = $_POST['email'];
    $username = $_POST['username'];
    $password = $_POST['pwd'];


    if ($email != "") {

        $valid = checkEmail($email, $link);
        if($valid == 1) {
            
            $newId = insertUser($email, $username, $password, $link);
            echo 'http://'.$SERVER_DNS.'/cloud_programming/home.php';
        } else {
            echo 'http://'.$SERVER_DNS.'/cloud_programming/index.php';
        }
    } else {
        echo 'http://'.$SERVER_DNS.'/cloud_programming/index.php';
    }

    function insertUser($email, $username, $password, $link){
        //insert user
        $sql = "INSERT INTO user VALUES (NULL, '$email', '$username', '$password', 0)";
        mysqli_query($link, $sql);

        //get user id
        $sql = "SELECT id FROM user WHERE email = '$email'";
        $result = $link->query($sql);
        $res = $result->fetch_array(MYSQLI_BOTH);
        $newId = $res[0];
        return $newId;
    }


    function checkEmail($email, $link){
            $sql = "SELECT id FROM user WHERE email = '$email'";
            $result = $link->query($sql);
            $res = $result->fetch_array(MYSQLI_BOTH);
            if(count($res)==0)   return 1;
            else                 return 0;
    }
?>
