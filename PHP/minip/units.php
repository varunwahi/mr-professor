<?php

require "connect.php";


$selectedsubject = $_POST["subject"];

$query = "select id from subjects where name = '$selectedsubject'";

$result = mysqli_query($con,$query);

$row = mysqli_fetch_assoc($result);

$subjectid = $row['id'];

if($subjectid =="")
{

exit();

}


$query = "select name from units where id like '$subjectid%'";


$result = mysqli_query($con,$query);

$resultarray = array();

while($row = mysqli_fetch_assoc($result))
{
	$resultarray[] = $row;
	
}

echo json_encode($resultarray);

mysqli_close($con);

?>