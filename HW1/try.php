<?php
    use Aws\Sns\SnsClient;
    include('config/mysqli.php');
    include('config/setting.php');
	include('subscribe2.php');
    require_once 'vendor/autoload.php';

    $client = sns_init();

	$result = $client->unsubscribe(array(
	    // SubscriptionArn is required
	    'SubscriptionArn' => 'arn:aws:sns:us-east-1:661664929584:cp08-plant:39a3732a-6771-4fc3-8172-6a6a298ff5fd',
	));

	echo $result;


?>
