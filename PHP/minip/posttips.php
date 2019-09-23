<?php

require "connect.php";


$selectedunit = $_POST["unit"];
$selectedsubject = $_POST["subject"];
$user = $_POST["user"];
$title = $_POST["title"];
$content = $_POST["content"];


$query = "select id from subjects where name = '$selectedsubject'";
$result = mysqli_query($con,$query);


$row = mysqli_fetch_assoc($result);

$subjectid = $row['id'];



$query = "select id,noti from units where name = '$selectedunit'";
$result = mysqli_query($con,$query);


$row = mysqli_fetch_assoc($result);

$unitid = $row['id'];

$noti = $row['noti'];
$noti++;





if($subjectid =="" || $unitid =="")
{

exit();

}


$query = "update units set noti = noti + 1 where name = '$selectedunit'";
$result = mysqli_query($con,$query);





$tablename = $subjectid."tips";

$newtipid = $unitid.$noti;


$query = "insert into $tablename values ($newtipid,'$user','$title','$content',0)";
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