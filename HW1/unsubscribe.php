<?php
    use Aws\Sns\SnsClient;
    include('config/mysqli.php');
    include('config/setting.php');
	include('subscribe2.php');
    require_once 'vendor/autoload.php';
    session_start();

    $email = $_SESSION['email'];
    $category = $_POST['category'];

    $sns_client = sns_init();

    for($i=0;$i<count($category);$i++)
    	unsubscribe($sns_client, $email, $category[$i]);

    echo "<meta http-equiv='Refresh' content='1;url=http://".$SERVER_DNS."/cloud_programming/subscription2.php'>";

    function unsubscribe($client, $email, $topic){
    	$subArn = getSubscriptionArn($client, $email, $topic);

    	if($subArn!=""){
	    	$result = $client->unsubscribe(array(
	    		//SubscriptionArn is required
	            'SubscriptionArn' => $subArn,
	        ));
    	}
    }

    function getSubscriptionArn($client, $email, $topic){
    	$result = $client->listSubscriptionsByTopic(array(
    		//TopicArn is required
            'TopicArn' => $topic,
            'NextToken' => '',
        ));

        $subscriptions = $result->get("Subscriptions");
        for($i=0;$i<count($subscriptions);$i++){
            if ($subscriptions[$i]["Endpoint"] == $email 
                && $asubscriptions[$i]["SubscriptionArn"] != 'PendingConfirmation' 
                && $subscriptions[$i]["SubscriptionArn"] != 'Deleted'){
                return $subscriptions[$i]["SubscriptionArn"];
            }
        }
        return "";
    }
	
?>