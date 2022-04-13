class DeleteHTTP {
  
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
}

// Instantiating new DeleteHTTP class
const http = new DeleteHTTP;
  
// Update Post
document.body.onload = async () => {

    const buttons = document.querySelectorAll('#delete-student');

    buttons.forEach(button => { 
        button.addEventListener('click', function handleClick(e) {
            e.preventDefault(); // to avoid problems related to Cross-Origin Resource Sharing (CORS)
    
            console.log('delete student button clicked');
    
            let link = button.getAttribute('data-student-id');
    
            http.delete(link)
      
            // Resolving promise for response data
            .then(data => console.log(data))

            .then(() => { location.reload(); })
            
            // Resolving promise for error
            .catch(err => console.log(err));
        });
    });
}