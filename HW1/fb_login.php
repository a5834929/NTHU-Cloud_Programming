<?php
    include("config/mysqli.php");
    include("config/setting.php");
    session_start();

    $email = $_POST['email'];
	$mobile = $_POST['mobile'];	
	//$mobile=1;
	//$email='lala@la';
	if($mobile==1){ //for android
		$username = $_POST['name'];
		//$username = 'lalaaaa';
		$result=login($email, $link); //get uid(if exist)		
		$cnt = $result->num_rows;
		$uid=-1;
		if($cnt==0){ //first login, insert email
			$uid = insertUser($email, $username, $link);
		}
		else{
			$res = $result->fetch_array(MYSQLI_BOTH);
			$uid = $res['id'];			
		}
		$resp["uid"]=$uid;
		//$resp["uid"]=3;
				
		header($_SERVER["SERVER_PROTOCOL"]." 200");
		echo json_encode($resp);			
	}	
	else{ //for webpage
		$result = login($email, $link);
		$cnt = $result->num_rows;

		if ($cnt == 0) { // email doesn't exist
			echo 'http://'.$SERVER_DNS.'/cloud_programming/index.php';
		} else { //check psw
			$res = $result->fetch_array(MYSQLI_BOTH);
		  
			$_SESSION['email']      = $email;
			$_SESSION['userId']     = $res['id'];
			$_SESSION['username']   = $res['username'];
			echo 'http://'.$SERVER_DNS.'/cloud_programming/home.php';

		}
	}
    

    function login($email, $link){
        $sql = "SELECT * FROM user WHERE email='$email'";
        $result = $link->query($sql);
        return $result;
    }
	
	function insertUser($email, $username, $link){
		//insert user
		$sql = "INSERT INTO user VALUES (NULL, '$email', '$username', NULL, 0)";
		mysqli_query($link, $sql);

		//get user id
		$sql = "SELECT id FROM user WHERE email = '$email'";
		$result = $link->query($sql);
		$res = $result->fetch_array(MYSQLI_BOTH);
		$newId = $res[0];
		
		//sendVerifyEmail($email, $newId);
		return $newId;
	}

?>
