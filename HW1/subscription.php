<?php
  use Aws\S3\S3Client;
  use Aws\Sns\SnsClient;

  require_once 'config/mysqli.php';
  require_once 'config/setting.php';

  require_once 'vendor/autoload.php';
  require_once 'subscribe.php';
  session_start();

  $userId = $_SESSION['userId'];
  $email = $_SESSION['email'];
  $username = $_SESSION['username'];
  
  // subscription handle
  $sns_client = sns_init();
  echo 'client done<br>';

  $subscriptions = getSubscriptions($sns_client, $link); // return an array;
  echo 'sub done<br>';
  $animalImgs = ($subscriptions['Animal'] == 1) ? grabImages('Animal', $link) : null;
  $peopleImgs = ($subscriptions['People'] == 1) ? grabImages('People', $link) : null;
  $plantImgs  = ($subscriptions['Plant']  == 1) ? grabImages('Plant' , $link) : null;

  $categories = getCategories($link);
  $catContent = categoriesContent($categories);
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
          <a class="navbar-brand" href="#">Cloud Programming</a>
      </div>

      <!-- Collect the nav links, forms, and other content for toggling -->
      <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
          <ul class="nav navbar-nav">
              <li class=""><a href="./home.php">My Images</a></li>
              <li class="active"><a href="./subscription.php">My Subscription</a></li>
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
                      <li><a href="#">Edit Profile</a></li>
                      <li><a href="./logout.php">Log out</a></li>
                  </ul>
              </li>
          </ul>
      </div><!-- /.navbar-collapse -->

    </div><!-- /.container-fluid -->
  </nav>


  <div class="container">

    <div class="row">
      <div class="col-md-12">
        <form method="post" action="subscription.php">
          <?php
            if(isset($_POST['category'])) {
              // print_r($_POST['category']);
              for ($i = 0; $i < count($_POST['category']); $i++) {
                subscribe($sns_client, $_POST['category'][$i]);
              }
                
            }
          ?>
            
          <div class="form-group">
            <label>Subscribe what you want!</label>
          </div>
          <?php echo $catContent;?>
          
          <input type="submit" value="Subscribe!" class="btn btn-primary">
        </form>
      </div>
    </div>

    <div class="row">
      <?php echo "$animalImgs"; ?>
      <?php echo "$peopleImgs"; ?>
      <?php echo "$plantImgs"; ?>
    </div>
    
  </div>

  <script src="https://code.jquery.com/jquery-1.11.2.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>
</body>
</html>
