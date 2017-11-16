<?php
	include("config/mysqli.php");
	include("config/setting.php");
	session_start();

	$email = $_POST['email'];
	$password = $_POST['pwd'];
	$mobile = $_POST['mobile'];

	if($mobile==1){ //for android
		if($email!="" && $password!=""){
			$result=login($email,$password, $link);
			$cnt=$result->num_rows;
			if($cnt==0){ // email doesn't exist
				header($_SERVER["SERVER_PROTOCOL"]." 400");
				header("CauseId: 0");
			}else{ //check psw
				$res = $result->fetch_array(MYSQLI_BOTH);
				$pwd = $res['pwd'];
				if(strcmp($password, $pwd)!=0){ //wrong psw
					header($_SERVER["SERVER_PROTOCOL"]." 401");
				}else{//correct psw
					$resp["uid"]=$res['id'];
					header($_SERVER["SERVER_PROTOCOL"]." 200");

					echo json_encode($resp);
				}
			}
		}
		else{
			header($_SERVER["SERVER_PROTOCOL"]." 400");
			header("CauseId: 1");
		}

	}
	else {//for website
		if ($email != "" && $password != ""){

			$result = login($email,$password, $link);
			$cnt = $result->num_rows;

			if ($cnt == 0) { // email doesn't exist
				echo "<script>alert('Email does not exist. Please register!');</script>";
				echo "<meta http-equiv='Refresh' content='0;url=http://".$SERVER_DNS."/cloud_programming/index.php'>";
			} else { //check psw
				$res = $result->fetch_array(MYSQLI_BOTH);
				$pwd = $res['pwd'];

				if (strcmp($password, $pwd) != 0) { //wrong psw
					echo "<script>alert('Wrong password!');</script>";
					echo "<meta http-equiv='Refresh' content='0;url=http://".$SERVER_DNS."/cloud_programming/index.php'>";
				} else {//correct psw
					$_SESSION['email'] 		= $email;
					$_SESSION['userId'] 	= $res['id'];
					$_SESSION['username'] = $res['username'];
					echo "<meta http-equiv='Refresh' content='0;url=http://".$SERVER_DNS."/cloud_programming/home.php'>";
				}
			}
		}
		else {
			echo "<script>alert('Please enter both email and password');</script>";
			echo "<meta httpd-equiv='Refresh' content='0;url=http://".$SERVER_DNS."/cloud_programming/index.php'>";
		}
	}


	function login($email, $password, $link){
		$sql = "SELECT * FROM user WHERE email='$email'";
		$result = $link->query($sql);
		return $result;
	}
?>
