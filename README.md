# Kafka Connect Demo

This repo provides a basic demo to show how you can define your own Kafka connectors to run of a Kafka connect cluster. This demo creates a connector to hit an HTTP endpoint with all messages published to a topic.

### Repo structure

The repo is split into the 3 main parts:

- The root directory contains a bunch of config and helper scripts to get all the Kafka stuff running. I highly encourage you to check out the configs/scripts to properly understand what is going on when you run them.
- The `./http-sink` contains a Java Maven project for our custom HTTP Sink connector.
- The `./http-receiver` contains a super tiny Golang project that spins up 3 web servers on 3 different ports that will log the request they receive on `POST /notification`. This will be used as the receiver of HTTP calls from our custom connector.

### Pre-requisites

This demo relies on having the following things setup/installed:

- Kafka
  - Get a local copy of Kafka and save the location of the `bin` folder in the environment variables `KAFKA_BIN` to run the scripts in this repo
- Golang
- Maven
- Java

### Set up a local Kafka

1. Run `./start-zookeeper.sh` and `./start-kafka.sh` in separate terminals to start a local Kafka instance
2. Run `./create-my-topic.sh` to create the topic `my-topic`
3. Run `./start-console-producer.sh` and `./start-console-consumer.sh` in separate terminals
4. Begin writing messages in the console producer terminal and you should see them appear in the console consumer terminal

If all the above steps worked, you now have succesfully setup a local Kafka instance.

### Run the Kafka Connect demo

1. `cd` into the `./http-sink` folder and run `mvn deploy`. This will build the JAR file for our custom Kafka Connector
2. In a separate terminal, `cd` into the `./http-receiver` folder and run `go run main.go`. This runs the HTTP server that will receive calls from Kafka Connect
3. In another separate terminal, run `./start-kafka-connect.sh`. This runs an instance of Kafka Connect that should connect to the local Kafka cluster using the custom built connector from earlier.
4. Double check Kafka Connect is running succesfully by calling `curl localhost:8083`. This should succesfully return status 200 with some basic details about the cluster

### Create and run connectors

Now Kafka and Kafka Connect are setup, we are ready to interact with Kafka Connect to operate Connectors/Tasks:

1. Call `curl localhost:8083/connectors` and observe the response is an empty array as we don't have any connectors running yet.
2. In order to create and run our HTTP connector run the following:

```bash
curl -X "POST" "http://localhost:8083/connectors" \
     -d $'{
  "name": "http-sink-1",
  "config": {
    "connector.class": "HttpSinkConnector",
    "http.url": "http://localhost:8080/notification",
    "topics": "my-topic",
    "tasks.max": "1"
  }
}'
```

Some notable parts of this command:

- `"connector.class": "HttpSinkConnector"`: this tells Kafka Connect to use the class defined in `./http-sink/src/main/java/com/dazkins/kafka/HttpSinkConnector.java`
- `"http.url": "http://localhost:8080/notification"`: this configures our custom connector to hit one of the servers hosted earlier in the `./http-receiver`

3. Now go back to the terminal running the `./http-receiver` and if you have already written some messages to the topic you should see them being logged there.
4. Write some more message in the console consumer terminal and observe them being logged in the `./http-receiver`

### Exercise

Now that you have seen how to interact with the Kafka Connect cluster I will leave it as an exercise to create two more connector instances called `http-sink-2` and `http-sink-3` that connector to the other 2 ports on the `./http-receiver` `:8081` and `:8082` respectively
