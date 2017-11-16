<?php
  include('config/mysqli.php');
  include('config/setting.php');
  include('getUserUpload.php');
  require 'vendor/autoload.php';
  use Aws\S3\S3Client;
  session_start();

  $userId = $_SESSION['userId'];
  $email = $_SESSION['email'];
  $username = $_SESSION['username'];
  $myImg="";
  $result=getUpload($userId, $link);     
  
  for($i=0;$i<count($result);$i++){
    if($i%3==0) $myImg .= '<div class="row"> ';

  	$res = $result[$i];
    $imgLink = $S3_URL.'user_upload/'.$res['s3_link'];
    $myImg .=

        '<div class="col-md-4">
            <div class="thumbnail raw-img">
              
                <div class="caption">
                    <div class="img-title">
                        <h4>' .$res['title']. '</h4>
                        <h5>' .$res['caption']. '</h5>
                    </div>
                </div>

                <img src="' .$imgLink.'" alt="..." class="portrait">

            </div>
        </div>';

      if($i%3==2) $myImg .= '</div>';
  }
  if($i%3==2) $myImg .= '</div>'; 

?>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Home</title>
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css">
  <link rel="stylesheet" href="//maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css">
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
          <li class="active"><a href="./home.php">Home<span class="sr-only">(current)</span></a></li>
          <li><a href="./subscription2.php">Subscription</a></li>
          <li><a href="./home_all.php">All</a></li>
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

    <div class="col-md-3">
        <div class="row" style="position:fixed;">
            <div class="col-md-12">
                <form enctype="multipart/form-data" method="post" action="uploadImage.php">
                    <div class="form-group">
                        <label for="title">Title</label>
                        <input type="text" class="form-control" name="title" id="title" placeholder="Title">
                    </div>
                    <div class="form-group">
                        <label for="caption">Caption</label>
                        <input type="text" class="form-control" name="caption" id="caption" placeholder="Caption">
                    </div>
                    <div class="form-group">
                        <label>Select an Image</label>
                        <input type="file" name="file">
                    </div>
                    <input type="submit" value="Upload Image" class="btn btn-primary">
                </form>
            </div>
        </div>
    </div>

    <div class="col-md-9">
        <div class="row">
            <h3>Upload More!</h3>
            <hr class="style-two">
        </div>
        <?php echo $myImg;?>
    </div>

  </div>

    <!-- Large modal -->
    <button type="button" class="btn btn-primary modelBtn" data-toggle="modal" data-target=".bs-example-modal-lg" style="display:none;"></button>

    <div class="modal fade bs-example-modal-lg" tabindex="-1" role="dialog" aria-labelledby="myLargeModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-lg">
            <div class="modal-content">
              <img>
            </div>
        </div>
    </div>


    <script src="https://code.jquery.com/jquery-1.11.2.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>
    <script src="home.js"></script>
    
</body>
</html>
