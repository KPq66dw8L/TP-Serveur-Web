
export default function Register() {

    return (
        <div>
            <ul>
                {/* <#list users as user>
                    <li>{user.id} - {user.firstName} {user.lastName} aka {user.username} shhhh: {user.salt} {user.hashedPassword} </li>
                </#list> */}
            </ul>

            <form method='post' encType='multipart/form-data'>
                <input type='text' name='firstname' placeholder="firstname"/>
                <input type='text' name='lastname' placeholder="lastname"/>
                <input type='text' name='username' placeholder="username"/>
                <input type='text' name='password' placeholder="password"/>
            <button>Create prof</button>
            </form>
        </div>
    );
}