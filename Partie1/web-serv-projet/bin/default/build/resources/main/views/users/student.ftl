<#ftl encoding="utf-8">

<body xmlns="http://www.w3.org/1999/html">

    <h1>${user.id} - ${user.firstName} ${user.lastName} in ${user.group}</h1>



        <h2>${user.getNb_white()} gommettes white</h2>
        <ul><#list gommetteWhite>

            <#items as gommete1>
                <li>White. Description: ${gommete1.getGommette().getDescription()}</li>
            </#items>

        </#list></ul>

        <h2>${user.getNb_green()} gommettes green</h2>
        <ul><#list gommetteGreen>

            <#items as gommete2>
                <li>Green. Description: ${gommete2.getGommette().getDescription()}</li>
            </#items>

        </#list></ul>

        <h2>${user.getNb_red()} gommettes red</h2>
        <ul><#list gommetteRed>

            <#items as gommete3>
                <li>Red. Description: ${gommete3.getGommette().getDescription()}. Date: ${gommete3.getDate()}. Prof id: ${gommete3.getId_prof()}</li>
            </#items>

        </#list></ul>



</body>

</html>
