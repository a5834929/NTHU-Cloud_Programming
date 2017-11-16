<?php
	use Aws\S3\S3Client;
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

    function categoriesContent($arr) {
        global $sns_client;
        $subscriptions = getSubscriptions($sns_client);
        $response = '';
        for ($i = 0; $i < count($arr); $i++) {
            $checked = '';
            if ( $subscriptions[$arr[$i]['name']] == 1) {
                $checked = 'checked';
            } 

            $response .=
                '<div class="checkbox">
                    <label>
                        <input type="checkbox" name="category[]" value="' .$arr[$i]["arn"]. '"' .$checked. '>' .$arr[$i]["name"].
                    '</label>
                </div>';
        }
            
        return $response;
    }

    function getSubscriptions($client, $link) {
        $response = array();

        $result = $client->listSubscriptionsByTopic(array(
            // TopicArn is required
            'TopicArn'  => 'arn:aws:sns:us-east-1:661664929584:cp08-animal',
            'NextToken' => '',
        ));
        $arr = $result->get("Subscriptions");
        $response["Animal"] = filterUsersSubscription($arr);

        $result = $client->listSubscriptionsByTopic(array(
            // TopicArn is required
            'TopicArn'  => 'arn:aws:sns:us-east-1:661664929584:cp08-people',
            'NextToken' => '',
        ));
        $arr = $result->get("Subscriptions");
        $response["People"] = filterUsersSubscription($arr);

        $result = $client->listSubscriptionsByTopic(array(
            // TopicArn is required
            'TopicArn'  => 'arn:aws:sns:us-east-1:661664929584:cp08-plant',
            'NextToken' => '',
        ));
        $arr = $result->get("Subscriptions");
        $response["Plant"] = filterUsersSubscription($arr);

        return $response;
    }

    function filterUsersSubscription($arr) {
        global $email;
        for( $i = 0; $i < count($arr); $i++ ) {
            if ($arr[$i]["Endpoint"] == $email && $arr[$i]["SubscriptionArn"] != 'PendingConfirmation') {
                return 1;
            }
        }
        return 0;
    }

    function grabImages($cat, $link) {
        $response = [];

        $sql = "SELECT * FROM image WHERE category_id IN (SELECT id FROM category WHERE name = '$cat') ORDER BY date DESC";
        $result = $link->query($sql);

        while( $img = $result->fetch_array(MYSQLI_BOTH) ) {
            array_push($response, $img);
        }
        
        // return $response; // It will return the whole array with img datas
        return imgContent($response, $cat);
    }

    function imgContent($arr, $cat) {
        global $S3_URL;
        $response = '<h3>' .$cat. '</h3>';

        for ( $i = 0; $i < count($arr); $i++ ) {
            if ( $i % 4 == 0 ) {
                $response .= '<div class="row">';
            }
            $response .= '
                <div class="col-md-4">
                    <div class="thumbnail">
                        <img src="' .$S3_URL. '' .$arr[$i]["s3_link"]. '" alt="...">
                        <div class="caption">
                            <h4 class="label label-info">' .$cat. '<h4>
                            <hr>
                            <h3>' .$arr[$i]["title"]. '</h3>
                            <p>'.$arr[$i]["caption"]. '</p>
                        </div>
                    </div>
                </div>';
            if ( $i % 4 == 3 ) {
                $response .= '</div>';
            }
        }
        return $response;
    }
?>
