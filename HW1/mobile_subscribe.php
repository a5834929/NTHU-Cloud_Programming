<?php
    use Aws\Sns\SnsClient;
	include('config/mysqli.php');
	include('config/setting.php');
    include('mobile_unsubscribe.php');
	require_once 'vendor/autoload.php';
    
	//get para
	$mobile = $_POST["mobile"];
	$action = $_POST["action"];	
	$email = $_POST["email"];
	
	/*$mobile=1;
	$action="update";
	$email="yilin1218@gmail.com";*/
	
	//start
	if($mobile==1){
		$sns_client=sns_init();
		if($action=="get"){ //get user subscribion
			$categories = getCategories($link);
			$catContent = categoriesContent($categories);
			
			echo json_encode($catContent);
		}
		if($action=="update"){ //subscribe or unsubscribe
			$categories = getCategories($link);
			$catmap = createCatmap($categories);
			
			$numSub = $_POST["numSub"];
			$numUnsub = $_POST["numUnsub"];
			for($i=0;$i<$numSub;$i++){
				$id = $_POST["sub".$i];
				subscribe($sns_client, $catmap[$id]);
			}	
            for($i=0;$i<$numUnsub;$i++){
                $id = $_POST["unsub".$i];
                unsubscribe($sns_client, $catmap[$id], $email);
            }
		}
	}
	
    function sns_init() {
        global $AWS_KEY, $AWS_SECRET;

        $client = SnsClient::factory(array(
            'key' => $AWS_KEY,
            'secret' => $AWS_SECRET,
            'region' => 'us-east-1'
        ));

        return $client;
    }

	function createCatmap($categories){
		$map = array();
		for ($i = 0; $i < count($categories); $i++) {
			$map[$categories[$i]['id']]=$categories[$i]['arn'];            		
        }
		return $map;
	}

    function subscribe($client, $topic) {
        global $email;
        $result = $client->subscribe(array(
            // TopicArn is required
            'TopicArn' => $topic,
            // Protocol is required
            'Protocol' => 'email',
            'Endpoint' => $email,
        ));
    }

    function getCategories($link) {
        $response = [];
        $sql = "SELECT * FROM category";
        $result = $link->query($sql);
        while($res = $result->fetch_array(MYSQLI_BOTH)){
            array_push($response, $res);
        }
		$result->close();
        return $response;
    }

    function categoriesContent($arr) {
        global $sns_client, $link;
        $subscriptions = getSubscriptions($sns_client, $link);
        $response = array();
		$isSub=array();
		$pending = array();
        for ($i = 0; $i < count($arr); $i++) {
            if ( $subscriptions[$arr[$i]['name']] == 1) { //sub
                array_push($isSub, $arr[$i]['id']);
            } 
			else if($subscriptions[$arr[$i]['name']] == -1){ //pending
				array_push($pending, $arr[$i]['id']);
			}			
        }
		array_push($response, $isSub);
		array_push($response, $pending);
        return $response;
    }

    function getSubscriptions($client, $link) {
        $response = array();
		
		$result = $client->listSubscriptionsByTopic(array(
            // TopicArn is required
            'TopicArn'  => 'arn:aws:sns:us-east-1:661664929584:cp08-people',
            'NextToken' => '',
        ));
        $arr = $result->get("Subscriptions");
        $response["People"] = filterUsersSubscription($arr);

        $result = $client->listSubscriptionsByTopic(array(
            // TopicArn is required
            'TopicArn'  => 'arn:aws:sns:us-east-1:661664929584:cp08-animal',
            'NextToken' => '',
        ));
        $arr = $result->get("Subscriptions");
        $response["Animal"] = filterUsersSubscription($arr);

        $result = $client->listSubscriptionsByTopic(array(
            // TopicArn is required
            'TopicArn'  => 'arn:aws:sns:us-east-1:661664929584:cp08-plant',
            'NextToken' => '',
        ));
        $arr = $result->get("Subscriptions");
        $response["Plant"] = filterUsersSubscription($arr);		
        return $response;        
    }

    function filterUsersSubscription($arr) { //1: sub, -1:pennding, 0:no
        global $email;
        for( $i = 0; $i < count($arr); $i++ ) {
            if ($arr[$i]["Endpoint"] == $email && $arr[$i]["SubscriptionArn"] != 'PendingConfirmation' && $arr[$i]["SubscriptionArn"]!="Deleted")
                return 1;            
			else if($arr[$i]["Endpoint"] == $email && $arr[$i]["SubscriptionArn"] == 'PendingConfirmation')
				return -1;
        }
        return 0;
    }   	
?>
