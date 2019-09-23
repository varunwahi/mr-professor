<?php

require "connect.php";


$selectedyear = $_POST["year"];

$query = "select id from year where year = '$selectedyear'";

$result = mysqli_query($con,$query);

$row = mysqli_fetch_assoc($result);

$yearid = $row['id'];

if($yearid =="")
{

exit();

}


$query = "select name from subjects where id like '$yearid%'";


$result = mysqli_query($con,$query);

$resultarray = array();

while($row = mysqli_fetch_assoc($result))
{
	$resultarray[] = $row;
	
}

echo json_encode($resultarray);

mysqli_close($con);

?>