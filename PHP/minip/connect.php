<?php

$db_name = "id2133425_minip";
$user = "";
$pass = "";
$server_name = "localhost";

$con=mysqli_connect($server_name,$user,$pass,$db_name);

if(!$con)
{
	die("Connection error".mysql_connect_error());
}


?>