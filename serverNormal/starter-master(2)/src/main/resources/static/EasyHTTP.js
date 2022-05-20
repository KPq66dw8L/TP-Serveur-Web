export default class EasyHTTP {

    // Make a HTTP GET Request
    async get(url) {
  
        // Awaiting fetch which contains 
        // method, headers and content-type
        const response = await fetch(url, {
            method: 'GET',
            headers: {
                'Content-type': 'application/json',
            }
        });
  
        // Awaiting for the resource to be deleted
        // const resData = "recu";
        const resData = await response;
  
        // Return response data 
        return resData;
    }

    // Make an HTTP POST Request
    async post(url, data) {
  
        // Awaiting fetch which contains 
        // method, headers and content-type
        const response = await fetch(url, {
            method: 'POST',
            headers: {
                'Content-type': 'application/json'
            },
            body: JSON.stringify(data)
        });
  
        // Awaiting for the resource to be deleted
        // const resData = await response.json();
        const resData = await response;
  
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
                'Content-type': 'application/json'
            }
        });
  
        // Awaiting for the resource to be deleted
        const resData = 'resource deleted...';
  
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
              'Content-type': 'application/json'
            },
            body: JSON.stringify(data)
        });
  
        // Awaiting for the resource to be deleted
        // const resData = await response.json();
        const resData = 'resource updated...';
  
        // Return response data 
        return resData;
    }
}