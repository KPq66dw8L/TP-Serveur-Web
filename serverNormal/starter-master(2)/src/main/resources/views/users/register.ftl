<#ftl encoding="utf-8">
<link rel="stylesheet" href="style.css">

<body xmlns="http://www.w3.org/1999/html">

<ul class="navBar">
    <li><a href="http://localhost:8081/login">login</a></li>
    <li><a href="http://localhost:8081/register">register & profs</a></li>
    <li><a href="http://localhost:8081/users">students</a></li>
</ul>
<h1>Professors :</h1>
<ul>
    <#list users as user>
        <li>${user.id} - ${user.firstName} ${user.lastName} aka ${user.username} <#if logged == true><button data-prof-id="http://localhost:8081/prof/${user.id}/delete" id="delete-prof">Delete</button></#if></li>
    </#list>
</ul>
<h1>Registration :</h1>
<#if logged == false>
    <form method='post' enctype='multipart/form-data'>
        <input type='text' name='firstname' placeholder="firstname">
        <input type='text' name='lastname' placeholder="lastname">
        <input type='text' name='username' placeholder="username">
        <input type='password' name='password' placeholder="password">
    <button>Register / Create prof</button>
    </form>
</#if>
</body>

<script src="./register.js" type="module"></script>

</html>
