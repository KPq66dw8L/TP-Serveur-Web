<#ftl encoding="utf-8">

<body xmlns="http://www.w3.org/1999/html">

<ul>
    <#list users as user>

        <li>${user.id} - <a href="http://localhost:8081/users/${user.id}">${user.firstName} ${user.lastName}</a> in ${user.group}, Gommettes: ${user.getNb_white()}, ${user.getNb_green()}, ${user.getNb_red()} </li>

        <#if logged == true>

            <a data-student-id="http://localhost:8081/users/${user.id}/delete" href="#" id="delete-student">Delete student</a>

            <form method='post' enctype='multipart/form-data' id="add-gommette">

                <select name="gommette" id="gommette">
                    <option value="white">White</option>
                    <option value="green">Green</option>
                    <option value="red">Red</option>
                </select>
                <input required type="text" name="description">
                <input type="hidden" value='${user.id}' name="studentId">
                <button type="submit" name="add-gommette">Add gommette</button>
            </form>
        </#if>

    </#list>
</ul>

        <#if logged == true>
            <form method='post' enctype='multipart/form-data'>
              <input type='text' name='firstname' >
              <input type='text' name='lastname' >
              <input type='text' name='group' >

              <button type="submit" name="add-student">Add student</button>
            </form>
        </#if>

<script src="./app.js"></script>

</body>

</html>
