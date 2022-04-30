<#ftl encoding="utf-8">
<link rel="stylesheet" href="style.css">

<body xmlns="http://www.w3.org/1999/html">

    <ul class="navBar">
        <li><a href="http://localhost:8081/login">login</a></li>
        <li><a href="http://localhost:8081/register">register & profs</a></li>
        <li><a href="http://localhost:8081/users">students</a></li>
    </ul>



    <h1>${user.id} - ${user.firstName} ${user.lastName} in ${user.group}</h1>



        <h2>${user.getNb_white()} gommettes white</h2>
        <ul><#list gommetteWhite>

            <div>
                <#items as gommete1>
                    <li>White. Description: ${gommete1.getGommette().getDescription()} Date: ${gommete1.getDate()}. Prof id: ${gommete1.getId_prof()} <#if logged == true><a data-gommette-id="http://localhost:8081/gommette/${gommete1.getId()}/delete" href="#" id="delete-gommette">Delete gommette</a><button id="modify-gommette">Modify gommette</button></#if></li>


                    <form style="display: none;" id="modify-form">
                        <select name="gommette" id="gommette">
                            <option value="white">White</option>
                            <#if gommete1.getGommette().getColour() == "green">
                                <option selected="selected" value="green">Green</option>
                            <#else>
                                <option value="green">Green</option>
                            </#if>

                            <#if gommete1.getGommette().getColour() == "red">
                                <option selected="selected" value="red">Red</option>
                            <#else>
                                <option value="red">Red</option>
                            </#if>
                        </select>
                        <input required type="text" name="description" value="${gommete1.getGommette().getDescription()}">
                        <input type="hidden" value='${gommete1.getGommette().getId()}' name="gommetteId">
                        <input type="hidden" value='${user.id}' name="studentId">
                        <button type="submit" name="send-modify">Modify gommette</button>
                    </form>


                        </#items>
            </div>

        </#list></ul>

        <h2>${user.getNb_green()} gommettes green</h2>
        <ul><#list gommetteGreen>

            <div>
                <#items as gommete2>
                    <li>Green. Description: ${gommete2.getGommette().getDescription()} Date: ${gommete2.getDate()}. Prof id: ${gommete2.getId_prof()} <#if logged == true><a data-gommette-id="http://localhost:8081/gommette/${gommete2.getId()}/delete" href="#" id="delete-gommette">Delete gommette</a><button id="modify-gommette">Modify gommette</button></#if></li>

                        <form style="display: none;" id="modify-form">
                            <select name="gommette" >
                                <option value="white">White</option>
                                <#if gommete2.getGommette().getColour() == "green">
                                    <option selected="selected" value="green">Green</option>
                                <#else>
                                    <option value="green">Green</option>
                                </#if>

                                <#if gommete2.getGommette().getColour() == "red">
                                    <option selected="selected" value="red">Red</option>
                                <#else>
                                    <option value="red">Red</option>
                                </#if>
                            </select>
                            <input required type="text" name="description" value="${gommete2.getGommette().getDescription()}">
                            <input type="hidden" value='${gommete2.getGommette().getId()}' name="gommetteId">
                            <input type="hidden" value='${user.id}' name="studentId">
                            <button type="submit" name="send-modify">Modify gommette</button>
                        </form>

                </#items>
            </div>

        </#list></ul>

        <h2>${user.getNb_red()} gommettes red</h2>
        <ul><#list gommetteRed>

            <div>
                <#items as gommete3>
                    <li>Red. Description: ${gommete3.getGommette().getDescription()}. Date: ${gommete3.getDate()}. Prof id: ${gommete3.getId_prof()} <#if logged == true><a data-gommette-id="http://localhost:8081/gommette/${gommete3.getId()}/delete" href="#" id="delete-gommette">Delete gommette</a><button id="modify-gommette">Modify gommette</button></#if></li>
                    <form style="display: none;" id="modify-form">
                            <select name="gommette" >
                                <option value="white">White</option>
                                <#if gommete3.getGommette().getColour() == "green">
                                    <option selected="selected" value="green">Green</option>
                                <#else>
                                    <option value="green">Green</option>
                                </#if>

                                <#if gommete3.getGommette().getColour() == "red">
                                    <option selected="selected" value="red">Red</option>
                                <#else>
                                    <option value="red">Red</option>
                                </#if>
                            </select>
                            <input required type="text" name="description" value="${gommete3.getGommette().getDescription()}">
                            <input type="hidden" value='${gommete3.getGommette().getId()}' name="gommetteId">
                            <input type="hidden" value='${user.id}' name="studentId">
                            <button type="submit" name="send-modify">Modify gommette</button>
                        </form>
                </#items>
            </div>

        </#list></ul>

    <script src="../app.js"></script>
</body>

</html>
