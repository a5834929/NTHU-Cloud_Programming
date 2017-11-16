<?php
	include("config/mysqli.php");
	include("config/setting.php");
	
	//$mobile=$_POST['mobile'];
	$mobile=1;
	if($mobile==1){//for android
		//$email = $_POST['email'];
		$email='000@000';
		$username = $_POST['name']
		$uid=getUserId($email, $link);
		
		//header($_SERVER["SERVER_PROTOCOL"]." 200");
		echo json_encode($uid);		
	}
	
	function getUserId($email, $username, $link){		
		$sql = "SELECT id FROM user WHERE email = '$email'";
		$result = $link->query($sql);		
		$uid=-1;
		while($res = $result->fetch_array(MYSQLI_BOTH)){
			$uid=$res['id'];
		}
		$result->close();
		
		if($uid==-1){
			$sql = "INSERT INTO user VALUES (NULL, '$email', '$username', NULL, 0)";
			mysqli_query($link, $sql);

			//get user id
			$sql = "SELECT id FROM user WHERE email = '$email'";
			$result = $link->query($sql);
			$res = $result->fetch_array(MYSQLI_BOTH);
			$uid = $res[0];
		}
		return $uid;
	}

?>
