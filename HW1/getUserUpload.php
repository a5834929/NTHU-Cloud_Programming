<?php
	include("config/mysqli.php");
	include("config/setting.php");
	session_start();
	
	$mobile=$_POST['mobile'];
	
	if($mobile==1){//for android
		$userId = $_POST['uid'];
		$arr=getUpload($userId, $link);
		
		header($_SERVER["SERVER_PROTOCOL"]." 200");
		echo json_encode($arr);		
	}

	function getUpload($userId, $link){
		$sql = "SELECT * FROM image WHERE '$userId' = user_id AND category_id<0";
		$result = $link->query($sql);
		$arr=array();
		while($res = $result->fetch_array(MYSQLI_BOTH)){
			array_push($arr, $res);
		}
		$result->close();
		return $arr;
	}
	

?>