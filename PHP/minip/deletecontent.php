<?php

require "connect.php";


$selectedsubject = $_POST["subject"];
$requiredid = $_POST["requiredid"];
$type= $_POST["type"];

$query = "select id from subjects where name = '$selectedsubject'";
$result = mysqli_query($con,$query);

$row = mysqli_fetch_assoc($result);
$subjectid = $row['id'];



if($subjectid =="")
{

exit();

}

if($type=="links")
{

$tablename = $subjectid."links";


$query = "delete from $tablename where id = $requiredid";
$result = mysqli_query($con,$query);



if(mysqli_affected_rows($con)==1)
{
	mysqli_close($con);
	die("success");
}
else
{
	mysqli_close($con);
	die("fail");
}

}

else if($type=="doubts")
{

$tablename = $subjectid."answers";
$query = "delete from $tablename where id like '$requiredid%'";
$result = mysqli_query($con,$query);

$tablename = $subjectid."doubts";
$query = "delete from $tablename where id = '$requiredid'";
$result = mysqli_query($con,$query);
if(mysqli_affected_rows($con)==1)
{
	mysqli_close($con);
	die("success");
}
else
{
	mysqli_close($con);
	die("fail");
}


}

else if($type=="answers")
{

$tablename = $subjectid."answers";

$query = "delete from $tablename where id = '$requiredid'";
$result = mysqli_query($con,$query);
if(mysqli_affected_rows($con)==1)
{
	mysqli_close($con);
	die("success");
}
else
{
	mysqli_close($con);
	die("fail");
}


}

else if($type=="tips")
{

$tablename = $subjectid."tips";

$query = "delete from $tablename where id = '$requiredid'";
$result = mysqli_query($con,$query);
if(mysqli_affected_rows($con)==1)
{
	mysqli_close($con);
	die("success");
}
else
{
	mysqli_close($con);
	die("fail");
}


}



?>