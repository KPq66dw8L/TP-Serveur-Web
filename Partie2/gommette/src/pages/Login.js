
export default function Login() {

    let msg = null;

    return (
        <div>

            <h1>Login</h1>

            <form method='post' encType='multipart/form-data'>
                <input type='text' name='username' placeholder="username" />
                <input type='text' name='password' placeholder="password"/>

                <button>Login</button>
            </form>

            <h1>{msg}</h1>
        </div>
    );
}