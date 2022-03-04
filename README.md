# Job Search Problem

A student can log in to the student portal and 
search for a list of jobs posted. A student can 
search for a job with a set of filters like 
type, skill, company name, experience, etc. 
The jobs get listed based on the search criteria 
given by the student. 

#Input Request
The input request for the API is as below.
{
"studentId":"1234",
"filter":{
"experience":"3-5",
"type":"permanent",
"skill":"java,mysql",
"companyName":"ABC",
"location":"any"
}
}

#Response
And the response should have the below format:-

{
"jobsList":[
{
"jobId":"JOB123",  
"company":"ABC",
"location":"Bangalore",
"experience":"3-5",
"description":"job description for the job",
"type":"permanent",
"contact":"9999999999"

},
{
"jobId":"JOB456",  
"company":"XYZ",
"location":"Hyderabad",
"experience":"3-5",
"description":"job description for the job",
"type":"permanent",
"contact":"9999999999"
}
]
}
