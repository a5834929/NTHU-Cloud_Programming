<?php
	include("config/mysqli.php");
	include("config/setting.php");
	
	$mobile=$_POST['mobile'];
	//$mobile=1;
	if($mobile==1){//for android
		$arr=getRawImage($link);
		
		header($_SERVER["SERVER_PROTOCOL"]." 200");
		echo json_encode($arr);		
	}

	function getRawImage($link){
		$sql = "SELECT * FROM image WHERE category_id = -1";
		$result = $link->query($sql);
		$arr=array();
		while($res = $result->fetch_array(MYSQLI_BOTH)){
			array_push($arr, $res);
		}
		$result->close();
		//print_r($arr);
		return $arr;
	}
	

?>