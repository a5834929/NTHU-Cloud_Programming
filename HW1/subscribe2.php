<?php
    use Aws\Sns\SnsClient;
    
    function sns_init() {
        global $AWS_KEY, $AWS_SECRET;

        $client = SnsClient::factory(array(
            'key' => $AWS_KEY,
            'secret' => $AWS_SECRET,
            'region' => 'us-east-1'
        ));

        return $client;
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
        return $response;
    }

    function categoriesContent($arr, $sub) {
        global $sns_client;
        $subscriptions = getSubscriptions($sns_client);
        $response = '';
        for ($i = 0; $i < count($arr); $i++){
            $status = "";
            if($sub==1)
                $status = ($subscriptions[$arr[$i]['name']]==-1)?'Please check your email for confirmation!':'Already subscribed!';
            $disabled = ($subscriptions[$arr[$i]['name']] == $sub)?'disabled':'';
            $disabled = ($subscriptions[$arr[$i]['name']] == -1)?'disabled':$disabled;
            $tooltip = ($disabled=='disabled')?'data-toggle="tooltip" data-placement="right" title="'.$status.'"':'';
            
            $response .=

                '<div class="checkbox '.$disabled.'-box" >
                    <label '.$tooltip.'>
                        <input type="checkbox" name="category[]" value="' .$arr[$i]["arn"]. '" ' .$disabled.'>' 
                        .$arr[$i]["name"].
                    '</label>
                </div>';
        }
            
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
		
		/*$sql = "SELECT * FROM category";
		$result = $link->query($sql);
		while($res = $result->fetch_array(MYSQLI_BOTH)){
			$list = $client->listSubscriptionsByTopic(array(
				// TopicArn is required
				'TopicArn'  => $res['arn'],
				'NextToken' => '',
			));
			$arr = $list->get("Subscriptions");
			$response[$res['name']] = filterUsersSubscription($arr);
		}*/
        return $response;        
    }

    function filterUsersSubscription($arr) {
        global $email;
        for( $i = 0; $i < count($arr); $i++ ) {
            if($arr[$i]["Endpoint"] == $email 
                && $arr[$i]["SubscriptionArn"] != 'PendingConfirmation' 
                && $arr[$i]["SubscriptionArn"] != 'Deleted'){
                return 1;

            }else if($arr[$i]["Endpoint"] == $email && $arr[$i]["SubscriptionArn"] == 'PendingConfirmation')
                return -1;
        }
        return 0;
    }

    function grabImages($sub, $link) {
        $cat = array();
		$response = array();
	
		$sql = "SELECT id, name FROM category";
		$result = $link->query($sql);
		while($res = $result->fetch_array(MYSQLI_BOTH)){
			if($sub[$res['name']]==1)
				array_push($cat, $res['id']);
		}
		if(count($cat)!=0){
    		$sql = "SELECT title, caption, s3_link, name FROM image,category WHERE category_id IN (";
            for($i=0;$i<count($cat);$i++){
    			$sql .= $cat[$i];
    			if($i<count($cat)-1) $sql .= ",";
    		}
    		$sql .= ") AND image.category_id=category.id ORDER BY image.date DESC";
    		
            $result = $link->query($sql);
            while( $img = $result->fetch_array(MYSQLI_BOTH) )
                array_push($response, $img);
            return imgContent($response);

        }else return "";
    }

    function imgContent($arr) {
        global $S3_URL;
		$mySub = "";
        for($i=0;$i<count($arr);$i++){
            $res = $arr[$i];
            $mySub .= 
				'<div class="col-md-12">
                    <div class="thumbnail sub-img">

                        <div class="caption">
                            <div class="img-title">
                                <h4>' .$res['title']. '</h4>
                                <h5>' .$res['caption']. '</h5>
                            </div>
                        </div>

                        <img src="' .$S3_URL.$res["s3_link"]. '" alt="...">

                    </div>
                </div>';
        }
        return $mySub;
    }
	
	function getAllMergedImg($link){
		global $S3_URL;
		$sql = "SELECT title, caption, s3_link, name FROM image, category WHERE category_id > 0 AND category_id=category.id ORDER BY date DESC";
		$result = $link->query($sql);
		$allSub = "";
		while($res = $result->fetch_array(MYSQLI_BOTH)){
			$allSub .=
				'<div class="col-md-12">
                    <div class="thumbnail sub-img">

                         <div class="caption">
                           
                            <div class="img-title">
                                <h4>' .$res['title']. '</h4>
                                <h5>' .$res['caption']. '</h5>
                            </div>
                        </div>

                        <img src="' .$S3_URL.$res["s3_link"]. '" alt="...">

                    </div>
                </div>';
		}
		return $allSub;
	}
	
?>
