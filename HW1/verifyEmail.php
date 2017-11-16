<?php
	include("config/mysqli.php");
	include("config/setting.php");
	session_start();

	$userId = substr($_GET['id'], 32);
	$sessId = $_SESSION['userId'];
	
	//if($sessId==$userId)
		verifySuccess($userId, $link);
	/*else
		verifyFailure($userId, $link);*/
	

	function verifySuccess($userId, $link){
		$sql = "UPDATE user SET verified=1 WHERE id='$userId'";
		$link->query($sql);
		global $SERVER_IP;
		echo "Verification Success!<br />Welcome to CP08!";
		echo "<meta http-equiv='Refresh' content='1;url=http://".$SERVER_IP."/cloud_programming/home.php'>";
	}

	function verifyFailure($userId, $link){
		session_unset();
		session_destroy();
		global $SERVER_IP;
		echo "Verification Failure!";
		echo "<meta http-equiv='Refresh' content='1;url=http://".$SERVER_IP."/cloud_programming/registerPage.php'>";
	}
	
?>
