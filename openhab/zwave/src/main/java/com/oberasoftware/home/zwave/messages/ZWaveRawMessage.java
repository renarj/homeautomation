/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package com.oberasoftware.home.zwave.messages;

import com.oberasoftware.home.zwave.messages.ControllerMessageType;
import com.oberasoftware.home.zwave.messages.ControllerMessageUtil;
import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicLong;

/**
 * This class represents a message which is used in serial API 
 * interface to communicate with usb Z-Wave stick
 * 
 * A ZWave serial message frame is made up as follows
 * Byte 0 : SOF (Start of Frame) 0x01
 * Byte 1 : Length of frame - number of bytes to follow
 * Byte 2 : Request (0x00) or Response (0x01)
 * Byte 3 : Message Class (see SerialMessageClass)
 * Byte 4+: Message Class data                             >> Message Payload
 * Byte x : Last byte is checksum
 * 
 * @author Victor Belov
 * @author Brian Crosby
 * @author Chris Jackson
 * @since 1.3.0
 */
public class ZWaveRawMessage implements ZWaveMessage {

	private static final Logger logger = LoggerFactory.getLogger(ZWaveRawMessage.class);
	private final static AtomicLong sequence = new AtomicLong();

	private long sequenceNumber;
	private byte[] messagePayload;
	private int messageLength = 0;
	private MessageType messageType;
	private ControllerMessageType controllerMessageType;
//	private SerialMessageClass expectedReply;

	private int messageNode = 255;
	
	private int transmitOptions = 0;
	private int callbackId = 0;
	
	private boolean transActionCanceled = false;

	/**
	 * Indicates whether the serial message is valid.
	 */
	public boolean isValid = false;
	
	/**
	 * Indicates the number of retry attempts left
	 */
	public int attempts = 3;

	/**
	 * Constructor. Creates a new instance of the SerialMessage class.
	 */
	public ZWaveRawMessage() {
		logger.trace("Creating empty message");
		messagePayload = new byte[] {};
	}
	
	/**
	 * Constructor. Creates a new instance of the SerialMessage class using the 
	 * specified message class and message type. An expected reply can be given
	 * to indicate that a transaction is complete. The priority indicates the
	 * priority to send the message with. Higher priority messages are taken from
	 * the send queue earlier than lower priority messages.
	 * @param messageClass the message class to use
	 * @param messageType the message type to use
	 */
	public ZWaveRawMessage(ControllerMessageType messageClass, MessageType messageType) {
		this(255, messageClass, messageType);
	}
	
	/**
	 * Constructor. Creates a new instance of the SerialMessage class using the 
	 * specified message class and message type. An expected reply can be given
	 * to indicate that a transaction is complete. The priority indicates the
	 * priority to send the message with. Higher priority messages are taken from
	 * the send queue earlier than lower priority messages.
	 * @param nodeId the node the message is destined for
	 * @param controllerMessageType the message class to use
	 * @param messageType the message type to use
	 */
	public ZWaveRawMessage(int nodeId, ControllerMessageType controllerMessageType, MessageType messageType) {
		logger.debug(String.format("NODE %d: Creating empty message of class = %s (0x%02X), type = %s (0x%02X)",
				new Object[] { nodeId, controllerMessageType, controllerMessageType.getKey(), messageType, messageType.ordinal()}));
		this.sequenceNumber = sequence.getAndIncrement();
		this.controllerMessageType = controllerMessageType;
		this.messageType = messageType;
		this.messagePayload = new byte[] {};
		this.messageNode = nodeId;
	}

	/**
	 * Constructor. Creates a new instance of the SerialMessage class from a
	 * specified buffer.
	 * @param buffer the buffer to create the SerialMessage from.
	 */
	public ZWaveRawMessage(byte[] buffer) {
		this(255, buffer);
	}
	
	/**
	 * Constructor. Creates a new instance of the SerialMessage class from a
	 * specified buffer, and subsequently sets the node ID.
	 * @param nodeId the node the message is destined for
	 * @param buffer the buffer to create the SerialMessage from.
	 */
	public ZWaveRawMessage(int nodeId, byte[] buffer) {
		logger.trace("NODE {}: Creating new SerialMessage from buffer = {}", nodeId, ZWaveRawMessage.bb2hex(buffer));
		messageLength = buffer.length - 2; // buffer[1];
		byte messageCheckSumm = calculateChecksum(buffer);
		byte messageCheckSummReceived = buffer[messageLength+1];
		logger.trace(String.format("NODE %d: Message checksum calculated = 0x%02X, received = 0x%02X", nodeId, messageCheckSumm, messageCheckSummReceived));
		if (messageCheckSumm == messageCheckSummReceived) {
			logger.trace("NODE {}: Checksum matched", nodeId);
			isValid = true;
		} else {
			logger.trace("NODE {}: Checksum error", nodeId);
			isValid = false;
			return;
		}
		this.messageType = buffer[2] == 0x00 ? MessageType.Request : MessageType.Response;
		this.controllerMessageType = ControllerMessageUtil.getMessageClass(buffer[3] & 0xFF);
		this.messagePayload = ArrayUtils.subarray(buffer, 4, messageLength + 1);
		this.messageNode = nodeId;
		logger.trace("NODE {}: Message payload = {}", getNodeId(), ZWaveRawMessage.bb2hex(messagePayload));
	}

    /**
     * Converts a byte array to a hexadecimal string representation    
     * @param bb the byte array to convert
     * @return string the string representation
     */
    static public String bb2hex(byte[] bb) {
		String result = "";
		for (int i=0; i<bb.length; i++) {
			result = result + String.format("%02X ", bb[i]);
		}
		return result;
	}
	
	/**
	 * Calculates a checksum for the specified buffer.
	 * @param buffer the buffer to calculate.
	 * @return the checksum value.
	 */
	private static byte calculateChecksum(byte[] buffer) {
		byte checkSum = (byte)0xFF;
		for (int i=1; i<buffer.length-1; i++) {
			checkSum = (byte) (checkSum ^ buffer[i]);
		}
		logger.trace(String.format("Calculated checksum = 0x%02X", checkSum));
		return checkSum;
	}

//	/**
//	 * Returns a string representation of this SerialMessage object.
//	 * The string contains message class, message type and buffer contents.
//	 * {@inheritDoc}
//	 */
//	@Override
//	public String toString() {
//		return String.format("Message: class = %s (0x%02X), type = %s (0x%02X), payload = %s",
//				new Object[] { controllerMessageType, controllerMessageType.getKey(), messageType, messageType.ordinal(),
//				ZWaveRawMessage.bb2hex(this.getMessagePayload()) });
//	};


	@Override
	public String toString() {
		return "ZWaveRawMessage{" +
				"messagePayload=" + bb2hex(this.getMessagePayload()) +
				", messageType=" + messageType +
				", controllerMessageType=" + controllerMessageType +
				", messageNode=" + messageNode +
				'}';
	}

	/**
	 * Gets the SerialMessage as a byte array.
	 * @return the message
	 */
	public byte[] getMessageBuffer() {
		ByteArrayOutputStream resultByteBuffer = new ByteArrayOutputStream();
		byte[] result;
		resultByteBuffer.write((byte)0x01);
		int messageLength = messagePayload.length + 
				(this.controllerMessageType == ControllerMessageType.SendData &&
				this.messageType == MessageType.Request ? 5 : 3); // calculate and set length
		
		resultByteBuffer.write((byte) messageLength);
		resultByteBuffer.write((byte) messageType.ordinal());
		resultByteBuffer.write((byte) controllerMessageType.getKey());
		
		try {
			resultByteBuffer.write(messagePayload);
		} catch (IOException e) {
			
		}

		// callback ID and transmit options for a Send Data message.
		if (this.controllerMessageType == ControllerMessageType.SendData && this.messageType == MessageType.Request) {
			resultByteBuffer.write(transmitOptions);
			resultByteBuffer.write(callbackId);
		}
		
		resultByteBuffer.write((byte) 0x00);
		result = resultByteBuffer.toByteArray();
		result[result.length - 1] = 0x01;
		result[result.length - 1] = calculateChecksum(result);
		logger.debug("Assembled message buffer = " + ZWaveRawMessage.bb2hex(result));
		return result;
	}
	
//	/**
//	 * Gets the message type (Request / Response).
//	 * @return the message type
//	 */
//	public SerialMessageType getMessageType() {
//		return messageType;
//	}

	/**
	 * Gets the message class. This is the function it represents.
	 * @return
	 */
	public ControllerMessageType getControllerMessageType() {
		return controllerMessageType;
	}

	/**
	 * Returns the Node Id for / from this message.
	 * @return the messageNode
	 */
	public int getNodeId() {
		return messageNode;
	}

	/**
	 * Gets the message payload.
	 * @return the message payload
	 */
	public byte[] getMessagePayload() {
		return messagePayload;
	}
	
	/**
	 * Gets a byte of the message payload at the specified index.
	 * The byte is returned as an integer between 0x00 (0) and 0xFF (255).
	 * @param index the index of the byte to return.
	 * @return an integer between 0x00 (0) and 0xFF (255).
	 */
	public int getMessagePayloadByte(int index) {
		return messagePayload[index] & 0xFF;
	}
	
	/**
	 * Sets the message payload.
	 * @param messagePayload
	 */
	public void setMessagePayload(byte[] messagePayload) {
		this.messagePayload = messagePayload;
	}

	/**
	 * Gets the transmit options for this SendData Request.
	 * @return the transmitOptions
	 */
	public int getTransmitOptions() {
		return transmitOptions;
	}

	/**
	 * Sets the transmit options for this SendData Request.
	 * @param transmitOptions the transmitOptions to set
	 */
	public void setTransmitOptions(int transmitOptions) {
		this.transmitOptions = transmitOptions;
	}

	/**
	 * Gets the callback ID for this SendData Request.
	 * @return the callbackId
	 */
	public int getCallbackId() {
		return callbackId;
	}

	/**
	 * Sets the callback ID for this SendData Request
	 * @param callbackId the callbackId to set
	 */
	public void setCallbackId(int callbackId) {
		this.callbackId = callbackId;
	}

	public MessageType getMessageType() {
		return messageType;
	}

	//	/**
//	 * Gets the expected reply for this message.
//	 * @return the expectedReply
//	 */
//	public SerialMessageClass getExpectedReply() {
//		return expectedReply;
//	}

//	/**
//	 * Indicates that the transaction for the incoming message is canceled by a command class
//	 * @return the transActionCanceled
//	 */
//	public boolean isTransActionCanceled() {
//		return transActionCanceled;
//	}
//
//	/**
//	 * Sets the transaction for the incoming message to canceled.
//	 * @param transActionCanceled the transActionCanceled to set
//	 */
//	public void setTransActionCanceled(boolean transActionCanceled) {
//		this.transActionCanceled = transActionCanceled;
//	}
}
