<?php

require "connect.php";

$name = $_POST['name'];
$type = $_POST['type'];


if(isset($_POST['selectedyear']))
{
$selectedyear = $_POST['selectedyear'];
}
if(isset($_POST['selectedsubject']))
{
$selectedsubject = $_POST['selectedsubject'];
}


function deletesubject($name)
{

	global $con;

	$query = "select id from subjects where name = '$name'";
	$result = mysqli_query($con,$query);
	$row = mysqli_fetch_assoc($result);
	$subjectid = $row['id'];

	$tablename = $subjectid."doubts";
	$query = "drop table $tablename";
	$result = mysqli_query($con,$query);

	$tablename = $subjectid."links";
	$query = "drop table $tablename";
	$result = mysqli_query($con,$query);

	$tablename = $subjectid."tips";
	$query = "drop table $tablename";
	$result = mysqli_query($con,$query);

	$tablename = $subjectid."answers";
	$query = "drop table $tablename";
	$result = mysqli_query($con,$query);

	
	$query = "delete from units where id like '$subjectid%'";
	$result = mysqli_query($con,$query);

	$query = "delete from subjects where name = '$name'";
	$result = mysqli_query($con,$query);


	//$query = "update year set nos = nos - 1 where year = '$selectedyear'";
	//$result = mysqli_query($con,$query);


}
 

switch ($type){

case 'year':


	$query = "select id from year where year = '$name'";
	$result = mysqli_query($con,$query);
	$row = mysqli_fetch_assoc($result);
	$yearid = $row['id'];

	$query = "select name from subjects where id like '$yearid%'";
	$result = mysqli_query($con,$query);

	while($row = mysqli_fetch_array($result))
	{
		deletesubject($row['name']);
	}

	$query = "delete from year where year = '$name'";

	$result = mysqli_query($con,$query);

	if(mysqli_affected_rows($con)==1)
	{

		die('success');

	}
	else
	{

		die('fail');

	}


break;

case 'subject':

	deletesubject($name);
	die('success');

break;

case 'unit':

	$query = "select id from units where name = '$name'";
	$result = mysqli_query($con,$query);
	$row = mysqli_fetch_assoc($result);
	$unitid = $row['id'];

	$query = "select id from subjects where name = '$selectedsubject'";
	$result = mysqli_query($con,$query);
	$row = mysqli_fetch_assoc($result);
	$subjectid = $row['id'];

	
	$tablename = $subjectid."doubts";
	$query = "delete from $tablename where id like '$unitid%'";
	$result = mysqli_query($con,$query);

	$tablename = $subjectid."links";
	$query = "delete from $tablename where id like '$unitid%'";
	$result = mysqli_query($con,$query);

	$tablename = $subjectid."tips";
	$query = "delete from $tablename where id like '$unitid%'";
	$result = mysqli_query($con,$query);

	$tablename = $subjectid."answers";
	$query = "delete from $tablename where id like '$unitid%'";
	$result = mysqli_query($con,$query);
	


	$query = "delete from units where name = '$name'";
	$result = mysqli_query($con,$query);


	//$query = "update subjects set nou = nou - 1 where name = '$selectedsubject'";
	//$result = mysqli_query($con,$query);

	if(mysqli_affected_rows($con) == 1)
	{

		die('success');

	}
	else
	{

		die('fail');

	}

break;


}

mysqli_close($con);

?>
