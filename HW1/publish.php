<?php
    require_once 'config/setting.php';
    require_once 'vendor/autoload.php';
    use Aws\Sns\SnsClient;

    function publish($client, $topic, $imgLink) {
        $message = 'Hello dude, something has just changed!\n Please check out the website '.
                    'or just click on the folowing link!\n'.$imgLink."\n".
        $result = $client->publish(array(
            'TopicArn' => $topic,
            // Message is required
            'Message' => $message,
            'Subject' => 'Update Notification', 
        ));
    }

        
?>

    
