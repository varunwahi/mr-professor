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



switch ($type){

case 'year':


	$query = "insert into year (year,nos) values ('$name',0)";
	$result = mysqli_query($con,$query);

	if(mysqli_affected_rows($con) == 1)
	{

		die('success');

	}
	else
	{

		die('fail');

	}


break;

case 'subject':

	$query = "select id,nos from year where year = '$selectedyear'";
	$result = mysqli_query($con,$query);
	$row = mysqli_fetch_assoc($result);

	$requiredid = $row['id'];
	$nos = $row['nos'];

	$nos++;

	$id = $requiredid.$nos;

	$query = "insert into subjects values ($id,'$name',0)";
	$result = mysqli_query($con,$query);

	$query = "update year set nos = nos + 1 where year = '$selectedyear'";
	$result = mysqli_query($con,$query);

	if(mysqli_affected_rows($con) == 1)
	{

		$tablename = $id."doubts";
		$query = "CREATE TABLE $tablename (
 id int(7) NOT NULL,
 byuser varchar(20) NOT NULL,
 title varchar(80) NOT NULL,
 content varchar(255) NOT NULL,
 votes int(3) NOT NULL,
 noa int(3) NOT NULL
)";
		$result = mysqli_query($con,$query);


		$tablename = $id."links";
		$query = "CREATE TABLE $tablename (
 id int(7) NOT NULL,
 byuser varchar(20) NOT NULL,
 title varchar(80) NOT NULL,
 url varchar(512) NOT NULL,
 votes int(3) NOT NULL
)";
		$result = mysqli_query($con,$query);

		$tablename = $id."tips";
		$query = "CREATE TABLE $tablename (
 id int(7) NOT NULL,
 byuser varchar(20) NOT NULL,
 title varchar(80) NOT NULL,
 content varchar(255) NOT NULL,
 votes int(3) NOT NULL
) ";
		$result = mysqli_query($con,$query);


		$tablename = $id."answers";
		$query = "CREATE TABLE $tablename (
 id int(7) NOT NULL,
 byuser varchar(20) NOT NULL,
 content varchar(255) NOT NULL,
 votes int(3) NOT NULL
)";
		$result = mysqli_query($con,$query);

		die('success');

	}
	else
	{

		die('fail');

	}

break;

case 'unit':

	$query = "select id,nou from subjects where name = '$selectedsubject'";
	$result = mysqli_query($con,$query);
	$row = mysqli_fetch_assoc($result);

	$requiredid = $row['id'];
	$nou = $row['nou'];

	$nou++;

	$id = $requiredid.$nou;

	$query = "insert into units values ($id,'$name',0,0,0)";
	$result = mysqli_query($con,$query);

	$query = "update subjects set nou = nou + 1 where name = '$selectedsubject'";
	$result = mysqli_query($con,$query);

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
