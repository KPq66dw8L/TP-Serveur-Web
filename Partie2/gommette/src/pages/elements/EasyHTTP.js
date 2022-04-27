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

        // let str = "";

        let tmp = "";
        if (localStorage.getItem('user') !== null) {
            tmp = JSON.parse(localStorage.getItem('user')).token
        }
  
        // Awaiting fetch which contains 
        // method, headers and content-type
        const response = await fetch(url, {
            method: 'POST',
            headers: {
                'Content-type': 'application/json',
                'Access-Control-Allow-Origin': 'http://localhost:8081',
                'Authorization': `${tmp}`
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
        let tmp = "";
        if (localStorage.getItem('user') !== null) {
            tmp = JSON.parse(localStorage.getItem('user')).token
        }
        const response = await fetch(url, {
            method: 'PUT',
            headers: {
              'Content-type': 'application/json',
              'Access-Control-Allow-Origin': 'http://localhost:8081',
              'Authorization': `${tmp}`
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
                'Access-Control-Allow-Origin': 'http://localhost:8081',
                'Authorization': `${JSON.parse(localStorage.getItem('user')).token}`
            }
        });
  
        // Awaiting for the resource to be deleted
        const resData = response.status + ' resource deleted...';
  
        // Return response data 
        return resData;
    }
}

export default EasyHTTP;