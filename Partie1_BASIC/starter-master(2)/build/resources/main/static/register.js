import EasyHTTP from "./EasyHTTP.js";

// Instantiating new EasyHTTP class
const http = new EasyHTTP;

let buttons = document.querySelectorAll('#delete-prof');

buttons.forEach(button => {
    button.addEventListener('click', async (e) => {
        e.preventDefault();
    
        let link = button.getAttribute('data-prof-id');
    
        http.delete(link)
            .then(() => { location.reload(); })
            .catch(err => console.log(err));
        
    });
});