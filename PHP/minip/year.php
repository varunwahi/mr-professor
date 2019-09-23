<?php

require "connect.php";


$query = "select year from year";

$result = mysqli_query($con,$query);

$resultarray = array();

while($row = mysqli_fetch_assoc($result))
{
	$resultarray[] = $row;
	
}

echo json_encode($resultarray);

mysqli_close($con);

?>
