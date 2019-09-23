<?php


require "connect.php";


require ('phpmailer\phpmailer.php');
require ('phpmailer\STMP.php');


$newemail = $_POST["newemail"];


$query = "select emailid from users where emailid = '$newemail'";

$result = mysqli_query($con,$query);


if (mysqli_num_rows($result) > 0)
{
	mysqli_close($con);
	die("email already registered");	
}



$vercode = mt_rand(100000, 999999);

$query = "select emailid from userverification where emailid = '$newemail'";


$result = mysqli_query($con,$query);


if (mysqli_num_rows($result) > 0)
{
	$query = "update userverification set code =  '$vercode',datetime = NOW() where emailid = '$newemail'";
	mysqli_query($con,$query);
}

else
{
	$query = "insert into userverification values('$vercode','$newemail',NOW())";
	mysqli_query($con,$query);
}



$mail = new PHPMailer(true);
try {
    //Server settings
    $mail->SMTPDebug = 0;                                 // Enable verbose debug output
    $mail->isSMTP();                                      // Set mailer to use SMTP
    $mail->Host = 'smtp.gmail.com';  // Specify main and backup SMTP servers
    $mail->SMTPAuth = true;                               // Enable SMTP authentication
    $mail->Username = '';                 // SMTP username
    $mail->Password = '';                           // SMTP password
    $mail->SMTPSecure = 'tls';                            // Enable TLS encryption, `ssl` also accepted
    $mail->Port = 587;                                    // TCP port to connect to

    //Recipients
    $mail->setFrom('mrprofessorcustcare@gmail.com','Mr. Professor');
    $mail->addAddress($newemail);     // Add a recipient

    //Content                       
    $mail->Subject = 'Verification Code';
    $mail->Body    = 'Your Verification Code is : '.$vercode.". Enter this code in the app.";


    $mail->send();
    echo 'Message has been sent';
} catch (Exception $e) {
    echo 'Message could not be sent.';
    echo 'Mailer Error: ' . $mail->ErrorInfo;
}



mysqli_close($con);



?>