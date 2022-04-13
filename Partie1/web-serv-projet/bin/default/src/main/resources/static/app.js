class DeleteHTTP {
  
    // Make an HTTP PUT Request
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
}

// Instantiating new EasyHTTP class
const http = new DeleteHTTP;
  
// Update Post
document.body.onload = async () => {

    let button = document.getElementById('delete-student');

    http.delete('https://users/:id/delete')
  
    // Resolving promise for response data
    .then(data => console.log(data))
    
    // Resolving promise for error
    .catch(err => console.log(err));
}