<?php

require "connect.php";


$selectedsubject = $_POST["subject"];
$requiredid = $_POST["requiredid"];
$vote = $_POST["vote"];
$type = $_POST["doubtoranswer"];
$username = $_POST["username"];




$query = "select username from votes where username = '$username' and contentid = '$requiredid' and type = '$type'";
$result = mysqli_query($con,$query);

if(mysqli_num_rows($result) > 0){

	mysqli_close($con);
	die("already voted");	

}



$query = "select id from subjects where name = '$selectedsubject'";
$result = mysqli_query($con,$query);

$row = mysqli_fetch_assoc($result);
$subjectid = $row['id'];


if($subjectid =="")
{

exit();

}

if($type=="doubt")
{

$typee = "doubts";
$tablename = $subjectid."doubts";

}
else if($type=="answer")
{

$typee = "answers";
$tablename = $subjectid."answers";

}
else if($type=="link")
{

$typee = "links";
$tablename = $subjectid."links";

}
else if($type=="tip")
{

$typee = "tips";
$tablename = $subjectid."tips";

}


if($vote=="upvote")
{
$query = "update $tablename set votes = votes + 1 where id = $requiredid";
}
else if($vote=="downvote")
{

$query = "select votes from $tablename where id = $requiredid";
$result = mysqli_query($con,$query);
$row = mysqli_fetch_assoc($result);
if($row['votes']==-14)
{

$url = 'http://192.168.43.227/minip/deletecontent.php';
$fields = array(
   'subject' => $selectedsubject,
   'requiredid' => $requiredid,
   'type' => $typee,
);

// build the urlencoded data
$postvars = http_build_query($fields);
$ch = curl_init();

// set the url, number of POST vars, POST data
curl_setopt($ch, CURLOPT_URL, $url);
curl_setopt($ch, CURLOPT_POST, count($fields));
curl_setopt($ch, CURLOPT_POSTFIELDS, $postvars);

// execute post
$result = curl_exec($ch);


// close connection
curl_close($ch);

exit();

}
else
{

$query = "update $tablename set votes = votes - 1 where id = $requiredid";

}
}

$result = mysqli_query($con,$query);


if(mysqli_affected_rows($con)==1)
{
	$query = "insert into votes values('$username','$requiredid','$type')";
	$result = mysqli_query($con,$query);

	if(mysqli_affected_rows($con)==1)
	{
	echo "success";
	}
	else
	{
	echo "fail";
	}
}
else
{
	echo "fail";
}



mysqli_close($con);

?>