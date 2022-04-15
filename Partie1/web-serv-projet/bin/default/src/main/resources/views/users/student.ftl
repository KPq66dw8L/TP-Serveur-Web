<#ftl encoding="utf-8">

<body xmlns="http://www.w3.org/1999/html">

    <h1>${user.id} - ${user.firstName} ${user.lastName} in ${user.group}</h1>



        <h2>${user.getNb_white()} gommettes white</h2>
        <ul><#list gommetteWhite>

            <#items as gommete1>
                <li>White. Description: ${gommete1.getGommette().getDescription()} Date: ${gommete1.getDate()}. Prof id: ${gommete1.getId_prof()} <a data-gommette-id="http://localhost:8081/gommette/${gommete1.getId()}/delete" href="#" id="delete-gommette">Delete gommette</a></li>
            </#items>

        </#list></ul>

        <h2>${user.getNb_green()} gommettes green</h2>
        <ul><#list gommetteGreen>

            <#items as gommete2>
                <li>Green. Description: ${gommete2.getGommette().getDescription()} Date: ${gommete2.getDate()}. Prof id: ${gommete2.getId_prof()} <a data-gommette-id="http://localhost:8081/gommette/${gommete2.getId()}/delete" href="#" id="delete-gommette">Delete gommette</a></li>
            </#items>

        </#list></ul>

        <h2>${user.getNb_red()} gommettes red</h2>
        <ul><#list gommetteRed>

            <#items as gommete3>
                <li>Red. Description: ${gommete3.getGommette().getDescription()}. Date: ${gommete3.getDate()}. Prof id: ${gommete3.getId_prof()} <a data-gommette-id="http://localhost:8081/gommette/${gommete3.getId()}/delete" href="#" id="delete-gommette">Delete gommette</a></li>
            </#items>

        </#list></ul>

    <script src="../app.js"></script>
</body>

</html>
