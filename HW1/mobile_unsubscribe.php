<?php
	use Aws\Sns\SnsClient;
	include('config/mysqli.php');
	include('config/setting.php');
	require_once ('vendor/autoload.php');

	function unsubscribe($client, $topic, $email){
    	$subArn = getSubscriptionArn($client, $topic, $email);

    	if($subArn!=""){
	    	$result = $client->unsubscribe(array(
	    		//SubscriptionArn is required
	            'SubscriptionArn' => $subArn,
	        ));
    	}
    }

    function getSubscriptionArn($client, $topic, $email){
    	$result = $client->listSubscriptionsByTopic(array(
    		//TopicArn is required
            'TopicArn' => $topic,
            'NextToken' => '',
        ));

        $subscriptions = $result->get("Subscriptions");
        for($i=0;$i<count($subscriptions);$i++){
            if ($subscriptions[$i]["Endpoint"] == $email 
                && $subscriptions[$i]["SubscriptionArn"] != 'PendingConfirmation' 
                && $subscriptions[$i]["SubscriptionArn"] != 'Deleted'){
                return $subscriptions[$i]["SubscriptionArn"];
            }
        }
        return "";
    }
	
	
?>