## Blog-Application App


### Description:

The Simple Blog Post Application is a web-based application that allows users to create and publish blog posts. Users can create an account, log in, create new posts, view existing posts, and interact with other users by leaving comments on their posts.

## Installation

- [Installation] #intelliJ, #eclipse, #pgAdmin
- [Usage] #read/write blogs
- [Features] #blogs , #blogs in sorted order by date, filter with different cases, searching

1. Clone the repository: [Blog-App](#)

2. Install the dependencies:


3. Set up the database:

- Create a new PostgreSQL database.
- Update the database configuration in `application.properties` file with your database credentials.

4. Run the migrations to create the required tables:


5. Start the application:

6. Access the application in your web browser at `http://localhost:8080`.

## Features

- User authentication: Users can create an account and log in to the application.
- Create new blog posts: Users can create new blog posts with a title, content, and optional image.
- View blog posts: Users can view a list of existing blog posts with their titles and authors.
- View blog posts: Users can view a list of existing blog posts with their titles and authors in sorted order by Date.
- View blog posts: Users can view a list of existing blog posts with their titles and authors by searching author name,tags,or post name.
- View blog posts: Users can view a list of existing blog posts with their titles and authors with filter by authorId, tagId and publish date Id.
- View post details: Users can view the full details of a blog post, including the author, content, and comments.
- Comment on posts: Users can leave comments on other users' blog posts.
- Edit and delete posts: Users can edit or delete their own posts.
- Edit or delete comments: user can delete and Update the comments on its own post.





