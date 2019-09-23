<?php


require "connect.php";


require ('phpmailer\phpmailer.php');
require ('phpmailer\STMP.php');


$email = $_POST["emailaddress"];



$query = "select * from users where emailid = '$email'";
$result = mysqli_query($con,$query);


if (mysqli_num_rows($result) > 0)

{

$row = mysqli_fetch_assoc($result);

$user = $row['user'];
$pass = $row['pass'];




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
    $mail->addAddress($email);     // Add a recipient

    //Content                       
    $mail->Subject = 'Login Credentials';
    $mail->Body    = "Your login credentials are : \n\nUsername : $user \nPassword : $pass ";


    $mail->send();
    echo 'Message has been sent';
    exit();

} catch (Exception $e) {
    echo 'Message could not be sent.';
    echo 'Mailer Error: ' . $mail->ErrorInfo;
}
		
	
}

else
{

	die('invalid');
}


mysqli_close($con);



?>