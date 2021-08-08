# graphql-scalar-poc


## Step to be followed to run and test the application locally.

- Bring up the spring boot application.
- Enter the URL as.
  - http://localhost:8080/digital/v1/sample-scalarapp/graphiql?query=query%7B%0A%20%20getStudentDataById(studentId%3A%20%22a0ec2a02-af74-4da1-a44b-d65b70a7bb82%22)%0A%7D
  - Enter the below query with StudentId as mentioned.
  
    ```
    query{
            getStudentDataById(studentId: "a0ec2a02-af74-4da1-a44b-d65b70a7bb82")
        }
    ```
  - Please note we are not telling the schema, what all values we are trying to read. 
  - Instead it will fetch all the available column data.

![N|Solid](https://github.com/catallicpankaj/graphql-scalar-poc/blob/main/graphiql-screenshot.png)

![N|Solid](https://github.com/catallicpankaj/graphql-scalar-poc/blob/main/H2-console-screenshot.jpeg)



