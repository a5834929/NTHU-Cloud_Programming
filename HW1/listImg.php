<?php
	/*include("mysqli.php");
	include("config/setting.php");

	$userId = $_SESSION['userId'];
	if($userId!=""){
		
		
	}*/
	
	
	use Aws\S3\S3Client;
	require 'vendor/autoload.php';
	require_once('config/setting.php');
	
	$client = S3Client::factory(array(
		'key' => $AWS_KEY,
		'secret' => $AWS_SECRET
	  ));
	
	/*if (($contents = $client->getBucket($BUCKET_NAME)) !== false) {
        foreach ($contents as $object) {
            print_r($object);
        }
    }*/
	$result = $client->listObjects(array(
		// Bucket is required
		'Bucket' => $BUCKET_NAME
	));
	
	echo $result;

?>