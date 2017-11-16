<?php

include("config/mysqli.php");
include("sendEmail.php");
include("config/setting.php");

$email    = $_POST['email'];
$username = $_POST['username'];
$password = $_POST['pwd'];
$category = $_POST['category'];
$mobile   = $_POST['mobile'];


if($mobile==1){ //from android

  if($email!="" && $password!=""){
        $valid = checkEmail($email, $link);
        if($valid==1){
			$newId=insertUser($email, $password, $link);
			$resp["uid"]=$newId;
			header('Content-type: application/json');
			header($_SERVER["SERVER_PROTOCOL"]." 200");
			echo json_encode($resp);
		}
        else{//email duplicate
			header($_SERVER["SERVER_PROTOCOL"]." 400");
			header("CauseId: 0");
        }
	}else{
		header($_SERVER["SERVER_PROTOCOL"]." 400");
		header("CauseId: 1");
	}
}
else { //from website

	if ($email != "" && $password != "") {

    $valid = checkEmail($email, $link);
    
      if($valid == 1) {
        $newId = insertUser($email, $username, $password, $link);
			  echo '<meta http-equiv="Refresh" content="0;url=http://'.$SERVER_DNS.'/cloud_programming/verifyNotice.php">';
		  } else {
        echo '<script>alert("This email has already registered");</script>';
			  echo '<meta http-equiv="Refresh" content="0;url=http://'.$SERVER_DNS.'/cloud_programming/registerPage.php">';
      }

	} else {
    echo '<script>alert("Please enter both email and password");</script>';
    echo '<meta http-equiv="Refresh" content="0;url=http://'.$SERVER_DNS.'/cloud_programming/registerPage.php">';
	}
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

	//insert subscribe
	/*for($i=0;$i<count($category);$i++){
		$catId = $category[$i];
		$sql = "INSERT INTO user_category VALUES (NULL, '$newId', '$catId')";
		$link->query($sql);
	}*/
	sendVerifyEmail($email, $newId);

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
