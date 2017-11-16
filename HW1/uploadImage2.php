<?php
	include('config/mysqli.php');
	require('config/setting.php');
	require 'vendor/autoload.php';
	use Aws\S3\S3Client;
	session_start();

	$time = Time();	
	$title = $_POST['title'];
	$caption = $_POST['caption'];
	$mobile = $_POST['$mobile'];
	$category_id = -1;

	if($mobile=="1"){ //for android
		$name = $_POST['name'];
		$userId = $_POST['uid'];
		dbInsert($link, $title, $caption, $name, $user_id, $category_id);
		
		header($_SERVER["SERVER_PROTOCOL"]." 204");
	}
	else{ //for webpage
		$name = $_FILES["file"]["name"] . (string)$time;
		$file = $_FILES["file"]["tmp_name"];
		$s3_link = "user_upload/".$name;
		$user_id = $_SESSION['userId'];		
		dbInsert($link, $title, $caption, $name, $user_id, $category_id);
		s3Insert($s3_link, $file);
		echo '<meta http-equiv="Refresh" content="0;url=http://'.$SERVER_DNS.'/cloud_programming/home.php">';
	}
	
	function dbInsert($link, $title, $caption, $name, $user_id, $category_id){
		$sql = "INSERT INTO image VALUES (NULL, '$title', '$caption', '$name', '$user_id', '$category_id', CURRENT_TIMESTAMP)";
		$link->query($sql);		
	}
	
	function s3Insert($s3_link, $file){
		global $BUCKET_NAME, $AWS_KEY, $AWS_SECRET;
		$client = S3Client::factory(array(
			'key' => $AWS_KEY,
			'secret' => $AWS_SECRET
		));

		//insert object into bucket
		$result = $client->putObject(array(
			'Bucket' => $BUCKET_NAME,
			'Key'    => $s3_link,
			'SourceFile'   => $file,
			'ContentType' => 'image/jpeg'
		));
		
	}

?>
