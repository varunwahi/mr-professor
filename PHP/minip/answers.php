<?php

require "connect.php";


$doubtid = $_POST["doubtid"];
$selectedsubject = $_POST["subject"];

$query = "select id from subjects where name = '$selectedsubject'";

$result = mysqli_query($con,$query);

$row = mysqli_fetch_assoc($result);

$subjectid = $row['id'];


if($subjectid =="")
{

exit();

}


$tablename = $subjectid."answers";


$query = "select * from $tablename where id like '$doubtid%' order by votes desc";


$result = mysqli_query($con,$query);

$resultarray = array();

while($row = mysqli_fetch_assoc($result))
{
	$resultarray[] = $row;
	
}

echo json_encode($resultarray);

mysqli_close($con);

?>