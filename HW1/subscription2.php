<?php
    use Aws\S3\S3Client;
    use Aws\Sns\SnsClient;

    include('config/mysqli.php');
    include('config/setting.php');

    require_once 'vendor/autoload.php';
    require_once 'subscribe2.php';
    session_start();

    $userId = $_SESSION['userId'];
    $email = $_SESSION['email'];
    $username = $_SESSION['username'];


    // subscription handle
    $sns_client = sns_init();
    $subscriptions = getSubscriptions($sns_client, $link); // return an array;

    $mySub = grabImages($subscriptions,$link);  
    $allSub = getAllMergedImg($link);

    $categories = getCategories($link);
    $subContent = categoriesContent($categories, 1);
    $unsubContent = categoriesContent($categories, 0);
?>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Home</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css">
    <link rel="stylesheet" href="./css/home.css">
</head>
<body>

    <nav class="navbar navbar-default">
        <div class="container-fluid">
            <!-- Brand and toggle get grouped for better mobile display -->
            <div class="navbar-header">
                <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand" href="./home.php">Cloud Programming</a>
            </div>

            <!-- Collect the nav links, forms, and other content for toggling -->
            <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
                <ul class="nav navbar-nav">
                    <li class=""><a href="./home.php">Home</a></li>
                    <li class="active"><a href="./subscription2.php">Subscription</a></li>
                    <li class=""><a href="./home_all.php">All<span class="sr-only">(current)</span></a></li>
                </ul>

                <ul class="nav navbar-nav navbar-right">
                    <li class="dropdown">
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false">
                        <?php
                            if($username!=null) echo $username;
                            else echo $email;
                        ?>
                        <span class="caret"></span></a>
                        <ul class="dropdown-menu" role="menu">
                          <li><a href="./logout.php">Log out</a></li>
                        </ul>
                    </li>
                </ul>
            </div><!-- /.navbar-collapse -->
        </div><!-- /.container-fluid -->
    </nav>

<div class="container-fluid">

    <div class="col-md-2">
        <div class="row">
            <div class="col-md-8">
                <form method="post" action="subscription2.php">
                    <?php
                        if(isset($_POST['category'])) 
                            for ($i = 0; $i < count($_POST['category']); $i++)
                                subscribe($sns_client, $_POST['category'][$i]);
                    ?>
                    <div class="form-group">
                        <label>Subscribe!</label>
                    </div>
                    <?php echo $subContent;?>
                    <input type="submit" value="Subscribe!" class="btn btn-primary">
                </form>
            </div>
        </div></br></br>
        <div class="row">
            <div class="col-md-8">
                <form method="post" action="unsubscribe.php">
                    <?php
                        if(isset($_POST['category'])) 
                            for ($i = 0; $i < count($_POST['category']); $i++)
                                subscribe($sns_client, $_POST['category'][$i]);
                    ?>
                
                    <div class="form-group">
                        <label>Unsubscribe!?</label>
                    </div>
                    <?php echo $unsubContent;?>
                    <input type="submit" value="Unsubscribe!" class="btn btn-primary">
                </form>
            </div>
        </div>
    </div>
    <div class="col-md-5" id="mySub">
        <h3>These are your subscriptions!</h3>
        <hr class="style-two">
        <?php echo $mySub;?>
    </div>
    <div class="col-md-5" id="allSub">
        <h3>Take a look at what others have done!</h3>
        <hr class="style-two">
        <?php echo $allSub;?>
    </div>
    

    <button type="button" class="btn btn-primary modelBtn" data-toggle="modal" data-target=".bs-example-modal-lg" style="display:none;"></button>

    <div class="modal fade bs-example-modal-lg" tabindex="-1" role="dialog" aria-labelledby="myLargeModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-lg">
            <div class="modal-content">
              <img>
            </div>
        </div>
    </div>
    
    </div>

    <script src="https://code.jquery.com/jquery-1.11.2.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>
    <script src="home.js"></script>
</body>
</html>
