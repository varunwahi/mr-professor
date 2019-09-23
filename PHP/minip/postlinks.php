<?php

require "connect.php";


$selectedunit = $_POST["unit"];
$selectedsubject = $_POST["subject"];
$user = $_POST["user"];
$title = $_POST["title"];
$url = $_POST["url"];


$query = "select id from subjects where name = '$selectedsubject'";
$result = mysqli_query($con,$query);


$row = mysqli_fetch_assoc($result);

$subjectid = $row['id'];



$query = "select id,nol from units where name = '$selectedunit'";
$result = mysqli_query($con,$query);


$row = mysqli_fetch_assoc($result);

$unitid = $row['id'];

$nol = $row['nol'];
$nol++;





if($subjectid =="" || $unitid =="")
{

exit();

}


$query = "update units set nol = nol + 1 where name = '$selectedunit'";
$result = mysqli_query($con,$query);





$tablename = $subjectid."links";

$newlinkid = $unitid.$nol;


$query = "insert into $tablename values ($newlinkid,'$user','$title','$url',0)";
$result = mysqli_query($con,$query);

if(mysqli_affected_rows($con)==1)
{
	echo "success";
}
else
{
	echo "fail";
}



mysqli_close($con);

?>