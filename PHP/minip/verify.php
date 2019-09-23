<?php


require "connect.php";


require ('phpmailer\phpmailer.php');
require ('phpmailer\STMP.php');


$emailtobeverified = $_POST["emailtobeverified"];
$enteredcode = $_POST["enteredcode"];
$user = $_POST["user"];
$pass = $_POST["pass"];


$query = "select code from userverification where emailid = '$emailtobeverified'";

$result = mysqli_query($con,$query);


$row = mysqli_fetch_assoc($result);

if($enteredcode == $row['code'])
{

	$query = "insert into users(emailid,user,pass,uora) values('$emailtobeverified','$user','$pass','u')";
	mysqli_query($con,$query);

	$query = "delete from userverification where emailid = '$emailtobeverified'";
	mysqli_query($con,$query);
	
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
    $mail->addAddress($emailtobeverified);     // Add a recipient

    //Content                       
    $mail->Subject = 'Verification Successful';
    $mail->Body    = "Your login credentials are : \n\nUsername : $user \nPassword : $pass ";


    $mail->send();
    echo 'Message has been sent';
} catch (Exception $e) {
    echo 'Message could not be sent.';
    echo 'Mailer Error: ' . $mail->ErrorInfo;
}


	
}

else
{
	echo 'failed';	
}




mysqli_close($con);



?>