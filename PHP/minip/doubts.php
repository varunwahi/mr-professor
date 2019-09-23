<?php

require "connect.php";


$selectedunit = $_POST["unit"];
$selectedsubject = $_POST["subject"];

$query = "select id from subjects where name = '$selectedsubject'";

$result = mysqli_query($con,$query);

$row = mysqli_fetch_assoc($result);

$subjectid = $row['id'];



$query = "select id from units where name = '$selectedunit'";



$result = mysqli_query($con,$query);

$row = mysqli_fetch_assoc($result);

$unitid = $row['id'];


if($subjectid =="" || $unitid =="")
{

exit();

}




$tablename = $subjectid."doubts";


$query = "select * from $tablename where id like '$unitid%' order by votes desc";

//,id desc


$result = mysqli_query($con,$query);

$resultarray = array();

while($row = mysqli_fetch_assoc($result))
{
	$resultarray[] = $row;
	
}

echo json_encode($resultarray);

mysqli_close($con);

?>