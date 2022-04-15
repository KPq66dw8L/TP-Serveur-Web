class EasyHTTP {
  
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
        const resData = await response.json();
  
        // Return response data 
        return resData;
    }
}

// Instantiating new EasyHTTP class
const http = new EasyHTTP;
  
document.body.onload = async () => {

    let buttons = null;
    let forms = null;

    // distinguish if we are on the /users page or the /users/:id page
    if(window.location.href === 'http://localhost:8081/users'){
        buttons = document.querySelectorAll('#delete-student');
        forms = document.querySelectorAll('#add-gommette');
    } else {
        buttons = document.querySelectorAll('#delete-gommette');
    }

    /*
    * Delete a resource
    */

    // for each to handle the click on every buttons generated on the page
    buttons.forEach(button => { 
        button.addEventListener('click', function handleClick(e) {
            e.preventDefault(); // to avoid problems related to Cross-Origin Resource Sharing (CORS)
    
            let link;
            // get the link to make a DELETE request to, depending on the specific gommette or student 
            if (button.getAttribute('data-student-id') != null ) {
                link = button.getAttribute('data-student-id');
            } 
            if (button.getAttribute('data-gommette-id') != null ) {
                link = button.getAttribute('data-gommette-id');
            }
    
            http.delete(link)
      
            // Resolving promise for response data
            .then(data => console.log(data))

            .then(() => { location.reload(); })
            
            // Resolving promise for error
            .catch(err => console.log(err));
        });
    });

    /*
    * Update a resource
    */

    forms.forEach(form => {
        form.addEventListener('submit', function handleSubmit(e) {
            e.preventDefault(); // the form will not be submitted yet
    
            let data = {
                gommette: e.target.elements.gommette.value,
                description: e.target.elements.description.value,
                name: e.target.elements.studentName.value,
            };

            http.put("http://localhost:8081/users", data)
      
            // Resolving promise for response data
            .then(data => console.log(data))

            .then(() => { location.reload(); })
            
            // Resolving promise for error
            .catch(err => console.log(err));
        });
    });
}