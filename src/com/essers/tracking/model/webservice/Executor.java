package com.essers.tracking.model.webservice;

import com.essers.tracking.model.processor.Processor;
import com.essers.tracking.model.processor.Processor.ProcessorException;

/**
 * Interface to define how webservices can be called on. All whats needed is the URL and {@link Processor} to process the callback.
 * @author Sam
 *
 */
public interface Executor {

	/**
	 * Executes a HTTP call to the given URL, the results will be processed by the {@link Processor}
	 * @param url
	 * @param processor
	 * @throws ProcessorException
	 */
	public void execute(String url, Processor processor) throws ProcessorException;
}
