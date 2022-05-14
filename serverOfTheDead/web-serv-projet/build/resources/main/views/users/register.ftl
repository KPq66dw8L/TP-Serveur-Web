<#ftl encoding="utf-8">

<body xmlns="http://www.w3.org/1999/html">

<ul>
    <#list users as user>
        <li>${user.id} - ${user.firstName} ${user.lastName} aka ${user.username} shhhh: ${user.salt} ${user.hashedPassword} </li>
    </#list>
</ul>

<form method='post' enctype='multipart/form-data'>
    <input type='text' name='firstname' >
    <input type='text' name='lastname' >
    <input type='text' name='username' >
    <input type='text' name='password' >
<button>Create prof</button>
</form>

</body>

</html>
