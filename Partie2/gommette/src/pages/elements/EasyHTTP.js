class EasyHTTP {
  
    // Make a HTTP GET Request
    async get(url) {
  
        // Awaiting fetch which contains 
        // method, headers and content-type
        const response = await fetch(url, {
            method: 'GET',
            headers: {
                'Content-type': 'application/json',
                'Access-Control-Allow-Origin': 'http://localhost:8081',
            }
        });
  
        // Awaiting for the resource to be deleted
        // const resData = "recu";
        const resData = await response.json();
  
        // Return response data 
        return resData;
    }

    // Make an HTTP PUT Request
    async post(url, data) {
  
        // Awaiting fetch which contains 
        // method, headers and content-type
        const response = await fetch(url, {
            method: 'POST',
            headers: {
              'Content-type': 'application/json',
              'Access-Control-Allow-Origin': 'http://localhost:8081',
            },
            body: JSON.stringify(data)
        });
  
        // Awaiting for the resource to be deleted
        // const resData = await response.json();
        const resData = response.status + ' ressource created...';
  
        // Return response data 
        return resData;
    }
}

export default EasyHTTP;