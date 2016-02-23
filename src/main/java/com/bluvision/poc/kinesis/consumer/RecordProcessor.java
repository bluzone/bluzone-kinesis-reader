package com.bluvision.poc.kinesis.consumer;

import com.amazonaws.services.kinesis.clientlibrary.interfaces.IRecordProcessor;
import com.amazonaws.services.kinesis.clientlibrary.interfaces.IRecordProcessorCheckpointer;
import com.amazonaws.services.kinesis.clientlibrary.types.ShutdownReason;
import com.amazonaws.services.kinesis.model.Record;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author dare
 * @since 1.0
 */
public class RecordProcessor implements IRecordProcessor {

	protected Logger log = LoggerFactory.getLogger(RecordProcessor.class);

	private String shardId;

	@Override
	public void initialize(String shardId) {
		log.info("initialize():  shardId={}", shardId);
		this.shardId = shardId;
	}

	@Override
	public void processRecords(List<Record> records, IRecordProcessorCheckpointer checkpointer) {
		log.info("processRecords():");
	}

	@Override
	public void shutdown(IRecordProcessorCheckpointer checkpointer, ShutdownReason reason) {

	}
}
