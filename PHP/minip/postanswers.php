<?php

require "connect.php";

$doubtid = $_POST["doubtid"];
$selectedsubject = $_POST["subject"];
$user = $_POST["user"];
$content = $_POST["content"];


$query = "select id from subjects where name = '$selectedsubject'";
$result = mysqli_query($con,$query);


$row = mysqli_fetch_assoc($result);

$subjectid = $row['id'];


if($subjectid =="")
{

exit();

}

$tablename = $subjectid."doubts";

$query = "select noa from $tablename where id = '$doubtid'";
$result = mysqli_query($con,$query);
$row = mysqli_fetch_assoc($result);

$newnoa = ++$row['noa'];


$query = "update $tablename set noa = noa + 1 where id = '$doubtid'";
$result = mysqli_query($con,$query);



$tablename = $subjectid."answers";

$newid = $doubtid.$newnoa;

$query = "insert into $tablename values ($newid,'$user','$content',0)";
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