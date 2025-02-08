// script.js

async function addFanPost() {
    const postText = document.getElementById('post-text').value.trim();
    const postImageFile = document.getElementById('post-image').files[0]; // Get the first file from the list
    const fanPostsContainer = document.getElementById('fan-posts');

    if (!postText) {
        alert("Please enter some text for your post.");
        return;
    }

    // Function to convert image file to Data URL (for local storage)
    function toDataURL(file) {
        return new Promise((resolve, reject) => {
            const reader = new FileReader();
            reader.onload = () => resolve(reader.result);
            reader.onerror = reject;
            reader.readAsDataURL(file);
        });
    }

    // Function to create and display a fan post element
    function createFanPostElement(post) {
        const postDiv = document.createElement('div');
        postDiv.classList.add('fan-post');

        const postHeader = document.createElement('div');
        postHeader.classList.add('post-header');
        const authorSpan = document.createElement('span');
        authorSpan.classList.add('post-author');
        authorSpan.textContent = "Rockstar Fan"; // Replace with actual username if user accounts are implemented
        const dateSpan = document.createElement('span');
        dateSpan.classList.add('post-date');
        dateSpan.textContent = new Date(post.date).toLocaleDateString() + ' ' + new Date(post.date).toLocaleTimeString();
        postHeader.appendChild(authorSpan);
        postHeader.appendChild(dateSpan);
        postDiv.appendChild(postHeader);

        const postContent = document.createElement('div');
        postContent.classList.add('post-content');
        const textParagraph = document.createElement('p');
        textParagraph.textContent = post.text;
        postContent.appendChild(textParagraph);
        postDiv.appendChild(postContent);

        if (post.image) { // If there's image data in the post
            const imageContainer = document.createElement('div');
            imageContainer.classList.add('post-image-container');
            const imageElement = document.createElement('img');
            imageElement.src = post.image; // Use the data URL directly
            imageContainer.appendChild(imageElement);
            postDiv.appendChild(imageContainer);
        }
        return postDiv;
    }

    // Function to load posts from local storage on page load
    function loadFanPostsFromStorage() {
        const savedPostsJSON = localStorage.getItem('fanPosts');
        if (savedPostsJSON) {
            const savedPosts = JSON.parse(savedPostsJSON);
            savedPosts.forEach(post => {
                const postElement = createFanPostElement(post);
                fanPostsContainer.appendChild(postElement); // Append to display in correct order
            });
        }
    }

    let postImageDataURL = null;
    if (postImageFile) {
        postImageDataURL = await toDataURL(postImageFile); // Convert image to data URL
    }

    const newPost = {
        text: postText,
        image: postImageDataURL, // Store data URL, can be null if no image
        date: new Date().toISOString() // Store date as ISO string
    };

    // Get existing posts from local storage
    let savedPosts = JSON.parse(localStorage.getItem('fanPosts')) || [];

    savedPosts.unshift(newPost); // Add new post to the beginning of the array

    // Limit number of saved posts (optional, for demo purposes)
    if (savedPosts.length > 5) {
        savedPosts = savedPosts.slice(0, 5); // Keep only latest 5
    }

    localStorage.setItem('fanPosts', JSON.stringify(savedPosts)); // Save back to local storage

    // Create and prepend the post element to the UI
    const postElement = createFanPostElement(newPost);
    fanPostsContainer.prepend(postElement);

    // Clear the form
    document.getElementById('post-text').value = '';
    document.getElementById('post-image').value = ''; // Reset file input
}

// Load saved posts when the script runs (page loads)
loadFanPostsFromStorage();

// Add event listener for the "Post to Fan Wall" button (assuming you have this button in your HTML)
document.getElementById('post-button').addEventListener('click', addFanPost);

localStorage.setItem('testKey', 'testValue');
console.log("Test value from local storage: ", localStorage.getItem('testKey'));