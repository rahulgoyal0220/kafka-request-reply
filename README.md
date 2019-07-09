# kafka-request-reply
Spring Kafka RequestReply template with elasticsearch

1. Setup and Run Kafka Confluent : https://docs.confluent.io/3.1.1/installation.html
2. Setup and Run elasticDB : https://www.elastic.co/guide/en/elasticsearch/reference/current/install-elasticsearch.html
3. Create 2 topics: request-user and reply-user
4. Clone the git and run the Consumer and Producer application using :  
    mvn spring-boot:run
    
5. Create some data into the elasticDB : (This is a normal HTTP call, not using kafkaRequest Reply)
 POST   http://localhost:3002/api/user 
    {
	"userId": 1,
    "companyId": 632321,
    "email": "rodeo11@gmail.com",
    "companyName": "Intersect",
    "designation": "TA"
}


6. To test the Kafka Request Reply pattern (Get data based on the "userId"):
  POST http://localhost:3000/api/user
  {
      	"userId": 1
  }
