/*
 * This is the JavaScript file for 
 * the blog portion of the website
 */

/*
 * Gets comments to display
 */
function getComments() {
	fetch('/data?max-num=' + (document.getElementById("numComments").value))
        .then(response => response.json())
        .then((comments) => {
            const commentsElement = document.getElementById("comments");
            console.log('Comments: ', comments);
            commentsElement.innerHTML += 'Here are all the comments: <br>';
            for (const comment of comments) {
                commentsElement.innerHTML += comment + '<br>';
            }
	});
}

function return_home() {
   location.replace("/index.html");
   console.log(`Return Home`);
}
