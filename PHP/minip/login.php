<?php

require "connect.php";

$user = $_POST["user"];
$pass = $_POST["pass"];

$query = "select uora from users where user = '$user' and pass = '$pass'";

$result = mysqli_query($con,$query);


if (mysqli_num_rows($result) == 1)
{
	$row = mysqli_fetch_assoc($result);
	if($row['uora']=='u')
	{

	echo "successu";
	
	}
	else
	{

	echo "successa";	

	}	
}

else
{
	mysqli_close($con);
	echo "invalid";
}


?>