<#ftl encoding="utf-8">

<body xmlns="http://www.w3.org/1999/html">

<ul>
    <#list users as user>
        <li>${user.id} - ${user.firstName} ${user.lastName}</li>
    </#list>
</ul>

<form method='post' enctype='multipart/form-data'>
  <input type='text' name='firstname' >
  <input type='text' name='lastname' >
  <input type='text' name='prof' >
  <input type='text' name='blanche' >
  <input type='text' name='rouge' >
  <input type='text' name='verte' >

  <button>Upload picture</button>
</form>

</body>

</html>
