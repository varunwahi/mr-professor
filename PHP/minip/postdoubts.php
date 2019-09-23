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



$query = "select id,nod from units where name = '$selectedunit'";
$result = mysqli_query($con,$query);


$row = mysqli_fetch_assoc($result);

$unitid = $row['id'];

$nod = $row['nod'];
$nod++;





if($subjectid =="" || $unitid =="")
{

exit();

}


$query = "update units set nod = nod + 1 where name = '$selectedunit'";
$result = mysqli_query($con,$query);





$tablename = $subjectid."doubts";

$newdoubtid = $unitid.$nod;


$query = "insert into $tablename values ($newdoubtid,'$user','$title','$content',0,0)";
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