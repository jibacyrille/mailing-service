<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Spring Boot Email using FreeMarker</title>
</head>
<body>
<h1> <font color="red">Ici iL s'agit du template d'avertissement</font></h1>
<hr>
<div style="margin-top: 10px">Attention, ${mailInfo.userInfo.name}</div>
<div>Votre nom d'utilisateur est: <b>${mailInfo.userInfo.username}</b></div>
<div>Votre adresse est: <b>${mailInfo.userInfo.email}</b></div>
<br/>
<div> Attention à ce que vous faites!</div>
</body>
</html>