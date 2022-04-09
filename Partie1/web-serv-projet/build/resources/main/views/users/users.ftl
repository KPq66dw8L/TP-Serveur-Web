<#ftl encoding="utf-8">

<body xmlns="http://www.w3.org/1999/html">

<ul>
    <#list users as user>
        <li>${user.id} - ${user.firstName} ${user.lastName} in ${user.group} </li>
        <#if x == 1>
            <button>Add gommette</button>
        <#else>
            x is not 1
        </#if>
    </#list>
</ul>

<form method='post' enctype='multipart/form-data'>
  <input type='text' name='firstname' >
  <input type='text' name='lastname' >
  <input type='text' name='group' >

  <button>Add student</button>
</form>


</body>

</html>
