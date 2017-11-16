<?php

?>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Cloud Programming</title>
  <!-- Latest compiled and minified CSS -->
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css">

</head>
<body>


  <div class="container">
    <div class="col-md-4 col-md-offset-4">
      <div class="panel panel-default">
        <div class="panel-heading">Login</div>
        <div class="panel-body">
          <form action="login.php" method="post">
            <div class="form-group">
                    <label for="email">Email address</label>
                    <input type="email" class="form-control" id="email" name="email" placeholder="Enter email">
            </div>
            <div class="form-group">
                    <label for="pwd">Password</label>
                    <input type="password" class="form-control" id="pwd" name="pwd" placeholder="Password">
            </div>

            <button type="submit" class="btn btn-default">Login</button>
          </form>
        </div>
        <div class="panel-footer">
          <a href="#" id="fblogin">Facebook</a><br>
          <a href="./registerPage.php">Register</a>
        </div>
      </div>
    </div>
  </div>


  <!-- Latest compiled and minified JavaScript -->
  <script src="//code.jquery.com/jquery-1.11.2.min.js"></script>
  <script src="//code.jquery.com/jquery-migrate-1.2.1.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>
  <script src="./fb_setting.js"></script>
  <script src="./index.js"></script>
</body>
</html>
