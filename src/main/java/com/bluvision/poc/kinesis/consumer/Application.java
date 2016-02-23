package com.bluvision.poc.kinesis.consumer;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.kinesis.AmazonKinesisClient;
import com.amazonaws.services.kinesis.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

/**
 * Spring-boot application context entry point.
 *
 * @author dare (robert.dare@gmail.com)
 * @since 1.0
 */
@SpringBootApplication
public class Application implements CommandLineRunner {

	private Logger log = LoggerFactory.getLogger(Application.class);

	@Value("${aws.access.key.id}")
	protected String awsAccessKey;

	@Value("${aws.secret.key}")
	protected String awsSecretKey;

	@Value("${aws.kinesis.endpoint}")
	protected String kinesisEndpoint;

	@Value("${system.config.streamName}")
	protected String streamName;

	@Autowired
	AmazonKinesisClient amazonKinesisClient;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	public void run(String... args) throws Exception {
		log.info("Starting...");
		DescribeStreamResult describeStreamResult = amazonKinesisClient.describeStream(streamName);
		StreamDescription description = describeStreamResult.getStreamDescription();
		// Use the first shard:
		Shard shard = description.getShards().stream().findFirst().get();
		log.info("-- StreamName:  {}", streamName);
		log.info("-- Endpoint:  {}", kinesisEndpoint);

		GetShardIteratorRequest getShardIteratorRequest = new GetShardIteratorRequest();
		getShardIteratorRequest.setStreamName(streamName);
		getShardIteratorRequest.setShardId(shard.getShardId());
		getShardIteratorRequest.setShardIteratorType("LATEST");
		GetShardIteratorResult getShardIteratorResult = amazonKinesisClient.getShardIterator(getShardIteratorRequest);
		String shardIterator = getShardIteratorResult.getShardIterator();
		log.debug("-- Using ShardIterator:  {}", shardIterator);

		GetRecordsRequest request = new GetRecordsRequest();
		request.setShardIterator(shardIterator);

		for (;;) {
			request.setShardIterator(shardIterator);
			GetRecordsResult result = amazonKinesisClient.getRecords(request);
			List<Record> records = result.getRecords();
			for (Record r : records) {
				String data = new String(r.getData().array());
				log.debug("R: {}", data);
			}
			// records.stream().forEach(r -> log.debug(r.getData().toString()));
			shardIterator = result.getNextShardIterator();
		}
	}

	@Bean
	public AmazonKinesisClient amazonKinesisClient() {
		AmazonKinesisClient client = new AmazonKinesisClient(
				new BasicAWSCredentials(awsAccessKey, awsSecretKey));
		client.setEndpoint(kinesisEndpoint);
		return client;
	}


}
