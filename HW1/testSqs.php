<?php
        use Aws\Sqs\SqsClient;
        require 'vendor/autoload.php';
        require_once('config/setting.php');
        include("config/mysqli.php");
        session_start();

        $userId = $_SESSION['userId'];
        $client = SqsClient::factory(array(
                'key' => $AWS_KEY,
                'secret' => $AWS_SECRET,
                'region' => 'us-east-1'
        ));
        $queueUrl = "https://sqs.us-east-1.amazonaws.com/661664929584/input-cp08";
       
        $msg = "";
        $chosenImg = $_POST['merge'];
        $category = $_POST['category'];
        $title = $_POST['title'];
        $caption = $_POST['caption'];
        $imgCnt = count($chosenImg);

        if($imgCnt!=0){
	        for($i=0;$i<$imgCnt;$i++)
	                $msg .= $S3_URL."user_upload/".$chosenImg[$i]."\n";
		
			if($title=="") $title = "|";
			if($caption=="") $caption = "|";

	        $client->sendMessage(array(
	                'QueueUrl'    => $queueUrl,
	                'MessageBody' => $msg,
	                'MessageAttributes' => array(
	                        'User' => array(
	                                'StringValue' => $userId,
	                                'DataType' => 'Number'
	                        ),
	                        'Category' => array(
	                                'StringValue' => $category,
	                                'DataType' => 'Number'
	                        ),
	                        'Title' => array(
	                                'StringValue' => $title,
	                                'DataType' => 'String'
	                        ),
	                        'Caption' => array(
	                                'StringValue' => $caption,
	                                'DataType' => 'String'
	                        )
	                )
	        ));

	        header("Location:home.php");
	        exit;
	    }else{
	    	echo '<script>alert("Please select at least one image!");</script>';
	    	echo '<meta http-equiv="Refresh" content="0;url=home_all.php">';
	    }

?>

