package com.test.server.beans;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.test.packet.Packet;
import com.test.packet.Type;

/**
 * Session Bean implementation class TestService
 */
@Stateless
@LocalBean
public class TestService {

	/**
	 * Logger for TestService.
	 */
	private final Logger LOGGER = LoggerFactory.getLogger(TestService.class);

	/**
	 * Default constructor.
	 */
	public TestService() {

	}

	@EJB
	TestDAO testDAO;

	/**
	 * Handle message get from MDB and persist into db.
	 * 
	 * @param message
	 */
	public void handleMessage(Packet packet) {
		LOGGER.debug("Handle message: {}", packet.GetMessage());

		switch (packet.GetType()) {
		case SEND:
			testDAO.insertMessage(packet.GetMessage());
			LOGGER.info("Type : {} Message : {}", packet.GetType(),
					packet.GetMessage());
			break;

		case DELETE:
			testDAO.deleteMessage(packet.GetId());
			LOGGER.info("Type : {} Id : {}", packet.GetType(), packet.GetId());
			break;
		//
		case UPDATE:
			// TODO:
		default:
			LOGGER.info("Type ERROR! : {} ", packet.GetType());
		}

		/*if (packet.GetType() == TYPE.SEND) {
			testDAO.insertMessage(packet.GetMessage());
			LOGGER.info("Type : {} Message : {}", packet.GetType(),
					packet.GetMessage());
		}

		else if (packet.GetType() == TYPE.DELETE) {
			testDAO.deleteMessage(packet.GetId());
			LOGGER.info("Type : {} Id : {}", packet.GetType(), packet.GetId());
		}

		else if (packet.GetType() == TYPE.UPDATE) {

		}

		else {
			LOGGER.info("Type ERROR! : {} ", packet.GetType());
		}*/

		LOGGER.debug("Message {} persists successfully.", packet.GetMessage());
	}
}
