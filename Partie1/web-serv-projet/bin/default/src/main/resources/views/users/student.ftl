<#ftl encoding="utf-8">

<body xmlns="http://www.w3.org/1999/html">

    <h1>${user.id} - ${user.firstName} ${user.lastName} in ${user.group}</h1>

    <h2>${user.getNb_white()} gommettes white</h2>
    <ul><#list users as user>

        <li>${gommete.colour}. Description: ${gommete.description}</li>


    </#list></ul>

    <h2>${user.getNb_green()} gommettes green</h2>
    <ul><#list users as user>

    <li>${gommete.colour}. Description: ${gommete.description}</li>


    </#list></ul>

    <h2>${user.getNb_red()} gommettes red</h2>
    <ul><#list users as user>

    <li>${gommete.colour}. Description: ${gommete.description}</li>


    </#list></ul>

</body>

</html>
