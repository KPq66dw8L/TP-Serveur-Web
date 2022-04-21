class EasyHTTP {
  
    // Make a HTTP GET Request
    async get(url) {
  
        // Awaiting fetch which contains 
        // method, headers and content-type
        const response = await fetch(url, {
            method: 'GET',
            headers: {
                'Content-type': 'application/json',
                'Access-Control-Allow-Origin': 'http://localhost:8081'
            }
        });
  
        // Awaiting for the resource to be deleted
        // const resData = "recu";
        const resData = await response.json();
  
        // Return response data 
        return resData;
    }

    // Make an HTTP POST Request
    async post(url, data) {

        let str = "";

        if (localStorage.getItem('user') !== null) {
            str = `Basic ${localStorage.getItem('user')}`;
        }
  
        // Awaiting fetch which contains 
        // method, headers and content-type
        const response = await fetch(url, {
            method: 'POST',
            headers: {
                'Content-type': 'application/json',
                'Access-Control-Allow-Origin': 'http://localhost:8081',
                'Authorization': str
            },
            body: JSON.stringify(data)
        });
  
        // Awaiting for the resource to be deleted
        // const resData = await response.json();
        const resData = response;
  
        // Return response data 
        return resData;
    }

    // Make an HTTP PUT Request
    async put(url, data) {
  
        // Awaiting fetch which contains 
        // method, headers and content-type
        const response = await fetch(url, {
            method: 'PUT',
            headers: {
              'Content-type': 'application/json',
              'Access-Control-Allow-Origin': 'http://localhost:8081',
              'Authorization': `Basic ${localStorage.getItem('user')}`
            },
            body: JSON.stringify(data)
        });
  
        // Awaiting for the resource to be deleted
        // const resData = await response.json();
        const resData = response.status + ' resource updated...';
  
        // Return response data 
        return resData;
    }

    // Make an HTTP DELETE Request
    async delete(url) {
  
        // Awaiting fetch which contains 
        // method, headers and content-type
        const response = await fetch(url, {
            method: 'DELETE',
            headers: {
                'Content-type': 'application/json',
                'Access-Control-Allow-Origin': 'http://localhost:8081'
            }
        });
  
        // Awaiting for the resource to be deleted
        const resData = response.status + ' resource deleted...';
  
        // Return response data 
        return resData;
    }

    

    // private method
    async #authHeader(url, auth) {
        
        // return auth header with jwt if user is logged in and request is to the api url
        const token = auth?.token;
        const isLoggedIn = !!token; // The double exclamation point, or double bang, converts a truthy or falsy value to “true” or “false”
        // const isApiUrl = url.startsWith(process.env.REACT_APP_API_URL);
        const isApiUrl = url.startsWith("http://localhost:8081");
        if (isLoggedIn && isApiUrl) {
            return { Authorization: `Basic ${token}` };
        } else {
            return {};
        }
    }

    async handleResponse(response, auth, setAuth) {
        return response.text().then(text => {
            const data = text && JSON.parse(text);
            
            if (!response.ok) {
                if ([401, 403].includes(response.status) && auth?.token) {
                    // auto logout if 401 Unauthorized or 403 Forbidden response returned from api
                    localStorage.removeItem('user');
                    setAuth(null);
                    // history.push('/login');
                }
    
                const error = (data && data.message) || response.statusText;
                return Promise.reject(error);
            }
    
            return data;
        });
    }    
}

export default EasyHTTP;