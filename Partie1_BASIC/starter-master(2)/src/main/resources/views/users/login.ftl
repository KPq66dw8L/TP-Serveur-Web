<#ftl encoding="utf-8">

<body xmlns="http://www.w3.org/1999/html">

<ul>
    <li><a href="http://localhost:8081/login">login</a></li>
    <li><a href="http://localhost:8081/register">register</a></li>
    <li><a href="http://localhost:8081/users">students</a></li>
</ul>


    <h1>Login</h1>

<#if logged == false>
    <form method='post' enctype='multipart/form-data'>
        <input type='text' name='username' placeholder="username">
        <input type='text' name='password' placeholder="password">

    <button>Login</button>
    </form>
</#if>
<h1>${msg}</h1>

</body>

</html>
