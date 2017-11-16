<?php
	require 'vendor/autoload.php';
	use Aws\S3\S3Client;
	
	$client = S3Client::factory();
	$bucket = "cp08";	
	
	//insert object into bucket
	$key = 'hello_world3.txt';
	echo "Creating a new object with key {$key}\n";
	$result = $client->putObject(array(
		'Bucket' => $bucket,
		'Key'    => $key,
		'Body'   => "Hello World!"
	));
	
	//list all objects in the bucket
	/*$iterator = $client->getIterator('ListObjects', array(
		'Bucket' => $bucket
	));

	foreach ($iterator as $object) {
		echo $object['Key'] . "\n";
	}*/

?>